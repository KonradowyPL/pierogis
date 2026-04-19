package pl.konradowy.pierogis.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock.HeatLevel;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;

import pl.konradowy.pierogis.Items;

@Mixin(BasinBlockEntity.class)
public class BasinMixin {

	long lastTick = 0;
	double p = 0.001; // 0.1% per second
	// double p = 1; // 100% per second

	@Inject(method = "tick", at = @At("TAIL"))
	private void onTick(CallbackInfo ci) {
		BlockEntity be = ((BlockEntity) (Object) this);
		Level level = be.getLevel();

		// calculate probability of it happening between last call
		long ticks = level.getGameTime();
		long delta = ticks - lastTick;

		double seconds = delta / 20.0;
		double prob = 1.0 - Math.pow(1.0 - p, seconds);

		double random = Math.random();
		lastTick = ticks;
		if (random >= prob) {
			return;
		}

		BlockPos pos = ((BlockEntity) (Object) this).getBlockPos();

		// IS HEATED:
		if (BasinBlockEntity.getHeatLevelOf(level.getBlockState(pos.below(1))) == HeatLevel.NONE) {
			return;
		}

		String above = BuiltInRegistries.BLOCK.getKey(level.getBlockState(pos.above(2)).getBlock()).toString();
		if (above.equals("create:mechanical_press") || above.equals("create:mechanical_mixer")) {
			return;
		}

		CompoundTag nbt = be.saveWithFullMetadata(level.registryAccess());

		ListTag inputTanks = nbt.getList("InputTanks", Tag.TAG_COMPOUND);
		if (inputTanks.isEmpty())
			return;

		CompoundTag firstTank = inputTanks.getCompound(0);
		CompoundTag tankContent = firstTank.getCompound("TankContent");

		if (tankContent.contains("Fluid")) {
			CompoundTag fluid = tankContent.getCompound("Fluid");
			String fluidId = fluid.getString("id");
			int amount = fluid.getInt("amount");

			if (amount == 1000 && fluidId != "minecraft:water") {

				fluid.putInt("amount", 1);

				tankContent.put("Fluid", fluid);
				firstTank.put("TankContent", tankContent);
				inputTanks.set(0, firstTank);

				nbt.put("InputTanks", inputTanks);

				be.loadWithComponents(nbt, level.registryAccess());
				be.setChanged();
				level.sendBlockUpdated(pos, be.getBlockState(), be.getBlockState(), 3);

				IItemHandler handler = level.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);

				ItemStack stack = new ItemStack((ItemLike) Items.SALT, 1);

				if (handler != null) {
					for (int i = 0; i < handler.getSlots(); i++) {
						stack = handler.insertItem(i, stack, false);
						if (stack.isEmpty())
							break;
					}
				}
			}
		}
	}
}

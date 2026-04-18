package pl.konradowy.pierogis.mixins;

import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.basin.BasinInventory;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
// import pl.konradowy.pierogis.;;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.core.Direction;

@Mixin(BasinBlockEntity.class)
public class BasinMixin {
	@Shadow
	public SmartFluidTankBehaviour inputTank;
	@Shadow
	public SmartFluidTankBehaviour outputTank;

	long lastTick = 0;

	@Inject(method = "tick", at = @At("TAIL"))
	private void onTick(CallbackInfo ci) {
		BlockEntity be = ((BlockEntity) (Object) this);
		Level level = be.getLevel();
		long ticks = level.getGameTime();

		long delta = ticks - lastTick;

		lastTick = ticks;

		if (level.isClientSide)
			return;

		BlockPos pos = ((BlockEntity) (Object) this).getBlockPos();
		CompoundTag nbt = be.saveWithFullMetadata(level.registryAccess());
		// CompoundTag fluid = getFirstInputTankFluid(tag);

		// if (fluid == null)
		// return;

		// String fluidId = fluid.getString("id");
		// int amount = fluid.getInt("amount");
		// System.err.println(fluidId);
		// System.err.println(amount);
		ListTag inputTanks = nbt.getList("InputTanks", Tag.TAG_COMPOUND);
		if (inputTanks.isEmpty())
			return;

		CompoundTag firstTank = inputTanks.getCompound(0);
		CompoundTag tankContent = firstTank.getCompound("TankContent");

		if (tankContent.contains("Fluid")) {
			CompoundTag fluid = tankContent.getCompound("Fluid");
			String fluidId = fluid.getString("id");
			int amount = fluid.getInt("amount");
			System.err.println(fluidId);
			System.err.println(amount);

			amount = fluid.getInt("amount");
			if (amount == 1000) {

				fluid.putInt("amount", 1);
				System.err.println("SET!");

				tankContent.put("Fluid", fluid);
				firstTank.put("TankContent", tankContent);
				inputTanks.set(0, firstTank);

				nbt.put("InputTanks", inputTanks);

				be.loadWithComponents(nbt, level.registryAccess());
				be.setChanged();
				level.sendBlockUpdated(pos, be.getBlockState(), be.getBlockState(), 3);

				IItemHandler handler = level.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);

				ItemStack stack = new ItemStack(Items.DIAMOND, 1);

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

	// private static CompoundTag getFirstInputTankFluid(CompoundTag nbt) {

	// // InputTanks is a list
	// ListTag inputTanks = nbt.getList("InputTanks", Tag.TAG_COMPOUND);

	// if (inputTanks.isEmpty()) {
	// return null;
	// }

	// CompoundTag firstTank = inputTanks.getCompound(0);
	// CompoundTag tankContent = firstTank.getCompound("TankContent");
	// if (!tankContent.contains("Fluid")) {
	// return null;
	// }
	// return tankContent.getCompound("Fluid");
	// }
}

package pl.konradowy.pierogis.mixins;

import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.basin.BasinInventory;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;

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

@Mixin(BasinBlockEntity.class)
public class BasinMixin {
	@Shadow
	public SmartFluidTankBehaviour inputTank;
	@Shadow
	public SmartFluidTankBehaviour outputTank;

	@Inject(method = "tick", at = @At("TAIL"))
	private void onTick(CallbackInfo ci) {
		System.err.println("BASIN TICK");
		Level level = ((BlockEntity) (Object) this).getLevel();
		BlockPos pos = ((BlockEntity) (Object) this).getBlockPos();

		if (level.isClientSide)
			return;

		System.err.println(pos);
		BlockEntity blockEntity = level.getBlockEntity(pos);
		CompoundTag tag = blockEntity.saveWithFullMetadata(level.registryAccess());
		System.err.println(tag);

		// System.err.println(this.);

	}
}
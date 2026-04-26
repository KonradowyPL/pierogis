package pl.konradowy.pierogis;

import java.util.Set;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.EffectCure;

public class LugolEffect extends MobEffect {

  public LugolEffect() {
    super(MobEffectCategory.BENEFICIAL, 0x98D982); // color in hex
  }

  @Override
  public boolean applyEffectTick(LivingEntity entity, int amplifier) {
    return true;
  }

  @Override
  public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
    return true;
  }

  public Set<EffectCure> getCures() {
    return Set.of();
  }
}
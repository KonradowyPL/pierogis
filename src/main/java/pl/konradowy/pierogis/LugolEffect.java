package pl.konradowy.pierogis;

import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class LugolEffect extends MobEffect {

  public LugolEffect() {
    super(MobEffectCategory.BENEFICIAL, 0xfeec8e); // color in hex
  }

  @Override
  public boolean applyEffectTick(LivingEntity entity, int amplifier) {
    return true;
  }

  @Override
  public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
    return false;
  }

  @Override
  public void onEffectStarted(LivingEntity entity, int amplifier) {
    MinecraftServer server = entity.getServer();
    server.getPlayerList().broadcastSystemMessage(
        Component.literal(
            "Gracz " + entity.getName().getString() + " wypił płyn lugola!"),
        false);
  }
}
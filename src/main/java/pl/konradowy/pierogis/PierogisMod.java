package pl.konradowy.pierogis;

import java.util.function.Supplier;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(PierogisMod.MODID)
public class PierogisMod {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "pierogis";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister
            .create(BuiltInRegistries.SOUND_EVENT, MODID);

    public static final DeferredHolder<SoundEvent, SoundEvent> MY_SOUND = SOUND_EVENTS.register(
            "siemanko", // must match the resource location on the next line
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MODID, "siemanko")));

    public static final DeferredHolder<SoundEvent, SoundEvent> HL_SOUND = SOUND_EVENTS.register(
            "hl", // must match the resource location on the next line
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MODID, "hl")));

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, MODID);
    public static final Supplier<MobEffect> MY_EFFECT = EFFECTS.register("lugol", LugolEffect::new);

    // The constructor for the mod class is the first code that is run when your mod
    // is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and
    // pass them in automatically.
    public PierogisMod(IEventBus modEventBus, ModContainer modContainer) {
        ModGameRules.register();
        EFFECTS.register(modEventBus);
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        Items.register(modEventBus);
        SOUND_EVENTS.register(modEventBus);
        NeoForge.EVENT_BUS.register(this);

    }

    private void commonSetup(FMLCommonSetupEvent event) {
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        // LOGGER.info("HELLO from server starting");
    }

    @SubscribeEvent
    public void onServerTick(net.neoforged.neoforge.event.tick.ServerTickEvent.Post event) {
        var server = event.getServer();

        var overworld = server.overworld(); // or any ServerLevel
        long ticks = overworld.getGameTime();
        int radius = overworld.getGameRules().getInt(ModGameRules.BORDER_RADIUS);
        server.getPlayerList().getPlayers().forEach(player -> {
            Vec3 pos = player.getPosition(0);
            double distance = Math.sqrt((pos.x - 0.5) * (pos.x - 0.5) + (pos.z - 0.5) * (pos.z - 0.5));
            double beyond = distance - radius;
            if (beyond < 0.5)
                return;

            if (player.hasEffect((Holder<MobEffect>) MY_EFFECT)) {
                return;
            }

            player.removeEffect(MobEffects.REGENERATION);
            player.removeEffect(MobEffects.DAMAGE_RESISTANCE);
            player.getFoodData().setSaturation(0);
            player.addEffect(new MobEffectInstance(MobEffects.POISON, 10, 0, true, false, false));
            if (player.getFoodData().getFoodLevel() >= 18)
                player.getFoodData().setFoodLevel(17);

            if (ticks % 50 != 0)
                return;
            player.hurt(player.damageSources().fellOutOfWorld(), 1);

            if (beyond > 10) {
                player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 200, 0, true, false, false));
            }
        });
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {

        if (event.getEntity() instanceof ServerPlayer player) {
            ServerLevel level = player.serverLevel();

            int radius = level.getGameRules()
                    .getInt(ModGameRules.BORDER_RADIUS);

            PacketDistributor.sendToPlayer(player,
                    new BorderRadiusSyncPayload(radius));
        }
    }

    @SubscribeEvent
    public void onEffectAdded(MobEffectEvent.Added event) {
        MobEffectInstance instance = event.getEffectInstance();
        if (instance.getEffect().getRegisteredName().equals("pierogis:lugol")) {
            instance.getCures().clear();
        }
    }
}

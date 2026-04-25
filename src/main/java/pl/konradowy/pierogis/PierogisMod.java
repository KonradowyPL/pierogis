package pl.konradowy.pierogis;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.phys.Vec3;

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
            MODID, // must match the resource location on the next line
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MODID, "siemanko")));

    // The constructor for the mod class is the first code that is run when your mod
    // is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and
    // pass them in automatically.
    public PierogisMod(IEventBus modEventBus, ModContainer modContainer) {
        ModGameRules.register();
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
        int radius = overworld.getGameRules().getInt(ModGameRules.BORDER_RADIUS);
        System.err.println("Server radius: " + radius);
        // This runs every server tick
        server.getPlayerList().getPlayers().forEach(player -> {
            // your logic here
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

}

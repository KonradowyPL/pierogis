package pl.konradowy.pierogis;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
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
    public void onClientTick(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null)
            return;
        // if (minecraft.player.tickCount % 20 != 0)
        // return;
        if (minecraft.level == null)
            return;
        if (minecraft.isPaused())
            return;

        int radius = minecraft.level.getGameRules().getInt(ModGameRules.BORDER_RADIUS);
        System.err.println("Client radius: " + radius);

        ClientLevel level = minecraft.level;

        Vec3 cameraPos = minecraft.gameRenderer.getMainCamera().getPosition();

        // scale
        double RADIUS = 1000;
        double x = (cameraPos.x - 0.5) / RADIUS;
        double y = (cameraPos.y - 0.5) / RADIUS;
        double z = (cameraPos.z - 0.5) / RADIUS;

        boolean isOutside = (x * x + z * z) > 1;

        double r = 30 / RADIUS;

        double area = estimateArea(x, y, z, r);
        double realArea = area * RADIUS * RADIUS;

        for (int i = 0; i < realArea / 20; i++) {

            Vec3 point = samplePoint(x, y, z, r);
            if (point == null)
                return;

            point = new Vec3((point.x * RADIUS) + Math.random(), point.y * RADIUS, (point.z * RADIUS) + Math.random());

            SimpleParticleType particle = isOutside ? ParticleTypes.FALLING_NECTAR
                    : (Math.random() > 0.2 ? ParticleTypes.TRIAL_SPAWNER_DETECTED_PLAYER
                            : ParticleTypes.TRIAL_SPAWNER_DETECTED_PLAYER_OMINOUS);

            for (int j = 0; j < 5; j++) {
                level.addParticle(particle, point.x,
                        point.y,
                        point.z, 0, 0, 0);

            }

        }
    }

    public static Vec3 samplePoint(double x0, double y0, double z0, double r) {
        double rho = Math.sqrt(x0 * x0 + z0 * z0);

        // Special case: sphere center on cylinder axis
        if (rho == 0.0) {
            return null; // no intersection unless r >= 1
        }

        double phi = Math.atan2(z0, x0);

        double k = (1 + rho * rho - r * r) / (2 * rho);

        // No intersection
        if (k > 1.0) {
            return null;
        }

        double alpha = Math.acos(k);

        // Sample angle on valid arc
        double t = phi - alpha + Math.random() * (2 * alpha);

        double cx = Math.cos(t);
        double cz = Math.sin(t);

        double dx = cx - x0;
        double dz = cz - z0;

        double D = dx * dx + dz * dz;

        double h = Math.sqrt(Math.max(0.0, r * r - D));

        // Sample y instead of z
        double y = y0 - h + Math.random() * (2 * h);

        return new Vec3(cx, y, cz);
    }

    public static double estimateArea(double x0, double y0, double z0, double r) {

        // distance of sphere center projection to cylinder axis
        double rho = Math.sqrt(x0 * x0 + z0 * z0);

        // degenerate case: center exactly on axis
        if (rho == 0.0) {
            // full symmetry: sphere cuts a full ring
            double h = Math.sqrt(Math.max(0.0, r * r - 1.0));
            return 2.0 * Math.PI * (2.0 * h);
        }

        // angular half-width of intersection
        double k = (1.0 + rho * rho - r * r) / (2.0 * rho);

        // no intersection
        if (k > 1.0) {
            return 0.0;
        }

        // clamp for numerical safety
        k = Math.max(-1.0, Math.min(1.0, k));

        double alpha = Math.acos(k);

        // closest approach approximation for height
        double minDist = (1.0 - rho) * (1.0 - rho);
        double inside = r * r - minDist;

        if (inside <= 0.0) {
            return 0.0;
        }

        double h = Math.sqrt(inside);

        // final approximation: arc length * height
        double area = (2.0 * alpha) * (2.0 * h);

        return area;
    }
}

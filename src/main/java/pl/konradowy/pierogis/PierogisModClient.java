package pl.konradowy.pierogis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.minecraft.client.Minecraft;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = PierogisMod.MODID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods
// in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = PierogisMod.MODID, value = Dist.CLIENT)
public class PierogisModClient {
    public PierogisModClient(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your
        // mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json
        // file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        // Some client setup code
        PierogisMod.LOGGER.info("HELLO FROM CLIENT SETUP");
        PierogisMod.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
    }


    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null)
            return;
        // if (minecraft.player.tickCount % 20 != 0)
        // return;
        if (minecraft.level == null)
            return;

        ClientLevel level = minecraft.level;

        Vec3 cameraPos = minecraft.gameRenderer.getMainCamera().getPosition();

        
        // scale
        double RADIUS = 1000;
        double x = (cameraPos.x - 0.5) / RADIUS;
        double y = cameraPos.y;
        double z = (cameraPos.z - 0.5) / RADIUS;
        double r = 50 / RADIUS;

        double area = estimateArea(x, y, z, r);
        double realArea = area * RADIUS * RADIUS;

        for (int i = 0; i < realArea / 20; i++) {

            Vec3 point = samplePoint(x, y, z, r);
            if (point == null)
                return;

            SimpleParticleType particle = Math.random() > 0.2 ? ParticleTypes.TRIAL_SPAWNER_DETECTED_PLAYER
                    : ParticleTypes.TRIAL_SPAWNER_DETECTED_PLAYER_OMINOUS;

            for (int j = 0; j < 5; j++) {
                level.addParticle(particle, point.x * RADIUS + 0.5,
                        point.y() * RADIUS + 0.5,
                        point.z() * RADIUS + 0.5, 0, 0, 0);

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

package pl.konradowy.pierogis;

import net.minecraft.world.level.GameRules;

public class ModGameRules {

    public static GameRules.Key<GameRules.IntegerValue> BORDER_RADIUS;

    public static void register() {
        BORDER_RADIUS = GameRules.register(
                "pierogis",
                GameRules.Category.MOBS,
                GameRules.IntegerValue.create(100)
        );
    }
}
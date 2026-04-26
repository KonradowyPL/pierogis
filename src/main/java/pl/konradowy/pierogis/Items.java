package pl.konradowy.pierogis;

import java.util.function.Supplier;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class Items {
        // registers
        public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(PierogisMod.MODID);
        public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister
                        .create(Registries.CREATIVE_MODE_TAB, PierogisMod.MODID);
        public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(PierogisMod.MODID);

        // example block
        public static final DeferredBlock<Block> KAFELKI = BLOCKS.register("kafelki",
                        registryName -> new Block(BlockBehaviour.Properties.of()
                                        .destroyTime(2.0f)
                                        .explosionResistance(10.0f)
                                        .sound(SoundType.MANGROVE_ROOTS)));

        public static final DeferredItem<BlockItem> KAFELKI_ITEM = ITEMS.register("kafelki",
                        () -> new PierogisBlock(KAFELKI.get(), new Item.Properties()));

        // bloki pierogów
        // ruski
        public static final DeferredBlock<Block> RUSKI_BLOK = BLOCKS.register("ruski_crate",
                        registryName -> new Block(BlockBehaviour.Properties.of()
                                        .destroyTime(2.0f)
                                        .explosionResistance(10.0f)
                                        .sound(SoundType.MANGROVE_ROOTS)));
        public static final DeferredItem<BlockItem> RUSKI_ITEM = ITEMS.register("ruski_crate",
                        () -> new BlockItem(RUSKI_BLOK.get(), new Item.Properties()));

        // syr
        public static final DeferredBlock<Block> SYR_BLOK = BLOCKS.register("syr_crate",
                        registryName -> new Block(BlockBehaviour.Properties.of()
                                        .destroyTime(2.0f)
                                        .explosionResistance(10.0f)
                                        .sound(SoundType.MANGROVE_ROOTS)));

        public static final DeferredItem<BlockItem> SYR_ITEM = ITEMS.register("syr_crate",
                        () -> new BlockItem(SYR_BLOK.get(), new Item.Properties()));

        // jagoda
        public static final DeferredBlock<Block> JAGODA_BLOK = BLOCKS.register("jagoda_crate",
                        registryName -> new Block(BlockBehaviour.Properties.of()
                                        .destroyTime(2.0f)
                                        .explosionResistance(10.0f)
                                        .sound(SoundType.MANGROVE_ROOTS)));

        public static final DeferredItem<BlockItem> JAGODA_ITEM = ITEMS.register("jagoda_crate",
                        () -> new BlockItem(JAGODA_BLOK.get(), new Item.Properties()));

        // mienso
        public static final DeferredBlock<Block> MIENSO_BLOK = BLOCKS.register("mienso_crate",
                        registryName -> new Block(BlockBehaviour.Properties.of()
                                        .destroyTime(2.0f)
                                        .explosionResistance(10.0f)
                                        .sound(SoundType.MANGROVE_ROOTS)));

        public static final DeferredItem<BlockItem> MIENSO_ITEM = ITEMS.register("mienso_crate",
                        () -> new BlockItem(MIENSO_BLOK.get(), new Item.Properties()));

        // kapusta
        public static final DeferredBlock<Block> KAPUSTA_BLOCK = BLOCKS.register("kapusta_crate",
                        registryName -> new Block(BlockBehaviour.Properties.of()
                                        .destroyTime(2.0f)
                                        .explosionResistance(10.0f)
                                        .sound(SoundType.MANGROVE_ROOTS)));

        public static final DeferredItem<BlockItem> KAPUSTA_ITEM = ITEMS.register("kapusta_crate",
                        () -> new BlockItem(KAPUSTA_BLOCK.get(), new Item.Properties()));

        @SuppressWarnings("deprecation")
        private static final FoodProperties RAW_FOOD = new FoodProperties.Builder()
                        .nutrition(-1)
                        .saturationModifier(-1)
                        .effect(new MobEffectInstance(MobEffects.POISON, 200), 0.5f)
                        .effect(new MobEffectInstance(MobEffects.HUNGER, 1200), 0.5f)
                        .effect(new MobEffectInstance(MobEffects.DARKNESS, 50), 0.1f)
                        .effect(new MobEffectInstance(MobEffects.BLINDNESS, 400), 0.05f)
                        .build();

        public static ItemAttributeModifiers walek_modifiers = ItemAttributeModifiers.builder()
                        .add(
                                        Attributes.ATTACK_KNOCKBACK,
                                        new AttributeModifier(
                                                        ResourceLocation.fromNamespaceAndPath("minecraft",
                                                                        "attack_knockback"),
                                                        6.0,
                                                        AttributeModifier.Operation.ADD_VALUE),
                                        EquipmentSlotGroup.MAINHAND)
                        .add(
                                        Attributes.ATTACK_SPEED,
                                        new AttributeModifier(
                                                        ResourceLocation.fromNamespaceAndPath("minecraft",
                                                                        "attack_speed"),
                                                        -3.5,
                                                        AttributeModifier.Operation.ADD_VALUE),
                                        EquipmentSlotGroup.MAINHAND)
                        .build();

        public static final DeferredItem<Item> CIASTO = ITEMS.register("ciasto", () -> new Item(new Item.Properties()));
        public static final DeferredItem<Item> CIASTO_FLAT = ITEMS.register("ciasto_flat",
                        () -> new Item(new Item.Properties()));

        public static final DeferredItem<Item> JOD = ITEMS.register("jod", () -> new Item(new Item.Properties()));

        // Surowe pierogi
        public static final DeferredItem<Item> SYR_RAW = ITEMS.registerSimpleItem("syr_raw",
                        new Item.Properties().food(RAW_FOOD));
        public static final DeferredItem<Item> MIENSO_RAW = ITEMS.registerSimpleItem("mienso_raw",
                        new Item.Properties().food(RAW_FOOD));
        public static final DeferredItem<Item> KAPUSTA_RAW = ITEMS.registerSimpleItem("kapusta_raw",
                        new Item.Properties().food(RAW_FOOD));
        public static final DeferredItem<Item> RUSKI_RAW = ITEMS.registerSimpleItem("ruski_raw",
                        new Item.Properties().food(RAW_FOOD));
        public static final DeferredItem<Item> JAGODY_RAW = ITEMS.registerSimpleItem("jagoda_raw",
                        new Item.Properties().food(RAW_FOOD));

        public static final DeferredItem<Item> SALT = ITEMS.register("salt", () -> new Salt(new Item.Properties()));
        public static final DeferredItem<Item> WALEK = ITEMS.register(
                        "walek",
                        () -> new WalekItem(new Item.Properties().attributes(walek_modifiers)));

        public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(Registries.POTION,
                        PierogisMod.MODID);

        public static final Supplier<Potion> PLYN_LUGOLA = POTIONS.register("lugol",
                        () -> new Potion(new MobEffectInstance((Holder<MobEffect>) PierogisMod.MY_EFFECT,
                                        20 * 60 * 60 * 10))); // 10h

        // Gotowane pierogi
        @SuppressWarnings("deprecation")
        public static final DeferredItem<Item> SYR_COOKED = ITEMS.registerSimpleItem("syr_cooked",
                        new Item.Properties().food(new FoodProperties.Builder()
                                        .nutrition(4)
                                        .saturationModifier(2)
                                        .effect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 600), 1f)
                                        .build()));
        @SuppressWarnings("deprecation")
        public static final DeferredItem<Item> MIENSO_COOKED = ITEMS.registerSimpleItem("mienso_cooked",
                        new Item.Properties().food(new FoodProperties.Builder()
                                        .nutrition(7)
                                        .saturationModifier(6)
                                        .effect(new MobEffectInstance(MobEffects.DIG_SPEED, 500), 1f)
                                        .build()));
        @SuppressWarnings("deprecation")
        public static final DeferredItem<Item> KAPUSTA_COOKED = ITEMS.registerSimpleItem("kapusta_cooked",
                        new Item.Properties().food(new FoodProperties.Builder()
                                        .nutrition(5)
                                        .saturationModifier(7)
                                        .effect(new MobEffectInstance(MobEffects.ABSORPTION, 500), 1f)
                                        .build()));
        @SuppressWarnings("deprecation")
        public static final DeferredItem<Item> RUSKI_COOKED = ITEMS.registerSimpleItem("ruski_cooked",
                        new Item.Properties().food(new FoodProperties.Builder()
                                        .nutrition(6)
                                        .saturationModifier(3)
                                        .effect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 600), 1f)
                                        .build()));
        @SuppressWarnings("deprecation")
        public static final DeferredItem<Item> JAGODY_COOKED = ITEMS.registerSimpleItem("jagoda_cooked",
                        new Item.Properties().food(new FoodProperties.Builder()
                                        .nutrition(2)
                                        .saturationModifier(6)
                                        .effect(new MobEffectInstance(MobEffects.REGENERATION, 240), 1f)
                                        .build()));

        public static final DeferredItem<Item> DANIE = ITEMS.registerSimpleItem("danie",
                        new Item.Properties().food(new FoodProperties.Builder()
                                        .nutrition(15)
                                        .saturationModifier(15)
                                        .build()));

        // Creates a creative tab with the id "examplemod:example_tab" for the example
        // item, that is placed after the combat tab
        public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS
                        .register("pierogis", () -> CreativeModeTab.builder()
                                        .title(Component.translatable("itemGroup.pierogis")) // The language key for the
                                                                                             // title of your
                                                                                             // CreativeModeTab
                                        .withTabsBefore(CreativeModeTabs.COMBAT)
                                        .icon(() -> RUSKI_COOKED.get().getDefaultInstance())
                                        .displayItems((parameters, output) -> {

                                                output.accept(MIENSO_RAW.get());
                                                output.accept(SYR_RAW.get());
                                                output.accept(KAPUSTA_RAW.get());
                                                output.accept(JAGODY_RAW.get());
                                                output.accept(RUSKI_RAW.get());

                                                //
                                                output.accept(WALEK.get());
                                                output.accept(SALT.get());
                                                output.accept(JOD.get());
                                                output.accept(DANIE.get());

                                                output.accept(MIENSO_COOKED.get());
                                                output.accept(SYR_COOKED.get());
                                                output.accept(KAPUSTA_COOKED.get());
                                                output.accept(JAGODY_COOKED.get());
                                                output.accept(RUSKI_COOKED.get());

                                                //
                                                output.accept(KAFELKI_ITEM.get());
                                                output.accept(CIASTO.get());
                                                output.accept(CIASTO_FLAT.get());
                                                output.accept(net.minecraft.world.item.Items.POTION);

                                                // gotowe itemy
                                                output.accept(MIENSO_ITEM.get());
                                                output.accept(SYR_ITEM.get());
                                                output.accept(KAPUSTA_ITEM.get());
                                                output.accept(JAGODA_ITEM.get());
                                                output.accept(RUSKI_ITEM.get());

                                        }).build());

        public static void register(IEventBus eventBus) {
                ITEMS.register(eventBus);
                BLOCKS.register(eventBus);
                POTIONS.register(eventBus);
                CREATIVE_MODE_TABS.register(eventBus);

        }
}

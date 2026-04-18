package pl.konradowy.pierogis;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
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
  public static final DeferredBlock<Block> EXAMPLE_BLOCK = BLOCKS.registerSimpleBlock("example_block",
      BlockBehaviour.Properties.of().mapColor(MapColor.STONE));
  public static final DeferredItem<BlockItem> EXAMPLE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("example_block",
      EXAMPLE_BLOCK);

  // example item
  public static final DeferredItem<Item> EXAMPLE_ITEM = ITEMS.registerSimpleItem("example_item",
      new Item.Properties().food(new FoodProperties.Builder()
          .alwaysEdible().nutrition(1).saturationModifier(2f).build()));

  public static final DeferredItem<Item> CIASTO = ITEMS.register("ciasto", () -> new Item(new Item.Properties()));

  private static final FoodProperties RAW_FOOD = new FoodProperties.Builder()
      .nutrition(-1)
      .saturationModifier(-1)
      .effect(new MobEffectInstance(MobEffects.POISON, 200), 0.5f)
      .effect(new MobEffectInstance(MobEffects.HUNGER, 1200), 0.5f)
      .effect(new MobEffectInstance(MobEffects.DARKNESS, 50), 0.1f)
      .effect(new MobEffectInstance(MobEffects.BLINDNESS, 400), 0.05f)
      .build();

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
  public static final DeferredItem<Item> SALT = ITEMS.registerSimpleItem("salt",
      new Item.Properties().food(RAW_FOOD));

  // Gotowane pierogi
  public static final DeferredItem<Item> SYR_COOKED = ITEMS.registerSimpleItem("syr_cooked",
      new Item.Properties().food(RAW_FOOD));
  public static final DeferredItem<Item> MIENSO_COOKED = ITEMS.registerSimpleItem("mienso_cooked",
      new Item.Properties().food(RAW_FOOD));
  public static final DeferredItem<Item> KAPUSTA_COOKED = ITEMS.registerSimpleItem("kapusta_cooked",
      new Item.Properties().food(RAW_FOOD));
  public static final DeferredItem<Item> RUSKI_COOKED = ITEMS.registerSimpleItem("ruski_cooked",
      new Item.Properties().food(RAW_FOOD));
  public static final DeferredItem<Item> JAGODY_COOKED = ITEMS.registerSimpleItem("jagoda_cooked",
      new Item.Properties().food(RAW_FOOD));

  // Creates a creative tab with the id "examplemod:example_tab" for the example
  // item, that is placed after the combat tab
  public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS
      .register("example_tab", () -> CreativeModeTab.builder()
          .title(Component.translatable("itemGroup.pierogis")) // The language key for the title of your CreativeModeTab
          .withTabsBefore(CreativeModeTabs.COMBAT)
          .icon(() -> RUSKI_COOKED.get().getDefaultInstance())
          .displayItems((parameters, output) -> {
            output.accept(EXAMPLE_ITEM.get());
            output.accept(EXAMPLE_BLOCK_ITEM.get());
            
            output.accept(CIASTO.get());
            output.accept(SALT.get());
            
            output.accept(RUSKI_COOKED.get());
            output.accept(SYR_RAW.get());
            output.accept(MIENSO_RAW.get());
            output.accept(KAPUSTA_RAW.get());
            output.accept(JAGODY_RAW.get());
            output.accept(RUSKI_RAW.get());

            output.accept(SYR_COOKED.get());
            output.accept(MIENSO_COOKED.get());
            output.accept(KAPUSTA_COOKED.get());
            output.accept(JAGODY_COOKED.get());
            output.accept(RUSKI_COOKED.get());
            

          }).build());

  public static void register(IEventBus eventBus) {
    ITEMS.register(eventBus);
    BLOCKS.register(eventBus);
    CREATIVE_MODE_TABS.register(eventBus);

  }
}

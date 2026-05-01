package pl.konradowy.pierogis;

import java.util.List;
import java.util.Random;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class Salt extends Item {
  private String name;

  public Salt(Properties properties) {
    super(properties);
    String[] names = { "chlorek sodu",
        "sodek chloru",
        "NaCl",
        "sól kuchenna",
        "sól spożywcza",
        "sól jadalna",
        "sól stołowa",
        "sól warzona",
        "sól kopalniana",
        "sól.mp4",
        "sól sodowa",
        "soda chlorkowa",
        "chlorek sodowy",
        "sodium chloridum",
        "sul",
        "to coś co dodaje się do pierogów", };
    name = names[new Random().nextInt(names.length)];
  }

  @Override
  public void appendHoverText(ItemStack stack,
      TooltipContext context,
      List<Component> tooltip,
      TooltipFlag flag) {

    tooltip.add(Component.translatable("tooltip.pierogis.salt")
        .withStyle(ChatFormatting.GRAY));
  }

  @Override
  public Component getName(ItemStack stack) {

    return Component.literal(name);
  }
}

package pl.konradowy.pierogis;

import java.util.List;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class PierogisBlock extends BlockItem {
  public PierogisBlock(Block block, Item.Properties properties) {
    super(block, properties);
  }

  @Override
  public void appendHoverText(ItemStack stack,
      TooltipContext context,
      List<Component> tooltip,
      TooltipFlag flag) {

    tooltip.add(Component.translatable("tooltip.pierogis.block")
        .withStyle(ChatFormatting.GRAY));
  }
}

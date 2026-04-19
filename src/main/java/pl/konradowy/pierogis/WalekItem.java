package pl.konradowy.pierogis;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.ItemAbility;

public class WalekItem extends Item {

  public WalekItem(Properties properties) {
    super(properties);
  }

  @Override
  public boolean canPerformAction(ItemStack stack, ItemAbility ability) {
    // return ability == ItemAbilities.SHOVEL_DIG || ability ==
    // ItemAbilities.HOE_TILL || ability == ItemAbility.get("walek_use");
    return ability == ItemAbility.get("walek_use");
  }
}
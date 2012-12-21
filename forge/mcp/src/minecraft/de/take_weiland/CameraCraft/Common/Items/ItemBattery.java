package de.take_weiland.CameraCraft.Common.Items;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StringTranslate;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBattery extends CameraCraftItem {

	public ItemBattery(int par1) {
		super(par1);
		setHasSubtypes(true);
	}
	
	@Override
	public void getSubItems(int itemID, CreativeTabs tab, List list) {
		for (int i = 0; i < 5; i++) {
			list.add(new ItemStack(CameraCraftItem.battery, 1, i));
		}
	}
	
	@Override
	public int getIconFromDamage(int damage) {
		return damage < 5 ? damage : 0;
	}
	
	public int getVoltage(int damage) {
		return (MathHelper.clamp_int(damage, 0, 4) * 2) + 1;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public String getItemDisplayName(ItemStack itemStack) {
        return String.format(StringTranslate.getInstance().translateNamedKey(this.getLocalItemName(itemStack)).trim(), itemStack.getItemDamage() < 5 ? itemStack.getItemDamage() * 2 + 1 : 1);
    }
}

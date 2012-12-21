package de.take_weiland.CameraCraft.Common.Items;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StringTranslate;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.take_weiland.CameraCraft.Common.FilterType;

public class ItemPhotoFilter extends CameraCraftItem {

	public ItemPhotoFilter(int itemID) {
		super(itemID);
		setHasSubtypes(true);
	}

	@Override
	public int getIconFromDamage(int damage) {
		return FilterType.fromItemDamage(damage).getIcon();
	}
	
	@Override
	public void getSubItems(int itemID, CreativeTabs tab, List list) {
		for (int i = 0; i < 5; i++) {
			list.add(new ItemStack(this, 1, i));
		}
	}
	
	@SideOnly(Side.CLIENT)
    public String getItemDisplayName(ItemStack itemStack) {
		int damage = MathHelper.clamp_int(itemStack.getItemDamage(), 0, 3);
		return StringTranslate.getInstance().translateKeyFormat("item.photoFilter.name", StringTranslate.getInstance().translateKey("item.photoFilter." + FilterType.fromItemDamage(itemStack.getItemDamage()).getColorName()));
	}
}

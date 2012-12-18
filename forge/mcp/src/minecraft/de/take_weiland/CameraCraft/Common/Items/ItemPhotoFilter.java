package de.take_weiland.CameraCraft.Common.Items;

import java.util.List;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import de.take_weiland.CameraCraft.Common.FilterType;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.StringTranslate;

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

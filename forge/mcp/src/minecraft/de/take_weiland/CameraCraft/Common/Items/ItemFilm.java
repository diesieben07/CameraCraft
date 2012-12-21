package de.take_weiland.CameraCraft.Common.Items;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StringTranslate;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemFilm extends ItemPhotoStorage {

	public ItemFilm(int itemID) {
		super(itemID);
		setHasSubtypes(true);
	}
	
	@Override
	public int getIconFromDamage(int damage) {
		switch (damage) {
		case 1:
			return 8;
		case 2:
			return 14;
		case 3:
			return 15;
		default:
			return 7;	
		}
	}

	@Override
	public void getSubItems(int itemID, CreativeTabs tab, List list) {
		for (int i = 0; i < 2; i++) {
			list.add(new ItemStack(this, 1, i));
		}
	}
	
	@Override
	public String getItemNameIS(ItemStack itemStack) {
        return (itemStack.getItemDamage() & 1) == 1 ? "item.film.color" : "item.film.blackwhite";
    }
	
	@SideOnly(Side.CLIENT)
	@Override
	public String getItemDisplayName(ItemStack stack) {
        return StringTranslate.getInstance().translateKeyFormat(getItemNameIS(stack) + ".name", stack.getItemDamage() > 1 ? StringTranslate.getInstance().translateKey("item.film.finished") : ""); 
    }
	
	@SideOnly(Side.CLIENT)
	@Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean showExtended) {
		if (itemStack.getItemDamage() < 2) {
			super.addInformation(itemStack, player, list, showExtended);
		} else {
			list.add(String.valueOf(getNumPhotos(itemStack)) + " " + StringTranslate.getInstance().translateKey("item.photostorage.count"));
		}
	}

	@Override
	public int getCapacity() {
		return 36;
	}
}

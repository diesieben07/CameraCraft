package de.take_weiland.CameraCraft.Common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.take_weiland.CameraCraft.Common.Items.CameraCraftItem;

public class CreativeTab extends CreativeTabs {

	public CreativeTab() {
		super("");
	}
	
	@Override
	public ItemStack getIconItemStack() {
		return new ItemStack(CameraCraftItem.camera);
	}
	
	@SideOnly(Side.CLIENT)
    public String getTranslatedTabLabel() {
        return "Camera Craft";
    }

}

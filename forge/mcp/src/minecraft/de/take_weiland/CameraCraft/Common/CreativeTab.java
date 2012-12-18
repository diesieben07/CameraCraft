package de.take_weiland.CameraCraft.Common;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import de.take_weiland.CameraCraft.Common.Items.CameraCraftItem;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.ItemStack;
import net.minecraft.src.StringTranslate;

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

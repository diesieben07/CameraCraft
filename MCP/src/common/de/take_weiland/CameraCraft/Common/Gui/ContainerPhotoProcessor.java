package de.take_weiland.CameraCraft.Common.Gui;

import java.util.Iterator;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

import de.take_weiland.CameraCraft.Common.Inventory.InventoryHelper;
import de.take_weiland.CameraCraft.Common.Inventory.SlotForItem;
import de.take_weiland.CameraCraft.Common.Items.CameraCraftItem;
import de.take_weiland.CameraCraft.Common.TileEntities.TileEntityPhotoProcessor;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ICrafting;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;

public class ContainerPhotoProcessor extends GenericContainer {
	private int lastProcessTime = -1;
	private int lastDuration = -1;
	
	public ContainerPhotoProcessor(TileEntityPhotoProcessor tileEntity, InventoryPlayer playerInventory) {
		super(tileEntity, playerInventory);
		
		addSlotToContainer(new SlotForItem(tileEntity, 0, 146, 31, Item.bucketLava, Item.bucketMilk, Item.bucketWater));
		addSlotToContainer(new SlotForItem(tileEntity, 1, 121, 31, CameraCraftItem.film, 0, 1));
		
		InventoryHelper.addPlayerInventoryToContainer(this, playerInventory, 8, 84);
	}	

	@Override
	public String getGuiBackgroundTexture() {
		return "/CameraCraft/GUIPhotoProcessor.png";
	}
}

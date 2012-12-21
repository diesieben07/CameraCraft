package de.take_weiland.CameraCraft.Common.Gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import de.take_weiland.CameraCraft.Common.Inventory.InventoryHelper;
import de.take_weiland.CameraCraft.Common.Inventory.SlotForItem;
import de.take_weiland.CameraCraft.Common.Items.CameraCraftItem;
import de.take_weiland.CameraCraft.Common.TileEntities.TileEntityPhotoProcessor;

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

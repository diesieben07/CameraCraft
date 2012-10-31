package de.take_weiland.CameraCraft.Common.Gui;

import de.take_weiland.CameraCraft.Common.CameraType;
import de.take_weiland.CameraCraft.Common.Inventory.InventoryCamera;
import de.take_weiland.CameraCraft.Common.Inventory.InventoryHelper;
import de.take_weiland.CameraCraft.Common.Inventory.SlotBattery;
import de.take_weiland.CameraCraft.Common.Inventory.SlotForItem;
import de.take_weiland.CameraCraft.Common.Items.CameraCraftItem;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;

public class ContainerCamera extends GenericContainer {
	
	public final InventoryCamera cameraInv;
	
	public ContainerCamera(InventoryPlayer inventoryPlayer, InventoryCamera inventoryCamera) {
		super(inventoryCamera, inventoryPlayer);
		cameraInv = inventoryCamera;
		
		switch (inventoryCamera.getType()) {
		case DIGITAL:
			addSlotToContainer(new SlotForItem(inventoryCamera, 0, 146, 31, CameraCraftItem.memoryCard));
			addSlotToContainer(new SlotBattery(inventoryCamera, 1, 121, 31, CameraCraftItem.battery));
			addSlotToContainer(new SlotForItem(inventoryCamera, 2, 96, 31, CameraCraftItem.photoFilter));
			break;
		case STANDARD:
			addSlotToContainer(new SlotForItem(inventoryCamera, 0, 146, 31, CameraCraftItem.film));
			addSlotToContainer(new SlotForItem(inventoryCamera, 1, 121, 31, CameraCraftItem.photoFilter));
			break;
		}
		
		
		InventoryHelper.addPlayerInventoryToContainer(this, playerInventory, 8, 84);
	}

	@Override
	public String getGuiBackgroundTexture() {
		return cameraInv.getType().guiBackground();
	}
}
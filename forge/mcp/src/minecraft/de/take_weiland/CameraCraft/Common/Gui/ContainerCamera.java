package de.take_weiland.CameraCraft.Common.Gui;

import net.minecraft.entity.player.InventoryPlayer;
import de.take_weiland.CameraCraft.Common.Inventory.InventoryCamera;
import de.take_weiland.CameraCraft.Common.Inventory.InventoryHelper;
import de.take_weiland.CameraCraft.Common.Inventory.SlotBattery;
import de.take_weiland.CameraCraft.Common.Inventory.SlotForItem;
import de.take_weiland.CameraCraft.Common.Items.CameraCraftItem;

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
	}/*
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
        ItemStack stackCopy = null;
        Slot theSlot = (Slot)this.inventorySlots.get(slot);

        if (theSlot != null && theSlot.getHasStack()) {
            ItemStack stack = theSlot.getStack();
            stackCopy = stack.copy();

            if (slot < (cameraInv.getType() == CameraType.DIGITAL ? 3 : 2))) {
            	if (!this.mergeItemStack(stackCopy, 3, 39, true)) {
                    return null;
                }
                theSlot.onSlotChange(stack, stackCopy);
            } else {
            	stack.stackSize = 0;
            }

            if (stack.stackSize == 0) {
                theSlot.putStack(null);
            } else {
                theSlot.onSlotChanged();
            }

            if (stack.stackSize == stackCopy.stackSize) {
                return null;
            }

            theSlot.onPickupFromSlot(player, stack);
        }

        return stackCopy;
    }*/
}
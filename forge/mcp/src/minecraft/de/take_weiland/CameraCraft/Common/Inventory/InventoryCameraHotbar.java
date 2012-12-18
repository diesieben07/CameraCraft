package de.take_weiland.CameraCraft.Common.Inventory;

import de.take_weiland.CameraCraft.Common.Gui.GuiScreens;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;

public class InventoryCameraHotbar extends InventoryCamera {

	private EntityPlayer player;

	public InventoryCameraHotbar(ItemStack cameraStack, EntityPlayer player) {
		super(cameraStack);
		this.player = player;
	}

	@Override
	public void onInventoryChanged() {
		player.inventory.setInventorySlotContents(player.inventory.currentItem, cameraStack);
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return !player.isDead && ItemStack.areItemStacksEqual(player.getCurrentEquippedItem(), cameraStack);
	}

	@Override
	public GuiScreens getScreenTypeViewPhotos() {
		return GuiScreens.VIEW_PHOTOS_CURRENT_ITEM;
	}

	@Override
	public int getX() {
		return 0;
	}

	@Override
	public int getY() {
		return 0;
	}

	@Override
	public int getZ() {
		return 0;
	}
}
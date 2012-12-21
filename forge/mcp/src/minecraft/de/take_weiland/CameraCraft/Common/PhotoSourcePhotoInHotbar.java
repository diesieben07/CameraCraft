package de.take_weiland.CameraCraft.Common;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import de.take_weiland.CameraCraft.Common.Gui.GuiScreens;
import de.take_weiland.CameraCraft.Common.Items.ItemPhoto;
import de.take_weiland.CameraCraft.Common.Items.ItemPhotoStorage;

public class PhotoSourcePhotoInHotbar implements IPhotoSource {

	private EntityPlayer player;

	public PhotoSourcePhotoInHotbar(EntityPlayer player) {
		this.player = player;
	}
	
	@Override
	public void nameChanged(int photoIndex, String newName) {
		player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemPhotoStorage.renamePhoto(player.getCurrentEquippedItem(), photoIndex, newName));
	}

	@Override
	public boolean canViewPhotos(EntityPlayer player) {
		return !player.isDead && player.getCurrentEquippedItem().getItem() instanceof ItemPhoto && ItemPhotoStorage.getNumPhotos(player.getCurrentEquippedItem()) != 0;
	}

	@Override
	public PhotoInformation getPhotoInformation(String photoId) {
		return ItemPhotoStorage.getPhotoWithID(player.getCurrentEquippedItem(), photoId);
	}

	@Override
	public PhotoInformation getPhotoInformation(int index) {
		return ItemPhotoStorage.getPhotoWithIndex(player.getCurrentEquippedItem(), index);
	}

	@Override
	public int numPhotos() {
		return 1;
	}

	@Override
	public void deletePhoto(int photoIndex) {
		player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemPhotoStorage.deletePhoto(player.getCurrentEquippedItem(), photoIndex));
	}

	@Override
	public boolean canDelete() {
		return false;
	}

	@Override
	public boolean canPrint() {
		return false;
	}

	@Override
	public void addToPrintQueue(List<PhotoSizeAmountInfo> photos) { }

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

	@Override
	public void startViewing(EntityPlayer player) { }

	@Override
	public void stopViewing(EntityPlayer player) { }
}
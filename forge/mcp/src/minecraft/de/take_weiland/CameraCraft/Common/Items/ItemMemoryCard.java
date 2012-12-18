package de.take_weiland.CameraCraft.Common.Items;

public class ItemMemoryCard extends ItemPhotoStorage {

	public ItemMemoryCard(int itemID) {
		super(itemID);
	}

	@Override
	public int getCapacity() {
		return 50;
	}

}

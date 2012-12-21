package de.take_weiland.CameraCraft.Common.Gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import de.take_weiland.CameraCraft.Common.IPhotoSource;

public class ContainerViewPhotos extends Container {

	private final IPhotoSource source;
	private final EntityPlayer player;
	
	public ContainerViewPhotos(EntityPlayer player, IPhotoSource source) {
		this.source = source;
		this.player = player;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return source.canViewPhotos(player);
	}
	
	public IPhotoSource getSource() {
		return source;
	}
	
	@Override
	public void onCraftGuiClosed(EntityPlayer player) {
		super.onCraftGuiClosed(player);
		if (!player.worldObj.isRemote) {
			source.stopViewing(player);
		}
	}
}
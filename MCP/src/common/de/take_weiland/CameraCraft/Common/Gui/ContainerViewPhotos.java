package de.take_weiland.CameraCraft.Common.Gui;

import cpw.mods.fml.common.FMLCommonHandler;
import de.take_weiland.CameraCraft.Common.CameraCraft;
import de.take_weiland.CameraCraft.Common.IPhotoSource;
import de.take_weiland.CameraCraft.Common.PhotoSizeAmountInfo;
import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.InventoryPlayer;

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
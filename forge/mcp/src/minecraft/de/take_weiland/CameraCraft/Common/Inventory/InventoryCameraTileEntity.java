package de.take_weiland.CameraCraft.Common.Inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import de.take_weiland.CameraCraft.Common.Gui.GuiScreens;
import de.take_weiland.CameraCraft.Common.TileEntities.TileEntityCamera;

public class InventoryCameraTileEntity extends InventoryCamera {

	private TileEntityCamera tileEntity;

	public InventoryCameraTileEntity(ItemStack cameraStack, TileEntityCamera tileEntity) {
		super(cameraStack);
		this.tileEntity = tileEntity;
	}

	@Override
	public void onInventoryChanged() {
		//tileEntity.worldObj.markBlockNeedsUpdate(getX(), getY(), getZ());
		//System.out.println("updating inv on " + FMLCommonHandler.instance().getEffectiveSide());
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return tileEntity.worldObj.getBlockTileEntity(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord) != tileEntity ? false : player.getDistanceSq((double)tileEntity.xCoord + 0.5D, (double)tileEntity.yCoord + 0.5D, (double)tileEntity.zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public GuiScreens getScreenTypeViewPhotos() {
		return GuiScreens.CAMERA_INVENTORY_TILE_ENTITY;
	}

	@Override
	public int getX() {
		return tileEntity.xCoord;
	}

	@Override
	public int getY() {
		return tileEntity.yCoord;
	}

	@Override
	public int getZ() {
		return tileEntity.zCoord;
	}

}

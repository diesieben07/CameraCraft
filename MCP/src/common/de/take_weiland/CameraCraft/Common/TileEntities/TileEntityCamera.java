package de.take_weiland.CameraCraft.Common.TileEntities;

import java.util.List;

import de.take_weiland.CameraCraft.Common.CameraType;
import de.take_weiland.CameraCraft.Common.IPhotoSource;
import de.take_weiland.CameraCraft.Common.PhotoInformation;
import de.take_weiland.CameraCraft.Common.PhotoSizeAmountInfo;
import de.take_weiland.CameraCraft.Common.Entities.EntityCamera;
import de.take_weiland.CameraCraft.Common.Gui.GuiScreens;
import de.take_weiland.CameraCraft.Common.Inventory.InventoryCamera;
import de.take_weiland.CameraCraft.Common.Inventory.InventoryCameraTileEntity;
import de.take_weiland.CameraCraft.Common.Items.CameraCraftItem;
import de.take_weiland.CameraCraft.Common.Items.ItemCamera;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.INetworkManager;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet132TileEntityData;
import net.minecraft.src.StringTranslate;
import net.minecraft.src.TileEntity;

public class TileEntityCamera extends TileEntity {
	private String owner;
	private String photoName = StringTranslate.getInstance().translateKey("cameracraft.photoname.default");
	private InventoryCamera cameraInventory = new InventoryCameraTileEntity(new ItemStack(CameraCraftItem.camera), this);
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		setCameraAndUpdate(ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("camera")));
		owner = nbt.getString("owner");
		photoName = nbt.getString("photoname");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setCompoundTag("camera", cameraInventory.getCameraStack().writeToNBT(new NBTTagCompound()));
		nbt.setString("owner", owner);
		nbt.setString("photoname", photoName);
	}
	
	public InventoryCamera getCameraInventory() {
		return cameraInventory;
	}
	
	public String getOwner() {
		return owner;
	}
	public String getPhotoName() {
		return photoName;
	}
	
	public void setInfo(EntityPlayer player, ItemStack camera) {
		this.owner = player.username;
		setCameraAndUpdate(camera);
	}

	public void setCameraAndUpdate(ItemStack cameraStack) {
		cameraInventory.updateCamera(cameraStack);
	}
	
	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt) {
		readFromNBT(pkt.customParam1);
    }
	
	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return new Packet132TileEntityData(xCoord, yCoord, zCoord, 0, nbt);
    }
}
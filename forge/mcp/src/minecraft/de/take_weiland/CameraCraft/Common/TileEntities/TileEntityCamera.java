package de.take_weiland.CameraCraft.Common.TileEntities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StringTranslate;
import de.take_weiland.CameraCraft.Common.Inventory.InventoryCamera;
import de.take_weiland.CameraCraft.Common.Inventory.InventoryCameraTileEntity;
import de.take_weiland.CameraCraft.Common.Items.CameraCraftItem;

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
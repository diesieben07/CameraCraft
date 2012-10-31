package de.take_weiland.CameraCraft.Common;

import net.minecraft.src.NBTTagCompound;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import de.take_weiland.CameraCraft.Common.Network.PacketHelper;

public class PhotoSizeAmountInfo {
	private int sizeX;
	private int sizeY;
	private int amount;
	private int photoIndex;
	
	public int getSizeX() {
		return sizeX;
	}
	
	public PhotoSizeAmountInfo setSizeX(int sizeX) {
		this.sizeX = sizeX;
		return this;
	}
	
	public int getSizeY() {
		return sizeY;
	}
	
	public PhotoSizeAmountInfo setSizeY(int sizeY) {
		this.sizeY = sizeY;
		return this;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public PhotoSizeAmountInfo setAmount(int amount) {
		this.amount = amount;
		return this;
	}
	
	public int getPhotoIndex() {
		return photoIndex;
	}
	
	public PhotoSizeAmountInfo setPhotoIndex(int photoIndex) {
		this.photoIndex = photoIndex;
		return this;
	}

	public PhotoSizeAmountInfo(int sizeX, int sizeY, int amount, int photoIndex) {
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.amount = amount;
		this.photoIndex = photoIndex;
	}

	public PhotoSizeAmountInfo() {	}
	
	public void writeTo(ByteArrayDataOutput data) {
		data.writeByte(sizeX);
		data.writeByte(sizeY);
		data.writeByte(amount);
		data.writeByte(photoIndex);
	}
	
	public PhotoSizeAmountInfo readFrom(ByteArrayDataInput data) {
		sizeX = data.readByte();
		sizeY = data.readByte();
		amount = data.readByte();
		photoIndex = data.readByte();
		return this;
	}
	
	public static PhotoSizeAmountInfo createFrom(ByteArrayDataInput data) {
		return new PhotoSizeAmountInfo().readFrom(data);
	}
}
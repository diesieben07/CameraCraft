package de.take_weiland.CameraCraft.Common;

import java.util.Collection;
import java.util.HashMap;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import de.take_weiland.CameraCraft.Common.Inventory.InventoryHelper;
import de.take_weiland.CameraCraft.Common.Items.CameraCraftItem;
import de.take_weiland.CameraCraft.Common.Items.ItemPhotoStorage;
import de.take_weiland.CameraCraft.Common.Network.PacketHelper;
import de.take_weiland.CameraCraft.Common.TileEntities.TileEntityTeleporatationEnabler;

import net.minecraft.src.DamageSource;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Teleporter;
import net.minecraft.src.World;
import net.minecraftforge.common.Configuration;

public class PhotoInformation {
	
	private String photoID = "";
	private String player = "";
	private String name = "";
	private byte sizeX = 0;
	private byte sizeY = 0;
	private int locX = 0;
	private int locY = 0;
	private int locZ = 0;
	private float pitch = 0;
	private float yaw = 0;
	private int dimension;
	private boolean isTeleport = false;
	private int amount;
	
	public static HashMap<String,PhotoInformation> waitingTeleports = new HashMap<String, PhotoInformation>();
	
	public int getAmount() {
		return amount;
	}

	public PhotoInformation setAmount(int amount) {
		this.amount = amount;
		return this;
	}

	public PhotoInformation setPhotoId(String photoID) {
		this.photoID = photoID;
		return this;
	}
	
	public PhotoInformation setRotationPitch(float pitch) {
		this.pitch = pitch;
		return this;
	}
	
	public PhotoInformation setRotationYaw(float yaw) {
		this.yaw = yaw;
		return this;
	}
	
	public PhotoInformation setLocX(int locX) {
		this.locX = locX;
		return this;
	}
	
	public PhotoInformation setLocY(int locY) {
		this.locY = locY;
		return this;
	}
	
	public PhotoInformation setLocZ(int locZ) {
		this.locZ = locZ;
		return this;
	}
	
	public PhotoInformation setPlayer(String player) {
		this.player = player;
		return this;
	}
	
	public PhotoInformation setDimension(int dimension) {
		this.dimension = dimension;
		return this;
	}
	
	public PhotoInformation setName(String name) {
		this.name = name;
		return this;
	}
	
	public void setLocation(EntityLiving entity) {
		this.locX = MathHelper.floor_double(entity.posX);
		this.locY = MathHelper.floor_double(entity.posY);
		this.locZ = MathHelper.floor_double(entity.posZ);
		this.yaw = entity.rotationYaw;
		this.pitch = entity.rotationPitch;
		this.dimension = entity.worldObj.provider.dimensionId;
	}
	
	public PhotoInformation setPlayerAndLocation(EntityPlayer player) {
		this.player = player.username;
		setLocation(player);
		return this;
	}
	
	public PhotoInformation setSizeX(int sizeX) {
		this.sizeX = (byte) sizeX;
		return this;
	}
	
	public PhotoInformation setSizeY(int sizeY) {
		this.sizeY = (byte) sizeY;
		return this;
	}
	
	public PhotoInformation setTeleport() {
		return setTeleport(true);
	}
	
	public PhotoInformation unsetTeleport() {
		return setTeleport(false);
	}
	
	public PhotoInformation setTeleport(boolean state) {
		this.isTeleport = state;
		return this;
	}
	
	public int getSizeX() {
		return sizeX;
	}
	
	public int getSizeY() {
		return sizeY;
	}
	
	public String getPhotoId() {
		return photoID;
	}
	
	public String getPlayer() {
		return player;
	}
	
	public String getName() {
		return name;
	}
	
	public int getLocX() {
		return locX;
	}
	
	public int getLocY() {
		return locY;
	}
	
	public int getLocZ() {
		return locZ;
	}
	
	public int getDimension() {
		return dimension;
	}
	
	public float getRotationYaw() {
		return yaw;
	}
	
	public float getRotationPitch() {
		return pitch;
	}
	
	public boolean isTeleport() {
		return isTeleport;
	}
	
	public void teleport(EntityPlayerMP player) {
		boolean canTeleportForFree = ConfigurationManager.allowFreeTeleport || TileEntityTeleporatationEnabler.isTileEntityInRange(player.worldObj, (int)player.posX, (int)player.posY, (int)player.posZ, 50);
		if (ConfigurationManager.allowTeleport && isTeleport && (player.inventory.hasItem(CameraCraftItem.teleportationBattery.shiftedIndex) || player.capabilities.isCreativeMode || canTeleportForFree)) {
			if (!player.capabilities.isCreativeMode && !canTeleportForFree) {
				int slotIndex = InventoryHelper.getSlotThatHasItem(player.inventory, CameraCraftItem.teleportationBattery.shiftedIndex);
				ItemStack stack = player.inventory.getStackInSlot(slotIndex);
				stack.setItemDamage(stack.getItemDamage() + 1);
				if (stack.getItemDamage() > stack.getMaxDamage()) {
					stack.stackSize--;
					stack.setItemDamage(0);
				}
				if (stack.stackSize == 0) {
					stack = null;
				}
				player.inventory.setInventorySlotContents(slotIndex, stack);
				player.attackEntityFrom(DamageSource.fall, 2);
			}
			player.worldObj.playSoundAtEntity(player, "mob.endermen.portal", 1, 1);
			if (player.dimension != dimension) {
				player.mcServer.getConfigurationManager().transferPlayerToDimension(player, dimension, new PhotoDimensionChanger());
			} else {
				setPosition(player);
			}
		}
	}
	
	public void teleport(EntityPlayer player) {
		if (player instanceof EntityPlayerMP) {
			teleport((EntityPlayerMP)player);
		}
	}
	
	private class PhotoDimensionChanger extends Teleporter {
		
		@Override
		public void placeInPortal(World world, Entity entity, double entityX, double entityY, double entityZ, float entityRotationYaw) {
			setPosition(entity);
		}
	}
	
	public void setPosition(Entity entity) {
		if (entity instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP)entity;
			player.playerNetServerHandler.setPlayerLocation(locX, locY, locZ, yaw, pitch);
			player.triggerAchievement(Achievements.teleport);
		} else {
			entity.setLocationAndAngles(locX, locY + 0.4, locZ, yaw, pitch);
		}
		entity.worldObj.playSoundAtEntity(entity, "mob.endermen.portal", 1, 1);
	}
	
	public void writeToNBT(NBTTagCompound nbt) {
		nbt.setString("photoid", photoID);
		nbt.setString("player", player);
		nbt.setString("name", name);
		nbt.setInteger("locx", locX);
		nbt.setInteger("locy", locY);
		nbt.setInteger("locz", locZ);
		nbt.setByte("sizeX", sizeX);
		nbt.setByte("sizeY", sizeY);
		nbt.setBoolean("teleport", isTeleport);
		nbt.setByte("amount", (byte) amount);
		nbt.setByte("dim", (byte) dimension);
		nbt.setFloat("yaw", yaw);
		nbt.setFloat("pitch", pitch);
	}
	
	public PhotoInformation readFromNBT(NBTTagCompound nbt) {
		photoID = nbt.getString("photoid");
		player = nbt.getString("player");
		name = nbt.getString("name");
		locX = nbt.getInteger("locx");
		locY = nbt.getInteger("locy");
		locZ = nbt.getInteger("locz");
		sizeX = nbt.getByte("sizeX");
		sizeY = nbt.getByte("sizeY");
		isTeleport = nbt.getBoolean("teleport");
		amount = nbt.getByte("amount");
		yaw = nbt.getFloat("yaw");
		pitch = nbt.getFloat("pitch");
		dimension = nbt.getByte("dim");
		return this;
	}
	
	public void writeToData(ByteArrayDataOutput data) {
		data.writeUTF(photoID);
		data.writeUTF(player);
		data.writeUTF(name);
		data.writeInt(locX);
		data.writeInt(locY);
		data.writeInt(locZ);
		data.writeByte(sizeX);
		data.writeByte(sizeY);
		data.writeBoolean(isTeleport);
		data.writeByte(amount);
		data.writeFloat(yaw);
		data.writeFloat(pitch);
		data.writeByte(dimension);
	}
	
	public PhotoInformation readFromData(ByteArrayDataInput data) {
		photoID = data.readUTF();
		player = data.readUTF();
		name = data.readUTF();
		locX = data.readInt();
		locY = data.readInt();
		locZ = data.readInt();
		sizeX = data.readByte();
		sizeY = data.readByte();
		isTeleport = data.readBoolean();
		amount = data.readByte();
		yaw = data.readFloat();
		pitch = data.readFloat();
		dimension = data.readByte();
		return this;
	}
	
	public NBTTagCompound createNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return nbt;
	}
	
	public static PhotoInformation createFromNBT(NBTTagCompound nbt) {
		return new PhotoInformation().readFromNBT(nbt);
	}
	
	public static PhotoInformation createFromData(ByteArrayDataInput data) {
		return new PhotoInformation().readFromData(data);
	}
}

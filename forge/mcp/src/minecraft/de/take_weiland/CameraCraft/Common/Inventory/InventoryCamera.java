package de.take_weiland.CameraCraft.Common.Inventory;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import de.take_weiland.CameraCraft.Common.Achievements;
import de.take_weiland.CameraCraft.Common.CameraType;
import de.take_weiland.CameraCraft.Common.ConfigurationManager;
import de.take_weiland.CameraCraft.Common.IPhotoSource;
import de.take_weiland.CameraCraft.Common.IndustrialCraftManager;
import de.take_weiland.CameraCraft.Common.PhotoInformation;
import de.take_weiland.CameraCraft.Common.PhotoSizeAmountInfo;
import de.take_weiland.CameraCraft.Common.Items.CameraCraftItem;
import de.take_weiland.CameraCraft.Common.Items.ItemBattery;
import de.take_weiland.CameraCraft.Common.Items.ItemPhotoStorage;
import de.take_weiland.CameraCraft.Common.TileEntities.TileEntityCamera;

public abstract class InventoryCamera implements IInventory, IPhotoSource {

	protected ItemStack cameraStack;
	private CameraType cameraType;
	
	public InventoryCamera(ItemStack cameraStack) {
		updateCamera(cameraStack);
	}
	
	public void updateCamera(ItemStack cameraStack) {
		this.cameraStack = cameraStack;
		cameraType = CameraType.fromItemDamage(cameraStack.getItemDamage());
		CameraCraftItem.createStackTagCompound(cameraStack);
	}
	
	public ItemStack getCameraStack() {
		return cameraStack;
	}
	
	public CameraType getType() {
		return cameraType;
	}

	public int getPartialBattery() {
		return cameraStack.stackTagCompound.getByte("partialBattery");
	}

	public void setPartialBattery(int partialBattery) {
		cameraStack.stackTagCompound.setByte("partialBattery", (byte) partialBattery);
	}
	
	public void addPhoto(String photoId, EntityPlayer player, String photoName) {
		ItemStack stackToSavePhotoTo = getStackForPhotoSave();
		Item theItem = stackToSavePhotoTo.getItem();
		if (theItem instanceof ItemPhotoStorage) {
			PhotoInformation photo = new PhotoInformation().setPhotoId(photoId).setPlayerAndLocation(player).setName(photoName);
			ItemPhotoStorage.addPhoto(stackToSavePhotoTo, photo);
			player.triggerAchievement(Achievements.takePhoto);
			decreaseBattery();
		}
		setStackForPhotoSave(stackToSavePhotoTo);
	}
	
	public void addPhoto(String photoId, TileEntityCamera tileEntity) {
		ItemStack stackToSavePhotoTo = getStackForPhotoSave();
		Item theItem = stackToSavePhotoTo.getItem();
		if (theItem instanceof ItemPhotoStorage) {
			PhotoInformation photo = new PhotoInformation().setPhotoId(photoId).setName(tileEntity.getPhotoName()).setPlayer(tileEntity.getOwner()).setLocX(tileEntity.xCoord).setLocY(tileEntity.yCoord).setLocZ(tileEntity.zCoord);
			ItemPhotoStorage.addPhoto(stackToSavePhotoTo, photo);
			decreaseBattery();
		}
		setStackForPhotoSave(stackToSavePhotoTo);
	}
	
	@Override
	public int getSizeInventory() {
		return cameraType.numSlots();
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return ItemStack.loadItemStackFromNBT(cameraStack.stackTagCompound.getCompoundTag("slot" + String.valueOf(slot)));
	}

	@Override
	public ItemStack decrStackSize(int slot, int numDecrease) {
		return InventoryHelper.genericStackDecrease(this, slot, numDecrease);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		return getStackInSlot(slot);
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		if (slot >= 0 && slot < cameraType.numSlots()) {
			NBTTagCompound slotCompound = new NBTTagCompound();
			if (stack != null) {
				slotCompound = stack.writeToNBT(slotCompound);
			}
			
			cameraStack.stackTagCompound.setCompoundTag("slot" + String.valueOf(slot), slotCompound);	
		}
		onInventoryChanged();
	}

	@Override
	public String getInvName() {
		return cameraType.nameLocalization() + ".name";
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void openChest() {
	}

	@Override
	public void closeChest() {
	}

	public ItemStack getStackForPhotoSave() {
		return getStackInSlot(0);
	}
	
	public void setStackForPhotoSave(ItemStack stack) {
		setInventorySlotContents(0, stack);
		onInventoryChanged();
	}

	public void decreaseBattery() {
		if (cameraType != CameraType.DIGITAL) {
			return;
		}
		
		ItemStack battery = getStackInSlot(0);
		
		if (ConfigurationManager.ic2BatteryCost > 0 && IndustrialCraftManager.canItemProvideEnergy(battery) && IndustrialCraftManager.hasEnoughPower(battery, ConfigurationManager.ic2BatteryCost)) {
			IndustrialCraftManager.discharge(battery, ConfigurationManager.ic2BatteryCost);
			setInventorySlotContents(1, battery);
			System.out.println("yes");
		} else {
		
			int partialBattery = getPartialBattery();
			if (partialBattery > 0) {
				setPartialBattery(partialBattery - 1);
			} else {
				ItemStack stack = getStackInSlot(1);
				if (stack != null && stack.getItem() instanceof ItemBattery) {
					setPartialBattery(((ItemBattery)stack.getItem()).getVoltage(stack.getItemDamage()) - 1);
					decrStackSize(1, 1);
				}
			}
		}
	}

	/* Photo source part */
	@Override
	public void nameChanged(int photoIndex, String newName) {
		setStackForPhotoSave(ItemPhotoStorage.renamePhoto(getStackForPhotoSave(), photoIndex, newName));
	}

	@Override
	public boolean canViewPhotos(EntityPlayer player) {
		return isUseableByPlayer(player);
	}

	@Override
	public PhotoInformation getPhotoInformation(String photoId) {
		return ItemPhotoStorage.getPhotoWithID(getStackForPhotoSave(), photoId);
	}

	@Override
	public void deletePhoto(int photoIndex) {
		setStackForPhotoSave(ItemPhotoStorage.deletePhoto(getStackForPhotoSave(), photoIndex));
	}

	@Override
	public boolean canDelete() {
		return cameraType == CameraType.DIGITAL;
	}

	@Override
	public boolean canPrint() {
		return false;
	}

	@Override
	public void addToPrintQueue(List<PhotoSizeAmountInfo> photos) { }

	@Override
	public PhotoInformation getPhotoInformation(int index) {
		return ItemPhotoStorage.getPhotoWithIndex(getStackForPhotoSave(), index);
	}

	@Override
	public int numPhotos() {
		return ItemPhotoStorage.getNumPhotos(getStackForPhotoSave());
	}

	@Override
	public void startViewing(EntityPlayer player) { }

	@Override
	public void stopViewing(EntityPlayer player) { }
}

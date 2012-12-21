package de.take_weiland.CameraCraft.Common.Items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StringTranslate;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.take_weiland.CameraCraft.Common.PhotoInformation;

public abstract class ItemPhotoStorage extends CameraCraftItem {

	public ItemPhotoStorage(int itemID) {
		super(itemID);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean something) {
		list.add(String.valueOf(getNumPhotos(itemStack)) + " / " + String.valueOf(getCapacity()) + " " + StringTranslate.getInstance().translateKey("item.photostorage.count"));
	}
	
	@Override
	public boolean getShareTag() {
		return true;
	}
	
	public abstract int getCapacity();
	
	public static boolean isFull(ItemStack stack) {
		if (stack == null || !(stack.getItem() instanceof ItemPhotoStorage)) {
			return false;
		}
		
		return getFreePhotos(stack) == 0;
	}
	
	public static boolean hasFreePhotos(ItemStack stack) {
		if (stack == null || !(stack.getItem() instanceof ItemPhotoStorage)) {
			return false;
		}
		
		return !isFull(stack);
	}
	
	public static int getNumPhotos(ItemStack stack) {
		if (stack == null || !(stack.getItem() instanceof ItemPhotoStorage)) {
			return 0;
		}
		
		createStackTagCompound(stack);
		return stack.stackTagCompound.getTagList("photos").tagCount();
	}
	
	public static int getFreePhotos(ItemStack stack) {
		if (stack == null || !(stack.getItem() instanceof ItemPhotoStorage)) {
			return 0;
		}
		
		return ((ItemPhotoStorage)stack.getItem()).getCapacity() - getNumPhotos(stack);
	}
	
	public static PhotoInformation[] getPhotosFromItemStack(ItemStack stack) {
		if (stack == null || !(stack.getItem() instanceof ItemPhotoStorage)) {
			return null;
		}
		
		CameraCraftItem.createStackTagCompound(stack);
		
		NBTTagList photos = stack.stackTagCompound.getTagList("photos");
		PhotoInformation[] infos = new PhotoInformation[photos.tagCount()];
		for (int i = 0; i < infos.length; i++) {
			infos[i] = PhotoInformation.createFromNBT((NBTTagCompound)photos.tagAt(i));
		}
		
		return infos;
	}
	
	public static PhotoInformation getPhotoWithID(ItemStack stack, String photoID) {
		if (stack == null || !(stack.getItem() instanceof ItemPhotoStorage)) {
			return null;
		}
		
		CameraCraftItem.createStackTagCompound(stack);
		NBTTagList photos = stack.stackTagCompound.getTagList("photos");
		for (int i = 0; i < photos.tagCount(); i++) {
			PhotoInformation info = PhotoInformation.createFromNBT(((NBTTagCompound)photos.tagAt(i)));
			if (info.getPhotoId().equals(photoID)) {
				return info;
			}
		}
		return null;
	}
	
	public static boolean hasPhotoWithId(ItemStack stack, String photoId) {
		return getPhotoWithID(stack, photoId) != null;
	}
	
	public static PhotoInformation getFirstPhoto(ItemStack stack) {
		if (stack == null || !(stack.getItem() instanceof ItemPhotoStorage)) {
			return null;
		}
		
		CameraCraftItem.createStackTagCompound(stack);
		
		NBTTagList photos = stack.stackTagCompound.getTagList("photos");
		if (photos.tagCount() == 0) {
			return null;
		} else {
			return PhotoInformation.createFromNBT((NBTTagCompound)photos.tagAt(0));
		}
	}
	
	public static ItemStack deletePhoto(ItemStack stack, String photoID) {
		if (stack == null || !(stack.getItem() instanceof ItemPhotoStorage)) {
			return stack;
		}
		
		CameraCraftItem.createStackTagCompound(stack);
		
		NBTTagList photos = stack.stackTagCompound.getTagList("photos");
		NBTTagList newPhotos = new NBTTagList();
		for (int i = 0; i < photos.tagCount(); i++) {
			if (!PhotoInformation.createFromNBT((NBTTagCompound) photos.tagAt(i)).getPhotoId().equals(photoID)) {
				newPhotos.appendTag(photos.tagAt(i));
			}
		}
		stack.stackTagCompound.setTag("photos", newPhotos);
		return stack;
	}
	
	public static ItemStack deletePhoto(ItemStack stack, int photoIndex) {
		if (stack == null || !(stack.getItem() instanceof ItemPhotoStorage)) {
			return stack;
		}
		
		CameraCraftItem.createStackTagCompound(stack);
		
		NBTTagList photos = stack.stackTagCompound.getTagList("photos");
		NBTTagList newPhotos = new NBTTagList();
		for (int i = 0; i < photos.tagCount(); i++) {
			if (i != photoIndex) {
				newPhotos.appendTag(photos.tagAt(i));
			}
		}
		stack.stackTagCompound.setTag("photos", newPhotos);
		return stack;
	}
	
	public static PhotoInformation getPhotoWithIndex(ItemStack stack, int index) {
		if (stack == null || !(stack.getItem() instanceof ItemPhotoStorage)) {
			return null;
		}
		
		CameraCraftItem.createStackTagCompound(stack);
		NBTTagList photos = stack.stackTagCompound.getTagList("photos");
		if (index < photos.tagCount()) {
			return PhotoInformation.createFromNBT((NBTTagCompound)photos.tagAt(index));
		} else {
			return null;
		}
	}
	
	public static ItemStack addPhoto(ItemStack stack, PhotoInformation photo) {
		if (stack == null || !(stack.getItem() instanceof ItemPhotoStorage)) {
			return stack;
		}
		
		CameraCraftItem.createStackTagCompound(stack);
		
		NBTTagList photos = stack.stackTagCompound.getTagList("photos");
		photos.appendTag(photo.createNBT());
		stack.stackTagCompound.setTag("photos", photos);
		return stack;
	}
	
	public static ItemStack renamePhoto(ItemStack stack, int index, String newName) {
		if (stack == null || !(stack.getItem() instanceof ItemPhotoStorage)) {
			return stack;
		}
		
		CameraCraftItem.createStackTagCompound(stack);
		
		NBTTagList photos = stack.stackTagCompound.getTagList("photos");
		NBTTagList newPhotos = new NBTTagList();
		for (int i = 0; i < photos.tagCount(); i++) {
			if (i != index) {
				newPhotos.appendTag(photos.tagAt(i));
			} else {
				newPhotos.appendTag(PhotoInformation.createFromNBT((NBTTagCompound)photos.tagAt(i)).setName(newName).createNBT());
			}
		}
		stack.stackTagCompound.setTag("photos", newPhotos);
		return stack;
	}
}
package de.take_weiland.CameraCraft.Common.TileEntities;

import java.util.Random;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import de.take_weiland.CameraCraft.Common.Inventory.IProgressInventory;
import de.take_weiland.CameraCraft.Common.Items.CameraCraftItem;
import de.take_weiland.CameraCraft.Common.Items.ItemPhotoStorage;

public class TileEntityPhotoProcessor extends TileEntityInventory implements IProgressInventory {

	private int processTimeLeft = 0;
	private int processDuration;
	private Random rand = new Random(); 
	
	
	@Override
	public int getSizeInventory() {
		return 2;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        processTimeLeft = nbt.getShort("procTime");
        processDuration = nbt.getShort("procDur");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setShort("procTime", (short) processTimeLeft);
        nbt.setShort("procDur", (short) processDuration);
    }
    
    @Override
    public void updateEntity() {
    	if (!worldObj.isRemote) {
	    	if (processTimeLeft > 0) {
	    		if (getStackInSlot(0) == null || getProcessTime(getStackInSlot(1)) == -1) {
	    			processTimeLeft = processDuration = 0;
	    		} else {    		
	    			processTimeLeft--;
	    		}
	    	} else {
		    	ItemStack filmStack = getStackInSlot(1);
		    	ItemStack bucketStack = getStackInSlot(0);
	    		int processTime;
		    	if (bucketStack != null && bucketStack.getItem() != Item.bucketEmpty && (processTime = getProcessTime(filmStack)) != -1) {
		    		processTimeLeft = processDuration = processTime;
		    	}
	    	}
	    	
	    	if (processTimeLeft == 0) {
	    		ItemStack filmStack = getStackInSlot(1);
	    		if (getProcessTime(filmStack) == processDuration) {
	    			ItemStack bucketItem = getStackInSlot(0);
	    			if (bucketItem.getItem() == Item.bucketWater) {
	    				filmStack.setItemDamage(filmStack.getItemDamage() + 2);
		    			setInventorySlotContents(1, filmStack);
	    			} else {
	    				if (bucketItem.getItem() == Item.bucketLava) {
	    					worldObj.playSoundEffect(xCoord, yCoord, zCoord, "random.fizz", 0.4F, 2.0F + rand.nextFloat() * 0.4F);
	    				}
	    				setInventorySlotContents(1, null);
	    			}
	    			
	    			setInventorySlotContents(0, bucketItem.getItem().getContainerItemStack(bucketItem));
	    		}
	    		processDuration = 0;
	    	}
    	}
    }
    
    private static int getProcessTime(ItemStack filmStack) {
    	int numPhotos;
    	return filmStack != null && filmStack.getItem() == CameraCraftItem.film && filmStack.getItemDamage() < 2 && (numPhotos = ItemPhotoStorage.getNumPhotos(filmStack)) != 0 ? numPhotos * 100 : -1;
    }
    
    public int getProcessTime() {
    	return processTimeLeft;
    }

	public void setProcessTime(int time) {
		processTimeLeft = time;
	}

	public int getProcessDuration() {
		return processDuration;
	}

	public void setProcessDuration(int time) {
		processDuration = time;		
	}
}
package de.take_weiland.CameraCraft.Common.TileEntities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;

import de.take_weiland.CameraCraft.Client.CameraCraftClientEventHandler;
import de.take_weiland.CameraCraft.Common.CameraCraft;
import de.take_weiland.CameraCraft.Common.CommonTickHandler;
import de.take_weiland.CameraCraft.Common.IPhotoSource;
import de.take_weiland.CameraCraft.Common.PhotoInformation;
import de.take_weiland.CameraCraft.Common.PhotoSizeAmountInfo;
import de.take_weiland.CameraCraft.Common.Blocks.CameraCraftBlock;
import de.take_weiland.CameraCraft.Common.Gui.GuiScreens;
import de.take_weiland.CameraCraft.Common.Inventory.IProgressInventory;
import de.take_weiland.CameraCraft.Common.Inventory.InventoryHelper;
import de.take_weiland.CameraCraft.Common.Inventory.InventoryHelper.InventorySearchInfo;
import de.take_weiland.CameraCraft.Common.Items.CameraCraftItem;
import de.take_weiland.CameraCraft.Common.Items.ItemPhotoStorage;
import de.take_weiland.CameraCraft.Common.Network.PacketHelper;
import de.take_weiland.CameraCraft.Common.Network.NetAction;
import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.INetworkManager;
import net.minecraft.src.InventoryLargeChest;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet132TileEntityData;
import net.minecraft.src.TileEntityChest;
import net.minecraft.src.WorldServer;
import net.minecraftforge.common.MinecraftForge;

public class TileEntityPhotoStation extends TileEntityInventory implements IProgressInventory, IPhotoSource {

	private static final int TIME_MULTIPLIER = 50;
	private static final int SOUND_TIMEOUT = 74;
	
	private String viewingPlayer = null;
	private ArrayList<PhotoInformation> printJobs = new ArrayList<PhotoInformation>();
	private int processTimeLeft = 0;
	private int processDuration = 0;
	private int jobsAlreadyFinished = 0;
	private int ticksBeforeSound = 1;
	
	private static final ItemStack[] DYES_REQUIRED = {
		new ItemStack(Item.dyePowder, 1, 6), // cyan
		new ItemStack(Item.dyePowder, 1, 11), // yellow
		new ItemStack(Item.dyePowder, 1, 13) // magenta
	};

	@Override
	public int getSizeInventory() {
		return 10; 
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        processDuration = nbt.getShort("duration");
        processTimeLeft = nbt.getShort("timeleft");
        jobsAlreadyFinished = nbt.getShort("finished");
        NBTTagList printJobsSaved = nbt.getTagList("jobs");
        for (int i = 0; i < printJobsSaved.tagCount(); i++) {
        	NBTTagCompound currentJob = (NBTTagCompound)printJobsSaved.tagAt(i);
        	printJobs.add(PhotoInformation.createFromNBT(currentJob));
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setShort("duration", (short)processDuration);
        nbt.setShort("timeleft", (short)processTimeLeft);
        nbt.setShort("finished", (short)jobsAlreadyFinished);
        NBTTagList jobsForSave = new NBTTagList();
        for (PhotoInformation info : printJobs) {
        	jobsForSave.appendTag(info.createNBT());
        }
        nbt.setTag("jobs", jobsForSave);
    }
    
    @Override
    public void updateEntity() {
    	if (!worldObj.isRemote) {
    		if (processDuration != 0 && processTimeLeft == getTimeNeededForJobsWithoutNext()) {
    			
    			PhotoInformation thisJob = printJobs.remove(0);
    			
    			int paperNeeded = getPaperNeededForJob(thisJob);
    			InventorySearchInfo[] inventories = getInventoriesToSearch();
    			int paperHave = InventoryHelper.findItemAmountInInventories(Item.paper, inventories);
    			
    			if (paperNeeded <= paperHave && areDyesThere(inventories)) {
    				InventoryHelper.consumeItemFromInventories(Item.paper, paperNeeded, inventories);
    				consumeDyes(inventories);
    				ItemStack photo = new ItemStack(CameraCraftItem.photo, 1, 0);
        			photo = ItemPhotoStorage.addPhoto(photo, thisJob);
        			InventoryHelper.putStackInFirstFreeSlotOrDrop(photo, worldObj, xCoord, yCoord, zCoord, inventories);
    			}
    		}
    		
    		if (processTimeLeft > 0) {
	    		InventorySearchInfo[] inventories = getInventoriesToSearch();
    			    		
    			if (areDyesThere(inventories) && getPaperNeededForJob(printJobs.get(0)) <= InventoryHelper.findItemAmountInInventories(Item.paper, inventories)) {
	    			processTimeLeft--;
	    			ticksBeforeSound--;
	    		}	    		
	    	} else {
	    		processDuration = 0;
	    		ticksBeforeSound = 1;
	    	}
    		
    		if (ticksBeforeSound == 0) {
    			ticksBeforeSound = SOUND_TIMEOUT;
    			worldObj.playSoundEffect(xCoord, yCoord, zCoord, "cameracraft.printer", 0.5F, 1);
    		}
    	}
    }
    
    private boolean areDyesThere(InventorySearchInfo[] inventories) {
    	for (ItemStack stack : DYES_REQUIRED) {
			if (InventoryHelper.findItemAmountInInventories(stack, inventories) < 1) {
				return false;
			}
		}
    	return true;
    }
    
    private void consumeDyes(InventorySearchInfo[] inventories) {
    	for (ItemStack stack : DYES_REQUIRED) {
    		InventoryHelper.consumeItemFromInventories(stack, 1, inventories);
    	}
    }
    
    private int getPaperNeededForJob(PhotoInformation info) {
    	return info.getAmount() * info.getSizeX() * info.getSizeY();
	}

	private int getTimeNeededForJobsWithoutNext() {
    	int time = 0;
    	boolean first = true;
    	for (PhotoInformation info : printJobs) {
    		if (first) {
    			first = false;
    		} else {
    			time += getTimeForJob(info);
    		}
    	}
    	return time;
	}

	private int getTimeForJob(PhotoInformation info) {
		return info.getAmount() * info.getSizeX() * info.getSizeY() * TIME_MULTIPLIER;
	}

	private InventorySearchInfo[] getInventoriesToSearch() {
		ArrayList<InventorySearchInfo> inventories = new ArrayList<InventorySearchInfo>();
		inventories.add(new InventorySearchInfo(this, 1, 10));
				
		getChestInventoryAt(inventories, xCoord + 1, yCoord, zCoord);
		getChestInventoryAt(inventories, xCoord - 1, yCoord, zCoord);
		getChestInventoryAt(inventories, xCoord, yCoord, zCoord + 1);
		getChestInventoryAt(inventories, xCoord, yCoord, zCoord - 1);
		return inventories.toArray(new InventorySearchInfo[inventories.size()]);
	}
    
    private void getChestInventoryAt(ArrayList<InventorySearchInfo> list, int x, int y, int z) {
    	if (worldObj.getBlockId(x, y, z) != Block.chest.blockID) {
    		return;
    	} else {
    		IInventory inventory = (IInventory)worldObj.getBlockTileEntity(x, y, z);
    		if (worldObj.getBlockId(x - 1, y, z) == Block.chest.blockID) {
                inventory = new InventoryLargeChest("container.chestDouble", (TileEntityChest)worldObj.getBlockTileEntity(x - 1, y, z), inventory);
            }

            if (worldObj.getBlockId(x + 1, y, z) == Block.chest.blockID) {
                inventory = new InventoryLargeChest("container.chestDouble", inventory, (TileEntityChest)worldObj.getBlockTileEntity(x + 1, y, z));
            }

            if (worldObj.getBlockId(x, y, z - 1) == Block.chest.blockID) {
                inventory = new InventoryLargeChest("container.chestDouble", (TileEntityChest)worldObj.getBlockTileEntity(x, y, z - 1), inventory);
            }

            if (worldObj.getBlockId(x, y, z + 1) == Block.chest.blockID) {
                inventory = new InventoryLargeChest("container.chestDouble", inventory, (TileEntityChest)worldObj.getBlockTileEntity(x, y, z + 1));
            }
            list.add(new InventorySearchInfo(inventory));
    	}
    }
    
    @Override
    public int getProcessTime() {
    	return processTimeLeft;
    }

    @Override
	public void setProcessTime(int time) {
		processTimeLeft = time;
	}

    @Override
	public int getProcessDuration() {
		return processDuration;
	}

    @Override
	public void setProcessDuration(int time) {
		processDuration = time;		
	}
	
	/* photo source part */
	@Override
	public void startViewing(EntityPlayer player) {
		viewingPlayer = player.username;
		worldObj.markBlockNeedsUpdate(xCoord, yCoord, zCoord);
	}
	
	@Override
	public void stopViewing(EntityPlayer player) {
		if (player.username.equals(viewingPlayer)) {
			viewingPlayer = null;
		}
		worldObj.markBlockNeedsUpdate(xCoord, yCoord, zCoord);
	}
	
	@Override
	public void nameChanged(int photoIndex, String newName) {
		setInventorySlotContents(0, ItemPhotoStorage.renamePhoto(getStackInSlot(0), photoIndex, newName));
	}

	@Override
	public boolean canViewPhotos(EntityPlayer player) {
		return viewingPlayer == null || viewingPlayer.equals(player.username);
	}

	@Override
	public PhotoInformation getPhotoInformation(String photoId) {
		return ItemPhotoStorage.getPhotoWithID(getStackInSlot(0), photoId);
	}

	@Override
	public void deletePhoto(int photoIndex) {
		setInventorySlotContents(0, ItemPhotoStorage.deletePhoto(getStackInSlot(0), photoIndex));
	}

	@Override
	public boolean canDelete() {
		return getStackInSlot(0).getItem() == CameraCraftItem.memoryCard;
	}

	@Override
	public boolean canPrint() {
		return true;
	}

	@Override
	public void addToPrintQueue(List<PhotoSizeAmountInfo> photos) {
		int timeNeededForJobs = 0;
		
		if (getStackInSlot(0) != null) {
			for (PhotoSizeAmountInfo info : photos) {
				PhotoInformation job = ItemPhotoStorage.getPhotoWithIndex(getStackInSlot(0), info.getPhotoIndex());
				if (job != null) {	
					printJobs.add(job.setAmount(info.getAmount()).setSizeX(info.getSizeX()).setSizeY(info.getSizeY()));
					timeNeededForJobs += getTimeForJob(job);
				}
			}
		}
		
		processDuration += timeNeededForJobs;
		processTimeLeft += timeNeededForJobs;
	}

	@Override
	public PhotoInformation getPhotoInformation(int index) {
		return ItemPhotoStorage.getPhotoWithIndex(getStackInSlot(0), index);
	}

	@Override
	public int numPhotos() {
		return ItemPhotoStorage.getNumPhotos(getStackInSlot(0));
	}

	@Override
	public GuiScreens getScreenTypeViewPhotos() {
		return GuiScreens.VIEW_PHOTOS_TILE_ENTITY;
	}

	@Override
	public int getX() {
		return xCoord;
	}

	@Override
	public int getY() {
		return yCoord;
	}

	@Override
	public int getZ() {
		return zCoord;
	}
	
	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbt = new NBTTagCompound();
		if (viewingPlayer != null) {
			nbt.setString("p", viewingPlayer);
		}
		return new Packet132TileEntityData(xCoord, yCoord, zCoord, 0, nbt);
	}
	
	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt) {
		if (pkt.actionType == 0) {
			viewingPlayer = pkt.customParam1.hasKey("p") ? pkt.customParam1.getString("p") : null;
		}
	}
}
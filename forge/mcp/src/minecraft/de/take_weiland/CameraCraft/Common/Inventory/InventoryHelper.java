package de.take_weiland.CameraCraft.Common.Inventory;

import java.util.Random;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import de.take_weiland.CameraCraft.Common.Gui.GenericContainer;

public class InventoryHelper {
	
	private static Random rand = new Random();
	
	public static ItemStack genericStackDecrease(IInventory inventory, int slot, int numDecrease) {
		ItemStack slotContents = inventory.getStackInSlot(slot);
		
		if (slotContents == null) {
			return null;
		} else {
			if (slotContents.stackSize <= numDecrease) {
                inventory.setInventorySlotContents(slot, null);
                inventory.onInventoryChanged();
                return slotContents;
            }

            ItemStack tempItemStack = slotContents.splitStack(numDecrease);

            if (slotContents.stackSize == 0) {
            	inventory.setInventorySlotContents(slot, null);
            } else {
            	inventory.setInventorySlotContents(slot, slotContents);
            }
            
            inventory.onInventoryChanged();
            return tempItemStack;
		}
	}
	
	public static void genericNBTWrite(NBTTagCompound nbt, ItemStack[] contents) {
		NBTTagList contentList = new NBTTagList();

        for (int currentSlot = 0; currentSlot < contents.length; ++currentSlot)
        {
            if (contents[currentSlot] != null)
            {
                NBTTagCompound slotCompound = new NBTTagCompound();
                slotCompound.setByte("Slot", (byte)currentSlot);
                contents[currentSlot].writeToNBT(slotCompound);
                contentList.appendTag(slotCompound);
            }
        }
        nbt.setTag("Items", contentList);
	}
	
	public static ItemStack[] genericNBTRead(NBTTagCompound nbt, int numStacks) {
		NBTTagList var2 = nbt.getTagList("Items");
        ItemStack[] contents = new ItemStack[numStacks];
        
        for (int i = 0; i < var2.tagCount(); ++i) {
            NBTTagCompound slotTag = (NBTTagCompound)var2.tagAt(i);
            int slot = slotTag.getByte("Slot");

            if (slot >= 0 && slot < numStacks) {
                contents[slot] = ItemStack.loadItemStackFromNBT(slotTag);
            }
        }
        return contents;
	}
	
	public static int getSlotThatHasItem(IInventory inv, int itemId) {
        for (int slotCheck = 0; slotCheck < inv.getSizeInventory(); ++slotCheck) {
            if (inv.getStackInSlot(slotCheck) != null && inv.getStackInSlot(slotCheck).itemID == itemId) {
                return slotCheck;
            }
        }
        return -1;
    }
	
	public static void addPlayerInventoryToContainer(GenericContainer container, IInventory inventoryPlayer, int xStart, int yStart) {
		for (int j = 0; j < 3; j++)
        {
            for (int i1 = 0; i1 < 9; i1++)
            {
                container.addSlotToContainer(new Slot(inventoryPlayer, i1 + j * 9 + 9, xStart + i1 * 18, yStart + j * 18));
            }
        }

        for (int k = 0; k < 9; k++)
        {
            container.addSlotToContainer(new Slot(inventoryPlayer, k, xStart + k * 18, yStart + 58));
        }
	}
	
	public static void genericContainerRemoval(World world, int x, int y, int z)  {
		if (world.isRemote) {
			return;
		}
		
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		
		if (!(tileEntity instanceof IInventory) || tileEntity == null) {
			return;
		}
		
		IInventory inventory = (IInventory)tileEntity;

        for (int currentSlot = 0; currentSlot < inventory.getSizeInventory(); ++currentSlot) {
        	ItemStack currentDroppingStack = inventory.getStackInSlot(currentSlot);

        	if (currentDroppingStack != null) {
        		float var10 = rand.nextFloat() * 0.8F + 0.1F;
        		float var11 = rand.nextFloat() * 0.8F + 0.1F;
        		float var12 = rand.nextFloat() * 0.8F + 0.1F;

        		while (currentDroppingStack.stackSize > 0) {
        			int partialStackSize = rand.nextInt(21) + 10;

        			if (partialStackSize > currentDroppingStack.stackSize) {
        				partialStackSize = currentDroppingStack.stackSize;
        			}

        			currentDroppingStack.stackSize -= partialStackSize;
        			EntityItem entityItem = new EntityItem(world, (double)((float)x + var10), (double)((float)y + var11), (double)((float)z + var12), new ItemStack(currentDroppingStack.itemID, partialStackSize, currentDroppingStack.getItemDamage()));

        			if (currentDroppingStack.hasTagCompound()) {
        				entityItem.func_92014_d().setTagCompound((NBTTagCompound)currentDroppingStack.getTagCompound().copy());
        			}

        			float motionMultiplier = 0.05F;
        			entityItem.motionX = (double)((float)rand.nextGaussian() * motionMultiplier);
        			entityItem.motionY = (double)((float)rand.nextGaussian() * motionMultiplier + 0.2F);
        			entityItem.motionZ = (double)((float)rand.nextGaussian() * motionMultiplier);
        			world.spawnEntityInWorld(entityItem);
        		}
        	}
        }
	}
	
	public static void putStackInFirstFreeSlotOrDrop(ItemStack stack, World world, int x, int y, int z, InventorySearchInfo... inventoryInfos) {
		
		for (InventorySearchInfo search : inventoryInfos) {
			for (int i = search.searchStart; i < search.searchEnd; i++) {
				if (search.inv.getStackInSlot(i) == null) {
					search.inv.setInventorySlotContents(i, stack);
					return;
				}
			}
		}
		
		EntityItem item = new EntityItem(world, x, y + 1.5, z, stack);
		world.spawnEntityInWorld(item);
	}
	
	public static int findItemAmountInInventories(Item item, InventorySearchInfo... inventories) {
		return findItemAmountInInventories(new ItemStack(item), inventories);
	}
	
	public static int findItemAmountInInventories(ItemStack itemStack, InventorySearchInfo... inventories) {
		int found = 0;
		for (InventorySearchInfo search : inventories) {
			for (int i = search.searchStart; i < search.searchEnd; i++) {
				ItemStack stack = search.inv.getStackInSlot(i); 
				if (stack != null && stack.isItemEqual(itemStack)) {
					found += stack.stackSize;
				}
			}
		}
		return found;
	}
	
	public static void consumeItemFromInventories(Item item, int consumeAmount, InventorySearchInfo... inventories) {
		consumeItemFromInventories(new ItemStack(item), consumeAmount, inventories);
	}
	
	public static void consumeItemFromInventories(ItemStack itemStack, int consumeAmount, InventorySearchInfo... inventories) {
		for (InventorySearchInfo search : inventories) {
			for (int i = search.searchStart; i < search.searchEnd; i++) {
				ItemStack stack = search.inv.getStackInSlot(i); 
				if (stack != null && stack.isItemEqual(itemStack)) {
					int consumeThis = Math.min(consumeAmount, stack.stackSize);
					stack.stackSize -= consumeThis;
					if (stack.stackSize == 0) {
						search.inv.setInventorySlotContents(i, null);
					}
					consumeAmount -= consumeThis;
					if (consumeAmount == 0) {
						return;
					}
				}
			}
		}
	}
	
	public static class InventorySearchInfo {
		public IInventory inv;
		public int searchStart;
		public int searchEnd;
		public InventorySearchInfo(IInventory inv) {
			this.inv = inv;
			searchStart = 0;
			searchEnd = inv.getSizeInventory();
		}
		
		public InventorySearchInfo(IInventory inv, int searchStart, int searchEnd) {
			this.inv = inv;
			this.searchStart = searchStart;
			this.searchEnd = searchEnd;
		}
	}
	
	public static int findItemOnce(Item item, InventoryCrafting crafting) {
		int found = -1;
		for (int i = 0; i < crafting.getSizeInventory(); i++) {
			if (crafting.getStackInSlot(i) != null && crafting.getStackInSlot(i).getItem().shiftedIndex == item.shiftedIndex) {
				if (found != -1) {
					return -1;
				} else {
					found = i;
				}
			}
		}
		return found;
	}
	
	public static int getSlotsOccupied(InventoryCrafting crafting) {
		int numSlots = 0;
		for (int i = 0; i < crafting.getSizeInventory(); i++) {
			if (crafting.getStackInSlot(i) != null) {
				numSlots++;
			}
		}
		return numSlots;
	}
}
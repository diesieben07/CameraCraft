package de.take_weiland.CameraCraft.Common.TileEntities;

import de.take_weiland.CameraCraft.Common.Inventory.InventoryHelper;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;

public abstract class TileEntityInventory extends TileEntity implements IInventory {

	protected ItemStack[] contents;
	
	public TileEntityInventory() {
		contents = new ItemStack[getSizeInventory()];
	}
	
	@Override
	public ItemStack getStackInSlot(int slot) {
		return slot < contents.length ? contents[slot] : null;
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
		if (slot < contents.length) {
			contents[slot] = stack;
		}
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}
	
	@Override
	public String getInvName() {
		return getBlockType().getBlockName() + ".name";
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this ? false : player.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void openChest() { }

	@Override
	public void closeChest() { }

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        contents = InventoryHelper.genericNBTRead(nbt, getSizeInventory());
	}
	
	@Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        InventoryHelper.genericNBTWrite(nbt, contents);
	}
}

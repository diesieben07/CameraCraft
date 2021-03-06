package de.take_weiland.CameraCraft.Common.Gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.take_weiland.CameraCraft.Common.Inventory.IProgressInventory;

public abstract class GenericContainer extends Container {
	
	protected IInventory upperInventory;
	protected InventoryPlayer playerInventory;
	
	private int lastProcessTime;
	private int lastDuration;
	
	public GenericContainer(IInventory upperInventory, InventoryPlayer playerInventory) {
		this.upperInventory = upperInventory;
		this.playerInventory = playerInventory;
	}
	
	/**
	 * used to determine the Background Texture of this Container
	 * @return a String that defines the Background Texture of this Container, null if no texture should be used
	 */
	public abstract String getGuiBackgroundTexture();
	
	public final IInventory getPlayerInventory() {
		return playerInventory;
	}
	
	public final IInventory getUpperInventory() {
		return upperInventory;
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
        // TODO
		return null;
    }
	
	@Override
	public void updateCraftingResults() {
		super.updateCraftingResults();
		if (upperInventory != null && upperInventory instanceof IProgressInventory) {    
			IProgressInventory progressInventory = (IProgressInventory)upperInventory;
			for (Object crafter : crafters) {
	        	ICrafting iCraftingCrafter = (ICrafting)crafter;
	        	if (lastProcessTime != progressInventory.getProcessTime()) {
	        		iCraftingCrafter.sendProgressBarUpdate(this, 0, progressInventory.getProcessTime());
	        		lastProcessTime = progressInventory.getProcessTime();
	        	}
	        	
	        	if (lastDuration != progressInventory.getProcessDuration()) {
	        		iCraftingCrafter.sendProgressBarUpdate(this, 1, progressInventory.getProcessDuration());
	        		lastDuration = progressInventory.getProcessDuration();
	        	}
	        }
	    }
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public void updateProgressBar(int barID, int value) {
		if (upperInventory != null && upperInventory instanceof IProgressInventory) {
			IProgressInventory progressInventory = (IProgressInventory)upperInventory;
			switch (barID) {
			case 0:
				progressInventory.setProcessTime(value);
				break;
			case 1:
				progressInventory.setProcessDuration(value);
				break;
			}
		}
	}

	@Override
	public void addCraftingToCrafters(ICrafting crafter) {
		super.addCraftingToCrafters(crafter);
		if (upperInventory != null && upperInventory instanceof IProgressInventory) {
			IProgressInventory progressInventory = (IProgressInventory)upperInventory;
	        crafter.sendProgressBarUpdate(this, 0, progressInventory.getProcessTime());
	        crafter.sendProgressBarUpdate(this, 1, progressInventory.getProcessDuration());
		}
    }
	
	/**
	 * to be overridden if upperInventory is not used
	 */
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return upperInventory != null ? upperInventory.isUseableByPlayer(player) : false;
	}
	
	// overridden for public access (InventoryHelper)
	@Override
	public Slot addSlotToContainer(Slot slot) {
		return super.addSlotToContainer(slot);
	}
}
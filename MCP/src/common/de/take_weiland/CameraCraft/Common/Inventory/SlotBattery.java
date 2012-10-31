package de.take_weiland.CameraCraft.Common.Inventory;

import de.take_weiland.CameraCraft.Common.IndustrialCraftManager;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;

public class SlotBattery extends SlotForItem {

	public SlotBattery(IInventory inventory, int id, int x, int y, Object... validItems) {
		super(inventory, id, x, y, validItems);
	}
	
	public boolean isItemValid(ItemStack itemStack) {
		return super.isItemValid(itemStack) || IndustrialCraftManager.canItemProvideEnergy(itemStack);
	}
}
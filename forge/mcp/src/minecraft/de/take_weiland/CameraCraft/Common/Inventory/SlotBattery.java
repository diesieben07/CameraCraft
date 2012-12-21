package de.take_weiland.CameraCraft.Common.Inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import de.take_weiland.CameraCraft.Common.IndustrialCraftManager;

public class SlotBattery extends SlotForItem {

	public SlotBattery(IInventory inventory, int id, int x, int y, Object... validItems) {
		super(inventory, id, x, y, validItems);
	}
	
	public boolean isItemValid(ItemStack itemStack) {
		return super.isItemValid(itemStack) || IndustrialCraftManager.canItemProvideEnergy(itemStack);
	}
}
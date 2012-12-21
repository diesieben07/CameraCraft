package de.take_weiland.CameraCraft.Common.Inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SlotForItem extends Slot {

	private final HashMap<Item,List<Integer>> validItems = new HashMap<Item,List<Integer>>();
	
	
	public SlotForItem(IInventory inventory, int id, int x, int y, Object... validItems) {
		super(inventory, id, x, y);
		List<Integer> currentList = null;
		Item currentItem = null;
		for (Object param : validItems) {
			if (currentItem != null && param instanceof Integer) {
				currentList.add((Integer)param);
			} else if (param instanceof Item) {
				currentItem = (Item)param;
				currentList = new ArrayList<Integer>();
				this.validItems.put(currentItem, currentList);
			}
		}
	}
	
	@Override
	public boolean isItemValid(ItemStack itemStack) {
		List<Integer> tempDamages = validItems.get(itemStack.getItem());
		return validItems.containsKey(itemStack.getItem()) && (tempDamages.isEmpty() || tempDamages.contains(itemStack.getItemDamage()));
    }
}
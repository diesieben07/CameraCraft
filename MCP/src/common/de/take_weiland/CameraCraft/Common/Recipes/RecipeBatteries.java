package de.take_weiland.CameraCraft.Common.Recipes;

import de.take_weiland.CameraCraft.Common.Inventory.InventoryHelper;
import de.take_weiland.CameraCraft.Common.Items.CameraCraftItem;
import net.minecraft.src.IRecipe;
import net.minecraft.src.InventoryCrafting;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class RecipeBatteries implements IRecipe {

	@Override
	public boolean matches(InventoryCrafting crafting, World world) {
		int batteryIndex = InventoryHelper.findItemOnce(CameraCraftItem.battery, crafting);
		return batteryIndex != -1 && crafting.getStackInSlot(batteryIndex).getItemDamage() < 4 && InventoryHelper.findItemOnce(Item.redstone, crafting) != -1 && InventoryHelper.getSlotsOccupied(crafting) == 2; 
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting crafting) {
		ItemStack stack = crafting.getStackInSlot(InventoryHelper.findItemOnce(CameraCraftItem.battery, crafting));
		ItemStack result = new ItemStack(CameraCraftItem.battery, 1, stack.getItemDamage() + 1);
		return result;
	}

	@Override
	public int getRecipeSize() {
		return 1;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(CameraCraftItem.battery);
	}
}
package de.take_weiland.CameraCraft.Common.Recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import de.take_weiland.CameraCraft.Common.Inventory.InventoryHelper;
import de.take_weiland.CameraCraft.Common.Items.CameraCraftItem;

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
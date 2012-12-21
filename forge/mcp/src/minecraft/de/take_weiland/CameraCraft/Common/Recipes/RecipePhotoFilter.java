package de.take_weiland.CameraCraft.Common.Recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import de.take_weiland.CameraCraft.Common.FilterType;
import de.take_weiland.CameraCraft.Common.Inventory.InventoryHelper;
import de.take_weiland.CameraCraft.Common.Items.CameraCraftItem;

public class RecipePhotoFilter implements IRecipe {

	@Override
	public boolean matches(InventoryCrafting crafting, World world) {
		int dyeIndex = InventoryHelper.findItemOnce(Item.dyePowder, crafting);
		boolean dyeValid = false;
		if (dyeIndex != -1) {
			int damage = crafting.getStackInSlot(dyeIndex).getItemDamage();
			dyeValid = damage == 1 || damage == 2 || damage == 4 || damage == 11;
		}
		int filterIndex = InventoryHelper.findItemOnce(CameraCraftItem.photoFilter, crafting);
		return InventoryHelper.getSlotsOccupied(crafting) == 2 && dyeValid && filterIndex != -1 && crafting.getStackInSlot(filterIndex).getItemDamage() == 0;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting crafting) {
		int dyeIndex = InventoryHelper.findItemOnce(Item.dyePowder, crafting);
		return new ItemStack(CameraCraftItem.photoFilter, 1, FilterType.fromDyeDamage(crafting.getStackInSlot(dyeIndex).getItemDamage()).toItemDamage());
	}

	@Override
	public int getRecipeSize() {
		return 1;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(CameraCraftItem.photoFilter);
	}
}

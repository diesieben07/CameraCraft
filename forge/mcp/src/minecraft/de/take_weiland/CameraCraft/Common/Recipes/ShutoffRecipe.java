package de.take_weiland.CameraCraft.Common.Recipes;

import net.minecraft.src.IRecipe;
import net.minecraft.src.InventoryCrafting;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class ShutoffRecipe implements IRecipe {

	private IRecipe parentRecipe;
	private static boolean isEnabled = false;
	public boolean forceDisable = false;
	
	public ShutoffRecipe(IRecipe parentRecipe) {
		this.parentRecipe = parentRecipe;
	}
	
	@Override
	public boolean matches(InventoryCrafting crafting, World world) {
		return isEnabled && !forceDisable && parentRecipe.matches(crafting, world);
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting crafting) {
		return parentRecipe.getCraftingResult(crafting);
	}

	@Override
	public int getRecipeSize() {
		return parentRecipe.getRecipeSize();
	}

	@Override
	public ItemStack getRecipeOutput() {
		return parentRecipe.getRecipeOutput();
	}

	public static void enable() {
		isEnabled = true;
	}
	
	public static void disable() {
		isEnabled = false;
	}
}

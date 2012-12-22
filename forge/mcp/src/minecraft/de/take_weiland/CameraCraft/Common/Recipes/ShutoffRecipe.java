package de.take_weiland.CameraCraft.Common.Recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class ShutoffRecipe implements IRecipe {

	private IRecipe parentRecipe;
	private static boolean isEnabled = false;
	public boolean forceDisable = false;
	
	public ShutoffRecipe(IRecipe parentRecipe) {
		this.parentRecipe = parentRecipe;
	}
	
	public IRecipe parent() {
		return parentRecipe;
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
		return isEnabled && !forceDisable ? parentRecipe.getRecipeOutput() : null;
	}

	public static void enable() {
		isEnabled = true;
	}
	
	public static void disable() {
		isEnabled = false;
	}
	
	public static boolean enabled() {
		return isEnabled;
	}
}

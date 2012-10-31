package de.take_weiland.CameraCraft.Common.Recipes;

import de.take_weiland.CameraCraft.Common.Items.CameraCraftItem;
import net.minecraft.src.IRecipe;
import net.minecraft.src.InventoryCrafting;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;

public class RecipePhotoToPoster implements IRecipe {
	
	@Override
	public boolean matches(InventoryCrafting crafting, World world) {
		if (crafting.getSizeInventory() != 9) {
			return false;
		}
		
		for (int i = 0; i < 9; i++) {
			if (crafting.getStackInSlot(i) == null || i != 4 && crafting.getStackInSlot(i).getItem().shiftedIndex != Item.stick.shiftedIndex) {
				return false;
			}
		}
		return crafting.getStackInSlot(4).getItem() == CameraCraftItem.photo && crafting.getStackInSlot(4).getItemDamage() == 0 && crafting.getStackInSlot(4).stackTagCompound != null; 
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting crafting) {
		ItemStack result = new ItemStack(CameraCraftItem.photo, 1, 1);
		result.stackTagCompound = (NBTTagCompound) crafting.getStackInSlot(4).stackTagCompound.copy();
		return result;
	}

	@Override
	public int getRecipeSize() {
		return 1;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(CameraCraftItem.photo, 1, 1);
	}
}
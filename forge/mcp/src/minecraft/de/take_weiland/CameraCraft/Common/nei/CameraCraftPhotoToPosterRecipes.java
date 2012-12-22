package de.take_weiland.CameraCraft.Common.nei;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import codechicken.nei.recipe.ShapedRecipeHandler;
import de.take_weiland.CameraCraft.Common.Items.CameraCraftItem;

public class CameraCraftPhotoToPosterRecipes extends ShapedRecipeHandler {

	@Override
	public String getRecipeName() {
		return "CameraCraft";
	}

	@Override
	public void loadCraftingRecipes(ItemStack result) {
		int damage = result.getItemDamage();
		if (result.itemID == CameraCraftItem.photo.shiftedIndex && damage == 1 && result.hasTagCompound()) {
			ItemStack inPhoto = new ItemStack(CameraCraftItem.photo, 1, 0);
			inPhoto.stackTagCompound = (NBTTagCompound)result.stackTagCompound;
			ItemStack stick = new ItemStack(Item.stick);
			ItemStack[] in = new ItemStack[] {
				stick, stick, stick,
				stick, inPhoto, stick,
				stick, stick, stick
			};
			arecipes.add(new CachedShapedRecipe(3, 3, in, result.copy()));
		}
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient) {
		int damage = ingredient.getItemDamage();
		if (ingredient.itemID == CameraCraftItem.photo.shiftedIndex && damage == 0 && ingredient.hasTagCompound()) {
			ItemStack out = new ItemStack(CameraCraftItem.photo, 1, 1);
			out.stackTagCompound = (NBTTagCompound)ingredient.stackTagCompound.copy();
			ItemStack stick = new ItemStack(Item.stick);
			ItemStack[] in = new ItemStack[] {
				stick, stick, stick,
				stick, ingredient.copy(), stick,
				stick, stick, stick
			};
			arecipes.add(new CachedShapedRecipe(3, 3, in, out));
		}
	}
}
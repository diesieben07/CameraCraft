package de.take_weiland.CameraCraft.Common.nei;

import java.util.Arrays;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import codechicken.nei.recipe.ShapelessRecipeHandler;
import codechicken.nei.recipe.ShapedRecipeHandler.CachedShapedRecipe;
import de.take_weiland.CameraCraft.Common.PhotoInformation;
import de.take_weiland.CameraCraft.Common.Items.CameraCraftItem;
import de.take_weiland.CameraCraft.Common.Items.ItemPhotoStorage;

public class CameraCraftTeleportationRecipes extends ShapelessRecipeHandler {

	@Override
	public String getRecipeName() {
		return "CameraCraft";
	}

	@Override
	public void loadCraftingRecipes(ItemStack result) {
		int damage = result.getItemDamage();
		if (result.itemID == CameraCraftItem.photo.shiftedIndex && damage < 2 && result.hasTagCompound()) {
			ItemStack in = result.copy();
			PhotoInformation info = ItemPhotoStorage.getFirstPhoto(in).unsetTeleport();
			in = ItemPhotoStorage.addPhoto(ItemPhotoStorage.deletePhoto(in, 0), info);
			arecipes.add(new CachedShapelessRecipe(Arrays.asList(in, new ItemStack(Item.enderPearl)), result.copy()));
		}
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient) {
		int damage = ingredient.getItemDamage();
		if (ingredient.itemID == CameraCraftItem.photo.shiftedIndex && damage < 2 && ingredient.hasTagCompound()) {
			ItemStack out = ingredient.copy();
			PhotoInformation info = ItemPhotoStorage.getFirstPhoto(out).setTeleport();
			out = ItemPhotoStorage.addPhoto(ItemPhotoStorage.deletePhoto(out, 0), info);
			arecipes.add(new CachedShapelessRecipe(Arrays.asList(ingredient.copy(), new ItemStack(Item.enderPearl)), out));
		}
	}
}
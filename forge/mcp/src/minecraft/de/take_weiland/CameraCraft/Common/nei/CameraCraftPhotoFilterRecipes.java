package de.take_weiland.CameraCraft.Common.nei;

import java.util.Arrays;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import codechicken.nei.recipe.ShapelessRecipeHandler;
import codechicken.nei.recipe.ShapelessRecipeHandler.CachedShapelessRecipe;
import de.take_weiland.CameraCraft.Common.FilterType;
import de.take_weiland.CameraCraft.Common.Items.CameraCraftItem;

public class CameraCraftPhotoFilterRecipes extends ShapelessRecipeHandler {

	@Override
	public String getRecipeName() {
		return "CameraCraft";
	}

	@Override
	public void loadCraftingRecipes(String outputId, Object... results) {
		if (!outputId.equals("crafting")) {
			super.loadCraftingRecipes(outputId, results);
			return;
		}
		for (FilterType type : FilterType.values()) {
			if (type != FilterType.TRANSPARENT) {
				arecipes.add(new CachedShapelessRecipe(Arrays.asList(new ItemStack(CameraCraftItem.photoFilter, 1, FilterType.TRANSPARENT.toItemDamage()), new ItemStack(Item.dyePowder, 1, type.toDyeDamage())), new ItemStack(CameraCraftItem.photoFilter, 1, type.toItemDamage())));
			}
		}
	}

	@Override
	public void loadCraftingRecipes(ItemStack result) {
		int damage = result.getItemDamage();
		FilterType type = FilterType.fromItemDamage(damage);
		if (result.itemID == CameraCraftItem.photoFilter.shiftedIndex && type != FilterType.TRANSPARENT) {
			arecipes.add(new CachedShapelessRecipe(Arrays.asList(new ItemStack(CameraCraftItem.photoFilter, 1, FilterType.TRANSPARENT.toItemDamage()), new ItemStack(Item.dyePowder, 1, type.toDyeDamage())), result.copy()));
		}
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient) {
		int damage = ingredient.getItemDamage();
		if (ingredient.itemID == CameraCraftItem.photoFilter.shiftedIndex) {
			FilterType type = FilterType.fromItemDamage(damage);
			if (type == FilterType.TRANSPARENT) {
				loadCraftingRecipes("crafting");
			}
		} else if (ingredient.itemID == Item.dyePowder.shiftedIndex) {
			FilterType type = FilterType.fromDyeDamage(damage);
			if (type != FilterType.TRANSPARENT) {
				loadCraftingRecipes(new ItemStack(CameraCraftItem.photoFilter, 1, type.toItemDamage()));
			}
		}
	}

}

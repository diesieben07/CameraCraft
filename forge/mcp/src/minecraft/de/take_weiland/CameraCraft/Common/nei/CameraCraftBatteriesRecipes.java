package de.take_weiland.CameraCraft.Common.nei;

import java.util.Arrays;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import codechicken.nei.recipe.ShapelessRecipeHandler;
import de.take_weiland.CameraCraft.Common.Items.CameraCraftItem;

public class CameraCraftBatteriesRecipes extends ShapelessRecipeHandler {

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
		for (int i = 0; i < 4; i++) {
			arecipes.add(new CachedShapelessRecipe(Arrays.asList(new ItemStack(CameraCraftItem.battery, 1, i), new ItemStack(Item.redstone)), new ItemStack(CameraCraftItem.battery, 1, i + 1)));
		}
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient) {
		int damage = ingredient.getItemDamage();
		if (ingredient.itemID == Item.redstone.shiftedIndex) {
			loadCraftingRecipes("crafting");
		} else if (ingredient.itemID == CameraCraftItem.battery.shiftedIndex && damage < 4) {
			loadCraftingRecipes(new ItemStack(CameraCraftItem.battery, 1, damage + 1));
		}
	}

	@Override
	public void loadCraftingRecipes(ItemStack result) {
		int damage = result.getItemDamage();
		if (result.itemID == CameraCraftItem.battery.shiftedIndex && damage <= 4 && damage > 0) {
			arecipes.add(new CachedShapelessRecipe(Arrays.asList(new ItemStack(CameraCraftItem.battery, 1, damage - 1), new ItemStack(Item.redstone)), result.copy()));
		}
	}
}
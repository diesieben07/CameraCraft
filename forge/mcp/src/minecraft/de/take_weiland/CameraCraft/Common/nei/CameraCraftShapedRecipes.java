package de.take_weiland.CameraCraft.Common.nei;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import codechicken.nei.NEIClientUtils;
import codechicken.nei.NEICompatibility;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.RecipeInfo;
import codechicken.nei.recipe.ShapedRecipeHandler;
import codechicken.nei.recipe.weakDependancy_Forge;
import codechicken.nei.recipe.ShapedRecipeHandler.CachedShapedRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import de.take_weiland.CameraCraft.Common.Recipes.ShutoffRecipe;

public class CameraCraftShapedRecipes extends ShapedRecipeHandler {

	@Override
	public String getRecipeName() {
		return "CameraCraft";
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient) {
		List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
		for (IRecipe recipe : recipes) {
			if (recipe instanceof ShutoffRecipe && ShutoffRecipe.enabled()) {
				ShutoffRecipe soRecipe = (ShutoffRecipe)recipe;
				if (!soRecipe.forceDisable) {
					IRecipe parent = soRecipe.parent();
					if (parent instanceof ShapedRecipes) {
						CachedShapedRecipe cachedRecipe = new CachedShapedRecipe((ShapedRecipes)parent);
						if (cachedRecipe.contains(cachedRecipe.ingredients, ingredient)) {
							cachedRecipe.setIngredientPermutation(cachedRecipe.ingredients, ingredient);
							arecipes.add(cachedRecipe);
						}
					}
				}
			}
		}
	}

	@Override
	public void loadCraftingRecipes(String outputId, Object... results) {
		if (outputId.equals("crafting")) {
			List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
			for (IRecipe recipe : recipes) {
				if (recipe instanceof ShutoffRecipe && ShutoffRecipe.enabled()) {
					ShutoffRecipe soRecipe = (ShutoffRecipe)recipe;
					if (!soRecipe.forceDisable) {
						IRecipe parent = soRecipe.parent();
						if (parent instanceof ShapedRecipes) {
							arecipes.add(new CachedShapedRecipe((ShapedRecipes)parent));
						}
					}
				}
			}
		} else {
			super.loadCraftingRecipes(outputId, results);
		}
	}

	@Override
	public void loadCraftingRecipes(ItemStack result) {
		List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
		for(IRecipe recipe : recipes) {
			if(NEIClientUtils.areStacksSameTypeCrafting(recipe.getRecipeOutput(), result)) {
				CachedShapedRecipe cachedRecipe = null;
				if (recipe instanceof ShutoffRecipe) {
					IRecipe parent = ((ShutoffRecipe)recipe).parent();
					if (parent instanceof ShapedRecipes) {
						arecipes.add(new CachedShapedRecipe((ShapedRecipes)parent));
					}
				}	
			}
		}
	}

	@Override
	public boolean hasOverlay(GuiContainer gui, Container container, int recipe) {
		return RecipeInfo.hasDefaultOverlay(gui, getOverlayIdentifier()) || RecipeInfo.hasOverlayHandler(gui, getOverlayIdentifier());
	}
}
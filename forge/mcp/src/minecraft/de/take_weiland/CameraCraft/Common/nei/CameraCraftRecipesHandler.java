/*package de.take_weiland.CameraCraft.Common.nei;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.ShapedRecipeHandler.CachedShapedRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import de.take_weiland.CameraCraft.Common.Recipes.ShutoffRecipe;

public class CameraCraftRecipesHandler extends TemplateRecipeHandler {

	public class CachedCameraCraftShapedRecipe extends CachedShapedRecipe {

		private ShutoffRecipe recipe;
		private PositionedStack result;
		private ArrayList<PositionedStack> ingredients;
		
		public CachedCameraCraftShapedRecipe(ShutoffRecipe recipe) {
//			super(
		}

		@Override
		public PositionedStack getResult() {
			return result;
		}

		@Override
		public ArrayList getIngredients() {
			return ingredients;
		}
		
	}
	
	@Override
	public Class getGuiClass() {
		return GuiCrafting.class;
	}

	@Override
	public String getOverlayIdentifier() {
		return "crafting";
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

	public void loadCraftingRecipes(ItemStack result) {
		
	}

	@Override
	public String getRecipeName() {
		return "Shaped Crafting";
	}

	@Override
	public String getGuiTexture() {
		return "/gui/crafting.png";
	}

}
*/
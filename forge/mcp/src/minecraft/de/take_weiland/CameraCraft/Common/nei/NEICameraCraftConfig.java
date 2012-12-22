package de.take_weiland.CameraCraft.Common.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.recipe.TemplateRecipeHandler;
import de.take_weiland.CameraCraft.Common.CameraCraft;
import de.take_weiland.CameraCraft.Common.Items.CameraCraftItem;

public class NEICameraCraftConfig implements IConfigureNEI {

	@Override
	public void loadConfig() {
		registerHandler(new CameraCraftShapedRecipes());
		registerHandler(new CameraCraftBatteriesRecipes());
		registerHandler(new CameraCraftPhotoFilterRecipes());
		registerHandler(new CameraCraftPhotoToPosterRecipes());
		registerHandler(new CameraCraftTeleportationRecipes());
		
		API.hideItem(CameraCraftItem.photo.shiftedIndex);
	}
	
	private void registerHandler(TemplateRecipeHandler handler) {
		API.registerRecipeHandler(handler);
		API.registerUsageHandler(handler);
	}

	@Override
	public String getName() {
		return "CameraCraft";
	}

	@Override
	public String getVersion() {
		return CameraCraft.VERSION;
	}
}
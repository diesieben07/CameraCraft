package de.take_weiland.CameraCraft.Common.Recipes;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import cpw.mods.fml.common.registry.GameRegistry;
import de.take_weiland.CameraCraft.Common.Blocks.CameraCraftBlock;
import de.take_weiland.CameraCraft.Common.Items.CameraCraftItem;

public class CameraCraftRecipes {
	public static ShutoffRecipe teleportationEnabler;
	
	public static void registerRecipes() {
		
		// standard Camera
		addShutoffRecipe(new ItemStack(CameraCraftItem.camera, 1, 0), new Object[] {
			"III", "IRG", "III", 'I', Item.ingotIron, 'G', Block.glass
		});
		
		// digital camera
		addShutoffRecipe(new ItemStack(CameraCraftItem.camera, 1, 1), new Object[] {
			"III", "IRG", "III", 'I', Item.ingotIron, 'R', Item.redstone, 'G', Block.glass
		});
		
		// black and white film
		addShutoffRecipe(new ItemStack(CameraCraftItem.film, 1, 0), new Object[] {
			"PPP", "XXX", "PPP", 'P', Item.paper
		});
		
		// colored film
		addShutoffRecipe(new ItemStack(CameraCraftItem.film, 1, 1), new Object[] {
			"PPP", "RGB", "PPP", 'P', Item.paper, 'R', new ItemStack(Item.dyePowder, 1, 1), 'G', new ItemStack(Item.dyePowder, 1, 2), 'B', new ItemStack(Item.dyePowder, 1, 4)
		});
		
		// 1 v battery
		addShutoffRecipe(new ItemStack(CameraCraftItem.battery, 1, 0), new Object[] {
			"III", "TRT", "III", 'I', Item.ingotIron, 'T', Block.torchRedstoneActive, 'R', Item.redstoneRepeater
		});
		
		// transparent photo filter		
		addShutoffRecipe(new ItemStack(CameraCraftItem.photoFilter, 4, 0), new Object[] {
			"XGX", "GXG", "XGX", 'G', Block.thinGlass
		});
		
		// Photo processor
		addShutoffRecipe(new ItemStack(CameraCraftBlock.photoProcessor), new Object[] {
			"CCC", "CBR", "CCC", 'C', Block.cobblestone, 'B', Item.bucketEmpty, 'R', Item.redstone
		});
		
		// photo station
		addShutoffRecipe(new ItemStack(CameraCraftBlock.photoStation), new Object[] {
			"CIC", "CRC", "CIC", 'C', Block.cobblestone, 'I', Item.ingotIron, 'R', Item.redstone
		});
		
		// memory card
		addShutoffRecipe(new ItemStack(CameraCraftItem.memoryCard), new Object[] {
			"IRR", "IIR", "III", 'I', Item.ingotIron, 'R', Item.redstone
		});
		
		// tripod
		addShutoffRecipe(new ItemStack(CameraCraftBlock.tripod), new Object[] {
			"XIX", "IXI", "IXI", 'I', Item.ingotIron
		});
		
		// teleportation battery
		addShutoffRecipe(new ItemStack(CameraCraftItem.teleportationBattery), "RPR", "XRX", 'R', Item.redstone, 'P', Item.enderPearl);
		
		// teleporation enabler
		teleportationEnabler = addShutoffRecipe(new ItemStack(CameraCraftBlock.enabler), "III", "IBI", "IRI", 'I', Item.ingotIron, 'R', Item.redstone, 'B', CameraCraftItem.teleportationBattery);
		
		addShutoffRecipe(new RecipeBatteries());
		addShutoffRecipe(new RecipePhotoFilter());
		addShutoffRecipe(new RecipePhotoToPoster());
		addShutoffRecipe(new RecipeTeleportationPhoto());
	}
	
	private static ShutoffRecipe addShutoffRecipe(ItemStack output, Object... recipe) {
		GameRegistry.addRecipe(output, recipe);
		List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
		IRecipe lastRecipe = recipes.remove(recipes.size() - 1);
		ShutoffRecipe shutoffRecipe = new ShutoffRecipe(lastRecipe);
		recipes.add(shutoffRecipe);
		return shutoffRecipe;
	}
	
	private static ShutoffRecipe addShutoffRecipe(IRecipe recipe) {
		ShutoffRecipe shutoffRecipe = new ShutoffRecipe(recipe);
		GameRegistry.addRecipe(shutoffRecipe);
		return shutoffRecipe;
	}
	
	public static void enableRecipesAndItems() {
		ShutoffRecipe.enable();
		CameraCraftItem.enable();
	}
	
	public static void disableRecipesAndItems() {
		ShutoffRecipe.disable();
		CameraCraftItem.disable();
	}
}
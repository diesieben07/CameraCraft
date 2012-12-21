package de.take_weiland.CameraCraft.Common.Recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import de.take_weiland.CameraCraft.Common.PhotoInformation;
import de.take_weiland.CameraCraft.Common.Inventory.InventoryHelper;
import de.take_weiland.CameraCraft.Common.Items.CameraCraftItem;
import de.take_weiland.CameraCraft.Common.Items.ItemPhotoStorage;

public class RecipeTeleportationPhoto implements IRecipe {
	
	@Override
	public boolean matches(InventoryCrafting crafting, World world) {
		int photoIndex = InventoryHelper.findItemOnce(CameraCraftItem.photo, crafting);
		return InventoryHelper.findItemOnce(Item.enderPearl, crafting) != -1 && photoIndex != -1 && InventoryHelper.getSlotsOccupied(crafting) == 2 && !ItemPhotoStorage.getFirstPhoto(crafting.getStackInSlot(photoIndex)).isTeleport();
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting crafting) {
		ItemStack original = crafting.getStackInSlot(InventoryHelper.findItemOnce(CameraCraftItem.photo, crafting)).copy();
		PhotoInformation info = ItemPhotoStorage.getFirstPhoto(original).setTeleport();
		return ItemPhotoStorage.addPhoto(ItemPhotoStorage.deletePhoto(original, 0), info);
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

package de.take_weiland.CameraCraft.Client.Rendering;

import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

public class TeleportationPhotoItemRenderer implements IItemRenderer {

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return false;
		//return type == ItemRenderType.INVENTORY && item.itemID == CameraCraftItem.photo.shiftedIndex && item.getItemDamage() == 1 && ItemPhotoStorage.getFirstPhoto(item).isTeleport();
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		
	}

}

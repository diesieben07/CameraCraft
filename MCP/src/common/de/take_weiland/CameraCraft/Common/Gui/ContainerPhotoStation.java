package de.take_weiland.CameraCraft.Common.Gui;

import de.take_weiland.CameraCraft.Common.Inventory.InventoryHelper;
import de.take_weiland.CameraCraft.Common.Inventory.SlotForItem;
import de.take_weiland.CameraCraft.Common.Items.CameraCraftItem;
import de.take_weiland.CameraCraft.Common.TileEntities.TileEntityPhotoStation;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ICrafting;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.Item;

public class ContainerPhotoStation extends GenericContainer {
	
	public ContainerPhotoStation(TileEntityPhotoStation tileEntity, InventoryPlayer playerInventory) {
		super(tileEntity, playerInventory);
		addSlotToContainer(new SlotForItem(upperInventory, 0, 121, 31, CameraCraftItem.film, 2, 3, CameraCraftItem.memoryCard));
		
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				addSlotToContainer(new SlotForItem(upperInventory, (y * 3) + x + 1, 17 + 18 * x, 16 + 18 * y, Item.paper, Item.dyePowder, 6, 11, 13));
			}
		}
		
		InventoryHelper.addPlayerInventoryToContainer(this, playerInventory, 8, 84);
	}
		
	@Override
	public String getGuiBackgroundTexture() {
		return "/CameraCraft/GUIPhotoPrinter.png";
	}
}

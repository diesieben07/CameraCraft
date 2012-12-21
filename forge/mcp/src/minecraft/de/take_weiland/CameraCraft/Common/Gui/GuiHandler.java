package de.take_weiland.CameraCraft.Common.Gui;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StringTranslate;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IGuiHandler;
import de.take_weiland.CameraCraft.Common.CameraCraft;
import de.take_weiland.CameraCraft.Common.IPhotoSource;
import de.take_weiland.CameraCraft.Common.PhotoSourcePhotoInHotbar;
import de.take_weiland.CameraCraft.Common.Inventory.InventoryCamera;
import de.take_weiland.CameraCraft.Common.Items.ItemCamera;
import de.take_weiland.CameraCraft.Common.Items.ItemPhoto;
import de.take_weiland.CameraCraft.Common.TileEntities.TileEntityCamera;
import de.take_weiland.CameraCraft.Common.TileEntities.TileEntityPhotoProcessor;
import de.take_weiland.CameraCraft.Common.TileEntities.TileEntityPhotoStation;

public class GuiHandler implements IGuiHandler {
	
	private static final GuiHandler instance = new GuiHandler();

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		ItemStack currentItem = player.getCurrentEquippedItem();
		switch (GuiScreens.values()[ID]) {
		case CAMERA_INVENTORY:
			InventoryCamera inv = ItemCamera.getInventory(currentItem, player);
			return new ContainerCamera(player.inventory, inv);
		/*case CAMERA_INVENTORY_TILE_ENTITY:
			System.out.println("opening" + FMLCommonHandler.instance().getEffectiveSide());
			TileEntityCamera tileEntityCamera = ((TileEntityCamera)world.getBlockTileEntity(x, y, z));
			return new ContainerCamera(player.inventory, tileEntityCamera.getCameraInventory(), tileEntityCamera.getCameraInventory().getType());*/
		
		case PHOTO_PROCESSOR:
			return new ContainerPhotoProcessor((TileEntityPhotoProcessor)world.getBlockTileEntity(x, y, z), player.inventory);
		case PHOTO_STATION:
			TileEntityPhotoStation tileEntity = (TileEntityPhotoStation)world.getBlockTileEntity(x, y, z);
			ContainerPhotoStation container = new ContainerPhotoStation(tileEntity, player.inventory);
			return container;
		case VIEW_PHOTOS_CURRENT_ITEM:
			IPhotoSource source = currentItem.getItem() instanceof ItemCamera ? ItemCamera.getInventory(currentItem, player) : currentItem.getItem() instanceof ItemPhoto ? new PhotoSourcePhotoInHotbar(player) : null;
			if (source != null && source.canViewPhotos(player)) {
				source.startViewing(player);
				return source == null ? null : new ContainerViewPhotos(player, source);
			} else {
				return null;
			}
			
		case VIEW_PHOTOS_TILE_ENTITY:
			TileEntity te = world.getBlockTileEntity(x, y, z);
			source = null;
			if (te instanceof IPhotoSource) {
				source = (IPhotoSource)te;
			} else if (te instanceof TileEntityCamera) {
				source = ((TileEntityCamera)te).getCameraInventory();
			}
			if (source != null) {
				if (source.canViewPhotos(player)) {
					source.startViewing(player);
					return new ContainerViewPhotos(player, source);
				} else {
					player.sendChatToPlayer(StringTranslate.getInstance().translateKey("tile.photoStation.occupied"));
					return null;
				}
			} else {
				return null;
			}
		case VIEW_PHOTOS_ENTITY:
			Entity entity = CameraCraft.proxy.getEntityById(player, x);
			if (entity instanceof IPhotoSource) {
				source = (IPhotoSource)entity;
				source.startViewing(player);
				return new ContainerViewPhotos(player, source);
			} else {
				return null;
			}
		default:
			return null;
		}
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return CameraCraft.proxy.createClientGuiElement(ID, player, world, x, y, z);
	}

	public static GuiHandler instance() {
		return instance;
	}
}

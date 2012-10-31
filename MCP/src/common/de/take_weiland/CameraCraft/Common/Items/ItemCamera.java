package de.take_weiland.CameraCraft.Common.Items;

import java.util.HashMap;
import java.util.List;

import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.StringTranslate;
import net.minecraft.src.World;
import de.take_weiland.CameraCraft.Common.CameraCraft;
import de.take_weiland.CameraCraft.Common.Achievements;
import de.take_weiland.CameraCraft.Common.CameraType;
import de.take_weiland.CameraCraft.Common.ConfigurationManager;
import de.take_weiland.CameraCraft.Common.PhotoInformation;
import de.take_weiland.CameraCraft.Common.Blocks.BlockCamera;
import de.take_weiland.CameraCraft.Common.Blocks.CameraCraftBlock;
import de.take_weiland.CameraCraft.Common.Entities.EntityCamera;
import de.take_weiland.CameraCraft.Common.Gui.GuiScreens;
import de.take_weiland.CameraCraft.Common.Inventory.InventoryCamera;
import de.take_weiland.CameraCraft.Common.Inventory.InventoryCameraHotbar;
import de.take_weiland.CameraCraft.Common.Network.PacketHelper;
import de.take_weiland.CameraCraft.Common.Network.NetAction;
import de.take_weiland.CameraCraft.Common.TileEntities.TileEntityCamera;

public class ItemCamera extends CameraCraftItem {

	private static final int COOLDOWN = 25;
	public static HashMap<EntityPlayer,Integer> takePhotoCooldown = new HashMap<EntityPlayer,Integer>();
	
	public ItemCamera(int par1) {
		super(par1);
		setHasSubtypes(true);
	}
	
	@Override
	public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float par8, float par9, float par10) {
        int blockAt = world.getBlockId(x, y, z);

        if (blockAt == Block.snow.blockID) {
            side = 1;
        } else if (blockAt != Block.vine.blockID && blockAt != Block.tallGrass.blockID && blockAt != Block.deadBush.blockID) {
            switch (side) {
            case 0:
            	y--;
            	break;
            case 1:
            	y++;
            	break;
            case 2:
            	z--;
            	break;
            case 3:
            	z++;
            	break;
            case 4:
            	x--;
            	break;
            case 5:
            	x++;
            	break;
            }
        }

        //           canPlayerEdit
        if (!player.func_82246_f(x, y, z) || itemStack.stackSize == 0) {
            return false;
        } else {
        	if (CameraCraftBlock.cameraPlaced.canPlaceBlockAt(world, x, y, z)) {
        		int metadataRotation = MathHelper.floor_double(((player.rotationYaw + 180.0) * 16.0 / 360.0) + 0.5) & 15;
                world.setBlockAndMetadataWithNotify(x, y, z, CameraCraftBlock.cameraPlaced.blockID, metadataRotation);
        		CameraCraftBlock.cameraPlaced.onBlockPlacedBy(world, x, y, z, player);
        		((TileEntityCamera)world.getBlockTileEntity(x, y, z)).setInfo(player, itemStack);
        		player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
        		return true;
        	} else {
        		return false;
        	}
        }
    }
	
	@Override
	public int getIconFromDamage(int damage) {
		return CameraType.fromItemDamage(damage).texture();
	}
	
	@Override
	public void getSubItems(int itemID, CreativeTabs tab, List list) {
		for (CameraType type : CameraType.values()) {
			list.add(new ItemStack(this, 1, type.toItemDamage()));
		}
	}
	
	@Override
	public String getItemNameIS(ItemStack itemStack) {
        return CameraType.fromItemDamage(itemStack.getItemDamage()).nameLocalization();
    }
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		if (!world.isRemote) {
			if (!ConfigurationManager.enableShiftRightclick || player.isSneaking()) {
				player.openGui(CameraCraft.instance, GuiScreens.CAMERA_INVENTORY.toGuiId(), world, 0, 0, 0);
			} else { 
				if (canTakePhoto(itemStack)) {
					tryTakePhoto(itemStack, player);
				}
			}
		}
		return itemStack;
    }
	
	public static void tryTakePhoto(ItemStack itemStack, EntityPlayer player) {
		if (!canTakePhoto(itemStack)) {
			player.sendChatToPlayer(StringTranslate.getInstance().translateKey("cameracraft.takephoto.fail"));
		} else {
			if (takePhotoCooldown.get(player) == null) {	
				takePhotoCooldown.put(player, COOLDOWN);
				ByteArrayDataOutput output = PacketHelper.buildPacket(NetAction.SERVER_REQUEST_PHOTO);
				output.writeUTF(ItemPhoto.buildID(player.username));
				PacketDispatcher.sendPacketToPlayer(PacketHelper.finishPacket(output), (Player)player);
				player.worldObj.playSoundAtEntity(player, "cameracraft.takephoto", 1, 1);
			}
		}
	}

	public static InventoryCamera getInventory(ItemStack camera, EntityPlayer player) {
		return camera.getItem() instanceof ItemCamera ? new InventoryCameraHotbar(camera, player) : null;
	}
	
	@Override
	public boolean getShareTag() {
		return true;
	}

	public static boolean canTakePhoto(ItemStack itemToCheck) {
		if (itemToCheck != null && itemToCheck.getItem() instanceof ItemCamera) {
			InventoryCamera inventory = getInventory(itemToCheck, null);
			if (inventory.getStackInSlot(0) == null || !(inventory.getStackInSlot(0).getItem() instanceof ItemPhotoStorage) || ItemPhotoStorage.isFull(inventory.getStackInSlot(0))) {
				return false;
			}
			
			return inventory.getType() == CameraType.STANDARD || inventory.getStackInSlot(1) != null || inventory.getPartialBattery() != 0;
		} else {
			return false;
		}
	}

	public static void photoTakenWithCamera(ItemStack camera, EntityPlayer player, String photoID, String photoName) {
		if (!canTakePhoto(camera)) {
			return;
		} else {
			getInventory(camera, player).addPhoto(photoID, player, photoName);
		}
	}
	
	public static void photoTakenWithCamera(TileEntityCamera tileEntity, String photoId) {
		if (!canTakePhoto(tileEntity.getCameraInventory().getCameraStack())) {
			return;
		} else {
			tileEntity.getCameraInventory().addPhoto(photoId, tileEntity);
			EntityCamera camera = BlockCamera.findAssociatedEntityCamera(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord, tileEntity.worldObj);
			EntityPlayer player = tileEntity.worldObj.getPlayerEntityByName(tileEntity.getOwner());
			if (camera != null && player != null && player.getDistanceSq(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord) <= 64) {	
				player.triggerAchievement(Achievements.tripod);
			}
		}
	}
}
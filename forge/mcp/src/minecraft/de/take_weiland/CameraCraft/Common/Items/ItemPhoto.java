package de.take_weiland.CameraCraft.Common.Items;

import java.io.File;
import java.util.List;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.StringTranslate;
import net.minecraft.src.World;
import de.take_weiland.CameraCraft.Common.CameraCraft;
import de.take_weiland.CameraCraft.Common.Achievements;
import de.take_weiland.CameraCraft.Common.PhotoInformation;
import de.take_weiland.CameraCraft.Common.Entities.EntityPhoto;
import de.take_weiland.CameraCraft.Common.Gui.GuiScreens;


public class ItemPhoto extends ItemPhotoStorage {

	public ItemPhoto(int par1) {
		super(par1);
		setHasSubtypes(true);
	}
	
	@Override
	public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int tileX, int tileY, int tileZ, int side, float par8, float par9, float par10) {

		if (itemStack.getItemDamage() != 1 || !player.func_82247_a(tileX, tileY, tileZ, side, itemStack)) {
        	return false;
        } else {
        	byte direction = 0;
            switch (side) {
            case 0:
            case 1:
            	return false;
            case 4:
            	direction = 1;
            	break;
            case 3:
            	direction = 2;
            	break;
            case 5:
            	direction = 3;
            	break;
            }
            
            PhotoInformation info = getFirstPhoto(itemStack);
            
            EntityPhoto entity = new EntityPhoto(world, info, tileX, tileY, tileZ, direction);
            
            if (entity.onValidSurface()) {
            	if (!world.isRemote) {
            		world.spawnEntityInWorld(entity);
            		player.triggerAchievement(Achievements.poster);
            	}
            	
            	itemStack.stackSize--;
            }
            return true;
        }
    }
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		player.openGui(CameraCraft.instance, GuiScreens.VIEW_PHOTOS_CURRENT_ITEM.toGuiId(), world, 0, 0, 0);
		return itemStack;
	}
	
	@Override
	public int getIconFromDamage(int damage) {
		return damage == 1 ? 11 : 6;
	}

	public static String buildID(String username) {
		String id = username.toLowerCase().replaceAll("[^a-z0-9_]", "_");
		int currentAppendNo = 0;
		while (doesPhotoExist(id + String.valueOf(currentAppendNo))) {
			currentAppendNo++;
		}
		return id + String.valueOf(currentAppendNo);
	}
	
	public static boolean doesPhotoExist(String photoID) {
		File saveFolder = CameraCraft.proxy.getCameraCraftSaveFolder();
		return (new File(saveFolder, photoID + ".png")).exists();
	}

	public static File getPhotoSaveFile(String photoID) {
		return new File(CameraCraft.proxy.getCameraCraftSaveFolder(), photoID + ".png");
	}

	@Override
	public int getCapacity() {
		return 1;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean showExtended) {
		PhotoInformation photo = getFirstPhoto(itemStack);
		if (photo != null) {
			list.add(StringTranslate.getInstance().translateKeyFormat("item.photo.info.user", photo.getPlayer()));
			list.add(photo.getSizeX() + " x " + photo.getSizeY());
		}
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public String getItemDisplayName(ItemStack itemStack) {
		PhotoInformation photo = getFirstPhoto(itemStack);
		if (photo != null) {
			return photo.getName();
		} else {
			return StringTranslate.getInstance().translateKey(getItemNameIS(itemStack));
		}
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public boolean hasEffect(ItemStack stack) {
        PhotoInformation info = getFirstPhoto(stack);
		return info != null && info.isTeleport();
    }
}

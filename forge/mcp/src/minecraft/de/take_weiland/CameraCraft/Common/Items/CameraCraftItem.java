package de.take_weiland.CameraCraft.Common.Items;

import de.take_weiland.CameraCraft.Common.CameraCraft;
import static de.take_weiland.CameraCraft.Common.ConfigurationManager.*;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraftforge.common.Configuration;

public class CameraCraftItem extends Item {
	
	private boolean showOnCreative = false;
	
	public static CreativeTabs creativeTab;
	
	public static final Item camera = new ItemCamera(cameraId).setShowOnCreative().setItemName("camera").setMaxStackSize(1);
	public static final Item photo = new ItemPhoto(photoId).setItemName("photo").setMaxStackSize(1);
	public static final Item battery = new ItemBattery(batteryId).setShowOnCreative().setItemName("battery");
	public static final ItemPhotoStorage film = (ItemPhotoStorage)new ItemFilm(filmId).setShowOnCreative().setItemName("film").setMaxStackSize(1);
	public static final ItemPhotoStorage memoryCard = (ItemPhotoStorage)new ItemMemoryCard(memoryCardId).setShowOnCreative().setItemName("memoryCard").setIconIndex(10).setMaxStackSize(1);
	public static final Item photoFilter = new ItemPhotoFilter(photoFilterId).setShowOnCreative().setItemName("photoFilter").setItemName("photoFilter").setMaxStackSize(16);
	public static final Item teleportationBattery = new CameraCraftItem(teleportationBatteryId).setShowOnCreative().setItemName("teleportationBattery").setMaxDamage(10).setMaxStackSize(16).setIconCoord(12, 1);
	
	public CameraCraftItem(int itemID) {
		super(itemID);
	}
	
	@Override
	public String getTextureFile() {
		return "/CameraCraft/tex.png";
	}

	public static void createStackTagCompound(ItemStack stack) {
		if (stack.stackTagCompound == null) {
			stack.stackTagCompound = new NBTTagCompound();
		}
	}
	
	public Item setShowOnCreative() {
		showOnCreative = true;
		return this;
	}

	@Override
	public CreativeTabs getCreativeTab() {
		return showOnCreative ? creativeTab : null;
	}
	
	public static void enable() {
		creativeTab = CameraCraft.theCreativeTab;
	}
	
	public static void disable() {
		creativeTab = null;
	}
}
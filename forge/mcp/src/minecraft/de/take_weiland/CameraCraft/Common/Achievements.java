package de.take_weiland.CameraCraft.Common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;
import cpw.mods.fml.common.ICraftingHandler;
import de.take_weiland.CameraCraft.Common.Blocks.CameraCraftBlock;
import de.take_weiland.CameraCraft.Common.Items.CameraCraftItem;

public class Achievements implements ICraftingHandler {
	public static final Achievement buildCamera = new Achievement(19991, "buildCamera", 0, 0, new ItemStack(CameraCraftItem.camera, 1, CameraType.STANDARD.toItemDamage()), null).registerAchievement();
	public static final Achievement takePhoto = new Achievement(19992, "takePhoto", 1, -2, new ItemStack(CameraCraftItem.photo, 1, 0), buildCamera).registerAchievement();
	public static final Achievement teleport = new Achievement(19993, "teleportPhoto", 3, -2, Item.enderPearl, takePhoto).setSpecial().registerAchievement();
	public static final Achievement poster = new Achievement(19994, "poster", 3, 0, new ItemStack(CameraCraftItem.photo, 1, 1), takePhoto).registerAchievement();
	public static final Achievement tripod = new Achievement(19995, "tripod", 3, -4, new ItemStack(CameraCraftBlock.tripod), takePhoto).registerAchievement();
	
	@Override
	public void onCrafting(EntityPlayer player, ItemStack item, IInventory craftMatrix) {
		if (item.getItem() == CameraCraftItem.camera && CameraType.fromItemDamage(item.getItemDamage()) == CameraType.STANDARD) {
			player.triggerAchievement(buildCamera);
		}
	}

	@Override
	public void onSmelting(EntityPlayer player, ItemStack item) { }
	
	public static void init() {
		AchievementPage.registerAchievementPage(new AchievementPage("Camera Craft", buildCamera, takePhoto, teleport, poster, tripod));
	}
}
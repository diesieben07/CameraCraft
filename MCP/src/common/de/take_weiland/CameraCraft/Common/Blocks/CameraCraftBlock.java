package de.take_weiland.CameraCraft.Common.Blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import de.take_weiland.CameraCraft.Common.Items.CameraCraftItem;
import de.take_weiland.CameraCraft.Common.TileEntities.TileEntityCamera;
import de.take_weiland.CameraCraft.Common.TileEntities.TileEntityPhotoProcessor;
import de.take_weiland.CameraCraft.Common.TileEntities.TileEntityPhotoStation;
import de.take_weiland.CameraCraft.Common.TileEntities.TileEntityTeleporatationEnabler;
import static de.take_weiland.CameraCraft.Common.ConfigurationManager.*;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.Material;

public class CameraCraftBlock extends Block {

	public static final Block photoProcessor = new BlockPhotoProcessor(photoProcessorId).setBlockName("photoProcessor").setHardness(3.5F).setStepSound(soundStoneFootstep);
	public static final Block photoStation = new BlockPhotoStation(photoStationId).setBlockName("photoPrinter").setHardness(3.5F).setStepSound(soundStoneFootstep);
	public static final Block tripod = new BlockTripod(tripodId).setBlockName("tripod").setHardness(0.5F).setStepSound(soundMetalFootstep);
	public static final Block cameraPlaced = new BlockCamera(cameraBlockId).setHardness(0).setStepSound(soundMetalFootstep);
	public static final Block enabler = new BlockTeleportationEnabler(teleportationEnablerId).setBlockName("teleportationEnabler").setHardness(1.5F).setStepSound(soundMetalFootstep);
	
	public CameraCraftBlock(int blockID, int texture, Material material) {
		super(blockID, texture, material);
	}
	
	@Override
	public CreativeTabs getCreativeTabToDisplayOn() {
		return CameraCraftItem.creativeTab;
	}

	@Override
	public String getTextureFile() {
		return "/CameraCraft/tex.png";
	}
	
	public static void registerBlocks() {
		GameRegistry.registerBlock(photoProcessor);
		GameRegistry.registerBlock(photoStation);
		GameRegistry.registerBlock(tripod, ItemTripod.class);
		GameRegistry.registerBlock(enabler);
		GameRegistry.registerTileEntity(TileEntityPhotoProcessor.class, "PhotoProcessor");
		GameRegistry.registerTileEntity(TileEntityPhotoStation.class, "PhotoPrinter");
		GameRegistry.registerTileEntity(TileEntityCamera.class, "CameraCraftCamera");
		GameRegistry.registerTileEntity(TileEntityTeleporatationEnabler.class, "teleportationEnabler");
	}
	
	public static class ItemTripod extends ItemBlock {

		public ItemTripod(int itemId) {
			super(itemId);
			setMaxStackSize(16);
		}
		
	}
}

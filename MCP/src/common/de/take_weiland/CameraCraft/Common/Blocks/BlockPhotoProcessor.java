package de.take_weiland.CameraCraft.Common.Blocks;

import java.util.Random;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

import de.take_weiland.CameraCraft.Common.CameraCraft;
import de.take_weiland.CameraCraft.Common.Gui.GuiScreens;
import de.take_weiland.CameraCraft.Common.Inventory.InventoryHelper;
import de.take_weiland.CameraCraft.Common.TileEntities.TileEntityPhotoProcessor;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class BlockPhotoProcessor extends GenericBlockContainer {
	
	private static final BlockDirectionHelper directionHelper = new BlockDirectionHelper().setTextureTopBottom(23).setTextureFront(13).setTextureSides(12);
	
	public BlockPhotoProcessor(int blockID) {
		super(blockID, 0, Material.rock);
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entity) {
		BlockDirectionHelper.directionalBlockPlaced(world, x, y, z, entity);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side) {
		return directionHelper.getTexture(side, blockAccess.getBlockMetadata(x, y, z));
	}
	
	@Override
	public int getBlockTextureFromSide(int side) {
		return side < 2 ? 23 : side == 3 ? 13 : 12;
	}

	@Override
	public TileEntity createNewTileEntity(World var1) {
		return new TileEntityPhotoProcessor();
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
		player.openGui(CameraCraft.instance, GuiScreens.PHOTO_PROCESSOR.toGuiId(), world, x, y, z);
		return true;
	}
}

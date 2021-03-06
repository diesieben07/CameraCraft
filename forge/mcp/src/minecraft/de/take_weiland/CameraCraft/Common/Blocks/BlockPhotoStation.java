package de.take_weiland.CameraCraft.Common.Blocks;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.take_weiland.CameraCraft.Common.CameraCraft;
import de.take_weiland.CameraCraft.Common.Gui.GuiScreens;
import de.take_weiland.CameraCraft.Common.TileEntities.TileEntityPhotoStation;

public class BlockPhotoStation extends GenericBlockContainer {

	private static final BlockDirectionHelper directionHelper = new BlockDirectionHelper().setTextureBottom(23).setTextureFront(21).setTextureSides(12);
	
	protected BlockPhotoStation(int blockID) {
		super(blockID, 0, Material.rock);
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entity) {
		BlockDirectionHelper.directionalBlockPlaced(world, x, y, z, entity);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side) {
		int meta = blockAccess.getBlockMetadata(x, y, z);
		directionHelper.setTextureTop(meta == 2 ? 26 : meta == 5 ? 25 : meta == 3 ? 22 : 27);
		return directionHelper.getTexture(side, meta);
	}
	
	@Override
	public int getBlockTextureFromSide(int side) {
		return side == 0 ? 23 : side == 1 ? 22 : side == 3 ? 21 : 12;
	}

	@Override
	public TileEntity createNewTileEntity(World var1) {
		return new TileEntityPhotoStation();
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
		if (!world.isRemote) {
			player.openGui(CameraCraft.instance, GuiScreens.PHOTO_STATION.toGuiId(), world, x, y, z);
		}
		return true;
	}

}

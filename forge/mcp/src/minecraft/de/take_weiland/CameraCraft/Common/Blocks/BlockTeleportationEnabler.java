package de.take_weiland.CameraCraft.Common.Blocks;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import de.take_weiland.CameraCraft.Common.TileEntities.TileEntityTeleporatationEnabler;

public class BlockTeleportationEnabler extends CameraCraftBlock {

	public BlockTeleportationEnabler(int blockID) {
		super(blockID, 29, Material.iron);
	}
	
	@Override
	public boolean hasTileEntity(int metadata) {
		return true;
	}
	
	public TileEntity createTileEntity(World world, int metadata) {
		return new TileEntityTeleporatationEnabler();
	}
}
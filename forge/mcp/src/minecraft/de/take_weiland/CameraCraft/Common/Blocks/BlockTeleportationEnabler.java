package de.take_weiland.CameraCraft.Common.Blocks;

import de.take_weiland.CameraCraft.Common.TileEntities.TileEntityTeleporatationEnabler;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

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
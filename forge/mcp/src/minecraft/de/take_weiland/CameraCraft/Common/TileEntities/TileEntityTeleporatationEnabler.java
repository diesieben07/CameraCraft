package de.take_weiland.CameraCraft.Common.TileEntities;

import java.util.List;

import de.take_weiland.CameraCraft.Common.Blocks.CameraCraftBlock;

import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class TileEntityTeleporatationEnabler extends TileEntity {
	public static boolean isTileEntityInRange(World world, int x, int y, int z, int range) {
		for (TileEntity entity : (List<TileEntity>)world.loadedTileEntityList) {
			if (entity instanceof TileEntityTeleporatationEnabler && !entity.isInvalid() && world.getBlockId(entity.xCoord, entity.yCoord, entity.zCoord) == CameraCraftBlock.enabler.blockID && getDistance(x, y, z, entity.xCoord, entity.yCoord, entity.zCoord) <= range * range) {
				return true;
			}
		}
		return false;
	}
	
	private static int getDistance(int x, int y, int z, int x2, int y2, int z2) {
		int xD = x - x2;
		int yD = y - y2;
		int zD = z - z2;
		return xD * xD + yD * yD + zD * zD;
	}
}

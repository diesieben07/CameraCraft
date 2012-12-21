package de.take_weiland.CameraCraft.Common.Blocks;

import net.minecraft.entity.EntityLiving;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockDirectionHelper {
	
	private int textureTop;
	private int textureBottom;
	private int textureSides;
	private int textureFront;

	public BlockDirectionHelper setTextureTop(int textureTop) {
		this.textureTop = textureTop;
		return this;
	}

	public BlockDirectionHelper setTextureBottom(int textureBottom) {
		this.textureBottom = textureBottom;
		return this;
	}

	public BlockDirectionHelper setTextureFront(int textureFront) {
		this.textureFront = textureFront;
		return this;
	}


	public BlockDirectionHelper setTextureSides(int textureSides) {
		this.textureSides = textureSides;
		return this;
	}
	
	public int getTexture(int side, int meta) {
		if (side == 1) {
			return textureTop;
		} else if (side == 0) {
			return textureBottom;
		} else if (side == meta) {
			return textureFront;
		} else {
			return textureSides;
		}
	}

	public static void directionalBlockPlaced(World world, int x, int y, int z, EntityLiving entity) {
		int rotation = MathHelper.floor_double((double)(entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		int meta = 0;
		
		switch (rotation) {
		case 0:
			meta = 2;
			break;
		case 1:
			meta = 5;
			break;
		case 2:
			meta = 3;
			break;
		case 3:
			meta = 4;
			break;
		}
		
		world.setBlockMetadataWithNotify(x, y, z, meta);
	}

	public BlockDirectionHelper setTextureTopBottom(int topBottom) {
		textureBottom = textureTop = topBottom;
		return this;
	}
}

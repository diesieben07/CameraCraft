package de.take_weiland.CameraCraft.Common.Blocks;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import de.take_weiland.CameraCraft.Common.Entities.EntityCamera;
import de.take_weiland.CameraCraft.Common.TileEntities.TileEntityCamera;

public class BlockCamera extends CameraCraftBlock {


	public BlockCamera(int blockID) {
		super(blockID, 0, Material.iron);
		setRequiresSelfNotify();
        this.setBlockBounds(0, 0, 0, 1, 0.75F, 1);
	}
	
	@Override
	public boolean hasTileEntity(int metadata) {
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		return new TileEntityCamera();
	}
	
	@Override
	public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune) {
		return new ArrayList<ItemStack>(0);
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving placer) {
		if (!world.isRemote && placer instanceof EntityPlayer) {
			EntityCamera cameraEntity = new EntityCamera(world);
			//cameraEntity.setPosition(x + 0.5, y - 1, z + 0.5);
			cameraEntity.setPositionAndRotation(x + 0.5, y - 1, z + 0.5, (placer.rotationYaw + 180) % 360, 0);
			cameraEntity.rotationYawHead = cameraEntity.rotationYaw;
			//cameraEntity.rotationYawHead = cameraEntity.rotationYaw = (placer.rotationYaw + 180) % 360;
			world.spawnEntityInWorld(cameraEntity);
			TileEntityCamera tileEntity = (TileEntityCamera)world.getBlockTileEntity(x, y, z);
			EntityPlayer player = (EntityPlayer) placer;
			tileEntity.setInfo(player, player.getCurrentEquippedItem());
		}
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, int blockID, int meta) {
		EntityCamera camera = findAssociatedEntityCamera(x, y, z, world);
		if (camera != null) {
			camera.setDead();
			dropBlockAsItem_do(world, x, y, z, ((TileEntityCamera)world.getBlockTileEntity(x, y, z)).getCameraInventory().getCameraStack());
		}
	}
	
	@Override
	public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }
    
    @Override
    public int getRenderType() {
    	return -1;
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
    	return world.getBlockId(x, y - 1, z) == CameraCraftBlock.tripod.blockID;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
        return null;
    }
    
    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int blockID) {
	    if (!canPlaceBlockAt(world, x, y, z)) {
	    	dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
	    	world.setBlockWithNotify(x, y, z, 0);
	    }
    }
    
    public static EntityCamera findAssociatedEntityCamera(int x, int y, int z, World world) {
    	List entities = world.getEntitiesWithinAABB(EntityCamera.class, AxisAlignedBB.getAABBPool().addOrModifyAABBInPool(x - 0.5, y, z - 0.5, x + 0.5, y + 1, z + 0.5));
    	if (entities.size() == 1) {
    		return (EntityCamera)entities.get(0);
    	}
    	return null;
    }
    
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
    	//TODO: Find out why this doesn't work
    	/*if (!world.isRemote) {
    		EntityCamera assocEntity = findAssociatedEntityCamera(x, y, z, world);
			if (assocEntity != null) {
				player.openGui(CameraCraft.instance, GuiScreens.CAMERA_INVENTORY_TILE_ENTITY.toGuiId(), world, x, y, z);
			}
		}*/
    	return true;
    }
}
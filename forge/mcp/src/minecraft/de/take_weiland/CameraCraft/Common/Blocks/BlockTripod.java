package de.take_weiland.CameraCraft.Common.Blocks;

import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import de.take_weiland.CameraCraft.Common.ConfigurationManager;
import de.take_weiland.CameraCraft.Common.Items.ItemCamera;
import de.take_weiland.CameraCraft.Common.Items.ItemPhoto;
import de.take_weiland.CameraCraft.Common.Network.PacketHelper;
import de.take_weiland.CameraCraft.Common.Network.NetAction;
import de.take_weiland.CameraCraft.Common.TileEntities.TileEntityCamera;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Material;
import net.minecraft.src.World;
import net.minecraftforge.common.ForgeDirection;

public class BlockTripod extends CameraCraftBlock {

	public BlockTripod(int blockID) {
		super(blockID, 16, Material.circuits);
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
        return 1;
    }
    
    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
    	return world.isBlockSolidOnSide(x, y - 1, z, ForgeDirection.UP);
    }
    
    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int blockID) {
    	if (!world.isRemote) {
    		if (!canPlaceBlockAt(world, x, y, z)) {
	    		dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
	    		world.setBlockWithNotify(x, y, z, 0);
	    	}  else {
	    		boolean isGettingPowered = world.isBlockIndirectlyGettingPowered(x, y, z);
	    		if (isGettingPowered && world.getBlockId(x, y + 1, z) == cameraPlaced.blockID && world.getBlockMetadata(x, y, z) == 0) {
	    			TileEntityCamera tileEntity = (TileEntityCamera)world.getBlockTileEntity(x, y + 1, z);
	    			if (ItemCamera.canTakePhoto(tileEntity.getCameraInventory().getCameraStack())) {
	    				ByteArrayDataOutput data = PacketHelper.buildPacket(NetAction.SCREENSHOT_CAMERA_ENTITY);
	    				data.writeInt(x);
		    			data.writeInt(y + 1);
		    			data.writeInt(z);
		    			data.writeUTF(ItemPhoto.buildID(tileEntity.getOwner()));
		    			EntityPlayer player = world.getPlayerEntityByName(tileEntity.getOwner());
		    			if (ConfigurationManager.foreignScreenshotOnTripod && (player == null || player.getDistanceSq(x, y + 1, z) > 10000)) {
		    				player = world.getClosestPlayer(x, y + 1, z, 100);
		    			}
		    			if (player != null) {
		    				PacketDispatcher.sendPacketToPlayer(PacketHelper.finishPacket(data), (Player)player);
		    				world.playSoundEffect(x, y, z, "cameracraft.takephoto", 1, 1);
		    			}
	    			}
	    		}
	    		
	    		if (isGettingPowered) {
	    			world.setBlockMetadata(x, y, z, 1);
	    		} else {
	    			world.setBlockMetadata(x, y, z, 0);
	    		}
	    	}
		}
    }
}

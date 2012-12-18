package de.take_weiland.CameraCraft.Common.Blocks;

import de.take_weiland.CameraCraft.Common.Inventory.InventoryHelper;
import de.take_weiland.CameraCraft.Common.Items.CameraCraftItem;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public abstract class GenericBlockContainer extends BlockContainer {

	protected GenericBlockContainer(int par1, int par2, Material par3Material) {
		super(par1, par2, par3Material);
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
		InventoryHelper.genericContainerRemoval(world, x, y, z);
	}
	
	@Override
	public CreativeTabs getCreativeTabToDisplayOn() {
		return CameraCraftItem.creativeTab;
	}
	
	@Override
	public String getTextureFile() {
		return "/CameraCraft/tex.png";
	}
}

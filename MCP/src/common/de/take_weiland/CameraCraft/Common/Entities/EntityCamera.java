package de.take_weiland.CameraCraft.Common.Entities;

import java.io.IOException;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.StringTranslate;
import net.minecraft.src.World;

public class EntityCamera extends EntityLiving {


	public EntityCamera(World world) {
		super(world);
	}

	@Override
	public int getMaxHealth() {
		return 1;
	}
	
	@Override
	public boolean canBeCollidedWith() {
		return false;
	}
	
	public boolean canBePushed() {
		return false;
	}
	
	@Override
	public void onLivingUpdate() {}

	@Override
	protected void updateAITasks() {}
	
	@Override
	protected void updateEntityActionState() {}
}
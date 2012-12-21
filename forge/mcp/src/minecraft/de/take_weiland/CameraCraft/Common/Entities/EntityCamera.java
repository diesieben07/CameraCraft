package de.take_weiland.CameraCraft.Common.Entities;

import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;

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
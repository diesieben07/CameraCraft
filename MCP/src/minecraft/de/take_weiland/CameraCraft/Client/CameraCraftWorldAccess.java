package de.take_weiland.CameraCraft.Client;

import de.take_weiland.CameraCraft.Client.Rendering.RenderEntityPhoto;
import de.take_weiland.CameraCraft.Common.Entities.EntityPhoto;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IWorldAccess;

public class CameraCraftWorldAccess implements IWorldAccess {

	private static final CameraCraftWorldAccess instance = new CameraCraftWorldAccess();
	
	private CameraCraftWorldAccess() { }

	@Override
	public void obtainEntitySkin(Entity entity) {
		if (entity instanceof EntityPhoto) {
			EntityPhoto photo = (EntityPhoto)entity;
			RenderEntityPhoto.registerUse(new PhotoSizeInfo(photo.info));
		}
	}

	@Override
	public void releaseEntitySkin(Entity entity) {
		if (entity instanceof EntityPhoto) {
			EntityPhoto photo = (EntityPhoto)entity;
			RenderEntityPhoto.unregisterUse(new PhotoSizeInfo(photo.info));
		}
	}
	
	@Override
	public void markBlockNeedsUpdate(int var1, int var2, int var3) { }

	@Override
	public void markBlockNeedsUpdate2(int var1, int var2, int var3) { }

	@Override
	public void markBlockRangeNeedsUpdate(int var1, int var2, int var3, int var4, int var5, int var6) { }

	@Override
	public void playSound(String var1, double var2, double var4, double var6, float var8, float var9) { }

	@Override
	public void spawnParticle(String var1, double var2, double var4, double var6, double var8, double var10, double var12) { }

	@Override
	public void playRecord(String var1, int var2, int var3, int var4) { }

	@Override
	public void playAuxSFX(EntityPlayer var1, int var2, int var3, int var4, int var5, int var6) { }

	@Override
	public void destroyBlockPartially(int var1, int var2, int var3, int var4, int var5) { }

	public static CameraCraftWorldAccess instance() {
		return instance;
	}

	@Override
	public void func_82746_a(int var1, int var2, int var3, int var4, int var5) { }
}

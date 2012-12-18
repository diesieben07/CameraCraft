package de.take_weiland.CameraCraft.Client.Rendering;

import org.lwjgl.opengl.GL11;

import de.take_weiland.CameraCraft.Client.Models.ModelDigitalCamera;
import de.take_weiland.CameraCraft.Client.Models.ModelStandardCamera;
import de.take_weiland.CameraCraft.Common.CameraType;
import de.take_weiland.CameraCraft.Common.TileEntities.TileEntityCamera;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntitySpecialRenderer;

public class RenderTileEntityCamera extends TileEntitySpecialRenderer {

	public static boolean shouldRender = true;
	private ModelStandardCamera standardModel = new ModelStandardCamera();
	private ModelDigitalCamera digitalModel = new ModelDigitalCamera();
	
	private void renderCameraAt(TileEntityCamera camera, double d, double d2, double d3, float f) {
		if (camera.getCameraInventory() != null) {
			bindTextureByName("/CameraCraft/Cameras.png");
			GL11.glPushMatrix();
			GL11.glTranslatef((float)d + 0.5F, (float)d2 + 1.5F, (float)d3 + 0.5F);
			GL11.glRotatef(-(camera.getBlockMetadata() * 360) / 16, 0.0F, 1.0F, 0.0F);
			GL11.glScalef(1.0F, -1F, -1F);
			if (camera.getCameraInventory().getType() == CameraType.DIGITAL) {
				digitalModel.renderCamera();
			} else {
				standardModel.renderCamera();
			}
			GL11.glPopMatrix();
		}
	}
	
	@Override
	public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8) {
		if (shouldRender) {
			renderCameraAt((TileEntityCamera)var1, var2, var4, var6, var8);
		}
	}
}
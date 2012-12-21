package de.take_weiland.CameraCraft.Client;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ScreenShotHelper;
import net.minecraft.util.StringTranslate;
import de.take_weiland.CameraCraft.Client.Image.CameraCraftImageHelper;
import de.take_weiland.CameraCraft.Client.Rendering.RenderTileEntityCamera;

public class PhotoCreator implements AfterTickCallback {
	
	private Minecraft mc;
	private ItemStack camera;
	private boolean guiStatePrevious;
	private String photoID;
	private int x;
	private int y;
	private int z;
	private boolean useTileEntity;
	
	public PhotoCreator(Minecraft mc, ItemStack camera, boolean guiStatePrevious, String photoID) {
		this.mc = mc;
		this.camera = camera;
		this.guiStatePrevious = guiStatePrevious;
		this.photoID = photoID;
		useTileEntity = false;
	}
	
	public PhotoCreator(Minecraft mc, ItemStack camera, boolean guiStatePrevious, String photoID, int x, int y, int z) {
		this.mc = mc;
		this.camera = camera;
		this.guiStatePrevious = guiStatePrevious;
		this.photoID = photoID;
		this.x = x;
		this.y = y;
		this.z = z;
		useTileEntity = true;
	}


	@Override
	public void afterTick() {
		File tempDir = new File(Minecraft.getMinecraftDir(), "/CameraCraftTemp");
		tempDir.mkdirs();
		
		String filenameInfo = ScreenShotHelper.saveScreenshot(tempDir, mc.displayWidth, mc.displayHeight);
		mc.gameSettings.hideGUI = guiStatePrevious;
		mc.renderViewEntity = mc.thePlayer;
		RenderTileEntityCamera.shouldRender = true;
		mc.theWorld.markBlockForUpdate(x, y, z);
		
		if (filenameInfo.startsWith("Saved screenshot as ")) {
			File tempImageFile = new File(Minecraft.getMinecraftDir(), "/CameraCraftTemp/screenshots/" + filenameInfo.substring(20));
			tempImageFile.deleteOnExit();
			CameraCraftImageHelper.processScreenshotToImage(camera, tempImageFile, new File(Minecraft.getMinecraftDir(), "/CameraCraftTemp/"), photoID, useTileEntity, x, y, z);
		} else {
			mc.ingameGUI.getChatGUI().printChatMessage(StringTranslate.getInstance().translateKey("cameracraft.screenshot.fail"));
		}
	}
}

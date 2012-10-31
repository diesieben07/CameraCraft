package de.take_weiland.CameraCraft.Client.Image;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Random;

import javax.imageio.ImageIO;

import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.PacketDispatcher;

import de.take_weiland.CameraCraft.Client.Gui.GuiRenamePhoto;
import de.take_weiland.CameraCraft.Client.Gui.IRenamePhotoCallback;
import de.take_weiland.CameraCraft.Common.CameraCraft;
import de.take_weiland.CameraCraft.Common.CameraType;
import de.take_weiland.CameraCraft.Common.FilterType;
import de.take_weiland.CameraCraft.Common.Inventory.InventoryCamera;
import de.take_weiland.CameraCraft.Common.Items.CameraCraftItem;
import de.take_weiland.CameraCraft.Common.Items.ItemCamera;
import de.take_weiland.CameraCraft.Common.Network.PacketHelper;
import de.take_weiland.CameraCraft.Common.Network.NetAction;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ISaveHandler;
import net.minecraft.src.ItemStack;
import net.minecraft.src.SaveHandler;
import net.minecraft.src.StringTranslate;
import net.minecraft.src.World;

public class CameraCraftImageHelper {

	public static BufferedImage createImageFromByteBuffer(ByteBuffer byteBuffer) {
		byte[] buf = new byte[byteBuffer.remaining()];
		byteBuffer.get(buf);
		ByteArrayInputStream stream = new ByteArrayInputStream(buf);
		BufferedImage im = null;
		try {
			im = ImageIO.read(stream);
		} catch (Exception e) {	} finally {
			try {
				stream.close();
			} catch (Exception e) {	}
		}
		return im;
	}

	// resizes the big screenshot, applies photo filters and sends the created image to the server
	public static void processScreenshotToImage(ItemStack camera, File screenshotFile, File targetDirectory, String photoID, boolean useTileEntity, int x, int y, int z) {
		try {
			(new ThreadImageResize(ImageIO.read(screenshotFile), 256, 256, new ImageResizeCallbackScreenshotProcessed(photoID, camera, useTileEntity, x, y, z))).start();
		} catch (IOException e) {
			CameraCraft.proxy.imageProcessingFailed();
			e.printStackTrace();
		}
	}
	
	private static class ImageResizeCallbackScreenshotProcessed implements IImageResizeFinishedCallback {

		private String photoID;
		private ItemStack camera;
		private boolean useTileEntity;
		private int x;
		private int y;
		private int z;

		public ImageResizeCallbackScreenshotProcessed(String photoID, ItemStack camera, boolean useTileEntity, int x, int y, int z) {
			this.photoID = photoID;
			this.camera = camera;
			this.useTileEntity = useTileEntity;
			this.x = x;
			this.y = y;
			this.z = z;
		}

		@Override
		public void resizeFinished(BufferedImage resized) {
			try {
				
				boolean blackWhite = false;
				FilterType filterType = FilterType.TRANSPARENT; 
				
				InventoryCamera inventory = ItemCamera.getInventory(camera, null);
				CameraType type = CameraType.fromItemDamage(camera.getItemDamage());
				ItemStack storage = inventory.getStackInSlot(0);
				// its a black and white film
				if (storage.getItem().shiftedIndex == CameraCraftItem.film.shiftedIndex && storage.getItemDamage() == 0) {
					blackWhite = true;
				}
				
				ItemStack filter = inventory.getStackInSlot(type == CameraType.DIGITAL ? 2 : 1);
				if (filter != null) {
					filterType = FilterType.fromItemDamage(filter.getItemDamage());
				}
				
				for (int x = 0; x < resized.getWidth(); x++) {
					for (int y = 0; y < resized.getHeight(); y++) {
						int rgb = resized.getRGB(x, y);
						int r = rgb >> 16;
						int g = (rgb >> 8) & 255;
						int b = rgb & 255;
						switch (filterType) {
						case BLUE:
							r = 0;
							g = 0;
							break;
						case GREEN:
							r = 0;
							b = 0;
							break;
						case RED:
							g = 0;
							b = 0;
							break;
						case YELLOW:
							b = 0;
							break;
						}
						resized.setRGB(x, y, r << 16 | g << 8 | b);
					}
				}
				if (blackWhite) {
					BufferedImage result = new BufferedImage(resized.getWidth(), resized.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
					ColorConvertOp op = new ColorConvertOp(resized.getColorModel().getColorSpace(), result.getColorModel().getColorSpace(), null);
					op.filter(resized, result);
					resized = result;
				}
				
				if (!useTileEntity) {
					final ByteArrayDataOutput data = PacketHelper.buildPacket(NetAction.PHOTO_TAKEN);
					data.writeUTF(photoID);
					FMLClientHandler.instance().getClient().displayGuiScreen(new GuiRenamePhoto(new IRenamePhotoCallback() {
						
						@Override
						public void nameChanged(String newName) {
							data.writeUTF(newName);
							PacketDispatcher.sendPacketToServer(PacketHelper.finishPacket(data));
							FMLClientHandler.instance().getClient().displayGuiScreen(null);
						}
						
						@Override
						public void abort() {
							data.writeUTF(StringTranslate.getInstance().translateKey("cameracraft.photoname.default"));
							PacketDispatcher.sendPacketToServer(PacketHelper.finishPacket(data));
							FMLClientHandler.instance().getClient().displayGuiScreen(null);
						}
					}, StringTranslate.getInstance().translateKey("cameracraft.photoname.default")));
				} else {
					ByteArrayDataOutput output = PacketHelper.buildPacket(NetAction.PHOTO_TAKEN_NON_PLAYER);
					output.writeInt(x);
					output.writeInt(y);
					output.writeInt(z);
					output.writeUTF(photoID);
					PacketDispatcher.sendPacketToServer(PacketHelper.finishPacket(output));
				}
				
				try {
					PacketHelper.sendPacketsToServer(PacketHelper.writeFileFromImage(photoID, resized));
				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				CameraCraft.proxy.imageProcessingFailed();
				e.printStackTrace();
			}			
		}

		@Override
		public void resizeFailed(Exception e) {
			e.printStackTrace();
			CameraCraft.proxy.imageProcessingFailed();			
		}
		
	}
}

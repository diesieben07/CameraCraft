package de.take_weiland.CameraCraft.Client;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.src.RenderEngine;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.FMLTextureFX;

public class TextureFXTeleportationBattery extends FMLTextureFX {

	// offset of the Battery Icon in the texture
	private static final int OFFSET_X = 12;
	private static final int OFFSET_Y = 1;
	
	// the colors for the dot in the middle (16 colors, given as RGB values)
	private int[] colors = {
			0x0d251f, // 1
			0x13241f, // 2
			0x19231f, // 3
			0x1f231e, // 4 
			0x25221d, // 5
			0x2c221d, // 6
			0x32211d, // 7
			0x38201c, // 31 8 
			0x3e201b, // 32 9
			0x441e1b, // 33 10
			0x4a1e1a, // 34 11
			0x511d1a, // 35 12
			0x571c1a, // 36 13
			0x5d1c19, // 37 14
			0x641c19, // 38 15
			0x691b18 // 39 16
			
	};
	
	private int[] pixelNeedColor;
	
	private final Minecraft mc;
	
	private int ticks = 0;
	private int currentColor = 0;
	private int direction = 1;
	
	public TextureFXTeleportationBattery(int icon) {
		super(icon);
		mc = FMLClientHandler.instance().getClient();
	}
	
	@Override
	public void bindImage(RenderEngine engine) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, engine.getTexture("/CameraCraft/tex.png"));
    }
	
	@Override
	public void setup() {
		super.setup();
		try {
			BufferedImage imageDataIcon = ImageIO.read(mc.texturePackList.getSelectedTexturePack().getResourceAsStream("/CameraCraft/tex.png"));
			imageDataIcon = imageDataIcon.getSubimage(OFFSET_X * (imageDataIcon.getWidth() / 16), OFFSET_Y * (imageDataIcon.getHeight() / 16), imageDataIcon.getWidth() / 16, imageDataIcon.getHeight() / 16);
			if (imageDataIcon.getWidth() != tileSizeBase) {
				BufferedImage resized = new BufferedImage(tileSizeBase, tileSizeBase, BufferedImage.TYPE_INT_RGB);
				Graphics2D graphics = resized.createGraphics();
				graphics.drawImage(imageDataIcon, 0, 0, tileSizeBase, tileSizeBase, 0, 0, imageDataIcon.getHeight(), imageDataIcon.getWidth(), null);
				graphics.dispose();
				imageDataIcon = resized;
			}
			int[] pixels = new int[tileSizeSquare];
			imageDataIcon.getRGB(0, 0, tileSizeBase, tileSizeBase, pixels, 0, tileSizeBase);
			
			boolean[] coloredPixel = new boolean[tileSizeSquare];
			int numColoredPixel = 0;
			
			for (int i = 0; i < pixels.length; i++) {
                int i4 = i * 4;
                imageData[i4 + 0] = (byte)(pixels[i] >> 16 & 255);
                imageData[i4 + 1] = (byte)(pixels[i] >> 8  & 255);
                imageData[i4 + 2] = (byte)(pixels[i] >> 0  & 255);
                imageData[i4 + 3] = (byte)(pixels[i] >> 24 & 255);
                if (imageData[i4] == 0 && imageData[i4 + 1] == 0 && imageData[i4 + 2] == 0 && imageData[i4 + 3] != 0) {
                	coloredPixel[i] = true;
                	numColoredPixel++;
                } else {
                	coloredPixel[i] = false;
                }
            }
			
			pixelNeedColor = new int[numColoredPixel];
			int count = 0;
			for (int i = 0; i < coloredPixel.length; i++) {
				if (coloredPixel[i]) {	
					pixelNeedColor[count] = i;
					count++;
				}
			}
			
		} catch (IOException e) { }
	}
	
	@Override
	public void onTick() {
		Random random = new Random();
		ticks++;
		if (ticks >= 2) {
			byte color1 = (byte) (colors[currentColor] >> 16 & 255);
			byte color2 = (byte) (colors[currentColor] >> 8 & 255);
			byte color3 = (byte) (colors[currentColor] & 255);
			byte color4 = (byte) 255;
			for (int colorPixel : pixelNeedColor) {
				int pixelOffset = colorPixel * 4;
				imageData[pixelOffset] = color1;
				imageData[pixelOffset + 1] = color2;
				imageData[pixelOffset + 2] = color3;
				imageData[pixelOffset + 3] = color4;
			}
			ticks = 0;
			currentColor += direction;
			if (currentColor == colors.length - 1 || currentColor == 0) {
				direction = -direction;
			}
		}
	}
}
package de.take_weiland.CameraCraft.Client.Rendering;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;

import net.minecraft.src.Entity;
import net.minecraft.src.IntHashMap;
import net.minecraft.src.MathHelper;
import net.minecraft.src.OpenGlHelper;
import net.minecraft.src.Render;
import net.minecraft.src.Tessellator;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.PacketDispatcher;

import de.take_weiland.CameraCraft.Client.PhotoSizeInfo;
import de.take_weiland.CameraCraft.Client.Image.IImageResizeFinishedCallback;
import de.take_weiland.CameraCraft.Client.Image.ThreadImageResize;
import de.take_weiland.CameraCraft.Common.CameraCraft;
import de.take_weiland.CameraCraft.Common.Entities.EntityPhoto;
import de.take_weiland.CameraCraft.Common.Network.PacketHelper;
import de.take_weiland.CameraCraft.Common.Network.NetAction;

public class RenderEntityPhoto extends Render
{
    /** RNG. */
    private Random rand = new Random();
    
    // raw images as downloaded from the server
    public static HashMap<String,BufferedImage> rawImagesForPhotoIDs = new HashMap<String,BufferedImage>();
    
    // textures that have already been created for specific sizes 
    // null means: no download requested
    // -1 means: download in progress
    // otherwise texture ID
    public static ConcurrentHashMap<PhotoSizeInfo,Integer> textureNamesForPhotos = new ConcurrentHashMap<PhotoSizeInfo,Integer>();
    
    // photos were processed for a specific size are put in here by the resizing thread
    public static ConcurrentHashMap<PhotoSizeInfo,BufferedImage> resizedFinishedPhotos = new ConcurrentHashMap<PhotoSizeInfo, BufferedImage>();
    
    // how often a specific size is used
    public static HashMap<PhotoSizeInfo,Integer> photoSizeUses = new HashMap<PhotoSizeInfo,Integer>();
    
    // how often a photo id itself is used
    public static HashMap<String,Integer> photoIDUses = new HashMap<String,Integer>();
    
    public static RenderEntityPhoto instance;
    
    public RenderEntityPhoto() {
    	instance = this;
    }
    
    private class ImageResizeCallbackSizeSpecific implements IImageResizeFinishedCallback {

    	private PhotoSizeInfo info;
    	
		public ImageResizeCallbackSizeSpecific(PhotoSizeInfo info) {
			this.info = info;
		}

		@Override
		public void resizeFinished(BufferedImage resized) {
			// this prevents the photo from being set up, after the cache was cleared
			for (int x = 0; x < resized.getWidth(); x++) {
				for (int y = 0; y < resized.getHeight(); y++) {
					if (x < 2 || y < 2 || x > resized.getWidth() - 3 || y > resized.getHeight() - 3) {
						int borderRed = 78 + rand.nextInt(10);
						int borderGreen = 47 + rand.nextInt(10);
						int borderBlue = 15 + rand.nextInt(10);
						resized.setRGB(x, y, borderRed << 16 | borderGreen << 8 | borderBlue);	
					}
				}
			}
			
			if (textureNamesForPhotos.containsKey(info)) {
				resizedFinishedPhotos.put(info, resized);
			}
		}

		@Override
		public void resizeFailed(Exception e) { 
			CameraCraft.proxy.imageProcessingFailed();
			e.printStackTrace();
		}
    }
    
    public static void registerUse(PhotoSizeInfo info) {
    	int uses = photoSizeUses.containsKey(info) ? photoSizeUses.get(info) : 0;
    	photoSizeUses.put(info, uses + 1);
    	uses = photoIDUses.containsKey(info) ? photoIDUses.get(info.getPhotoID()) : 0;
    	photoIDUses.put(info.getPhotoID(), uses + 1);
    }
    
    public static void unregisterUse(PhotoSizeInfo info) {
    	int uses = photoSizeUses.containsKey(info) ? photoSizeUses.get(info) : 0;
    	if (uses == 1) {
    		photoSizeUses.remove(info);
    	} else if (uses != 0) {
    		photoSizeUses.put(info, uses - 1);
    	}
    	uses = photoIDUses.containsKey(info) ? photoIDUses.get(info.getPhotoID()) : 0;
    	if (uses == 1) {
    		photoIDUses.remove(info.getPhotoID());
    	} else if (uses != 0) {
    		photoIDUses.put(info.getPhotoID(), uses - 1);
    	}
    }
    
    
    public void bindTextureForPhotoSize(PhotoSizeInfo info) {
    	// remove unneccessary bound photos
    	Iterator<PhotoSizeInfo> it = photoSizeUses.keySet().iterator();
    	while (it.hasNext()) {
    		PhotoSizeInfo key = it.next();
    		if (photoSizeUses.get(key) == 0) {
    			it.remove();
    			Integer tex = textureNamesForPhotos.get(key); 
    			if (tex != null) {
    				FMLClientHandler.instance().getClient().renderEngine.deleteTexture(tex);
    			}
    			textureNamesForPhotos.remove(key);
    		}
    	}
    	
    	// remove unneccessary raw images
    	Iterator<String> iterator = photoIDUses.keySet().iterator();
    	while(iterator.hasNext()) {
    		String key = iterator.next();
    		if (photoIDUses.get(key) == 0) {
    			iterator.remove();
    			rawImagesForPhotoIDs.remove(key);
    		}
    	}
    	
    	for (PhotoSizeInfo key : resizedFinishedPhotos.keySet()) {
    		textureNamesForPhotos.put(key, FMLClientHandler.instance().getClient().renderEngine.allocateAndSetupTexture(resizedFinishedPhotos.get(key)));
    		resizedFinishedPhotos.remove(key);
    	}
    	
    	Integer texture = textureNamesForPhotos.get(info);
			
    	if (texture == null) {
    		if (rawImagesForPhotoIDs.containsKey(info.getPhotoID())) {
    			BufferedImage img = rawImagesForPhotoIDs.get(info.getPhotoID());
    			if (img != null) {
    				(new ThreadImageResize(img, info.getSizeX() * 64, info.getSizeY() * 64, new ImageResizeCallbackSizeSpecific(info))).start();
    				textureNamesForPhotos.put(info, -1);
    			}
    		} else {
    			// request data
    			ByteArrayDataOutput data = PacketHelper.buildPacket(NetAction.REQUEST_IMAGEDATA);
            	data.writeUTF(info.getPhotoID());
            	PacketDispatcher.sendPacketToServer(PacketHelper.finishPacket(data));
            	rawImagesForPhotoIDs.put(info.getPhotoID(), null);
    		}
    	} else if (texture != -1) {
    		FMLClientHandler.instance().getClient().renderEngine.bindTexture(texture);
    	} else {
    		loadTexture("/art/kz.png");
    	}
    }
    
    public void performRender(EntityPhoto entityPhoto, double par2, double par4, double par6, float par8, float par9)
    {
    	this.rand.setSeed(187L);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)par2, (float)par4, (float)par6);
        GL11.glRotatef(par8, 0.0F, 1.0F, 0.0F);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        bindTextureForPhotoSize(new PhotoSizeInfo(entityPhoto.info));
        float scale = 0.015625F;
        GL11.glScalef(scale, scale, scale);
        this.renderImageForEntityAtOffsetWithSize(entityPhoto, entityPhoto.info.getSizeX() * 64, entityPhoto.info.getSizeY() * 64, 0, 0);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }

    private void renderImageForEntityAtOffsetWithSize(EntityPhoto entityPhoto, int sizeX, int sizeY, int offsetX, int offsetY)
    {
        float var6 = (float)(-sizeX) / 2.0F;
        float var7 = (float)(-sizeY) / 2.0F;
        float var8 = -0.5F;
        float var9 = 0.5F;

        for (int var10 = 0; var10 < sizeX / 16; ++var10)
        {
            for (int var11 = 0; var11 < sizeY / 16; ++var11)
            {
                float var12 = var6 + (float)((var10 + 1) * 16);
                float var13 = var6 + (float)(var10 * 16);
                float var14 = var7 + (float)((var11 + 1) * 16);
                float var15 = var7 + (float)(var11 * 16);
                this.func_77008_a(entityPhoto, (var12 + var13) / 8F, (var14 + var15) / 8F);
                float var16 = (float)(offsetX + sizeX - var10 * 16) / sizeX;
                float var17 = (float)(offsetX + sizeX - (var10 + 1) * 16) / sizeX;
                float var18 = (float)(offsetY + sizeY - var11 * 16) / sizeY;
                float var19 = (float)(offsetY + sizeY - (var11 + 1) * 16) / sizeY;
                float var20 = 0.75F;
                float var21 = 0.8125F;
                float var22 = 0.0F;
                float var23 = 0.0625F;
                float var24 = 0.75F;
                float var25 = 0.8125F;
                float var26 = 0.001953125F;
                float var27 = 0.001953125F;
                float var28 = 0.7519531F;
                float var29 = 0.7519531F;
                float var30 = 0.0F;
                float var31 = 0.0625F;
                Tessellator tessellator = Tessellator.instance;
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, 0.0F, -1.0F);
                tessellator.addVertexWithUV((double)var12, (double)var15, (double)var8, (double)var17, (double)var18);
                tessellator.addVertexWithUV((double)var13, (double)var15, (double)var8, (double)var16, (double)var18);
                tessellator.addVertexWithUV((double)var13, (double)var14, (double)var8, (double)var16, (double)var19);
                tessellator.addVertexWithUV((double)var12, (double)var14, (double)var8, (double)var17, (double)var19);
                tessellator.setNormal(0.0F, 0.0F, 1.0F);
                tessellator.addVertexWithUV((double)var12, (double)var14, (double)var9, (double)var20, (double)var22);
                tessellator.addVertexWithUV((double)var13, (double)var14, (double)var9, (double)var21, (double)var22);
                tessellator.addVertexWithUV((double)var13, (double)var15, (double)var9, (double)var21, (double)var23);
                tessellator.addVertexWithUV((double)var12, (double)var15, (double)var9, (double)var20, (double)var23);
                tessellator.setNormal(0.0F, 1.0F, 0.0F);
                tessellator.addVertexWithUV((double)var12, (double)var14, (double)var8, (double)var24, (double)var26);
                tessellator.addVertexWithUV((double)var13, (double)var14, (double)var8, (double)var25, (double)var26);
                tessellator.addVertexWithUV((double)var13, (double)var14, (double)var9, (double)var25, (double)var27);
                tessellator.addVertexWithUV((double)var12, (double)var14, (double)var9, (double)var24, (double)var27);
                tessellator.setNormal(0.0F, -1.0F, 0.0F);
                tessellator.addVertexWithUV((double)var12, (double)var15, (double)var9, (double)var24, (double)var26);
                tessellator.addVertexWithUV((double)var13, (double)var15, (double)var9, (double)var25, (double)var26);
                tessellator.addVertexWithUV((double)var13, (double)var15, (double)var8, (double)var25, (double)var27);
                tessellator.addVertexWithUV((double)var12, (double)var15, (double)var8, (double)var24, (double)var27);
                tessellator.setNormal(-1.0F, 0.0F, 0.0F);
                tessellator.addVertexWithUV((double)var12, (double)var14, (double)var9, (double)var29, (double)var30);
                tessellator.addVertexWithUV((double)var12, (double)var15, (double)var9, (double)var29, (double)var31);
                tessellator.addVertexWithUV((double)var12, (double)var15, (double)var8, (double)var28, (double)var31);
                tessellator.addVertexWithUV((double)var12, (double)var14, (double)var8, (double)var28, (double)var30);
                tessellator.setNormal(1.0F, 0.0F, 0.0F);
                tessellator.addVertexWithUV((double)var13, (double)var14, (double)var8, (double)var29, (double)var30);
                tessellator.addVertexWithUV((double)var13, (double)var15, (double)var8, (double)var29, (double)var31);
                tessellator.addVertexWithUV((double)var13, (double)var15, (double)var9, (double)var28, (double)var31);
                tessellator.addVertexWithUV((double)var13, (double)var14, (double)var9, (double)var28, (double)var30);
                tessellator.draw();
            }
        }
    }

    private void func_77008_a(EntityPhoto entityPhoto, float par2, float par3)
    {
        int var4 = MathHelper.floor_double(entityPhoto.posX);
        int var5 = MathHelper.floor_double(entityPhoto.posY + (double)(par3 / 16.0F));
        int var6 = MathHelper.floor_double(entityPhoto.posZ);

        if (entityPhoto.direction == 0)
        {
            var4 = MathHelper.floor_double(entityPhoto.posX + (double)(par2 / 16.0F));
        }

        if (entityPhoto.direction == 1)
        {
            var6 = MathHelper.floor_double(entityPhoto.posZ - (double)(par2 / 16.0F));
        }

        if (entityPhoto.direction == 2)
        {
            var4 = MathHelper.floor_double(entityPhoto.posX - (double)(par2 / 16.0F));
        }

        if (entityPhoto.direction == 3)
        {
            var6 = MathHelper.floor_double(entityPhoto.posZ + (double)(par2 / 16.0F));
        }

        int var7 = this.renderManager.worldObj.getLightBrightnessForSkyBlocks(var4, var5, var6, 0);
        int var8 = var7 % 65536;
        int var9 = var7 / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)var8, (float)var9);
        GL11.glColor3f(1.0F, 1.0F, 1.0F);
    }

    @Override
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
        performRender((EntityPhoto)par1Entity, par2, par4, par6, par8, par9);
    }
}

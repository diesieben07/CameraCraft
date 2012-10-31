package de.take_weiland.CameraCraft.Client;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.google.common.io.ByteArrayDataOutput;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.Container;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.ItemStack;
import net.minecraft.src.StringTranslate;
import net.minecraft.src.TileEntityRenderer;
import net.minecraft.src.TileEntitySpecialRenderer;
import net.minecraft.src.World;
import net.minecraft.src.WorldClient;
import net.minecraft.src.WorldServer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.TextureFXManager;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.registry.TickRegistry;
import de.take_weiland.CameraCraft.Client.Gui.GuiGenericContainer;
import de.take_weiland.CameraCraft.Client.Gui.GuiContainerCamera;
import de.take_weiland.CameraCraft.Client.Gui.GuiContainerPhotoProcessor;
import de.take_weiland.CameraCraft.Client.Gui.GuiContainerPhotoStation;
import de.take_weiland.CameraCraft.Client.Gui.GuiContainerViewPhotos;
import de.take_weiland.CameraCraft.Client.Rendering.RenderEntityPhoto;
import de.take_weiland.CameraCraft.Client.Rendering.RenderCameraEntityDummy;
import de.take_weiland.CameraCraft.Client.Rendering.RenderTileEntityCamera;
import de.take_weiland.CameraCraft.Client.Rendering.TeleportationPhotoItemRenderer;
import de.take_weiland.CameraCraft.Common.CameraCraftCommonProxy;
import de.take_weiland.CameraCraft.Common.PhotoInformation;
import de.take_weiland.CameraCraft.Common.Blocks.BlockCamera;
import de.take_weiland.CameraCraft.Common.Blocks.CameraCraftBlock;
import de.take_weiland.CameraCraft.Common.Entities.EntityCamera;
import de.take_weiland.CameraCraft.Common.Entities.EntityPhoto;
import de.take_weiland.CameraCraft.Common.Gui.GenericContainer;
import de.take_weiland.CameraCraft.Common.Gui.GuiHandler;
import de.take_weiland.CameraCraft.Common.Gui.ContainerCamera;
import de.take_weiland.CameraCraft.Common.Gui.ContainerPhotoProcessor;
import de.take_weiland.CameraCraft.Common.Gui.ContainerPhotoStation;
import de.take_weiland.CameraCraft.Common.Gui.ContainerViewPhotos;
import de.take_weiland.CameraCraft.Common.Gui.GuiScreens;
import de.take_weiland.CameraCraft.Common.Items.CameraCraftItem;
import de.take_weiland.CameraCraft.Common.Items.ItemCamera;
import de.take_weiland.CameraCraft.Common.TileEntities.TileEntityCamera;

public class CameraCraftClientProxy extends CameraCraftCommonProxy {
	
	@Override
	public GuiScreen createClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		Container container = (Container)GuiHandler.instance().getServerGuiElement(ID, player, world, x, y, z);
		if (container instanceof ContainerPhotoProcessor) {
			return new GuiContainerPhotoProcessor((ContainerPhotoProcessor)container);
		} else if (container instanceof ContainerPhotoStation) {
			return new GuiContainerPhotoStation((ContainerPhotoStation)container);
		} else if (container instanceof ContainerCamera) {
			return new GuiContainerCamera((ContainerCamera)container);
		} else if (container instanceof ContainerViewPhotos) {
			return new GuiContainerViewPhotos((ContainerViewPhotos)container);
		} else if (container != null && container instanceof GenericContainer) {
			return new GuiGenericContainer((GenericContainer)container);
		} else {
			return null;
		}
	}
	
	@Override
	public void registerClientStuff() {		
		MinecraftForgeClient.preloadTexture("/CameraCraft/tex.png");
		RenderingRegistry.registerEntityRenderingHandler(EntityPhoto.class, new RenderEntityPhoto());
		RenderingRegistry.registerEntityRenderingHandler(EntityCamera.class, new RenderCameraEntityDummy());
		
		RenderTileEntityCamera renderer = new RenderTileEntityCamera();
		renderer.setTileEntityRenderer(TileEntityRenderer.instance);
		TileEntityRenderer.instance.specialRendererMap.put(TileEntityCamera.class, renderer);
		
		MinecraftForgeClient.registerItemRenderer(CameraCraftItem.photo.shiftedIndex, new TeleportationPhotoItemRenderer());
		
		KeyBindingRegistry.registerKeyBinding(CameraCraftKeyHandler.instance());
		TickRegistry.registerTickHandler(CameraCraftClientTickHandler.instance(), Side.CLIENT);
		
		TextureFXManager.instance().addAnimation(new TextureFXTeleportationBattery(CameraCraftItem.teleportationBattery.getIconFromDamage(0)));
	}
	
	@Override
	public void preInit() {
		MinecraftForge.EVENT_BUS.register(new CameraCraftClientEventHandler());
	}
	
	@Override
	public void executePhotoPlayer(String photoID) {		
		Minecraft mc = FMLClientHandler.instance().getClient();
		
		ItemStack currentItem = mc.thePlayer.getCurrentEquippedItem();
		if (ItemCamera.canTakePhoto(currentItem)) {
			boolean hideGuiStatePrevious = mc.gameSettings.hideGUI;
			mc.gameSettings.hideGUI = true;
			CameraCraftClientTickHandler.watchNextTick(new AfterTickCallback(mc, currentItem, hideGuiStatePrevious, photoID));
		}
	}
	@Override
	public File getCameraCraftSaveFolder() {
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		File saveFolder = server.getFile("saves/" + server.getFolderName() + "/cameracraft/");
		saveFolder.mkdirs();
		return saveFolder;
	}
	
	@Override
	public void imageProcessingFailed() {
		FMLClientHandler.instance().getClient().ingameGUI.getChatGUI().printChatMessage(StringTranslate.getInstance().translateKey("cameracraft.imageprocess.fail"));
	}
	
	@Override
	public void imageDataRecieved(String photoID, BufferedImage img) {		
		RenderEntityPhoto.rawImagesForPhotoIDs.put(photoID, img);
	}
	
	@Override
	public void executePhotoCameraEntity(int x, int y, int z, String photoId) {
		Minecraft mc = FMLClientHandler.instance().getClient();
		if (mc.theWorld.getBlockId(x, y, z) == CameraCraftBlock.cameraPlaced.blockID) {
			TileEntityCamera tileEntity = (TileEntityCamera)mc.theWorld.getBlockTileEntity(x, y, z);
			EntityCamera camera = BlockCamera.findAssociatedEntityCamera(x, y, z, mc.theWorld);
			if (camera != null) {
				RenderTileEntityCamera.shouldRender = false;
				mc.renderViewEntity = camera;
				boolean hideGuiStatePrevious = mc.gameSettings.hideGUI;
				mc.gameSettings.hideGUI = true;
				CameraCraftClientTickHandler.watchNextTick(new AfterTickCallback(mc, tileEntity.getCameraInventory().getCameraStack(), hideGuiStatePrevious, photoId, x, y, z));
			}
		}
	}
	
	@Override
	public Entity getEntityById(EntityPlayer player, int id) {
		if (player.worldObj instanceof WorldClient) {
			return ((WorldClient)player.worldObj).getEntityByID(id);
		} else {
			return super.getEntityById(player, id);
		}
	}
}
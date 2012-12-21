package de.take_weiland.CameraCraft.Common;

import java.awt.image.BufferedImage;
import java.io.File;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import cpw.mods.fml.common.FMLCommonHandler;

public class CameraCraftCommonProxy {

	public Object createClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		/* NO-OP on server */
		return null;
	}

	public void registerClientStuff() {
		/* NO-OP on server */
	}

	public void executePhotoPlayer(String photoID) { }
	
	public File getCameraCraftSaveFolder() {
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		File saveFolder = server.getFile(server.getFolderName() + "/cameracraft/");
		saveFolder.mkdirs();
		return saveFolder;
	}

	public void imageProcessingFailed() {
		// NO-OP on server
	}
	
	public void imageDataRecieved(String photoID, BufferedImage img) {
		// NO-OP on server
	}

	public void preInit() {
		// NO-OP on server
	}

	public Entity getEntityById(EntityPlayer player, int id) {
		if (player.worldObj instanceof WorldServer) {
			return ((WorldServer)player.worldObj).getEntityByID(id);
		} else {
			return null;
		}
	}

	public void executePhotoCameraEntity(int x, int y, int z, String photoId) {
		// NO-OP on server
	}
}

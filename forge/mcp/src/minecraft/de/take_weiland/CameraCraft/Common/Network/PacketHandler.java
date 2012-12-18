package de.take_weiland.CameraCraft.Common.Network;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.INetworkManager;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.StringTranslate;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import de.take_weiland.CameraCraft.Common.CameraCraft;
import de.take_weiland.CameraCraft.Common.CameraType;
import de.take_weiland.CameraCraft.Common.IPhotoSource;
import de.take_weiland.CameraCraft.Common.PhotoInformation;
import de.take_weiland.CameraCraft.Common.PhotoSizeAmountInfo;
import de.take_weiland.CameraCraft.Common.Blocks.CameraCraftBlock;
import de.take_weiland.CameraCraft.Common.Entities.EntityCamera;
import de.take_weiland.CameraCraft.Common.Gui.GenericContainer;
import de.take_weiland.CameraCraft.Common.Gui.ContainerCamera;
import de.take_weiland.CameraCraft.Common.Gui.ContainerPhotoStation;
import de.take_weiland.CameraCraft.Common.Gui.ContainerViewPhotos;
import de.take_weiland.CameraCraft.Common.Gui.GuiScreens;
import de.take_weiland.CameraCraft.Common.Items.ItemCamera;
import de.take_weiland.CameraCraft.Common.Items.ItemPhoto;
import de.take_weiland.CameraCraft.Common.Items.ItemPhotoStorage;
import de.take_weiland.CameraCraft.Common.Recipes.CameraCraftRecipes;
import de.take_weiland.CameraCraft.Common.TileEntities.TileEntityCamera;
import de.take_weiland.CameraCraft.Common.TileEntities.TileEntityPhotoStation;

public class PacketHandler implements IPacketHandler {
	
	private static HashMap<String,ByteBuffer> partialImages = new HashMap<String,ByteBuffer>();
	
	
	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, final Player player) {
		ByteArrayDataInput data = PacketHelper.openPacket(packet);
		NetAction action = PacketHelper.readAction(data);
				
		String photoID;
		ByteArrayDataOutput output;
		final ItemStack currentItem;
		final EntityPlayer playerEntity = (EntityPlayer)player;
		
		switch (action) {
		// only received on the server, will trigger the check if a photo can be taken and if that is true, confirm the request with an id for the photo
		case REQUEST_PHOTO:
			currentItem = playerEntity.getCurrentEquippedItem(); 
			ItemCamera.tryTakePhoto(currentItem, playerEntity);
			break;
		
		case SERVER_REQUEST_PHOTO:
			CameraCraft.proxy.executePhotoPlayer(data.readUTF());
			break;
		
		case SCREENSHOT_CAMERA_ENTITY:
			int x = data.readInt();
			int y = data.readInt();
			int z = data.readInt();
			String photoId = data.readUTF();
			CameraCraft.proxy.executePhotoCameraEntity(x, y, z, photoId);
			break;
		
		// only received on the server to transfer the photo
		case PHOTO_TAKEN:
			currentItem = playerEntity.getCurrentEquippedItem();
			
			if (ItemCamera.canTakePhoto(currentItem)) {
				photoID = data.readUTF();
				final String photoName = data.readUTF();
				PacketHelper.registerFileReceiveCallback(photoID, new IFileReceiveCallback() {
					@Override
					public void fileReceived(String fileID, byte[] bytes) {
						try {
							PacketHelper.readFile(ItemPhoto.getPhotoSaveFile(fileID), bytes);
							ItemCamera.photoTakenWithCamera(currentItem, playerEntity, fileID, photoName);
						} catch (Exception e) {
							playerEntity.sendChatToPlayer(StringTranslate.getInstance().translateKey("cameracraft.imageprocess.fail"));
							e.printStackTrace();
						}
					}
				});				
				
			} else {
				playerEntity.sendChatToPlayer(StringTranslate.getInstance().translateKey("cameracraft.takephoto.fail"));
			}
			
			break;
			
		case PHOTO_TAKEN_NON_PLAYER:
			x = data.readInt();
			y = data.readInt();
			z = data.readInt();
			final String photoId1 = data.readUTF();
			if (playerEntity.worldObj.getBlockId(x, y, z) == CameraCraftBlock.cameraPlaced.blockID) {
				final TileEntityCamera tileEntity = (TileEntityCamera)playerEntity.worldObj.getBlockTileEntity(x, y, z);
				if (ItemCamera.canTakePhoto(tileEntity.getCameraInventory().getCameraStack())) {
					PacketHelper.registerFileReceiveCallback(photoId1, new IFileReceiveCallback() {
						
						@Override
						public void fileReceived(String fileID, byte[] bytes) {
							try {
								PacketHelper.readFile(ItemPhoto.getPhotoSaveFile(fileID), bytes);
								ItemCamera.photoTakenWithCamera(tileEntity, photoId1);
							} catch (Exception e) {
								e.printStackTrace();
							}							
						}
					});
				}
			}
			break;
		
		// received on the server, it will send the image corresponding to the photoID
		case REQUEST_IMAGEDATA:
			photoID = data.readUTF();
			
			if (!ItemPhoto.doesPhotoExist(photoID)) {
				((EntityPlayer)player).sendChatToPlayer(StringTranslate.getInstance().translateKey("cameracraft.photomiss"));
			} else {
				output = PacketHelper.buildPacket(NetAction.IMAGEDATA);
				try {
					PacketHelper.sendPacketsToPlayer(PacketHelper.writeFile(photoID, ItemPhoto.getPhotoSaveFile(photoID)), player);
					
					output.writeUTF(photoID);
					PacketDispatcher.sendPacketToPlayer(PacketHelper.finishPacket(output), player);
				} catch (Exception e) {
					playerEntity.sendChatToPlayer(StringTranslate.getInstance().translateKey("cameracraft.photomiss"));
				}
			}
			break;

		// received on the client, used to transfer the image to the client
		case IMAGEDATA:
			photoID = data.readUTF();
			PacketHelper.registerFileReceiveCallback(photoID, new IFileReceiveCallback() {				
				@Override
				public void fileReceived(String fileID, byte[] bytes) {
					CameraCraft.proxy.imageDataRecieved(fileID, PacketHelper.readFileToImage(bytes));
				}
			});
			break;
		
		case REQUEST_VIEW_PHOTOS:
			int windowId = data.readInt();
			System.out.println("received " + windowId + " expected " + playerEntity.craftingInventory.windowId);
			if (playerEntity.craftingInventory.windowId == windowId && playerEntity.craftingInventory instanceof GenericContainer) {
				GenericContainer container = (GenericContainer)playerEntity.craftingInventory;
				if (container.getUpperInventory() instanceof IPhotoSource) {
					IPhotoSource source = (IPhotoSource)container.getUpperInventory();
					if (source.canViewPhotos(playerEntity)) {
						playerEntity.openGui(CameraCraft.instance, source.getScreenTypeViewPhotos().toGuiId(), playerEntity.worldObj, source.getX(), source.getY(), source.getZ());
					}
				}
			}
			break;
			
		case REQUEST_RENAME:
			windowId = data.readInt();
			int photoIndex = data.readByte();
			String newName = data.readUTF();
			if (playerEntity.craftingInventory.windowId == windowId && playerEntity.craftingInventory instanceof ContainerViewPhotos) {
				ContainerViewPhotos container = (ContainerViewPhotos)playerEntity.craftingInventory;
				container.getSource().nameChanged(photoIndex, newName);
			}
			break;
			
		case REQUEST_PRINT:
			windowId = data.readInt();
			int numPhotos = data.readByte();
			PhotoSizeAmountInfo[] amountInfo = new PhotoSizeAmountInfo[numPhotos];
			for (int i = 0; i < numPhotos; i++) {
				amountInfo[i] = PhotoSizeAmountInfo.createFrom(data);
			}
			
			if (playerEntity.craftingInventory instanceof ContainerViewPhotos && playerEntity.craftingInventory.windowId == windowId) {
				ContainerViewPhotos container = (ContainerViewPhotos)playerEntity.craftingInventory;
				if (container.getSource().canPrint()) {
					container.getSource().addToPrintQueue(Arrays.asList(amountInfo));
					playerEntity.closeScreen();
				}
			}
			break;
			
		case REQUEST_TELEPORT:
			windowId = data.readInt();
			photoIndex = data.readByte();
			if (playerEntity.craftingInventory.windowId == windowId && playerEntity.craftingInventory instanceof ContainerViewPhotos) {
				ContainerViewPhotos container = (ContainerViewPhotos)playerEntity.craftingInventory;
				PhotoInformation info = container.getSource().getPhotoInformation(photoIndex);
				if (info != null) {
					info.teleport(playerEntity);
					playerEntity.closeScreen();
				}
			}
			break;

		case FILEPART:
			String fileID = data.readUTF();
			int totalLength = data.readInt();
			int lengthThisPacket = data.readInt();
			int startThisPacket = data.readInt();
			byte[] bytes = new byte[lengthThisPacket];
			data.readFully(bytes);
			PacketHelper.partialFileReceived(fileID, totalLength, startThisPacket, bytes);
			break;
		
		case REQUEST_DELETE:
			windowId = data.readInt();
			photoIndex = data.readByte();
			if (playerEntity.craftingInventory instanceof ContainerViewPhotos && playerEntity.craftingInventory.windowId == windowId) {
				ContainerViewPhotos container = (ContainerViewPhotos)playerEntity.craftingInventory;
				if (container.getSource().canDelete()) {
					container.getSource().deletePhoto(photoIndex);
					if (container.getSource().numPhotos() == 0) {
						playerEntity.closeScreen();
					}
				}
			}
			break;
			
		case CONFIRM_CAMERACRAFT:
			CameraCraftRecipes.enableRecipesAndItems();
			boolean enableEnabler = data.readBoolean();
			CameraCraftRecipes.teleportationEnabler.forceDisable = !enableEnabler;
			break;
			
		case UNKNOWN:
			FMLCommonHandler.instance().getFMLLogger().warning("[CameraCraft] Received unknown NetAction with channel CameraCraft!");
			break;
		}
	}

}

package de.take_weiland.CameraCraft.Client.Gui;

import org.lwjgl.opengl.GL11;

import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.common.network.PacketDispatcher;

import de.take_weiland.CameraCraft.Client.PhotoSizeInfo;
import de.take_weiland.CameraCraft.Client.Rendering.RenderEntityPhoto;
import de.take_weiland.CameraCraft.Common.IPhotoSource;
import de.take_weiland.CameraCraft.Common.PhotoInformation;
import de.take_weiland.CameraCraft.Common.PhotoSizeAmountInfo;
import de.take_weiland.CameraCraft.Common.Gui.ContainerViewPhotos;
import de.take_weiland.CameraCraft.Common.Network.PacketHelper;
import de.take_weiland.CameraCraft.Common.Network.NetAction;
import net.minecraft.src.Container;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.StringTranslate;

public class GuiContainerViewPhotos extends GuiContainer {
	
	private static final int BUTTON_RENAME = 0;
	private static final int BUTTON_PREV = 1;
	private static final int BUTTON_NEXT = 2;
	private static final int BUTTON_INCR_X = 3;
	private static final int BUTTON_INCR_Y = 4;
	private static final int BUTTON_DECR_X = 5;
	private static final int BUTTON_DECR_Y = 6;
	private static final int BUTTON_PRINT = 7;
	private static final int BUTTON_SELECT = 8;
	private static final int BUTTON_DELETE = 9;
	private static final int BUTTON_TELEPORT = 10;
	
	protected int currentImage = 0;
	private GuiButton nextButton;
	private GuiButton prevButton;
	private GuiButton renameButton;
	private GuiButton teleportButton;
	private GuiButton printButton;
	private GuiButtonImage selectButton;
	private GuiButton decrSizeX;
	private GuiButton decrSizeY;
	private GuiButton incrSizeX;
	private GuiButton incrSizeY;
	private GuiButton decrAmount;
	private GuiButton incrAmount;	
	private GuiButton deleteButton;
	private int numSelectedPhotos;
	private String subtitle;
	private int subtitleWidth;
	private String sizeStringX;
	private int sizeStringXWidth;
	private String sizeStringY;
	private int sizeStringYWidth;
	private int currentPaperNeed;
	private boolean teleport = true;
	
	protected final ContainerViewPhotos container;
	private final IPhotoSource source;
	
	private PhotoSizeAmountInfo[] amountInfo;
	
	public GuiContainerViewPhotos(ContainerViewPhotos container) {
		super(container);
		this.container = container;
		source = container.getSource();
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	@Override
	public void initGui() {
		for (int i = 0; i < source.numPhotos(); i++) {
			RenderEntityPhoto.registerUse(new PhotoSizeInfo(source.getPhotoInformation(i).getPhotoId(), 4, 4));
		}
		
		amountInfo = new PhotoSizeAmountInfo[source.numPhotos()];
		
		String renameText = StringTranslate.getInstance().translateKey("cameracraft.gui.renamePhoto");
		String deleteText = StringTranslate.getInstance().translateKey("cameracraft.gui.deletePhoto");
		int renameDeleteMaxWidth = Math.max(fontRenderer.getStringWidth(renameText), fontRenderer.getStringWidth(deleteText));
		controlList.add((renameButton = new GuiButton(BUTTON_RENAME, 10, height - 30, renameDeleteMaxWidth + 15, 20, renameText)));
		controlList.add((prevButton = new GuiButton(BUTTON_PREV, 10, (height / 2) - 10, 15, 20, "<")));
		controlList.add((nextButton = new GuiButton(BUTTON_NEXT, 30, (height / 2) - 10, 15, 20, ">")));
		
		controlList.add((incrSizeX = new GuiButton(BUTTON_INCR_X, width - 20, height - 30, 15, 20, ">")));
		controlList.add((decrSizeX = new GuiButton(BUTTON_DECR_X, width - 60, height - 30, 15, 20, "<")));
			
		controlList.add((incrSizeY = new GuiButton(BUTTON_INCR_Y, width - 30, 10, 20, 20, "^")));
		controlList.add((decrSizeY = new GuiButtonImage(BUTTON_DECR_Y, width - 30, 60, "/CameraCraft/tex.png", 20, 196)));
			
		controlList.add((printButton = new GuiButtonImage(BUTTON_PRINT, 35, height - 90, "/CameraCraft/tex.png", 20, 76)));
		controlList.add((selectButton = new GuiButtonImage(BUTTON_SELECT, 10, height - 90, "/CameraCraft/tex.png", 0, 96)));	
	
		controlList.add((deleteButton = new GuiButton(BUTTON_DELETE, 10, height - 60, renameDeleteMaxWidth + 15, 20, deleteText)));
		
		controlList.add((teleportButton = new GuiButtonImage(BUTTON_TELEPORT, 50, (height / 2) - 10, "/CameraCraft/tex.png", 0, 196)));
		updateButtons();
	}
	
	/**
	 * recalculates button visibility and text on screen
	 */
	private void updateButtons() {		
		incrSizeX.drawButton = incrSizeY.drawButton = decrSizeX.drawButton = decrSizeY.drawButton = printButton.drawButton = selectButton.drawButton = source.canPrint();
		if (source.canPrint() && amountInfo[currentImage] != null) {
			amountInfo[currentImage].setSizeX(loopInt(amountInfo[currentImage].getSizeX(), 1, 4));
			amountInfo[currentImage].setSizeY(loopInt(amountInfo[currentImage].getSizeY(), 1, 4));
			sizeStringX = String.valueOf(amountInfo[currentImage].getSizeX());
			sizeStringXWidth = fontRenderer.getStringWidth(sizeStringX);
			
			sizeStringY = String.valueOf(amountInfo[currentImage].getSizeY());
			sizeStringYWidth = fontRenderer.getStringWidth(sizeStringY);
		}
				
		PhotoInformation currentInfo = source.getPhotoInformation(currentImage);
		subtitle = currentInfo.getName() + " - " + StringTranslate.getInstance().translateKeyFormat("item.photo.info.user", currentInfo.getPlayer()) + " (" + currentInfo.getLocX() + ", " + currentInfo.getLocY() + ", " + currentInfo.getLocZ() + ")";
		subtitleWidth = fontRenderer.getStringWidth(subtitle);
		
		prevButton.enabled = currentImage != 0;
		nextButton.enabled = currentImage != (source.numPhotos() - 1);
		renameButton.enabled = mc.thePlayer.username.equalsIgnoreCase(currentInfo.getPlayer());
		deleteButton.enabled = source.canDelete();
		
		if (source.canPrint()) {
			printButton.enabled = numSelectedPhotos != 0;
			selectButton.setTextureY(amountInfo[currentImage] == null ? 136 : 96);
			incrSizeX.enabled = incrSizeY.enabled = decrSizeX.enabled = decrSizeY.enabled = amountInfo[currentImage] != null;
			
			currentPaperNeed = 0;
			for (PhotoSizeAmountInfo info : amountInfo) {
				if (info != null) {
					currentPaperNeed += info.getSizeX() * info.getSizeY() * info.getAmount();
				}
			}
		}
		teleportButton.enabled = currentInfo.isTeleport();
	}
	
	/**
	 * Helper method to "loop a number around".
	 * if i > upperBound lowerBound is returned,
	 * if i < lowerBound upperBound is returned,
	 * otherwise i is returned
	 * @param i the number to loop
	 * @param lowerBound the minimum the number can be
	 * @param upperBound the maximum the number can be
	 * @return the looped number
	 */
	private static int loopInt(int i, int lowerBound, int upperBound) {
		if (i > upperBound) {
			i = lowerBound;
		}
		
		if (i < lowerBound) {
			i = upperBound;
		}
		return i;
	}
	
	@Override
	public void actionPerformed(GuiButton button) {
		ByteArrayDataOutput output;
		switch (button.id) {
		case BUTTON_RENAME:
			mc.displayGuiScreen(new GuiRenamePhoto(new RenameCallback(this), source.getPhotoInformation(currentImage).getName()));
			break;
		case BUTTON_NEXT:
			currentImage++;
			updateButtons();
			break;
		case BUTTON_PREV:
			currentImage--;
			updateButtons();
			break;
		case BUTTON_DECR_X:
			amountInfo[currentImage].setSizeX(amountInfo[currentImage].getSizeX() - 1);
			updateButtons();
			break;
		case BUTTON_DECR_Y:
			amountInfo[currentImage].setSizeY(amountInfo[currentImage].getSizeY() - 1);
			updateButtons();
			break;
		case BUTTON_INCR_X:
			amountInfo[currentImage].setSizeX(amountInfo[currentImage].getSizeX() + 1);
			updateButtons();
			break;
		case BUTTON_INCR_Y:
			amountInfo[currentImage].setSizeY(amountInfo[currentImage].getSizeY() + 1);
			updateButtons();
			break;
		
		case BUTTON_TELEPORT:
			output = PacketHelper.buildPacket(NetAction.REQUEST_TELEPORT);
			output.writeInt(mc.thePlayer.craftingInventory.windowId);
			output.writeByte(currentImage);
			PacketDispatcher.sendPacketToServer(PacketHelper.finishPacket(output));
			break;
		
		case BUTTON_DELETE:
			output = PacketHelper.buildPacket(NetAction.REQUEST_DELETE);
			output.writeInt(mc.thePlayer.craftingInventory.windowId);
			output.writeByte(currentImage);
			PacketDispatcher.sendPacketToServer(PacketHelper.finishPacket(output));
			source.deletePhoto(currentImage);
			if (source.numPhotos() != 0) {
				PhotoSizeAmountInfo[] newAmountInfo = new PhotoSizeAmountInfo[source.numPhotos()];
				for (PhotoSizeAmountInfo info : amountInfo) {
					if (info != null && info.getPhotoIndex() != currentImage) {
						int newIndex = info.getPhotoIndex() > currentImage ? info.getPhotoIndex() - 1 : info.getPhotoIndex();
						newAmountInfo[newIndex] = new PhotoSizeAmountInfo(info.getSizeX(), info.getSizeY(), info.getAmount(), newIndex);
					}
				}
				amountInfo = newAmountInfo;
				if (currentImage != 0) {
					currentImage--;
				}
				updateButtons();
			} else {
				mc.thePlayer.closeScreen();
			}
			break;
			
		case BUTTON_SELECT:
			if (amountInfo[currentImage] == null) {
				amountInfo[currentImage] = new PhotoSizeAmountInfo(1, 1, 1, currentImage);
				numSelectedPhotos++;
			} else {
				amountInfo[currentImage] = null;
				numSelectedPhotos--;
			}
			updateButtons();
			break;
			
		case BUTTON_PRINT:
			output = PacketHelper.buildPacket(NetAction.REQUEST_PRINT);
			output.writeInt(mc.thePlayer.craftingInventory.windowId);
			output.writeByte(numSelectedPhotos);
			for (PhotoSizeAmountInfo info : amountInfo) {
				if (info != null) {
					info.writeTo(output);
				}
			}
			PacketDispatcher.sendPacketToServer(PacketHelper.finishPacket(output));
			break;
		}
	}
	
	private class RenameCallback implements IRenamePhotoCallback {

		private GuiContainerViewPhotos parent;

		public RenameCallback(GuiContainerViewPhotos parent) {
			this.parent = parent;
		}
		
		@Override
		public void nameChanged(String newName) {
			container.getSource().nameChanged(currentImage, newName);
        	ByteArrayDataOutput output = PacketHelper.buildPacket(NetAction.REQUEST_RENAME);
        	output.writeInt(mc.thePlayer.craftingInventory.windowId);
        	output.writeByte(currentImage);
        	output.writeUTF(newName);
        	PacketDispatcher.sendPacketToServer(PacketHelper.finishPacket(output));
        	mc.displayGuiScreen(parent);
		}

		@Override
		public void abort() {
			mc.displayGuiScreen(parent);
		}
		
	}
	
	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		for (int i = 0; i < source.numPhotos(); i++) {
			RenderEntityPhoto.unregisterUse(new PhotoSizeInfo(source.getPhotoInformation(i).getPhotoId(), 4, 4));
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) { }
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		// the photo
		RenderEntityPhoto.instance.bindTextureForPhotoSize(new PhotoSizeInfo(source.getPhotoInformation(currentImage).getPhotoId(), 4, 4));
		GL11.glColor3f(1, 1, 1);
		drawTexturedModalRect(width / 2 - 128, height / 2 - 128, 0, 0, 256, 256);

				
		if (source.canPrint()) {
			// the info how much paper you'll need
			mc.renderEngine.bindTexture(mc.renderEngine.getTexture("/gui/items.png"));
			drawTexturedModalRect(60, height - 88, 160, 48, 16, 16);
			String paperNeed = String.valueOf(currentPaperNeed);
			drawString(fontRenderer, paperNeed, 77 - fontRenderer.getStringWidth(paperNeed), height - 79, 0xffffff);
			
			// sizeX and sizeY display
			if (amountInfo[currentImage] != null) {
				drawString(fontRenderer, sizeStringX, width - 33 - sizeStringXWidth / 2, height - 24, 0xffffff);
				drawString(fontRenderer, sizeStringY, width - 20 - sizeStringYWidth / 2, 41, 0xffffff);
			}
		}
		
		// the subtitle
		drawString(fontRenderer, subtitle, width / 2 - subtitleWidth / 2, height - 20, 0xaae029);
	}
}
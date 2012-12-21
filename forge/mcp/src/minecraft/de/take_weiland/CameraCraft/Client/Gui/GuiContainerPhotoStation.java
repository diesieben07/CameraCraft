package de.take_weiland.CameraCraft.Client.Gui;

import net.minecraft.client.gui.GuiButton;

import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.common.network.PacketDispatcher;
import de.take_weiland.CameraCraft.Common.PhotoInformation;
import de.take_weiland.CameraCraft.Common.Gui.ContainerPhotoStation;
import de.take_weiland.CameraCraft.Common.Items.ItemPhotoStorage;
import de.take_weiland.CameraCraft.Common.Network.NetAction;
import de.take_weiland.CameraCraft.Common.Network.PacketHelper;
import de.take_weiland.CameraCraft.Common.TileEntities.TileEntityPhotoStation;

public class GuiContainerPhotoStation extends GuiGenericContainer {

	private int tickCount = 0;
	private GuiButton buttonViewPhotos;
	private final TileEntityPhotoStation tileEntity;
	
	public GuiContainerPhotoStation(ContainerPhotoStation container) {
		super(container);
		tileEntity = (TileEntityPhotoStation)container.getUpperInventory();
		progressBarX = 84;
		progressBarY = 63;
		progressBarTexX = 0;
		progressBarTexY = 250;
		progressBarWidth = 80;
		progressBarHeight = 6;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		controlList.add((buttonViewPhotos = new GuiButtonImage(0, guiLeft + 96, guiTop + 29, "/CameraCraft/tex.png", 20, 136)));
		updateButtons();
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int var2, int var3) {
		super.drawGuiContainerBackgroundLayer(partialTicks, var2, var3);
		tickCount++;
		
		if (!inventorySlots.getSlot(0).getHasStack()) {
			drawTexturedModalRect(guiLeft + 121, guiTop + 31, 176, tickCount < 100 ? 0 : 16, 16, 16);
		}
		if (tickCount == 200) {
			tickCount = 0;
		}
		updateButtons();
	}
	
	private void updateButtons() {
		buttonViewPhotos.enabled = tileEntity.canViewPhotos(mc.thePlayer);
	}
	
	@Override
	public void actionPerformed(GuiButton button) {
		super.actionPerformed(button);
		if (button.id == 0 && inventorySlots.getSlot(0).getHasStack()) {
			PhotoInformation[] infos = ItemPhotoStorage.getPhotosFromItemStack(inventorySlots.getSlot(0).getStack());
			if (infos.length > 0) {
				ByteArrayDataOutput output = PacketHelper.buildPacket(NetAction.REQUEST_VIEW_PHOTOS);
				output.writeInt(inventorySlots.windowId);
				PacketDispatcher.sendPacketToServer(PacketHelper.finishPacket(output));
			}
		}
	}
}
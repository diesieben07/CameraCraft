package de.take_weiland.CameraCraft.Client.Gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.StringTranslate;

import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.common.network.PacketDispatcher;
import de.take_weiland.CameraCraft.Common.CameraType;
import de.take_weiland.CameraCraft.Common.PhotoInformation;
import de.take_weiland.CameraCraft.Common.Gui.ContainerCamera;
import de.take_weiland.CameraCraft.Common.Inventory.InventoryCamera;
import de.take_weiland.CameraCraft.Common.Items.ItemPhotoStorage;
import de.take_weiland.CameraCraft.Common.Network.NetAction;
import de.take_weiland.CameraCraft.Common.Network.PacketHelper;
import de.take_weiland.CameraCraft.Common.TileEntities.TileEntityCamera;

public class GuiContainerCamera extends GuiGenericContainer {

	private final ContainerCamera container;
	
	public GuiContainerCamera(ContainerCamera container) {
		super(container);
		this.container = container;
	}
	
	@Override
	public void initGui() {
		super.initGui();		
		if (container.cameraInv.getType() == CameraType.DIGITAL) {
			controlList.add(new GuiButtonImage(0, guiLeft + 66, guiTop + 29, "/CameraCraft/tex.png", 20, 136));
		}
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		if (container.cameraInv.getType() == CameraType.DIGITAL) {
			int partialBattery;
			if (container.getUpperInventory() instanceof InventoryCamera) {
				partialBattery = ((InventoryCamera)container.getUpperInventory()).getPartialBattery();
			} else {
				partialBattery = ((TileEntityCamera)container.getUpperInventory()).getCameraInventory().getPartialBattery();
			}
			fontRenderer.drawString(StringTranslate.getInstance().translateKeyFormat("cameracraft.gui.photosLeft", String.valueOf(partialBattery)), 95, 53, 0x404040);
		}
	}

	@Override
	public void actionPerformed(GuiButton button) {
		super.actionPerformed(button);
		if (button.id == 0 && inventorySlots.getSlot(0).getHasStack()) {
			PhotoInformation[] infos = ItemPhotoStorage.getPhotosFromItemStack(inventorySlots.getSlot(0).getStack());
			if (infos.length > 0) {
				ByteArrayDataOutput output = PacketHelper.buildPacket(NetAction.REQUEST_VIEW_PHOTOS);
				output.writeInt(mc.thePlayer.openContainer.windowId);
				PacketDispatcher.sendPacketToServer(PacketHelper.finishPacket(output));
			}
		}
	}
}

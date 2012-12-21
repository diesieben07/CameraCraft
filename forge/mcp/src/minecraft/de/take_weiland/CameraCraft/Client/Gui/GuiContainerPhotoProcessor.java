package de.take_weiland.CameraCraft.Client.Gui;

import de.take_weiland.CameraCraft.Common.Gui.ContainerPhotoProcessor;

public class GuiContainerPhotoProcessor extends GuiGenericContainer {
	
	public GuiContainerPhotoProcessor(ContainerPhotoProcessor container) {
		super(container);
		progressBarX = 7;
		progressBarY = 36;
		progressBarTexX = 0;
		progressBarTexY = 166;
		progressBarWidth = 80;
		progressBarHeight = 6;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int var2, int var3) {
		super.drawGuiContainerBackgroundLayer(partialTicks, var2, var3);
		if (!inventorySlots.getSlot(1).getHasStack()) {
			drawTexturedModalRect(guiLeft + 121, guiTop + 31, 176, 0, 16, 16);
		}
	}
}
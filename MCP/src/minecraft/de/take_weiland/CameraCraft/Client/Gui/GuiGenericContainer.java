package de.take_weiland.CameraCraft.Client.Gui;

import org.lwjgl.opengl.GL11;

import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.common.network.PacketDispatcher;

import de.take_weiland.CameraCraft.Common.Gui.GenericContainer;
import de.take_weiland.CameraCraft.Common.Gui.ContainerPhotoProcessor;
import de.take_weiland.CameraCraft.Common.Inventory.IProgressInventory;
import de.take_weiland.CameraCraft.Common.Network.PacketHelper;
import de.take_weiland.CameraCraft.Common.Network.NetAction;
import de.take_weiland.CameraCraft.Common.TileEntities.TileEntityPhotoProcessor;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.IInventory;
import net.minecraft.src.StatCollector;

public class GuiGenericContainer extends GuiContainer {

	private GenericContainer container;
	
	protected int progressBarX = 0;
	protected int progressBarY = 0;
	protected int progressBarTexX = 0;
	protected int progressBarTexY = 0;
	protected int progressBarWidth = 0;
	protected int progressBarHeight = 0;
	
	public GuiGenericContainer(GenericContainer container) {
		super(container);
		this.container = container;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		GL11.glColor4f(1, 1, 1, 1);
		String tex = container.getGuiBackgroundTexture();
		if (tex != null) {
			int texture = mc.renderEngine.getTexture(tex);
			mc.renderEngine.bindTexture(texture);
			drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		}
		
		IInventory inv = container.getUpperInventory();
		
		if (inv != null && inv instanceof IProgressInventory) {
			IProgressInventory progressInventory = (IProgressInventory)inv;
			
			int scaledProgress = progressInventory.getProcessDuration() == 0 ? progressBarWidth : (int)(((float)progressInventory.getProcessTime() / (float)progressInventory.getProcessDuration()) * progressBarWidth);
			drawTexturedModalRect(guiLeft + progressBarX, guiTop + progressBarY, progressBarTexX, progressBarTexY, progressBarWidth - scaledProgress, progressBarHeight);
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		if (container.getUpperInventory() != null) {
			fontRenderer.drawString(StatCollector.translateToLocal(container.getUpperInventory().getInvName()), 8, 6, 0x404040);
		}
        if (container.getPlayerInventory() != null) {
        	fontRenderer.drawString(StatCollector.translateToLocal(container.getPlayerInventory().getInvName()), 8, this.ySize - 96 + 2, 0x404040);
        }
    }
}
package de.take_weiland.CameraCraft.Client.Gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.StringTranslate;

import org.lwjgl.input.Keyboard;

public class GuiRenamePhoto extends GuiScreen {
	private final IRenamePhotoCallback callback;
	private final String originalName;
	
	private GuiTextField textFieldName;
	
	private String heading;
	
	public GuiRenamePhoto(IRenamePhotoCallback callback, String originalName) {
		this.callback = callback;
		this.originalName = originalName;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		heading = StringTranslate.getInstance().translateKey("cameracraft.choosephotoname");
		Keyboard.enableRepeatEvents(true);
		textFieldName = new GuiTextField(fontRenderer, width / 2 - 150, 80, 300, 20);
		textFieldName.setText(originalName);
		textFieldName.setFocused(true);
	}
	
	@Override
	public void updateScreen() {
        textFieldName.updateCursorCounter();
    }
	
	@Override
	protected void keyTyped(char par1, int par2) {
        if (par2 == Keyboard.KEY_RETURN) {
        	callback.nameChanged(textFieldName.getText());
        } else if (par2 == Keyboard.KEY_ESCAPE) {
        	callback.abort();
        }
		textFieldName.textboxKeyTyped(par1, par2);
	}
	
	@Override
	public void drawScreen(int par1, int par2, float par3) {
		drawDefaultBackground();
		
		super.drawScreen(par1, par2, par3);
		
		textFieldName.drawTextBox();
		
		drawCenteredString(fontRenderer, heading, width / 2, 20, 0xfffff);
    }
	
	@Override
	public void onGuiClosed() {
    	Keyboard.enableRepeatEvents(false);
	}
}

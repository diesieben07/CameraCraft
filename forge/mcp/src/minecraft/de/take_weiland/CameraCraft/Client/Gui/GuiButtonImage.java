package de.take_weiland.CameraCraft.Client.Gui;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiButton;

import org.lwjgl.opengl.GL11;

public class GuiButtonImage extends GuiButton {
    
	private String texture;
	private int textureX;
	private int textureY;
	
	public GuiButtonImage(int id, int x, int y, String texture, int textureX, int textureY) {
        super(id, x, y, 20, 20, "");
        this.texture = texture;
        this.textureX = textureX;
        this.textureY = textureY;
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.drawButton) {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture(texture));
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            boolean mouseover = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            
            drawTexturedModalRect(xPosition, yPosition, textureX, textureY + height * getHoverState(mouseover), width, height);
        }
    }

	public int getTextureX() {
		return textureX;
	}

	public void setTextureX(int textureX) {
		this.textureX = textureX;
	}

	public int getTextureY() {
		return textureY;
	}

	public void setTextureY(int textureY) {
		this.textureY = textureY;
	}  
}

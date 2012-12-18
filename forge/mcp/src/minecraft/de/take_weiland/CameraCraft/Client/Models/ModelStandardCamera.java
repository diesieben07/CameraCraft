// Date: 18.10.2012 23:14:14
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX

package de.take_weiland.CameraCraft.Client.Models;

import net.minecraft.src.ModelBase;
import net.minecraft.src.ModelRenderer;

public class ModelStandardCamera extends ModelBase
{
  //fields
    ModelRenderer Main_Camera;
    ModelRenderer Button1;
    ModelRenderer Flash_Bottom;
    ModelRenderer Flash_Top;
    ModelRenderer Button_2;
  
  public ModelStandardCamera()
  {
    textureWidth = 64;
    textureHeight = 64;
    
      Main_Camera = new ModelRenderer(this, 0, 0);
      Main_Camera.addBox(0F, 0F, 0F, 14, 8, 1);
      Main_Camera.setRotationPoint(-7F, 16F, 0F);
      Main_Camera.setTextureSize(64, 64);
      Main_Camera.mirror = true;
      setRotation(Main_Camera, 0F, 0F, 0F);
      Button1 = new ModelRenderer(this, 0, 9);
      Button1.addBox(0F, 0F, 0F, 3, 1, 1);
      Button1.setRotationPoint(-6F, 15F, 0F);
      Button1.setTextureSize(64, 64);
      Button1.mirror = true;
      setRotation(Button1, 0F, 0F, 0F);
      Flash_Bottom = new ModelRenderer(this, 38, 0);
      Flash_Bottom.addBox(0F, 0F, 0F, 4, 1, 1);
      Flash_Bottom.setRotationPoint(-2F, 15F, 0F);
      Flash_Bottom.setTextureSize(64, 64);
      Flash_Bottom.mirror = true;
      setRotation(Flash_Bottom, 0F, 0F, 0F);
      Flash_Top = new ModelRenderer(this, 48, 0);
      Flash_Top.addBox(0F, 0F, 0F, 2, 1, 1);
      Flash_Top.setRotationPoint(-1F, 14F, 0F);
      Flash_Top.setTextureSize(64, 64);
      Flash_Top.mirror = true;
      setRotation(Flash_Top, 0F, 0F, 0F);
      Button_2 = new ModelRenderer(this, 30, 0);
      Button_2.addBox(0F, 0F, 0F, 3, 2, 1);
      Button_2.setRotationPoint(3F, 14F, 0F);
      Button_2.setTextureSize(64, 64);
      Button_2.mirror = true;
      setRotation(Button_2, 0F, 0F, 0F);
  }
  
  public void renderCamera()
  {
    Main_Camera.render(0.0625F);
    Button1.render(0.0625F);
    Flash_Bottom.render(0.0625F);
    Flash_Top.render(0.0625F);
    Button_2.render(0.0625F);
  }
  
  private void setRotation(ModelRenderer model, float x, float y, float z)
  {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
}
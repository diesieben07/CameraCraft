package de.take_weiland.CameraCraft.Client;

import java.util.EnumSet;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;

import com.google.common.io.ByteArrayDataOutput;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.KeyBinding;
import net.minecraft.src.Packet250CustomPayload;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import de.take_weiland.CameraCraft.Common.CameraCraft;
import de.take_weiland.CameraCraft.Common.Items.ItemCamera;
import de.take_weiland.CameraCraft.Common.Network.PacketHelper;
import de.take_weiland.CameraCraft.Common.Network.NetAction;

public class CameraCraftKeyHandler extends KeyHandler {

	private static CameraCraftKeyHandler instance;
	
	
	private CameraCraftKeyHandler() {
		super(new KeyBinding[] {new KeyBinding("key.takephoto", Keyboard.KEY_P)}, new boolean[] {false});
	}
	
	public static CameraCraftKeyHandler instance() {
		if (instance == null) {
			instance = new CameraCraftKeyHandler();
		}
		return instance;
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.PLAYER);
	}

	@Override
	public String getLabel() {
		return "key.takephoto";
	}

	@Override
	public void keyDown(EnumSet<TickType> types, KeyBinding theKey, boolean tickEnd, boolean isRepeat) {
		
		Minecraft mc = FMLClientHandler.instance().getClient();
		
		if (mc.theWorld != null && tickEnd && mc.currentScreen == null && ItemCamera.canTakePhoto(mc.thePlayer.getCurrentEquippedItem())) {
			ByteArrayDataOutput data = PacketHelper.buildPacket(NetAction.REQUEST_PHOTO);
			Packet250CustomPayload packet = PacketHelper.finishPacket(data);
			PacketDispatcher.sendPacketToServer(packet);
		}
		
		/*Minecraft mc = FMLClientHandler.instance().getClient();
		// TODO: some basic code for later third person photos
		
		if (mc.renderViewEntity instanceof EntityPlayer) {
			System.out.println("key");
			for (Entity ent : (List<Entity>)mc.theWorld.getLoadedEntityList()) {
				if (ent instanceof EntityLiving && (new Random()).nextInt(5) == 1) {
					mc.renderViewEntity = (EntityLiving)ent;
				}
			}
		} else {
			mc.renderViewEntity = mc.thePlayer;
		}*/
	}

	@Override
	public void keyUp(EnumSet<TickType> types, KeyBinding theKey, boolean tickEnd) { }

}

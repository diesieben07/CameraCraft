package de.take_weiland.CameraCraft.Client;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Entity;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class CameraCraftClientTickHandler implements ITickHandler {

	private static final CameraCraftClientTickHandler instance = new CameraCraftClientTickHandler();
	
	private static AfterTickCallback watching = null;
	private static boolean shouldExecute = false;
	
	public static CameraCraftClientTickHandler instance() {
		return instance;
	}
	
	public synchronized static void watchNextTick(AfterTickCallback callback) {
		watching = callback;
	}
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) { }

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		if (watching != null) {
			if (shouldExecute) {
				watching.afterTick();
				watching = null;
				shouldExecute = false;
			} else {
				shouldExecute = true;
			}
		}
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.RENDER);
	}

	@Override
	public String getLabel() {
		return "CameraCraftTicker";
	}
}
package de.take_weiland.CameraCraft.Common;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.src.EntityPlayer;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import de.take_weiland.CameraCraft.Common.Gui.ContainerCamera;
import de.take_weiland.CameraCraft.Common.Items.ItemCamera;

public class CommonTickHandler implements ITickHandler {

	private static final CommonTickHandler instance = new CommonTickHandler();
	
	public static CommonTickHandler instance() {
		return instance;
	}
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		Iterator<Entry<EntityPlayer,Integer>> iterator = ItemCamera.takePhotoCooldown.entrySet().iterator();
		
		while (iterator.hasNext()) {
			Entry<EntityPlayer,Integer> entry = iterator.next();
			entry.setValue(entry.getValue() - 1);
			if (entry.getValue() == 0) {
				iterator.remove();
			}
		}
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) { }

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.SERVER, TickType.CLIENT);
	}

	@Override
	public String getLabel() {
		return "CameraCraftTicker";
	}

}
package de.take_weiland.CameraCraft.Client;

import de.take_weiland.CameraCraft.Common.CameraCraft;
import net.minecraft.src.WorldClient;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;


public class CameraCraftClientEventHandler {
	
	@ForgeSubscribe
	public void onSoundLoad(SoundLoadEvent event) {
		event.manager.soundPoolSounds.addSound("cameracraft/takephoto.ogg", CameraCraft.class.getResource("/CameraCraft/cameraclick.ogg"));
		event.manager.soundPoolSounds.addSound("cameracraft/printer.ogg", CameraCraft.class.getResource("/CameraCraft/printer.ogg"));
	}
	
	@ForgeSubscribe
	public void onWorldLoad(WorldEvent.Load event) {
		if (event.world instanceof WorldClient) {
			event.world.addWorldAccess(CameraCraftWorldAccess.instance());
		}
	}
}
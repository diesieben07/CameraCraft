package de.take_weiland.CameraCraft.Common.nei;

import codechicken.nei.api.IConfigureNEI;
import de.take_weiland.CameraCraft.Common.CameraCraft;

public class NEICameraCraftConfig implements IConfigureNEI {

	@Override
	public void loadConfig() {
		
	}

	@Override
	public String getName() {
		return "CameraCraft";
	}

	@Override
	public String getVersion() {
		return CameraCraft.VERSION;
	}

}

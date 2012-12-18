package de.take_weiland.CameraCraft.Common;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import cpw.mods.fml.common.registry.LanguageRegistry;

public class Localization {
	
	public static void addLocalizations() {
		try {
			InputStreamReader reader = new InputStreamReader(CameraCraft.class.getResourceAsStream("/CameraCraft/CameraCraft.lang"));
			BufferedReader in = new BufferedReader(reader);
			
			String line;
			while ((line = in.readLine()) != null) {
				if (!line.startsWith("#")) {
					int colonIndex = line.indexOf(':');
					if (colonIndex > 0) {
						String key = line.substring(0, colonIndex);
						String value = line.substring(colonIndex + 1);
						LanguageRegistry.instance().addStringLocalization(key, value);
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Failed to register localizations");
		}
	}
}

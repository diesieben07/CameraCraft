package de.take_weiland.CameraCraft.Common;

import net.minecraftforge.common.Configuration;

public class ConfigurationManager {
	// generic stuff
	public static boolean allowTeleport;
	public static boolean allowCraftTeleporter;
	public static boolean allowFreeTeleport;
	public static boolean enableShiftRightclick;
	public static int ic2BatteryCost = -1;
	public static boolean foreignScreenshotOnTripod;
	
	// block Ids
	public static int photoProcessorId;
	public static int photoStationId;
	public static int tripodId;
	public static int cameraBlockId;
	public static int teleportationEnablerId;
	
	// item Ids
	public static int cameraId;
	public static int photoId;
	public static int batteryId;
	public static int filmId;
	public static int memoryCardId;
	public static int photoFilterId;
	public static int teleportationBatteryId;
	
	
	public static void initDefaults(Configuration conf) {
		allowTeleport = conf.get(Configuration.CATEGORY_GENERAL, "allowTeleport", true).getBoolean(true);
		allowCraftTeleporter = conf.get(Configuration.CATEGORY_GENERAL, "allowCraftTeleporter", false).getBoolean(false);
		allowFreeTeleport = conf.get(Configuration.CATEGORY_GENERAL, "allowFreeTeleport", false).getBoolean(false);
		enableShiftRightclick = conf.get(Configuration.CATEGORY_GENERAL, "shiftRightclick", true).getBoolean(true);
		//ic2BatteryCost = conf.get(Configuration.CATEGORY_GENERAL, "ic2BatteryCost", 1).getInt();
		foreignScreenshotOnTripod = conf.get(Configuration.CATEGORY_GENERAL, "allowForeignTripodPhoto", true).getBoolean(true);
		
		photoProcessorId = conf.getBlock("photoProcessorID", 1000).getInt();
		photoStationId = conf.getBlock("printerID", 1001).getInt();
		tripodId = conf.getBlock("tripodID", 1002).getInt();
		cameraBlockId = conf.getBlock("cameraBlockID", 1003).getInt();
		teleportationEnablerId = conf.getBlock("teleportationEnablerID", 1004).getInt();
		
		cameraId = conf.getItem("cameraID", 240).getInt();
		photoId = conf.getItem("photoID", 241).getInt();
		batteryId = conf.getItem("batteryID", 242).getInt();
		filmId = conf.getItem("filmID", 243).getInt();
		memoryCardId = conf.getItem("memoryCardID", 244).getInt();
		photoFilterId = conf.getItem("photoFilterID", 245).getInt();
		teleportationBatteryId = conf.getItem("teleportationBatteryID", 246).getInt();
	}
}

package de.take_weiland.CameraCraft.Common;

import static de.take_weiland.CameraCraft.Common.CameraCraft.logger;

import java.lang.reflect.Method;

import net.minecraft.item.ItemStack;

public class IndustrialCraftManager {
	public static boolean isICInstalled = false;
	
	private static Class<?> interfaceElectricItem;
	private static Method methodCanProvideEnergy;

	private static Class<?> electricItemHelperClass;
	private static Method methodHasEnoughPower;
	private static Method methodDischarge;	
	
	public static ItemStack icBattery;
	
	public static void init() {
		try {
			logger.fine("Attempting to Connect to IC2 API...");
			Class<?> icItems = Class.forName("ic2.api.Items");			
			
			interfaceElectricItem = Class.forName("ic2.api.IElectricItem");
			methodCanProvideEnergy = interfaceElectricItem.getDeclaredMethod("canProvideEnergy");
			
			electricItemHelperClass = Class.forName("ic2.api.ElectricItem");
			methodHasEnoughPower = electricItemHelperClass.getDeclaredMethod("canUse", ItemStack.class, int.class);
			methodDischarge = electricItemHelperClass.getDeclaredMethod("discharge", ItemStack.class, int.class, int.class, boolean.class, boolean.class);
			
			Method getItem = icItems.getDeclaredMethod("getItem", String.class);
			
			//isICInstalled = true;
			//logger.info("IC2 API found. Batteries will be used.");
		} catch (ClassNotFoundException e) {
			logger.info("IC2 API not found.");
		} catch (NoSuchMethodException e) {
			connectionFailed();
		} catch (SecurityException e) {
			connectionFailed();
		}
	}
	
	private static void connectionFailed() {
		logger.warning("IC2 API found but Communication failed!");
	}

	public static boolean canItemProvideEnergy(ItemStack itemStack) {
		if (!isICInstalled || itemStack == null) {
			return false;
		} else if (!interfaceElectricItem.isAssignableFrom(itemStack.getItem().getClass())) {
			return false;
		} else {
			try {
				return (Boolean)methodCanProvideEnergy.invoke(itemStack.getItem());
			} catch (Exception e) {
				
				return false;
			}
		}
	}

	public static boolean hasEnoughPower(ItemStack itemStack, int amount) {
		if (!canItemProvideEnergy(itemStack)) {
			return false;
		} else {
			try {
				return (Boolean)methodHasEnoughPower.invoke(null, itemStack, amount);
			} catch (Exception e) {
				logger.throwing("IndustrialCraftManager", "hasEnoughPower", e);
				return false;
			}
		}
	}
	
	public static int discharge(ItemStack stack, int amount) {
		if (!isICInstalled || stack == null) {
			return 0;
		} else if (!interfaceElectricItem.isAssignableFrom(stack.getItem().getClass())) {
			return 0;
		} else {
			try {
				// 											  ItemStack, amount, tier, ignoreLimit, simulate
				return (Integer)methodDischarge.invoke(null, stack, amount, 1, true, false);
			} catch (Exception e) {
				logger.throwing("IndustrialCraftManager", "discharge", e);
				return 0;
			}
		}
	}
}
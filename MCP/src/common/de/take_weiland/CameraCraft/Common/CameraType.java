package de.take_weiland.CameraCraft.Common;

import net.minecraft.src.Slot;

public enum CameraType {
	
	STANDARD(2, 1, 5, "item.camera.standard", "/CameraCraft/GUIStandardCamera.png"),
	DIGITAL(3, 2, 9, "item.camera.digital", "/CameraCraft/GUIDigitalCamera.png");
	
	private int numSlots;
	private int textureIndex;
	private String name;
	private String guiBackgroundTexture;
	private int slotsRequiredFull;
	
	private static final CameraType[] values = values();
	
	private CameraType(int numSlots, int slotsRequiredFull, int textureIndex, String name, String guiBackgroundTexture) {
		this.numSlots = numSlots;
		this.slotsRequiredFull = slotsRequiredFull;
		this.textureIndex = textureIndex;
		this.name = name;
		this.guiBackgroundTexture = guiBackgroundTexture;
	}
	
	public int numSlots() {
		return numSlots;
	}
	
	public int slotsRequiredFull() {
		return slotsRequiredFull;
	}
	
	public int texture() {
		return textureIndex;
	}
	
	public String nameLocalization() {
		return name;
	}
	
	public String guiBackground() {
		return guiBackgroundTexture;
	}
	
	public static CameraType fromItemDamage(int damage) {
		if (damage >= values.length) {
			damage = 0;
		}
		return values[damage];
	}
	
	public int toItemDamage() {
		return ordinal();
	}
}

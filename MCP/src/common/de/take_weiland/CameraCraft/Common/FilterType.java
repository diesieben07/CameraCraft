package de.take_weiland.CameraCraft.Common;

public enum FilterType {
	
	TRANSPARENT("none", 24), RED("red", 17), GREEN("green", 18), BLUE("blue", 19), YELLOW("yellow", 20);
	
	private String colorName;
	private int icon;
	
	private FilterType(String colorName, int icon) {
		this.colorName = colorName;
		this.icon = icon;
	}

	public String getColorName() {
		return colorName;
	}

	public int getIcon() {
		return icon;
	}
	
	public static FilterType fromItemDamage(int damage) {
		switch (damage) {
		case 1:
			return RED;
		case 2:
			return GREEN;
		case 3:
			return BLUE;
		case 4:
			return YELLOW;
		default:
			return TRANSPARENT;
		}
	}
	
	public int toItemDamage() {
		switch (this) {
		case RED:
			return 1;
		case GREEN:
			return 2;
		case BLUE:
			return 3;
		case YELLOW:
			return 4;
		default:
			return 0;
		}
	}

	public static FilterType fromDyeDamage(int itemDamage) {
		switch (itemDamage) {
		case 1:
			return RED;
		case 2:
			return GREEN;
		case 4:
			return BLUE;
		case 11:
			return YELLOW;
		default:
			return TRANSPARENT;
		}
	}
}
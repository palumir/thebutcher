package player;

import units.Unit;

// Global player data.
public class Player  {
	private static Unit selectedUnit;
	
	public void initPlayerData() {
		
	}

	public static Unit getSelectedUnit() {
		return selectedUnit;
	}

	public static void setSelectedUnit(Unit s) {
		selectedUnit = s;
	}
}

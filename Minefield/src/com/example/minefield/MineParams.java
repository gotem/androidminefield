package com.example.minefield;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class MineParams {

	float maxDistance;
	float triggerRadius;
	float warnRadius;
	Integer numberMines;
	Integer numberPrizes;
	
	public MineParams(GameService service) {
		// TODO Auto-generated constructor stub
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(service);
		maxDistance = Float.parseFloat(sharedPref.getString("maxDistance", "0"));
		triggerRadius = Float.parseFloat(sharedPref.getString("triggerRadius", "5"));
		warnRadius = Float.parseFloat(sharedPref.getString("warnRadius", "10"));
		numberMines = sharedPref.getInt("numberMines", 1);
		numberPrizes = sharedPref.getInt("numberPrizes", 0);
	}

}

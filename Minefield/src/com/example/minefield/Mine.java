package com.example.minefield;

import android.location.Location;

public class Mine {

	Boolean isTriggered;
	Boolean isNear;
	Integer value;
	Location location;
	
	public Mine(Integer p_val,MineParams p_params,Location p_centerlocation)
	{
		this.value = p_val;
		this.isNear = false;
		this.isTriggered = false;
	}
};

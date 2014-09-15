package com.insomniagames.minefield;

import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class Mine {

	Boolean isTriggered;
	Boolean isNear;
	Integer value;
	Location location;
	public static int PrizeValue = 0;
	public static int MineValue = 1;
	private static int latmeters = 110852;
	private static int longmeters = 96486;
	
	public Mine(Integer p_val,MineParams p_params,Location p_centerlocation)
	{
		this.value = p_val;
		this.isNear = false;
		this.isTriggered = false;
		this.location = new Location(LocationManager.GPS_PROVIDER);
		Double latdistance = (Math.random()*p_params.maxDistance*2)-p_params.maxDistance;
		Double longdistance = (Math.random()*p_params.maxDistance*2)-p_params.maxDistance;
		location.setLatitude(p_centerlocation.getLatitude() + (latdistance/latmeters));
		location.setLongitude(p_centerlocation.getLongitude() + (longdistance/longmeters));
    	Log.d("MineField","mine value:"+value+" "+location.getLatitude()+" "+location.getLongitude());

	}
	
	public Double DistanceTo(Location p_location)
	{
		Double latdiff = Math.abs(p_location.getLatitude() - location.getLatitude())*latmeters;
		Double longdiff = Math.abs(p_location.getLongitude() - location.getLongitude())*longmeters;
		return Math.sqrt((latdiff *latdiff ) + (longdiff*longdiff));
	}
};

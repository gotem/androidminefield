package com.example.minefield;

import android.location.Location;

public class Mine {

	Boolean isTriggered;
	Boolean isNear;
	Integer value;
	Location location;
	private static int EarthRadius = 6371000;
	private static int latmeters = 110852;
	private static int longmeters = 96486;
	
	public Mine(Integer p_val,MineParams p_params,Location p_centerlocation)
	{
		this.value = p_val;
		this.isNear = false;
		this.isTriggered = false;
		Double latdistance = (Math.random()*p_params.maxDistance*2)-p_params.maxDistance;
		Double longdistance = (Math.random()*p_params.maxDistance*2)-p_params.maxDistance;
		location.setLatitude(p_centerlocation.getLatitude() + (latdistance/latmeters));
		location.setLongitude(p_centerlocation.getLongitude() + (longdistance/longmeters));
	}
	
	public Double DistanceTo(Location p_location)
	{
		Double p1 = (p_location.getLongitude() - location.getLongitude()) * Math.cos ( 0.5*(p_location.getLatitude()+location.getLatitude()) ); //convert lat/lon to radians
		Double	p2 = (p_location.getLatitude() - location.getLatitude());
		return EarthRadius * Math.sqrt( p1*p1 + p2*p2);
	}
};

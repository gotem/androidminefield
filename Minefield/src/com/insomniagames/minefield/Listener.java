package com.insomniagames.minefield;

import java.util.ArrayList;

import android.location.*;
import android.os.Bundle;
import android.util.Log;

public class Listener implements LocationListener
{
	private Boolean initialized;
	private MineParams params;
	private ArrayList<Mine> mines;
	private ArrayList<Mine> prizes;
	private GameService service;
	
	
	public Listener(GameService p_service)
	{
		Log.d("MineField","listener created");
		service = p_service;
		initialized = false;
		params = service.params;
	}
	
    public void onLocationChanged(Location location) {
		Log.d("MineField","location changed");
		Double mindistance = 2000.0;
    	if(!initialized)
    	{
    		initialized = true;
    		Initialize(location);
    	}
    	for (Mine mine : mines)
    	{
    		Double distance = mine.DistanceTo(location);
    		if(distance<mindistance)
    			mindistance = distance;
    	    if (distance <= params.warnRadius && mine.isNear == false )
    	    {
    	    	//warn is near
    	    	Warn(mine);
    	    }
    	    if (distance <= params.triggerRadius && mine.isTriggered == false )
    	    {
    	    	Trigger(mine);
    	    }
    	}
    	for (Mine mine : prizes)
    	{
    		
    		Double distance = mine.DistanceTo(location);
    		if(distance<mindistance)
    			mindistance = distance;
    	    if (distance <= params.warnRadius && mine.isNear == false )
    	    {
    	    	Warn(mine);
    	    }
    	    if (distance <= params.triggerRadius && mine.isTriggered == false )
    	    {
    	    	Trigger(mine);
    	    }
    	}
    	if (params.debug)
    	{
    		service.debugUpdate(location,mindistance);
    	}
    }

    public void Initialize(Location location)
    {
    	Log.d("MineField","center "+location.getLatitude()+" "+location.getLongitude());
    	mines = new ArrayList<Mine>();
    	prizes = new ArrayList<Mine>();
    	for(int i = 0;i<params.numberMines;i++)
    	{
    		mines.add( new Mine(Mine.MineValue,params,location)); 		
    	}
    	for(int i = 0;i<params.numberPrizes;i++)
    	{
    		prizes.add( new Mine(Mine.PrizeValue,params,location));
    	}
    	service.SetInitialized();
    	if(params.debug)
    	{
    		service.debug(location, mines, prizes);
    	}
    }
    
    private void Warn(Mine mine)
    {
    	mine.isNear = true;
    	service.Warn(mine.value);
    }
    
    private void Trigger(Mine mine)
    {
    	mine.isTriggered = true;
    	service.Trigger(mine.value);
    }
    
    public void onStatusChanged(String provider, int status, Bundle extras) 
    {
    	Log.d("MineField","status changed");
    }

    public void onProviderEnabled(String provider) 
    {
    	Log.d("MineField","provider enabled");
    }

    public void onProviderDisabled(String provider) 
    {
    	Log.d("MineField","provider disabled");
    }

  };

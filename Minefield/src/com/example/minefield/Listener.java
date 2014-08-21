package com.example.minefield;

import java.util.ArrayList;

import android.location.*;
import android.os.Bundle;
import android.widget.Toast;

public class Listener implements LocationListener
{
	private Boolean initialized;
	private MineParams params;
	private ArrayList<Mine> mines;
	private ArrayList<Mine> prizes;
	private GameService service;
	
	
	public Listener(GameService p_service)
	{
		service = p_service;
	}
	
    public void onLocationChanged(Location location) {
    	if(!initialized)
    	{
    		initialized = true;
    		Initialize(location);
    	}
    	for (Mine mine : mines)
    	{
    		Double distance = mine.DistanceTo(location);
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
    	    if (distance <= params.warnRadius && mine.isNear == false )
    	    {
    	    	Warn(mine);
    	    }
    	    if (distance <= params.triggerRadius && mine.isTriggered == false )
    	    {
    	    	Trigger(mine);
    	    }
    	}
    }

    private void Initialize(Location location)
    {
    	for(int i = 0;i<params.numberMines;i++)
    	{
    		mines.add( new Mine(1,params,location));
    	}
    	for(int i = 0;i<params.numberPrizes;i++)
    	{
    		prizes.add( new Mine(0,params,location));
    	}
    	service.handler.post(new Runnable() {
    		   @Override
    		   public void run() {
    			   Toast.makeText(service.getApplicationContext(), "Location Initialized", Toast.LENGTH_SHORT).show();
    		   }
    		});
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
    
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    public void onProviderEnabled(String provider) {}

    public void onProviderDisabled(String provider) {}

  };

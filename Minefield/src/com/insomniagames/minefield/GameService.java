package com.insomniagames.minefield;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;

public class GameService extends IntentService {

	private static final int ONGOING_NOTIFICATION_ID = 1;
	public static final String BROADCAST_ACTION = "com.insomnigames.minefield.status";
	public Handler handler;
	private Intent broadcastintent;
	private Integer traps;
	private Integer prizes;
	private Boolean initialized;
	private Listener listener;
	private String debug;
	private String debugupdate;
	MineParams params;
	
	
	public GameService() {
		super("GameService");
		// TODO Auto-generated constructor stub
		traps = 0;
		prizes = 0;
		initialized = false;
		traps = Integer.valueOf(0);
		prizes = Integer.valueOf(0);
		debug="";
		debugupdate="";
	}

	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		params = (MineParams) intent.getParcelableExtra("param");
		startMonitoring();
	   handler = new Handler();
	   broadcastintent = new Intent(BROADCAST_ACTION);
	   return super.onStartCommand(intent, flags, startId);
	}
	/**
	   * The IntentService calls this method from the default worker thread with
	   * the intent that started the service. When this method returns, IntentService
	   * stops the service, as appropriate.
	   */
	  @Override
	  protected void onHandleIntent(Intent intent) {
		  

			startForeground(ONGOING_NOTIFICATION_ID, getMyActivityNotification("Starting..."));
			Boolean stopping=false;
	      while(!stopping)
	      {
	    	  synchronized (this) {

	              try {
	                  wait(5000);
	                  Status();
	              } catch (Exception e) {
	              }
	    	  }
	      }
	  }
	  
	  @SuppressWarnings("deprecation")
	private Notification getMyActivityNotification(String text){
	        // The PendingIntent to launch our activity if the user selects
	        // this notification
	        CharSequence title = getText(R.string.app_name);
	        PendingIntent contentIntent = PendingIntent.getActivity(this,
	                0, new Intent(this, MainActivity.class), 0);
	        return new Notification.Builder(this)
	                .setContentTitle(title)
	                .setContentText(text)
	                .setSmallIcon(R.drawable.ic_launcher)
	                .setContentIntent(contentIntent).setDefaults(Notification.DEFAULT_ALL).getNotification();     
	}
	  
		public void startMonitoring()
		{
			listener = new Listener(this);
			LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
			
			Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if(loc!=null)
			{
				if(loc.getTime() > (new Date().getTime() - 1000*2*60)) //2Minutes back
				{
					listener.Initialize(loc);
				}
			}
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
		}
		
		public void UpdateNotification(String text)
		{
            Notification notification = getMyActivityNotification(text);
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(ONGOING_NOTIFICATION_ID, notification);
		}
		
		public void Warn(Integer value)
		{
			String object="";
			if(value==Mine.PrizeValue)
				object="Prize";
			else
				object = "Mine";
			UpdateNotification("You are near a "+object);
		}

		public void Trigger(Integer value)
		{
			if(value == Mine.MineValue)
			{
				traps++;
				UpdateNotification("Trap Triggered!");
			}
			else
			{
				prizes++;
				UpdateNotification("You have found a prize!");
			}
		}
		public void debugUpdate(Location location,Double distance)
		{
			DecimalFormat frmt = new DecimalFormat("#.0#####");
			debugupdate = "current location: "+location.getLatitude()+","+location.getLongitude()+"<br>";
			debugupdate += " closest object at "+frmt.format(distance)+" meters <br>";
			debugupdate += "<a href='http://maps.googleapis.com/maps/api/staticmap?size=800x800&markers=color:blue|label:C|"+frmt.format(location.getLatitude())+","+frmt.format(location.getLongitude())
					+"&key=AIzaSyBSayrSS8eOrr3xoPcFIsmi03gcIczDg_o'>current location</a><br>";
		}
		
		public void SetInitialized()
		{
			initialized = true;
			UpdateNotification("Game Started!");
		}
		
	    
	    public void debug(Location location,ArrayList<Mine> mines,ArrayList<Mine> prizes)
	    {
	    	String sentence="http://maps.googleapis.com/maps/api/staticmap?size=800x800";
	    	DecimalFormat frmt = new DecimalFormat("#.0#####");
	    	sentence +="&markers=color:blue|label:C|"+frmt.format(location.getLatitude())+","+frmt.format(location.getLongitude());
	    	for (Mine mine : mines)
	    	{
	    		sentence += "&markers=color:red|label:M|"+frmt.format(mine.location.getLatitude())+","+frmt.format(mine.location.getLongitude());
	    	}
	    	for (Mine mine : prizes)
	    	{
	    		sentence += "&markers=color:green|label:P|"+frmt.format(mine.location.getLatitude())+","+frmt.format(mine.location.getLongitude());
	    	}
	    	sentence +="&key=AIzaSyBSayrSS8eOrr3xoPcFIsmi03gcIczDg_o";
	    	debug=sentence;
	    }
		
	    public void Status()
	    {
	        broadcastintent.putExtra("traps", traps);
	        broadcastintent.putExtra("prizes", prizes);
	        broadcastintent.putExtra("initialized", initialized);
	        broadcastintent.putExtra("debug", debug);
	        broadcastintent.putExtra("debugextra", debugupdate);
	        sendBroadcast(broadcastintent);
	    }
}

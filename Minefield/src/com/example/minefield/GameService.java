package com.example.minefield;

import java.util.ArrayList;
import java.util.Date;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

public class GameService extends IntentService {

	private static final int ONGOING_NOTIFICATION_ID = 1;
	public static final String BROADCAST_ACTION = "com.example.minefield.status";
	public Handler handler;
	private Intent broadcastintent;
	private Integer traps;
	private Integer prizes;
	private Boolean initialized;
	private Listener listener;
	private String debug;
	MineParams params;
	
	
	public GameService() {
		super("GameService");
		// TODO Auto-generated constructor stub
		traps = 0;
		prizes = 0;
		initialized = false;
		traps = new Integer(0);
		prizes = new Integer(0);
		debug="";
	}

	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		params = (MineParams) intent.getParcelableExtra("param");
		startMonitoring();
	   handler = new Handler();
	   broadcastintent = new Intent(BROADCAST_ACTION);
	   handler.removeCallbacks(UpdateStatus);
       handler.postDelayed(UpdateStatus, 1000); // 1 second   
	   return super.onStartCommand(intent, flags, startId);
	}
	/**
	   * The IntentService calls this method from the default worker thread with
	   * the intent that started the service. When this method returns, IntentService
	   * stops the service, as appropriate.
	   */
	  @Override
	  protected void onHandleIntent(Intent intent) {
		  
		  
		  /*Notification notification = new Notification(R.drawable.ic_launcher, getText(R.string.app_name),
			        System.currentTimeMillis());
			Intent notificationIntent = new Intent(this, MainActivity.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
			notification.setLatestEventInfo(this, getText(R.string.app_name),
			        "Starting", pendingIntent);*/
			startForeground(ONGOING_NOTIFICATION_ID, getMyActivityNotification("Starting..."));
			Boolean stopping=false;
	      while(!stopping)
	      {
	    	  synchronized (this) {

	              try {
	                  wait(5000);
	              } catch (Exception e) {
	              }
	    	  }
	      }
	  }
	  
	  private Notification getMyActivityNotification(String text){
	        // The PendingIntent to launch our activity if the user selects
	        // this notification
	        CharSequence title = getText(R.string.app_name);
	        PendingIntent contentIntent = PendingIntent.getActivity(this,
	                0, new Intent(this, MainActivity.class), 0);
	        //Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
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
			/*Criteria criteria = new Criteria();
			criteria.setAccuracy(Criteria.ACCURACY_FINE);
			locationManager.requestLocationUpdates(0L,0f,criteria,listener,null);*/
			
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
		
		public void SetInitialized()
		{
			initialized = true;
			UpdateNotification("Game Started!");
		}
		
		private Runnable UpdateStatus = new Runnable() {
	        public void run() {
	            Status();            
	            handler.postDelayed(this, 5000); // 5 seconds
	        }
	    };  
	    
	    public void debug(Location location,ArrayList<Mine> mines,ArrayList<Mine> prizes)
	    {
	    	String sentence="";
	    	sentence += "Center: "+location.getLatitude()+", "+location.getLongitude()+"\n";
	    	for (Mine mine : mines)
	    	{
	    		sentence += "Mine: "+mine.location.getLatitude()+", "+mine.location.getLongitude()+"\n";
	    	}
	    	for (Mine mine : prizes)
	    	{
	    		sentence += "Prize: "+mine.location.getLatitude()+", "+mine.location.getLongitude()+"\n";
	    	}
	    	debug=sentence;
	    }
		
	    public void Status()
	    {
	        broadcastintent.putExtra("traps", traps);
	        broadcastintent.putExtra("prizes", prizes);
	        broadcastintent.putExtra("initialized", initialized);
	        broadcastintent.putExtra("debug", debug);
	        sendBroadcast(broadcastintent);
	    }
}

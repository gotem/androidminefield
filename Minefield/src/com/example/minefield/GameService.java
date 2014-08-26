package com.example.minefield;

import java.util.Date;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
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
	MineParams params;
	
	
	public GameService() {
		super("GameService");
		// TODO Auto-generated constructor stub
		traps = 0;
		prizes = 0;
		initialized = false;
	}

	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
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
		  
		  
		  Notification notification = new Notification(R.drawable.ic_launcher, getText(R.string.app_name),
			        System.currentTimeMillis());
			Intent notificationIntent = new Intent(this, MainActivity.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
			notification.setLatestEventInfo(this, getText(R.string.app_name),
			        getText(R.string.hello_world), pendingIntent);
			params = (MineParams) intent.getParcelableExtra("param");
			startForeground(ONGOING_NOTIFICATION_ID, notification);
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
		
		public void Warn(Integer value)
		{
			
		}

		public void Trigger(Integer value)
		{
			
		}
		
		public void SetInitialized()
		{
			initialized = true;
		}
		
		private Runnable UpdateStatus = new Runnable() {
	        public void run() {
	            Status();            
	            handler.postDelayed(this, 5000); // 5 seconds
	        }
	    };  
		
	    public void Status()
	    {
	        broadcastintent.putExtra("traps", traps);
	        broadcastintent.putExtra("prizes", prizes);
	        broadcastintent.putExtra("initializes", initialized);
	        sendBroadcast(broadcastintent);
	    }
}

package com.example.minefield;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

public class GameService extends IntentService {

	private static final int ONGOING_NOTIFICATION_ID = 1;
	public Handler handler;
	public GameService() {
		super("GameService");
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
	   handler = new Handler();
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
			startForeground(ONGOING_NOTIFICATION_ID, notification);
			startMonitoring();
			Boolean stopping=false;
	      while(!stopping)
	      {
	    	  synchronized (this) {

	              try {
	                  wait(100000);
	              } catch (Exception e) {
	              }
	    	  }
	      }
	  }
	  
		public void startMonitoring()
		{
			LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new Listener(this));
		}

}

package com.example.minefield;

import java.util.Date;

import android.app.Activity;
import android.app.ActionBar;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if(isMyServiceRunning(GameService.class))
			((android.widget.Button)findViewById(R.id.startbutton)).setText("Stop");
		else
			((android.widget.Button)findViewById(R.id.startbutton)).setText("Start");
		if (savedInstanceState == null) {
			/*getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();*/
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent intent = new Intent(this, LaunchGame.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	  @Override
	    public void onResume() {
	        super.onResume();        
	        registerReceiver(broadcastReceiver, new IntentFilter(GameService.BROADCAST_ACTION));
	    }
	    
	    @Override
	    public void onPause() {
	        super.onPause();
	        unregisterReceiver(broadcastReceiver);     
	    }
	
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            UpdateStatus(intent);      
        }
    };    
	
	private boolean isMyServiceRunning(Class<?> serviceClass) {
	    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (serviceClass.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
	
	private void UpdateStatus(Intent intent)
	{
		String text="";
		if(intent.getBooleanExtra("initialized", false))
		{
			text ="Game Initialized\n";
			text += intent.getIntExtra("traps",0)+" traps triggered\n";
			text += intent.getIntExtra("prizes",0)+" prizes found\n";
			text += "debug:"+intent.getStringExtra("debug")+"\n";
		
		}
		else
			text ="Waiting for GPS to settle!";
		((android.widget.TextView)findViewById(R.id.gameView)).setText(text);
	}
	
	//isMyServiceRunning(GameService.class)
	/**
	 * A placeholder fragment containing a simple view.
	 
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}*/
	
	public void toggleGame(View view) {
		if(isMyServiceRunning(GameService.class))
		{
			Intent intent = new Intent(this, GameService.class);
			stopService(intent);
			((android.widget.Button)view).setText("Start");
		}
		else
		{
			Intent intent = new Intent(this, GameService.class);
			MineParams params = new MineParams(getApplicationContext());
			intent.putExtra("param", params);
			//startMonitoring(params);
			startService(intent);
			((android.widget.Button)view).setText("Stop");
		}
	}
	
	/*public void startMonitoring(MineParams params)
	{
		Listener listener = new Listener(params);
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
	}*/
	

}

package com.example.minefield;

import android.app.Activity;
import android.app.ActionBar;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
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

	private boolean isMyServiceRunning(Class<?> serviceClass) {
	    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (serviceClass.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
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
			startService(intent);
			((android.widget.Button)view).setText("Stop");
		}
	}
	
	

}

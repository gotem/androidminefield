package com.insomniagames.minefield;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.PreferenceManager;

public class MineParams implements Parcelable{

	float maxDistance;
	float triggerRadius;
	float warnRadius;
	Integer numberMines;
	Integer numberPrizes;
	Boolean debug;
	
	public MineParams(Context ctx) {
		// TODO Auto-generated constructor stub
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ctx);
		maxDistance = Float.parseFloat(sharedPref.getString("maxDistance", "500"));
		triggerRadius = Float.parseFloat(sharedPref.getString("triggerRadius", "5"));
		warnRadius = Float.parseFloat(sharedPref.getString("warnRadius", "10"));
		numberMines = Integer.parseInt(sharedPref.getString("numberMines", "1"));
		numberPrizes = Integer.parseInt(sharedPref.getString("numberPrizes", "0"));
		debug = sharedPref.getBoolean("debug",false);
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeFloat(maxDistance);
		dest.writeFloat(triggerRadius);
		dest.writeFloat(warnRadius);
		dest.writeInt(numberMines);
		dest.writeInt(numberPrizes);
		dest.writeInt(debug?1:0);
	}

	public MineParams(Parcel in)
	{
		maxDistance = in.readFloat();
		triggerRadius = in.readFloat();
		warnRadius = in.readFloat();
		numberMines = in.readInt();
		numberPrizes = in.readInt();
		debug = (in.readInt()==1);

	}
	
    public static final Parcelable.Creator<MineParams> CREATOR = new Parcelable.Creator<MineParams>() 
    		{
		        public MineParams createFromParcel(Parcel in) {
		            return new MineParams(in);
        }

				@Override
				public MineParams[] newArray(int size) {
					// TODO Auto-generated method stub
					return null;
				}
    };
	
}

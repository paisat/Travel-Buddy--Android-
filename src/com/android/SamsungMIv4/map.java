package com.android.SamsungMIv4;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class map extends MapActivity{

	MapView mv;
	
	@Override
	public void onCreate(Bundle Icicle)
	{
		super.onCreate(Icicle);
		setContentView(R.layout.map);
		mv = (MapView)findViewById(R.id.mapview3);
		mv.setBuiltInZoomControls(true);
		mv.setSatellite(false);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    
	    menu.add(0, 1, 1, "TOGGLE SATELLITE");
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case 1:	boolean isSat = mv.isSatellite();
				mv.setSatellite(!isSat);
				return super.onOptionsItemSelected(item);
		
	    default:
	    	return super.onOptionsItemSelected(item);
	    	}
	    }
	    
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}

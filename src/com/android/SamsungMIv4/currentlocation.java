package com.android.SamsungMIv4;

import java.io.IOException;
import java.util.List;

import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

public class currentlocation extends MapActivity {
	int one = 0;
	String string = null;
	MapView mv;
	MapController mc;
	MyLocationOverlay mylocationoverlay;
	LocationManager locationManager;
	TextView tv;
	@Override
	public void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);
		
		
		setContentView(R.layout.currentlocation);
		//tv = (TextView)findViewById(R.id.cltv);
		mv = (MapView)findViewById(R.id.mapview);
		mv.setBuiltInZoomControls(true);
		mv.setSatellite(false);
		mc = mv.getController();
		mv.getOverlays();
		mc.setZoom(12);
		mylocationoverlay = new MyLocationOverlay(this,mv);
		mv.getOverlays().add(mylocationoverlay);
		mylocationoverlay.enableCompass();
		mylocationoverlay.enableMyLocation();
		
		
		
		
		mylocationoverlay.runOnFirstFix(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//System.out.println("Inside run");
				mc.animateTo(mylocationoverlay.getMyLocation());
				//GeoPoint geopoint = mylocationoverlay.getMyLocation();
				//String string = geopoint.toString();
				//AlertDialog.Builder alertbox = new AlertDialog.Builder(currentlocation.this);
				//Toast tost = Toast.makeText(currentlocation.this, "hello toast!", Toast.LENGTH_LONG);
				//tost.show();
				//one = geopoint.getLatitudeE6();
				//string = Integer.toString(geopoint.getLatitudeE6());
				//tv.append("hello" + "\n");
				//tv.append(Integer.toString(mylocationoverlay.getMyLocation().getLatitudeE6()));
			}
		});

		//GeoPoint geopoint = mylocationoverlay.getMyLocation();
		//String string = geopoint.toString();
		//Toast tost = Toast.makeText(currentlocation.this, "hello toast!", Toast.LENGTH_LONG);
		//tost.show();
		//tv.append(Integer.toString(one));
		//tv.append(string);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    menu.add(0, 1, 1, "CURRENT LOCATION");
	    menu.add(0, 2, 2, "TOGGLE SATELLITE");
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    
	    case 2 : boolean isSat = mv.isSatellite();
		mv.setSatellite(!isSat);
		
	    case 1:	locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
				String provider = locationManager.getBestProvider(new Criteria(), true);
				Location l = locationManager.getLastKnownLocation(provider);
				Double cur_lat = l.getLatitude();
				Double cur_lng = l.getLongitude();
				Geocoder g = new Geocoder(this);
				try {
					List<Address> list = g.getFromLocation(cur_lat, cur_lng, 1);
					Address a = list.get(0);
					Toast tost = Toast.makeText(currentlocation.this, "Latitude : " + Double.toString(a.getLatitude()) +"\n" + "Longitude : " + Double.toString(a.getLongitude()) + "\n" + "Place : " +a.getAddressLine(0), Toast.LENGTH_LONG);
					tost.show();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
	    		
	    		
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


package com.android.SamsungMIv4;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.SamsungMIv4.HelloItemizedOverlay;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class searchlocationmap extends MapActivity {

	MapView mv;
	MapController mc;
	
	@Override
	public void onCreate(Bundle Icicle)
	{
		super.onCreate(Icicle);
		setContentView(R.layout.searchlocationmap);
		mv = (MapView)findViewById(R.id.mapview2);
		mv.setBuiltInZoomControls(true);
		mv.setSatellite(false);
		mc = mv.getController();
		mv.getOverlays();
		mc.setZoom(12);
		Bundle bundle = this.getIntent().getExtras();
		Double cur_lat = bundle.getDouble("lat");
		Double cur_lng = bundle.getDouble("lng");
		List<Overlay> mapOverlays = mv.getOverlays();
        Drawable drawable = this.getResources().getDrawable(R.drawable.androidmarker);
        HelloItemizedOverlay hio = new HelloItemizedOverlay(drawable);
        GeoPoint gp = new GeoPoint((int)(cur_lat * 1E6),(int)(cur_lng * 1E6));
        OverlayItem overlayitem = new OverlayItem(gp, "Hi", "Here it is!");
        hio.addOverlay(overlayitem);
        mapOverlays.add(hio);
        mc.animateTo(gp);
		Toast tost = Toast.makeText(searchlocationmap.this, "Latitude : " + Double.toString(cur_lat) + "\n" + "Longitude : " + Double.toString(cur_lng), Toast.LENGTH_LONG);
		tost.show();
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

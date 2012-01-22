package com.android.SamsungMIv4;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.harrison.lee.twitpic4j.TwitPic;
import com.harrison.lee.twitpic4j.TwitPicResponse;
import com.harrison.lee.twitpic4j.exception.TwitPicException;

public class TwitPicActivity extends Activity {
    /** Called when the activity is first created. */
	
	EditText et1; 
	EditText et2;
	Button b;
	LocationManager locationManager;
	String place;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twitpic);
        
        et1 = (EditText)findViewById(R.id.username);
    	et2 = (EditText)findViewById(R.id.password);
    	b = (Button)findViewById(R.id.send);
     // Create file
    	
    	Bundle extra = getIntent().getExtras();
    	
    	final String notes = extra.getString("notes");
    	final String location = extra.getString("location");
    	final String photoName = extra.getString("photoname");
    	//Toast t = Toast.makeText(TwitPicActivity.this, notes, Toast.LENGTH_LONG);
    	//t.show();
    	//final Bitmap bitmap;
    	b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				
				
				
				
				
				File picture = new File(Environment.getExternalStorageDirectory()+"/SMI1/"+photoName);
		        /*if(picture.exists())
		        {
		            Toast t = Toast.makeText(TwitPicActivity.this, "Pic exists!", Toast.LENGTH_LONG);
		            t.show();
		        }
		        else
		        {
		            Toast t = Toast.makeText(TwitPicActivity.this, "Pic doesn't exist!", Toast.LENGTH_LONG);
		            t.show();
		        }*/

		        /*locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
				String provider = locationManager.getBestProvider(new Criteria(), true);
				Location l = locationManager.getLastKnownLocation(provider);
				Double cur_lat = l.getLatitude();
				Double cur_lng = l.getLongitude();
				Geocoder g = new Geocoder(this);
				try {
					List<Address> list = g.getFromLocation(cur_lat, cur_lng, 1);
					Address a = list.get(0);
					place = a.getSubLocality();;
					//Toast tost = Toast.makeText(TwitPicActivity.this, "Latitude : " + Double.toString(a.getLatitude()) +"\n" + "Longitude : " + Double.toString(a.getLongitude()) + "\n" + "Place : " +a.getAddressLine(0), Toast.LENGTH_LONG);
					//tost.show();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				
		        
		        // Create TwitPic object and allocate TwitPicResponse object
		        
		        String username = et1.getText().toString()+"";
		        String password = et2.getText().toString()+"";
		         //String username = tpRequest.getUsername();
		           //String password = tpRequest.getPassword();
      	
		        Bitmap bitmap;
		        //TwitPic tp = new TwitPic();
		       // String us = tp.getUsername();
		        //String pa = tp.getPassword();
		        TwitPic tpRequest = new TwitPic(username, password);
		        TwitPicResponse tpResponse = null;

		        // Make request and handle exceptions                           
		        try {
		        	
		        		if(photoName.equals(""))
		        		{
		        			bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.icon);
		        			
		        			ByteArrayOutputStream stream = new ByteArrayOutputStream();
		        			
		        			bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream); 

		 		            byte [] byte_arr = stream.toByteArray();
		
		 		            tpResponse = tpRequest.uploadAndPost(byte_arr,notes + "\n" +  "Location : " + location);
		        			
		        				Toast t = Toast.makeText(TwitPicActivity.this, tpResponse.toString(), Toast.LENGTH_LONG);
				                t.show();
				                finish();
		        			
		        		
		        			//Toast t = Toast.makeText(TwitPicActivity.this, "Uploaded!", Toast.LENGTH_LONG);
			                //t.show();
		        			
		        			
		        			
		        		}
		        		
		        		else
		        		{
		        			tpResponse = tpRequest.uploadAndPost(picture, notes + "\n" +  "Location : " + location);
		        			
		        			
		        				Toast t = Toast.makeText(TwitPicActivity.this,tpResponse.toString(), Toast.LENGTH_LONG);
				                t.show();
				                finish();
		        			
		        			//Toast t = Toast.makeText(TwitPicActivity.this, "Uploaded!", Toast.LENGTH_LONG);
		        			//t.show();
		        			
		        			
		        		}
		        } catch (IOException e) {
		            Toast t = Toast.makeText(TwitPicActivity.this, "Failed1!", Toast.LENGTH_LONG);
		            t.show();
		                e.printStackTrace();
		        } catch (TwitPicException e) {
		            Toast t = Toast.makeText(TwitPicActivity.this, "Error! Try again!", Toast.LENGTH_LONG);
		            t.show();
		                e.printStackTrace();
		        }

		        // If we got a response back, print out response variables                              
		        if(tpResponse != null)
		                tpResponse.dumpVars();
		 
				
				
				
				
				
				
				
			}
		});
    }
}
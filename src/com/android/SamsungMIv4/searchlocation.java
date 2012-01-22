package com.android.SamsungMIv4;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class searchlocation extends Activity {
	
	EditText neartext;
	String nearbyplace = null;
	EditText searchtext;
	String searchplace = null;
	Button gobutton;
	ListView listview;
    String addressline = null;
    String countryname;
    String[] List;
    List<Address> places;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle iCiCle) {
        super.onCreate(iCiCle);
        setContentView(R.layout.searchlocation);
        
        neartext = (EditText)findViewById(R.id.editText2);
        searchtext = (EditText)findViewById(R.id.editText1);
        
        gobutton = (Button)findViewById(R.id.gobutton);
        listview = (ListView)findViewById(R.id.listView1);
        final Geocoder gcd = new Geocoder(this);

        gobutton.setOnClickListener(new View.OnClickListener() {
        	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				try {
					searchplace = searchtext.getText().toString();
					nearbyplace = neartext.getText().toString();
					Toast toast = Toast.makeText(searchlocation.this, "Place :" + searchplace, Toast.LENGTH_LONG);
					toast.show();
					 places = gcd.getFromLocationName(searchplace + "," + nearbyplace, 50);
					int count = places.size();
					List = new String[count];
					for(int i=0;i<count;i++)
					{
					Address place = places.get(i);
					/*while(place.getAddressLine(j) != null)
					{
						addressline = addressline + " " + place.getAddressLine(j);
						j++;
					}*/
					addressline = place.getAddressLine(0) + ": " +  place.getAddressLine(1) + ": " + place.getAddressLine(2);
					countryname = place.getCountryName();
					addressline = addressline + ", " + countryname;
					List[i] = addressline;
					}
					listview.setAdapter(new ArrayAdapter<String>(searchlocation.this, android.R.layout.simple_list_item_1, List));

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
        
        
    	
    	
    	
    	listview.setOnItemClickListener(new OnItemClickListener() {
    		public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
    			Address address = places.get(myItemInt);
				Bundle bundle = new Bundle();
				bundle.putDouble("lat", address.getLatitude());
				bundle.putDouble("lng", address.getLongitude());
				Intent intent2  = new Intent(searchlocation.this,searchlocationmap.class);
				intent2.putExtras(bundle);
				startActivity(intent2);
    		}
    	});
        
    }
    
    
}
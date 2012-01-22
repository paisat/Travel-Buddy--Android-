package com.android.SamsungMIv4;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
public class TravelbuddyActivity extends Activity {
    /** Called when the activity is first created. */
	
	
	public ListView oldPosts;
	TextView text1;
	boolean resume=false;
	Activity a;
	
	

    @Override
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oldpost);
       
        oldPosts=(ListView)findViewById(R.id.oldPosts);
       oldPosts.setDivider(null);
       oldPosts.setDividerHeight(10);
        text1=(TextView)findViewById(R.id.text1);
        Typeface font =Typeface.createFromAsset(getAssets(), "scrible.ttf");
        text1.setTypeface(font);
      
        populatelist();
     
    }
    
    @Override
    public void onResume()
    {
    	super.onResume();
    	
    	populatelist();
    	oldPosts.setOnItemClickListener(new OnItemClickListener() {
    		
    	@Override
    	public void onItemClick(AdapterView<?> a ,View v,int i,long id)
    	{
    		Intent intent =new Intent(TravelbuddyActivity.this,Note.class);
    		intent.putExtra("_id", id);
    		TravelbuddyActivity.this.startActivity(intent);
    		
    		
    		
    	}
    		
		});
    }
    
       public void populatelist()
       {
    	  final DbAdapter dbAd =new DbAdapter(this);
    	  dbAd.open();
    	  final ImageLoader im=new ImageLoader(this);
    	  a=this;
    	
    	   final class MySimpleCursorAdapter extends SimpleCursorAdapter {
    		   
    		  

      		    public MySimpleCursorAdapter(Context context, int layout, Cursor cur,
      		        String[] from, int[] to) {
      		        super(context, layout, cur, from, to);
      		       
      		    }

      		    @Override
      		    public void setViewImage(ImageView v, String id) {
      		    	
      		    	Log.v("mysimple",id);
      		    	if(id.equals(""))
      		    	v.setImageResource(R.drawable.stub);
      		    	else
      		   
      		    		im.DisplayImage(id,a, v,70);
      		     
      		     
      		    }

      		}
    	   
    	  
    	   
    	   Cursor c= dbAd.fetchAll();
    	   startManagingCursor(c);
    	  c.moveToFirst();
    	  
    	  
    	   MySimpleCursorAdapter list = new MySimpleCursorAdapter(this,R.layout.list,c,new String [] {"name","photo"},new int [] {R.id.listtext,R.id.listimage});
    	  
           oldPosts.setAdapter(list);
           list.notifyDataSetChanged();
          
          // dbAd.close();
           
           
       }
        
        
    
}
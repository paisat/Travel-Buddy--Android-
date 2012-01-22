package com.android.SamsungMIv4;

import java.io.File;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.OrientationEventListener;
import android.widget.Toast;

public class camera extends Activity {
	
	Long photoId;
	String photoname=null;
	boolean pictureTaken=false;
	boolean onResume=false;
	int mOrientation;
	String orientations;
	ImageLoader loader;
    private static final int TAKEPICTURE_ACTIVITY = 0;
    OrientationEventListener mOrientationEventListener;
    private static final int ORIENTATION_PORTRAIT_NORMAL =  0;
    private static final int ORIENTATION_PORTRAIT_INVERTED =  180;
    private static final int ORIENTATION_LANDSCAPE_NORMAL =  270;
    private static final int ORIENTATION_LANDSCAPE_INVERTED =  90;
    File f;
    File file;
    boolean fileExists=false;
    Activity a;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        a=this;
        fileExists=false;
       
       
        Bundle saved=getIntent().getExtras();
        photoId=saved.getLong("id");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR | ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        mOrientationEventListener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL) 
        { 
         @Override
         public void onOrientationChanged(int orientation) 
         {
          mOrientation = orientation;
          
          if (orientation >= 315 || orientation < 45) {
              if (mOrientation != ORIENTATION_PORTRAIT_NORMAL) {                          
                  mOrientation = ORIENTATION_PORTRAIT_NORMAL;
              }
          }
          else if (orientation < 315 && orientation >= 225) {
              if (mOrientation != ORIENTATION_LANDSCAPE_NORMAL) {
                  mOrientation = ORIENTATION_LANDSCAPE_NORMAL;
              }                       
          }
          else if (orientation < 225 && orientation >= 135) {
              if (mOrientation != ORIENTATION_PORTRAIT_INVERTED) {
                  mOrientation = ORIENTATION_PORTRAIT_INVERTED;
              }                       
          }
          else { 
              if (mOrientation != ORIENTATION_LANDSCAPE_INVERTED) {
                  mOrientation = ORIENTATION_LANDSCAPE_INVERTED;
              }                       
          }   
        // mDeviceOrientation=(90*Math.round(mDeviceOrientation/90))%360;*
         Log.v("orientation",mOrientation+"");
         
         }
        };
         		
        if(mOrientationEventListener.canDetectOrientation())
        {
         mOrientationEventListener.enable();
        }
        
       
        
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
        try{ 
        	
        	File folder = new File(Environment.getExternalStorageDirectory(),"SMI1");
    		
    		if(!folder.exists())
    		{
    			folder.mkdir();
    		}
        	
        	
        	
        		photoname="test"+photoId+".jpg";
        		
        		
        	
        	
        	Log.v("photocamera",""+photoname);
        	 file = new File(Environment.getExternalStorageDirectory() + "/SMI1", photoname); 
        	 
        	Uri outputFileUri = Uri.fromFile(file); 
        	i.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        	i.putExtra(MediaStore.EXTRA_FINISH_ON_COMPLETION, true);
        	i.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, "android.intent.extra.screenOrientation");
                startActivityForResult(i, TAKEPICTURE_ACTIVITY); 
        }catch(ActivityNotFoundException e){ 
                Toast.makeText(this, "Application not available", 
Toast.LENGTH_SHORT).show();} 
        
    }   
   
   
    public void Resume(int orientation)
    {
    	
    	
    	if(!pictureTaken)
    	photoname="";
    	Log.v("camera.java on resume",photoname);
    	Intent result=new Intent();
    	result.putExtra("photoname",photoname);
    	result.putExtra("orientation", orientation);
    	setResult(RESULT_OK, result);
    	
    	
    finish();
    		
    	
    	
    }
   
  
 @Override 
    protected void onActivityResult(int requestCode, int resultCode, 
Intent intent) { 
        super.onActivityResult(requestCode, resultCode, intent); 
      if (requestCode == TAKEPICTURE_ACTIVITY){ 
                if (resultCode == RESULT_OK) {
                	
                	
                	   
                  
                     
                    
                	pictureTaken=true;
                	
                
                	
                	Resume(mOrientation);
                	
                	
                    
             }
                else
                {
                	finish();
                }
                
  }
      
     
      
}
 
 
}

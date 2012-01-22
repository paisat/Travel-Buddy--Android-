package com.android.SamsungMIv4;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;


import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;

public abstract class FacebookActivity extends Activity {
    public static final String TAG = "FACEBOOK";
    public Facebook mFacebook;
    private static final String APP_ID = "123056311121086"; 
    private AsyncFacebookRunner mAsyncRunner;
    private static final String[] PERMS = new String[] { "publish_stream" };
    private SharedPreferences sharedPrefs;
    private Context mContext;
    Handler mHandler=new Handler();
    String photo;
    int orientation;
    String notes;
    String location;
    String country;
    int psize;
    
   public boolean logged =false;
 
    
   
 
    public void setConnection() {
            mContext = this;
            mFacebook = new Facebook(APP_ID);
            mAsyncRunner = new AsyncFacebookRunner(mFacebook);
        	
          
    }
    
    public void logout()
    {
    	
    	mAsyncRunner.logout(this, new LogoutRequestListener());
    }
 
    public void getID(String note,String pic,int or,int size,String con) {
    	
    	photo=pic;
    	notes=note;
    	orientation=or;
    	 psize=size;
    	 country=con;
    	logged=false;
           
            if (isSession()) {
                    Log.d(TAG, "sessionValid");
                    
                    mAsyncRunner.request("me", new IDRequestListener());
            } else {
                    // no logged in, so relogin
                    Log.d(TAG, "sessionNOTValid, relogin");
                    mFacebook.authorize(this, PERMS,-1, new LoginDialogListener());
                   
            }
    }
 
    public boolean isSession() {
            sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
            String access_token = sharedPrefs.getString("token", null);
            
            Long expires = sharedPrefs.getLong("expires", -1);
          
 
            if (access_token != null && expires != -1) {
            	Log.v("access", access_token);
            	Log.v("expires",expires+"");
            	
                    mFacebook.setAccessToken(access_token);
                    mFacebook.setAccessExpires(expires);
            }
            return mFacebook.isSessionValid();
    }
 
    public class LoginDialogListener implements DialogListener {
 
            @Override
            public void onComplete(Bundle values) {
                    Log.d(TAG, "LoginONComplete");
                    String token = mFacebook.getAccessToken();
                    long token_expires = mFacebook.getAccessExpires();
                    Log.d(TAG, "AccessToken: " + token);
                    Log.d(TAG, "AccessExpires: " + token_expires);
                    sharedPrefs = PreferenceManager
                                    .getDefaultSharedPreferences(mContext);
                    sharedPrefs.edit().putLong("expires", token_expires)
                                    .commit();
                    sharedPrefs.edit().putString("token", token).commit();
                    mAsyncRunner.request("me", new IDRequestListener());
            }
 
            @Override
            public void onFacebookError(FacebookError e) {
                    Log.d(TAG, "FacebookError: " + e.getMessage());
            }
 
            @Override
            public void onError(DialogError e) {
                    Log.d(TAG, "Error: " + e.getMessage());
            }
 
            @Override
            public void onCancel() {
                    Log.d(TAG, "OnCancel");
            }
    }
    
    private class LogoutRequestListener implements RequestListener {
   	 
		@Override
		public void onComplete(String response, Object state) {
 
			// Dispatch on its own thread
			mHandler.post(new Runnable() {
				public void run() {
					
					sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
					sharedPrefs.edit().clear().commit();
					
					
					Toast toast=Toast.makeText(FacebookActivity.this,"Logged out", Toast.LENGTH_LONG);
					toast.show();
					
					
				}
			});
		}
 
		@Override
		public void onIOException(IOException e, Object state) {
			// TODO Auto-generated method stub
 
		}
 
		@Override
		public void onFileNotFoundException(FileNotFoundException e,
				Object state) {
			// TODO Auto-generated method stub
 
		}
 
		@Override
		public void onMalformedURLException(MalformedURLException e,
				Object state) {
			// TODO Auto-generated method stub
 
		}
 
		@Override
		public void onFacebookError(FacebookError e, Object state) {
			// TODO Auto-generated method stub
 
		}
 
	}
    
    private class uploadListener implements RequestListener
    {
    	@Override
    	public void onComplete( String response,Object state)
    	{
    		final String myresponse=response;
    		
    	
				
				FacebookActivity.this.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						
						
						//String msg;
						if(myresponse.equals("")||myresponse==null)
			    		{
							
							Toast toast=Toast.makeText(getApplicationContext(), "Error try again", Toast.LENGTH_LONG);
		            		toast.show();
		            		
			    			
			    			
			    		}
			    		else
			    		{
			    			
			    			Toast toast=Toast.makeText(getApplicationContext(), "Uploaded To Facebook", Toast.LENGTH_LONG);
		            		toast.show();
			    			
			    			 
			    			
			    		}
						
						
					}
				});
					
		
    		
    	}
    	
    	 @Override
         public void onIOException(IOException e, Object state) {
                 Log.d(TAG, "IOException: " + e.getMessage());
         }

         @Override
         public void onFileNotFoundException(FileNotFoundException e,
                         Object state) {
                 Log.d(TAG, "FileNotFoundException: " + e.getMessage());
         }

         @Override
         public void onMalformedURLException(MalformedURLException e,
                         Object state) {
                 Log.d(TAG, "MalformedURLException: " + e.getMessage());
         }

         @Override
         public void onFacebookError(FacebookError e, Object state) {
                 Log.d(TAG, "FacebookError: " + e.getMessage());
         }
    }
    
   
 
    private class IDRequestListener implements RequestListener {
 
            @Override
            public void onComplete(String response, Object state) {
                    try {
                            Log.d(TAG, "IDRequestONComplete");
                            Log.d(TAG, "Response: " + response.toString());
                            JSONObject json = Util.parseJson(response);
 
                         
                            final String name = json.getString("name");
                            FacebookActivity.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                    
                                           Toast toast =Toast.makeText(FacebookActivity.this,"Welcome "+name+". You can logout by pressing option button in dashboard",Toast.LENGTH_LONG);
                                           toast.show();
                                           logged=true;
                                           
                                           if(notes.equals("")&&photo.equals(""))
                                           {
                                        	   
                                           }
                                           else
                                           {
                                        	   postOnWall(notes, photo, orientation, psize,country);
                                           }
                                        	   
                                           
 
                                    }
                            });
                    } catch (JSONException e) {
                            Log.d(TAG, "JSONException: " + e.getMessage());
                    } catch (FacebookError e) {
                            Log.d(TAG, "FacebookError: " + e.getMessage());
                    }
            }
 
            @Override
            public void onIOException(IOException e, Object state) {
                    Log.d(TAG, "IOException: " + e.getMessage());
            }
 
            @Override
            public void onFileNotFoundException(FileNotFoundException e,
                            Object state) {
                    Log.d(TAG, "FileNotFoundException: " + e.getMessage());
            }
 
            @Override
            public void onMalformedURLException(MalformedURLException e,
                            Object state) {
                    Log.d(TAG, "MalformedURLException: " + e.getMessage());
            }
 
            @Override
            public void onFacebookError(FacebookError e, Object state) {
                    Log.d(TAG, "FacebookError: " + e.getMessage());
            }
 
    }
 
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            mFacebook.authorizeCallback(requestCode, resultCode, data);
    }
     
    public void postOnWall(String msg,String Photo,int orientation,int size,String co) {
        Log.d("Tests graph API %%%%%$$$$%%%", msg);
         try {
        	 	
        	 	
        	 	Log.v(TAG,msg);
        	 	
               
                
                Bundle parameters = new Bundle();
                AsyncFacebookRunner postRunner=new AsyncFacebookRunner(mFacebook);
                
                if(!Photo.equals(""))
                {
                	File f=new File(Environment.getExternalStorageDirectory()+"/SMI1/"+photo);
                	
                	if(f.exists())
                	{
                		
                		Toast toast=Toast.makeText(getApplicationContext(), "Uploading . you will get a notification when its done", Toast.LENGTH_LONG);
                		toast.show();
                	
	                		Matrix m=new Matrix();
	        			m.setRotate(orientation+90);
	        			  BitmapFactory.Options o=new BitmapFactory.Options();
	        		        o.inJustDecodeBounds=true;
	        		    	
	        			Bitmap b=BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/SMI1/"+Photo,o);
	        		       int   width_tmp=o.outWidth; int height_tmp=o.outHeight;
	        		        int scale=1;
	        		        final int REQUIRED_SIZE=size;
	        		        //final int WIDTH=(int)(oWidth/2);
	
	        		        while(true){
	        		            if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
	        		                break;
	        		            width_tmp/=2;
	        		            height_tmp/=2;
	        		            scale*=2;
	        		        }
	        		        BitmapFactory.Options o2 = new BitmapFactory.Options();
	        		        o2.inSampleSize=scale;
	        		        
	        		      
	        		       
	        			
	        		         b=BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/SMI1/"+Photo,o2);
	        		         Bitmap resize=Bitmap.createBitmap(b, 0, 0, width_tmp, height_tmp, m, true);
	                	
	                	byte [] data=null;
	                
	                	ByteArrayOutputStream baos=new ByteArrayOutputStream();
	                
	                	resize.compress(Bitmap.CompressFormat.JPEG,100, baos);
	                	data=baos.toByteArray();
	                	//parameters.putString("method", "photos.upload");
	                	parameters.putByteArray("photo", data);
	                	parameters.putString("caption", msg);
	                	parameters.putString("message", msg+ "\n" +" Location "+co);
	                	
	                	
	                	 postRunner.request("/me/photos", parameters,"POST",new uploadListener(), null);
                	}
                	else
                	{
                		Toast toast=Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG);
                		toast.show();
                	}
                }
                else
                {
                	Toast toast=Toast.makeText(getApplicationContext(), "Uploading . you will get a notification when its done", Toast.LENGTH_LONG);
            		toast.show();
                	 parameters.putString("message", msg+ "\n" + " Location :"+co);
                	
                	
                	 postRunner.request("/me/feed", parameters,"POST",new uploadListener(), null);
                }
                
                
                
         } catch(Exception e) {
        	 
        	 Toast toast =Toast.makeText(this,"Oops Error occured!", Toast.LENGTH_LONG);
        	 toast.show();
             e.printStackTrace();
         }
    }
}

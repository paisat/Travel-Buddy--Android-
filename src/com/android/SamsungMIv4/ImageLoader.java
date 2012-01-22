package com.android.SamsungMIv4;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Stack;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

public class ImageLoader  {
	
	int picSize;
	int width_tmp;
	int height_tmp;
	DbAdapter db;
	
    private HashMap<String, Bitmap> cache=new HashMap<String, Bitmap>();
    
    private File cacheDir;
    
    public ImageLoader(Context context){
       
        photoLoaderThread.setPriority(Thread.NORM_PRIORITY-1);
        
        db=new DbAdapter(context);
      
       
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir=new File(android.os.Environment.getExternalStorageDirectory(),"SMIList");
        else
            cacheDir=context.getCacheDir();
        if(!cacheDir.exists())
            cacheDir.mkdirs();
    }
    
    final int stub_id=R.drawable.loading;
    public void DisplayImage(String src, Activity activity, ImageView imageView,int size)
    {
    	
    	File f=new File(Environment.getExternalStorageDirectory()+"/SMI1/"+src);
    	if(f.exists())
    	{
		    	db.open();
		        
		        Cursor or=db.fetchRowByOrientation(src);
		        or.moveToFirst();
		        
		        String orientation="0";
		        if(or.moveToFirst())
		         orientation=or.getString(or.getColumnIndex("orientation"));
		        db.close();
		    	
		    	picSize=size;
		        if(cache.containsKey(src))
		        {
		            imageView.setImageBitmap(cache.get(src));
		        	
		        }
		        else
		        {
		            queuePhoto(src, activity, imageView,orientation);
		           imageView.setImageResource(stub_id);

		        }
    	}
    	else
    	{
    		imageView.setImageResource(R.drawable.imagenotfound);
    	}
    }
    
    public void DisplayImage(String src, Activity activity, ImageView imageView,int size,String orientation)
    {
    	
    	File f=new File(Environment.getExternalStorageDirectory()+"/SMI1/"+src);
        
    	if(f.exists())
    	{
		    	picSize=size;
		        if(cache.containsKey(src))
		        {
		            imageView.setImageBitmap(cache.get(src));
		        	
		        }
		        else
		        {
		            queuePhoto(src, activity, imageView,orientation);
		           imageView.setImageResource(stub_id);
		           
		            
		           
		        }
    	}
    	else
    		imageView.setImageResource(R.drawable.imagenotfound);
    }
        
    private void queuePhoto(String src, Activity activity, ImageView imageView,String orientation)
    {
       
        photosQueue.Clean(imageView);
        PhotoToLoad p=new PhotoToLoad(src, imageView,orientation);
        synchronized(photosQueue.photosToLoad){
            photosQueue.photosToLoad.push(p);
            photosQueue.photosToLoad.notifyAll();
        }
        
        
        if(photoLoaderThread.getState()==Thread.State.NEW)
            photoLoaderThread.start();
    }
    
    private Bitmap getBitmap(String src) 
    {
      
        String filename=String.valueOf(src.hashCode());
        Log.v("Filename",filename);
        File f=new File(cacheDir, filename);
        
        
        Bitmap b = decodeFile(f);
        if(b!=null)
            return b;
        
        
        try {
        	Log.v("src",src);
            //Bitmap bitmap=null;
          
            
            FileInputStream is=new FileInputStream(new File(Environment.getExternalStorageDirectory()+"/SMI1/"+src));
            
         
           OutputStream os=new FileOutputStream(f);
           Utils.CopyStream(is, os);
           os.close();
           return decodeFile(f);
            
            
            
            
        } catch (Exception ex){
           ex.printStackTrace();
           return null;
        }
    }

    
    private Bitmap decodeFile(File f){
        try {
        	
        	Log.v("inside decode file","true");
           
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);
            
            
            final int REQUIRED_SIZE=picSize;
           width_tmp=o.outWidth; 
           height_tmp=o.outHeight;
            int scale=1;
            while(true){
                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
                    break;
                width_tmp/=2;
                height_tmp/=2;
                scale*=2;
            }
            
           
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }
    
  
    private class PhotoToLoad
    {
        public String src;
        public ImageView imageView;
        public String orient;
        public PhotoToLoad(String u, ImageView i,String or){
            src=u; 
            imageView=i;
            orient=or;
        }
    }
    
    PhotosQueue photosQueue=new PhotosQueue();
    
    public void stopThread()
    {
        photoLoaderThread.interrupt();
    }
    
    
    class PhotosQueue
    {
        private Stack<PhotoToLoad> photosToLoad=new Stack<PhotoToLoad>();
        
        
        public void Clean(ImageView image)
        {
            for(int j=0 ;j<photosToLoad.size();){
                if(photosToLoad.get(j).imageView==image)
                    photosToLoad.remove(j);
                else
                    ++j;
            }
        }
    }
    
    class PhotosLoader extends Thread {
        public void run() {
            try {
                while(true)
                {
                    
                    if(photosQueue.photosToLoad.size()==0)
                        synchronized(photosQueue.photosToLoad){
                            photosQueue.photosToLoad.wait();
                        }
                    if(photosQueue.photosToLoad.size()!=0)
                    {
                        PhotoToLoad photoToLoad;
                        synchronized(photosQueue.photosToLoad){
                            photoToLoad=photosQueue.photosToLoad.pop();
                        }
                        Bitmap bmp=getBitmap(photoToLoad.src);
                        
                       
                        
                       String porient=photoToLoad.orient;
                       if(porient.equals(""))
                    	   porient="0";
                        
                        
                       
                        Matrix m=new Matrix();
                        m.setRotate(90+Integer.parseInt(porient));
                        
                        Bitmap resize=Bitmap.createBitmap(bmp, 0, 0, width_tmp, height_tmp, m, true);
                        cache.put(photoToLoad.src, resize);
                        
                        Log.v("inside phtoloader thread","true");
                        //f(tag!=null && ((String)tag).equals(photoToLoad.src)){
                        	Log.v("inside tag","true");
                            BitmapDisplayer bd=new BitmapDisplayer(resize, photoToLoad.imageView);
                            Activity a=(Activity)photoToLoad.imageView.getContext();
                            a.runOnUiThread(bd);
                       
                 // }
                    }
                    if(Thread.interrupted())
                        break;
                }
            } catch (InterruptedException e) {
                
            }
        }
    }
    
    PhotosLoader photoLoaderThread=new PhotosLoader();
    
   
    class BitmapDisplayer implements Runnable
    {
        Bitmap bitmap;
        ImageView imageView;
        public BitmapDisplayer(Bitmap b, ImageView i){bitmap=b;imageView=i;}
        public void run()
        {
            if(bitmap!=null)
            {
            	
            	
                imageView.setImageBitmap(bitmap);
                
            }
            else
            {
               imageView.setImageResource(stub_id);
            	
            }
        }
    }

    public void clearCache() {
       
        cache.clear();
        
       
        File[] files=cacheDir.listFiles();
        for(File f:files)
            f.delete();
    }

}

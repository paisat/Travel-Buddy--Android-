package com.android.SamsungMIv4;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class Note extends Activity{
	
	
	ScrollView oldnote;
	ScrollView scrollnote;
	ImageView image;
	TextView preview;
	int height;
	int width;
	Cursor note;
	String photo;
	TextView header;
	TextView header2;
	long id;
	String orientation;
	int photoOrientation;
	ImageLoader img;
	Activity a;
	
	
	int requestcode=1;
	
	@Override
	public void onCreate(Bundle Savedinstance)
	{
		
		super.onCreate(Savedinstance);
		setContentView(R.layout.noteview);
		Bundle Saved=getIntent().getExtras();
		id=Saved.getLong("_id");
	
		scrollnote=(ScrollView)findViewById(R.id.scrollnote);
		image=(ImageView)findViewById(R.id.oldimage);
		preview=(TextView)findViewById(R.id.preview);
		header=(TextView)findViewById(R.id.header);
		header2=(TextView)findViewById(R.id.header2);
		oldnote=(ScrollView)findViewById(R.id.olddnotes);
	
		DbAdapter dbAd =new DbAdapter(this);
		Typeface font=Typeface.createFromAsset(getAssets(), "scrible.ttf");
		preview.setTypeface(font);
		header.setTypeface(font);
		header2.setTypeface(font);
	
		Display display=getWindowManager().getDefaultDisplay();
		height=display.getHeight()-50;
		width=display.getWidth();
		Log.v("id",String.valueOf(id));
		dbAd.open();
		
		
		note=dbAd.fetchRowById(String.valueOf(id));
		startManagingCursor(note);
		note.moveToFirst();
		
		photo=note.getString(note.getColumnIndex("photo"));
		orientation=note.getString(note.getColumnIndex("orientation"));
		if(orientation.equals(""))
		{
			orientation="0";
		}
		photoOrientation=Integer.parseInt(orientation);
		
		if(photo.equals(""))
		{
			image.setVisibility(View.GONE);
			LinearLayout.LayoutParams param =new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,height/2);
			oldnote.setLayoutParams(param);
			scrollnote.setVisibility(View.GONE);
			
			header.setText(note.getString(note.getColumnIndex("name")));
			header2.setText(note.getString(note.getColumnIndex("date")));
			preview.setText(note.getString(note.getColumnIndex("notes")));
		
			
		}
		else
		{
			
			scrollnote.setVisibility(View.VISIBLE);
			
			Log.v("entered else","true");
			
						
			header.setText(note.getString(note.getColumnIndex("name")));
			header2.setText(note.getString(note.getColumnIndexOrThrow("date")));
			preview.setText(note.getString(note.getColumnIndex("notes")));
			
		    	int photolayout=height/2;
			
		    	LinearLayout.LayoutParams param2=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,height/2);		
				oldnote.setLayoutParams(param2);
			LinearLayout.LayoutParams param3 =new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,height/2);
				
			scrollnote.setLayoutParams(param3);
		       
		        
		     
		    	img=new ImageLoader(this);
		    	
		    	img.DisplayImage(photo, a, image, photolayout);
		    	
		    	
		        image.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
						Intent photoview=new Intent();
						photoview.setAction(Intent.ACTION_VIEW);
						photoview.setDataAndType(Uri.parse("file://" + "/sdcard/SMI1/"+photo),"image/*");
						startActivity(photoview);
						
						
					}
				});
			
		        Log.v("leaving else","true");
			
		}
		
		
		
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		
		super.onConfigurationChanged(newConfig);
		Log.v("entered on config","true");
		
		Display display=getWindowManager().getDefaultDisplay();
		height=display.getHeight()-29;
		width=display.getWidth();
		LinearLayout.LayoutParams param2 =new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,height/2);
		if(photo.equals(""))
		{
			
			image.setVisibility(View.GONE);
			oldnote.setLayoutParams(param2);
			
		}
		else
		{
			image.setVisibility(View.VISIBLE);
			oldnote.setLayoutParams(param2);
			scrollnote.setLayoutParams(param2);
			
		}
		
		Log.v("Leaving config","true");
	}
	
	
	
	public boolean onCreateOptionsMenu(Menu menu)
	{
		
		
		super.onCreateOptionsMenu(menu);
		Log.v("entered on options","true");
		MenuItem edit =menu.add(0,1,Menu.NONE,"Edit");
		edit.setIcon(R.drawable.editicon);
		MenuItem delete=menu.add(0,2,Menu.NONE,"Delete");
		delete.setIcon(R.drawable.deleteicon);
		
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item)
	{
		super.onOptionsItemSelected(item);
		final DbAdapter dbAd=new DbAdapter(this);
		
		if(item.getItemId()==2)
		{
			AlertDialog.Builder delete =new AlertDialog.Builder(this);
			
			dbAd.open();
			Cursor delc=dbAd.fetchRowById(String.valueOf(id));
			startManagingCursor(delc);
			delc.moveToFirst();
			final String photo=delc.getString(delc.getColumnIndex("photo"));
			final String note=delc.getString(delc.getColumnIndex("name"));
			delete.setTitle("Delete "+note+" ?");
			delete.setMessage("Are You Sure ?");
			
			delete.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
					dbAd.delete(id);
					File f=new File(Environment.getExternalStorageDirectory()+"/SMI1/"+photo);
					if(f.exists())
					{
					f.delete();
					img.clearCache();
					}
					
					
					Toast toast =Toast.makeText(getApplicationContext(),"Note "+note+" Deleted", Toast.LENGTH_LONG);
					toast.show();
					//dbAd.close();
					finish();
					
				}
			});
			
			delete.setNegativeButton("No", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
					
					
				}
			});
			
			AlertDialog deleteDialog=delete.create();
			deleteDialog.show();
			
			
		}
		
		if(item.getItemId()==1)
		{
			dbAd.open();
			Cursor editc=dbAd.fetchRowById(String.valueOf(id));
			startManagingCursor(editc);
			editc.moveToFirst();
			String note=null;String photo=null;String name=null;String or=null;
			
			note=editc.getString(editc.getColumnIndex("notes"));
			photo=editc.getString(editc.getColumnIndex("photo"));
			name=editc.getString(editc.getColumnIndex("name"));
			or=editc.getString(editc.getColumnIndex("orientation"));
			Intent i=new Intent();
			i.putExtra("notes", note);
			i.putExtra("photo", photo);
			i.putExtra("_id", id);
			i.putExtra("name", name);
			i.putExtra("orientation", or);
			i.setClass(getApplicationContext(), newpost.class);
			Note.this.startActivityForResult(i, requestcode);		
			//dbAd.close();
			
		}
		
		
		return true;
	}
	
	public void onActivityResult(int requestCode,int resultCode,Intent data)
	{
		if(requestCode==requestcode)
		{
			if(resultCode==RESULT_OK)
			{
				
				reload();
				
			}
		}
	}
	
	
	
	public void reload() {

	    Intent intent = getIntent();
	
	    finish();

	  
	    startActivity(intent);
	}
	

}

package com.android.SamsungMIv4;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONStringer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SamsungMIv4Activity extends FacebookActivity {

	Button img1;
	Button img2;
	Button img3;
	Button img6;
	Button img4;
	Button img5;
	Gallery gallery;
	int height;
	int width;
	LinearLayout buttons;
	LinearLayout gal;
	List<String> photo;
	boolean resume = false;
	String message;
	ImageLoader img;
	DbAdapter dbAd;
	showGallery galShow;
	TextView welcome;
	int galleryWidth;

	@Override
	public void onCreate(Bundle Icicle) {
		super.onCreate(Icicle);
		setContentView(R.layout.dashboard);
		img = new ImageLoader(this);
		

		img1 = (Button) findViewById(R.id.mylocation_button);
		img2 = (Button) findViewById(R.id.searchlocation_button);
		img3 = (Button) findViewById(R.id.newpost_button);
		img4 = (Button) findViewById(R.id.oldposts_button);
		gallery = (Gallery) findViewById(R.id.gallery);
		welcome = (TextView) findViewById(R.id.welcome);
		dbAd = new DbAdapter(this);
		photo=new ArrayList<String>();
		galShow=new showGallery();
		Typeface font = Typeface.createFromAsset(getAssets(), "scrible.ttf");
		welcome.setTypeface(font);
		buttons = (LinearLayout) findViewById(R.id.dashboardButton);
		gal = (LinearLayout) findViewById(R.id.galleryLayout);

		img1.setTypeface(font);
		img2.setTypeface(font);
		img3.setTypeface(font);
		img4.setTypeface(font);
		
		img1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent();
				i.setClass(SamsungMIv4Activity.this, currentlocation.class);
				SamsungMIv4Activity.this.startActivity(i);
			}
		});

		img4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent i = new Intent();
				i.setClass(SamsungMIv4Activity.this, TravelbuddyActivity.class);
				SamsungMIv4Activity.this.startActivity(i);

			}
		});

		img2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent();
				i.setClass(SamsungMIv4Activity.this, searchlocation.class);
				SamsungMIv4Activity.this.startActivity(i);
			}
		});

		img3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent();
				i.setClass(SamsungMIv4Activity.this, newpost.class);
				SamsungMIv4Activity.this.startActivity(i);
			}
		});

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		MenuItem facebook = menu.add(0, 1, Menu.NONE, "Settings");
		facebook.setIcon(R.drawable.facebook);
		return true;

	}

	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		if (item.getItemId() == 1) {
			AlertDialog.Builder Login = new AlertDialog.Builder(this);
			Login.setTitle("Facebook Settings");

			setConnection();

			if (isSession()) {
				message = "Logout";

				Login.setMessage(message);

			} else {
				Log.v("tag", "inside else");

				message = "Login";

				Login.setMessage(message);
			}

			Login.setPositiveButton(message, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub

					if (message.equals("Login")) {

						getID("", "", 0, 0, "");

					}

					if (message.equals("Logout")) {
						logout();

					}

				}
			});

			AlertDialog showDialog = Login.create();
			showDialog.show();

		}
		return true;

	}
	
	@Override
	public void onWindowFocusChanged(boolean hasChanged)
	{
		super.onWindowFocusChanged(hasChanged);
		
		Display display=getWindowManager().getDefaultDisplay();
		Rect reactangle=new Rect();
		Window window=getWindow();
		window.getDecorView().getWindowVisibleDisplayFrame(reactangle);
		int  StatusBarHeight=reactangle.top;
		height=display.getHeight()-StatusBarHeight-50;
		width=display.getWidth();
		
		if(display.getHeight()>display.getWidth())
		{
			galleryWidth=width/2;
		}
		else if(display.getWidth()>display.getHeight())
		{
			galleryWidth=width/3;
		}
		else
			galleryWidth=width/2;
		
		Log.v("gallerywidth",galleryWidth+"");
		
		initGallery();
		
	}
	
	
	class showGallery extends Handler
	{
		@Override
		public void handleMessage(Message msg)
		{
			
			super.handleMessage(msg);
			gallery();
			
			
		}
	}
	
	void initGallery()
	{
		Thread initGallery=new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				dbAd.open();
				String photoName = "";
				photo.clear();
				Cursor images = dbAd.fetchAll();

				startManagingCursor(images);
				if (images.moveToFirst()) {
					do {
						photoName = images.getString(images.getColumnIndex("photo"));
						Log.v("photo", photoName);

						if (!photoName.equals("")) {
							photo.add(photoName);
						}

					} while (images.moveToNext());
				
					images.close();
					galShow.sendEmptyMessage(0);
			}
				
			
				
		}
		});
		
		initGallery.start();
	}

	public void gallery() {
	

		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, height / 3);
		gal.setLayoutParams(param);
			gallery.setAdapter(new ImageAdapter(this));

			gallery.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView parent, View v,
						int position, long id) {

					Intent photoview = new Intent();
					photoview.setAction(Intent.ACTION_VIEW);
					photoview.setDataAndType(
							Uri.parse("file://" + "/sdcard/SMI1/"
									+ photo.get(position)), "image/*");

					startActivity(photoview);

				}
			});

		
	}

	public class ImageAdapter extends BaseAdapter {
		int mGalleryItemBackground;
		private Context mContext;

		public ImageAdapter(Context c) {

			mContext = c;
			TypedArray attr = mContext
					.obtainStyledAttributes(R.styleable.HelloGallery);
			mGalleryItemBackground = attr.getResourceId(
					R.styleable.HelloGallery_android_galleryItemBackground, 0);
			attr.recycle();

		}

		public int getCount() {
			return photo.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			ImageView imageView = new ImageView(mContext);

			img.DisplayImage(photo.get(position), SamsungMIv4Activity.this, imageView, 70);
			imageView.setLayoutParams(new Gallery.LayoutParams(galleryWidth,
					LinearLayout.LayoutParams.WRAP_CONTENT));
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			imageView.setBackgroundResource(mGalleryItemBackground);

			return imageView;

		}
	}
	
	

	@Override
	public void onResume() {
		super.onResume();
		if (!resume) {

			resume = true;
			return;
		}

		initGallery();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();


	}

}

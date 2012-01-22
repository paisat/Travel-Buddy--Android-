package com.android.SamsungMIv4;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

public class newpost extends FacebookActivity {

	Button i1;
	Button i2;
	Button i3;
	Button i4;
	Button i5;
	Button i6;
	ImageView photo;
	String location = "";
	String countryname = "";
	Long photoId;
	LocationManager locationManager;
	EditText text;

	LinearLayout note;

	LinearLayout buttonLayout;
	String notes;
	Long _id;

	String photoName = "";
	String photoBundle;
	String namebundle = "";
	DbAdapter dbAd = new DbAdapter(this);
	String textName;
	int height;
	int width;
	boolean resumeHasRun = false;
	EditText nameField;
	boolean clicked = false;
	int cameraCode = 1;
	int twitterCode = 100;
	Bundle saved;
	ScrollView scroll;
	int measure;
	int height_tmp, width_tmp;
	int oHeight, oWidth;
	ImageLoader loader;
	int orientation = 0;
	Activity a;
	AlertDialog.Builder photoDialog;
	int SELECT_IMAGE = 5;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newpostlayout);
		i1 = (Button) findViewById(R.id.camera_button);
		i2 = (Button) findViewById(R.id.facebook_button);
		i3 = (Button) findViewById(R.id.twitter_button);
		i4 = (Button) findViewById(R.id.save_button);
		i5 = (Button) findViewById(R.id.cancel_button);
		i6 = (Button) findViewById(R.id.website_button);
		note = (LinearLayout) findViewById(R.id.note);

		text = (EditText) findViewById(R.id.maineditText);
		note = (LinearLayout) findViewById(R.id.note);
		buttonLayout = (LinearLayout) findViewById(R.id.buttonLayout);
		photo = (ImageView) findViewById(R.id.photo);
		scroll = (ScrollView) findViewById(R.id.scroll);
		loader = new ImageLoader(this);
		photoName = "";
		photoDialog = new AlertDialog.Builder(this);

		Typeface font = Typeface.createFromAsset(getAssets(), "scrible.ttf");
		text.setTypeface(font);
		a = this;

		Display display = getWindowManager().getDefaultDisplay();
		width = display.getWidth();
		height = display.getHeight();
		Log.v("Actual height", height + "");
		Log.v("Actual width", width + "");

		oHeight = height - 35;
		oWidth = width;
		Log.v("orientation", "potrait");

		saved = getIntent().getExtras();

		if (saved != null) {
			_id = saved.getLong("_id");

			photoBundle = saved.getString("photo");
			notes = saved.getString("notes");
			namebundle = saved.getString("name");
			Log.v("id", _id + "");
			Log.v("note", notes);
			Log.v("name", namebundle);
			Log.v("photo", photoBundle);
			photoId = _id - 1;
			Log.v("photoid", photoId + "");
			if (!photoBundle.equals("") || photoBundle != null) {
				photoName = photoBundle;
				resumeHasRun = true;
				String porientation = saved.getString("orientation");
				if (porientation.equals(""))
					porientation = "0";
				orientation = Integer.parseInt(porientation);

			} else {
				LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.FILL_PARENT, oHeight / 2);

				photo.setVisibility(View.GONE);
				buttonLayout.setLayoutParams(param);
				scroll.setLayoutParams(param);
				text.setLayoutParams(param);
				orientation = 0;
			}

			text.setText(notes);

		}

		else {

			LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT, oHeight / 2);

			photo.setVisibility(View.GONE);
			buttonLayout.setLayoutParams(param);
			text.setLayoutParams(param);
			scroll.setLayoutParams(param);

			dbAd.open();
			Cursor c = dbAd.fetchAll();
			startManagingCursor(c);

			if (!(c.moveToFirst())) {
				Log.v("condition", "here");
				photoId = 0L;

			} else {
				c.moveToFirst();
				photoId = c.getLong(c.getColumnIndex("_id"));

			}
			Log.v("photo", "" + photoId);

			dbAd.close();
		}

		/*
		 * locationManager =
		 * (LocationManager)getSystemService(LOCATION_SERVICE); String provider
		 * = locationManager.getBestProvider(new Criteria(), true); Location l =
		 * locationManager.getLastKnownLocation(provider); Double cur_lat =
		 * l.getLatitude(); Double cur_lng = l.getLongitude(); Geocoder g = new
		 * Geocoder(this); try { List<Address> list = g.getFromLocation(cur_lat,
		 * cur_lng, 1); Address a = list.get(0); String location =
		 * a.getLocality(); String countryname = a.getCountryName(); Toast tost
		 * = Toast.makeText(newpost.this, location + ", " + countryname,
		 * Toast.LENGTH_LONG); tost.show();
		 * 
		 * //Toast tost = Toast.makeText(newpost.this, "Latitude : " +
		 * Double.toString(a.getLatitude()) +"\n" + "Longitude : " +
		 * Double.toString(a.getLongitude()) + "\n" + "Place : "
		 * +a.getAddressLine(0), Toast.LENGTH_LONG); //tost.show(); } catch
		 * (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */

		// i1.setClickable(true);

		i6.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				final String notes = text.getText() + "";
				// Toast toast = Toast.makeText(newpost.this, "Clicked",
				// Toast.LENGTH_LONG);
				// toast.show();

				Thread thread = new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub

						// Bitmap bitmap =
						// BitmapFactory.decodeResource(getResources(),R.drawable.icon);
						// Bitmap bitmap2 =
						// BitmapFactory.decodeFile("/sdcard/android.jpg");
						Bitmap b;
						// File file = new
						// File(Environment.getExternalStorageDirectory()+"/SMI1/"+photoName);
						if (photoName.equals("")) {
							b = BitmapFactory.decodeResource(getResources(),
									R.drawable.icon);
						} else {
							b = BitmapFactory.decodeFile(Environment
									.getExternalStorageDirectory()
									+ "/SMI1/"
									+ photoName);
						}

						ByteArrayOutputStream stream = new ByteArrayOutputStream();

						b.compress(Bitmap.CompressFormat.JPEG, 90, stream);

						byte[] byte_arr = stream.toByteArray();

						String image_str = Base64.encodeBytes(byte_arr);

						ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

						nameValuePairs.add(new BasicNameValuePair("image",
								image_str));
						nameValuePairs.add(new BasicNameValuePair("id", Long
								.toString(photoId)));
						if (photoId == 0) {
							nameValuePairs.add(new BasicNameValuePair("reset",
									"true"));
						} else {
							nameValuePairs.add(new BasicNameValuePair("reset",
									"false"));
						}

						String loc = null;
						// Toast t = Toast.makeText(newpost.this, notes,
						// Toast.LENGTH_LONG);
						// t.show();

						loc = getLocation();
						// loc = "Bangalore";
						// nameValuePairs.add(new BasicNameValuePair("location",
						// "Bangalore"));
						nameValuePairs.add(new BasicNameValuePair("location",
								loc));
						nameValuePairs.add(new BasicNameValuePair("comment",
								notes));
						TelephonyManager tp = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
						String str = tp.getDeviceId().toString();
						nameValuePairs.add(new BasicNameValuePair("imei", str));

						try {

							HttpClient httpclient = new DefaultHttpClient();

							HttpPost httppost = new HttpPost(
									"http://travelbuddy.x10.mx/upload_image.php");

							httppost.setEntity(new UrlEncodedFormEntity(
									nameValuePairs));

							httpclient.execute(httppost);

							Toast toast = Toast.makeText(newpost.this,
									"UPLOADED!", Toast.LENGTH_LONG);
							toast.show();

							// HttpResponse response =
							// httpclient.execute(httppost);

							// String the_string_response =
							// convertResponseToString(response);

							// Toast.makeText(TrialActivity.this, "Response " +
							// the_string_response, Toast.LENGTH_LONG).show();

						} catch (Exception e) {

							// Toast.makeText(newpost.this,
							// "ERROR! Could not upload ",
							// Toast.LENGTH_LONG).show();

							System.out.println("Error in http connection "
									+ e.toString());

						}

					}
				});

				thread.start();

				popup();

			}
		});

		i3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String notes = text.getText() + "";
				String loc = null;
				// Toast t = Toast.makeText(newpost.this, notes,
				// Toast.LENGTH_LONG);
				// t.show();
				loc = getLocation();
				// loc = "Bangalore";
				Intent intent = new Intent();
				intent.setClass(newpost.this, TwitPicActivity.class);
				intent.putExtra("notes", notes);
				intent.putExtra("location", loc);
				intent.putExtra("photoname", photoName);
				startActivityForResult(intent, twitterCode);
			}
		});

		i1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				photoDialog.setTitle("photo");
				photoDialog.setMessage("Choose Action");
				photoDialog.setPositiveButton("Camera",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								// TODO Auto-generated method stub

								Intent i = new Intent();
								i.putExtra("id", photoId);
								i.setClass(newpost.this, camera.class);
								newpost.this.startActivityForResult(i,
										cameraCode);
							}
						});

				photoDialog.setNegativeButton("Gallery",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								imageGallery();

							}
						});

				AlertDialog photoD = photoDialog.create();
				photoD.show();

			}
		});

		i4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (photoName.equals("")
						&& text.getText().toString().equals("")) {
					Toast toast = Toast.makeText(getApplicationContext(),
							"Nothing to Save", Toast.LENGTH_LONG);
					toast.show();

				} else

					popup();

			}
		});

		i5.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				File file = new File(Environment.getExternalStorageDirectory()
						+ "/SMI1/" + photoName);
				if (file.exists() && saved == null) {
					file.delete();

				}
				if (saved != null) {
					setResult(RESULT_OK);
					finish();
				} else
					finish();
			}
		});

		i2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				setConnection();
				if (isSession()) {
					getID(text.getText().toString(), photoName, orientation,
							oHeight, getLocation());
					popup();
				} else {
					Toast toast = Toast
							.makeText(
									getApplicationContext(),
									"Please go To the Dashboard and Login by pressing the option button",
									Toast.LENGTH_LONG);
					toast.show();

				}

			}
		});
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == twitterCode) {
			popup();
		}
		if (requestCode == cameraCode) {
			if (resultCode == RESULT_OK) {
				loader.clearCache();
				photo.setImageBitmap(null);
				photoName = data.getStringExtra("photoname");
				orientation = data.getIntExtra("orientation", 0);

				Log.v("newpost.java photoname", photoName + "hi");
			}
		}
		if (resultCode == RESULT_OK) {

			if (requestCode == SELECT_IMAGE) {
				loader.clearCache();
				photo.setImageBitmap(null);
				String mSelectedImagePath = getPath(data.getData());

				Log.v("selectedImagePath", mSelectedImagePath);

				try {
					File sd = Environment.getExternalStorageDirectory();
					// File dat = Environment.getDataDirectory();
					if (sd.canWrite()) {

						String destinationImagePath = "/SMI1/" + "test"
								+ photoId + ".jpg";
						File source = new File(mSelectedImagePath);

						File destination = new File(
								Environment.getExternalStorageDirectory()
										+ destinationImagePath);
						if (source.exists()) {
							Log.v("inside", "copy");

							FileChannel src = new FileInputStream(source)
									.getChannel();
							FileChannel dst = new FileOutputStream(destination)
									.getChannel();
							dst.transferFrom(src, 0, src.size());
							src.close();
							dst.close();
							photoName = "test" + photoId + ".jpg";
							orientation = -90;
						}

						else
							photoName = "";

					} else
						photoName = "";
				} catch (Exception e) {

				}

			}
		}
	}

	public void popup() {

		
		final AlertDialog.Builder name = new AlertDialog.Builder(this);
		name.setTitle("Name");

		name.setMessage("Give a Name for note");
		nameField = new EditText(this);
		nameField.setSingleLine();
		if (namebundle.equals(""))
			nameField.setText("");
		else
			nameField.setText(namebundle);

		name.setView(nameField);

		name.setPositiveButton("ok", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

				if (nameField.getText().toString().equals("")) {
					Toast toast = Toast.makeText(getApplicationContext(),
							"Enter Name", Toast.LENGTH_LONG);
					toast.show();
					popup();
				} else {
					String loc = getLocation();
					//String loc=null;
					String notes = text.getText() + "";

					if (saved == null) {
						dbAd.open();

						String date = DateFormat.getDateInstance().format(
								new Date());

						dbAd.insertRow(nameField.getText().toString(), notes,
								loc, photoName, date, orientation + "");

						dbAd.close();
						Toast toast = Toast.makeText(getApplicationContext(),
								"Saved", Toast.LENGTH_LONG);
						toast.show();
						
						Intent result=new Intent();
						if(photoName!=null)
						result.putExtra("isPhotoTaken",true );
						else
							result.putExtra("isPhototaken", false);
						
						setResult(RESULT_OK, result);
						finish();
						
						
					} else {

						String date = DateFormat.getDateInstance().format(
								new Date());
						dbAd.open();
						dbAd.update(_id, "notes", notes);
						dbAd.update(_id, "photo", photoName);
						Log.v("update photoname", photoName + "hi");
						dbAd.update(_id, "name", nameField.getText().toString());
						dbAd.update(_id, "orientation", orientation + "");
						dbAd.update(_id, "date", date);
						dbAd.close();
						Toast toast = Toast.makeText(getApplicationContext(),
								"Updated", Toast.LENGTH_LONG);
						toast.show();

						setResult(RESULT_OK);
						finish();

					}
				}

			}
		});

		AlertDialog nameDialog = name.create();
		nameDialog.show();

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		Display display = getWindowManager().getDefaultDisplay();
		width = display.getWidth();
		height = display.getHeight();
		Log.v("Actual height", height + "");
		Log.v("Actual width", width + "");

		oHeight = height - 35;
		oWidth = width;

		if (photoName.equals("")) {

			LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT, oHeight / 2);

			photo.setVisibility(View.GONE);
			buttonLayout.setLayoutParams(param);
			text.setLayoutParams(param);
			scroll.setLayoutParams(param);
		} else {

			LinearLayout.LayoutParams param6 = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT, oHeight / 2);
			LinearLayout.LayoutParams param4 = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT, 100);
			text.setLayoutParams(param4);
			buttonLayout.setLayoutParams(param6);
			scroll.setLayoutParams(param6);

		}

	}

	public void imageGallery() {
		Intent gallery = new Intent(Intent.ACTION_PICK,
				MediaStore.Images.Media.INTERNAL_CONTENT_URI);
		startActivityForResult(gallery, SELECT_IMAGE);
	}

	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		startManagingCursor(cursor);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	@Override
	public void onResume() {
		super.onResume();

		if (!resumeHasRun) {
			resumeHasRun = true;
			return;

		}

		if (!photoName.equals("")) {

			Log.v("height", height + "");
			Log.v("width", width + "");

			int photolayout = (oHeight) / 2;

			LinearLayout.LayoutParams param6 = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT, oHeight / 2);
			LinearLayout.LayoutParams param4 = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT, 100);
			text.setLayoutParams(param4);
			buttonLayout.setLayoutParams(param6);
			scroll.setLayoutParams(param6);
			Log.v("photolayout", photolayout + "");

			photo.setVisibility(View.VISIBLE);

			loader.DisplayImage(photoName, a, photo, photolayout, orientation
					+ "");

			photo.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					Intent photoview = new Intent();
					photoview.setAction(Intent.ACTION_VIEW);
					photoview.setDataAndType(
							Uri.parse("file://" + "/sdcard/SMI1/" + photoName),
							"image/*");
					startActivity(photoview);

				}
			});
		}
	}

	public String getLocation() {

		String loc = null;
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		String provider = locationManager.getBestProvider(new Criteria(), true);
		Location l = locationManager.getLastKnownLocation(provider);
		if(l!=null)
		{
			Double cur_lat = l.getLatitude();
			Double cur_lng = l.getLongitude();
			Geocoder g = new Geocoder(newpost.this);
			try {
				List<Address> list = g.getFromLocation(cur_lat, cur_lng, 1);
				Address a = list.get(0);
				loc = a.getAddressLine(0);
				loc += ", " + a.getCountryName();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return loc;
	}

}

package com.android.SamsungMIv4;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class HelloItemizedOverlay extends ItemizedOverlay<OverlayItem> {

	private Context mContext;
	private ArrayList<OverlayItem> mOverLays = new ArrayList<OverlayItem>();
	
	public HelloItemizedOverlay(Drawable defaultMarker)
	{
		super(boundCenterBottom(defaultMarker));
	}
	
	public HelloItemizedOverlay(Drawable defaultMarker, Context context)
	{
		super(defaultMarker);
		mContext = context;
	}
	
	public void addOverlay(OverlayItem overlay)
	{
		mOverLays.add(overlay);
		populate();
	}

	@Override
	protected OverlayItem createItem(int i) {
		// TODO Auto-generated method stub
		return mOverLays.get(i);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return mOverLays.size();
	}
	
	@Override
	protected boolean onTap(int index) {
	  OverlayItem item = mOverLays.get(index);
	  AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
	  dialog.setTitle(item.getTitle());
	  dialog.setMessage(item.getSnippet());
	  dialog.show();
	  return true;
	}
}
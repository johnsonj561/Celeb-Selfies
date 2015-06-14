package com.putty.celebselfie_v2;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RelativeLayout;

public class GetSignature extends Activity {

	/**
	 * Release resources to prevent memory leaks
	 */
	@Override
	protected void onPause() {
		Log.d("SIG", "onPause()");
		if(parent != null){
			parent.removeView(myDrawView);
			parent = null;
			Log.d("SIG", "parent.removeView");
		}
		if(myDrawView != null){
			myDrawView.destroyDrawingCache();
			myDrawView = null;
			Log.d("SIG", "myDrawView.destroyDrawingCache()");
		}		super.onPause();
	}

	/**
	 * Release resources to prevent memory leaks
	 */
	@Override
	protected void onStop() {
		Log.d("SIG", "onStop()");
		if(parent != null){
			parent.removeView(myDrawView);
			parent = null;
			Log.d("SIG", "parent.removeView");
		}
		if(myDrawView != null){
			myDrawView.destroyDrawingCache();
			myDrawView = null;
			Log.d("SIG", "myDrawView.destroyDrawingCache()");
		}
		super.onStop();
	}

	protected void onStart(){
		parent = (RelativeLayout) findViewById(R.id.signImageParent);
		myDrawView = new MyDrawView(this);
		parent.addView(myDrawView);
		super.onStart();
	}


	/**
	 * Initialize DrawView
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_signature);
		// Removing title from action bar, until I find a better way to do this
		getActionBar().setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
		
		//launch color dialog first to make user aware of options
		//otherwise user will tend to use black ink, which doesn't show up clearly
		showColorDialog();
	}

	
	
	/**
	 * Display menu options to user
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.get_signature, menu);
		return true;
	}

	/**
	 * Handle menu selections
	 */
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_submit_sig:
			saveSignature();
			Intent i = new Intent(GetSignature.this, SignedPicture.class);
			//i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i);
			return true;
		case R.id.action_clear_sig:
			myDrawView.clear();
			return true;
		case R.id.action_change_sig_thickness:
			showThicknessDialog();
			return true;
		case R.id.action_change_sig_color:
			showColorDialog();
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Capture signature from parent view and save it as byte[]
	 */
	public void saveSignature() {
		Log.d("SIG", "Saving signature");
		parent.setDrawingCacheEnabled(true);
		b = parent.getDrawingCache();
		//ByteArrayOutputStream stream = new ByteArrayOutputStream();
		//b.compress(Bitmap.CompressFormat.PNG, 100, stream);
		//rawSig = stream.toByteArray();
		setResult(RESULT_OK);
		
		//free resources
		//b.recycle();	//this may be unecessary as b scope is local to saveSignature()
		//try {
			//stream.close();
			//Log.d("SIG", "saveSignature stream closed");
		//} catch (IOException e) {
			//e.printStackTrace();
		//}
	}

	/**
	 * Display color dialog fragment to user.
	 */
	public void showColorDialog() {
		FragmentManager fragManager = getFragmentManager();
		ColorDialog colorDialog = new ColorDialog();
		colorDialog.show(fragManager, "dialog_color_choices");
	}
	
	/**
	 * Display selection of sizes to user
	 */
	public void showThicknessDialog(){
		FragmentManager fragManager2 = getFragmentManager();
		ThicknessDialog thicknessDialog = new ThicknessDialog();
		thicknessDialog.show(fragManager2, "thickness_choices");
	}

	/**
	 * Handle return from Color Dialog
	 * @param color
	 */
	public void onColorDialogReturn(int color) {
		myDrawView.changePaintColor(color);
	}

	/**
	 * Handle return from Thickness Dialog
	 * @param size
	 */
	public void onThicknessDialogReturn(int size){
		myDrawView.changeSigThickness(size);
	}
	
	
	public static Bitmap b;
	private RelativeLayout parent;
	public static byte[] rawSig;
	private MyDrawView myDrawView;
}

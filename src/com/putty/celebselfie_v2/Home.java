package com.putty.celebselfie_v2;

import java.io.File;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
 * Landing page Simple layout that provides basic features 
 * 1 - take new photo 2 - view current photos 3 - update profile
 * @author Pustikins
 * 
 */
public class Home extends Activity {

	/**
	 * Initialize variables on activity creation
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		//Removing title from action bar, until I find a better way to do this
		getActionBar().setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
		
		// link buttons to corresponding intents
		linkButtons();
	}

	/**
	 * Link buttons and set up OnClickListerners to handle selections
	 */
	private void linkButtons() {
		takePhotoButton = (Button) findViewById(R.id.takePhotoButton);
		takePhotoButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// LAUNCH CAMERA
				//if user is in landscape mode, prompt them to rotate screen to portrait
				if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
					Toast.makeText(getApplicationContext(), "Rotate Screen", Toast.LENGTH_LONG).show();
				}
				Intent i = new Intent(Home.this, CameraActivity.class);
				startActivity(i);
			}
		});

		//Launches device's built in gallery to appropriate directory
		viewPhotosButton = (Button) findViewById(R.id.viewPhotosButton);
		viewPhotosButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// LAUNCH GALLERY
				launchGallery();
			}
		});

		//To be completed - a use profile that allows user to enter basic info
		editProfileButton = (Button) findViewById(R.id.editProfileButton);
		editProfileButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// LAUNCH PROFILE SETTINGS
				Toast.makeText(getApplicationContext(), "Coming Soon", Toast.LENGTH_SHORT).show();
			}
		});
	}

	/**
	 * Launch photo gallery
	 * Open to application's saved photo directory 'CelebSelfies'
	 */
	private void launchGallery() {
		File folder = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				"CelebSelfies/");
		
		//if folder exists, create singlemediascanner
		if(folder.exists()){
			File[] allFiles = folder.listFiles();

			// Log most recent photo//Debugging purposes
			Log.d("allFiles", allFiles[allFiles.length - 1].toString());
			Log.d("allFiles", "Photo Array Length: " + allFiles.length);
			// Open media scanner to the most recent photo
			new SingleMediaScanner(Home.this, allFiles[allFiles.length - 1]);
		}
		//else notify user that no photos exist
		else{
			Toast.makeText(getApplicationContext(), "No Photos Taken", Toast.LENGTH_SHORT).show();
		}
		
	}

	
	/**Scan device media for CelebSelfie Picture Directory
	 * Launch device gallery intent ACTION_VIEW, opens CelebSelfie directory
	 */
	public class SingleMediaScanner implements MediaScannerConnectionClient {

		private MediaScannerConnection mMs;
		private File mFile;

		public SingleMediaScanner(Context context, File f) {
			mFile = f;
			mMs = new MediaScannerConnection(context, this);
			mMs.connect();
		}

		
		public void onMediaScannerConnected() {
			mMs.scanFile(mFile.getAbsolutePath(), null);
		}

		public void onScanCompleted(String path, Uri uri) {
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(uri);
			startActivity(i);
			mMs.disconnect();
		}
	}
		
	private Button takePhotoButton;
	private Button viewPhotosButton;
	private Button editProfileButton;

}
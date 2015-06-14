package com.putty.celebselfie_v2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

public class SignedPicture extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_signed_picture);

		// Get a copy of the photo and signature
		if (savedInstanceState != null) {
			sigImagePosition = savedInstanceState.getInt("sigImagePosition");
		} else {
			sigImagePosition = 3;
		}

		// connect buttons and onClickListeners
		linkButtons();

		// draw portrait and handle any errors
		drawPortrait();

		// make sure signature is in correct position from previous instance
		moveSig(sigImagePosition);
	}

	/**
	 * Save raw image data to be drawn on re-start
	 */
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("sigImagePosition", sigImagePosition);
	}

	/**
	 * Handle back button by starting a new GetSignature activity
	 */
	@Override
	public void onBackPressed() {
		Log.d("SignedPicture", "onBackPressed Called");
		GetSignature.b.recycle();
		Intent i = new Intent(SignedPicture.this, GetSignature.class);
		startActivity(i);
	}

	
	/**
	 * Draw both photo and signature bitmaps on imageviews to display to user
	 */
	private void drawPortrait() {
		// draw photo to photo imageview
		photoImageView = (ImageView) findViewById(R.id.celeb_photo_image);
		photoImageView.setImageBitmap(drawBitmap(CameraActivity.rawPhoto));
		// draw signature to each of the 3 signature imageviews
		// Note - only 1 sigImageView can be active at any time, handled by
		// moveSig()
		sigImageView1 = (ImageView) findViewById(R.id.sig_image1);
		sigImageView1.setImageBitmap(GetSignature.b);
		sigImageView2 = (ImageView) findViewById(R.id.sig_image2);
		sigImageView2.setImageBitmap(GetSignature.b);
		sigImageView3 = (ImageView) findViewById(R.id.sig_image3);
		sigImageView3.setImageBitmap(GetSignature.b);
	}

	/**
	 * Connect all buttons and set up onClickListeners to handle user input
	 */
	private void linkButtons() {

		// save Portrait to SD card
		savePortraitButton = (Button) findViewById(R.id.save_portrait);
		savePortraitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				hideButtons();
				savePhoto();
				displaySavedDialog();

			}
		});

		// Return to home screen without saving image
		// Releases resources to prevent memory leaks
		erasePortraitButton = (Button) findViewById(R.id.discard_portrait);
		erasePortraitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				GetSignature.b.recycle();
				CameraActivity.rawPhoto = null;
				Intent i = new Intent(SignedPicture.this, Home.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(i);
			}
		});

		// Launch GetSignature activity to obtain a new signature
		reSignButton = (Button) findViewById(R.id.get_new_sig);
		reSignButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				GetSignature.b.recycle();
				Intent i = new Intent(SignedPicture.this, GetSignature.class);
				startActivity(i);
				finish();
			}
		});

		// Move signature to the next position
		// If position is invalid, default to 1
		moveSigButton = (Button) findViewById(R.id.move_sig);
		moveSigButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sigImagePosition = (sigImagePosition < 1 || sigImagePosition > 2) ? sigImagePosition = 1
						: sigImagePosition + 1;
				moveSig(sigImagePosition);
			}
		});
	}

	/**
	 * Convert array of raw data to bitmap image
	 * 
	 * @param data
	 *            Array containing raw image
	 * @return Bitmap generated from byte[]
	 */
	private Bitmap drawBitmap(byte[] data) {
		Log.d("drawBitmap", "data size: " + data.length);
		Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion < android.os.Build.VERSION_CODES.KITKAT) {
			//Note - getAllocationByteCount was added in API 19
			Log.d("drawBitmap", "bitmap size: " + bitmap.getAllocationByteCount());
		}
		if (bitmap == null) {
			Log.d("Camera", "bitmap is null");
		} else {
			Log.d("CAMERA", "bitmap decoded succesfully");
		}
		return bitmap;
	}

	/**
	 * Move signature to a new location
	 * 
	 * @param position
	 *            that sig is being moved to
	 */
	private void moveSig(int position) {
		switch (position) {
		case 1:
			sigImageView1.setVisibility(View.VISIBLE);
			sigImageView2.setVisibility(View.INVISIBLE);
			sigImageView3.setVisibility(View.INVISIBLE);
			break;
		case 2:
			sigImageView1.setVisibility(View.INVISIBLE);
			sigImageView2.setVisibility(View.VISIBLE);
			sigImageView3.setVisibility(View.INVISIBLE);
			break;
		case 3:
			sigImageView1.setVisibility(View.INVISIBLE);
			sigImageView2.setVisibility(View.INVISIBLE);
			sigImageView3.setVisibility(View.VISIBLE);
			break;
		default:// default to position 3
			sigImageView1.setVisibility(View.INVISIBLE);
			sigImageView2.setVisibility(View.INVISIBLE);
			sigImageView3.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * Convert entire portrait to a bitmap for storage purposes
	 * 
	 * @param view
	 *            being converted to bitmap
	 * @return bitmap containing portrait photo and signature
	 */
	public static Bitmap getBitmapFromView(View view) {
		// Define a bitmap with the same size as the view
		Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(),
				view.getHeight(), Bitmap.Config.ARGB_8888);
		// Bind a canvas to it
		Canvas canvas = new Canvas(returnedBitmap);
		// Get the view's background
		Drawable bgDrawable = view.getBackground();
		if (bgDrawable != null)
			// has background drawable, then draw it on the canvas
			bgDrawable.draw(canvas);
		else
			// does not have background drawable, default to blank bitmap
			canvas.drawColor(Color.WHITE);
		// draw the view on the canvas
		view.draw(canvas);
		// return the bitmap
		return returnedBitmap;
	}

	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(int type) {
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.

		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				"CelebSelfies");
		// This location works best if you want the created images to be shared
		// between applications and persist after your app has been uninstalled.

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("MyCameraApp", "failed to create directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
				.format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "IMG_" + timeStamp + ".jpg");
		} else if (type == MEDIA_TYPE_VIDEO) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "VID_" + timeStamp + ".mp4");
		} else {
			return null;
		}

		return mediaFile;
	}

	/**
	 * Make buttons invisible Needed for storing photo, don't want to store
	 * photo with buttons visible
	 */
	private void hideButtons() {
		savePortraitButton.setVisibility(View.INVISIBLE);
		erasePortraitButton.setVisibility(View.INVISIBLE);
		reSignButton.setVisibility(View.INVISIBLE);
		moveSigButton.setVisibility(View.INVISIBLE);
	}

	/**
	 * Alert user that photo has saved successfully
	 */
	private void displaySavedDialog() {
		photoSavedDialog = new PhotoSavedDialog();
		FragmentManager fm = getFragmentManager();
		photoSavedDialog.show(fm, "");
	}

	/**
	 * Save photo to device storage Uses new thread to handle heavy lifting
	 */
	private void savePhoto() {
		new Thread(new Runnable() {
			public void run() {
				finalPortrait = getBitmapFromView(findViewById(R.id.portraitBackground));
				File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);

				if (pictureFile == null) {
					Log.d("CAMERA",
							"Error creating media file, check storage permissions: ");
					return;
				}

				try {
					FileOutputStream strm = new FileOutputStream(pictureFile);
					finalPortrait.compress(CompressFormat.PNG, 80, strm);
					Log.d("CAMERA", "PICTURE SAVED");
					strm.close();
				} catch (IOException e) {
					Log.d("EXCEPTION", e.getMessage());
				}

				
				
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
				{
						Log.d("Portrait", " >= KitKat; scanning media gallery");
				        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
				        File f = new File("file://"+ Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));
				        Uri contentUri = Uri.fromFile(f);
				        mediaScanIntent.setData(contentUri);
				        sendBroadcast(mediaScanIntent);
				}
				else
				{
						Log.d("Portrait", "< KitKat; scanning media gallery");
				        sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
				}
				
		/*		
				// re-scan device to update the photo gallery with any new
				// pictures
				// NOT WORKING on 4.4 - Android added new security feature
				int currentapiVersion = android.os.Build.VERSION.SDK_INT;
				if (currentapiVersion < android.os.Build.VERSION_CODES.KITKAT) {
					sendBroadcast(new Intent(
							Intent.ACTION_MEDIA_MOUNTED,
							Uri.parse("file://"
									+ Environment.getExternalStorageDirectory())));
				}*/
			}
		}).start();


	}

	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	private Bitmap finalPortrait;
	private ImageView photoImageView;
	private ImageView sigImageView1;
	private ImageView sigImageView2;
	private ImageView sigImageView3;
	private int sigImagePosition;
	private Button savePortraitButton;
	private Button erasePortraitButton;
	private Button reSignButton;
	private Button moveSigButton;
	private PhotoSavedDialog photoSavedDialog;

}

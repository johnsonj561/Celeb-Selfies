package com.putty.celebselfie_v2;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;

public class CameraActivity extends Activity {

	/**
	 * Set layout and initialize variables NOTE - camera is instantiated in
	 * onStart()
	 * 
	 * @param savedInstanceState
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.camera_view);

		// initialize buttons & onClickListeners
		linkButtons();

	}

	/**
	 * Link buttons and OnClickListeners
	 */
	private void linkButtons() {
		
		captureButton = (Button) findViewById(R.id.button_capture);
		captureButton.bringToFront();
		captureButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// get an image from the camera
				mCamera.takePicture(shutterCallback, null,
						jpegPictureCallback);
				displaySaveOptions(true);
			}
		});
		
		rotateCamera = (Button) findViewById(R.id.rotate_camera);
		rotateCamera.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				rotateCamera();	
			}
		});
		

		savePhoto = (Button) findViewById(R.id.save_photo);
		savePhoto.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(CameraActivity.this, GetSignature.class);
				startActivity(i);
			}
		});

		// discard current photo and reset camera
		discardPhoto = (Button) findViewById(R.id.discard_photo);
		discardPhoto.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				displaySaveOptions(false);
				mCamera.startPreview();
				
			}
		});

		//When user clicks focusButton - disable capture button while focusing
		//NOTE - Most devices do not have autofocus on front facing camera!
		focusButton = (Button) findViewById(R.id.focus_button);
		focusButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				captureButton.setEnabled(false);
				mCamera.autoFocus(myAutoFocusCallback);
			}
		});

		
	}

	/**
	 * Enables or disables save/discard options
	 * 
	 * @param flag True will display save/discard options
	 * @param flag False will remove save/discard options
	 */
	private void displaySaveOptions(Boolean flag) {
		if (flag) {
			captureButton.setVisibility(Button.INVISIBLE);
			captureButton.setEnabled(false);
			rotateCamera.setVisibility(Button.INVISIBLE);
			rotateCamera.setEnabled(false);
			if(cameraID == 0){
				focusButton.setVisibility(Button.INVISIBLE);
				focusButton.setEnabled(false);
			}
			savePhoto.setVisibility(Button.VISIBLE);
			savePhoto.setEnabled(true);
			discardPhoto.setVisibility(Button.VISIBLE);
			discardPhoto.setEnabled(true);
		} else {
			captureButton.setVisibility(Button.VISIBLE);
			captureButton.setEnabled(true);
			rotateCamera.setVisibility(Button.VISIBLE);
			rotateCamera.setEnabled(true);
			if(cameraID == 0){
				focusButton.setVisibility(Button.VISIBLE);
				focusButton.setEnabled(true);
			}
			savePhoto.setVisibility(Button.INVISIBLE);
			savePhoto.setEnabled(false);
			discardPhoto.setVisibility(Button.INVISIBLE);
			discardPhoto.setEnabled(false);
		}
	}

	/**
	 * Free up camera and preview resources on pause
	 */
	@Override
	protected void onPause() {
		super.onPause();
		releaseCamera();
	}

	/**
	 * Free up camera and preview resources on stop
	 */
	@Override
	protected void onStop() {
		super.onStop();
		releaseCamera();
	}

	/**
	 * Start camera device and LOG error if fails Turn on appropriate camera
	 * buttons
	 */
	@Override
	protected void onStart() {
		super.onStart();
		if (!startCamera(cameraID)) {
			Log.d("CAMERA", "onStart() unable to startCamera()");
		}
		displaySaveOptions(false); // enables capture buttons//disables save &
									// discard options
	}

	/**
	 * Free up camera resource for other android activities 
	 * remove view and set mPreview = null
	 */
	private void releaseCamera() {
		if (mCamera != null) {
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}
		if (mPreview != null) {
			preview.removeView(mPreview);
			mPreview = null;
		}
	}

	/**
	 * Initialize camera and surface view to display preview
	 * @return Returns false if camera is not found
	 */
	private boolean startCamera(int direction) {
		if ((mCamera = getCameraInstance(direction)) == null) {
			return false;
		} else {
			mPreview = new CameraPreview(this, mCamera);
			preview = (FrameLayout) findViewById(R.id.camera_preview);
			preview.addView(mPreview);
			return true;
		}
	}


	/**
	 * Get instance of device camera Defaults to front facing camera for selfies
	 * :)
	 * @return instance of camera
	 * @return null if no camera exists
	 */
	public static Camera getCameraInstance(int direction) {
		Camera c = null;
		try {
				c = Camera.open(direction); //open appropriate camera hardware (front/back)
		} catch (Exception e) {
			Log.d("CAMERA", "Camera.open() failed" + e.getMessage());
		}
		return c; // returns null if camera is unavailable
	}
	
	/**
	 * Change camera direction
	 */
	private void rotateCamera(){
		//If device has only 1 camera, cameraID must remain 0
		if(Camera.getNumberOfCameras() == 1){
			cameraID = 0;
			releaseCamera();
			startCamera(cameraID);
			enableCameraOptions(true);
			
		}
		//else alternate between camera front and back facing cameras
		else{
			if(cameraID == 0){
				cameraID = 1;
				releaseCamera();
				startCamera(cameraID);
				enableCameraOptions(false);
			}
			else{
				cameraID = 0;
				releaseCamera();
				startCamera(cameraID);
				enableCameraOptions(true);
			}
		}
	}

	/**
	 * JPEG picture call back - do something with the picture!
	 */
	private PictureCallback jpegPictureCallback = new PictureCallback() {
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			Log.d("CAMERA", "jpegPictureCallback");
			rawPhoto = data;
		}
	};


	/**
	 * Once autofocus is complete, allow user to take photo
	 */
	private AutoFocusCallback myAutoFocusCallback = new AutoFocusCallback() {
		@Override
		public void onAutoFocus(boolean arg0, Camera arg1) {
			captureButton.setEnabled(true);
		}
	};

	/**
	 * Play default camera shutter sound
	 */
	ShutterCallback shutterCallback = new ShutterCallback() {
		public void onShutter() {
			Log.d("CAMERA", "onShutter activated");
		}
	};

	/**
	 * Display camera options - focus button and flash button
	 * @param flag True will turn on option button displays
	 */
	private void enableCameraOptions(Boolean flag){
		if(flag){
			focusButton.setVisibility(View.VISIBLE);
			focusButton.setEnabled(true);
		}
		else{
			focusButton.setVisibility(View.INVISIBLE);
			focusButton.setEnabled(false);
		}
	}
	
	
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	private int cameraID = 1;
	private Button focusButton;
	private Button captureButton;
	private Button rotateCamera;
	private Button savePhoto;
	private Button discardPhoto;
	private Camera mCamera;
	private CameraPreview mPreview;
	private FrameLayout preview;
	public static byte[] rawPhoto;
	
}

package com.putty.celebselfie_v2;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Camera preview creates a surface to display to user
 * @author Pustikins
 *
 */
public class CameraPreview extends SurfaceView implements
		SurfaceHolder.Callback {

	/**
	 * Constructor
	 * @param context
	 * @param camera
	 */
	public CameraPreview(Context context, Camera camera) {
		super(context);
		mCamera = camera;
		mHolder = getHolder();
		mHolder.addCallback(this);
		// deprecated setting, but required on Android versions prior to 3.0
		//we will be using 4.0 and higher - NEED TO RESOLVE THIS STATEMENT
		//mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	/**
	 * Set preview display and start preview on surface creation
	 */
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			mCamera.setPreviewDisplay(holder);
			mCamera.startPreview();
		} catch (IOException e) {
			Log.d("CAMERA", "Error setting camera preview: " + e.getMessage());
		}
	}

	/**
	 * Handle surface destruction by releasing camera resource
	 */
	public void surfaceDestroyed(SurfaceHolder holder) {
		if (mCamera != null) {
	        mCamera.release();
	    }
	}

	/**
	 * Handle changes to the surface (screen rotations)
	 */
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		if (mHolder.getSurface() == null) {
			// preview surface does not exist
			return;
		}

		// stop preview before making changes
		try {
			mCamera.stopPreview();
		} catch (Exception e) {
			// ignore: tried to stop a non-existent preview
		}

		// set preview size and make any resize, rotate or
		// reformatting changes here

		// start preview with new settings
		try {
			mCamera.setPreviewDisplay(mHolder);
			mCamera.startPreview();

		} catch (Exception e) {
			Log.d("CAMERA", "Error starting camera preview: " + e.getMessage());
		}
	}

	private SurfaceHolder mHolder;
	private Camera mCamera;
}
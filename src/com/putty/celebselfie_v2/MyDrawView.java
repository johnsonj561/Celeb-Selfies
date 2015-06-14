package com.putty.celebselfie_v2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

public class MyDrawView extends View {

	/**
	 * Constructor
	 * @param c Context
	 */
	public MyDrawView(Context c) {
		super(c);
		mPath = new Path();
		mBitmapPaint = new Paint(Paint.DITHER_FLAG);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setColor(0xFF000000);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(10);
	}

	/**
	 * adjusts bitmap size as needed
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
	}

	/**
	 * Traces finger path on draw
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
		canvas.drawPath(mPath, mPaint);
	}

	/**
	 * Handle new touch movement
	 * @param x coordinate
	 * @param y coordinate
	 */
	private void touch_start(float x, float y) {
		mPath.reset();
		mPath.moveTo(x, y);
		mX = x;
		mY = y;
	}

	/**
	 * Handle touch movements
	 * @param x coordinate
	 * @param y coordinate
	 */
	private void touch_move(float x, float y) {
		float dx = Math.abs(x - mX);
		float dy = Math.abs(y - mY);
		if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
			mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
			mX = x;
			mY = y;
		}
	}

	/**
	 * When touch is complete, commit the path drawn
	 */
	private void touch_up() {
		mPath.lineTo(mX, mY);
		// commit the path to our offscreen
		mCanvas.drawPath(mPath, mPaint);
		// kill this so we don't double draw
		mPath.reset();
	}
	
	/**
	 * Handle the various touch movements of user
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			touch_start(x, y);
			invalidate();
			break;
		case MotionEvent.ACTION_MOVE:
			touch_move(x, y);
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			touch_up();
			invalidate();
			break;
		}
		return true;
	}

	/**
	 * Clear signature pad of signature
	 */
	public void clear() {
		mBitmap.eraseColor(Color.TRANSPARENT);
		invalidate();
		System.gc();
	}

	/**
	 * Assign user's color selection to mPaint
	 * @param color Chosen by user in ColorDialog
	 */
	public void changePaintColor(int color) {
		mPaint.setColor(color);
	}
	
	/**
	 * Assign user's thickness selection tomPaint
	 * @param size Chosen by user in Thickness Dialog
	 */
	public void changeSigThickness(int size){
		mPaint.setStrokeWidth(size);
	}
	
	private Bitmap mBitmap;
	private Canvas mCanvas;
	private Path mPath;
	private Paint mBitmapPaint;
	private Paint mPaint;
	private float mX, mY;
	private static final float TOUCH_TOLERANCE = 2;
}

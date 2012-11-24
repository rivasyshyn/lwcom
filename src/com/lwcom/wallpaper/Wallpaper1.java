package com.lwcom.wallpaper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.SystemClock;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class Wallpaper1 extends WallpaperService {

	private final Handler mHandler = new Handler();

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public Engine onCreateEngine() {
		return new Wall1Engine();
	}
	
	private static interface Orientation {
		public final static int LANDSCAPE = 0x1;
		public final static int PORTRATE = 0x2;
	}

	class Wall1Engine extends Engine {

		private final Paint mPaint = new Paint();
		private float mOffset;
		private float mTouchX = -1;
		private float mTouchY = -1;
		private long mStartTime;
		private float mCenterX;
		private float mCenterY;

		private boolean mVisibility = false;
		private int mOrientation = Orientation.PORTRATE;

		Runnable mDrawall = new Runnable() {

			public void run() {
				drawall();
			}
		};

		public Wall1Engine() {
			final Paint paint = mPaint;
			paint.setColor(0xffffffff);
			paint.setAntiAlias(true);
			paint.setStrokeWidth(2);
			paint.setStrokeCap(Paint.Cap.ROUND);
			paint.setStyle(Paint.Style.STROKE);

			mStartTime = SystemClock.elapsedRealtime();

		}

		@Override
		public void onCreate(SurfaceHolder surfaceHolder) {
			super.onCreate(surfaceHolder);

			// By default we don't get touch events, so enable them.
			setTouchEventsEnabled(true);
		}

		@Override
		public void onDestroy() {
			super.onDestroy();
			mHandler.removeCallbacks(mDrawall);
		}

		@Override
		public void onVisibilityChanged(boolean visible) {
			mVisibility = visible;
			if (visible) {
				drawall();
			} else {
				mHandler.removeCallbacks(mDrawall);
			}
		}

		private int mWidth;
		private int mXWorckSpace;
		private int mYWorckSpace;

		@Override
		public void onSurfaceChanged(SurfaceHolder holder, int format,
				int width, int height) {
			super.onSurfaceChanged(holder, format, width, height);
			mCenterX = width / 2;
			mCenterY = height / 2;
			mWidth = width;
			if (mYWorckSpace == 0)
				mYWorckSpace = height;
			mOrientation = getOrientation(width, height);
			drawall();
		}

		private int getOrientation(int width, int height){
			if(height > width){
				return Orientation.PORTRATE;
			}else{
				return Orientation.LANDSCAPE;
			}
		}
		
		@Override
		public void onSurfaceCreated(SurfaceHolder holder) {
			super.onSurfaceCreated(holder);
			final Rect surface = holder.getSurfaceFrame();
			mOrientation = getOrientation(surface.width(), surface.height());
		}

		@Override
		public void onSurfaceDestroyed(SurfaceHolder holder) {
			super.onSurfaceDestroyed(holder);
			mVisibility = false;
			mHandler.removeCallbacks(mDrawall);
		}

		@Override
		public void onOffsetsChanged(float xOffset, float yOffset, float xStep,
				float yStep, int xPixels, int yPixels) {
			mOffset = mWidth * (1 - xOffset) + mX;

			if (mXWorckSpace == 0)
				mXWorckSpace = (int) (mWidth * 1 / xStep);
			mHandler.removeCallbacks(mDrawall);
			drawall();

		}

		/*
		 * Store the position of the touch event so we can use it for drawing
		 * later
		 */
		@Override
		public void onTouchEvent(MotionEvent event) {
			mX = event.getX();
			mY = event.getY();
			super.onTouchEvent(event);
		}

		private void drawall() {
			final long stime = System.currentTimeMillis();
			final SurfaceHolder holder = getSurfaceHolder();

			Canvas c = null;
			try {
				c = holder.lockCanvas();
				if (c != null) {
					// draw figures
					drawCircle(c);
				}
			} finally {
				if (c != null) {
					holder.unlockCanvasAndPost(c);
				}
			}
			
			Log.v("time", String.valueOf(System.currentTimeMillis() - stime));
			
			mHandler.removeCallbacks(mDrawall);
			if (mVisibility) {
				mHandler.postDelayed(mDrawall, 4000);
			}

		}

		boolean de = true;

		private float mX, mY;
		
		private void drawCircle(Canvas c) {
			c.save();
			if(mX != 0 && mY != 0){
				c.translate(mX, mY);
			}else{
				c.translate(mOffset, mCenterY);
			}
			c.drawColor(0xff000000);
			float r = 0;
			float d = (float) Math.sqrt(Math.pow((mXWorckSpace) / 2.0, 2)
					+ Math.pow((mYWorckSpace) / 2.0, 2));

			d -= 200.0;
			
			float cc = (float) (d / 765.0);

			int color = 0xff0000;
			int i = 0;
			int cr = 255, cg = 0, cb = 0;
			while (r < d) {
				if (i < 255) {
					cr -= 1;
					cg += 1;
				} else if (i >= 255 && i < 510) {
					cg -= 1;
					cb += 1;
				} else if (i >= 510) {
					cr += 1;
					cb -= 1;
				}
				i++;
				if (de)
					Log.v("color ",
							String.format("%d  -  %x", i,
									Color.argb(0xff, cr, cg, cb)));
				mPaint.setColor(Color.argb(0xff, cr, cg, cb));
				c.drawCircle(0, 0, r, mPaint);
				r += cc;
			}
			de = false;
			c.restore();
		}

	}

}

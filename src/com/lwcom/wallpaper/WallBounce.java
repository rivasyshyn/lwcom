package com.lwcom.wallpaper;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

public class WallBounce extends WallpaperService {

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
		// TODO Auto-generated method stub
		return new BounceEngine();
	}

	private class BounceEngine extends Engine {

		private final Paint mPaint = new Paint();
		private boolean mVisible;

		private final Runnable mDrawall = new Runnable() {

			public void run() {
				drawAll();
			}
		};

		public BounceEngine() {

			// initialize Paint object
			mPaint.setAntiAlias(true);
			mPaint.setStrokeCap(Cap.ROUND);
			mPaint.setColor(0xff000000);
			mPaint.setStrokeWidth(2);
			mPaint.setStyle(Style.STROKE);

		}

		@Override
		public void onCreate(SurfaceHolder holder) {
			super.onCreate(holder);
		}

		@Override
		public void onDestroy() {
			super.onDestroy();
		}

		@Override
		public void onDesiredSizeChanged(int width, int height) {
			super.onDesiredSizeChanged(width, height);
		}

		@Override
		public void onOffsetsChanged(float xOffset, float yOffset,
				float xOffsetStep, float yOffsetStep, int xPixelOffset,
				int yPixelOffset) {
			super.onOffsetsChanged(xOffset, yOffset, xOffsetStep, yOffsetStep,
					xPixelOffset, yPixelOffset);
		}

		@Override
		public void onSurfaceChanged(SurfaceHolder holder, int format,
				int width, int height) {
			super.onSurfaceChanged(holder, format, width, height);
		}

		@Override
		public void onSurfaceCreated(SurfaceHolder holder) {
			super.onSurfaceCreated(holder);
		}

		@Override
		public void onSurfaceDestroyed(SurfaceHolder holder) {
			super.onSurfaceDestroyed(holder);
		}

		@Override
		public void onVisibilityChanged(boolean visible) {
			mVisible = visible;
			if(visible){
				drawAll();
			}else{
				mHandler.removeCallbacks(mDrawall);
			}
		}

		private void drawAll() {
			SurfaceHolder holder = getSurfaceHolder();

			Canvas c = null;
			try {
				c = holder.lockCanvas();
				
			} finally {
				if (c != null) {
					holder.unlockCanvasAndPost(c);
				}
			}

			mHandler.removeCallbacks(mDrawall);
			if (mVisible) {
				mHandler.postDelayed(mDrawall, 1000 / 25);
			}
		}
	}

}

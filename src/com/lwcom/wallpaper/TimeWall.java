package com.lwcom.wallpaper;

import java.io.InputStream;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

import com.lwcom.R;
import com.lwcom.application.LwComApp;
import com.lwcom.model.ClockSettings;
import com.lwcom.model.SettingStorage;

public class TimeWall extends WallpaperService {

	private Handler mHandler = new Handler();
	private static Bitmap mBackground;
	private ClockSettings mSettings;
	private boolean mIsColor;
	private int mColor;
	private int mColorT;

	@Override
	public void onCreate() {
		super.onCreate();
		mSettings = (ClockSettings) SettingStorage.restore(LwComApp.SETTINGS,
				getApplicationContext());
		if (mSettings == null) {
			mSettings = new ClockSettings();
			mSettings.init();
			SettingStorage.store(mSettings, LwComApp.SETTINGS,
					getApplicationContext());
		}
		if (mSettings.getPath() == null) {
			mIsColor = true;
		} else {
			mIsColor = false;
			try {
				//Log.v("path", mSettings.getPath());
				InputStream imageStream = getContentResolver().openInputStream(
						Uri.parse(mSettings.getPath()));
				mBackground = BitmapFactory.decodeStream(imageStream);

				// mBackground = BitmapFactory.decodeStream(getAssets().open(
				// "backgrounds/back.jpg"));
			} catch (Exception e) {
			} finally {
				if (mBackground == null) {
					//Log.v("bg", "can\'t get img resource");
				}
			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public Engine onCreateEngine() {
		return new WallEngine();
	}

	private static interface Orientation {
		public static final int PORTRATE = 0x1;
		public static final int LANDSCAPE = 0x2;
	}

	class WallEngine extends Engine {

		private final Paint mPaint;

		private boolean mIsVisible = false;
		private int mOrientation = Orientation.PORTRATE;
		private Bitmap mBackground;
		private float mWidth;
		private float mHeight;
		private float mOffset;
		private float mOffsetStep;

		private Runnable mDraw = new Runnable() {

			public void run() {
				drawall();
			}
		};

		public WallEngine() {
			mPaint = new Paint();
			mPaint.setColor(0xffff0000);
			mPaint.setAntiAlias(true);
			mPaint.setStrokeWidth(2);
			mPaint.setStrokeCap(Paint.Cap.ROUND);
			mPaint.setStyle(Paint.Style.STROKE);
			// mPaint.setShadowLayer(2.0f, 2.0f, 2.0f, 0x55000000);
		}

		private int getOrientation(int width, int height) {
			final int orientation;
			if (height >= width) {
				orientation = Orientation.PORTRATE;
			} else {
				orientation = Orientation.LANDSCAPE;
			}
			prepareBackground(width, height);
			return orientation;
		}

		private void prepareBackground(int width, int height) {
			if (mIsColor) {
				return;
			}
			int dstWidth = 0, dstHeight = 0;
			float xScale = (float) TimeWall.mBackground.getWidth()
					/ (float) (width * 2);
			float yScale = (float) TimeWall.mBackground.getHeight()
					/ (float) height;
			//Log.v("scale", "x:" + xScale + " | y:" + yScale);
			if (xScale <= yScale) {
				dstWidth = (int) (TimeWall.mBackground.getWidth() / xScale);
				dstHeight = (int) (TimeWall.mBackground.getHeight() / xScale);
			} else {
				dstWidth = (int) (TimeWall.mBackground.getWidth() / yScale);
				dstHeight = (int) (TimeWall.mBackground.getHeight() / yScale);
			}
			try {
				mBackground = Bitmap.createScaledBitmap(TimeWall.mBackground,
						dstWidth, dstHeight, false);
			} catch (Exception e) {
			}
		}

		@Override
		public void onCreate(SurfaceHolder surfaceHolder) {
			super.onCreate(surfaceHolder);
		}

		@Override
		public void onDestroy() {
			super.onDestroy();
		}

		@Override
		public void onVisibilityChanged(boolean visible) {
			super.onVisibilityChanged(visible);
			mIsVisible = visible;
			if (visible) {
				mSettings = (ClockSettings) SettingStorage.restore(
						LwComApp.SETTINGS, getApplicationContext());
				if (mSettings.getPath() == null
						|| mSettings.getPath().equals("")) {
					mIsColor = true;
					mColor = Color.argb(0xff, mSettings.getRed(),
							mSettings.getGreen(), mSettings.getBlue());
				} else {
					mIsColor = false;
					mColor = 0xff000000;

					try {
						mBackground.recycle();
						//Log.v("path", mSettings.getPath());
						InputStream imageStream = getContentResolver()
								.openInputStream(Uri.parse(mSettings.getPath()));
						mBackground = BitmapFactory.decodeStream(imageStream);

						// mBackground =
						// BitmapFactory.decodeStream(getAssets().open(
						// "backgrounds/back.jpg"));
					} catch (Exception e) {
					} finally {
						if (mBackground == null) {
							//Log.v("bg", "can\'t get img resource");
						}
					}
				}
				mColorT = Color.argb(mSettings.getTr(), mSettings.getRedT(),
						mSettings.getGreenT(), mSettings.getBlueT());

				drawall();

			} else {
				mHandler.removeCallbacks(mDraw);
			}
		}

		@Override
		public void onSurfaceChanged(SurfaceHolder holder, int format,
				int width, int height) {
			super.onSurfaceChanged(holder, format, width, height);
			mWidth = width;
			mHeight = height;
			if (!mIsColor)
				mOrientation = getOrientation(width, height);

			drawall();
		}

		@Override
		public void onSurfaceCreated(SurfaceHolder holder) {
			super.onSurfaceCreated(holder);
			final Rect rect = holder.getSurfaceFrame();
			mIsVisible = true;
			drawall();
		}

		@Override
		public void onSurfaceDestroyed(SurfaceHolder holder) {
			super.onSurfaceDestroyed(holder);
			mIsVisible = false;
			mHandler.removeCallbacks(mDraw);
		}

		@Override
		public void onOffsetsChanged(float xOffset, float yOffset, float xStep,
				float yStep, int xPixels, int yPixels) {
			mOffset = xOffset;
			mOffsetStep = xStep;
			drawall();

		}

		private void drawall() {
			final long stime = System.currentTimeMillis();
			final SurfaceHolder holder = getSurfaceHolder();

			Canvas c = null;
			try {
				c = holder.lockCanvas();
				if (c != null) {
					// draw figures
					drawTime(c);
				}
			} finally {
				if (c != null) {
					holder.unlockCanvasAndPost(c);
				}
			}

			mHandler.removeCallbacks(mDraw);
			if (mIsVisible) {
				int n = 1000;
				if (mSettings.isMinutes() && !mSettings.isSeconds()) {
					n = 60 * 1000;
				}
				if (mSettings.isHours() && !mSettings.isMinutes()
						&& !mSettings.isSeconds()) {
					n = 60 * 60 * 1000;
				}
				int interval = (int) (n - System.currentTimeMillis() % n);
				mHandler.postDelayed(mDraw, interval);
			}

		}

		private void drawTime(Canvas c) {
			c.save();
			drawBackground(c);
			c.translate(0, (mHeight - 180) * mSettings.getPosition() / 100);
			Date d = new Date(System.currentTimeMillis());

			mPaint.setStyle(Style.FILL);
			mPaint.setColor(0xff000000);
			// c.drawRect(0, 0, mWidth, 500, mPaint);
			if (mSettings.isShadow())
				mPaint.setShadowLayer(1.0f, 0.5f, 1.0f, 0xAA000000);
			int cur, amount, str;
			if (mSettings.isSeconds()) {
				cur = d.getSeconds();
				amount = 59;
				str = 0;
				drawLine(c, cur, amount, str, getString(R.string.str_sec), 20,
						190);
			}
			if (mSettings.isMinutes()) {
				cur = d.getMinutes();
				amount = 59;
				str = 0;
				drawLine(c, cur, amount, str, getString(R.string.str_min), 40,
						175);
			}
			if (mSettings.isHours()) {
				cur = d.getHours();
				amount = 24;
				str = 1;
				drawLine(c, cur, amount, str, getString(R.string.str_hou), 90,
						50);
			}
			if (mSettings.isMonths()) {
				cur = d.getMonth();
				amount = 12;
				str = 1;
				drawLine(c, -1, amount, str,
						getResources().getStringArray(R.array.months)[cur], 30,
						20);
			}
			if (mSettings.isShadow())
				mPaint.clearShadowLayer();
			c.restore();
		}

		private void drawBackground(Canvas c) {

			mPaint.setColor(mColor);
			mPaint.setAlpha(0xff);
			mPaint.setStyle(Style.FILL);
			c.drawRect(0, 0, mWidth, mHeight, mPaint);
			if (!mIsColor) {
				if (mBackground == null) {
					return;
				}
				c.drawBitmap(mBackground,
						-((mBackground.getWidth() - mWidth) * mOffset), 0,
						mPaint);
			}
		}

		private void drawLine(Canvas c, int cn, int amount, int str,
				String lbl, int size, int offset) {
			c.translate(0, size);

			Rect bounds = new Rect();
			String st;
			if (cn >= 0) {
				st = String.format("%d", cn) + " " + lbl;
			} else {
				st = lbl;
			}
			if (mSettings.isFill()) {
				mPaint.setStyle(Style.FILL);
			} else {
				mPaint.setStyle(Style.STROKE);
			}
			mPaint.setColor(mColorT);
			mPaint.setTextSize(size);
			mPaint.setStrokeWidth(2);

			mPaint.getTextBounds(st, 0, st.length(), bounds);

			if (mSettings.isLines()) {
				c.drawLine(0, 0, mWidth, 0, mPaint);
			}

			c.drawText(st, ((mWidth - bounds.width()))
					* ((cn >= 0) ? mOffset : (1 - mOffset)), -5, mPaint);

		}
	}

}

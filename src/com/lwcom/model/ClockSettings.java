package com.lwcom.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ClockSettings implements Serializable {

	private boolean mSeconds;
	private boolean mMinutes;
	private boolean mHours;
	private boolean mMonths;
	private boolean mLines;
	private int mPosition;
	private int mRed, mGreen, mBlue;
	private int mTr, mRedT, mGreenT, mBlueT;
	private String mPath;
	private boolean mShadow;
	private boolean mFill;

	public void init() {
		setSeconds(true);
		setMinutes(true);
		setHours(true);
		setMonths(false);
		setLines(true);
		setPosition(10);
		setPath(null);
		setTr(225);
		setRedT(100);
		setGreenT(100);
		setBlueT(100);
	}

	public boolean isSeconds() {
		return mSeconds;
	}

	public void setSeconds(boolean mSeconds) {
		this.mSeconds = mSeconds;
	}

	public boolean isMinutes() {
		return mMinutes;
	}

	public void setMinutes(boolean mMinutes) {
		this.mMinutes = mMinutes;
	}

	public boolean isHours() {
		return mHours;
	}

	public void setHours(boolean mHours) {
		this.mHours = mHours;
	}

	public boolean isMonths() {
		return mMonths;
	}

	public void setMonths(boolean mMonths) {
		this.mMonths = mMonths;
	}

	public int getPosition() {
		return mPosition;
	}

	public void setPosition(int mPosition) {
		this.mPosition = mPosition;
	}

	public String getPath() {
		return mPath;
	}

	public void setPath(String mPath) {
		this.mPath = mPath;
	}

	public boolean isLines() {
		return mLines;
	}

	public void setLines(boolean mLines) {
		this.mLines = mLines;
	}

	public int getRed() {
		return mRed;
	}

	public void setRed(int mRed) {
		this.mRed = mRed;
	}

	public int getGreen() {
		return mGreen;
	}

	public void setGreen(int mGreen) {
		this.mGreen = mGreen;
	}

	public int getBlue() {
		return mBlue;
	}

	public void setBlue(int mBlue) {
		this.mBlue = mBlue;
	}

	public boolean isFill() {
		return mFill;
	}

	public void setFill(boolean mFill) {
		this.mFill = mFill;
	}

	public int getRedT() {
		return mRedT;
	}

	public void setRedT(int mRedT) {
		this.mRedT = mRedT;
	}

	public int getGreenT() {
		return mGreenT;
	}

	public void setGreenT(int mGreenT) {
		this.mGreenT = mGreenT;
	}

	public int getBlueT() {
		return mBlueT;
	}

	public void setBlueT(int mBlueT) {
		this.mBlueT = mBlueT;
	}

	public int getTr() {
		return mTr;
	}

	public void setTr(int mTr) {
		this.mTr = mTr;
	}

	public boolean isShadow() {
		return mShadow;
	}

	public void setShadow(boolean mShadow) {
		this.mShadow = mShadow;
	}

	public static class LineSettings implements Serializable {

		private static final int MAX_SIZE = 100;
		private int mSize = 20;

		public int getSize() {
			return mSize;
		}

		public void setSize(int mSize) {
			this.mSize = mSize;
		}

	}
}

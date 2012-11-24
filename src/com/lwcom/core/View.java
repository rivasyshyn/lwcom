package com.lwcom.core;

import android.graphics.Canvas;
import android.graphics.Rect;

public class View {

	private boolean mVisible;
	private boolean mEnable;
	private Rect mRect;
	
	public View(int left, int top, int width, int height){
		mRect = new Rect(left, top, left + width, top + height);
		mVisible = true;
		mEnable  = true;
	}
	
	public View(){
		new View(0, 0, 0, 0);
	}
	
	public void Draw(Canvas c){

	}
		
	
	
}

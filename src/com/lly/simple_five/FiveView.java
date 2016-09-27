package com.lly.simple_five;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class FiveView extends View {

	private int Width, Hight;
	private Paint mPaint;
	private int mBroad;
	private float mLineWidth;
	private static final int MAX_LINE = 10;
	private static final float roation = 3*1.0f/4;
	private Bitmap mBlackPiece;
	private Bitmap mWhilePiece;
	private ArrayList<Point> mBlackList ;
	private ArrayList<Point> mWhileList;
	private boolean isWhile=true;
	private boolean IsGameOver=false;
	private int FIVE_WIN =5;

	public FiveView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setColor(0x88000000);
		setBackgroundColor(0x44440000);
		mBlackList = new ArrayList<Point>();
		mWhileList = new ArrayList<Point>();
		

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		Width = MeasureSpec.getSize(widthMeasureSpec);
		Hight = MeasureSpec.getSize(heightMeasureSpec);
		int WidthMode = MeasureSpec.getMode(widthMeasureSpec);

		Width = Math.min(Width, Hight);
		int measuredWidth = MeasureSpec.makeMeasureSpec(Width, WidthMode);
		setMeasuredDimension(measuredWidth, measuredWidth);

	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mBroad = Width;
		mLineWidth = mBroad * 1.0f / MAX_LINE;
		mBlackPiece = BitmapFactory.decodeResource(getResources(),
				R.drawable.stone_b1);
		mWhilePiece = BitmapFactory.decodeResource(getResources(),
				R.drawable.stone_w2);
		int dstpoint = (int)(mLineWidth*roation);
		mBlackPiece = Bitmap.createScaledBitmap(mBlackPiece, dstpoint, dstpoint, false);
		mWhilePiece = Bitmap.createScaledBitmap(mWhilePiece, dstpoint, dstpoint, false);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(IsGameOver) return false;
		int x = (int) event.getX();
		int y = (int) event.getY();
		Point p = getPoint(x,y);
		
		if(event.getAction()==MotionEvent.ACTION_UP){
			if(mWhileList.contains(p)||mBlackList.contains(p)){
				return false;
			}
			if(isWhile){
				mWhileList.add(p);
			}else{
				mBlackList.add(p);	
			}
			invalidate();
			isWhile=!isWhile;
		}
		return true;
	}

	private Point getPoint(int x, int y) {
		
		return new Point((int)(x/mLineWidth),(int)(y/mLineWidth));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		DrawBroad(canvas);
		DrawPiece(canvas);
		checkGameOver();
	}
	private String INSTANCE = "instaNce";
	private String INSTANCE_GEMEOVER = "instance_gameover";
	private String INSTANCE_WHILEARRAY = "instance_whilearray";
	private String INSTANCE_BLACKARRAY = "instance_blackarray";
	
	@Override
	protected Parcelable onSaveInstanceState() {
		Bundle bundle = new Bundle();
		bundle.putParcelable(INSTANCE, super.onSaveInstanceState());
		bundle.putBoolean(INSTANCE_GEMEOVER, IsGameOver);
		bundle.putParcelableArrayList(INSTANCE_WHILEARRAY, mWhileList);
		bundle.putParcelableArrayList(INSTANCE_BLACKARRAY, mBlackList);
		
		return bundle;
	}
	
	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		if(state instanceof Bundle){
			Bundle bundle = (Bundle) state;
			IsGameOver = bundle.getBoolean(INSTANCE_GEMEOVER);
			mWhileList = bundle.getParcelableArrayList(INSTANCE_WHILEARRAY);
			mBlackList = bundle.getParcelableArrayList(INSTANCE_BLACKARRAY);
			super.onRestoreInstanceState(bundle.getBundle(INSTANCE));
			return;
		}
		super.onRestoreInstanceState(state);
	}

	private void checkGameOver() {
		boolean Whilewin = checkWhileFive(mWhileList);
		boolean Blackwin = checkWhileFive(mBlackList);
		if(Whilewin){
			IsGameOver=true;
			Toast.makeText(getContext(), "°×ÆìÊ¤Àû", Toast.LENGTH_SHORT).show();
		}
		if(Blackwin){
			IsGameOver=true;
			Toast.makeText(getContext(), "ºÚÆåÊ¤Àû", Toast.LENGTH_SHORT).show();
		}
	}


	private boolean checkWhileFive(List<Point> points) {
		for(Point p:points){
			int x = p.x;
			int y = p.y;
			
			 boolean Horizewin =checkHorizefive(x,y,points);
			 boolean verwin =checkverfive(x,y,points);
			 boolean leftwin =checkleftfive(x,y,points);
			 boolean reghitwin =checkreghitfive(x,y,points);
			 if(Horizewin||verwin||leftwin||reghitwin){
				 return true;
			 }
			
		}
		return false;
	}

	private boolean checkreghitfive(int x, int y, List<Point> points) {
		int count=1;
		for(int i=1;i<FIVE_WIN;i++){
			if(points.contains(new Point(x+i,y+i))){
				count++;
			}else{
				break;
			}
		}
		if(count ==FIVE_WIN) return true;
		for(int i=1;i<FIVE_WIN;i++){
			if(points.contains(new Point(x-i,y-i))){
				count++;
			}else{
				break;
			}
		}
		if(count ==FIVE_WIN) return true;
		return false;
	}

	private boolean checkleftfive(int x, int y, List<Point> points) {
		int count=1;
		for(int i=1;i<FIVE_WIN;i++){
			if(points.contains(new Point(x-i,y+i))){
				count++;
			}else{
				break;
			}
		}
		if(count ==FIVE_WIN) return true;
		for(int i=1;i<FIVE_WIN;i++){
			if(points.contains(new Point(x+i,y-i))){
				count++;
			}else{
				break;
			}
		}
		if(count ==FIVE_WIN) return true;
		return false;
	}

	private boolean checkverfive(int x, int y, List<Point> points) {
		int count=1;
		for(int i=1;i<FIVE_WIN;i++){
			if(points.contains(new Point(x,y+i))){
				count++;
			}else{
				break;
			}
		}
		if(count ==FIVE_WIN) return true;
		for(int i=1;i<FIVE_WIN;i++){
			if(points.contains(new Point(x,y-i))){
				count++;
			}else{
				break;
			}
		}
		if(count ==FIVE_WIN) return true;
		return false;
	}

	private boolean checkHorizefive(int x, int y, List<Point> points) {
		int count=1;
		for(int i=1;i<FIVE_WIN;i++){
			if(points.contains(new Point(x+i,y))){
				count++;
			}else{
				break;
			}
		}
		if(count ==FIVE_WIN) return true;
		for(int i=1;i<FIVE_WIN;i++){
			if(points.contains(new Point(x-i,y))){
				count++;
			}else{
				break;
			}
		}
		if(count ==FIVE_WIN) return true;
		return false;
		
	}

	private void DrawPiece(Canvas canvas) {
		for(int i=0,n=mWhileList.size();i<n;i++){
			Point WhilePiece = mWhileList.get(i);
			canvas.drawBitmap(mWhilePiece, (WhilePiece.x+(1-roation)/2)*mLineWidth,
					(WhilePiece.y+(1-roation)/2)*mLineWidth, mPaint);
			
		}
		for(int i=0,n=mBlackList.size();i<n;i++){
			Point BlackPiece = mBlackList.get(i);
			canvas.drawBitmap(mBlackPiece, (BlackPiece.x+(1-roation)/2)*mLineWidth,
					(BlackPiece.y+(1-roation)/2)*mLineWidth, null);
		}
		
		
	}

	private void DrawBroad(Canvas canvas) {
		for (int i = 0; i < MAX_LINE; i++) {
			float startX = mLineWidth / 2;
			float stopX = mBroad - (mLineWidth / 2);
			float startY = (float) ((0.5 + i) * mLineWidth);
			float stopY = (float) ((0.5 + i) * mLineWidth);
			canvas.drawLine(startX, startY, stopX, stopY, mPaint);

			canvas.drawLine(startY, startX, stopY, stopX, mPaint);
		}
	}

	public void setrestart() {
		IsGameOver=false;
		mWhileList.clear();
		mBlackList.clear();
		invalidate();
	}
}

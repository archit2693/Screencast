package com.example.gemswin.screancasttest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class CanvasView extends View {

	public int width;
	public int height;
	private Bitmap mBitmap;
	private Canvas mCanvas;
	private Path mPath;
	private Path mPath1;
	Context mcontext;
	private Paint mPaint;
	private Paint mPaint1;
	public float stroke=5f;
	private float mX, mY;
	private static final float TOLERANCE = 5;
	public int colour;

	ArrayList<Pair<Path, Paint>> paths = new ArrayList<Pair<Path, Paint>>();
	ArrayList<Pair<Path, Paint>> undopaths = new ArrayList<Pair<Path, Paint>>();
	ArrayList<Pair<Path, Paint>> clear;
	boolean check=false;
	boolean isEraser=false;

	public CanvasView(Context context) {
		super(context);
	}


	public CanvasView(Context context, AttributeSet attrs) {
		super(context, attrs);

		mcontext = context;
		mPath = new Path();
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.BLACK);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeWidth(stroke);

	}

	public CanvasView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

	}

	public CanvasView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);

	}


	// override onSizeChanged
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		// your Canvas will draw onto the defined Bitmap
		mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
	}

	// override onDraw
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// draw the mPath with the mPaint on the canvas when onDraw
		mPaint.setColor(colour);
		mPaint.setStrokeWidth(stroke);
		canvas.drawPath(mPath, mPaint);
		Draw(canvas);

	}

	private void Draw(Canvas canvas) {
		for (Pair<Path, Paint> p : paths)
		{
			canvas.drawPath(p.first, p.second);
		}
		Log.d("paths:", String.valueOf(paths.size()));

	}

	// when ACTION_DOWN start touch according to the x,y values
	private void startTouch(float x, float y) {
		mPath.moveTo(x, y);
		mX = x;
		mY = y;
	}

	// when ACTION_MOVE move touch according to the x,y values
	private void moveTouch(float x, float y) {
		float dx = Math.abs(x - mX);
		float dy = Math.abs(y - mY);
		if (dx >= TOLERANCE || dy >= TOLERANCE) {
			mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
			mX = x;
			mY = y;
		}
	}

	public void clearCanvas() {
		clear = new ArrayList<Pair<Path, Paint>>(paths);
		paths.clear();
		check=true;
		invalidate();
	}

	// when ACTION_UP stop touch
	private void upTouch() {
		mPath.lineTo(mX, mY);
		Paint newPaint = new Paint(mPaint); // Clones the mPaint object
		paths.add(new Pair<Path, Paint>(mPath, newPaint));
		mPath=new Path();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();

		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				startTouch(x, y);
				invalidate();
				break;
			case MotionEvent.ACTION_MOVE:
				moveTouch(x, y);
				invalidate();
				break;
			case MotionEvent.ACTION_UP:
				upTouch();
				invalidate();
				break;
		}
		return true;
	}



	public void onClickEraser()
	{
		colour=Color.WHITE;
		stroke=77f;

	}

	public  void onClickUndo ()
	{

		if(paths.size()>0)
		{
			undopaths.add(paths.remove(paths.size()-1));
			invalidate();
		}

	}

	public  void onClickRedo ()
	{
		if (check){
			paths=new ArrayList<Pair<Path, Paint>>(clear);
			clear.clear();
			check=false;
			invalidate();
		}

		else if (undopaths.size()>0)
		{
			paths.add(undopaths.remove(undopaths.size()-1));
			invalidate();
		}
}
}
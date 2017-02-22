package com.example.gemswin.screancasttest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.gemswin.screancasttest.ScreenCastLib.DecoderAsyncTask;
import com.example.gemswin.screancasttest.ScreenCastLib.MediaCodecFactory;
import com.example.gemswin.screancasttest.ScreenCastLib.VideoChunk;

import java.io.IOException;

public class CanvasMain extends Activity implements AdapterView.OnItemSelectedListener ,GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener, EncoderAsyncTask.MediaCodecListener, TextureView.SurfaceTextureListener, SenderAsyncTask.ReceiverListener {

	private CanvasView customCanvas;
	private Spinner spinner;
	private String colour[]={"Black","Red","Blue","Green"};
	private Button eraser,undo,redo;

	private static final short GET_MEDIA_PROJECTION_CODE = 986;
	private MediaCodecFactory mMediaCodecFactory;
	private EncoderAsyncTask mEncoderAsyncTask;
	private DecoderAsyncTask mDecoderAsyncTask;
	private SenderAsyncTask mSenderAsyncTask;
	private GestureDetectorCompat detector;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.canvas_new);

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		final int height = metrics.heightPixels ;
		final int width = metrics.widthPixels ;
		mMediaCodecFactory = new MediaCodecFactory(width, height);
		detector = new GestureDetectorCompat(this, this);
		detector.setOnDoubleTapListener(this);

		customCanvas = (CanvasView) findViewById(R.id.signature_canvas);
		spinner=(Spinner)findViewById(R.id.colour);
		eraser=(Button)findViewById(R.id.eraser);
		undo=(Button)findViewById(R.id.undo);
		redo=(Button)findViewById(R.id.redo);
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,colour);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);
		eraser.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				customCanvas.onClickEraser();
			}
		});


		undo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				customCanvas.onClickUndo();
			}
		});

		redo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				customCanvas.onClickRedo();
			}
		});

		@SuppressWarnings("ResourceType") MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
		startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), GET_MEDIA_PROJECTION_CODE);
	}


	public void clearCanvas(View v) {
		customCanvas.clearCanvas();
	}

	@Override
	public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

		switch (i) {
			case 0:
				// Whatever you want to happen when the first item gets selected
				customCanvas.colour= Color.BLACK;
				customCanvas.stroke= 5f;

//				customCanvas.init(Color.BLACK);

				break;
			case 1:
				// Whatever you want to happen when the second item gets selected
				customCanvas.colour= Color.RED;
				customCanvas.stroke= 5f;

//				customCanvas.init(Color.RED);
				break;
			case 2:
				// Whatever you want to happen when the Third item gets selected
				customCanvas.colour= Color.BLUE;
				customCanvas.stroke= 5f;
//				customCanvas.init(Color.BLUE);
				break;
			case 3:
				// Whatever you want to happen when the fourth item gets selected
				customCanvas.colour= Color.GREEN;
				customCanvas.stroke= 5f;
//				customCanvas.init(Color.GREEN);
				break;

		}


	}

	@Override
	public void onNothingSelected(AdapterView<?> adapterView) {




	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
		return false;
	}

	@Override
	public boolean onDoubleTap(MotionEvent motionEvent) {
		return false;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent motionEvent) {
		return false;
	}

	@Override
	public boolean onDown(MotionEvent motionEvent) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent motionEvent) {

	}

	@Override
	public boolean onSingleTapUp(MotionEvent motionEvent) {
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent motionEvent) {

	}

	@Override
	public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
		return false;
	}

	@Override
	public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {

	}

	@Override
	public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

	}

	@Override
	public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
		return false;
	}

	@Override
	public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

	}

	@Override
	public void onData(VideoChunk chunk) {
		if (mSenderAsyncTask != null) {
			mSenderAsyncTask.addChunk(chunk);
		}

	}

	@Override
	public void onEnd() {

	}

	@Override
	public void onClientDisconnected() {

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK && requestCode == GET_MEDIA_PROJECTION_CODE) {
			try {
				@SuppressWarnings("ResourceType") MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
				final MediaProjection mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data);
				mEncoderAsyncTask = new EncoderAsyncTask(this, mediaProjection, mMediaCodecFactory);
				mEncoderAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				mSenderAsyncTask = new SenderAsyncTask(getApplicationContext(), this);
				mSenderAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mEncoderAsyncTask != null) {
			mEncoderAsyncTask.cancel(true);
			mEncoderAsyncTask = null;
		}
		if (mDecoderAsyncTask != null) {
			mDecoderAsyncTask.cancel(true);
			mDecoderAsyncTask = null;
		}
		if (mSenderAsyncTask != null) {
			mSenderAsyncTask.cancel(true);
			mSenderAsyncTask = null;
		}
	}
}
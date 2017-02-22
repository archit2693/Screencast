package com.example.gemswin.screancasttest;

import android.content.Context;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.TextureView;

import com.example.gemswin.screancasttest.ScreenCastLib.DecoderAsyncTask;
import com.example.gemswin.screancasttest.ScreenCastLib.MediaCodecFactory;
import com.example.gemswin.screancasttest.ScreenCastLib.VideoChunk;

import java.io.IOException;

/**
 * Created by this pc on 20-02-17.
 */



public class TunnelPhoneActivity extends AppCompatActivity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener, EncoderAsyncTask.MediaCodecListener, TextureView.SurfaceTextureListener, SenderAsyncTask.ReceiverListener {

    private static final short GET_MEDIA_PROJECTION_CODE = 986;
    private MediaCodecFactory mMediaCodecFactory;
    private EncoderAsyncTask mEncoderAsyncTask;
    private DecoderAsyncTask mDecoderAsyncTask;
    private SenderAsyncTask mSenderAsyncTask;
    private GestureDetectorCompat detector;


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        final int height = metrics.heightPixels ;
        final int width = metrics.widthPixels ;
        mMediaCodecFactory = new MediaCodecFactory(width, height);
        detector = new GestureDetectorCompat(this, this);
        detector.setOnDoubleTapListener(this);
        Log.d("Aagyaaaa on pause m","aaagyaaa");
        @SuppressWarnings("ResourceType") MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), GET_MEDIA_PROJECTION_CODE);



    }

    @Override
    protected void onStop() {
        super.onStop();

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
    public void onData(VideoChunk info) {
        if (mSenderAsyncTask != null) {
            mSenderAsyncTask.addChunk(info);
        }

    }

    @Override
    public void onEnd() {

    }

    @Override
    public void onClientDisconnected() {

    }

    @Override
    protected void onDestroy() {

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
        super.onDestroy();
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
                mSenderAsyncTask = new SenderAsyncTask(getApplicationContext(),this);
                mSenderAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                this.moveTaskToBack(true);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        Log.d("Aagyaaaa on result m","aaagyaaa");

    }

}


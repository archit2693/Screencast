
package com.example.gemswin.screencastrecevertest;

import android.app.Activity;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Surface;
import android.view.TextureView;
import android.view.Window;
import android.view.WindowManager;

import com.example.gemswin.screancasttest.PrefManager;
import com.example.gemswin.screancasttest.R;
import com.example.gemswin.screencastrecevertest.ScreenCastLib.DecoderAsyncTask;
import com.example.gemswin.screencastrecevertest.ScreenCastLib.MediaCodecFactory;
import com.example.gemswin.screencastrecevertest.ScreenCastLib.OnVideoSizeChangedListener;
import com.example.gemswin.screencastrecevertest.ScreenCastLib.VideoChunk;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

import static android.os.AsyncTask.THREAD_POOL_EXECUTOR;


public class MainActivity_Reciever extends Activity implements ReceiverAsyncTask.ReceiverListener, TextureView.SurfaceTextureListener, OnVideoSizeChangedListener {

    private static final String LOG_TAG = MainActivity_Reciever.class.getSimpleName();
    private ReceiverAsyncTask mTask;
    private final MediaCodecFactory mMediaCodecFactory = new MediaCodecFactory(0, 0);
    private DecoderAsyncTask mDecoderAsyncTask;
    TextureView mTextureView;
    private PrefManager pref;
    public Context context;
    public Socket as;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_reciever);
        mTextureView = (TextureView) findViewById(R.id.textureView);
        mTextureView.setSurfaceTextureListener(this);
        mTextureView.requestLayout();
        mTextureView.invalidate();
        mTextureView.setOpaque(false);
        pref = new PrefManager(getApplicationContext());
        context=this;
        mTask = new ReceiverAsyncTask(this,context);
        mTask.executeOnExecutor(THREAD_POOL_EXECUTOR);

//        Intent intent1 = new Intent(this, MyBroadcastReceiver.class);
//        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(
//                getApplicationContext(), 234324243, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
//        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
//        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (10 * 1000), pendingIntent);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("on destroy ",String.valueOf(mTask.isCancelled()));
//        try {
//            Log.d("Socket Detail ", String.valueOf(as.isConnected()));
//            as.close();
//            Log.d("Socket Detail ", String.valueOf(as.isConnected()));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        if( as!=null) {
            Log.d("Socket Detail ", String.valueOf(as.isConnected()));
            try {
                as.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("Socket Detail ", String.valueOf(as.isConnected()));
        }


        mTask.cancel(true);
        Log.d("on destroy ",String.valueOf(mTask.isCancelled()));
    }


    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        Log.i(LOG_TAG, "onSurfaceTextureAvailable (" + width + "/" + height + ")");
        try {
            final Surface surface = new Surface(surfaceTexture);
            mDecoderAsyncTask = new DecoderAsyncTask(mMediaCodecFactory, surface, this);
            mDecoderAsyncTask.executeOnExecutor(THREAD_POOL_EXECUTOR);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }  //skip

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    } //skip

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    }

    @Override
    public void onVideoChunk(VideoChunk chunk) {

        if (mDecoderAsyncTask != null) {
            mDecoderAsyncTask.addChunk(chunk);
        }
    }//skip

    @Override
    public void onConnectionLost() {
        if (!isDestroyed()) {
            mTask = new ReceiverAsyncTask(this,context);
            mTask.executeOnExecutor(THREAD_POOL_EXECUTOR);
        }else
        {

        }

    }//skip

    @Override
    public void onSocketClosed(Socket accept){
            as= accept;

    }

    @Override
    public void onVideoSizeChanged(int videoWidth, int videoHeight) {
        // Get the SurfaceView layout parameters
        Log.i(LOG_TAG, "onVideoSizeChange (" + videoWidth + "/" + videoHeight + ")");
        android.view.ViewGroup.LayoutParams lp = mTextureView.getLayoutParams();
        float videoProportion = (float) videoWidth / (float) videoHeight;
        // Get the width of the screen

        int screenWidth = mTextureView.getWidth();
        int screenHeight = mTextureView.getHeight();
        float screenProportion = (float) screenWidth / (float) screenHeight;
        if (videoProportion > screenProportion) {
            //video is wider than our screen
            lp.width = screenWidth;
            lp.height = (int) ((float) screenWidth / videoProportion);
        } else {
            lp.width = (int) (videoProportion * (float) screenHeight);
            lp.height = screenHeight;
        }
        // Commit the layout parameters
        mTextureView.setLayoutParams(lp);

    } // end of videosize  //skip

    @Override

    public void onVideoEnded(List<Pair<Long, Integer>> chunksTimeSeries) {

    }

}


package com.example.gemswin.screencastrecevertest;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.gemswin.screancasttest.PrefManager;
import com.example.gemswin.screencastrecevertest.ScreenCastLib.VideoChunk;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ReceiverAsyncTask extends AsyncTask<Void, VideoChunk, Void> {

    private static final String LOG_TAG = ReceiverAsyncTask.class.getSimpleName();
    public PrefManager pref;
    public Context mContext;
    String serverIp;
    String portNumber;
    Socket accept;
    interface ReceiverListener {
        void onVideoChunk(VideoChunk chunk);
        void onConnectionLost();
        void onSocketClosed(Socket accept);
    }

    private final ReceiverListener mListener;

    ReceiverAsyncTask(ReceiverListener listener, Context context) {
        this.mListener = listener;
        mContext = context;
        pref = new PrefManager(mContext);
        serverIp = pref.getIPClient();
        portNumber = pref.getPortClient();

    }

    @Override
    protected Void doInBackground(Void... params) {
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

        try {
              Log.i("Client ...", "waiting for connection");
              accept=new Socket(serverIp,Integer.parseInt(portNumber));
              Log.i("Client ...", "Socket accepted");
              mListener.onSocketClosed(accept);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        final DataInputStream inputStream;
        try {
            inputStream = new DataInputStream(accept.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        while (!isCancelled() && accept.isConnected()) {
            try {
                int length = inputStream.readInt();
                int flags = inputStream.readInt();
                long timeUs = inputStream.readLong();
                byte[] data = new byte[length];
                inputStream.readFully(data, 0, length);
                VideoChunk chunk = new VideoChunk(data, flags, timeUs);
                publishProgress(chunk);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void Void) {
        super.onPostExecute(Void);
       // mListener.onConnectionLost();
        Log.d("lelooo ",String.valueOf(accept.isConnected()));
       // mListener.onSocketClosed(accept);
//        try {
//            a.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected void onProgressUpdate(VideoChunk... values) {
        super.onProgressUpdate(values);
        mListener.onVideoChunk(values[0]);
    }
}

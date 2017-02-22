
package com.example.gemswin.screancasttest;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.gemswin.screancasttest.ScreenCastLib.VideoChunk;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Modified by Iron_Man on 18/02/17.
 */

public class SenderAsyncTask extends AsyncTask<Void, Void, Void>  {

    private static final String LOG_TAG = SenderAsyncTask.class.getSimpleName();
    Context context;
    String serverIp;
    List<String> ipArray;
    private ServerSocket mServerSocket;
    public Socket accept=null;
    LinkedBlockingDeque<VideoChunk> mVideoChunks = new LinkedBlockingDeque<VideoChunk>();
    PrefManager pref;
    DataOutputStream outputStream = null;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        Log.d("SenderAsync PreExecute", "inside");
    }




    interface ReceiverListener {
       void onClientDisconnected();
    }

    private final ReceiverListener mListener;


    public void addChunk(VideoChunk chunk) {

        synchronized (mVideoChunks) {
            mVideoChunks.addFirst(chunk);
            if (mVideoChunks.size() > 2) {
                Log.i(LOG_TAG, "Chunks: " + mVideoChunks.size());

            }
        }
    }

    public void closeSocket(Socket socket) {
        try {
            Log.d("Socket Detail ", String.valueOf(socket.isConnected()));
            outputStream.flush();
            outputStream.close();
            socket.close();
            Log.d("Socket Detail ", String.valueOf(socket.isConnected()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }






    SenderAsyncTask(Context context, ReceiverListener mListener) {
        this.context = context;
        pref = new PrefManager(context);
        this.mListener = mListener;
    }



    @Override
    protected Void doInBackground(Void... params) {

        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

          // NEWW COde

//        try {
//
//            int port = Integer.parseInt(pref.getPort());
//            mServerSocket = new ServerSocket(port);
//            Log.i(LOG_TAG, "waiting for connection");
//            accept = mServerSocket.accept();
//            Log.i("Client ",accept.getInetAddress().toString()+" "+String.valueOf(accept.isConnected()));
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
        accept = SocketHandler.getSocket();

        try {
            outputStream=new DataOutputStream(accept.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (!isCancelled()) {
            if(accept.isConnected()){
            VideoChunk chunk = null;
            try {
                Log.d(LOG_TAG, "waiting for data to send");
                chunk = mVideoChunks.takeLast();
                Log.d(LOG_TAG,"Client status  "+accept.isConnected()+accept.isClosed());
                int length = chunk.getData().length;
                outputStream.writeInt(length);
                outputStream.writeInt(chunk.getFlags());
                outputStream.writeLong(chunk.getTimeUs());
                outputStream.write(chunk.getData());
                outputStream.flush();


            } catch (InterruptedException e) {

                try {
                    outputStream.flush();
                    outputStream.close();
                    accept.close();
                    Log.d("Socket in exception1", String.valueOf(accept.isConnected()));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                e.printStackTrace();
                Log.d("Socket in exception1", String.valueOf(accept.isConnected()));
                break;

            } catch (IOException e) {
                closeSocket(accept);
                e.printStackTrace();
                Log.d("Socket in exception2", String.valueOf(accept.isConnected()));
                break;
            }

        }
        else
            break;
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Log.d("post","working");
        mListener.onClientDisconnected();
    }
}

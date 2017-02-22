package com.example.gemswin.screancasttest;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import pl.bclogic.pulsator4droid.library.PulsatorLayout;


/**
 * Created by Iron_Man on 14/02/17.
 */

public class RadarActivity extends AppCompatActivity {

    public static final String LOG_TAG="RadarActivity Logs: ";
    public Socket connect;
    public ServerSocket mServerSocket;
    public PrefManager pref;
    public Context context;

     @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waitradar);
        context = getApplicationContext();
        pref= new PrefManager(getApplicationContext());
        PulsatorLayout pulsator = (PulsatorLayout) findViewById(R.id.pulsator);
        pulsator.start();
        new ConnectAsyncTask().execute();
    }




    class ConnectAsyncTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {

            try {

                int port = Integer.parseInt(pref.getPort());
                mServerSocket = new ServerSocket(port);
                Log.i(LOG_TAG, "waiting for connection");
                connect = mServerSocket.accept();
                Log.i("Client ",connect.getInetAddress().toString()+" "+String.valueOf(connect.isConnected()));

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return null;
        }



        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            SocketHandler.setSocket(connect);
            pref.setNotice(0);
            Intent i=new Intent(RadarActivity.this,FileTypeActivity.class);
            startActivity(i);
            finish();

        }

    }

}

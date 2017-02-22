package com.example.gemswin.screencastrecevertest;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gemswin.screancasttest.PrefManager;
import com.example.gemswin.screancasttest.R;

import java.net.InetAddress;
import java.net.UnknownHostException;


@SuppressWarnings("deprecation")
public class LoginActivity extends Activity {


    Button connect;
    String port;
    EditText portNumber;
    Context mContext=this;
    String serverIp;

    private PrefManager pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_reciever);
        pref = new PrefManager(getApplicationContext());

        portNumber = (EditText) findViewById(R.id.portNumber);

        try {
           serverIp = getServerIp();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        connect = (Button) findViewById(R.id.connect);
        connect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                port = portNumber.getText().toString();
                if (!port.equals("")) {
                    pref.setPortClient(port);
                    pref.setIPClient(serverIp);
                    Intent i=new Intent(LoginActivity.this,MainActivity_Reciever.class);
                    startActivity(i);
                    finish();
                } else
                    Toast.makeText(LoginActivity.this, "Please enter all the fields.", Toast.LENGTH_SHORT).show();

            }
        });


    }

    public String getServerIp() throws UnknownHostException {
        DhcpInfo d;
        WifiManager wifii;

        wifii = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        d = wifii.getDhcpInfo();

        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);

        String ipmaster = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        InetAddress host;
        host = InetAddress.getByName(intToIp(d.dns1));
        Log.d("sr ip,ns1,ipmaster",intToIp(d.serverAddress)+" "+host.toString()+" "+ipmaster);
        return intToIp(d.serverAddress);

    }

    public String intToIp(int i) {
        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                ((i >> 24) & 0xFF);
    }
}

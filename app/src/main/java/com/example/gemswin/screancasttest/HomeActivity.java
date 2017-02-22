package com.example.gemswin.screancasttest;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


@SuppressWarnings("deprecation")
public class HomeActivity extends Activity {

    Button update;
    String portupdate;
    PrefManager pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        pref = new PrefManager(getApplicationContext());
        update = (Button) findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                portupdate = ((EditText)findViewById(R.id.portUpdate)).getText().toString();


                if (!portupdate.equals("")) {
                    pref.setPort(portupdate);
                    Intent i = new Intent(HomeActivity.this, RadarActivity.class);
                    startActivity(i);
                    finish();
                } else
                    Toast.makeText(HomeActivity.this, "Please enter valid port number", Toast.LENGTH_SHORT).show();
            }
        });
    }

}

package com.example.gemswin.screancasttest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Iron_Man on 12/02/17.
 */

public class FirstActivity extends AppCompatActivity {

    Button master;
    Button client;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firstscreen);
        master = (Button)findViewById(R.id.master);
        master.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FirstActivity.this, HomeActivity.class);
                startActivity(i);
            }
        });

        client = (Button)findViewById(R.id.client);
        client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FirstActivity.this, com.example.gemswin.screencastrecevertest.LoginActivity.class);
                startActivity(i);
            }
        });


    }
}

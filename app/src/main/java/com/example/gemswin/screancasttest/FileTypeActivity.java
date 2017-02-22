package com.example.gemswin.screancasttest;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.Toast;

/**
 * Created by this pc on 14-02-17.
 */

public class FileTypeActivity extends AppCompatActivity{

    CustomGridAdapter customGridAdapter;
    GridView grid;
    String name[]={"PDFs","DOCs","Excel","canvas","Tunnel Phone"};
    int Imageid[]={R.drawable.pdfs,R.drawable.doc,R.drawable.xlss,R.drawable.canvass,R.drawable.phone};
    String extensions;
    int imaged;
    PrefManager pref;
    private static final int REQUEST_WRITE_STORAGE = 112;
    FrameLayout frameLayout;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref=new PrefManager(getApplicationContext());
        setContentView(R.layout.activity_filetype);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        int actionBarHeight = getSupportActionBar().getHeight();
        frameLayout=(FrameLayout)findViewById(R.id.notification);
        notification(actionBarHeight);
        boolean hasPermission = (ContextCompat.checkSelfPermission(FileTypeActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        boolean hasPermission1 = (ContextCompat.checkSelfPermission(FileTypeActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(FileTypeActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        }
        if (!hasPermission1) {
            ActivityCompat.requestPermissions(FileTypeActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        }

        grid=(GridView)findViewById(R.id.fieldGrid);
        customGridAdapter=new CustomGridAdapter(this,name,Imageid);
        grid.setAdapter(customGridAdapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(FileTypeActivity.this,FileBrowser.class);
                extensions=name[i];
                imaged=Imageid[i];
                switch(extensions)
                {
                    case "PDFs":pref.setExtension(".pdf");
                                pref.setImage(imaged);
                                startActivity(intent);
                                break;
                    case "DOCs":pref.setExtension(".docx");
                                pref.setImage(imaged);
                                startActivity(intent);
                                break;
                    case "Excel":pref.setExtension(".xls");
                                 pref.setImage(imaged);
                                 startActivity(intent);
                                 break;
                    case "Tunnel Phone":pref.setTunnel(1);
                                        Intent intent3=new Intent(FileTypeActivity.this,TunnelPhoneActivity.class);
                                        startActivity(intent3);
                                        break;
                    case "canvas":Intent intent2=new Intent(FileTypeActivity.this,CanvasMain.class);
                                  startActivity(intent2);

                }


                finish();
            }
        });


    }
//    ObjectAnimator anim;
//    class notification extends AsyncTask<Void,Void,Void>
//
//
//    {
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            return null;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            if(pref.getNotice()==0)
//            {
//            frameLayout.setVisibility(View.VISIBLE);
//            anim=ObjectAnimator.ofInt(frameLayout,"notify",0,100);
//            anim.setInterpolator(new DecelerateInterpolator());
//            anim.setDuration(500);
//            anim.start();
//            }
//
//        }
//
//    }

    Toast toast;
    public void notification(int height)
    {

        if(pref.getNotice()==0)
        {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.toast,
                    (ViewGroup) findViewById(R.id.toast_layout_root));
            toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.FILL_HORIZONTAL|Gravity.TOP, 0,150);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //toast.cancel();
    }


}

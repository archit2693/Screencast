package com.example.gemswin.screancasttest;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.gemswin.screancasttest.ScreenCastLib.DecoderAsyncTask;
import com.example.gemswin.screancasttest.ScreenCastLib.MediaCodecFactory;
import com.example.gemswin.screancasttest.ScreenCastLib.VideoChunk;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileBrowser extends Activity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener, EncoderAsyncTask.MediaCodecListener, TextureView.SurfaceTextureListener, SenderAsyncTask.ReceiverListener {

    private static final String ARG_COLOR = "color";
    private static final short GET_MEDIA_PROJECTION_CODE = 986;
    private MediaCodecFactory mMediaCodecFactory;
    private EncoderAsyncTask mEncoderAsyncTask;
    private DecoderAsyncTask mDecoderAsyncTask;
    private SenderAsyncTask mSenderAsyncTask;
    public PrefManager pref;
    private GestureDetectorCompat detector;
    public int c=0;
    private String path;

    // TODO: Rename and change types of parameters
    GridView grid;
    CustomGrid adapter;
    File path1;
    ProgressBar progressBar;
    private ObjectAnimator progressAnimator;
    private static final int REQUEST_WRITE_STORAGE = 112;
    static ArrayList<String> file_paths = new ArrayList<String>();
    static ArrayList<String> file_names = new ArrayList<String>();
    static ArrayList<Integer> file_images = new ArrayList<Integer>();
    final Context mContext = this;
    ObjectAnimator animation;
    DatabaseHandler db;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_browser);
        pref=new PrefManager(getApplicationContext());
        pref.setNotice(1);
        // NEWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        final int height = metrics.heightPixels ;
        final int width = metrics.widthPixels ;
        mMediaCodecFactory = new MediaCodecFactory(width, height);
        detector = new GestureDetectorCompat(this, this);
        detector.setOnDoubleTapListener(this);

        @SuppressWarnings("ResourceType") MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), GET_MEDIA_PROJECTION_CODE);

        boolean hasPermission = (ContextCompat.checkSelfPermission(FileBrowser.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        boolean hasPermission1 = (ContextCompat.checkSelfPermission(FileBrowser.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(FileBrowser.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        }
        if (!hasPermission1) {
            ActivityCompat.requestPermissions(FileBrowser.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);

        }

            file_paths.clear();
            file_names.clear();
            file_images.clear();

            progressBar = (ProgressBar) findViewById(R.id.progressBar);
            db=new DatabaseHandler(this);
            path1 = new File(Environment.getExternalStorageDirectory() + "");
           // searchFolderRecursive1(path1);

            new LoaderAsynktask().execute(path1);

        }

    public void makeGrid()
    {
        int image=pref.getImage();
        for (int i = 0; i < file_names.size(); i++) {
            file_images.add(image);
        }
        adapter = new CustomGrid(FileBrowser.this, file_names,file_images);
        grid = (GridView) findViewById(R.id.gridfile);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                path = file_paths.get(+position);

                File myFile = new File(path);
                try {
                    FileOpen.openFile(mContext, myFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void searchFolderRecursive1(File folder) {

        pref.setFlag(1);
        if (folder != null) {
            if (folder.listFiles() != null) {
                for (File file : folder.listFiles()) {
                    if (file.isFile()) {
                        //files
                        String extension=pref.getExtension();
                        if(file.getName().contains(".pdf") || file.getName().contains(".docx") || file.getName().contains(".doc") || file.getName().contains(".xls")) {
                            Log.d("File ka naaam",file.getName());
                            String a[]=file.getName().split("\\.");
                            String e = a[a.length-1];
                            Log.d("File ka naaam",e);
                           db.addFile(new FileDatabase(e, file.getName(), file.getPath()));
                        }
                        if (file.getName().contains(extension)) {
                            file_names.add(file.getName());
                            c++;
                            Log.e("oooo", "c : " + String.valueOf(c));
                            file_paths.add(file.getPath());
                            Log.e("pdf_paths", "" + file_paths);
                        }
                    } else {
                        searchFolderRecursive1(file);
                    }
                }
            }
        }
    }


    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {

        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

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

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    ProgressBarAnimation mpProgressBarAnimation;
    ObjectAnimator anim;

    @Override
    public void onClientDisconnected() {
        Toast.makeText(getApplicationContext(),"Client Disconnected!",Toast.LENGTH_LONG).show();
        mSenderAsyncTask.cancel(true);
        Intent i = new Intent(FileBrowser.this,FileTypeActivity.class);
        startActivity(i);
        finish();
    }

    class LoaderAsynktask extends AsyncTask<File,Integer,Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            anim = ObjectAnimator.ofInt(progressBar, "progress", 0, 500);
            anim.setRepeatCount(Animation.INFINITE);
            anim.setInterpolator(new DecelerateInterpolator());
            anim.setDuration(800);
            anim.start();

        }

        @Override
        protected Void doInBackground(File... files) {
            browseOrDatabase(files[0]);
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.clearAnimation();
            progressBar.setVisibility(View.GONE);
            makeGrid();
            // HIDE THE SPINNER AFTER LOADING FEEDS

        }
    }


    public void browseOrDatabase(File file){

        if(pref.getFlag()==0)
            searchFolderRecursive1(file);
        else
        {
            List<FileDatabase> files = db.getAllContacts();
            for (FileDatabase cn : files ) {
                String log = "Id: "+cn.getId()+" ,Extension: " + cn.getExtension() + " ,Path: " +
                        cn.getPath()+ "Name: "+cn.getName();

                String ext=pref.getExtension();
                if(("."+cn.getExtension()).equals(ext))
                {file_paths.add(cn.getPath());
                file_names.add(cn.getName());}
                // Writing Contacts to log
                Log.d("File in browseFn: ", log);
            }

        }
    }


}

class FileOpen {

    public static void openFile(Context context, File url) throws IOException {
        // Create URI
        File file = url;
        Uri uri = Uri.fromFile(file);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        // Check what kind of file you are trying to open, by comparing the url with extensions.
        // When the if condition is matched, plugin sets the correct intent (mime) type,
        // so Android knew what application to use to open the file
        if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
            // Word document
            intent.setDataAndType(uri, "application/msword");
        } else if (url.toString().contains(".pdf")) {
            // PDF file
            intent.setDataAndType(uri, "application/pdf");
        } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
            // Powerpoint file
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
            // Excel file
            intent.setDataAndType(uri, "application/vnd.ms-excel");
        } else if (url.toString().contains(".zip") || url.toString().contains(".rar")) {
            // WAV audio file
            intent.setDataAndType(uri, "application/x-wav");
        } else if (url.toString().contains(".rtf")) {
            // RTF file
            intent.setDataAndType(uri, "application/rtf");
        } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
            // WAV audio file
            intent.setDataAndType(uri, "audio/x-wav");
        } else if (url.toString().contains(".gif")) {
            // GIF file
            intent.setDataAndType(uri, "image/gif");
        } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
            // JPG file
            intent.setDataAndType(uri, "image/jpeg");
        } else if (url.toString().contains(".txt")) {
            // Text file
            intent.setDataAndType(uri, "text/plain");
        } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
            // Video files
            intent.setDataAndType(uri, "video/*");
        } else {
            //if you want you can also define the intent type for any other file

            //additionally use else clause below, to manage other unknown extensions
            //in this case, Android will show all applications installed on the device
            //so you can choose which application to use
            intent.setDataAndType(uri, "*/*");
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }






}
package com.example.saman.gifimage;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;


import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import life.knowledge4.videotrimmer.interfaces.OnTrimVideoListener;


public class TrimmerActivity extends Activity implements OnTrimVideoListener {

    private K4LVideoTrimmer mVideoTrimmer;
    File file1;
    ArrayList<Bitmap> frames;
    //FFmpegMediaMetadataRetriever mmRetriever;
    Uri myVideoUri;
    MediaPlayer mp;
    Bitmap bitmap;
    File myVideo;
    Uri VideoUri;
    ProgressDialog dialog;
    FFmpeg ffmpeg;
    private ProgressDialog progressDialog;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    File mypath;
    InterstitialAd mInterstitialAd;
    CountDownTimer timer;
    String path="";
    @Override
    protected void onStop() {
        super.onStop();
        timer.onFinish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      /*  getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();*/

        setContentView(R.layout.activity_trimmer);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        timer= new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                Log.e("Activity","Tick " + millisUntilFinished);

                if(mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
                else
                {
                    Log.e("Activity","Ad not loaded");
                }
            }

            public void onFinish() {
                timer.cancel();
            }
        }.start();


        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                timer.onFinish();
                //   gif();
            }
        });
        dialog = new ProgressDialog(TrimmerActivity.this);


        ffmpeg=new FFmpeg(this);
        sharedpreferences  = getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
      //  FFmpeg.getInstance(this .getApplicationContext());


        Intent extraIntent = getIntent();
        // path = "";

        if (extraIntent != null) {
            path = extraIntent.getStringExtra(MainScreen.EXTRA_VIDEO_PATH);
        }
    }

    public void requestNewInterstitial() {
        Log.e("Activity", "Request Ad " );
        AdRequest adRequest = new AdRequest.Builder().build();
             /*   .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();*/

        mInterstitialAd.loadAd(adRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();


        if (!mInterstitialAd.isLoaded()) {
            requestNewInterstitial();
        }

        mVideoTrimmer = ((K4LVideoTrimmer) findViewById(R.id.timeLine));
        if (mVideoTrimmer != null) {
            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            // path to /data/data/yourapp/app_data/imageDir
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

            // Create imageDir
        /*    mypath = new File(directory, "quickgif" + System.currentTimeMillis() + ".gif");
            mypath.createNewFile();*/
            mVideoTrimmer.setMaxDuration(30);
            mVideoTrimmer.setOnTrimVideoListener(this);
            mVideoTrimmer.setDestinationPath(directory.getPath());
            mVideoTrimmer.setVideoURI(Uri.parse(path));

            //   mVideoTrimmer.setVideoInformationVisibility(true);

            loadFFMpegBinary();
            initUI();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoTrimmer = null;
    }


    private void initUI() {
        //  runButton.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(null);
        progressDialog.setCancelable(false);
    }

    private void loadFFMpegBinary() {
        try {
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
                @Override
                public void onFailure() {
                    showUnsupportedExceptionDialog();
                }
            });
        } catch (FFmpegNotSupportedException e) {
            showUnsupportedExceptionDialog();
        }
    }

    @Override
    public void getResult(final Uri uri) {
        VideoUri=uri;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ContextWrapper cw = new ContextWrapper(getApplicationContext());
                // path to /data/data/yourapp/app_data/imageDir
                File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

                // Create image
                 mypath = new File(directory, "quickgif" + System.currentTimeMillis() + ".gif");


                //String[] command = cmd.split(" ");
                String[] command = {"-i",VideoUri.getPath().toString(),"-vf","scale=320:-1","-pix_fmt","rgb24",mypath.toString()};
                Log.e("Activity","COMMAND " + command);
                if (command.length != 0) {
                    execFFmpegBinary(command);
                } else {
                    Toast.makeText(TrimmerActivity.this, "You cannot execute empty command", Toast.LENGTH_LONG).show();
                }

               /* Toast.makeText(TrimmerActivity.this, "Video Saved at " +uri.getPath(), Toast.LENGTH_SHORT).show();*/
            }
        });


    }
    public void home(View v)
    {
        Intent i = new Intent(TrimmerActivity.this,MainScreen.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    private void execFFmpegBinary(final String[] command) {
        try {
            ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {
                @Override
                public void onFailure(String s) {
                    Log.e("Activity", "Failed command : ffmpeg " + s);
                    File trim_file=  new File(VideoUri.toString());
                    trim_file.delete();
                    progressDialog.dismiss();
                }

                @Override
                public void onSuccess(String s) {
                    Log.e("Activity", "Success command : ffmpeg " + s);
                    String last_file=(sharedpreferences.getString("videofilename", ""));
                    progressDialog.dismiss();
                    if(last_file!=null)
                    {
                        Log.e("Activity", "last file not null");
                        File lFile= new File(last_file);
                        lFile.delete();
                        File trim_file=  new File(VideoUri.toString());
                        trim_file.delete();
                    }
                    else
                    {
                        Log.e("Activity","last file is null");
                    }
//                    String file2=file1.getAbsolutePath().toString();
//                    editor.putString("videofilename", file2);
//                    editor.commit();

                    String giffile= mypath.getAbsolutePath().toString();
                    editor.putString("videofilename",giffile);
                    editor.commit();
                    Log.e("Activity", "VIDEO GIF NAME " + file1);
                    Intent j = new Intent(TrimmerActivity.this, VideoGIFView.class);
                    File file3= new File(mypath.toString());
                    j.putExtra("FILE_NAME", file3);
                    startActivity(j);
                }

                @Override
                public void onProgress(String s) {
                    Log.e("Activity", "Progress command : ffmpeg " + s);
                    //  addTextViewToLayout("progress : " + s);
                    progressDialog.setMessage("Processing ");
                }

                @Override
                public void onStart() {
                    //  outputLayout.removeAllViews();

                    Log.e("Activity", "Started command : ffmpeg " );
                    progressDialog.setMessage("Processing...");
                    progressDialog.show();
                }

                @Override
                public void onFinish() {
                    Log.e("Activity", "Finished command : ffmpeg " + command.toString());
                    progressDialog.dismiss();
                  /*  Intent j = new Intent(TrimmerActivity.this, VideoGIFView.class);
                    j.putExtra("FILE_NAME", file1);
                    startActivity(j);*/
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            Log.e("Activity","Exception: " + e);
            // do nothing for now
        }
    }


    private void showUnsupportedExceptionDialog() {
        new AlertDialog.Builder(TrimmerActivity.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Device not supported")
                .setMessage("FFmpeg is not supported on your device")
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TrimmerActivity.this.finish();
                    }
                })
                .create()
                .show();

    }


    @Override
    public void cancelAction() {
        mVideoTrimmer.destroy();
        finish();
    }

    /*class GIFAsync extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {

         *//*   myVideo = new File
                    (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getAbsolutePath(),"MiniMovie"+ File.separator +
                            "MiniMovie_Kids_160704.mp4");*//*

         //   myVideo = Uri.fromFile(uri.getPath());
                myVideo = new File(VideoUri.getPath());
            try {
            *//*    myVideo = new File
                        (Environment.getExternalStorageDirectory().getAbsolutePath(), "out5.mp4");*//*
                Log.e("Activity", "myVideo file name " + myVideo);
// URI to your video file
              //  myVideoUri = Uri.parse(myVideo.toString());
                myVideoUri=VideoUri;
// MediaMetadataRetriever instance
                mmRetriever = new FFmpegMediaMetadataRetriever();
                mmRetriever.setDataSource(myVideo.getAbsolutePath());

// Array list to hold your frames
                frames = new ArrayList<Bitmap>();

//Create a new Media Player
                mp = MediaPlayer.create(getBaseContext(), myVideoUri);
            //    Log.e("Activity","VIDEO INFO " + );
                int millis = mp.getDuration();

                Log.e("Activity", "TIME DURATION " + millis);
// Some kind of iteration to retrieve the frames and add it to Array list


                String nameoffile = "QuickGIF" + System.currentTimeMillis() + ".gif";
                file1 = new File(Environment.getExternalStorageDirectory(), "Quick GIF" + File.separator + nameoffile);
                Log.e("Activity", "GIF FILE NAME" + file1);
                // FileOutputStream os= new FileOutputStream(file1);
                file1.createNewFile();
                AnimatedGifWriter writer = new AnimatedGifWriter(true);
                OutputStream os = new FileOutputStream(file1);
                writer.prepareForWrite(os, -1, -1);


                for (int i = 0; i <= millis * 1000; i += 100000) {
                    bitmap = mmRetriever.getFrameAtTime(i, FFmpegMediaMetadataRetriever.OPTION_CLOSEST);
                    Log.e("Activity", "FRAMES CREATING " + i);
                    frames.add(bitmap);

                    if (bitmap != null) {
                        writer.writeFrame(os, bitmap);
                        bitmap.recycle();
                        bitmap = null;
                    }
                    Log.e("Activity", "FRAMES CREATING " + i);
                }


               *//* Log.e("Activity", "FRAME ARRAY SIZE " + frames.size());
                for (int i = 0; i < frames.size(); i++) {
                    Log.e("Activity", "0");
                    //  Bitmap  icon1 =gif.get(position);
                    writer.writeFrame(os, frames.get(i));
                    Log.e("Activity", "GIF ");
                }*//*
                writer.finishWrite(os);
                Log.e("Activity", "GIF created");
            } catch (Exception e) {
                Log.e("Activity", "Exception " + e);
            }


            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog.setMessage("Loading... Please Wait!");
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
            Log.e("Activity","GIF DELAYSPEED " + GifView.delayspeed);
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            if(dialog.isShowing())
                dialog.dismiss();

            Intent j = new Intent(TrimmerActivity.this, VideoGIFView.class);
            j.putExtra("FILE_NAME", file1);
            startActivity(j);
        }
    }*/
}
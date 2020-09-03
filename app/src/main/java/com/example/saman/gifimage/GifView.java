package com.example.saman.gifimage;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.crash.FirebaseCrash;
/*
import com.koushikdutta.async.http.BasicNameValuePair;
import com.koushikdutta.async.http.NameValuePair;
import com.koushikdutta.ion.Ion;
*/

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;

import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by saman on 4/15/2016.
 */
public class GifView extends Activity implements Animation.AnimationListener {
    ImageView gifView;
    static File file;
    ProgressDialog dialog;
    MultipartEntity multipartContent;
    public static final String MULTIPART_FORM_DATA = "multipart/form-data;" + "charset=ISO-8859-1";
    BufferedReader reader = null;
    String text;
    //  String temp1;
    int serverResponseCode;
    Map<String, Object> values;
    public static String Key = "qx1Cpd?eD7qx1Cpd?eD7qx1Cpd?eD7";
    File file1;
    PopupWindow alert;
    String temp1;
    ImageView image1, image2, image3;
    public static String upLoadServerUri = "http://staging.fsdcloud.com/shariq/gif-cam/api/image";
    int i = 0;
    public static File final_file;
    Animation animationslideleft;
    Animation animationslideright;
    public static int delayspeed = 300;
    TextView speedtext;
    SeekBar delayseekbar;
    int step = 1;
    int max = 1000;
    int min = 100;
    int value;
    LinearLayout speedbar;
    ImageView crop;
    ImageView speed;
    Runnable r;
    public int gif_status = 0;
    public int delay_status = 0;
    SharedPreferences sharedpreferences;
    public static ArrayList<Bitmap> gif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gif_viewer);
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        speedtext = (TextView) findViewById(R.id.speedtext);
        delayseekbar = (SeekBar) findViewById(R.id.seekBar2);
        gifView = (ImageView) findViewById(R.id.imageView1);
        speedbar = (LinearLayout) findViewById(R.id.speedbar);
        animationslideleft = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_left);
        animationslideleft.setAnimationListener(this);
        animationslideright = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);
        animationslideright.setAnimationListener(this);
        sharedpreferences = getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE);
        speed = (ImageView) findViewById(R.id.speed);
        dialog = new ProgressDialog(GifView.this);
        image1 = (ImageView) findViewById(R.id.imageView4);
        image2 = (ImageView) findViewById(R.id.imageView5);
        image3 = (ImageView) findViewById(R.id.imageView6);
        image2.setImageResource(R.drawable.save_gif);
        image2.setEnabled(true);
        image1.setEnabled(true);
        image3.setEnabled(true);
        gif = new ArrayList<Bitmap>();
        gif=PreviewGIF.gif_images;
        delayseekbar.setMax((max - min) / step);
        delayseekbar.setProgress(delayspeed);
        delayseekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {

                value = min + (progress * step);
                delayspeed = value;
                speedtext.setText(delayspeed + " ms");
                delay_status = 1;
            }
        });
        if(gif==null)
        {
            Intent i = new Intent(GifView.this,ActivityCamera.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
         else {
            r = new Runnable() {
                public void run() {
                    gifView.setImageBitmap(gif.get(i));

                    i++;
                    if (i >= gif.size()) {
                        i = 0;
                    }
                    gifView.postDelayed(r, delayspeed); //set to go off again in 3 seconds.
                }
            };
            gifView.postDelayed(r, delayspeed);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        gif = new ArrayList<Bitmap>();
        gif=PreviewGIF.gif_images;
        if(gif!=null) {
            String filename1 = (sharedpreferences.getString("filename", ""));
            delayseekbar.setProgress(delayspeed);
            speedbar.setVisibility(View.INVISIBLE);
            speedtext.setText(delayspeed + " ms");

            speed.setImageResource(R.drawable.time_1);
            file = new File(filename1);

            if (file != null) {
                Log.e("Activity", " " + file);
                final_file = file;
            } else {
                image2.setImageResource(R.drawable.save_btn_1);
                image2.setEnabled(false);
            }
        }
    }

    public void gif_speed(View v) {

        if (speedbar.getVisibility() == View.VISIBLE) {
            speedbar.setVisibility(View.INVISIBLE);
            speedbar.startAnimation(animationslideleft);
            speed.setImageResource(R.drawable.time_1);

        } else {
            speedbar.setVisibility(View.VISIBLE);
            speedbar.startAnimation(animationslideright);
            speed.setImageResource(R.drawable.time);

        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        /*if(file!=null) {
            if (file.exists())
                file.delete();
        }*/
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public void save_gif(View v) throws IOException {
        image1.setEnabled(false);
        image3.setEnabled(false);
        SaveGIFImageAsyncTask AsyncImage = new SaveGIFImageAsyncTask();
        AsyncImage.execute();
        }


    public class SaveGIFImageAsyncTask extends AsyncTask
    {
        @Override
        protected Object doInBackground(Object[] params) {
            try {
                downloadGIFImage();
            } catch (IOException e) {
                e.printStackTrace();
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

        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            dialog.dismiss();
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file1));
            sendBroadcast(mediaScanIntent);
            alert = new PopupWindow(GifView.this);


            alert.show();


            new android.os.Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    image1.setEnabled(true);
                    image3.setEnabled(true);
                    image2.setEnabled(true);
                    image2.setImageResource(R.drawable.save_gif);
                    alert.dismiss();
                }
            }, 2000);

        }
    }


   /* private class SaveGIFImageThread implements Runnable {


        @Override
        public void run() {
          *//*  Log.e("Activity", "Inside Thread " + mfile);
            Log.e("Activity", "Inside Thread " + iFilter);
          *//*
            try {
                downloadGIFImage();
            } catch (IOException e) {
                FirebaseCrash.logcat(Log.ERROR, "Quick GIF", "Error caught");
                FirebaseCrash.report(e);
                e.printStackTrace();
            }
        }
    }*/

    public void downloadGIFImage() throws IOException {
        if (delay_status == 0) {
            if (file != null) {
                String folder_main = "Quick GIF";

                File f = new File(Environment.getExternalStorageDirectory(), folder_main);
                if (!f.exists()) {
                    f.mkdirs();
                }


                String nameoffile = "QuickGIF" + System.currentTimeMillis() + ".gif";
                file1 = new File(Environment.getExternalStorageDirectory(), "Quick GIF" + File.separator + nameoffile);
                Log.e("Activity", "GIF FILE NAME" + file1);
                // FileOutputStream os= new FileOutputStream(file1);

                file1.createNewFile();

                //   Bitmap bitmap = BitmapFactory.decodeFile(file.toString());
                InputStream in = new FileInputStream(final_file);
                OutputStream out = new FileOutputStream(file1);

                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
                // file.delete();

                final_file = file1;
                // Dialog alertDialog = new AlertDialog.Builder(GifView.this).create();

            }
        } else {
            String nameoffile = "QuickGIF" + System.currentTimeMillis() + ".gif";
            file1 = new File(Environment.getExternalStorageDirectory(), "Quick GIF" + File.separator + nameoffile);
            Log.e("Activity", "GIF FILE NAME" + file1);
            // FileOutputStream os= new FileOutputStream(file1);

            file1.createNewFile();
            try {
                AnimatedGifWriter writer = new AnimatedGifWriter(true);
                OutputStream os = new FileOutputStream(file1);
                writer.prepareForWrite(os, -1, -1);
                for(int position=0;position<gif.size();position++) {
                  Bitmap  icon1 =gif.get(position);
                    writer.writeFrame(os, icon1);
                }
                writer.finishWrite(os);

            } catch (Exception e) {
            }

            final_file = file1;
            // Dialog alertDialog = new AlertDialog.Builder(GifView.this).create();

        }
    }

    public void home(View v)
    {
        Intent i = new Intent(GifView.this,MainScreen.class);

        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    public void shareit(View view) throws IOException {
        new Share().execute(upLoadServerUri);


    }


    private String readFully(InputStream entityResponse) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = entityResponse.read(buffer)) != -1) {
            baos.write(buffer, 0, length);
        }
        return baos.toString();
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }


    public class Share extends AsyncTask<String, Void, String> {
        String temp = null;
        OutputStream os = null;
        String boundary =  "*****";
        PrintWriter writer;
        String charset;


        private String PostRequest(String Url,
                                   int TimeoutSocket,
                                   int TimeoutConnection,
                                   MultipartEntity multipartContent) throws IOException {
         /*   if (ContentType.equals("")) {
                ContentType = "application/x-www-form-urlencoded";
            }  Log.e("Activity", "temp value" + 2);
*/
            URL url = new URL(Url);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TimeoutSocket /*milliseconds*/);
            conn.setConnectTimeout(TimeoutConnection /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.addRequestProperty("Content-length", multipartContent.getContentLength() + "");
            conn.addRequestProperty(multipartContent.getContentType().getName(), multipartContent.getContentType().getValue());

            OutputStream os = conn.getOutputStream();
            multipartContent.writeTo(conn.getOutputStream());

            conn.connect();
            int code=conn.getResponseCode();

            os.flush();
            if(code==200) {
                InputStream inputStream = conn.getInputStream();
                temp1= readFully(inputStream);
                //   Log.e("Activity", "temp " +temp);
                //clean up
                os.close();
                inputStream.close();
                conn.disconnect();
            }
           /* else
            {


            }*/
            return temp1;
        }
        @Override
        protected String doInBackground(String... urls) {
          try
            {
            if (delay_status == 0)
            {
                if (file != null) {
                    ContextWrapper cw = new ContextWrapper(getApplicationContext());
                    // path to /data/data/yourapp/app_data/imageDir
                    File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

                    // Create imageDir
                    file1 = new File(directory, "quickgif" + System.currentTimeMillis() + ".gif");
                    file1.createNewFile();

                    Log.e("Activity", "GIF FILE NAME" + file1);
                    // FileOutputStream os= new FileOutputStream(file1);

                    file1.createNewFile();

                    //   Bitmap bitmap = BitmapFactory.decodeFile(file.toString());
                    InputStream in = new FileInputStream(final_file);
                    OutputStream out = new FileOutputStream(file1);

                    // Transfer bytes from in to out
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    in.close();
                    out.close();
                    // file.delete();

                    final_file = file1;
                    // Dialog alertDialog = new AlertDialog.Builder(GifView.this).create();

                }
            } else
            {
                ContextWrapper cw = new ContextWrapper(getApplicationContext());
                // path to /data/data/yourapp/app_data/imageDir
                File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

                // Create imageDir
                file1 = new File(directory, "quickgif" + System.currentTimeMillis() + ".gif");
                file1.createNewFile();

                Log.e("Activity", "GIF FILE NAME" + file1);
                // FileOutputStream os= new FileOutputStream(file1);

                file1.createNewFile();

                    AnimatedGifWriter writer = new AnimatedGifWriter(true);
                    OutputStream os = new FileOutputStream(file1);
                    writer.prepareForWrite(os, -1, -1);
                    for(int position=0;position<gif.size();position++) {
                        Bitmap  icon1 = gif.get(position);
                        writer.writeFrame(os, icon1);
                    }
                    writer.finishWrite(os);


                final_file = file1;
                // Dialog alertDialog = new AlertDialog.Builder(GifView.this).create();

            }


            String last_file=(sharedpreferences.getString("filename", ""));
            if(last_file!=null)
            {
                Log.e("Activity","last file not null");
                File lFile= new File(last_file);
                lFile.delete();
            }
            else
            {
                Log.e("Activity","last file is null");
            }

                String file2=file1.getAbsolutePath().toString();
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("filename", file2);
            editor.commit();
                Log.e("Activity","FINAL FILE VALUE " + final_file);
                MultipartEntity multipartContent = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                String FileName=final_file.toString();
                if (!FileName.equals("")) {
                    File output = new File(FileName);
                    FileBody fileBody = new FileBody(output);

                    // We use ContentBody to transfer an image
                    multipartContent.addPart("image", fileBody);
                }
                multipartContent.addPart("key",new StringBody(Key) );


                temp1 = PostRequest(upLoadServerUri,20000,20000,multipartContent);


            }
            catch(Exception e)
            {
                Log.e("Activity", "Exception " + e.toString());
                FirebaseCrash.logcat(Log.ERROR, "Quick GIF", "Error caught");
                FirebaseCrash.report(e);
            }
            return temp1;


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            dialog.setMessage("Loading.. Please Wait!");
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();

        }

        @Override
        protected void onPostExecute(String result) {

            Log.e("Activity", "result " + result);
            if (result != null) // add this
            {
                String response = result.toString();
                Log.e("Response", "" + response);

                try {

                    JSONObject titleObject = new JSONObject(response);

                    Log.e("ERROR", " " + titleObject.getString("error"));



                    //  Log.e("TITLE", " " + textView1.getText().toString());

                    JSONObject new_array = titleObject.getJSONObject("data");
                    String url = new_array.getString("url");
                    if(dialog.isShowing())
                        dialog.dismiss();
                    Log.e("Activity","URL " + url);
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, url);
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);

                } catch (JSONException e) {
                    FirebaseCrash.logcat(Log.ERROR, "Quick GIF", "Error caught");
                    FirebaseCrash.report(e);
                    e.printStackTrace();
                }

            }
            else
            {
                if(dialog.isShowing())
                    dialog.dismiss();
                AlertDialog alertDialog = new AlertDialog.Builder(GifView.this).create();

                alertDialog.setTitle("Info");
                alertDialog.setMessage("Something went wrong. Please check your internet connection");
                alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                 /*       finish();*/


                    }
                });

                alertDialog.show();
            }
        }
    }


    public String GET(String url)
    {
        InputStream inputStream = null;

        String result = "";
        try {

            URL url1=new URL(url);
            HttpURLConnection connection= (HttpURLConnection)url1.openConnection();
            connection.connect();
            int code=connection.getResponseCode();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            Log.e("Activity", "CODE " + code);
            if(code==200)
            {     Log.e("code","" + code);
                inputStream = connection.getInputStream();

                // convert inputstream to string
                if (inputStream != null)
                    result = convertInputStreamToString(inputStream);
                else
                    result = "Did not work!";

                Log.e("Result", "" + result);

            }
            else
            {

                try {
                    AlertDialog alertDialog = new AlertDialog.Builder(GifView.this).create();

                    alertDialog.setTitle("Info");
                    alertDialog.setMessage("Something went wrong. Please check your internet connection");
                    alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();


                        }
                    });

                    alertDialog.show();
                }
                catch(Exception e)
                {
                    FirebaseCrash.logcat(Log.ERROR, "Quick GIF", "Error caught");
                    FirebaseCrash.report(e);
                    Log.d("ALERT DIALOG ERROR", "Show Dialog: "+e.getMessage());
                }

            }

        } catch (MalformedURLException e1) {
            FirebaseCrash.logcat(Log.ERROR, "Quick GIF", "Error caught");
            FirebaseCrash.report(e1);
            e1.printStackTrace();
        } catch (SocketTimeoutException e) {
            FirebaseCrash.logcat(Log.ERROR, "Quick GIF", "Error caught");
            FirebaseCrash.report(e);

        } catch (IOException e1) {
            FirebaseCrash.logcat(Log.ERROR, "Quick GIF", "Error caught");
            FirebaseCrash.report(e1);
            e1.printStackTrace();
        }
        return result;
    }

    public void finish(View v)
    {
        this.finish();
    }
}
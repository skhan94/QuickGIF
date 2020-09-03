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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.crash.FirebaseCrash;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by saman on 7/13/2016.
 */
public class VideoGIFView extends Activity {

    ImageView gifView;
    File file;
    File file1;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    ProgressDialog dialog;
    PopupWindow alert;
    String gif_file;
    String temp1;
    public static String Key = "qx1Cpd?eD7qx1Cpd?eD7qx1Cpd?eD7";
    public static String upLoadServerUri = "http://staging.fsdcloud.com/shariq/gif-cam/api/image";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vide_gif);
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        dialog = new ProgressDialog(this);
        gifView = (ImageView) findViewById(R.id.imageView1);
        sharedpreferences  = getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        gif_file=(sharedpreferences.getString("videofilename", ""));
       // if(getIntent().getSerializableExtra("FILE_NAME")!=null) {
        if(gif_file!=null)
        {
         //   file = (File) getIntent().getSerializableExtra("FILE_NAME");
            file=new File(gif_file);
            Log.e("Activity", "FILE NAME " + file);
            Glide.with(this.getApplicationContext()).load(file.toString()).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(gifView);
        }
    }

    public void home(View v)
    {
        Intent i = new Intent(VideoGIFView.this,MainScreen.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    public void finish(View v)
    {
        finish();
    }



    public void save_gif(View v) throws IOException {

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
            dialog.setProgress(0);
            dialog.show();

        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            dialog.dismiss();
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file1));
            sendBroadcast(mediaScanIntent);
            alert = new PopupWindow(VideoGIFView.this);


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

                    alert.dismiss();
                }
            }, 2000);

        }
    }
    public void downloadGIFImage() throws IOException {

        String folder_main = "Quick GIF";

        File f = new File(Environment.getExternalStorageDirectory(), folder_main);
        if (!f.exists()) {
            f.mkdirs();
        }
        try {

            String nameoffile = "QuickGIF" + System.currentTimeMillis() + ".gif";
            file1 = new File(Environment.getExternalStorageDirectory(), "Quick GIF" + File.separator + nameoffile);
            Log.e("Activity","GIF FILE " + file);
            Log.e("Activity", "GIF FILE NAME" + file1);
            // FileOutputStream os= new FileOutputStream(file1);

            //  file1.createNewFile();

            //   Bitmap bitmap = BitmapFactory.decodeFile(file.toString());
            InputStream in = new FileInputStream(file);
            OutputStream out = new FileOutputStream(file1);

            // Transfer bytes from in to out
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
                Log.e("Activity","GIF saving");
            }
            in.close();
            out.close();
            Toast.makeText(VideoGIFView.this,"GIF saved",Toast.LENGTH_SHORT).show();
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file1));
            this.sendBroadcast(mediaScanIntent);
            Log.e("Activity","GIF saved");
        }
        catch(Exception e)
        {
            Log.e("Activity","Exception " + e);
        }
    }

    public void shareit(View view) throws IOException {
        new Share().execute(upLoadServerUri);


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

        private String readFully(InputStream entityResponse) throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = entityResponse.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
                Log.e("Activity","Length " + length);
            }
            return baos.toString();
        }

        @Override
        protected String doInBackground(String... urls) {
            try
            {



              //  String last_file=(sharedpreferences.getString("filename", ""));

               /* String file2=file1.getAbsolutePath().toString();
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("filename", file2);
                editor.commit();
                Log.e("Activity","FINAL FILE VALUE " + final_file);*/
                MultipartEntity multipartContent = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                String FileName=gif_file.toString();
                if (!FileName.equals("")) {
                    File output = new File(FileName);
                    FileBody fileBody = new FileBody(output);

                    // We use ContentBody to transfer an image
                    multipartContent.addPart("image", fileBody);
                }
                multipartContent.addPart("key",new StringBody(Key) );


                temp1 = PostRequest(upLoadServerUri,50000,50000,multipartContent);


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
                AlertDialog alertDialog = new AlertDialog.Builder(VideoGIFView.this).create();

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


}

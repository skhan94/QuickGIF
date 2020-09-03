package com.example.saman.gifimage;

import android.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import life.knowledge4.videotrimmer.utils.FileUtils;


/**
 * Created by saman on 7/13/2016.
 */
public class MainScreen extends Activity {

    private static final int REQUEST_VIDEO_TRIMMER = 0x01;
    static final String EXTRA_VIDEO_PATH = "EXTRA_VIDEO_PATH";
    Uri fileUri;
    String f_name;
    int video_status=0;
    Uri selectedUri;
    public static int independence_filter=0;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private Uri fileUriImage;
    AdView mAdView;
    AdRequest adRequest;
    public static int crop_ind=0;
    File f1;
    Bitmap b;
    Bitmap bmp;
    boolean internet;
    String url1;
    String pkg1;
    public void showAppsDialog(Context context, final String pkg, String title, String text) {
        final Dialog dialog = new Dialog(context, R.style.Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.app_popup);
        Log.e("Testing", "Intent");
        TextView notification_title = (TextView) dialog.findViewById(R.id.textView2);
        notification_title.setText(title);

        TextView notification_Text = (TextView) dialog.findViewById(R.id.textView1);
        notification_Text.setText(text);


        Button notification_ok = (Button) dialog.findViewById(R.id.imageView5);
        notification_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + pkg)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + pkg)));
                }
                if (dialog.isShowing())
                    dialog.dismiss();
            }
        });

        Button notification_cancel = (Button) dialog.findViewById(R.id.imageView4);
        notification_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog.isShowing())
                    dialog.dismiss();
            }
        });


        ImageView notification_close = (ImageView) dialog.findViewById(R.id.imageView3);
        notification_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog.isShowing())
                    dialog.dismiss();
            }
        });

        if (!dialog.isShowing())
            dialog.show();

    }


    public void showAppsDialog2(Context context,final Bitmap b, final String pkg) throws MalformedURLException {
        final Dialog dialog = new Dialog(context, R.style.Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.app_popup1);
        ImageView notification_ok = (ImageView) dialog.findViewById(R.id.imageView4);
        try {
           Log.e("Activity","BITMAP " + b);
            notification_ok.setImageBitmap(b);
        }
        catch(Exception e)
        {

        }
     //   ImageView notification_ok = (ImageView) dialog.findViewById(R.id.imageView4);
       // notification_ok.setImageURI(Uri.parse(url));
        notification_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + pkg)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + pkg)));
                }
                if (dialog.isShowing())
                    dialog.dismiss();
            }
        });


        ImageView notification_close = (ImageView) dialog.findViewById(R.id.imageView3);
        notification_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog.isShowing())
                    dialog.dismiss();
            }
        });

        if (!dialog.isShowing())
            dialog.show();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        mAdView = (AdView) findViewById(R.id.adView);
        adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        if (getIntent().getExtras() != null)
        {

            Log.e("Testing","Intent");
            String type = getIntent().getStringExtra("type");
            if (type.equals("1"))
            {
                Log.d("FireBaseTest", "Version : " + type);
                final String pkg = getIntent().getStringExtra("pkg");
                final String title = getIntent().getStringExtra("title");
                final String content = getIntent().getStringExtra("content");
                Log.d("FireBaseTest", "Pkg : " + pkg);
                Log.d("FireBaseTest", "Version : " + type);
                showAppsDialog(MainScreen.this, pkg, title, content);
            }
            else if(type.equals("2"))
            {
                 url1 = getIntent().getStringExtra("url");
                pkg1 = getIntent().getStringExtra("pkg");

                PopupAsync popup = new PopupAsync();
                popup.execute();


            }


        }
        ImageView cameraButton =(ImageView) findViewById(R.id.button3);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gifimage();
            }
        });

        ImageView galleryButton = (ImageView) findViewById(R.id.galleryButton);
        if (galleryButton != null) {
            galleryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pickFromGallery();
                }
            });
        }


        ImageView recordButton = (ImageView) findViewById(R.id.cameravideoButton);
        if (recordButton != null) {
            recordButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openVideoCapture();
                }
            });
        }

     /*   ImageView camerarecordButton = (ImageView) findViewById(R.id.cameraIndependence);
        if (camerarecordButton != null)
        {
            camerarecordButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {



                    AlertDialog.Builder builder = new AlertDialog.Builder(MainScreen.this);
                    builder.setTitle("Select a Country")
                            .setItems(R.array.Independence_Day, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    independence_filter = which + 1;
                                    Intent i = new Intent(MainScreen.this, IndependenceCamera.class);
                                    startActivity(i);
                                }
                            });
                    AlertDialog alertDialogObject = builder.create();
                    //Show the dialog
                    alertDialogObject.show();
                }

            });
        }*/

    }

    @Override
    protected void onResume() {
        super.onResume();

        //independence_filter=0;
    }

    /*@Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
        Log.e("Activity","File Uri onSaveInstanceState" + fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
        Log.e("Activity","File Uri onRestoreInstanceState" + fileUri);
    }
*/

    public void gifimage()
    {
        Intent i= new Intent(MainScreen.this,ActivityCamera.class);
        startActivity(i);

    }

    private void openVideoCapture() {
        String folder_main = "Quick GIF";

        File f = new File(Environment.getExternalStorageDirectory(), folder_main);
        if (!f.exists()) {
            f.mkdirs();
        }
        Intent videoCapture = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        f_name="QuickGIF" + System.currentTimeMillis() + ".mp4";

        File video_file= new File(Environment.getExternalStorageDirectory(),"Quick GIF"+File.separator  + f_name);


        fileUri = Uri.fromFile(video_file);
       videoCapture.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
         video_status=0;
       // String videoPath = getRealPathFromURI(vid);
        startActivityForResult(videoCapture, REQUEST_VIDEO_TRIMMER);
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void pickFromGallery() {
            video_status=1;
            Intent intent = new Intent();
            intent.setTypeAndNormalize("video/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(Intent.createChooser(intent, "Select a Video"), REQUEST_VIDEO_TRIMMER);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
        {
/*
            if (resultCode == RESULT_OK) {
                 f1 = new File(Environment.getExternalStorageDirectory().toString(), "Quick GIF");
                for (File temp : f1.listFiles()) {
                    if (temp.getName().equals(f_name)) {
                        f1 = temp;
                        break;
                    }

                     b= BitmapFactory.decodeFile(f1.getAbsolutePath());
                    if (b.getWidth() >= b.getHeight()){

                        b = Bitmap.createBitmap(
                                b,
                                b.getWidth() / 2 - b.getHeight() / 2,
                                0,
                                b.getHeight(),
                                b.getHeight()
                        );

                    }else{

                        b = Bitmap.createBitmap(
                                b,
                                0,
                                b.getHeight()/2 - b.getWidth()/2,
                                b.getWidth(),
                                b.getWidth()
                        );
                    }


                    int w =b.getWidth();
                    int h = b.getHeight();

                    final Bitmap result = Bitmap.createBitmap(w, h, b.getConfig());
                    Canvas canvas = new Canvas(result);
                    canvas.drawBitmap(b, 0, 0, null);


                    int ww = Math.round((float) (b.getWidth() * 0.25));

                    int hh = Math.round((float) (bitmap.getHeight() * 0.15));


                    Bitmap waterMark = BitmapFactory.decodeResource(getResources(), R.drawable.watermark);
                    Bitmap watermark1 = Bitmap.createScaledBitmap(waterMark, ww, hh, true);
                    canvas.drawBitmap(watermark1, w - ww-5, h - hh-5, null);
                    finalfile.delete();
                    File finalfile1 = new File(Environment
                            .getExternalStorageDirectory(), "Quick GIF" + File.separator + "Image" + System.currentTimeMillis() + ".jpg");
                    FileOutputStream out1 = new FileOutputStream(finalfile1);
                    result.compress(Bitmap.CompressFormat.JPEG, 90, out1);

                    out1.flush();
                    out1.close();
                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,Uri.fromFile(finalfile1));
                    this.sendBroadcast(mediaScanIntent);
                    //Bitmap imagebitmap= Bitmap.createScaledBitmap(b,480,640,false);

                }
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
            }*/
        }
        else {
            if (resultCode == RESULT_OK) {
                if (requestCode == REQUEST_VIDEO_TRIMMER) {
                    if (video_status == 0) {
                        File f = new File(Environment.getExternalStorageDirectory().toString(), "Quick GIF");
                        for (File temp : f.listFiles()) {
                            if (temp.getName().equals(f_name)) {
                                f = temp;
                                break;
                            }
                        }
                        selectedUri = Uri.fromFile(f);
                    } else if (video_status == 1)   //video from gallery
                    {
                        selectedUri = data.getData();
                    }
                    // final Uri selectedUri = (Uri) data.getExtras().get("data");
//                final Uri selectedUri =  data.getData();
//                Log.e("Activity","SELECTED URI PATH 1" + data.getExtras().get("data"));

                    //     Log.e("Activity","SELECTED URI PATH " + selectedUri);
                    //     Log.e("Activity","FILE SELECTED URI PATH " + fileUri.toString());
                    if (selectedUri != null) {
                        startTrimActivity(selectedUri);
                    } else {

                        Toast.makeText(MainScreen.this, "Cannot retrieve selected video", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private void startTrimActivity(@NonNull Uri uri) {
        Intent intent = new Intent(this, TrimmerActivity.class);
        intent.putExtra(EXTRA_VIDEO_PATH, FileUtils.getPath(this, uri));
      //  Log.e("Activity","")
        startActivity(intent);
    }


class PopupAsync extends AsyncTask{

    @Override
    protected Object doInBackground(Object[] params) {


        try {
            Log.e("Activity", "IMAGE URI " + url1);
            URL url2 = new URL(url1.toString());
            bmp = BitmapFactory.decodeStream(url2.openConnection().getInputStream());
            Log.e("Activity","IMAGE URI2 BITMAP " + bmp);
            //  notification_ok.setImageBitmap(bmp);
        }
        catch(Exception e)
        {

        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
         super.onPostExecute(o);


        try {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if(activeNetworkInfo != null && activeNetworkInfo.isConnected())
                internet=true;
            else
                internet=false;

            if(internet) {
                showAppsDialog2(MainScreen.this, bmp, pkg1);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
}

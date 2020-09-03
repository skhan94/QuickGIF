package com.example.saman.gifimage;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

/**
 * Created by saman on 5/18/2016.
 */public class SplashScreen extends Activity {

    private static int SPLASH_TIME_OUT = 3000;
   // int version=0;
    String type = "0";
    String pkg = "", appname = "", content = "", title = "", version = "", url="";
//    url="https://i.ytimg.com/vi/9Nwn-TZfFUI/maxresdefault.jpg"
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run()
            {
                // This method will be executed once the timer is over
                // Start your app main activity
            /*    if (getIntent().getExtras() != null) {
                    for (String key : getIntent().getExtras().keySet()) {
                        String value = getIntent().getExtras().getString(key);
                        Log.e("Activity", "Key: " + key + " Value: " + value);
                    }
                }*/
             /*   if (getIntent().getExtras() != null) {
                    for (String key : getIntent().getExtras().keySet()) {
                        String value = getIntent().getExtras().getString(key);
                        if(key.equals("newVersion") && value.equals("true"))
                        {
                            version=1;
                        }
                        Log.e("Activity", "Key: " + key + " Value: " + value);
                    }
                }*/
                if (getIntent().getExtras() != null) {
                    for (String key : getIntent().getExtras().keySet()) {
                        String value = getIntent().getExtras().getString(key);
                        if (key.equalsIgnoreCase("type")) {
                            type = value;
                        } else if (key.equalsIgnoreCase("pkg")) {
                            pkg = value;
                        }
                        else if (key.equalsIgnoreCase("title")) {
                            title = value;
                        } else if (key.equalsIgnoreCase("content")) {
                            content = value;
                        }
                        else if (key.equalsIgnoreCase("url")) {
                            url = value;
                        }

                    }
                }
            /*    Intent Main_Menu = new Intent(MainActivity.this, MainScreen.class);
                Main_Menu.putExtra("type", type);
                Main_Menu.putExtra("pkg", pkg);
                Main_Menu.putExtra("title",title);
                Main_Menu.putExtra("content", content);
                Log.e("Testing","Type " +type + " pkg " + pkg + " title " + title + " content " + content);
                startActivity(Main_Menu);*/





/*
                String key="newVersion";
                String value="true";
                if(key=="newVersion" && value=="true") {
                    version = 1;
                }*/
                Log.e("Activity","Version Value " + version);


                int permissionCheck = ContextCompat.checkSelfPermission(SplashScreen.this, Manifest.permission.CAMERA);
                int permissionCheck1 = ContextCompat.checkSelfPermission(SplashScreen.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                int permissionCheck2 = ContextCompat.checkSelfPermission(SplashScreen.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if(permissionCheck == PackageManager.PERMISSION_GRANTED && permissionCheck1 == PackageManager.PERMISSION_GRANTED && permissionCheck2 == PackageManager.PERMISSION_GRANTED ) {
                    Intent i = new Intent(SplashScreen.this, MainScreen.class);
                    i.putExtra("type", type);
                    i.putExtra("pkg", pkg);
                    i.putExtra("title",title);
                    i.putExtra("content", content);
                    i.putExtra("url", url);
                    startActivity(i);
                }
                else
                {
                    Intent i = new Intent(SplashScreen.this, Permission.class);
                    i.putExtra("type", type);
                    i.putExtra("pkg", pkg);
                    i.putExtra("title",title);
                    i.putExtra("content", content);
                    i.putExtra("url", url);
                    startActivity(i);

                }
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }




}
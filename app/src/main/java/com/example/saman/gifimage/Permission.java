package com.example.saman.gifimage;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

/**
 * Created by saman on 5/27/2016.
 */
public class Permission extends Activity {

    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.permission);
        if (android.os.Build.VERSION.SDK_INT >= 23)
        // only for gingerbread and newer versions
        {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {

                Log.e("Activity", "Permission granted ");

            }

        }

        else
        {   if (getIntent().getExtras() != null) {
            String type = getIntent().getStringExtra("type");
            Log.d("FireBaseTest", "Version : " + type);
            final String pkg = getIntent().getStringExtra("pkg");
            final String title = getIntent().getStringExtra("title");
            final String content = getIntent().getStringExtra("content");
            final String url = getIntent().getStringExtra("url");
            Intent i = new Intent(Permission.this, MainScreen.class);
            i.putExtra("type", type);
            i.putExtra("pkg", pkg);
            i.putExtra("title", title);
            i.putExtra("content", content);
            i.putExtra("url", url);
            this.finish();
            startActivity(i);
        }
        else {
            Intent i = new Intent(Permission.this, MainScreen.class);
            this.finish();
            startActivity(i);
        }

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.e("Activity", "Permission granted ");
                    if (getIntent().getExtras() != null) {
                        String type = getIntent().getStringExtra("type");
                        Log.d("FireBaseTest", "Version : " + type);
                        final String pkg = getIntent().getStringExtra("pkg");
                        final String title = getIntent().getStringExtra("title");
                        final String content = getIntent().getStringExtra("content");
                        final String url = getIntent().getStringExtra("url");
                        Intent i = new Intent(Permission.this, MainScreen.class);
                        i.putExtra("type", type);
                        i.putExtra("pkg", pkg);
                        i.putExtra("title", title);
                        i.putExtra("content", content);
                        i.putExtra("url", url);
                        this.finish();
                        startActivity(i);
                    }
                    else {
                        Intent i = new Intent(Permission.this, MainScreen.class);
                        this.finish();
                        startActivity(i);
                    }
                } else {

                    finish();

                }
                break;
        }
    }
}
package com.example.saman.gifimage;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v17.leanback.widget.HorizontalGridView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import jp.co.cyberagent.android.gpuimage.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Policy;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.example.saman.gifimage.Camera.CameraHelper;

import com.example.saman.gifimage.filters.GPUImageFilterTools.FilterAdjuster;
import jp.co.cyberagent.android.gpuimage.GPUImageColorInvertFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHueFilter;
import tourguide.tourguide.Overlay;
import tourguide.tourguide.Pointer;
import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;


import com.example.saman.gifimage.Camera.CameraHelper.CameraInfo2;
import com.example.saman.gifimage.filters.*;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.crash.FirebaseCrash;

import net.yazeed44.imagepicker.model.ImageEntry;
import net.yazeed44.imagepicker.util.Picker;


public class ActivityCamera extends Activity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener, Picker.PickListener {

    public static CustomScrollview filterbar;
    public static RelativeLayout counterlayout;
    public static Activity ac;
    public static GPUImage mGPUImage;
    ImageView creategiffromgallery;
    public static ImageView creategif;
    public static TextView counterText;
    public static ImageView reset;
    public static ImageView captureImage;
    private ImageView flashCameraButton;
    ArrayList<Parcelable> path;
    public static ArrayList<File> captured_image;
    public static int i = 0;
    public static int status = 0;
    private CameraHelper mCameraHelper;
    private CameraLoader mCamera;
    public static GPUImageFilter mFilter;
    public static GPUImageFilter mFilter1;
    public static FilterAdjuster mFilterAdjuster;
    // private File imageFile;
    public static int flashstatus = 0;
    int flipstatus = 0;
    int cameraId;
    //    ImageView editPictureFromGallery;
    File editFile;
    static int filter_state = 0;
    public TourGuide mTourGuideHandler;
    ImageView button2;
    ImageView button3;
    ImageView button4;
    GPUImage GPUImage1;
    int photo = 0;
    //  HorizontalScrollView filterbar;
    public static int settings_state;
    ImageView flip;
    public static ImageView filterbutton;
    public static ImageView toggle;
    //  InterstitialAd mInterstitialAd;
    Boolean isFirstRun;
    ArrayList<String> mSelectedImages;
    // String appPackageName = getPackageName();
    int Version = 0;
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alertDialog;
    public static Activity activity;
    int green_state = 0;    //independence day filter  0 for all filter and 1 for capture image 2 for preview filter
    int front = 0;
    int new_filter = 0; //0 for old filter 1 for new filter
    Bitmap downloadbitmap1;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activitycamera);

        creategiffromgallery = (ImageView) findViewById(R.id.creategiffromgallery);
        mFilter = new GPUImageFilter();
        filterbar = (CustomScrollview) findViewById(R.id.horizontalfilterview);
        toggle = (ImageView) findViewById(R.id.settings);
        counterText = (TextView) findViewById(R.id.countertext);
        filterbar.setVisibility(View.INVISIBLE);
        filterbutton = (ImageView) findViewById(R.id.filterbar);
        settings_state = 0;
        ac = this;
        isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isfirstrun", true);
        //  isFirstRun=false;
        button2 = (ImageView) findViewById(R.id.filter40);
        button2.setOnClickListener(this);

        // For tutorial screen
        if (isFirstRun == true) {
            filterbar.setVisibility(View.VISIBLE);
            photo = 1;

            Log.e("Activity", "First Run");
            filterbar.setEnableScrolling(false);
            mTourGuideHandler = TourGuide.init(this).with(TourGuide.Technique.Click)
                    .setPointer(null) // set pointer to null
                    .setToolTip(new ToolTip().setDescription("Select a filter"))
                    .setOverlay(new Overlay())
                    .playOn(button2);

        }
        flip = (ImageView) findViewById(R.id.img_switch_camera);
        counterlayout = (RelativeLayout) findViewById(R.id.counterlayout);
        flip.setOnClickListener(this);
        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(this);
        findViewById(R.id.seekBar).setVisibility(View.GONE);
        ((SeekBar) findViewById(R.id.seekBar)).setOnSeekBarChangeListener(this);

        button3 = (ImageView) findViewById(R.id.filter41);
        button3.setOnClickListener(this);
        button4 = (ImageView) findViewById(R.id.filter4);
        button4.setOnClickListener(this);
        findViewById(R.id.filter1).setOnClickListener(this);
        findViewById(R.id.filter6).setOnClickListener(this);
        findViewById(R.id.filter5).setOnClickListener(this);
        findViewById(R.id.filter4).setOnClickListener(this);
        findViewById(R.id.filter7).setOnClickListener(this);
        findViewById(R.id.filter8).setOnClickListener(this);
        findViewById(R.id.filter9).setOnClickListener(this);
        findViewById(R.id.filter10).setOnClickListener(this);
        findViewById(R.id.filter11).setOnClickListener(this);
        findViewById(R.id.filter12).setOnClickListener(this);
        findViewById(R.id.filter13).setOnClickListener(this);
        findViewById(R.id.filter14).setOnClickListener(this);
        findViewById(R.id.filter15).setOnClickListener(this);
        findViewById(R.id.filter16).setOnClickListener(this);
        findViewById(R.id.filter17).setOnClickListener(this);
        findViewById(R.id.filter18).setOnClickListener(this);
        findViewById(R.id.filter19).setOnClickListener(this);
        findViewById(R.id.filter20).setOnClickListener(this);
        findViewById(R.id.filter21).setOnClickListener(this);
        findViewById(R.id.filter22).setOnClickListener(this);
        findViewById(R.id.filter23).setOnClickListener(this);
        findViewById(R.id.filter24).setOnClickListener(this);
        findViewById(R.id.filter25).setOnClickListener(this);
        findViewById(R.id.filter26).setOnClickListener(this);
        findViewById(R.id.filter27).setOnClickListener(this);
        findViewById(R.id.filter28).setOnClickListener(this);
        findViewById(R.id.filter29).setOnClickListener(this);
        findViewById(R.id.filter30).setOnClickListener(this);
        findViewById(R.id.filter31).setOnClickListener(this);
        findViewById(R.id.filter32).setOnClickListener(this);
        findViewById(R.id.filter33).setOnClickListener(this);
        findViewById(R.id.filter34).setOnClickListener(this);
        findViewById(R.id.filter42).setOnClickListener(this);
        findViewById(R.id.filter43).setOnClickListener(this);
        findViewById(R.id.filter37).setOnClickListener(this);


        reset = (ImageView) findViewById(R.id.reset);
        flashCameraButton = (ImageView) findViewById(R.id.flash);
        cameraId = CameraInfo.CAMERA_FACING_BACK;
        captureImage = (ImageView) findViewById(R.id.button_capture);
        captureImage.setOnClickListener(this);
        flashCameraButton.setOnClickListener(this);
        creategif = (ImageView) findViewById(R.id.creategif);
        captured_image = new ArrayList<>();

        mGPUImage = new GPUImage(this);

        mGPUImage.setGLSurfaceView((GLSurfaceView) findViewById(R.id.surfaceView));
        GPUImage1 = new GPUImage(this);
        mCameraHelper = new CameraHelper(this);
        mCamera = new CameraLoader();
        if (!getBaseContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_FLASH)) {
            flashCameraButton.setVisibility(View.INVISIBLE);
        }
        View cameraSwitchView = findViewById(R.id.img_switch_camera);
        cameraSwitchView.setOnClickListener(this);
        counterText.setText("0");
        if (!mCameraHelper.hasFrontCamera() || !mCameraHelper.hasBackCamera()) {
            cameraSwitchView.setVisibility(View.INVISIBLE);

            if (settings_state == 0) {

                creategiffromgallery.setVisibility(View.VISIBLE);
                reset.setVisibility(View.VISIBLE);


            } else if (settings_state == 1) {
                //  editPictureFromGallery.setVisibility(View.VISIBLE);
                creategiffromgallery.setVisibility(View.INVISIBLE);
                reset.setVisibility(View.INVISIBLE);

            }


        }
    }

    public void capture() {

        captureImage.setEnabled(false);
        if (photo != 0) {
            mTourGuideHandler.cleanUp();
        }
        if (mCamera.mCameraInstance.getParameters().getFocusMode().equals(
                Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            Log.e("Activity", "AUTO FOCUS OFF ");
            if(i>=5)
            {
                Toast.makeText(this, "You can't take more than 5 pictures",Toast.LENGTH_SHORT).show();
            }
            else
            {
                takePicture();
            }
        } else {
            mCamera.mCameraInstance.autoFocus(new Camera.AutoFocusCallback() {

                @Override
                public void onAutoFocus(final boolean success, final Camera camera) {
                    Log.e("Activity", "AUTO FOCUS ON ");

                    if(i>=5)
                    {
                        Toast.makeText(ActivityCamera.this, "You can't take more than 5 pictures",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        takePicture();
                    }
                }
            });
        }

    }


    @Override
    protected void onResume() {
        super.onResume();

        Log.e("Activity", "CAMERA ID " + mCamera.mCurrentCameraId);
        Log.e("Activity", "New FILTER " + new_filter);
      /*  if(new_filter==1) {
            new_filter=0;
            if (mCamera.mCurrentCameraId == 0) {
                MagicParams.front=0;
                MagicWarmFilter.green_state=0;
                MagicWarmFilter1.green_state=0;
                mFilter = new GPUImageFilter();
                mGPUImage.setFilter(mFilter);
            } else {
                MagicParams.front=0;
                MagicWarmFilter.green_state=0;
                MagicWarmFilter1.green_state=0;
                mCamera.switchCamera();
                mFilter = new GPUImageFilter();
                mGPUImage.setFilter(mFilter);
            }
        }*/
      /*  if(mCamera.mCurrentCameraId==0) {
            MagicParams.front = 0;
            if (new_filter == 1) {
                mFilter = new MagicWarmFilter(0);
                mGPUImage.setFilter(mFilter);
            }
        }else {
            MagicParams.front = 1;
            if (new_filter == 1) {
                mFilter = new MagicWarmFilter(0);
                mGPUImage.setFilter(mFilter);
            }
        }*/
       /* if (!mInterstitialAd.isLoaded()) {
            requestNewInterstitial();
        }*/
        activity = this;
        mCamera.onCreate();
        filterbar.setVisibility(View.INVISIBLE);
        captureImage.setEnabled(true);
        reset.setImageResource(R.drawable.reset_1);
        flashCameraButton.setImageResource(R.drawable.flash_1);

        flip.setImageResource(R.drawable.flip_1);
//        counterText.setText("0");
        creategiffromgallery.setImageResource(R.drawable.img_1);
        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isfirstrun", true);

        if (isFirstRun != true) {
            filterbar.setVisibility(View.INVISIBLE);
            photo = 0;
        } else {
            filterbar.setVisibility(View.VISIBLE);
        }
        if (settings_state == 0) {
           // i = 0;
          //  captured_image.clear();
            captureImage.setVisibility(View.VISIBLE);
            creategif.setVisibility(View.VISIBLE);
            creategiffromgallery.setVisibility(View.VISIBLE);
            reset.setVisibility(View.VISIBLE);
            toggle.setImageResource(R.drawable.toggle_gif);


        } else if (settings_state == 1) {
            i = 0;
            filter_state = 0;
            reset.setVisibility(View.INVISIBLE);
            creategif.setImageResource(R.drawable.photo);

        }

        if (getIntent().getExtras() != null) {
            Version = getIntent().getExtras().getInt("version");

            if (Version == 1) {
                Version = 0;
                alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Do you want to install the new version?");
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.fsd.quickgif")));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.fsd.quickgif")));
                        }
                    }
                });

                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        }
    }


    @Override
    protected void onPause() {
        mCamera.onPause();
        super.onPause();
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {

            case R.id.button_capture:
                   if(i<=5) {
                       capture();
                   }
                else
                   {
                       Toast.makeText(this,"Sorry,you can't take more than 5 pictures",Toast.LENGTH_SHORT).show();
                   }
                break;

            case R.id.img_switch_camera:
                if (flipstatus == 0) {
                    flip.setImageResource(R.drawable.flip);
                    flipstatus = 1;
                } else {
                    flip.setImageResource(R.drawable.flip_1);
                    flipstatus = 0;
                }

                mCamera.switchCamera();
                break;

            case R.id.flash:

                Parameters p = mCamera.mCameraInstance.getParameters();
                List<String> focusModes = p.getSupportedFlashModes();
                Log.e("Activity", "FlashModes " + focusModes);
                Log.e("Activity", "FlashStatus " + flashstatus);
                if (focusModes != null) {
                    if (flashstatus == 0) {
                        flashCameraButton.setImageResource(R.drawable.flash);
                        p.setFlashMode(Parameters.FLASH_MODE_TORCH);
                        flashstatus = 1;
                    } else {
                        flashCameraButton.setImageResource(R.drawable.flash_1);
                        p.setFlashMode(Parameters.FLASH_MODE_OFF);
                        flashstatus = 0;
                    }
                    mCamera.mCameraInstance.setParameters(p);
                }
                break;
            case R.id.filter1:
                filterbutton.setImageResource(R.drawable.filter_icon_1);
                filterbar.setVisibility(View.INVISIBLE);
                mFilter = new GPUImageFilter();
                new_filter = 0;
                mGPUImage.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;
            case R.id.filter40:
                //  mGPUImage.setFilter(null);
                filterbutton.setImageResource(R.drawable.filter_icon_1);
                filterbar.setVisibility(View.INVISIBLE);
                new_filter = 0;
                mFilter = new GPUImageColorInvertFilter();
                mGPUImage.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                if (photo == 1) {
                    photo = 1;
                    mTourGuideHandler.cleanUp();

                    mTourGuideHandler = TourGuide.init(this).with(TourGuide.Technique.Click)
                            .setPointer(null) // set pointer to null
                            .setToolTip(new ToolTip().setDescription("Capture Image").setGravity(Gravity.TOP))
                            .setOverlay(new Overlay())
                            .playOn(captureImage);

                }
                break;
            case R.id.filter41:
                // mGPUImage.setFilter(null);
                filterbutton.setImageResource(R.drawable.filter_icon_1);
                filterbar.setVisibility(View.INVISIBLE);
                new_filter = 0;
                mFilter = new GPUImageHueFilter(90.0f);
                mGPUImage.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                if (photo == 1) {
                    photo = 2;
                    mTourGuideHandler.cleanUp();
                    mTourGuideHandler = TourGuide.init(this).with(TourGuide.Technique.Click)
                            .setPointer(null) // set pointer to null
                            .setToolTip(new ToolTip().setDescription("Capture Another Image").setGravity(Gravity.TOP))
                            .setOverlay(new Overlay())
                            .playOn(captureImage);

                }
                break;
            case R.id.filter4:

                filterbutton.setImageResource(R.drawable.filter_icon_1);
                filterbar.setVisibility(View.INVISIBLE);
                new_filter = 0;
                mFilter = new MagicSketchFilter(this);
                mGPUImage.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                if (photo == 2) {
                    photo = 3;
                    mTourGuideHandler.cleanUp();
                    mTourGuideHandler = TourGuide.init(this).with(TourGuide.Technique.Click)
                            .setPointer(null) // set pointer to null
                            .setToolTip(new ToolTip().setDescription("Capture Another Image").setGravity(Gravity.TOP))
                            .setOverlay(new Overlay())
                            .playOn(captureImage);

                }

                break;

            case R.id.filter5:
                filterbutton.setImageResource(R.drawable.filter_icon_1);
                filterbar.setVisibility(View.INVISIBLE);
                new_filter = 0;
                //mGPUImage.setFilter(null);
                //     mFilter =  new GPUImageBrightnessFilter(1.5f);
                mFilter = new MagicBlackCatFilter(this);
                mGPUImage.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);


                break;

            case R.id.filter6:
                filterbutton.setImageResource(R.drawable.filter_icon_1);
                filterbar.setVisibility(View.INVISIBLE);
                new_filter = 0;
                //mGPUImage.setFilter(null);
                mFilter = new GPUImageSepiaFilter();
                mGPUImage.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter7:
                filterbutton.setImageResource(R.drawable.filter_icon_1);
                filterbar.setVisibility(View.INVISIBLE);
                new_filter = 0;
                //mGPUImage.setFilter(null);
                mFilter = new GPUImageGrayscaleFilter();
                mGPUImage.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter8:
                filterbutton.setImageResource(R.drawable.filter_icon_1);
                filterbar.setVisibility(View.INVISIBLE);
                new_filter = 0;
                //mGPUImage.setFilter(null);
                GPUImageSharpenFilter sharpness = new GPUImageSharpenFilter();
                sharpness.setSharpness(2.0f);
                mFilter = sharpness;
                mGPUImage.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;
            case R.id.filter9:
                filterbutton.setImageResource(R.drawable.filter_icon_1);
                filterbar.setVisibility(View.INVISIBLE);
                new_filter = 0;
                //mGPUImage.setFilter(null);
                GPUImage3x3ConvolutionFilter convolution = new GPUImage3x3ConvolutionFilter();
                convolution.setConvolutionKernel(new float[]{
                        -1.0f, 0.0f, 1.0f,
                        -2.0f, 0.0f, 2.0f,
                        -1.0f, 0.0f, 1.0f
                });
                mFilter = convolution;
                mGPUImage.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;
            case R.id.filter10:
                filterbutton.setImageResource(R.drawable.filter_icon_1);
                filterbar.setVisibility(View.INVISIBLE);
                new_filter = 0;
                //mGPUImage.setFilter(null);
                mFilter = new GPUImageEmbossFilter();
                mGPUImage.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter11:
                filterbutton.setImageResource(R.drawable.filter_icon_1);
                filterbar.setVisibility(View.INVISIBLE);
                new_filter = 0;
                //mGPUImage.setFilter(null);
                mFilter = new GPUImagePosterizeFilter();
                mGPUImage.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter12:
                filterbutton.setImageResource(R.drawable.filter_icon_1);
                filterbar.setVisibility(View.INVISIBLE);
                new_filter = 0;
                List<GPUImageFilter> filters = new LinkedList<GPUImageFilter>();
                filters.add(new GPUImageContrastFilter());
                filters.add(new GPUImageDirectionalSobelEdgeDetectionFilter());
                filters.add(new GPUImageGrayscaleFilter());

                mFilter = new GPUImageFilterGroup(filters);
                mGPUImage.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter13:
                filterbutton.setImageResource(R.drawable.filter_icon_1);
                filterbar.setVisibility(View.INVISIBLE);
                new_filter = 0;
                mFilter = new GPUImageSaturationFilter(1.0f);
                mGPUImage.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter14:
                filterbutton.setImageResource(R.drawable.filter_icon_1);
                filterbar.setVisibility(View.INVISIBLE);
                new_filter = 0;
                //    mFilter =   new GPUImageExposureFilter(0.0f);
                mFilter = new MagicAntiqueFilter(this);
                mGPUImage.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter15:
                filterbutton.setImageResource(R.drawable.filter_icon_1);
                filterbar.setVisibility(View.INVISIBLE);
                new_filter = 0;
                mFilter = new GPUImageHighlightShadowFilter(0.0f, 1.0f);
                mGPUImage.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;
            case R.id.filter16:
                filterbutton.setImageResource(R.drawable.filter_icon_1);
                filterbar.setVisibility(View.INVISIBLE);
                new_filter = 0;
                mFilter = new GPUImageMonochromeFilter(1.0f, new float[]{0.6f, 0.45f, 0.3f, 1.0f});
                mGPUImage.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter17:
                filterbutton.setImageResource(R.drawable.filter_icon_1);
                filterbar.setVisibility(View.INVISIBLE);
                new_filter = 0;
                mFilter = new MagicBeautyFilter(this);
                mGPUImage.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter18:
                filterbutton.setImageResource(R.drawable.filter_icon_1);
                filterbar.setVisibility(View.INVISIBLE);
                new_filter = 0;
                mFilter = new MagicCoolFilter(this);
                mGPUImage.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter19:
                filterbutton.setImageResource(R.drawable.filter_icon_1);
                filterbar.setVisibility(View.INVISIBLE);
                new_filter = 0;
                mFilter = new GPUImageWhiteBalanceFilter(5000.0f, 0.0f);
                mGPUImage.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter20:
                filterbutton.setImageResource(R.drawable.filter_icon_1);
                filterbar.setVisibility(View.INVISIBLE);
                new_filter = 0;
                PointF centerPoint = new PointF();
                centerPoint.x = 0.5f;
                centerPoint.y = 0.5f;
                mFilter = new GPUImageVignetteFilter(centerPoint, new float[]{0.0f, 0.0f, 0.0f}, 0.3f, 0.75f);
                mGPUImage.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;
            case R.id.filter21:
                filterbutton.setImageResource(R.drawable.filter_icon_1);
                filterbar.setVisibility(View.INVISIBLE);
                new_filter = 0;
                mFilter = new GPUImageGaussianBlurFilter();
                mGPUImage.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;
            case R.id.filter22:
                filterbutton.setImageResource(R.drawable.filter_icon_1);
                filterbar.setVisibility(View.INVISIBLE);
                new_filter = 0;
                mFilter = new GPUImageCrosshatchFilter();
                mGPUImage.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter23:
                filterbutton.setImageResource(R.drawable.filter_icon_1);
                filterbar.setVisibility(View.INVISIBLE);
                new_filter = 0;
                mFilter = new GPUImageBoxBlurFilter();
                mGPUImage.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter24:
                filterbutton.setImageResource(R.drawable.filter_icon_1);
                filterbar.setVisibility(View.INVISIBLE);
                new_filter = 0;
                mFilter = new GPUImageCGAColorspaceFilter();
                mGPUImage.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter25:
                filterbutton.setImageResource(R.drawable.filter_icon_1);
                filterbar.setVisibility(View.INVISIBLE);
                new_filter = 0;
                mFilter = new GPUImageDilationFilter();
                mGPUImage.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter26:
                filterbutton.setImageResource(R.drawable.filter_icon_1);
                filterbar.setVisibility(View.INVISIBLE);
                new_filter = 0;
                mFilter = new MagicEmeraldFilter(this);
                mGPUImage.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter27:
                filterbutton.setImageResource(R.drawable.filter_icon_1);
                filterbar.setVisibility(View.INVISIBLE);
                new_filter = 0;
                mFilter = new GPUImageBulgeDistortionFilter();
                mGPUImage.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;
            case R.id.filter28:
                filterbutton.setImageResource(R.drawable.filter_icon_1);
                filterbar.setVisibility(View.INVISIBLE);
                new_filter = 0;
                mFilter = new GPUImageGlassSphereFilter();
                mGPUImage.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter29:
                filterbutton.setImageResource(R.drawable.filter_icon_1);
                filterbar.setVisibility(View.INVISIBLE);
                new_filter = 0;
                mFilter = new GPUImageHazeFilter();
                mGPUImage.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter30:
                filterbutton.setImageResource(R.drawable.filter_icon_1);
                filterbar.setVisibility(View.INVISIBLE);
                new_filter = 0;
                mFilter = new GPUImageLaplacianFilter();
                mGPUImage.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter31:
                filterbutton.setImageResource(R.drawable.filter_icon_1);
                filterbar.setVisibility(View.INVISIBLE);
                new_filter = 0;
                mFilter = new GPUImageSphereRefractionFilter();
                mGPUImage.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter32:
                filterbutton.setImageResource(R.drawable.filter_icon_1);
                filterbar.setVisibility(View.INVISIBLE);
                new_filter = 0;
                mFilter = new GPUImageWeakPixelInclusionFilter();
                mGPUImage.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter33:
                filterbutton.setImageResource(R.drawable.filter_icon_1);
                filterbar.setVisibility(View.INVISIBLE);
                new_filter = 0;
                mFilter = new GPUImageFalseColorFilter();
                mGPUImage.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter34:
                filterbutton.setImageResource(R.drawable.filter_icon_1);
                filterbar.setVisibility(View.INVISIBLE);
                new_filter = 0;
                GPUImageLevelsFilter levelsFilter = new GPUImageLevelsFilter();
                levelsFilter.setMin(0.0f, 3.0f, 1.0f);
                mFilter = levelsFilter;
                mGPUImage.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter37:
                filterbutton.setImageResource(R.drawable.filter_icon_1);
                filterbar.setVisibility(View.INVISIBLE);
                new_filter = 0;
                mFilter = new MagiSkinWhitenFilter(this);

                mGPUImage.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;


            case R.id.filter43:
                filterbutton.setImageResource(R.drawable.filter_icon_1);
                filterbar.setVisibility(View.INVISIBLE);
                new_filter = 0;
                mFilter = new MagicLatteFilter(this);
                mGPUImage.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;


            case R.id.filter42:
                filterbutton.setImageResource(R.drawable.filter_icon_1);
                filterbar.setVisibility(View.INVISIBLE);

                mFilter = new MagicLomoFilter(this);

               /* green_state=0;
                new_filter = 1;
                mFilter = new MagicWarmFilter(green_state);
                green_state=2;*/
                //   mFilter = new MagicFairytaleFilter();
                mGPUImage.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;


        }
    }

    private void takePicture() {
        // TODO get a size that is about the size of the screen
        final Camera.Parameters params = mCamera.mCameraInstance.getParameters();

        if (flashstatus == 0) {
            flashCameraButton.setImageResource(R.drawable.flash_1);
            params.setFlashMode(Parameters.FLASH_MODE_OFF);


        } else {
            flashCameraButton.setImageResource(R.drawable.flash_1);
            params.setFlashMode(Parameters.FLASH_MODE_ON);

        }

        mCamera.mCameraInstance.setParameters(params);

        final int id = (mCamera.mCurrentCameraId == CameraInfo.CAMERA_FACING_FRONT ? 1 : 0);
        Log.e("Activity", "Camera ID " + id);
        for (Camera.Size size : params.getSupportedPictureSizes()) {
            Log.i("ASDF", "Supported: " + size.width + "x" + size.height);
        }

        try {
            mCamera.mCameraInstance.takePicture(new Camera.ShutterCallback() {
                @Override
                public void onShutter() {
                    if (flashstatus == 1) {
                        Camera.Parameters camera = mCamera.mCameraInstance.getParameters();
                        camera.setFlashMode(Parameters.FLASH_MODE_ON);
                        mCamera.mCameraInstance.setParameters(camera);

                    }
                }
            }, null, new Camera.PictureCallback() {

                @Override
                public void onPictureTaken(byte[] data, final Camera camera) {


                    if (settings_state == 0)
                    {
                        i = i + 1;
                        counterText.setText(String.valueOf(i));
                        if (i < 5)
                        {
                            if (i > 2) {
                                Thread myThread = new Thread(new DownloadImagesThread(data, id));
                                Log.e("Activity", "Thread Started");
                                myThread.start();
                                final GLSurfaceView view = (GLSurfaceView) findViewById(R.id.surfaceView);
                                //   camera.stopPreview();
                                view.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
                                camera.startPreview();
                                view.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
                                creategif.setVisibility(View.VISIBLE);
                                Log.e("Activity", "photo count " + photo);
                                if (photo == 3) {
                                    filterbar.setVisibility(View.VISIBLE);
                                    mTourGuideHandler = TourGuide.init(ActivityCamera.this).with(TourGuide.Technique.Click)
                                            .setPointer(null) // set pointer to null
                                            .setToolTip(new ToolTip().setDescription("Create Gif"))
                                            .setOverlay(new Overlay())
                                            .playOn(creategif);
                                    getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isfirstrun", false).commit();
                                    filterbar.setEnableScrolling(true);
                                }

                            } else {
                                Thread myThread = new Thread(new DownloadImagesThread(data, id));

                                myThread.start();

                                final GLSurfaceView view = (GLSurfaceView) findViewById(R.id.surfaceView);
                                //    camera.stopPreview();
                                view.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
                                camera.startPreview();
                                view.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
                                if (photo == 1) {
                                    filterbar.setVisibility(View.VISIBLE);
                                    mTourGuideHandler = TourGuide.init(ActivityCamera.this).with(TourGuide.Technique.Click)
                                            .setPointer(null) // set pointer to null
                                            .setToolTip(new ToolTip().setDescription("Select another filter"))
                                            .setOverlay(new Overlay())
                                            .playOn(button3);
                                }
                                if (photo == 2) {
                                    filterbar.setVisibility(View.VISIBLE);
                                    mTourGuideHandler = TourGuide.init(ActivityCamera.this).with(TourGuide.Technique.Click)
                                            .setPointer(null) // set pointer to null
                                            .setToolTip(new ToolTip().setDescription("Select another filter"))
                                            .setOverlay(new Overlay())
                                            .playOn(button4);
                                }
                            }
                        } else {
                            Thread myThread = new Thread(new DownloadImagesThread(data, id));
                            myThread.start();
                            captureImage.setEnabled(false);
                            final GLSurfaceView view = (GLSurfaceView) findViewById(R.id.surfaceView);
                            //  camera.stopPreview();
                            view.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
                            camera.startPreview();
                            view.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
                            Toast.makeText(ActivityCamera.this, "You can't take more than 5 pictures", Toast.LENGTH_LONG).show();
                        }


                    } else {


                        filter_state = 0;
                        //  }
                        Thread myThread = new Thread(new DownloadImagesThread(data, id));
                        Log.e("Activity", "Thread Started");
                        myThread.start();
                        final GLSurfaceView view = (GLSurfaceView) findViewById(R.id.surfaceView);
                        view.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
                        camera.startPreview();
                        view.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

                    }
                }


            });
        } catch (Exception e) {
            Log.e("Activity", "Exception " + e);
        }

    }
/*
    private void switchFilterTo(final GPUImageFilter filter) {
        if (mFilter == null
                || (filter != null && !mFilter.getClass().equals(filter.getClass()))) {
            mFilter = filter;
            mGPUImage.setFilter(mFilter);
            mFilterAdjuster = new FilterAdjuster(mFilter);
        }
    }*/

    @Override
    public void onProgressChanged(final SeekBar seekBar, final int progress,
                                  final boolean fromUser) {
        if (mFilterAdjuster != null) {
            mFilterAdjuster.adjust(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(final SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(final SeekBar seekBar) {
    }

    @Override
    public void onPickedSuccessfully(ArrayList<ImageEntry> images) {
        //  mSelectedImages = images;
        mSelectedImages = new ArrayList<String>();
        Intent i = new Intent(ActivityCamera.this, GIFGallery.class);
        for (int position = 0; position < images.size(); position++) {
            final String path = images.get(position).path;
            mSelectedImages.add(path);
        }
        i.putExtra("arraylist2", mSelectedImages);
        startActivity(i);
    }

    @Override
    public void onCancel() {

    }


    private class CameraLoader {

        private int mCurrentCameraId = 0;
        private Camera mCameraInstance;

        public void onCreate() {
            setUpCamera(mCurrentCameraId);


        }

        public void onPause() {
            releaseCamera();
        }

        public void switchCamera() {
            releaseCamera();
           /* if(new_filter==1) {
                if (MagicParams.front == 0) {
                    MagicParams.front = 1;
                    green_state = 0;
                    mFilter = new MagicWarmFilter(green_state);

                } else {
                    MagicParams.front = 0;
                    green_state = 0;
                    mFilter = new MagicWarmFilter(green_state);
                }
            }
            else
            {
                if(MagicParams.front==0)
                    MagicParams.front=1;
                else
                    MagicParams.front=0;
            }*/
            mGPUImage.deleteImage();
            mCurrentCameraId = (mCurrentCameraId + 1) % mCameraHelper.getNumberOfCameras();
            setUpCamera(mCurrentCameraId);
        }

        private void setUpCamera(final int id) {
            mCameraInstance = getCameraInstance(id);
            Camera.Parameters parameters = mCameraInstance.getParameters();
            // TODO adjust by getting supportedPreviewSizes and then choosing
            // the best one for screen size (best fill screen)
            parameters.setPictureSize(640, 480);

            showFlashButton(parameters);
            //   parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
            //cam.setParameters(p);
            mCameraInstance.setParameters(parameters);


            int orientation = mCameraHelper.getCameraDisplayOrientation(
                    ActivityCamera.this, mCurrentCameraId);
            CameraInfo2 cameraInfo = new CameraInfo2();
            mCameraHelper.getCameraInfo(mCurrentCameraId, cameraInfo);
            boolean flipHorizontal = cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT;
            final GLSurfaceView view = (GLSurfaceView) findViewById(R.id.surfaceView);
            view.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
            mGPUImage.setUpCamera(mCameraInstance, orientation, flipHorizontal, false);

            view.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
            mGPUImage.setFilter(mFilter);

        }

        /**
         * A safe way to get an instance of the Camera object.
         */
        private Camera getCameraInstance(final int id) {
            Camera c = null;
            try {
                c = mCameraHelper.openCamera(id);
            } catch (Exception e) {
                FirebaseCrash.logcat(Log.ERROR, "Quick GIF", "Error caught");
                FirebaseCrash.report(e);
                e.printStackTrace();
            }
            return c;
        }

        private void releaseCamera() {
            mCameraInstance.setPreviewCallback(null);
            mCameraInstance.release();
            mCameraInstance = null;
        }

        private void showFlashButton(Parameters params) {
            boolean showFlash = (getPackageManager().hasSystemFeature(
                    PackageManager.FEATURE_CAMERA_FLASH) && params.getFlashMode() != null)
                    && params.getSupportedFlashModes() != null
                    && params.getSupportedFocusModes().size() > 1;

            flashCameraButton.setVisibility(showFlash ? View.VISIBLE
                    : View.INVISIBLE);

        }

    }


    public void downloadImages(byte[] data, int id) throws IOException {
        final File finalfile;
        //  Bitmap downloadbitmap1;
        Bitmap loadedImage = null;
        // final File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
        loadedImage = BitmapFactory.decodeByteArray(data, 0,
                data.length);
        String folder_main = "Quick GIF";
        Log.e("Activity", "Green ID0 " + green_state);
      /*  if(green_state==2)
        {
            // mFilter=new GPUImageFilter();
            mFilter= new MagicWarmFilter(green_state);
            //  MagicWarmFilter.green_state=1;
            //   mFilter= new MagicWarmFilter(green_state);
        }*/
        Log.e("Activity", "Green ID0 " + green_state);
        File f = new File(Environment.getExternalStorageDirectory(), folder_main);
        if (!f.exists()) {
            f.mkdirs();
        }

        if (loadedImage == null) {
            Log.d("ASDF",
                    "Error creating media file, check storage permissions");
            return;
        }
   /*if(mFilter==null) {
       editPictureFromGallery.setText("Edit Last Picture");
      */

        if (id == 1)          //for front camera image

        {


            float[] mirrorY = {-1, 0, 0, 0, 1, 0, 0, 0, 1};
            Matrix matrix1 = new Matrix();
            Matrix matrixMirrorY = new Matrix();

            matrixMirrorY.setValues(mirrorY);

            matrix1.postConcat(matrixMirrorY);
            Matrix rotateMatrix = new Matrix();


            // id = (mCamera.mCurrentCameraId == CameraInfo.CAMERA_FACING_FRONT ? 1 : 0);
            Log.e("Activity", "front camera id " + id);
            if (id == 1) {
                // rotateMatrix.postConcat(matrixMirrorY);
                rotateMatrix.postRotate(90);
            }


            Bitmap rotatedBitmap = Bitmap.createBitmap(loadedImage, 0, 0,
                    loadedImage.getWidth(), loadedImage.getHeight(),
                    rotateMatrix, false);


            java.util.Date date = new java.util.Date();
            String fileName = "Image" + new Timestamp(date.getTime()).toString() + ".jpg";


            finalfile = new File(Environment
                    .getExternalStorageDirectory(), "Quick GIF" + File.separator + fileName);
            GPUImage1 = new GPUImage(this);
            GPUImage1.setImage(rotatedBitmap);
         /*   MagicWarmFilter1.green_state=1;
            mFilter1= new MagicWarmFilter1(green_state);*/
            GPUImage1.setFilter(mFilter1);

            if (new_filter == 0) {
                downloadbitmap1 = mGPUImage.getBitmapWithFilterApplied(rotatedBitmap);
            } else {    /*   Matrix rotateMatrix1 = new Matrix();
                rotateMatrix1.postRotate(180);
                Bitmap bitmap=Bitmap.createBitmap(rotatedBitmap,0, 0,
                        rotatedBitmap.getWidth(), rotatedBitmap.getHeight(),
                        rotateMatrix1, false);
                */
                float[] mirrorY1 = {-1, 0, 0, 0, 1, 0, 0, 0, 1};

                Matrix matrixMirrorY1 = new Matrix();
                Matrix matrix2 = new Matrix();
                matrixMirrorY1.setValues(mirrorY1);

                matrix2.postConcat(matrixMirrorY1);
                Bitmap bitmap = Bitmap.createBitmap(rotatedBitmap, 0, 0,
                        rotatedBitmap.getWidth(), rotatedBitmap.getHeight(),
                        matrix2, false);

                Matrix rotateMatrix1 = new Matrix();
                rotateMatrix1.postRotate(180);
                Bitmap bitmap1 = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(),
                        rotateMatrix1, false);
                downloadbitmap1 = GPUImage1.getBitmapWithFilterApplied(bitmap1);
            }
            FileOutputStream out = new FileOutputStream(finalfile);
            downloadbitmap1.compress(Bitmap.CompressFormat.JPEG, 90, out);

            out.flush();
            out.close();
            Bitmap bitmap = BitmapFactory.decodeFile(finalfile.toString());


            int w = bitmap.getWidth();
            int h = bitmap.getHeight();

            final Bitmap result = Bitmap.createBitmap(w, h, bitmap.getConfig());
            Canvas canvas = new Canvas(result);
            canvas.drawBitmap(bitmap, 0, 0, null);


            int ww = Math.round((float) (bitmap.getWidth() * 0.25));

            int hh = Math.round((float) (bitmap.getHeight() * 0.15));


            Bitmap waterMark = BitmapFactory.decodeResource(getResources(), R.drawable.watermark);
            Bitmap watermark1 = Bitmap.createScaledBitmap(waterMark, ww, hh, true);
            canvas.drawBitmap(watermark1, w - ww - 5, h - hh - 5, null);
            finalfile.delete();
            File finalfile1 = new File(Environment
                    .getExternalStorageDirectory(), "Quick GIF" + File.separator + "Image" + System.currentTimeMillis() + ".jpg");
            FileOutputStream out1 = new FileOutputStream(finalfile1);
            result.compress(Bitmap.CompressFormat.JPEG, 90, out1);

            out1.flush();
            out1.close();
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(finalfile1));
            this.sendBroadcast(mediaScanIntent);


            captured_image.add(finalfile1);
            Log.e("Activity", "File path " + finalfile);
            editFile = finalfile1;
        } else {

            if (loadedImage.getWidth() > loadedImage.getHeight()) {

                Matrix rotateMatrix = new Matrix();
                rotateMatrix.postRotate(90);
                Bitmap rotatedBitmap = Bitmap.createBitmap(loadedImage, 0, 0,
                        loadedImage.getWidth(), loadedImage.getHeight(),
                        rotateMatrix, false);

                java.util.Date date = new java.util.Date();
                String fileName = "Image" + new Timestamp(date.getTime()).toString() + ".jpg";
                finalfile = new File(Environment
                        .getExternalStorageDirectory(), "Quick GIF" + File.separator + fileName);
                FileOutputStream out = new FileOutputStream(finalfile);
                GPUImage1 = new GPUImage(this);
                GPUImage1.setImage(rotatedBitmap);
             /*   MagicWarmFilter1.green_state=1;
                mFilter1= new MagicWarmFilter1(green_state);*/
                GPUImage1.setFilter(mFilter1);

                if (new_filter == 0) {
                    downloadbitmap1 = mGPUImage.getBitmapWithFilterApplied(rotatedBitmap);
                } else {
                    downloadbitmap1 = GPUImage1.getBitmapWithFilterApplied(rotatedBitmap);
                }
                downloadbitmap1.compress(Bitmap.CompressFormat.JPEG, 90, out);

                out.flush();
                out.close();

            } else {

                java.util.Date date = new java.util.Date();
                String fileName = "Image" + new Timestamp(date.getTime()).toString() + ".jpg";
                finalfile = new File(Environment
                        .getExternalStorageDirectory(), "Quick GIF" + File.separator + fileName);
                FileOutputStream out = new FileOutputStream(finalfile);
                GPUImage1 = new GPUImage(this);
                GPUImage1.setImage(loadedImage);
              /*  MagicWarmFilter1.green_state=1;
                mFilter1= new MagicWarmFilter1(green_state);*/
                GPUImage1.setFilter(mFilter1);

                if (new_filter == 0) {
                    downloadbitmap1 = mGPUImage.getBitmapWithFilterApplied(loadedImage);
                } else {
                    downloadbitmap1 = GPUImage1.getBitmapWithFilterApplied(loadedImage);
                }
                downloadbitmap1.compress(Bitmap.CompressFormat.JPEG, 90, out);

                out.flush();
                out.close();


            }

            Bitmap bitmap = BitmapFactory.decodeFile(finalfile.toString());

            int w = bitmap.getWidth();
            int h = bitmap.getHeight();

            final Bitmap result = Bitmap.createBitmap(w, h, bitmap.getConfig());
            Canvas canvas = new Canvas(result);
            canvas.drawBitmap(bitmap, 0, 0, null);

            int ww = Math.round((float) (bitmap.getWidth() * 0.25));

            int hh = Math.round((float) (bitmap.getHeight() * 0.15));
            Bitmap waterMark = BitmapFactory.decodeResource(getResources(), R.drawable.watermark);
            Bitmap watermark1 = Bitmap.createScaledBitmap(waterMark, ww, hh, true);
            canvas.drawBitmap(watermark1, w - ww - 5, h - hh - 5, null);
            finalfile.delete();
            File finalfile1 = new File(Environment
                    .getExternalStorageDirectory(), "Quick GIF" + File.separator + "Image" + System.currentTimeMillis() + ".jpg");
            FileOutputStream out1 = new FileOutputStream(finalfile1);
            result.compress(Bitmap.CompressFormat.JPEG, 90, out1);

            out1.flush();
            out1.close();
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(finalfile1));
            this.sendBroadcast(mediaScanIntent);
            captured_image.add(finalfile1);
            Log.e("Activity", "File path " + finalfile);
            editFile = finalfile1;
        }


        ActivityCamera.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                captureImage.setEnabled(true);
                creategiffromgallery.setVisibility(View.VISIBLE);
                if (flashstatus == 0) {
                    Camera.Parameters p = mCamera.mCameraInstance.getParameters();
                    p.setFlashMode(Parameters.FLASH_MODE_OFF);


                    mCamera.mCameraInstance.setParameters(p);

                    flashCameraButton.setImageResource(R.drawable.flash_1);
                } else {
                    Camera.Parameters p = mCamera.mCameraInstance.getParameters();
                    p.setFlashMode(Parameters.FLASH_MODE_TORCH);


                    mCamera.mCameraInstance.setParameters(p);

                    flashCameraButton.setImageResource(R.drawable.flash);
                }
            }
        });


        //    MagicWarmFilter.green_state=0;
    }

    private class DownloadImagesThread implements Runnable {
        byte[] data2;
        int id1;

        // GPUImageFilter iFilter;
        DownloadImagesThread(byte[] data, int id) {
            data2 = data;
            id1 = id;
        }

        @Override
        public void run() {
          /*  Log.e("Activity", "Inside Thread " + mfile);
            Log.e("Activity", "Inside Thread " + iFilter);
          */
            try {
                downloadImages(data2, id1);
            } catch (IOException e) {
                FirebaseCrash.logcat(Log.ERROR, "Quick GIF", "Error caught");
                FirebaseCrash.report(e);
                e.printStackTrace();
            }
        }
    }

    public void gif() {
        if (settings_state == 0)
            if (i < 3) {
                Toast.makeText(this, "You need atleast 3 pics to create gif", Toast.LENGTH_SHORT).show();
            } else {
                status = 2;
                Intent i = new Intent(ActivityCamera.this, GIFGallery.class);
                i.putExtra("arraylist", captured_image);
                if (photo == 3) {
                    mTourGuideHandler.cleanUp();
                }
                startActivity(i);
            }
        else {
            if (i == 0) {
                Toast.makeText(this.getApplicationContext(), "Please take a picture first", Toast.LENGTH_SHORT).show();
            } else {
                filter_state = 1;
                Intent intent = new Intent(this, ActivityGallery.class);
                Log.e("Activity", "EDIT FILE " + editFile);
                intent.putExtra("editfile", editFile);
                startActivity(intent);
            }

        }
    }

    public void create_gif_gallery(View v) {

        creategiffromgallery.setImageResource(R.drawable.img);
        if (settings_state == 1) {
            filter_state = 0;
            startActivity(new Intent(this, ActivityGallery.class));
        } else

        {
            status = 0;

            new Picker.Builder(this, this, R.style.MIP_theme)
                    .setPickMode(Picker.PickMode.MULTIPLE_IMAGES)
                    .setLimit(5)
                    .build()
                    .startActivity();

        }
    }

    public void toggle(View v) {
        if (settings_state == 0) {
            if (i > 0) {
                Popup2 pop = new Popup2(this);
                pop.show();
            } else {
                settings_state = 1;
                i = 0;
                toggle.setImageResource(R.drawable.toggle_photo);
                reset.setVisibility(View.INVISIBLE);
                creategif.setImageResource(R.drawable.photo);
                counterText.setText("0");
                counterlayout.setVisibility(View.INVISIBLE);
                captured_image.clear();
            }
        } else {
            i = 0;
            settings_state = 0;
            counterText.setText("0");
            toggle.setImageResource(R.drawable.toggle_gif);
            reset.setVisibility(View.VISIBLE);
            creategif.setImageResource(R.drawable.gif);
            counterlayout.setVisibility(View.VISIBLE);
            captured_image.clear();
        }
    }

    public void gif_gallery(View v) {
        gif();

    }

    public void reset_gallery(View v) {
        if (i > 0) {

            reset.setImageResource(R.drawable.reset);
            Popup1 alert = new Popup1(this);
            alert.show();
        }

        mCamera.mCameraInstance.startPreview();

    }

    public void filter_bar(View view) {
        if (filterbar.getVisibility() == View.INVISIBLE) {
            filterbutton.setImageResource(R.drawable.filter_icon);
            filterbar.setVisibility(View.VISIBLE);
        } else {
            filterbutton.setImageResource(R.drawable.filter_icon_1);
            filterbar.setVisibility(View.INVISIBLE);
        }
    }

    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Log.d("CDA", "onKeyDown Called");
            onBackPressed();
            return true;
        }

        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_HOME
                && event.getRepeatCount() == 0) {
            Log.d("CDA", "onKeyDown Called");
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        *//*Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);*//*
        if (settings_state == 0 && captured_image.size() > 0) {
            final Dialog dialog = new Dialog(this, R.style.Dialog);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            dialog.setContentView(R.layout.popup2);

            TextView text = (TextView)dialog.findViewById(R.id.textView1);
            text.setText("All your images for gif will be deleted. DO you want to delete all your images?");
            ImageView notification_ok = (ImageView) dialog.findViewById(R.id.imageView3);

            //   ImageView notification_ok = (ImageView) dialog.findViewById(R.id.imageView4);
            // notification_ok.setImageURI(Uri.parse(url));
            notification_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
               *//* try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + pkg)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + pkg)));
                }*//*
                    if (dialog.isShowing())
                        dialog.dismiss();

                }
            });


            ImageView notification_close = (ImageView) dialog.findViewById(R.id.imageView4);
            notification_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (dialog.isShowing())
                        dialog.dismiss();
                }
            });

            ImageView notification_delete = (ImageView) dialog.findViewById(R.id.imageView5);
            notification_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  i = 0;
                  counterText.setText("0");
                  captured_image.clear();

                  creategif.setVisibility(View.VISIBLE);

                    //   ActivityCamera.captureImage.setVisibility(View.VISIBLE);
                  captureImage.setVisibility(View.VISIBLE);
                  captureImage.setEnabled(true);
                  reset.setImageResource(R.drawable.reset_1);
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Intent i = new Intent(ActivityCamera.this, MainScreen.class);

                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);


                }
            });
            if (!dialog.isShowing())
                dialog.show();

        } else

        {
            Intent i = new Intent(this, MainScreen.class);

            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
    }
*/
}
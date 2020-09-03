package com.example.saman.gifimage;
import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImage3x3ConvolutionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageBilateralFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageBoxBlurFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageBulgeDistortionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageCGAColorspaceFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageColorInvertFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageCrosshatchFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageDilationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageDirectionalSobelEdgeDetectionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageEmbossFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageExposureFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFalseColorFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilterGroup;
import jp.co.cyberagent.android.gpuimage.GPUImageGaussianBlurFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGlassSphereFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGrayscaleFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHazeFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHighlightShadowFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHueFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageLaplacianFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageLevelsFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageMonochromeFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageOpacityFilter;
import jp.co.cyberagent.android.gpuimage.GPUImagePosterizeFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageRGBDilationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSaturationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSepiaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSharpenFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSphereRefractionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageTransformFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageVignetteFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageWeakPixelInclusionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageWhiteBalanceFilter;
import tourguide.tourguide.Overlay;
import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;
//import jp.co.cyberagent.android.gpuimage.GPUImageView;
//import jp.co.cyberagent.android.gpuimage.GPUImageView;
//import jp.co.cyberagent.android.gpuimage.GPUImageView.OnPictureSavedListener;
import com.example.saman.gifimage.filters.GPUImageFilterTools;
import com.example.saman.gifimage.filters.GPUImageFilterTools.OnGpuImageFilterChosenListener;
import com.example.saman.gifimage.filters.GPUImageFilterTools.FilterAdjuster;
import com.example.saman.gifimage.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

import com.example.saman.gifimage.filters.GPUImageView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.crash.FirebaseCrash;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
//import com.example.saman.gifimage.GPUImage.*;
import com.example.saman.gifimage.filters.*;

public class ActivityGallery extends Activity implements SeekBar.OnSeekBarChangeListener,
        View.OnClickListener, GPUImageView.OnPictureSavedListener {

    private static final int REQUEST_PICK_IMAGE = 1;
    public static GPUImageFilter mFilter;
    private FilterAdjuster mFilterAdjuster;
    public static GPUImageView mGPUImageView;
    public static Bitmap result;
    public static File edit_file;
    public static File final_file;
    PopupWindow1 alert;
    AdView mAdView;
    AdRequest adRequest;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_photo);
        mAdView = (AdView) findViewById(R.id.adView);
        adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        ((SeekBar) findViewById(R.id.seekBar)).setOnSeekBarChangeListener(this);
        mGPUImageView = (GPUImageView) findViewById(R.id.gpuimage);
         mFilter=new GPUImageFilter();

        if(ActivityCamera.filter_state==1)
        {
            edit_file = (File) getIntent().getExtras().get("editfile");
            Log.e("Activity", "EDIT FILE NAME " + edit_file);
            ActivityCamera.filter_state=0;
            try {
                handleImage(Uri.fromFile(edit_file));
            } catch (IOException e) {
                FirebaseCrash.logcat(Log.ERROR, "Quick GIF", "Error caught");
                FirebaseCrash.report(e);
                e.printStackTrace();
            }
            final_file=edit_file;
        }
        else {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, REQUEST_PICK_IMAGE);
        }

        findViewById(R.id.filter1).setOnClickListener(this);

        findViewById(R.id.filter4).setOnClickListener(this);
        findViewById(R.id.filter41).setOnClickListener(this);
        findViewById(R.id.filter5).setOnClickListener(this);
        findViewById(R.id.filter6).setOnClickListener(this);
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
        findViewById(R.id.filter40).setOnClickListener(this);

        findViewById(R.id.filter37).setOnClickListener(this);
        findViewById(R.id.filter42).setOnClickListener(this);
        findViewById(R.id.filter43).setOnClickListener(this);
        findViewById(R.id.seekBar).setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityCamera.activity=this;
    }



  /*  public void requestNewInterstitial() {
        Log.e("Activity", "Request Ad " );
        AdRequest adRequest = new AdRequest.Builder().build();
             *//*   .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();*//*

        mInterstitialAd.loadAd(adRequest);
    }*/
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        switch (requestCode) {
            case REQUEST_PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    //  data.

                    try {
                        handleImage(data.getData());
                        File edit_file1 = new File(String.valueOf(data.getData()));
                        Log.e("Activity", "Final FIle " + edit_file1);
                        edit_file = edit_file1;
                        final_file=edit_file;
                        Log.e("Activity", "Final FIle " + edit_file);
                        //   edit_file=new File(String.valueOf(data.getData()));
                    } catch (IOException e) {
                        FirebaseCrash.logcat(Log.ERROR, "Quick GIF", "Error caught");
                        FirebaseCrash.report(e);
                        e.printStackTrace();
                    }
                } else {
                    finish();
                }
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }


    @Override
    public void onPictureSaved(final Uri uri) {
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
    }

    public void save_image(View v) throws IOException {

        String fileName = "Image" + System.currentTimeMillis() + ".jpg";
        GPUImage  mGPUImage = new GPUImage(this);
        mGPUImage.setImage(result);
        mGPUImage.setFilter(mFilter);
        Bitmap bitmap1=mGPUImage.getBitmapWithFilterApplied(result);

        String folder_main = "Quick GIF";

        File f = new File(Environment.getExternalStorageDirectory(), folder_main);
        if (!f.exists()) {
            f.mkdirs();
        }
        File finalfile = new File(Environment
                .getExternalStorageDirectory(), "Quick GIF" + File.separator + fileName);
        FileOutputStream out = new FileOutputStream(finalfile);

        bitmap1.compress(Bitmap.CompressFormat.JPEG, 90, out);
        Log.e("Activity", "file name " + finalfile);
        out.flush();
        out.close();
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,Uri.fromFile(finalfile));
        this.sendBroadcast(mediaScanIntent);

        final_file=finalfile;

        alert = new PopupWindow1(this);

        alert.show();


        new android.os.Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {

                alert.dismiss();
            }
        }, 2000);

    }
    public void finish(View v)
    {
        this.finish();
    }


    @Override
    public void onProgressChanged(final SeekBar seekBar, final int progress, final boolean fromUser) {
        if (mFilterAdjuster != null) {
            mFilterAdjuster.adjust(progress);
        }
        mGPUImageView.requestRender();
    }

    @Override
    public void onStartTrackingTouch(final SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(final SeekBar seekBar) {
    }

    private void handleImage(final Uri selectedImage) throws IOException {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        //   edit_file= new File(selectedImage.getPath());
        result = MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImage);
        Log.e("Activity","edit_file from handle image " + edit_file);

        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
        // BitmapFactory.decode(selectedImage.getPath(), options);
        int imageHeight = bitmap.getHeight();
        int imageWidth = bitmap.getWidth();
        int bounding = dpToPx(300);
        float xScale = ((float) bounding) / imageWidth;
        float yScale = ((float) bounding) / imageHeight;
        float scale = (xScale <= yScale) ? xScale : yScale;
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, imageWidth, imageHeight, matrix, true);
        imageWidth = scaledBitmap.getWidth(); // re-use
        imageHeight = scaledBitmap.getHeight();

        android.view.ViewGroup.LayoutParams layoutParams = mGPUImageView.getLayoutParams();
        layoutParams.width = imageWidth;
        layoutParams.height = imageHeight;
        Log.e("Activity", "GPU image width and height " +layoutParams.width+ " " +   layoutParams.height);
        mGPUImageView.setLayoutParams(layoutParams);
        mGPUImageView.setImage(selectedImage);
    }

    private int dpToPx(int dp) {
        float density = getApplicationContext().getResources().getDisplayMetrics().density;
        return Math.round((float)dp * density);
    }

    public void shareit(View v)
    {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(final_file));
        shareIntent.setType("image/jpeg");
        startActivity(Intent.createChooser(shareIntent,"Share Picture to" ));
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.filter1:
                mFilter = new GPUImageFilter();

                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;
            case R.id.filter40:
                //  mGPUImage.setFilter(null);

                mFilter = new GPUImageColorInvertFilter();
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);

                break;
            case R.id.filter41:
                // mGPUImage.setFilter(null);

                mFilter = new GPUImageHueFilter(90.0f);
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);

                break;
            case R.id.filter4:

                mFilter = new MagicSketchFilter(this);
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);


                break;


            case R.id.filter5:

                mFilter =new MagicBlackCatFilter(this);
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);

                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);

                break;

            case R.id.filter6:

                mFilter =  new GPUImageSepiaFilter();
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);


                break;

            case R.id.filter7:

                mFilter =  new GPUImageGrayscaleFilter();
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter8:

                GPUImageSharpenFilter sharpness = new GPUImageSharpenFilter();
                sharpness.setSharpness(2.0f);
                mFilter =  sharpness;
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;
            case R.id.filter9:

                GPUImage3x3ConvolutionFilter convolution = new GPUImage3x3ConvolutionFilter();
                convolution.setConvolutionKernel(new float[]{
                        -1.0f, 0.0f, 1.0f,
                        -2.0f, 0.0f, 2.0f,
                        -1.0f, 0.0f, 1.0f
                });
                mFilter =  convolution;
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;
            case R.id.filter10:


                mFilter =  new GPUImageEmbossFilter();
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter11:
                mFilter =  new GPUImagePosterizeFilter();
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter12:

                List<GPUImageFilter> filters = new LinkedList<GPUImageFilter>();
                filters.add(new GPUImageContrastFilter());
                filters.add(new GPUImageDirectionalSobelEdgeDetectionFilter());
                filters.add(new GPUImageGrayscaleFilter());

                mFilter =   new GPUImageFilterGroup(filters);
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter13:

                mFilter =   new GPUImageSaturationFilter(1.0f);
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;
            case R.id.filter14:

                //    mFilter =   new GPUImageExposureFilter(0.0f);
                mFilter = new MagicAntiqueFilter(this);
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter15:

                mFilter =   new GPUImageHighlightShadowFilter(0.0f, 1.0f);
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;
            case R.id.filter16:

                mFilter =  new GPUImageMonochromeFilter(1.0f, new float[]{0.6f, 0.45f, 0.3f, 1.0f});
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter17:

                mFilter = new MagicBeautyFilter(this);
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter18:

                //   mFilter =   new GPUImageOpacityFilter(1.0f);
                mFilter = new MagicCoolFilter(this);
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter19:

                mFilter = new GPUImageWhiteBalanceFilter(5000.0f, 0.0f);
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;
            case R.id.filter20:

                PointF centerPoint = new PointF();
                centerPoint.x = 0.5f;
                centerPoint.y = 0.5f;
                mFilter =new GPUImageVignetteFilter(centerPoint, new float[] {0.0f, 0.0f, 0.0f}, 0.3f, 0.75f);
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter21:

                mFilter = new GPUImageGaussianBlurFilter();
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter22:

                mFilter =  new GPUImageCrosshatchFilter();
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter23:

                mFilter =  new GPUImageBoxBlurFilter();
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter24:

                mFilter =  new GPUImageCGAColorspaceFilter();
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter25:
                // mGPUImageView.setFilter(mFilter);
                mFilter =   new GPUImageDilationFilter();
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter26:


                mFilter= new MagicEmeraldFilter(this);
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter27:

                mFilter = new GPUImageBulgeDistortionFilter();
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;
            case R.id.filter28:

                mFilter = new GPUImageGlassSphereFilter();
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter29:

                mFilter =  new GPUImageHazeFilter();
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter30:

                mFilter = new GPUImageLaplacianFilter();
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter31:

                mFilter =new GPUImageSphereRefractionFilter();
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter32:

                mFilter =new GPUImageWeakPixelInclusionFilter();
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter33:

                mFilter =new GPUImageFalseColorFilter();
                mGPUImageView.setFilter(mFilter);
               /* mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);*/
                break;

            case R.id.filter34:

                GPUImageLevelsFilter levelsFilter = new GPUImageLevelsFilter();
                levelsFilter.setMin(0.0f, 3.0f, 1.0f);
                mFilter =levelsFilter;
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;
         /*   case R.id.filter35:

                mFilter =new GPUImageBilateralFilter();
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                break;
*/
       /*     case R.id.filter36:
                filterbutton.setImageResource(R.drawable.filter_icon_1);
                filterbar.setVisibility(View.INVISIBLE);

                mFilter =new GPUImageTransformFilter();
                mGPUImage.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                break;
*/
            case R.id.filter37:

//                mFilter =new  MagiSkinWhitenFilter(this);
                mFilter =new  MagiSkinWhitenFilter(this);

                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);

                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;
            case R.id.filter43:

                mFilter =new MagicLatteFilter(this);

                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;


            case R.id.filter42:
              /* MagicWarmFilter.green_state=1;
                MagicParams.front=0;*/
                mFilter =new MagicLomoFilter(this);
              //  MagicParams.green_state=0;
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

         /*   case R.id.filter38:
z
                //  mFilter =new MagicBrooklynFilter(this);
                //   mFilter =new MagicBeautyFilter(this);
                //    mFilter =new MagicBlackCatFilter(this);

                mFilter = new MagicSketchFilter(this);
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new FilterAdjuster(mFilter);
                break;
*/
        }
    }

}
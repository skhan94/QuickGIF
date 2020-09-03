package com.example.saman.gifimage;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v17.leanback.widget.HorizontalGridView;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.crash.FirebaseCrash;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

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
import com.example.saman.gifimage.filters.*;

public class PreviewGIF extends Activity implements SeekBar.OnSeekBarChangeListener,View.OnClickListener, GPUImageView.OnPictureSavedListener, Animation.AnimationListener {
    private GPUImage mGPUImage;
    private HorizontalGridView horizontalGridView;
    public ArrayList<File> gifimage;
    public static GPUImageFilter mFilter;
    public GPUImageFilterTools.FilterAdjuster mFilterAdjuster;
    public static GPUImageView mGPUImageView;
    public static Activity a;
    public static GridElementAdapter adapter;
    Bitmap icon1 = null;
    File mypath;
    ImageView applychanges;
    Bitmap bitmap1;
    String fileName;
    Bitmap dstBmp;
    ProgressDialog dialog;
    int minwidth;
    int minHeight;
    Bitmap icon2=null;
    public static File croppedfile;
    public static File notcroppedfile;
  //  LinearLayout speedbar;
    ImageView crop;
  //  ImageView speed;
  /*  Animation animationslideleft;
    Animation animationslideright;
    public static int delayspeed=300;
    TextView speedtext;
    SeekBar delayseekbar;
    int step = 1;
    int max = 1000;
    int min = 100;
    int value;*/
    public static Activity activity;
    public static ArrayList<Bitmap> gif_images;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
  //  Bitmap result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview_gif);
        MainScreen.crop_ind=0;
        activity=this;
        horizontalGridView = (HorizontalGridView) findViewById(R.id.gridView);

        crop = (ImageView)findViewById(R.id.crop);
        ((SeekBar) findViewById(R.id.seekBar)).setOnSeekBarChangeListener(this);

        dialog = new ProgressDialog(PreviewGIF.this);
        gifimage = (ArrayList<File>) getIntent().getSerializableExtra("PREVIEWIMAGE");
        mGPUImageView = (GPUImageView) findViewById(R.id.gpuimage);
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        a = this;
        adapter = new GridElementAdapter(this, gifimage);
        //  ((SeekBar) findViewById(R.id.seekBar)).setOnSeekBarChangeListener(this);
        findViewById(R.id.filter1).setOnClickListener(this);
        //   findViewById(R.id.filter2).setOnClickListener(this);
        //   findViewById(R.id.filter3).setOnClickListener(this);
        findViewById(R.id.filter4).setOnClickListener(this);
        findViewById(R.id.filter40).setOnClickListener(this);
        findViewById(R.id.filter41).setOnClickListener(this);
        //   findViewById(R.id.filter2).setOnClickListener(this);
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
        findViewById(R.id.filter43).setOnClickListener(this);
        findViewById(R.id.filter42).setOnClickListener(this);
        findViewById(R.id.seekBar).setVisibility(View.GONE);
        //   findViewById(R.id.filter35).setOnClickListener(this);
        //  findViewById(R.id.filter36).setOnClickListener(this);
        findViewById(R.id.filter37).setOnClickListener(this);
        applychanges = (ImageView) findViewById(R.id.button4);
        //   applychanges.setVisibility(View.INVISIBLE);
        applychanges.setEnabled(false);
        applychanges.setImageResource(R.drawable.apply_1);
        //  findViewById(R.id.filter38).setOnClickListener(this);

        horizontalGridView.setAdapter(adapter);
        sharedpreferences  = getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE);
         editor = sharedpreferences.edit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityCamera.activity=this;
        gif_images= new ArrayList<Bitmap>();
        adapter.notifyDataSetChanged();
        if (GIFGallery.gallery_status == 0) {
            crop.setVisibility(View.INVISIBLE);
        }
        else
        {
            crop.setVisibility(View.VISIBLE);
        }

        crop.setImageResource(R.drawable.crop_1);
        applychanges.setEnabled(false);
        applychanges.setImageResource(R.drawable.apply_1);
        try {
            handleImage(Uri.fromFile(GridElementAdapter.elements.get(GridElementAdapter.new_position)));
        } catch (IOException e) {
            FirebaseCrash.logcat(Log.ERROR, "Quick GIF", "Error caught");
            FirebaseCrash.report(e);
            e.printStackTrace();
        }
        //  mGPUImageView.setImage(GridElementAdapter.elements.get(0));
    }

    public void save_image(View v) throws IOException {
        applychanges.setEnabled(false);
        applychanges.setImageResource(R.drawable.apply_1);
        //    applychanges.setVisibility(View.INVISIBLE);


        Thread myThread = new Thread(new DownloadImagesThread());
        Log.e("Activity", "Thread Started");
        myThread.start();

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


    public void preview_gif(View v) {
        if(GridElementAdapter.elements.size()>1) {
            dialog.setMessage("Loading.. Please Wait!");
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
            Thread myThread1 = new Thread(new PreviewGIFThread());
            Log.e("Activity", "Thread Started");
            myThread1.start();
        }
        else
        {
            Toast.makeText(PreviewGIF.this,"You need atleast 2 pictures to create a GIF", Toast.LENGTH_SHORT).show();
        }

    }


      public void crop_image(View v)
      {
          crop.setImageResource(R.drawable.crop);
          for (int j = 0; j < GridElementAdapter.elements.size(); j++)
          {
              icon1 = BitmapFactory.decodeFile(GridElementAdapter.elements.get(j).toString());
              if (icon1.getWidth() >= icon1.getHeight()) {
                  dstBmp = Bitmap.createBitmap(icon1, icon1.getWidth() / 2 - icon1.getHeight() / 2, 0, icon1.getHeight(), icon1.getHeight());

              } else {
                  dstBmp = Bitmap.createBitmap(icon1, 0, icon1.getHeight() / 2 - icon1.getWidth() / 2, icon1.getWidth(), icon1.getWidth());

              }

              int w = icon1.getWidth();
              int h = icon1.getHeight();

              if (j == 0) {
                  minwidth = w;
                  minHeight= h;
                  Log.e("Activity","Min Width " + minwidth);

              } else {
                  if (minwidth > w) {
                      minwidth = w;
                      Log.e("Activity", "Min Width " + minwidth);
                  }

                  if (minHeight > h) {
                      minHeight = h;
                      Log.e("Activity", "Min Height " + minHeight);
                  }
              }
          }

          if(minwidth>640)
          {

              minwidth=640;
          }

          if(minHeight>640)
          {

              minHeight=640;
          }
           Log.e("Activity","Final Minimum Height " + minHeight);
           Log.e("Activity","Final Minimum Width " + minwidth);
          String fileName = "Image" + System.currentTimeMillis() + ".jpg";
          croppedfile=new File(Environment.getExternalStorageDirectory(), "Quick GIF" + File.separator + fileName);
          Uri outputUri = Uri.fromFile(croppedfile);
          notcroppedfile=GridElementAdapter.elements.get(GridElementAdapter.new_position);
          Uri source= Uri.fromFile(notcroppedfile);
          if(minwidth<minHeight) {
              new Crop(source).output(outputUri).withFixedSize(minwidth, minwidth).start(this);
          }
          else
          {
              new Crop(source).output(outputUri).withFixedSize(minHeight, minHeight).start(this);
          }
      //    Log.e("Activity","Cancel value " + CropImageActivity.cancel);
        /*  if(CropImageActivity.cancel ==0) {
              GridElementAdapter.elements.set(GridElementAdapter.new_position, finalfile1);
              adapter.notifyDataSetChanged();
          }
          else
          {
              Log.e("Activity","Crop cancelled");
              CropImageActivity.cancel=0;
              finalfile1.delete();
          }*/
      }
    private void handleImage(final Uri selectedImage) throws IOException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Log.e("Activity", "SelectedImage " + selectedImage);
        Log.e("Activity", "SelectedImage " + selectedImage.getPath());
        Log.e("Activity", "SelectedImage " + new File(selectedImage.getPath()).getAbsolutePath());
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
        // BitmapFactory.decode(selectedImage.getPath(), options);
        int imageHeight = bitmap.getHeight();
        int imageWidth = bitmap.getWidth();
        int bounding1 = (imageWidth <= imageHeight) ? imageWidth : imageHeight;
        int bounding = dpToPx(300);
        float xScale = ((float) bounding) / imageWidth;
        float yScale = ((float) bounding) / imageHeight;
        float scale = (xScale <= yScale) ? xScale : yScale;
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, imageWidth, imageHeight, matrix, true);
        imageWidth = scaledBitmap.getWidth(); // re-use
        imageHeight = scaledBitmap.getHeight();
        Log.e("Activity", "new image width and height " + imageWidth + " " + imageHeight);
        Log.e("Activity", "new image width and height " + imageWidth + " " + imageWidth);
        android.view.ViewGroup.LayoutParams layoutParams = mGPUImageView.getLayoutParams();
        layoutParams.width = imageWidth;
        layoutParams.height = imageHeight;
        Log.e("Activity", "GPU image width and height " + layoutParams.width + " " + layoutParams.height);
        mGPUImageView.setLayoutParams(layoutParams);
        mGPUImageView.setImage(selectedImage);
    }

    private int dpToPx(int dp) {
        float density = getApplicationContext().getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
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

    private class DownloadImagesThread implements Runnable {


        @Override
        public void run() {
          /*  Log.e("Activity", "Inside Thread " + mfile);
            Log.e("Activity", "Inside Thread " + iFilter);
          */
            try {
                downloadImages();
            } catch (IOException e) {
                FirebaseCrash.logcat(Log.ERROR, "Quick GIF", "Error caught");
                FirebaseCrash.report(e);
                e.printStackTrace();
            }
        }
    }

    public void downloadImages() throws IOException {
        fileName = "Image" + System.currentTimeMillis() + ".jpg";
        File path = Environment
                .getExternalStorageDirectory();
        String folder_main = "Quick GIF";

        File f = new File(Environment.getExternalStorageDirectory(), folder_main);
        if (!f.exists()) {
            f.mkdirs();
        }
        Bitmap bitmap = BitmapFactory.decodeFile(GridElementAdapter.elements.get(GridElementAdapter.mPosition).toString());
        //    mGPUImageView.setImage(pictureFile);
        mGPUImage = new GPUImage(this);
        mGPUImage.setImage(bitmap);
        mGPUImage.setFilter(mFilter);
        bitmap1 = mGPUImage.getBitmapWithFilterApplied(bitmap);
        java.util.Date date = new java.util.Date();
        String fileName = "Image" + new Timestamp(date.getTime()).toString() + ".jpg";
        File finalfile = new File(Environment
                .getExternalStorageDirectory(), "Quick GIF" + File.separator + fileName);
        FileOutputStream out = new FileOutputStream(finalfile);

        bitmap1.compress(Bitmap.CompressFormat.JPEG, 90, out);
        GridElementAdapter.elements.set(GridElementAdapter.mPosition, finalfile);
        Log.e("Activity", "mPosition " + GridElementAdapter.mPosition);
        Log.e("Activity","new_position " + GridElementAdapter.new_position);
        PreviewGIF.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    handleImage(Uri.fromFile(GridElementAdapter.elements.get(GridElementAdapter.new_position)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();

                Intent intent = new Intent(getIntent());
                PreviewGIF.this.finish();
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                overridePendingTransition(0, 0);
                startActivity(intent);
                overridePendingTransition(0, 0);
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,Uri.fromFile(GridElementAdapter.elements.get(GridElementAdapter.new_position)));
                PreviewGIF.this.sendBroadcast(mediaScanIntent);
                //  handleImage(Uri);
            }
        });

        out.flush();
        out.close();
    }


    private class PreviewGIFThread extends Thread {

        private Handler handler = new Handler() {

            @Override
            public void close() {

                if(GridElementAdapter.elements.size()>1) {

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


                    String file1=mypath.getAbsolutePath().toString();
                    editor.putString("filename", file1);
                    editor.commit();
                    dialog.dismiss();
                    Intent j = new Intent(PreviewGIF.this, GifView.class);
                    j.putExtra("FILE_NAME", mypath);
                    startActivity(j);
                }
             /*   else
                {
                    Toast.makeText(PreviewGIF.this, "You need atleast two pictures to create a gif", Toast.LENGTH_SHORT).show();

                }*/
            }

            @Override
            public void flush() {

            }

            @Override
            public void publish(LogRecord record) {

            }

        /*    @Override
            public void handleMessage(Message msg) {

                dialog.dismiss();
            }*/
        };
        @Override
        public void run() {
          /*  Log.e("Activity", "Inside Thread " + mfile);
            Log.e("Activity", "Inside Thread " + iFilter);
          */

            try {
                previewImages();
            } catch (IOException e) {
                FirebaseCrash.logcat(Log.ERROR, "Quick GIF", "Error caught");
                FirebaseCrash.report(e);
                e.printStackTrace();
            }
            handler.close();
            //   handler.handleMessage(0);
        }

    }


    public void previewImages() throws IOException {

        try {

            if (GridElementAdapter.elements.size() > 1) {
                ContextWrapper cw = new ContextWrapper(getApplicationContext());
                // path to /data/data/yourapp/app_data/imageDir
                File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

                // Create imageDir
                mypath = new File(directory, "quickgif" + System.currentTimeMillis() + ".gif");
                mypath.createNewFile();
                Log.e("Activity", "GIF FILE EXISTS " + mypath.exists());

                AnimatedGifWriter writer = new AnimatedGifWriter(true);


                OutputStream os = new FileOutputStream(mypath);

                writer.prepareForWrite(os, -1, -1);

                int i;

                if (GIFGallery.gallery_status == 0) {
                    for (i = 0; i < GridElementAdapter.elements.size(); i++) {

                        Log.e("Activity", "List Size1 " + GridElementAdapter.elements.size());
                        icon1 = BitmapFactory.decodeFile(GridElementAdapter.elements.get(i).getAbsolutePath());
                        Log.e("Activity", "List Item" + GridElementAdapter.elements.get(i).getAbsolutePath());
                        Log.e("Activity", "List Item Bitmap" + icon1);

                        gif_images.add(icon1);

                        writer.writeFrame(os, icon1);
                        Log.e("Activity", "Added" + icon1);


                    }
                } else
                {
                    for (i = 0; i < GridElementAdapter.elements.size(); i++)
                    {
                        icon1 = BitmapFactory.decodeFile(GridElementAdapter.elements.get(i).toString());
                        if (icon1.getWidth() >= icon1.getHeight()) {
                            dstBmp = Bitmap.createBitmap(icon1, icon1.getWidth() / 2 - icon1.getHeight() / 2, 0, icon1.getHeight(), icon1.getHeight());

                        } else {
                            dstBmp = Bitmap.createBitmap(icon1, 0, icon1.getHeight() / 2 - icon1.getWidth() / 2, icon1.getWidth(), icon1.getWidth());

                        }

                        int w = dstBmp.getWidth();
                        int h = dstBmp.getHeight();

                        if (i == 0) {
                            minwidth = w;
                            Log.e("Activity","Min Width " + minwidth);

                        } else {
                            if (minwidth > w) {
                                minwidth = w;
                                Log.e("Activity", "Min Width " + minwidth);
                            }
                        }
                    }
                    for (i = 0; i < GridElementAdapter.elements.size(); i++)
                    {
                        icon1 = BitmapFactory.decodeFile(GridElementAdapter.elements.get(i).toString());

                        if (icon1.getWidth() >= icon1.getHeight()) {

                            dstBmp = Bitmap.createBitmap(icon1, icon1.getWidth() / 2 - icon1.getHeight() / 2, 0, minwidth, minwidth);


                        } else {

                            dstBmp = Bitmap.createBitmap(icon1, 0, icon1.getHeight() / 2 - icon1.getWidth() / 2, minwidth, minwidth);

                        }

                        int w = dstBmp.getWidth();
                        int h = dstBmp.getHeight();


                       Bitmap result = Bitmap.createBitmap(minwidth, minwidth, dstBmp.getConfig());
                        Canvas canvas = new Canvas(result);
                        canvas.drawBitmap(dstBmp, 0, 0, null);
                        int ww = Math.round((float) (result.getWidth() * 0.25));
                        int hh = Math.round((float) (result.getHeight() * 0.20));
                        Log.e("Activity", "WW " + ww);
                        Log.e("Activity", "hh " + hh);
                        Log.e("Activity", "w-WW " + (w - ww));
                        Log.e("Activity", "h-hh " + (h - hh));
                        Bitmap waterMark = BitmapFactory.decodeResource(getResources(), R.drawable.watermark);
                        Bitmap watermark1 = Bitmap.createScaledBitmap(waterMark, ww, hh, true);
                        canvas.drawBitmap(watermark1, w - ww - 5, h - hh - 5, null);
                         gif_images.add(result);

                        writer.writeFrame(os, result);

                    }
                }
                writer.finishWrite(os);

                Log.e("Activity", "Bitmap Created");
                Toast.makeText(PreviewGIF.this, "GIF Created", Toast.LENGTH_LONG).show();
                icon1.recycle();
                icon1 = null;


            }


        } catch (Exception e) {
            FirebaseCrash.logcat(Log.ERROR, "Quick GIF", "Error caught");
            FirebaseCrash.report(e);
            e.printStackTrace();
        }
    }


    public void finish(View v)
    {
        this.finish();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
           /* case R.id.button_choose_filter:

             //   if(Settings.settings_state==0)
                    startActivity(new Intent(this, ActivityGallery.class));
                break;*/
            case R.id.filter1:
                mFilter = new GPUImageFilter();

                mGPUImageView.setFilter(mFilter);
             /*   mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);*/
                break;
            case R.id.filter40:
                //  mGPUImage.setFilter(null);
                applychanges.setEnabled(true);
                applychanges.setImageResource(R.drawable.apply);
                mFilter = new GPUImageColorInvertFilter();
                mGPUImageView.setFilter(mFilter);
             /*   mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);*/

                break;
            case R.id.filter41:
                // mGPUImage.setFilter(null);
                applychanges.setEnabled(true);
                applychanges.setImageResource(R.drawable.apply);
                mFilter = new GPUImageHueFilter(90.0f);
                mGPUImageView.setFilter(mFilter);
              /*  mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);*/

                break;
            case R.id.filter4:
                //mGPUImage.setFilter(null);
                //    mTourGuideHandler.cleanUp();
                //  mFilter = new GPUImageGammaFilter(2.0f);
                applychanges.setEnabled(true);
                applychanges.setImageResource(R.drawable.apply);
                mFilter = new MagicSketchFilter(this);
                mGPUImageView.setFilter(mFilter);
               /* mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
*/

                break;


            case R.id.filter5:
                applychanges.setEnabled(true);
                applychanges.setImageResource(R.drawable.apply);
                mFilter =new MagicBlackCatFilter(this);
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);



                break;

            case R.id.filter6:
                applychanges.setEnabled(true);
                applychanges.setImageResource(R.drawable.apply);
                //mGPUImage.setFilter(null);
                mFilter =  new GPUImageSepiaFilter();
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);


                break;
            case R.id.filter7:
                applychanges.setEnabled(true);
                applychanges.setImageResource(R.drawable.apply);
                mFilter =  new GPUImageGrayscaleFilter();
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter8:
                applychanges.setEnabled(true);
                applychanges.setImageResource(R.drawable.apply);
                GPUImageSharpenFilter sharpness = new GPUImageSharpenFilter();
                sharpness.setSharpness(2.0f);
                mFilter =  sharpness;
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;
            case R.id.filter9:
                applychanges.setEnabled(true);
                applychanges.setImageResource(R.drawable.apply);
                GPUImage3x3ConvolutionFilter convolution = new GPUImage3x3ConvolutionFilter();
                convolution.setConvolutionKernel(new float[]{
                        -1.0f, 0.0f, 1.0f,
                        -2.0f, 0.0f, 2.0f,
                        -1.0f, 0.0f, 1.0f
                });
                mFilter =  convolution;
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;
            case R.id.filter10:
                applychanges.setEnabled(true);
                applychanges.setImageResource(R.drawable.apply);
                //mGPUImage.setFilter(null);
                mFilter =  new GPUImageEmbossFilter();
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter11:
                applychanges.setEnabled(true);
                applychanges.setImageResource(R.drawable.apply);
                //mGPUImage.setFilter(null);
                mFilter =  new GPUImagePosterizeFilter();
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter12:
                applychanges.setEnabled(true);
                applychanges.setImageResource(R.drawable.apply);
                List<GPUImageFilter> filters = new LinkedList<GPUImageFilter>();
                filters.add(new GPUImageContrastFilter());
                filters.add(new GPUImageDirectionalSobelEdgeDetectionFilter());
                filters.add(new GPUImageGrayscaleFilter());

                mFilter =   new GPUImageFilterGroup(filters);
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter13:
                applychanges.setEnabled(true);
                applychanges.setImageResource(R.drawable.apply);
                mFilter =   new GPUImageSaturationFilter(1.0f);
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter14:
                applychanges.setEnabled(true);
                applychanges.setImageResource(R.drawable.apply);
                //    mFilter =   new GPUImageExposureFilter(0.0f);
                mFilter = new MagicAntiqueFilter(this);
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter15:
                applychanges.setEnabled(true);
                applychanges.setImageResource(R.drawable.apply);
                mFilter =   new GPUImageHighlightShadowFilter(0.0f, 1.0f);
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter16:
                applychanges.setEnabled(true);
                applychanges.setImageResource(R.drawable.apply);
                mFilter =  new GPUImageMonochromeFilter(1.0f, new float[]{0.6f, 0.45f, 0.3f, 1.0f});
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;


            case R.id.filter17:
                applychanges.setEnabled(true);
                applychanges.setImageResource(R.drawable.apply);
                mFilter = new MagicBeautyFilter(this);
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter18:
                applychanges.setEnabled(true);
                applychanges.setImageResource(R.drawable.apply);
                //   mFilter =   new GPUImageOpacityFilter(1.0f);
                mFilter = new MagicCoolFilter(this);
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter19:
                applychanges.setEnabled(true);
                applychanges.setImageResource(R.drawable.apply);
                mFilter = new GPUImageWhiteBalanceFilter(5000.0f, 0.0f);
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter20:
                applychanges.setEnabled(true);
                applychanges.setImageResource(R.drawable.apply);
                PointF centerPoint = new PointF();
                centerPoint.x = 0.5f;
                centerPoint.y = 0.5f;
                mFilter =new GPUImageVignetteFilter(centerPoint, new float[] {0.0f, 0.0f, 0.0f}, 0.3f, 0.75f);
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter21:
                applychanges.setEnabled(true);
                applychanges.setImageResource(R.drawable.apply);
                mFilter = new GPUImageGaussianBlurFilter();
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter22:
                applychanges.setEnabled(true);
                applychanges.setImageResource(R.drawable.apply);
                mFilter =  new GPUImageCrosshatchFilter();
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter23:
                applychanges.setEnabled(true);
                applychanges.setImageResource(R.drawable.apply);
                mFilter =  new GPUImageBoxBlurFilter();
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter24:
                applychanges.setEnabled(true);
                applychanges.setImageResource(R.drawable.apply);
                mFilter =  new GPUImageCGAColorspaceFilter();
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter25:
                applychanges.setEnabled(true);
                applychanges.setImageResource(R.drawable.apply);
                // mGPUImageView.setFilter(mFilter);
                mFilter =   new GPUImageDilationFilter();
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter26:

                applychanges.setEnabled(true);
                applychanges.setImageResource(R.drawable.apply);
                mFilter= new MagicEmeraldFilter(this);
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;


            case R.id.filter27:
                applychanges.setEnabled(true);
                applychanges.setImageResource(R.drawable.apply);
                mFilter = new GPUImageBulgeDistortionFilter();
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;
            case R.id.filter28:
                applychanges.setEnabled(true);
                applychanges.setImageResource(R.drawable.apply);
                mFilter = new GPUImageGlassSphereFilter();
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter29:
                applychanges.setEnabled(true);
                applychanges.setImageResource(R.drawable.apply);
                mFilter =  new GPUImageHazeFilter();
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter30:
                applychanges.setEnabled(true);
                applychanges.setImageResource(R.drawable.apply);
                mFilter = new GPUImageLaplacianFilter();
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter31:
                applychanges.setEnabled(true);
                applychanges.setImageResource(R.drawable.apply);
                mFilter =new GPUImageSphereRefractionFilter();
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter32:
                applychanges.setEnabled(true);
                applychanges.setImageResource(R.drawable.apply);
                mFilter =new GPUImageWeakPixelInclusionFilter();
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter33:
                applychanges.setEnabled(true);
                applychanges.setImageResource(R.drawable.apply);
                mFilter =new GPUImageFalseColorFilter();
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter34:
                applychanges.setEnabled(true);
                applychanges.setImageResource(R.drawable.apply);
                GPUImageLevelsFilter levelsFilter = new GPUImageLevelsFilter();
                levelsFilter.setMin(0.0f, 3.0f, 1.0f);
                mFilter =levelsFilter;
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

            case R.id.filter37:

//                mFilter =new  MagiSkinWhitenFilter(this);
                applychanges.setEnabled(true);
                applychanges.setImageResource(R.drawable.apply);
                mFilter =new  MagiSkinWhitenFilter(this);

                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;
            case R.id.filter43:

                mFilter =new MagicLatteFilter(this);

                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;


            case R.id.filter42:

                mFilter =new MagicLomoFilter(this);

                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
                findViewById(R.id.seekBar).setVisibility(
                        mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
                break;

        /*    case R.id.filter38:

                //  mFilter =new MagicBrooklynFilter(this);
                //   mFilter =new MagicBeautyFilter(this);
                //    mFilter =new MagicBlackCatFilter(this);
                applychanges.setEnabled(true);
                applychanges.setImageResource(R.drawable.apply);
                mFilter = new MagicSketchFilter(this);
                mGPUImageView.setFilter(mFilter);
                mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
                break;
*/

        }
    }

    @Override
    public void onPictureSaved(final Uri uri) {
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
    }
}
package com.example.saman.gifimage;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.hardware.camera2.CameraDevice;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v17.leanback.widget.HorizontalGridView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Set;



/**
 * Created by saman on 4/7/2016.
 */
public class GIFGallery extends Activity {
    // GridView mygrid;
    GridView mygrid;
    ImageView imageView;
    private LayoutInflater inflater;
    ImageView back;
    TextView text;
    Bitmap icon1=null;
    Bitmap dstBmp;
    int[] check  ;
    ArrayList<File> list;
    ArrayList<String>list2;
   // ProgressDialog dialog;
    //  ProgressBar loading;
    int s=0;
    //  TextView text;
    galleryAdapter gAdapter;
    ImageView button;
    int count;
    ArrayList<File> preview_gif;
    public static int gallery_status=0;
    InterstitialAd mInterstitialAd;
    CountDownTimer timer;
    AdView mAdView;
    AdRequest adRequest;
    @Override
    protected void onResume() {
        super.onResume();

        if (!mInterstitialAd.isLoaded()) {
            requestNewInterstitial();
        }
        //  button.setText("CREATE GIF");
        // tText("Create GIF");
        //   Settings.spinner.setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview_gridgif);
       // dialog=new ProgressDialog(this);
        button=(ImageView)findViewById(R.id.button2);
        text=(TextView)findViewById(R.id.text);
        preview_gif= new ArrayList<File>();
        mAdView = (AdView) findViewById(R.id.adView);
        adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        timer= new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                Log.e("Activity","Tick " + millisUntilFinished);
                if (mAdView != null) {
                    mAdView.resume();
                }
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
     /*   mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        // [END instantiate_interstitial_ad]

        // [START create_interstitial_ad_listener]
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                gif();
            }
        });*/
        // check=new int[];
        //    button.setText("CREATE GIF");
        //  gAdapter= new galleryAdapter(this,)
        //  text = (TextView) findViewById(R.id.textView);

        //   text.setText("Create GIF");
        //  Settings.spinner.setVisibility(View.GONE);
        mygrid=(GridView)findViewById(R.id.gridView1);
        //  loading= (ProgressBar) findViewById(R.id.progressBar1);
        //loading.setVisibility(View.INVISIBLE);
        Log.e("GALLERY", " " + 0);
    /*    File file1= new File(Environment.getExternalStorageDirectory(),"YoutubeDownloaderVideos");
        Log.e("GALLERY", " " + file1.toString());*/
        //  list= imageReader(file1);
        Log.e("Activity", "Camera status " + ActivityCamera.status);
        if(ActivityCamera.status==2) {
            list = (ArrayList<File>) getIntent().getSerializableExtra("arraylist");
            if(list==null)
            {
                text.setText("0 images selected");

            }
            else {
                gallery_status=0;
                text.setText(list.size() + " images selected");
                mygrid.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
                mygrid.setAdapter(new galleryAdapter(this, list));
                gAdapter = new galleryAdapter(this, list);
                check = new int[list.size()];
                count = list.size();
            }

        }
        if(ActivityCamera.status==0) {

            list2 = getIntent().getStringArrayListExtra("arraylist2");
            if (list2==null)
            {
                text.setText("0 images selected");
            }
            else
            {
                gallery_status=1;
                text.setText(list2.size() + " images selected");
                mygrid.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
                mygrid.setAdapter(new galleryAdapter(this, list2));
                gAdapter = new galleryAdapter(this,list2);
                check= new int[list2.size()];
                count=list2.size();
            }

        }


    }
    @Override
    protected void onStop() {
        super.onStop();
        timer.onFinish();
    }
    public void requestNewInterstitial() {
        Log.e("Activity", "Request Ad " );
        AdRequest adRequest = new AdRequest.Builder().build();
             /*   .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();*/

        mInterstitialAd.loadAd(adRequest);
    }


    public class galleryAdapter extends BaseAdapter{

        LayoutInflater inflater;
        ArrayList list;
        int i = 0;
        ArrayList<File> tempValues = null;

        Activity activity;

        public galleryAdapter(Activity a, ArrayList arrayList) {


            this.activity = a;
            this.list = arrayList;
            inflater = (LayoutInflater) activity.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        class mHolder {

            ImageView hImageView;
            ImageView hImageView2;
            //  int check=1;
            // check[position]=1;
        }

        public mHolder holder;

        @Override
        public View getView(final int position,  View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.gallery_row, parent, false);

                holder = new mHolder();

                holder.hImageView = (ImageView) convertView.findViewById(R.id.imageView2);
                holder.hImageView2=(ImageView) convertView.findViewById(R.id.check);
                check[position]=0;
                //check[position]=1;
                convertView.setTag(holder);
            } else {
                holder = (mHolder) convertView.getTag();
            }
            //   tempValues=null;
            // tempValues = (ArrayList<File>) list.get(position);
            Log.e("GALLERY ADAPTER", " " + list.get(position).toString());
            Glide.with(activity.getApplicationContext()).load(list.get(position).toString()).into(holder.hImageView);

            //     holder.hImageView2.setImageResource(R.drawable.check);
//            holder.check=1;
            //  Glide.with(activity.getApplicationContext()).load(list.get(position).toString()).into(holder.hImageView);
            Log.e("GALLERY ADAPTER", " " + "holder1");
/*
              if(dialog.isShowing())
              {
                  Log.e("Activity", "Dialog showing " +CameraActivity.status);
                  gif_create();
              }*/
            final View finalConvertView = convertView;
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(check[position]==0)

                    {
                        Log.e("Activity", "Position0 " + position);
                        Log.e("Activity", "CheckPosition0 " + check[position]);
                        ImageView imageView = (ImageView) finalConvertView.findViewById(R.id.check);
                        imageView.setImageResource(R.drawable.uncheck);
                        count=count-1;
                        TextView text = (TextView)findViewById(R.id.text);
                        text.setText(count + " images selected");
                        //       holder.hImageView2.setImageResource(R.drawable.uncheck);
                        check[position]=1;
                        Log.e("Activity", "UNCHECKED");
                        notifyDataSetChanged();
                    }
                    else if(check[position]==1)
                    {
                        Log.e("Activity", "Position1 " + position);
                        Log.e("Activity", "CheckPosition1" + check[position]);

                        ImageView imageView = (ImageView) finalConvertView.findViewById(R.id.check);
                        imageView.setImageResource(R.drawable.check);
                        count=count+1;
                        TextView text = (TextView)findViewById(R.id.text);
                        text.setText(count + " images selected");
                        check[position]=0;
                        Log.e("Activity", "CHECKED");
                        notifyDataSetChanged();
                    }
                    else
                    {
                        ImageView imageView = (ImageView) finalConvertView.findViewById(R.id.check);
                        imageView.setImageResource(R.drawable.uncheck);
                        // holder.hImageView2.setImageResource(R.drawable.uncheck);
                        check[position]=1;
                        Log.e("Activity", "UNCHECKED");
                        notifyDataSetChanged();
                    }
                }
            });
            return convertView;
        }




    }

    public void gif(){
        Log.e("Activity", "Camera Status " +ActivityCamera.status);
        if (ActivityCamera.status == 2)
        {
            if(list==null)
            {
                Toast.makeText(this.getApplicationContext(),"No image selected",Toast.LENGTH_SHORT).show();
            }
            else {
                preview_gif.clear();
                for (int i = 0; i < list.size(); i++) {
                    if (check[i] == 0) {
                        Log.e("Activity", "selected image " + list.get(i));
                        preview_gif.add(list.get(i));
                    }
                }
                if (preview_gif.size() > 1) {
                    Intent i = new Intent(GIFGallery.this, PreviewGIF.class);
                    i.putExtra("PREVIEWIMAGE", preview_gif);
                    startActivity(i);
                } else {
                    Toast.makeText(this.getApplicationContext(), "Please select atleast 2 pictures", Toast.LENGTH_SHORT).show();
                }
            }

        }
        if (ActivityCamera.status == 0)
        {
            if(list2==null)
            {
                Toast.makeText(this.getApplicationContext(),"No images selected",Toast.LENGTH_SHORT).show();
            }
            else {
                preview_gif.clear();
                for (int i = 0; i < list2.size(); i++) {
                    if (check[i] == 0) {
                        Log.e("Activity", "selected image " + list2.get(i));
                        File file = new File(list2.get(i));
                        preview_gif.add(file);
                    }
                }
                if (preview_gif.size() > 1) {
                    Intent i = new Intent(GIFGallery.this, PreviewGIF.class);
                    i.putExtra("PREVIEWIMAGE", preview_gif);
                    startActivity(i);
                } else {
                    Toast.makeText(this.getApplicationContext(), "Please select atleast 2 pictures", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }
    public void create_gif_image(View v) {
        gif();
    }


    public void finish(View v)
    {
        this.finish();
    }
}
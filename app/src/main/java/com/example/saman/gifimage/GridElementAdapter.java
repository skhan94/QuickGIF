package com.example.saman.gifimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.saman.gifimage.R;
import com.google.firebase.crash.FirebaseCrash;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

public class GridElementAdapter extends RecyclerView.Adapter<GridElementAdapter.SimpleViewHolder>{

    private Context context;
    public static ArrayList<File> elements;
    //  public static ArrayList<File> elements1;
    public static int mPosition=0;
    public static int new_position=0;



    public GridElementAdapter(Context context, ArrayList<File> file){
        this.context = context;
        this.elements = file;
        //     this.elements1 = file;
        // Fill dummy list

    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        ImageView button;
        ImageView cancel;

        public SimpleViewHolder(View view) {
            super(view);
            button = (ImageView) view.findViewById(R.id.imageView2);
            cancel = (ImageView) view.findViewById(R.id.check);
        }
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(this.context).inflate(R.layout.grid_element, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder holder, final int position) {
        //   holder.button.setText(elements.get(position));
        Log.e("Activity", "Adapter notify" + elements.get(position).toString() );
        Log.e("Activity", "Adapter notify position" + position );
        Log.e("Activity", "Adapter notify mposition" + mPosition );
        Glide.with(context).load(elements.get(position).toString()).diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).into(holder.button);
        holder.cancel.setImageResource(R.drawable.cancell);
        holder.button.setBackgroundColor(Color.TRANSPARENT);
        if(new_position==position)
        {
            holder.button.setBackgroundResource(R.drawable.grid_selection);
        }

//        notifyDataSetChanged();
        holder.cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if(elements.size()>2) {
                    elements.remove(position);


                    if (new_position == position)
                        new_position = new_position - 1;
                    try {

                       handleImage(Uri.fromFile(elements.get(new_position)));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.e("Activity", "Elements1 " + elements.size());
                    Log.e("Activity", "Elements1 position " + position);
                    //     elements1.remove(position);
                    notifyDataSetChanged();
                }
                else
                {
                    Toast.makeText(PreviewGIF.activity, "You need atleast 2 pictures to create a GIF", Toast.LENGTH_SHORT).show();
                }
            }
        });



        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new_position=position;
                Log.e("Activity", "Adapter notify" + elements.get(position).toString() );
                Log.e("Activity", "Adapter notify position" + position );
                Log.e("Activity", "Adapter notify mposition" + mPosition );
                Glide.with(context).load(elements.get(position).toString()).into(holder.button);
                try {
                    mPosition=position;
                    Log.e("Activity", "Adapter notify position" + position );
                    Log.e("Activity", "Adapter notify mposition" + mPosition );
                    handleImage(Uri.fromFile(elements.get(position)));
                } catch (IOException e) {
                    FirebaseCrash.logcat(Log.ERROR, "QuickGIF", "NPE caught");
                    FirebaseCrash.report(e);
                    e.printStackTrace();
                }

                notifyDataSetChanged();
                //   holder.button.set
  // Toast.makeText(context, "Position =" + position, Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void handleImage(final Uri selectedImage) throws IOException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Log.e("Activity", "SelectedImage " + selectedImage);
        Log.e("Activity", "SelectedImage " + selectedImage.getPath());
        Log.e("Activity", "SelectedImage " +  new File(selectedImage.getPath()).getAbsolutePath());
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(PreviewGIF.a.getContentResolver(), selectedImage);
        // BitmapFactory.decode(selectedImage.getPath(), options);
        int imageHeight = bitmap.getHeight();
        int imageWidth = bitmap.getWidth();
        int bounding1= (imageWidth<=imageHeight)?imageWidth : imageHeight;
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
        android.view.ViewGroup.LayoutParams layoutParams = PreviewGIF.mGPUImageView.getLayoutParams();
        layoutParams.width = imageWidth;
        layoutParams.height = imageHeight;
        Log.e("Activity", "GPU image width and height " +layoutParams.width+ " " +   layoutParams.height);
        PreviewGIF.mGPUImageView.setLayoutParams(layoutParams);
        PreviewGIF.mGPUImageView.setFilter(new GPUImageFilter());
        PreviewGIF.mGPUImageView.setImage(selectedImage);
    }

    private int dpToPx(int dp) {
        float density = PreviewGIF.a.getResources().getDisplayMetrics().density;
        return Math.round((float)dp * density);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return this.elements.size();
    }



}

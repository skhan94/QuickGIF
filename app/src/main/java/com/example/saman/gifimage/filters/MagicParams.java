package com.example.saman.gifimage.filters;

import android.content.Context;
import android.os.Environment;

/**
 * Created by saman on 5/31/2016.
 */
public class MagicParams {
    public static Context context;
    public static int new_filter=0;
    public static int front=0;
    public static int green_state=0;
  //  public static MagicBaseView magicBaseView;

    public static String videoPath = Environment.getExternalStorageDirectory().getPath();
    public static String videoName = "MagicCamera_test.mp4";

    public static int beautyLevel = 5;

    public MagicParams() {

    }
}

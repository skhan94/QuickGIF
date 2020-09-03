package com.example.saman.gifimage;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

/**
 * Created by saman on 6/9/2016.
 */
public class PopupWindow1 extends Dialog {

    public Activity c;
    public Dialog d;


    TextView filename;
    TextView text;
    public static String customfilename;
    public static int status;
    public static int mPosition;
    public static String ID;

    public PopupWindow1(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_photo);

    }

}
package com.example.saman.gifimage;

import android.app.Dialog;

        import android.app.Activity;
        import android.os.Bundle;
        import android.view.Window;
        import android.widget.TextView;



public class PopupWindow extends Dialog {

    public Activity c;
    public Dialog d;



    TextView filename;
    TextView text;
    public static String customfilename;
    public static int status;
    public static int mPosition;
    public static String ID;

    public PopupWindow(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup);

    }


}
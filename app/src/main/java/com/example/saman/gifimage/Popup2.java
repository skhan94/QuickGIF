package com.example.saman.gifimage;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by saman on 5/30/2016.
 */
public class Popup2 extends Dialog implements View.OnClickListener {
    public Activity c;
    public Dialog d;
    public ImageView no2;
    public Button yes, no;
    public static String downloadfile;
    public static final int REQUEST_TAKE_GALLERY_VIDEO = 1;
    Activity dialogactivity;

    TextView filename;
    TextView text;
    public static String customfilename;
    public static int status;
    public static int mPosition;
    public static String ID;

    public Popup2(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup2);

        yes = (Button) findViewById(R.id.imageView4);
        no = (Button) findViewById(R.id.imageView5);
        no2 = (ImageView) findViewById(R.id.imageView3);
        filename = (TextView) findViewById(R.id.textView1);
        //   text = (TextView) findViewById(R.id.textView);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);
        no2.setOnClickListener(this);
        //  dialogactivity = MainScreen.activity1;
        //  filename.setText(customfilename.toString());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView4:
                ActivityCamera.reset.setImageResource(R.drawable.reset_1);
                dismiss();
                break;
            case R.id.imageView5: {

                ActivityCamera.settings_state = 1;
                ActivityCamera.i=0;
                ActivityCamera.toggle.setImageResource(R.drawable.toggle_photo);
                ActivityCamera.captureImage.setEnabled(true);
                ActivityCamera.reset.setVisibility(View.INVISIBLE);
                ActivityCamera.creategif.setImageResource(R.drawable.photo);
                ActivityCamera.counterText.setText("0");
                ActivityCamera.counterlayout.setVisibility(View.INVISIBLE);
                ActivityCamera.captured_image.clear();
                dismiss();
                break;
            }
            case R.id.imageView3:
                ActivityCamera.reset.setImageResource(R.drawable.reset_1);
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }




}
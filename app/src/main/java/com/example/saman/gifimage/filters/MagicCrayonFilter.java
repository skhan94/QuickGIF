package com.example.saman.gifimage.filters;
import android.content.Context;
import android.opengl.GLES20;

import com.example.saman.gifimage.R;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;


public class MagicCrayonFilter extends GPUImageFilter{

    private int mSingleStepOffsetLocation;
    //1.0 - 5.0
    private int mStrengthLocation;

    public MagicCrayonFilter(Context context){
        super(NO_FILTER_VERTEX_SHADER, OpenGLUtils.readShaderFromRawResource(context, R.raw.crayon));
    }

    public void onInit() {
        super.onInit();
        mSingleStepOffsetLocation = GLES20.glGetUniformLocation(getProgram(), "singleStepOffset");
        mStrengthLocation = GLES20.glGetUniformLocation(getProgram(), "strength");
        setFloat(mStrengthLocation, 2.0f);
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onInitialized(){
        super.onInitialized();
        setFloat(mStrengthLocation, 0.5f);
    }

    private void setTexelSize(final float w, final float h) {
        setFloatVec2(mSingleStepOffsetLocation, new float[] {1.0f / w, 1.0f / h});
    }

  /*  @Override
    public void onInputSizeChanged(final int width, final int height) {
        super.onInputSizeChanged(width, height);
        setTexelSize(width, height);
    }*/
}
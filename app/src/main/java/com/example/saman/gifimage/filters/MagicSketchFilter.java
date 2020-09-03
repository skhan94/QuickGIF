package com.example.saman.gifimage.filters;
import android.content.Context;
import android.opengl.GLES20;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import com.example.saman.gifimage.R;

public class MagicSketchFilter extends GPUImageFilter{

    private int mSingleStepOffsetLocation;
    //0.0 - 1.0
    private int mStrength;
    private Context mContext;

    public MagicSketchFilter(Context context){
        super(NO_FILTER_VERTEX_SHADER,OpenGLUtils.readShaderFromRawResource(context, R.raw.sketch));
        mContext = context;
    }

    public void onInit() {
        super.onInit();
        mSingleStepOffsetLocation = GLES20.glGetUniformLocation(getProgram(), "singleStepOffset");
        mStrength = GLES20.glGetUniformLocation(getProgram(), "strength");
        setFloat(mStrength, 0.5f);
    }

    public void onDestroy() {
        super.onDestroy();
    }

    private void setTexelSize(final float w, final float h) {
        setFloatVec2(mSingleStepOffsetLocation, new float[] {1.0f / w, 1.0f / h});
    }

    @Override
    public void onOutputSizeChanged(final int width, final int height) {
        super.onOutputSizeChanged(width, height);
        setTexelSize(width, height);
    }
}
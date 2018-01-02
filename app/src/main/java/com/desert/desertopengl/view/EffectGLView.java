package com.desert.desertopengl.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.desert.desertopengl.R;
import com.desert.desertopengl.textures.FlagFlutter;
import com.desert.desertopengl.utils.ShaderUtil;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by desert on 201/1/1
 */

public class EffectGLView extends GLSurfaceView implements GLSurfaceView.Renderer {
    private FlagFlutter mFlagFlutter;
    private int mTextureId;
    float xAngle;//整体场景绕X轴旋转的角度
    float yAngle;//整体场景绕Y轴旋转的角度
    private float mPreviousX;//上次的触控位置X坐标
    private float mPreviousY;//上次的触控位置X坐标
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;//角度缩放比例

    public EffectGLView(Context context) {
        this(context, null);
    }

    public EffectGLView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        //设置版本信息
        setEGLContextClientVersion(2);
        //设置渲染器
        setRenderer(this);
        //设置渲染模式
        //RENDERMODE_CONTINUOUSLY //连续不断的刷新  消耗性能
        //RENDERMODE_WHEN_DIRTY //被动的刷新  需要用户主动去调用  requestRender();
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //用白色清除屏幕
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);
        //开启深度测试
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        //关闭背面剪裁
        GLES20.glDisable(GLES20.GL_CULL_FACE);
        mFlagFlutter = new FlagFlutter(getContext());
        mTextureId = initTexture(R.drawable.android_flag);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //设置视口大小
        GLES20.glViewport(0, 0, width, height);
        //设置视角大小
        float r = (float) width / height;
        //设置投影矩阵
        Matrix.frustumM(ShaderUtil.mProMatrix, 0, -r, r, -1, 1, 1, 10);
        //设置照相机位置
        Matrix.setLookAtM(ShaderUtil.mVMatrix, 0, 0, 0, 3, 0, 0, 0, 0, 1, 0);
        //之前是设置投影矩阵然后设置单位矩阵

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //清除颜色缓存和深度缓存
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        mFlagFlutter.drawSelf(mTextureId,xAngle,yAngle);
    }

    public int initTexture(int id) {
        int[] texIds = new int[1];
        //生成纹理id
        GLES20.glGenTextures(1, texIds, 0);
        int textureId = texIds[0];
        //绑定纹理id
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        //设置过滤器  纹理类型
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
//方式1
        //        InputStream inputStream = getResources().openRawResource(id);
//        Bitmap bitmap=null;
//        try {
//             bitmap=BitmapFactory.decodeStream(inputStream);
//        }catch (Exception e){
//            try {
//                inputStream.close();
//            } catch (IOException e1) {
//                e1.printStackTrace();
//            }
//        }
        //方式二
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), id);
        //加载纹理
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
        return textureId;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dx = x - mPreviousX;//计算触控笔X位移
                float dy = y - mPreviousY;//计算触控笔Y位移
                yAngle += dx * TOUCH_SCALE_FACTOR;//设置整体场景绕Y轴旋转角度
                xAngle += dy * TOUCH_SCALE_FACTOR;//设置绕整体场景X轴旋转角度
                requestRender();//重绘画面
        }
        mPreviousX = x;//记录触控笔位置
        mPreviousY = y;//记录触控笔位置
        return true;
    }
}

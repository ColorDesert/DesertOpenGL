package com.desert.desertopengl.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.AttributeSet;

import com.desert.desertopengl.R;
import com.desert.desertopengl.picture.Image;
import com.desert.desertopengl.textures.Pyramid;
import com.desert.desertopengl.textures.TextureTriangle;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by desert on 2017/11/27
 */

public class GLView extends GLSurfaceView implements GLSurfaceView.Renderer {
    private static final String TAG = "GLView";
    private int textureId;
    private Context mContext;

    public GLView(Context context) {
        this(context, null);
    }

    public GLView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
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
        //requestLayout();
    }

    private TextureTriangle mTextureTriangle;
    private Image mImage;
    private Pyramid mPyramid;

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        //用白色清除屏幕
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);
        //开启深度测试
        GLES20.glEnable(GL10.GL_DEPTH_TEST);
        //关闭背面剪裁
        GLES20.glDisable(GLES20.GL_CULL_FACE);
        //  mTextureTriangle = new TextureTriangle(mGLSLView);
        //mPyramid = new Pyramid(mGLSLView);
        mImage = new Image(this);
        initTexture();
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        //设置视口大小
        GLES20.glViewport(0, 0, width, height);
        //设置视角大小
        float r = (float) width / height;
        //设置投影矩阵
        Matrix.frustumM(Image.mProMatrix, 0, -r, r, -1, 1, 1, 10);
        //设置照相机位置
        Matrix.setLookAtM(Image.mVMatrix, 0, 0, 0, 3, 0, 0, 0, 0, 1, 0);
        //之前是设置投影矩阵然后设置单位矩阵

    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        //清除颜色缓存和深度缓存
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        mImage.drawSelf(textureId);
    }

    private void initTexture() {
        //生成纹理id
        int[] textures = new int[1];
        GLES20.glGenTextures(1,//生成纹理id的个数
                textures,//纹理id数组
                0);
        textureId = textures[0];
        //绑定纹理
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        //设置过滤器

        //纹理过滤函数 glTexParameterf(要操作的纹理类型，过滤器，过滤参数)
        //要操作的纹理类型:GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_1D,GLES20.GL_TEXTURE_3D
        //过滤器
        // GLES20.GL_TEXTURE_MIN_FILTER指定缩小滤波的方法
        //GLES20.GL_TEXTURE_MAG_FILTER指定放大滤波的方法
        // 参数：
        //GLES20.GL_NEARE(最邻近过滤，获得靠近纹理坐标点像素)
        //GLES20.GL_LINEA(线性插值，获取坐标点附近4个像素的加权平均值)
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        //纹理环绕glTexParameterf( 要操作的纹理类型, 环绕方向,环绕参数 )
        //环绕方向:STR
        //环绕参数:
        //1.GL_CLAMP_TO_EDGE:超出纹理范围的坐标被截取城0和1，形成纹理边缘延展效果
        //2.GL_REPEA:超出纹理范围的坐标，超出部分形成重复使用
        //3.GL_MIRRORED_REPEAT 倒影
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        //加载图片
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.fengj);
        //加载纹理
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D,//纹理类型
                0,//纹理层次，0表示基本的图像层
                bitmap,//纹理图像
                0//纹理边框尺寸
        );

    }
}

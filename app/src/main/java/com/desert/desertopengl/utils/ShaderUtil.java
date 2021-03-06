package com.desert.desertopengl.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.support.annotation.DrawableRes;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by desert on 2017/12/9
 */

public class ShaderUtil {
    private static final String TAG = "ShaderUtil";
    //4*4的投影矩阵
    public static float[] mProMatrix = new float[16];
    //4*4的相机矩阵
    public static float[] mVMatrix = new float[16];
    //总变换矩阵(最后起作用给着色器程序的矩阵)
    public static float[] mMVPMatrix;
    //变换矩阵（旋转、平移、缩放）
    public static float[] mMMatrix = new float[16];

    /**
     * 矩阵变化
     *
     * @param spec 变换矩阵
     * @return
     */
    public float[] getMatrix(float[] spec) {
        mMVPMatrix = new float[16];
        Matrix.multiplyMM(
                mMVPMatrix, //总变换矩阵
                0, //总变换矩阵的起始索引
                mVMatrix, //照相机位置
                0,//照相机朝向起始索引
                spec, //投影变换矩阵
                0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProMatrix, 0, mMVPMatrix, 0);
        return mMVPMatrix;
    }

    /**
     * 创建程序
     * <p>创建程序</p>
     * <p>添加着色器对象</p>
     * <p>链接程序</p>
     *
     * @param vertexSource   顶点源代码
     * @param fragmentSource 片元源代码
     * @return
     */
    public int createProgram(String vertexSource, String fragmentSource) {
        //得到顶点着色器
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource);
        if (vertexShader == 0) {
            return 0;
        }
        //得到片元着色器
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);
        if (fragmentShader == 0) {
            return 0;
        }
        int program = GLES20.glCreateProgram();
        if (program != 0) {
            //向着色器中添加着色器对象（程序，顶点着色器对象）
            GLES20.glAttachShader(program, vertexShader);
            checkGLError("glAttachShader");
            //向着色器中添加着色器对象（程序，片元着色器对象）
            GLES20.glAttachShader(program, fragmentShader);
            checkGLError("glAttachShader");
            //链接程序
            GLES20.glLinkProgram(program);
            int linkStatus[] = new int[1];
            //获取链接情况
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
            if (linkStatus[0] == 0) {
                Log.e(TAG, "Link program fail: " + GLES20.glGetProgramInfoLog(program));
                GLES20.glDeleteProgram(program);
                program = 0;
            }
        }
        return program;
    }

    public void checkGLError(String op) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, op + ": glError " + error);
            throw new RuntimeException(op + ": glError " + error);
        }
    }

    /**
     * 加载着色器
     * <p>创建着色器</p>
     * <p>加载着色器源程序</p>
     * <p>编译着色器</p>
     * <p>获取编译结果</p>
     *
     * @param shaderType GLES20.GL_FRAGMENT_SHADER/GLES20.GL_VERTEX_SHADER
     * @param source     源程序
     * @return shader
     */

    public int loadShader(int shaderType, String source) {
        int shader = GLES20.glCreateShader(shaderType);
        if (shader != 0) {
            //加载着色器源程序
            GLES20.glShaderSource(shader, source);
            //编译着色器
            GLES20.glCompileShader(shader);
            int[] compiled = new int[1];
            //获取编译结果
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
            if (compiled[0] == 0) {
                Log.e(TAG, "Compile shader fail: " + GLES20.glGetShaderInfoLog(shader));
                GLES20.glDeleteShader(shader);
                shader = 0;
            }
        }
        return shader;
    }

    /**
     * 从assets中获取着色器文件
     * <p>打开文件的输入流</p>
     * <p>创建文件的输出流</p>
     * <p>逐个字节读取文件数据放入缓冲区</p>
     *
     * @param filename  文件名
     * @param resources Resources
     * @return 字节字符串
     */
    public String loadFromAssetsFile(String filename, Resources resources) {
        String result = null;
        InputStream inputStream = null;
        ByteArrayOutputStream baos = null;
        try {
            inputStream = resources.getAssets().open(filename);
            baos = new ByteArrayOutputStream();
            int ch;
            while ((ch = inputStream.read()) != -1) {
                baos.write(ch);
            }
            result = new String(baos.toByteArray(), "UTF-8");
            result = result.replaceAll("\\r\\n", "\n");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (baos != null)
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (inputStream != null)
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

        }
        return result;
    }

    public FloatBuffer getFloatBuffer(float[] buffer) {
        //由于不同平台字节顺序不同，数据单元不是字节的
        //一定要经过ByteBuffer转换，关键是通过ByteBuffer设置nativeOrder
        ByteBuffer bbf = ByteBuffer.allocateDirect(buffer.length * 4);
        bbf.order(ByteOrder.nativeOrder());//设置这个字节缓冲的字节顺序为本地平台的字节顺序
        FloatBuffer intBuffer = bbf.asFloatBuffer();//转换为Float型缓冲
        intBuffer.put(buffer);//向缓冲区中放入数据
        intBuffer.position(0);//设置缓冲区的起始位置
        return intBuffer;
    }

    public ByteBuffer getByteBuffer(byte[] buffer) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(buffer.length);
        byteBuffer.put(buffer);//向缓冲区中放入数据
        byteBuffer.position(0);//设置缓冲区的起始位置
        return byteBuffer;
    }

    public int initTexture(Context context, @DrawableRes int id) {
        //生成纹理id
        int[] textures = new int[1];
        GLES20.glGenTextures(1,//生成纹理id的个数
                textures,//纹理id数组
                0);
        int textureId = textures[0];
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
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), id);
        //加载纹理
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D,//纹理类型
                0,//纹理层次，0表示基本的图像层
                bitmap,//纹理图像
                0//纹理边框尺寸
        );
        return textureId;
    }
}

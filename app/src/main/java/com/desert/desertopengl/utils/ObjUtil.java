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

import com.desert.desertopengl.obj.Normal;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created by desert on 2017/12/9
 */

public class ObjUtil {
    public FloatBuffer verBuffer, texBuffer, norBuffer;
    private static final String TAG = "ShaderUtil";
    //4*4的投影矩阵
    public static float[] mProMatrix = new float[16];
    //4*4的相机矩阵
    public static float[] mVMatrix = new float[16];
    //总变换矩阵(最后起作用给着色器程序的矩阵)
    public static float[] mMVPMatrix;
    //变换矩阵（旋转、平移、缩放）
    public static float[] mCurrentMatrix;
    public int vCount;

    public float[] getUnitMatrix() {
        return new float[]{
                1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1
        };
    }

    /**
     * 矩阵变化
     */
    public float[] getMatrix() {
        mMVPMatrix = new float[16];
        Matrix.multiplyMM(
                mMVPMatrix, //总变换矩阵
                0, //总变换矩阵的起始索引
                mVMatrix, //照相机位置
                0,//照相机朝向起始索引
                mCurrentMatrix, //投影变换矩阵
                0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProMatrix, 0, mMVPMatrix, 0);
        return mMVPMatrix;
    }

    public void readObjFile(InputStream stream) {
        List<Float> mListVer = new ArrayList<>();//保存顶点坐标--从文件中加载
        List<Float> mListVerRes = new ArrayList<>();//保存最终顶点坐标--根据面的索引
        List<Float> mListNormal = new ArrayList<>();//保存顶点法向量坐标
        float[] ab = new float[3], bc = new float[3], nor = new float[3];
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                String[] temps = line.split("[ ]+");
                if (temps[0].trim().equals("v")) {//顶点xyz
                    mListVer.add(Float.parseFloat(temps[1]));
                    mListVer.add(Float.parseFloat(temps[2]));
                    mListVer.add(Float.parseFloat(temps[3]));
                } else if (temps[0].trim().equals("f")) {//面 顶点索引 从1开始
                    int a = Integer.parseInt(temps[1]) - 1;
                    int b = Integer.parseInt(temps[2]) - 1;
                    int c = Integer.parseInt(temps[3]) - 1;
                    int d = Integer.parseInt(temps[4]) - 1;
                    //存储顶点
                    mListVerRes.add(mListVer.get(a * 3));
                    mListVerRes.add(mListVer.get(a * 3 + 1));
                    mListVerRes.add(mListVer.get(a * 3 + 2));

                    mListVerRes.add(mListVer.get(b * 3));
                    mListVerRes.add(mListVer.get(b * 3 + 1));
                    mListVerRes.add(mListVer.get(b * 3 + 2));

                    mListVerRes.add(mListVer.get(c * 3));
                    mListVerRes.add(mListVer.get(c * 3 + 1));
                    mListVerRes.add(mListVer.get(c * 3 + 2));

                    mListVerRes.add(mListVer.get(a * 3));
                    mListVerRes.add(mListVer.get(a * 3 + 1));
                    mListVerRes.add(mListVer.get(a * 3 + 2));

                    mListVerRes.add(mListVer.get(c * 3));
                    mListVerRes.add(mListVer.get(c * 3 + 1));
                    mListVerRes.add(mListVer.get(c * 3 + 2));

                    mListVerRes.add(mListVer.get(d * 3));
                    mListVerRes.add(mListVer.get(d * 3 + 1));
                    mListVerRes.add(mListVer.get(d * 3 + 2));
                    //法向量怎么来算？
                    //abc三个空间点，法向量为向量AB与向量BC的外积
                    //向量AB与向量BC
                    for (int i = 0; i < 3; i++) {
                        ab[i] = mListVer.get(a * 3 + i) - mListVer.get(b * 3 + i);
                        bc[i] = mListVer.get(b * 3 + i) - mListVer.get(c * 3 + i);
                    }
                    //法向量 xyz
                    nor[0] = ab[1] * bc[2] - ab[2] * bc[1];
                    nor[1] = ab[2] * bc[0] - ab[0] * bc[2];
                    nor[2] = ab[0] * bc[1] - ab[1] * bc[0];
                    for (int i = 0; i < 6; i++) {
                        mListNormal.add(nor[0]);
                        mListNormal.add(nor[1]);
                        mListNormal.add(nor[2]);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        verBuffer = getFloatBuffer(mListVerRes);
        vCount = mListVerRes.size() / 3;
        norBuffer = getFloatBuffer(mListNormal);
    }

    public void readObjAndTexFile(InputStream stream) {
        List<Float> mListVer = new ArrayList<>();//保存顶点坐标--从文件中加载
        List<Float> mListVerRes = new ArrayList<>();//保存最终顶点坐标--根据面的顶点索引
        List<Float> mTexList = new ArrayList<>();//保存纹理顶点坐标--从文件中加载
        List<Float> mTexListRes = new ArrayList<>();//保存纹理最终顶点坐标--根据面的纹理索引
        List<Integer> mFaceIndex = new ArrayList<>();//顶点组装面索引列表--根据面的信息从文件中加载
        //平均前各个索引对应的点的法向量集合Map
        //此HashMap的key为点的索引， value为点所在的各个面的法向量的集合
        //相同顶点的法向量集合（顶点可能被重复使用）HashSet
        HashMap<Integer, HashSet<Normal>> map = new HashMap<>();
        float[] ab = new float[3], ac = new float[3], nor;
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line;
        try {
            while ((line = reader.readLine()) != null) {//顶点
                String[] temps = line.split("[ ]+");
                if (temps[0].trim().equals("v")) {
                    float x = Float.parseFloat(temps[1]);
                    float y = Float.parseFloat(temps[2]);
                    float z = Float.parseFloat(temps[3]);
                    mListVer.add(x);
                    mListVer.add(y);
                    mListVer.add(z);
                } else if (temps[0].trim().equals("vt")) {//纹理坐标
                    mTexList.add(Float.parseFloat(temps[1]) / 2.0f);
                    mTexList.add(Float.parseFloat(temps[2]) / 2.0f);
                } else if (temps[0].trim().equals("f")) {
                    int a = Integer.parseInt(temps[1].split("/")[0]) - 1;
                    int b = Integer.parseInt(temps[2].split("/")[0]) - 1;
                    int c = Integer.parseInt(temps[3].split("/")[0]) - 1;
                    int[] index = new int[3];//三个顶点索引值的数组
                    index[0] = a;
                    index[1] = b;
                    index[2] = c;
                    //三角形面第一个点
                    mListVerRes.add(mListVer.get(a * 3));
                    mListVerRes.add(mListVer.get(a * 3 + 1));
                    mListVerRes.add(mListVer.get(a * 3 + 2));
                    //三角形面第二个点
                    mListVerRes.add(mListVer.get(b * 3));
                    mListVerRes.add(mListVer.get(b * 3 + 1));
                    mListVerRes.add(mListVer.get(b * 3 + 2));
                    //三角形面第三个点
                    mListVerRes.add(mListVer.get(c * 3));
                    mListVerRes.add(mListVer.get(c * 3 + 1));
                    mListVerRes.add(mListVer.get(c * 3 + 2));

                    mFaceIndex.add(a);
                    mFaceIndex.add(b);
                    mFaceIndex.add(c);
                    //计算法向量 ab和ac
                    for (int i = 0; i < 3; i++) {
                        ab[i] = mListVer.get(b * 3 + i) - mListVer.get(a * 3 + i);
                        ac[i] = mListVer.get(c * 3 + i) - mListVer.get(a * 3 + i);
                    }
                    nor = VecUtil.getCrossProduct(ab, ac);
                    //记录每个索引点的法向量到平均前各个索引对应的点的法向量集合组成的Map中
                    for (int tempIndex : index) {
                        HashSet<Normal> hashSet = map.get(tempIndex);
                        if (hashSet == null) {
                            hashSet = new HashSet<>();
                        }
                        hashSet.add(new Normal(nor[0], nor[1], nor[2]));
                        map.put(tempIndex, hashSet);
                    }

                    //纹理坐标
                    int k = Integer.parseInt(temps[1].split("/")[1]) - 1;
                    mTexListRes.add(mTexList.get(k * 2));
                    mTexListRes.add(mTexList.get(k * 2 + 1));

                    k = Integer.parseInt(temps[2].split("/")[1]) - 1;
                    mTexListRes.add(mTexList.get(k * 2));
                    mTexListRes.add(mTexList.get(k * 2 + 1));

                    k = Integer.parseInt(temps[3].split("/")[1]) - 1;
                    mTexListRes.add(mTexList.get(k * 2));
                    mTexListRes.add(mTexList.get(k * 2 + 1));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //得到缓冲数据
        verBuffer = getFloatBuffer(mListVerRes);
        vCount = mListVerRes.size() / 3;
        texBuffer = getFloatBuffer(mTexListRes);
        float[] norArr = new float[mFaceIndex.size() * 3];
        int c = 0;
        for (int i = 0; i < mFaceIndex.size(); i++) {
            HashSet<Normal> normals = map.get(mFaceIndex.get(i));
            //得到顶点在各个面上法向量的平均向量
            //求出平均法向量
            float[] tn = VecUtil.getAverage(normals);
            //将计算出的平均法向量存放到法向量数组中
            norArr[c++] = tn[0];
            norArr[c++] = tn[1];
            norArr[c++] = tn[2];
        }
        norBuffer = getFloatBuffer(norArr);
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

    public FloatBuffer getFloatBuffer(List<Float> data) {
        //由于不同平台字节顺序不同，数据单元不是字节的
        //一定要经过ByteBuffer转换，关键是通过ByteBuffer设置nativeOrder
        ByteBuffer bbf = ByteBuffer.allocateDirect(data.size() * 4);
        bbf.order(ByteOrder.nativeOrder());//设置这个字节缓冲的字节顺序为本地平台的字节顺序
        FloatBuffer floatBuffer = bbf.asFloatBuffer();//转换为Float型缓冲
        for (int i = 0; i < data.size(); i++) {
            floatBuffer.put(data.get(i));
        }
        //floatBuffer.put(buffer);//向缓冲区中放入数据
        floatBuffer.position(0);//设置缓冲区的起始位置
        return floatBuffer;
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

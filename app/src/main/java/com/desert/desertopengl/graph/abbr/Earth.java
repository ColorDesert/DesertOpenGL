package com.desert.desertopengl.graph.abbr;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.desert.desertopengl.utils.ShaderUtil;

import java.nio.FloatBuffer;
import java.util.ArrayList;

/**
 * Created by desert on 2017/12/9
 */

public class Earth extends ShaderUtil {

    private FloatBuffer verBuffer;
    private Context mContext;
    private int mProgram, mPosition, aColor, mMVPMatrix;
    private int angle = 0;
    private int vCount;
    float y1, x1, z1, y2, x2, z2, len1, len2;

    public Earth(Context context) {
        mContext = context;
        initData();
        initShader();
    }

    private void initData() {
        ArrayList<Float> mList = new ArrayList<>();
        int angle = 1;
        for (int i = -90; i < 90 + angle; i += angle) {
            y1 = (float) Math.sin(Math.toRadians(i));
            y2 = (float) Math.sin(Math.toRadians(i + angle));
            //xz平面投影的长度
            len1 = (float) Math.cos(Math.toRadians(i));
            len2 = (float) Math.cos(Math.toRadians(i + angle));
            for (int j = 0; j < 360 + angle; j += angle) {
                x1 = (float) (len1 * Math.sin(Math.toRadians(j)));
                z1 = (float) (len1 * Math.cos(Math.toRadians(j)));
                x2 = (float) (len2 * Math.sin(Math.toRadians(j)));
                z2 = (float) (len2 * Math.cos(Math.toRadians(j)));
                mList.add(x1);
                mList.add(y1);
                mList.add(z1);
                //减少粗糙度
                mList.add(x2);
                mList.add(y2);
                mList.add(z2);
            }
        }
        vCount = mList.size();
        float[] ver = new float[vCount];
        for (int i = 0; i < vCount; i++) {
            ver[i] = mList.get(i);
        }
        verBuffer = getFloatBuffer(ver);
    }

    private void initShader() {
        String verSource = loadFromAssetsFile("Light/VerBallWithLight.glsl", mContext.getResources());
        String fragSource = loadFromAssetsFile("Light/BallWithLight.glsl", mContext.getResources());
        mProgram = createProgram(verSource, fragSource);
        //获取程序（着色器）中顶点位置（XYZ 引用的id）
        mPosition = GLES20.glGetAttribLocation(mProgram, "vPosition");
        //获取程序（着色器）中顶点颜色位置(RGB A引用的id)
        //aColor = GLES20.glGetAttribLocation(mProgram, "vColor");
        //获取程序（着色器） 总变换矩阵的id
        mMVPMatrix = GLES20.glGetUniformLocation(mProgram, "vMatrix");
    }


    public void drawSelf() {
        //使用某套 着色器程序
        GLES20.glUseProgram(mProgram);
        //设置变化矩阵（旋转）
        Matrix.translateM(mChangeMatrix, 0, 0, 0, -9);
        Matrix.setRotateM(mChangeMatrix, 0, angle, 1, 1, 0);
        //给着色器传变量值
        GLES20.glUniformMatrix4fv(mMVPMatrix, 1, false, getMatrix(mChangeMatrix), 0);
        //设置顶点数据 normalized是否归一化
        GLES20.glVertexAttribPointer(mPosition, 3, GLES20.GL_FLOAT, false, 0, verBuffer);
        //设置颜色顶点数据 normalized是否归一化
        //GLES20.glVertexAttribPointer(aColor, 4, GLES20.GL_FLOAT, false, 0, colorBuffer);
        //开启顶点和顶点颜色绘制
        GLES20.glEnableVertexAttribArray(mPosition);
        //GLES20.glEnableVertexAttribArray(aColor);
        //先一个圆
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, vCount / 3);
        GLES20.glDisableVertexAttribArray(mPosition);
        //GLES20.glDisableVertexAttribArray(aColor);
        angle += 3;

    }
}

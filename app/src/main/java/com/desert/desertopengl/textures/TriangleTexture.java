package com.desert.desertopengl.textures;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.desert.desertopengl.R;
import com.desert.desertopengl.utils.ShaderUtil;

import java.nio.FloatBuffer;

/**
 * Created by desert on 2017/12/9
 */

public class TriangleTexture extends ShaderUtil {

    private FloatBuffer verBuffer, texBuffer;
    private Context mContext;
    private int mProgram, aPosition, aTexture, uMVPMatrix;
    private int angle = 0;
    private int vCount;
    private int textureId;

    public TriangleTexture(Context context) {
        mContext = context;
        initData();
        textureId = initTexture(context, R.drawable.meinv);
        initShader();
    }

    private void initData() {
        float ver[] = {
                0, 1.5f, 0.5f,
                -1.5f, -1.5f, 0.5f,
                1.5f, -1.5f, 0.5f
        };
        vCount = 3;
        verBuffer = getFloatBuffer(ver);
        float tex[] = new float[]{
                0.5f, 0,
                0, 1,
                1, 1
//                0.5f, 0,
//                0, 1.5f,
//                1.5f, 1.5f
        };
        texBuffer = getFloatBuffer(tex);
    }

    private void initShader() {
        String verSource = loadFromAssetsFile("texVer.glsl", mContext.getResources());
        String fragSource = loadFromAssetsFile("texFrag.glsl", mContext.getResources());
        mProgram = createProgram(verSource, fragSource);
        //获取程序（着色器）中顶点位置（XYZ 引用的id）
        aPosition = GLES20.glGetAttribLocation(mProgram, "vPosition");
        //获取程序（着色器）中顶点纹理位置的id
        aTexture = GLES20.glGetAttribLocation(mProgram, "vTexPos");
        //获取程序（着色器） 总变换矩阵的id
        uMVPMatrix = GLES20.glGetUniformLocation(mProgram, "vMatrix");
    }


    public void drawSelf() {
        //使用某套 着色器程序
        GLES20.glUseProgram(mProgram);
        //初始化矩阵  单位矩阵
        Matrix.setRotateM(mMMatrix, 0, 0, 0, 1, 0);
        Matrix.translateM(mMMatrix, 0, 0, 0, 1);
        //设置变化矩阵（旋转）
        Matrix.rotateM(mMMatrix, 0, angle, 1, 1, 0);

        //给着色器传变量值
        GLES20.glUniformMatrix4fv(uMVPMatrix, 1, false, getMatrix(mMMatrix), 0);
        //设置顶点数据 normalized是否归一化
        GLES20.glVertexAttribPointer(aPosition, 3, GLES20.GL_FLOAT, false, 0, verBuffer);
        //设置颜色顶点数据 normalized是否归一化
        GLES20.glVertexAttribPointer(aTexture, 2, GLES20.GL_FLOAT, false, 0, texBuffer);
        //开启顶点和顶点纹理绘制
        GLES20.glEnableVertexAttribArray(aPosition);
        GLES20.glEnableVertexAttribArray(aTexture);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        //绘制三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
        GLES20.glDisableVertexAttribArray(aPosition);
        GLES20.glDisableVertexAttribArray(aTexture);

        //  angle += 3;

    }
}

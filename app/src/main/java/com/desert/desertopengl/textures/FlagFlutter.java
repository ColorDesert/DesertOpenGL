package com.desert.desertopengl.textures;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.desert.desertopengl.utils.ShaderUtil;

import java.nio.FloatBuffer;

/**
 * 飘动效果
 * Created by desert on 2018/1/1
 */

public class FlagFlutter extends ShaderUtil {
    private FloatBuffer verBuffer, txtBuffer;
    private int mProgram, mHPosition, mHMatrix, mHTxtCoor,mHWidthSpan,mHStartAngle;
    private float angle;
    private Context mContext;
    float WIDTH_SPAN = 3.3F;
    public int vCount;//顶点个数
    private float corrStartAngle;


    public FlagFlutter(Context context) {
        mContext = context;
        initData();
    }

    public void initData() {
        int cols = 12;
        int rows = cols * 3 / 4;
        initVer(cols,rows);
        initTex(cols, rows);
        initShared();
    }

    private void initVer(int cols, int rows) {
        //矩形个数=cols * rows；
        //三角形的个数=矩形个数*2=cols * rows * 2
        vCount = cols * rows * 2 * 3;
        float[] ver = new float[vCount * 3];
        float SIZE = WIDTH_SPAN / cols;//每格的单位长度
        int num = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                float x = -SIZE * cols / 2 + j * SIZE;
                float y = SIZE * rows / 2 - i * SIZE;
                float z = 0;
                ver[num++] = x;
                ver[num++] = y;
                ver[num++] = z;

                ver[num++] = x;
                ver[num++] = y - SIZE;
                ver[num++] = z;

                ver[num++] = x + SIZE;
                ver[num++] = y;
                ver[num++] = z;

                ver[num++] = x + SIZE;
                ver[num++] = y;
                ver[num++] = z;

                ver[num++] = x;
                ver[num++] = y - SIZE;
                ver[num++] = z;

                ver[num++] = x + SIZE;
                ver[num++] = y - SIZE;
                ver[num++] = z;
            }
        }
        verBuffer = getFloatBuffer(ver);
    }

    private void initTex(int bw, int bh) {
        int txtCount = bw * bh * 2 * 3 * 2;
        float[] txt = new float[txtCount];
        float wSize = 1.0f / bw;
        float hSize = 0.75f / bh;
        int num = 0;
        for (int i = 0; i < bh; i++) {
            for (int j = 0; j < bw; j++) {
                float s = wSize * j;
                float t = hSize * i;

                txt[num++] = s;
                txt[num++] = t;

                txt[num++] = s;
                txt[num++] = t + hSize;

                txt[num++] = s + wSize;
                txt[num++] = t;

                txt[num++] = s + wSize;
                txt[num++] = t;

                txt[num++] = s;
                txt[num++] = t + hSize;

                txt[num++] = s + wSize;
                txt[num++] = t + hSize;

            }
        }
        txtBuffer = getFloatBuffer(txt);
    }


    public void initShared() {
        String verSource = loadFromAssetsFile("flutter/vertex_tex_xy.glsl", mContext.getResources());
        String fragSource = loadFromAssetsFile("flutter/frag_tex.glsl", mContext.getResources());
        mProgram = createProgram(verSource, fragSource);
        mHMatrix = GLES20.glGetUniformLocation(mProgram, "vMatrix");
        mHWidthSpan = GLES20.glGetUniformLocation(mProgram, "vWidthSpan");
        mHStartAngle = GLES20.glGetUniformLocation(mProgram, "vStartAngle");
        mHPosition = GLES20.glGetAttribLocation(mProgram, "vPosition");
        mHTxtCoor = GLES20.glGetAttribLocation(mProgram, "vTexCoor");
    }

    public void drawSelf(int textureId,float xAngle,float yAngle) {
        GLES20.glUseProgram(mProgram);
        Matrix.setRotateM(mMMatrix,0,0,0,1,0);
       // Matrix.translateM(mMMatrix,0,0,0,-1);
        Matrix.rotateM(mMMatrix,0,xAngle,1,0,0);
        Matrix.rotateM(mMMatrix,0,yAngle,0,1,0);
        GLES20.glUniformMatrix4fv(mHMatrix, 1, false,  getMatrix(mMMatrix), 0);
        GLES20.glUniform1f(mHStartAngle, corrStartAngle);
        GLES20.glUniform1f(mHWidthSpan, WIDTH_SPAN);
        GLES20.glVertexAttribPointer(mHPosition, 3, GLES20.GL_FLOAT, false, 0, verBuffer);
        GLES20.glVertexAttribPointer(mHTxtCoor, 2, GLES20.GL_FLOAT, false, 0, txtBuffer);
        GLES20.glEnableVertexAttribArray(mHPosition);
        GLES20.glEnableVertexAttribArray(mHTxtCoor);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
        GLES20.glDisableVertexAttribArray(mHPosition);
        GLES20.glDisableVertexAttribArray(mHTxtCoor);
        corrStartAngle += 0.03;
    }
}

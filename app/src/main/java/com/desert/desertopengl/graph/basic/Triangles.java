package com.desert.desertopengl.graph.basic;

import com.desert.desertopengl.utils.OpenGLUtils;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by desert on 2017/12/3
 */

public class Triangles extends OpenGLUtils {
    private FloatBuffer colorBuffer, colorStripBuffer;
    private FloatBuffer verBuffer, verStripBuffer;
    private int vCount, vStripCount;
    private int mode;

    public Triangles() {
        initData();
        initStripData();
    }

    private void initData() {
        //角度
        int angle = 45;
        //顶点个数
        vCount = 360 / angle + 2;//(一个是中心点（0，0），最后一个点是重复点)
        //顶点数据
        float ver[] = new float[3 * vCount];
        int count = 0;
        ver[count++] = 0;
        ver[count++] = 0;
        ver[count++] = 0;
        for (int i = 0; i < 360 + angle; i += angle) {
            ver[count++] = (float) (Math.cos(Math.toRadians(i)) - Math.sin(Math.toRadians(i)));
            ver[count++] = (float) (Math.cos(Math.toRadians(i)) + Math.sin(Math.toRadians(i)));
            ver[count++] = 0;

        }
        //创建顶点缓冲数据
        verBuffer = getFloatBuffer(ver);
        //顶点颜色//int one = 65535;//支持65535色彩通道
        //顶点颜色数据
        count = 0;
        float[] color = new float[4 * vCount];
        for (int i = 0; i < vCount; i++) {
            color[count++] = 0.0f;
            color[count++] = 0.8f;
            color[count++] = 1.0f;
            color[count++] = 0;
        }
        //创建顶点颜色缓冲数据
        colorBuffer = getFloatBuffer(color);
    }

    public void initStripData() {
        //步长
        int angle = 30;
        //顶点个数
        vStripCount = (360 / angle + 1) * 2;//(最后一个点是重复点)
        //顶点数据
        float ver[] = new float[3 * vStripCount];
        int count = 0;
        for (int i = 0; i < 360 + angle; i += angle) {
            ver[count++] = (float) (Math.cos(Math.toRadians(i)) - Math.sin(Math.toRadians(i)));
            ver[count++] = (float) (Math.cos(Math.toRadians(i)) + Math.sin(Math.toRadians(i)));
            ver[count++] = 0;
            ver[count++] = 0.5f * (float) (Math.cos(Math.toRadians(i)) - Math.sin(Math.toRadians(i)));
            ver[count++] = 0.5f * (float) (Math.cos(Math.toRadians(i)) + Math.sin(Math.toRadians(i)));
            ver[count++] = 0;

        }
        //创建顶点缓冲数据
        verStripBuffer = getFloatBuffer(ver);
        //顶点颜色
        //顶点颜色数据
        count = 0;
        float[] color = new float[4 * vStripCount];
        for (int i = 0; i < vStripCount; i++) {
            color[count++] = 1.0f;
            color[count++] = 0.3f;
            color[count++] = 0.0f;
            color[count++] = 0;
        }
        //创建顶点颜色缓冲数据
        colorStripBuffer = getFloatBuffer(color);
    }

    public void drawSelf(GL10 gl) {
        //开启顶点坐标数组
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        //开启颜色坐标数组
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        gl.glPointSize(20);//设置绘制顶点的大小
        if (mode == GL10.GL_TRIANGLE_FAN || mode == GL10.GL_TRIANGLES) {
            gl.glVertexPointer(
                    3,//坐标个数（xyz）
                    GL10.GL_FLOAT,//数据类型
                    0,//连续顶点坐标数据的间隔
                    verBuffer);//顶点数据缓冲
            gl.glColorPointer(
                    4,//颜色通道个数（RGB A）
                    GL10.GL_FLOAT,//数据类型
                    0,//连续顶点坐标数据的间隔
                    colorBuffer);//顶点颜色数据缓冲
            gl.glDrawArrays(mode,//绘制模型（点/线/三角形）
                    0,//从数组缓存的那以为开始读取 一般为0
                    vCount//顶点个数
            );
        } else {
            gl.glVertexPointer(3, GL10.GL_FLOAT, 0, verStripBuffer);
            gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorStripBuffer);
            gl.glDrawArrays(mode, 0, vStripCount);
        }
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}

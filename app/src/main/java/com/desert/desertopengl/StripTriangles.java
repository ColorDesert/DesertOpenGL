package com.desert.desertopengl;

import com.desert.desertopengl.utils.OpenGLUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by desert on 2017/12/3
 */

public class StripTriangles extends OpenGLUtils {
    private IntBuffer colorBuffer;
    private FloatBuffer verBuffer;
    private int vCount;

    public StripTriangles() {
        init();
    }

    private void init() {
        //角度
        int angle = 45;
        //顶点个数
        vCount = 2 * (360 / angle + 1);//(1:最后一个是重复点)
        //顶点数据
        float ver[] = new float[3 * vCount];
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
        verBuffer = getFloatBuffer(ver);
        //顶点颜色
        int one = 65535;//支持65535色彩通道
        //顶点颜色数据
        count = 0;
        int[] color = new int[4 * vCount];
        for (int i = 0; i < vCount; i++) {
            color[count++] = one;
            color[count++] = one;
            color[count++] = 0;
            color[count++] = 0;
        }
        //创建顶点颜色缓冲数据
        colorBuffer = getIntBuffer(color);
    }

    public void drawSelf(GL10 gl) {
        //开启顶点坐标数组
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        //开启颜色坐标数组
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        gl.glVertexPointer(
                3,//坐标个数（xyz）
                GL10.GL_FLOAT,//数据类型
                0,//连续顶点坐标数据的间隔
                verBuffer);//顶点数据缓冲
        gl.glColorPointer(
                4,//颜色通道个数（RGB A）
                GL10.GL_FIXED,//数据类型
                0,//连续顶点坐标数据的间隔
                colorBuffer);//顶点颜色数据缓冲
        gl.glPointSize(20);//设置绘制顶点的大小
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP,//绘制模型（点/线/三角形）
                0,//从数组缓存的那以为开始读取 一般为0
                vCount//顶点个数
        );
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
    }
}

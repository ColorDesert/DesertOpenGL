package com.desert.desertopengl;

import com.desert.desertopengl.utils.OpenGLUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by desert on 2017/12/3
 */

public class Lines extends OpenGLUtils {
    private IntBuffer verBuffer, colorBuffer;
    private ByteBuffer indBuffer;

    public Lines() {
        init();
    }

    private void init() {
        //定点个数
        int UNIT_SIZE = 10000;//缩放比列
        //顶点数据
        int ver[] = new int[]{
                -2 * UNIT_SIZE, 3 * UNIT_SIZE, 0,
                2 * UNIT_SIZE, 3 * UNIT_SIZE, 0,
                -2 * UNIT_SIZE, -3 * UNIT_SIZE, 0,
                2 * UNIT_SIZE, -3 * UNIT_SIZE, 0
        };
        //创建顶点缓冲数据
        verBuffer = getIntBuffer(ver);
        //顶点颜色
        int one = 65535;//支持65535色彩通道
        //顶点颜色数据
        int[] color = new int[]{
                one, 0, 0, 0,
                one, 0, 0, 0,
                one, 0, 0, 0,
                one, 0, 0, 0
        };
        //创建顶点颜色缓冲数据
        colorBuffer = getIntBuffer(color);
        byte[] index = new byte[]{0, 3, 2, 1};
        //创建索引缓冲
        indBuffer = getByteBuffer(index);
    }

    public void drawSelf(GL10 gl) {
        //开启顶点坐标数组
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        //开启颜色坐标数组
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        gl.glVertexPointer(
                3,//坐标个数（xyz）
                GL10.GL_FIXED,//数据类型
                0,//连续顶点坐标数据的间隔
                verBuffer);//顶点数据缓冲
        gl.glColorPointer(
                4,//颜色通道个数（RGB A）
                GL10.GL_FIXED,//数据类型
                0,//连续顶点坐标数据的间隔
                colorBuffer);//顶点颜色数据缓冲
        gl.glLineWidth(5);//设置线的宽度
        gl.glDrawElements(GL10.GL_LINE_LOOP,//绘制模型（点/线/三角形）
                4,//顶点个数
                GL10.GL_UNSIGNED_BYTE,//数据类型
                indBuffer);//索引缓冲
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
    }
}

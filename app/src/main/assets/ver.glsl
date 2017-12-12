uniform mat4 uMVPMatrix;//总变化矩阵
attribute vec3 aPosition;//顶点数据
attribute vec4 aColor;//顶点颜色数据
varying vec4 vColor;//传递色值给片元着色器
void main(){
 gl_Position=uMVPMatrix*vec4(aPosition,1);
 vColor=aColor;
}
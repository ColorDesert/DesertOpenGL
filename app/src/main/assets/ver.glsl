uniform mat4 vMVPMatrix;//总变化矩阵
attribute vec3 vPosition;//顶点数据
attribute vec4 vColor;//顶点颜色数据
varying vec4 aColor;//传递色值给片元着色器
void main(){
 gl_Position=vMVPMatrix*vec4(vPosition,1);
 aColor=vColor;
}
uniform mat4 vMatrix; //总变换矩阵
uniform float vStartAngle;//本帧起始角度
uniform float vWidthSpan;//横向长度总跨度
attribute vec3 vPosition;  //顶点位置
attribute vec2 vTexCoor;    //顶点纹理坐标
varying vec2 aTextureCoord;  //用于传递给片元着色器的变量
void main(){
//波浪大小
     float angleSpanH=5.0*3.14159265;
     //起始位置
     float startX=-vWidthSpan/2.0;
     //X变化当前角度
     float spanX=vStartAngle+((vPosition.x-startX)/vWidthSpan)*angleSpanH;
     float vHeightSpan=vWidthSpan*0.75;
     //起始位置
     float startY=-vHeightSpan/2.0;
     //Y变化当前角度
     float spanY=vStartAngle+((vPosition.y-startY)/vHeightSpan)*angleSpanH;
     float xz=cos(spanX);
     float yz=cos(spanY);
     float resultZ=xz*yz*0.2;
     gl_Position=vMatrix*vec4(vPosition.x,vPosition.y,resultZ,1);
     aTextureCoord=vTexCoor;
}                      
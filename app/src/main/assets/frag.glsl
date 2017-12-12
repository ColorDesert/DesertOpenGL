precision mediump float;//片元着色器 没有默认
varying vec4 vColor;//传递色值给片元着色器
void main(){
gl_FragColor=vColor;
}
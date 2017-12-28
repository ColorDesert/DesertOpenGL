precision mediump float;//片元着色器 没有默认
varying vec2 vTexture;//传递纹理坐标点
uniform sampler2D sTexture;
void main(){

gl_FragColor=texture2D(sTexture,vTexture);
}
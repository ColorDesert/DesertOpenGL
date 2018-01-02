precision mediump float;
varying vec2 aTextureCoord; //纹理坐标
uniform sampler2D sTexture;//采样器
void main(){
   //给此片元从纹理中采样出颜色值
   gl_FragColor =texture2D(sTexture, aTextureCoord);
}
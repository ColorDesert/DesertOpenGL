precision mediump float;
varying vec4 vDiffuse;//要传递的散射光变量
varying vec2 vTexture;
uniform sampler2D sTex;
void main() {
gl_FragColor=texture2D(sTex,vTexture)*vDiffuse;
}

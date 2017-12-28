uniform mat4 vMatrix;
attribute vec3 vPosition;
attribute vec2 vTexPos;
varying vec2 vTexture;
void main() {
 gl_Position=vMatrix*vec4(vPosition,1);
 vTexture=vTexPos;
}

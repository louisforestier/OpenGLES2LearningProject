#version 100
precision mediump float;
uniform mat4 uModelViewMatrix;
uniform mat4 uProjectionMatrix;
uniform mat3 uNormalMatrix;
// Material definition
uniform bool uNormalizing;
// vertex attributes
attribute vec3 aVertexPosition;
attribute vec3 aVertexNormal;
attribute vec2 aVertexTexture;
// Interpolated data

varying vec4 posf;
varying vec3 normalf;
varying vec2 texturef;

void main(void) {
  posf=uModelViewMatrix*vec4(aVertexPosition, 1.0);
  normalf=uNormalMatrix * aVertexNormal;
  if (uNormalizing) normalf=normalize(normalf);
  texturef = aVertexTexture;
  gl_Position= uProjectionMatrix*posf;
}

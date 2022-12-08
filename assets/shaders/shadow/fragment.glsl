#version 330 core
in vec4 FragPos;

uniform vec3 u_lightPos;
uniform float u_farPlane;

void main()
{
    float lightDistance = length(FragPos.xyz - u_lightPos);
    // Ubah menjadi [0,1] dari awalnya [0,farPlane].
    lightDistance = lightDistance / u_farPlane;
    gl_FragDepth = lightDistance;
}
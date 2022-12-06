#version 330 core

layout(location = 0) in vec3 l_position;
layout(location = 1) in vec3 l_normal;
layout(location = 2) in vec2 l_texCoords;

uniform mat4 u_model;

void main() {
    vec4 position = vec4(l_position, 1.0);
    gl_Position =  u_model * position;
}
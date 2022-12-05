#version 430 core

layout (location = 0) in vec3 l_position;

uniform mat4 u_projection;
uniform mat4 u_model;
uniform mat4 u_view;

out vec3 texCoords;

void main() {
    texCoords = l_position;
    vec4 pos = u_projection * u_model * u_view * vec4(l_position, 1.0);
    gl_Position = pos.xyww;
}
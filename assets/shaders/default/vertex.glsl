#version 430 core

layout(location = 0) in vec3 l_position;

uniform mat4 u_projection;
uniform mat4 u_model;
uniform mat4 u_view;

out vec4 color;
out vec3 stuff;

void main() {
    vec4 position = vec4(l_position, 1.0);
    gl_Position = u_projection * u_view * u_model * position;
//     gl_Position = u_model * position;
}
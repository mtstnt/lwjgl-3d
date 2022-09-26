#version 430 core

layout(location = 0) in vec3 l_position;

uniform mat4 u_projection;
uniform mat4 u_model;
uniform mat4 u_view;

out vec4 ccolor;

void main() {
    vec4 position = vec4(l_position, 1.0);
    gl_Position = u_projection * u_view * u_model * position;
    ccolor = vec4(u_view[0][0], 1.0, 0.0, 1.0);
}
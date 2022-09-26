#version 430 core

in vec4 ccolor;
out vec4 o_color;

void main() {
//     o_color = vec4(1.0, 0.0, 0.0, 1.0);
    o_color = ccolor;
}
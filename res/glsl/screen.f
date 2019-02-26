#version 330 core

uniform sampler2D u_texture0;
uniform sampler2D u_texture1;
uniform sampler2D u_texture2;

in vec2 v_texture;

layout (location = 0) out vec4 color;

void main()
{
    color = texture2D(u_texture0, v_texture);
}
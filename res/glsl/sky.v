#version 330 core

uniform mat4 u_mvp;

layout (location = 0) in vec3 a_position;
layout (location = 1) in vec2 a_texture;

out vec2 v_texture;

void main()
{
    v_texture = a_texture;

    vec4 mvp_position = u_mvp * vec4(a_position, 1.0);

    gl_Position = mvp_position.xyww;
}
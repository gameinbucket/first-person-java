#version 330 core

uniform mat4 u_mvp;

layout (location = 0) in vec3 a_position;
layout (location = 1) in vec2 a_texture;
layout (location = 2) in vec3 a_normal;
layout (location = 3) in vec3 a_tangent;
layout (location = 4) in vec3 a_bitangent;

out vec2 v_texture;
out vec3 v_normal;
out vec3 v_tangent;
out vec3 v_bitangent;

void main()
{
    v_texture = a_texture;

    v_normal = a_normal;
    v_tangent = a_tangent;
    v_bitangent = a_bitangent;

    // v_view_space_normal = (view_matrix * vec4(a_normal, 1.0)).xyz;
    
    gl_Position = (u_mvp * vec4(a_position, 1.0));
}
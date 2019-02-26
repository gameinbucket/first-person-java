#version 330 core

uniform sampler2D u_texture0;

in vec2 v_texture;

layout (location = 0) out vec4 final;

const vec3 ambient = vec3(0.90, 0.90, 0.90);

void main()
{
    final = vec4(texture2D(u_texture0, v_texture).rgb * ambient, 1.0);
}

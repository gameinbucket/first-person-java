#version 330 core

uniform sampler2D u_texture0;
uniform sampler2D u_texture1;

in vec2 v_texture;

layout (location = 0) out vec4 final;

void main()
{
    vec3 color = texture2D(u_texture0, v_texture).rgb;
    float shadow = texture2D(u_texture1, v_texture).r;
    
    final = vec4(color * shadow, 1.0);
}

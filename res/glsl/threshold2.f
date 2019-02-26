#version 330 core

uniform sampler2D u_texture0;

uniform float threshold;

in vec2 v_texture;

layout (location = 0) out vec4 final;

const vec3 luminosity = vec3(0.2126, 0.7152, 0.0722);

void main()
{
    vec3 color = texture2D(u_texture0, v_texture).rgb;
    
    if (dot(color, luminosity) > threshold)
        final = vec4(color, 1.0);
    else
        final = vec4(0.0, 0.0, 0.0, 1.0);
}

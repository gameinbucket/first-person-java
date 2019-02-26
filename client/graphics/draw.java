package client.graphics;

import land.things.base.decal;
import land.triangle;
import land.wall;

public abstract class draw
{
    private draw()
    {
        
    }
    
    private static void quad(buffer b)
    {
        b.indices[b.index_count++] = 0 + b.index_offset;
        b.indices[b.index_count++] = 1 + b.index_offset;
        b.indices[b.index_count++] = 2 + b.index_offset;
        b.indices[b.index_count++] = 2 + b.index_offset;
        b.indices[b.index_count++] = 3 + b.index_offset;
        b.indices[b.index_count++] = 0 + b.index_offset;
        
        b.index_offset += 4;
    }
    
    public static void screen(buffer b, float x, float y, float w, float h)
    {
        b.vertices[b.vertex_count++] = x;
        b.vertices[b.vertex_count++] = y;
        
        b.vertices[b.vertex_count++] = x;
        b.vertices[b.vertex_count++] = y + h;
        
        b.vertices[b.vertex_count++] = x + w;
        b.vertices[b.vertex_count++] = y + h;
        
        b.vertices[b.vertex_count++] = x + w;
        b.vertices[b.vertex_count++] = y;
        
        quad(b);
    }
    
    public static void image(buffer b, float x, float y, float w, float h, float u, float v, float s, float t)
    {
        b.vertices[b.vertex_count++] = x;
        b.vertices[b.vertex_count++] = y;
        b.vertices[b.vertex_count++] = u;
        b.vertices[b.vertex_count++] = v;
        
        b.vertices[b.vertex_count++] = x;
        b.vertices[b.vertex_count++] = y + h;
        b.vertices[b.vertex_count++] = u;
        b.vertices[b.vertex_count++] = t;
        
        b.vertices[b.vertex_count++] = x + w;
        b.vertices[b.vertex_count++] = y + h;
        b.vertices[b.vertex_count++] = s;
        b.vertices[b.vertex_count++] = t;
        
        b.vertices[b.vertex_count++] = x + w;
        b.vertices[b.vertex_count++] = y;
        b.vertices[b.vertex_count++] = s;
        b.vertices[b.vertex_count++] = v;
        
        quad(b);
    }
    
    public static void sprite(buffer b, float x, float y, float z, float sin, float cos, sprite sprite)
    {
        float sine   = sprite.width * sin;
        float cosine = sprite.width * cos;
        
        b.vertices[b.vertex_count++] = x - cosine;
        b.vertices[b.vertex_count++] = y + sprite.height;
        b.vertices[b.vertex_count++] = z + sine;
        b.vertices[b.vertex_count++] = 0.0f;
        b.vertices[b.vertex_count++] = 0.0f;
        b.vertices[b.vertex_count++] = sin;
        b.vertices[b.vertex_count++] = 0.0f;
        b.vertices[b.vertex_count++] = cos;
        b.vertices[b.vertex_count++] = -cos; // tangent
        b.vertices[b.vertex_count++] = 0.0f;
        b.vertices[b.vertex_count++] = sin;
        b.vertices[b.vertex_count++] = 0.0f; // bitangent
        b.vertices[b.vertex_count++] = 1.0f;
        b.vertices[b.vertex_count++] = 0.0f;
        
        b.vertices[b.vertex_count++] = x - cosine;
        b.vertices[b.vertex_count++] = y;
        b.vertices[b.vertex_count++] = z + sine;
        b.vertices[b.vertex_count++] = 0.0f;
        b.vertices[b.vertex_count++] = 1.0f;
        b.vertices[b.vertex_count++] = sin;
        b.vertices[b.vertex_count++] = 0.0f;
        b.vertices[b.vertex_count++] = cos;
        b.vertices[b.vertex_count++] = -cos; // tangent
        b.vertices[b.vertex_count++] = 0.0f;
        b.vertices[b.vertex_count++] = sin;
        b.vertices[b.vertex_count++] = 0.0f; // bitangent
        b.vertices[b.vertex_count++] = 0.0f;
        b.vertices[b.vertex_count++] = 0.0f;
        
        b.vertices[b.vertex_count++] = x + cosine;
        b.vertices[b.vertex_count++] = y;
        b.vertices[b.vertex_count++] = z - sine;
        b.vertices[b.vertex_count++] = 1.0f;
        b.vertices[b.vertex_count++] = 1.0f;
        b.vertices[b.vertex_count++] = sin;
        b.vertices[b.vertex_count++] = 0.0f;
        b.vertices[b.vertex_count++] = cos;
        b.vertices[b.vertex_count++] = -cos; // tangent
        b.vertices[b.vertex_count++] = 0.0f;
        b.vertices[b.vertex_count++] = sin;
        b.vertices[b.vertex_count++] = 0.0f; // bitangent
        b.vertices[b.vertex_count++] = 0.0f;
        b.vertices[b.vertex_count++] = 0.0f;
        
        b.vertices[b.vertex_count++] = x + cosine;
        b.vertices[b.vertex_count++] = y + sprite.height;
        b.vertices[b.vertex_count++] = z - sine;
        b.vertices[b.vertex_count++] = 1.0f;
        b.vertices[b.vertex_count++] = 0.0f;
        b.vertices[b.vertex_count++] = sin;
        b.vertices[b.vertex_count++] = 0.0f;
        b.vertices[b.vertex_count++] = cos;
        b.vertices[b.vertex_count++] = -cos; // tangent
        b.vertices[b.vertex_count++] = 0.0f;
        b.vertices[b.vertex_count++] = sin;
        b.vertices[b.vertex_count++] = 0.0f; // bitangent
        b.vertices[b.vertex_count++] = 0.0f;
        b.vertices[b.vertex_count++] = 0.0f;
        
        quad(b);
    }
    
    public static void wall(buffer b, wall w)
    {
        b.vertices[b.vertex_count++] = w.a.x;
        b.vertices[b.vertex_count++] = w.ceil;
        b.vertices[b.vertex_count++] = w.a.y;
        b.vertices[b.vertex_count++] = w.u;
        b.vertices[b.vertex_count++] = w.t;
        b.vertices[b.vertex_count++] = w.def.nx;
        b.vertices[b.vertex_count++] = 0.0f;
        b.vertices[b.vertex_count++] = w.def.ny;
        b.vertices[b.vertex_count++] = -w.def.ny; // tangent
        b.vertices[b.vertex_count++] = 0.0f;
        b.vertices[b.vertex_count++] = w.def.nx;
        b.vertices[b.vertex_count++] = 0.0f; // bitangent
        b.vertices[b.vertex_count++] = 1.0f;
        b.vertices[b.vertex_count++] = 0.0f;
        
        b.vertices[b.vertex_count++] = w.a.x;
        b.vertices[b.vertex_count++] = w.floor;
        b.vertices[b.vertex_count++] = w.a.y;
        b.vertices[b.vertex_count++] = w.u;
        b.vertices[b.vertex_count++] = w.v;
        b.vertices[b.vertex_count++] = w.def.nx;
        b.vertices[b.vertex_count++] = 0.0f;
        b.vertices[b.vertex_count++] = w.def.ny;
        b.vertices[b.vertex_count++] = -w.def.ny; // tangent
        b.vertices[b.vertex_count++] = 0.0f;
        b.vertices[b.vertex_count++] = w.def.nx;
        b.vertices[b.vertex_count++] = 0.0f; // bitangent
        b.vertices[b.vertex_count++] = 1.0f;
        b.vertices[b.vertex_count++] = 0.0f;
        
        b.vertices[b.vertex_count++] = w.b.x;
        b.vertices[b.vertex_count++] = w.floor;
        b.vertices[b.vertex_count++] = w.b.y;
        b.vertices[b.vertex_count++] = w.s;
        b.vertices[b.vertex_count++] = w.v;
        b.vertices[b.vertex_count++] = w.def.nx;
        b.vertices[b.vertex_count++] = 0.0f;
        b.vertices[b.vertex_count++] = w.def.ny;
        b.vertices[b.vertex_count++] = -w.def.ny; // tangent
        b.vertices[b.vertex_count++] = 0.0f;
        b.vertices[b.vertex_count++] = w.def.nx;
        b.vertices[b.vertex_count++] = 0.0f; // bitangent
        b.vertices[b.vertex_count++] = 1.0f;
        b.vertices[b.vertex_count++] = 0.0f;
        
        b.vertices[b.vertex_count++] = w.b.x;
        b.vertices[b.vertex_count++] = w.ceil;
        b.vertices[b.vertex_count++] = w.b.y;
        b.vertices[b.vertex_count++] = w.s;
        b.vertices[b.vertex_count++] = w.t;
        b.vertices[b.vertex_count++] = w.def.nx;
        b.vertices[b.vertex_count++] = 0.0f;
        b.vertices[b.vertex_count++] = w.def.ny;
        b.vertices[b.vertex_count++] = -w.def.ny; // tangent
        b.vertices[b.vertex_count++] = 0.0f;
        b.vertices[b.vertex_count++] = w.def.nx;
        b.vertices[b.vertex_count++] = 0.0f; // bitangent
        b.vertices[b.vertex_count++] = 1.0f;
        b.vertices[b.vertex_count++] = 0.0f;
        
        quad(b);
    }
    
    public static void triangle(buffer b, triangle t)
    {
        b.vertices[b.vertex_count++] = t.c.x;
        b.vertices[b.vertex_count++] = t.height;
        b.vertices[b.vertex_count++] = t.c.y;
        b.vertices[b.vertex_count++] = t.u3;
        b.vertices[b.vertex_count++] = t.v3;
        b.vertices[b.vertex_count++] = 0.0f;
        b.vertices[b.vertex_count++] = t.normal;
        b.vertices[b.vertex_count++] = 0.0f;
        b.vertices[b.vertex_count++] = t.normal; // tangent
        b.vertices[b.vertex_count++] = 0.0f;
        b.vertices[b.vertex_count++] = 0.0f;
        b.vertices[b.vertex_count++] = 0.0f; // bitangent
        b.vertices[b.vertex_count++] = 0.0f;
        b.vertices[b.vertex_count++] = t.normal;
        
        b.vertices[b.vertex_count++] = t.b.x;
        b.vertices[b.vertex_count++] = t.height;
        b.vertices[b.vertex_count++] = t.b.y;
        b.vertices[b.vertex_count++] = t.u2;
        b.vertices[b.vertex_count++] = t.v2;
        b.vertices[b.vertex_count++] = 0.0f;
        b.vertices[b.vertex_count++] = t.normal;
        b.vertices[b.vertex_count++] = 0.0f;
        b.vertices[b.vertex_count++] = t.normal; // tangent
        b.vertices[b.vertex_count++] = 0.0f;
        b.vertices[b.vertex_count++] = 0.0f;
        b.vertices[b.vertex_count++] = 0.0f; // bitangent
        b.vertices[b.vertex_count++] = 0.0f;
        b.vertices[b.vertex_count++] = t.normal;
        
        b.vertices[b.vertex_count++] = t.a.x;
        b.vertices[b.vertex_count++] = t.height;
        b.vertices[b.vertex_count++] = t.a.y;
        b.vertices[b.vertex_count++] = t.u1;
        b.vertices[b.vertex_count++] = t.v1;
        b.vertices[b.vertex_count++] = 0.0f;
        b.vertices[b.vertex_count++] = t.normal;
        b.vertices[b.vertex_count++] = 0.0f;
        b.vertices[b.vertex_count++] = t.normal; // tangent
        b.vertices[b.vertex_count++] = 0.0f;
        b.vertices[b.vertex_count++] = 0.0f;
        b.vertices[b.vertex_count++] = 0.0f; // bitangent
        b.vertices[b.vertex_count++] = 0.0f;
        b.vertices[b.vertex_count++] = t.normal;
        
        b.indices[b.index_count++] = 0 + b.index_offset;
        b.indices[b.index_count++] = 1 + b.index_offset;
        b.indices[b.index_count++] = 2 + b.index_offset;
        
        b.index_offset += 3;
    }
    
    public static void decal(buffer b, decal d)
    {
        b.vertices[b.vertex_count++] = d.x1;
        b.vertices[b.vertex_count++] = d.z1;
        b.vertices[b.vertex_count++] = d.y1;
        b.vertices[b.vertex_count++] = d.u1;
        b.vertices[b.vertex_count++] = d.v1;
        b.vertices[b.vertex_count++] = d.nx;
        b.vertices[b.vertex_count++] = d.nz;
        b.vertices[b.vertex_count++] = d.ny;
        b.vertices[b.vertex_count++] = 0.0f; // tangent
        b.vertices[b.vertex_count++] = 0.0f;
        b.vertices[b.vertex_count++] = 0.0f;
        b.vertices[b.vertex_count++] = 0.0f; // bitangent
        b.vertices[b.vertex_count++] = 0.0f;
        b.vertices[b.vertex_count++] = 0.0f;
        
        b.vertices[b.vertex_count++] = d.x2;
        b.vertices[b.vertex_count++] = d.z2;
        b.vertices[b.vertex_count++] = d.y2;
        b.vertices[b.vertex_count++] = d.u2;
        b.vertices[b.vertex_count++] = d.v2;
        b.vertices[b.vertex_count++] = d.nx;
        b.vertices[b.vertex_count++] = d.nz;
        b.vertices[b.vertex_count++] = d.ny;
        b.vertices[b.vertex_count++] = 0.0f; // tangent
        b.vertices[b.vertex_count++] = 0.0f;
        b.vertices[b.vertex_count++] = 0.0f;
        b.vertices[b.vertex_count++] = 0.0f; // bitangent
        b.vertices[b.vertex_count++] = 0.0f;
        b.vertices[b.vertex_count++] = 0.0f;
        
        b.vertices[b.vertex_count++] = d.x3;
        b.vertices[b.vertex_count++] = d.z3;
        b.vertices[b.vertex_count++] = d.y3;
        b.vertices[b.vertex_count++] = d.u3;
        b.vertices[b.vertex_count++] = d.v3;
        b.vertices[b.vertex_count++] = d.nx;
        b.vertices[b.vertex_count++] = d.nz;
        b.vertices[b.vertex_count++] = d.ny;
        b.vertices[b.vertex_count++] = 0.0f; // tangent
        b.vertices[b.vertex_count++] = 0.0f;
        b.vertices[b.vertex_count++] = 0.0f;
        b.vertices[b.vertex_count++] = 0.0f; // bitangent
        b.vertices[b.vertex_count++] = 0.0f;
        b.vertices[b.vertex_count++] = 0.0f;
        
        b.vertices[b.vertex_count++] = d.x4;
        b.vertices[b.vertex_count++] = d.z4;
        b.vertices[b.vertex_count++] = d.y4;
        b.vertices[b.vertex_count++] = d.u4;
        b.vertices[b.vertex_count++] = d.v4;
        b.vertices[b.vertex_count++] = d.nx;
        b.vertices[b.vertex_count++] = d.nz;
        b.vertices[b.vertex_count++] = d.ny;
        b.vertices[b.vertex_count++] = 0.0f; // tangent
        b.vertices[b.vertex_count++] = 0.0f;
        b.vertices[b.vertex_count++] = 0.0f;
        b.vertices[b.vertex_count++] = 0.0f; // bitangent
        b.vertices[b.vertex_count++] = 0.0f;
        b.vertices[b.vertex_count++] = 0.0f;
        
        quad(b);
    }
    
    public static void cube(buffer b, float x, float y, float z, float radius)
    {
        b.vertices[b.vertex_count++] = x - radius;
        b.vertices[b.vertex_count++] = y - radius;
        b.vertices[b.vertex_count++] = z - radius;
        
        b.vertices[b.vertex_count++] = x + radius;
        b.vertices[b.vertex_count++] = y - radius;
        b.vertices[b.vertex_count++] = z - radius;
        
        b.vertices[b.vertex_count++] = x - radius;
        b.vertices[b.vertex_count++] = y + radius;
        b.vertices[b.vertex_count++] = z - radius;
        
        b.vertices[b.vertex_count++] = x + radius;
        b.vertices[b.vertex_count++] = y + radius;
        b.vertices[b.vertex_count++] = z - radius;
        
        b.vertices[b.vertex_count++] = x - radius;
        b.vertices[b.vertex_count++] = y - radius;
        b.vertices[b.vertex_count++] = z + radius;
        
        b.vertices[b.vertex_count++] = x + radius;
        b.vertices[b.vertex_count++] = y - radius;
        b.vertices[b.vertex_count++] = z + radius;
        
        b.vertices[b.vertex_count++] = x - radius;
        b.vertices[b.vertex_count++] = y + radius;
        b.vertices[b.vertex_count++] = z + radius;
        
        b.vertices[b.vertex_count++] = x + radius;
        b.vertices[b.vertex_count++] = y + radius;
        b.vertices[b.vertex_count++] = z + radius;
        
        b.indices[b.index_count++] = 3; // x back
        b.indices[b.index_count++] = 1;
        b.indices[b.index_count++] = 0;
        
        b.indices[b.index_count++] = 0;
        b.indices[b.index_count++] = 2;
        b.indices[b.index_count++] = 3;
        
        b.indices[b.index_count++] = 4; // x front
        b.indices[b.index_count++] = 5;
        b.indices[b.index_count++] = 7;
        
        b.indices[b.index_count++] = 7;
        b.indices[b.index_count++] = 6;
        b.indices[b.index_count++] = 4;
        
        b.indices[b.index_count++] = 0; // z back
        b.indices[b.index_count++] = 4;
        b.indices[b.index_count++] = 6;
        
        b.indices[b.index_count++] = 6;
        b.indices[b.index_count++] = 2;
        b.indices[b.index_count++] = 0;
        
        b.indices[b.index_count++] = 7; // z front
        b.indices[b.index_count++] = 5;
        b.indices[b.index_count++] = 1;
        
        b.indices[b.index_count++] = 1;
        b.indices[b.index_count++] = 3;
        b.indices[b.index_count++] = 7;
        
        b.indices[b.index_count++] = 0; // bottom
        b.indices[b.index_count++] = 1;
        b.indices[b.index_count++] = 5;
        
        b.indices[b.index_count++] = 5;
        b.indices[b.index_count++] = 4;
        b.indices[b.index_count++] = 0;
        
        b.indices[b.index_count++] = 7; // top
        b.indices[b.index_count++] = 3;
        b.indices[b.index_count++] = 2;
        
        b.indices[b.index_count++] = 2;
        b.indices[b.index_count++] = 6;
        b.indices[b.index_count++] = 7;
        
        b.index_offset += 36;
    }
    
    public static void inverse_cube(buffer b)
    {
        
    }
}

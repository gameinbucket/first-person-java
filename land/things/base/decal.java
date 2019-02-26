
package land.things.base;

import land.map;

public class decal
{
    public int texture;
    
    public float x1;
    public float y1;
    public float z1;
    
    public float u1;
    public float v1;
    
    public float x2;
    public float y2;
    public float z2;
    
    public float u2;
    public float v2;
    
    public float x3;
    public float y3;
    public float z3;
    
    public float u3;
    public float v3;
    
    public float x4;
    public float y4;
    public float z4;
    
    public float u4;
    public float v4;
    
    public float nx;
    public float ny;
    public float nz;
    
    public decal(map map)
    {
        map.add_decal(this);
    }
}

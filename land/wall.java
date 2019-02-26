
package land;

public class wall
{
    public linedef def;
    
    public vector a;
    public vector b;
    
    public int texture;
    
    public float floor;
    public float ceil;
    
    public float u;
    public float v;
    public float s;
    public float t;
    
    public wall(linedef def, vector a, vector b, int texture)
    {
        this.def = def;
        
        this.a = a;
        this.b = b;
        
        this.texture = texture;
    }
    
    public void set(float floor, float ceil, float u, float v, float s, float t)
    {
        this.floor = floor;
        this.ceil = ceil;
        
        this.u = u;
        this.v = v;
        this.s = s;
        this.t = t;
    }
}


package land.things.base;

import client.graphics.sprite;
import land.map;

public class particle extends entity
{
    public map map;
    
    public boolean gc;
    
    public float dx;
    public float dy;
    public float dz;

    public float height;
    
    public sprite image;
    
    public particle(map map, float x, float y, float z, float radius, float height)
    {
        this.map = map;
        
        this.x = x;
        this.y = y;
        this.z = z;
        
        this.radius = radius;
        this.height = height;
        
        map.add_particle(this);
    }
    
    public void integrate()
    {
        
    }
    
    public sprite sprite()
    {
        return image;
    }
}

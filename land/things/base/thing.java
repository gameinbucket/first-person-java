
package land.things.base;

import land.map;
import land.sector;

public abstract class thing
{
    private static int unique_index;
    
    public int uid;
    
    public map map;
    public sector sector;
    
    public float x;
    public float y;
    public float z;
    
    public thing(map map, float x, float y)
    {
        uid = unique_index++;
        
        sector = map.find_sector(x, y);
        
        this.map = map;
        
        this.x = x;
        this.y = y;
        this.z = sector.floor;
    }
}

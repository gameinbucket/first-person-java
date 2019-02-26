package land;

import land.things.thing;

public abstract class map_util
{
    private map_util()
    {
        
    }
    
    public static float approximate_distance(thing a, thing b)
    {
        float dx = Math.abs(a.x - b.x);
        float dy = Math.abs(a.y - b.y);
        
        if (dx > dy)
            return dx + dy - dy / 2;
        else
            return dx + dy - dx / 2;
    }
}

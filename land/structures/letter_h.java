
package land.structures;

import client.state;
import land.linedef;
import land.sector;
import land.vector;

public abstract class letter_h
{
    private letter_h()
    {
        
    }
    
    public static sector make(state s)
    {
        vector[] p =
        {   
            new vector(0, 0),
            new vector(0, 20),
            new vector(5, 20),
            new vector(5, 15),
            new vector(10, 15),
            new vector(10, 20),
            new vector(15, 20),
            new vector(15, 0),
            new vector(10, 0),
            new vector(10, 10),
            new vector(5, 10),
            new vector(5, 0),
        };
        
        linedef[] lines = null;
        
        sector sec = new sector(p, lines);
        sec.bottom(0);
        sec.floor(0);
        sec.ceil(0);
        sec.top(0);
        sec.floor_texture(s.t_grass);
        sec.ceil_texture(-1);
        
        s.land.add_sector(sec);
        
        return sec;
    }
}

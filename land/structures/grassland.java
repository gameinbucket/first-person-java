
package land.structures;

import client.state;
import land.linedef;
import land.sector;
import land.vector;

public abstract class grassland
{
    private grassland()
    {
        
    }
    
    public static sector make(state s)
    {
        vector[] p =
        {
            new vector(0, 20),
            new vector(0, 60),
            new vector(20, 80),
            new vector(40, 60),
            new vector(40, 20),
            new vector(20, 0),
        };
        
        linedef[] lines = new linedef[p.length];
        
        int j = p.length - 1;
        
        for (int i = 0; i < p.length; i++)
        {
            lines[i] = new linedef(p[i], p[j], -1, s.t_stone, -1);
            
            j = i;
        }
        
        sector sec = new sector(p, lines);
        sec.bottom(0);
        sec.floor(0);
        sec.ceil(5);
        sec.top(0);
        sec.floor_texture(s.t_grass);
        sec.ceil_texture(s.t_grass);
        
        s.land.add_sector(sec);
        
        return sec;
    }
}


package land.structures;

import client.state;
import land.linedef;
import land.sector;
import land.vector;

public abstract class box
{
    private box()
    {
        
    }
    
    public static sector make(state s, int x, int y)
    {
        vector[] p =
        {
            new vector(x, y),
            new vector(x, y + 4),
            new vector(x + 4, y + 4),
            new vector(x + 4, y),
        };
        
        linedef[] lines = new linedef[p.length];
        
        int j = p.length - 1;
        
        for (int i = 0; i < p.length; i++)
        {
            lines[i] = new linedef(p[j], p[i], s.t_grass, -1, -1);
            
            j = i;
        }
        
        sector sec = new sector(p, lines);
        sec.bottom(0);
        sec.floor(0.5f);
        sec.ceil(0);
        sec.top(0);
        sec.floor_texture(s.t_stone_floor);
        sec.ceil_texture(-1);
        sec.cutout(true, false);
        
        s.land.add_sector(sec);
        
        return sec;
    }
}

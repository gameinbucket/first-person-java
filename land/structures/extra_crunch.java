
package land.structures;

import client.state;
import land.linedef;
import land.sector;
import land.vector;

public abstract class extra_crunch
{
    private extra_crunch()
    {
        
    }
    
    public static sector make(state s, int x, int y)
    {
        vector[] p =
        {
            new vector(x, y),
            new vector(x - 1, y + 2),
            new vector(x, y + 4),
            new vector(x + 1, y + 3),
            new vector(x + 2, y + 4),
            new vector(x + 3, y + 2),
            new vector(x + 2, y),
            new vector(x + 1, y + 1),
        };
        
        linedef[] lines = new linedef[p.length];
        
        int j = p.length - 1;
        
        for (int i = 0; i < p.length; i++)
        {
            lines[i] = new linedef(p[j], p[i], s.t_grass, s.t_plank_floor, s.t_planks);
            
            j = i;
        }
        
        sector sec = new sector(p, lines);
        sec.bottom(0);
        sec.floor(1);
        sec.ceil(3);
        sec.top(5);
        sec.floor_texture(-1);
        sec.ceil_texture(-1);
        
        s.land.add_sector(sec);
        
        return sec;
    }
}

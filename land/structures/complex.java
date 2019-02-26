
package land.structures;

import client.state;
import land.linedef;
import land.sector;
import land.vector;

public abstract class complex
{
    private complex()
    {
        
    }
    
    public static sector make(state s, int x, int y)
    {
        vector[] p =
        {
            new vector(x + 0, 0 + y),
            new vector(x + 0, 5 + y),
            new vector(x + 3, 5 + y),
            new vector(x + 5, 10 + y),
            new vector(x + 10, 10 + y),
            new vector(x + 10, 5 + y),
            new vector(x + 5, 5 + y),
            new vector(x + 5, 0 + y),
        };
        
        linedef[] lines = new linedef[p.length];
        
        int j = p.length - 1;
        
        for (int i = 0; i < p.length; i++)
        {
            lines[i] = new linedef(p[j], p[i], s.t_stone, -1, -1);
            
            j = i;
        }
        
        sector sec = new sector(p, lines);
        sec.bottom(0);
        sec.floor(1.6f);
        sec.ceil(0);
        sec.top(0);
        sec.floor_texture(s.t_stone_floor);
        sec.ceil_texture(-1);
        sec.cutout(true, false);
        
        s.land.add_sector(sec);
        
        return sec;
    }
}

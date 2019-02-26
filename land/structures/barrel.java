
package land.structures;

import client.state;
import land.linedef;
import land.sector;
import land.vector;

public abstract class barrel
{
    private barrel()
    {
        
    }
    
    public static sector make(state s, int x, int y)
    {
        vector[] p =
        {
            new vector(x, y),
            new vector(x, y + 1),
            new vector(x + 1, y + 1),
            new vector(x + 1, y),
        };
        
        linedef[] lines = new linedef[p.length];
        
        int j = p.length - 1;
        
        for (int i = 0; i < p.length; i++)
        {
            lines[i] = new linedef(p[j], p[i], s.t_planks, -1, s.t_stone);
            
            j = i;
        }
        
        sector sec = new sector(p, lines);
        sec.bottom(0);
        sec.floor(-2);
        sec.ceil(7);
        sec.top(5);
        sec.floor_texture(s.t_plank_floor);
        sec.ceil_texture(s.t_stone_floor);
        
        s.land.add_sector(sec);
        
        return sec;
    }
}

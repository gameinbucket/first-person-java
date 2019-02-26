
package land.structures;

import client.state;
import land.linedef;
import land.sector;
import land.vector;

public abstract class house
{
    private house()
    {
        
    }
    
    public static sector[] make(state s, int x, int y)
    {
        vector[] p =
        {
            new vector(x, y),
            new vector(x, y + 20),
            new vector(x + 6, y + 20), // 2
            new vector(x + 6, y + 19),
            new vector(x + 1, y + 19),
            new vector(x + 1, y + 1),
            new vector(x + 19, y + 1),
            new vector(x + 19, y + 19),
            new vector(x + 14, y + 19),
            new vector(x + 14, y + 20), // 9
            new vector(x + 20, y + 20),
            new vector(x + 20, y),
        };
        
        vector[] vec_in = {p[2], p[9], p[8], p[7], p[6], p[5], p[4], p[3]};
        
        linedef[] lines = new linedef[p.length];
        
        int j = p.length - 1;
        
        for (int i = 0; i < p.length; i++)
        {
            lines[i] = new linedef(p[j], p[i], -1, s.t_planks, -1);
            
            j = i;
        }
        
        linedef[] def_in = new linedef[1];
        
        def_in[0] = new linedef(p[2], p[9], -1, -1, s.t_stone);
        
        sector outside = new sector(p, lines);
        outside.bottom(0);
        outside.floor(0);
        outside.ceil(6);
        outside.top(0);
        outside.floor_texture(-1);
        outside.ceil_texture(-1);
        
        sector inside = new sector(vec_in, def_in);
        inside.bottom(0);
        inside.floor(0);
        inside.ceil(5);
        inside.top(6);
        inside.floor_texture(s.t_plank_floor);
        inside.ceil_texture(s.t_plank_floor);
        
        sector[] sectors = {outside, inside};
        
        s.land.add_sector(outside);
        s.land.add_sector(inside);
        
        return sectors;
    }
}

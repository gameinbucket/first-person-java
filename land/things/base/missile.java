
package land.things.base;

import client.graphics.sprite;
import client.audio.sound;
import land.linedef;
import land.map;
import land.map_cell;
import land.sector;
import land.things.thing;

public class missile extends entity
{
    public map map;
    public sector sector;
    
    public boolean gc = false;
    
    public sprite image;
    public String impact_sound;
    
    public float dx;
    public float dy;
    public float dz;
    
    public float height;
    
    public int damage;
    public thing from;
    
    public missile(map map, float x, float y, float z, float radius, float height, float dx, float dy, float dz, int damage)
    {
        this.map = map;
        this.sector = map.find_sector(x, y);
        
        this.x = x;
        this.y = y;
        this.z = z;
        
        this.radius = radius;
        this.height = height;
        
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;
        
        this.damage = damage;
        
        map.add_missile(this);
    }
    
    public void hit(thing t)
    {
        if (t != null)
            t.damage(damage);
        
        new sound(impact_sound).start();
        
        gc = true;
    }
    
    protected boolean overlap(thing b)
    {
        float block = radius + b.radius;
        
        if (Math.abs(x - b.x) > block || Math.abs(y - b.y) > block)
            return false;
        
        return true;
    }
    
    protected boolean overlap(linedef line)
    {
        float vx = line.b.x - line.a.x;
        float vy = line.b.y - line.a.y;
        
        float wx = x - line.a.x;
        float wy = y - line.a.y;
        
        float t = (wx * vx + wy * vy) / (vx * vx + vy * vy);
        
        if (t < 0)
            t = 0;
        else if (t > 1)
            t = 1;
        
        float px = line.a.x + vx * t;
        float py = line.a.y + vy * t;
        
        px -= x;
        py -= y;
        
        if (px * px + py * py > radius * radius)
            return false;
        
        if (line.mid != null || z < line.plus.floor || z + height > line.plus.ceil)
            return true;
        
        return false;
    }
    
    private boolean overlap()
    {
        if (z < sector.floor || z + height > sector.ceil)
        {
            hit(null);
            return true;
        }
        
        int c_min = (int)(x - radius) >> map.shift;
        int r_min = (int)(y - radius) >> map.shift;
        
        int c_max = (int)(x + radius) >> map.shift;
        int r_max = (int)(y + radius) >> map.shift;
        
        if (r_min < 0 || c_min < 0 || r_max >= map.rows || c_max >= map.columns)
        {
            hit(null);
            return true;
        }
        
        for (int r = r_min; r <= r_max; r++)
        {
            for (int c = c_min; c <= c_max; c++)
            {
                map_cell cell = map.cells[c + r * map.columns];
                
                for (int thing = 0; thing < cell.thing_count; thing++)
                {
                    thing t = cell.things[thing];
                    
                    if (overlap(t))
                    {
                        hit(t);
                        return true;
                    }
                }
                
                for (int line = 0; line < cell.lines.length; line++)
                {
                    if (overlap(cell.lines[line]))
                    {
                        hit(null);
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    public void integrate()
    {
        if (overlap())
            return;
        
        x += dx;
        y += dy;
        z += dz;
        
        overlap();
    }
    
    public sprite sprite()
    {
        return image;
    }
}

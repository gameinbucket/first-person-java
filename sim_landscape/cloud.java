package sim_landscape;


public class cloud
{
    public int x;
    public int y;
    
    public int xx = 0;
    public int yy = 0;
    
    public int dx = 0;
    public int dy = 0;
    
    public int moisture = 100;
    
    public cloud(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    
    public boolean update(planet p)
    {
        hexagon h = p.hex(x, y);
        
        double radian = h.wind * Math.PI / 180.0;
        
        int nx = (int)(Math.cos(radian) * 20);
        int ny = (int)(Math.sin(radian) * 20);
        
        h.precipitation++;
        
        if (h.precipitation > 100) h.precipitation = 100;
        
        moisture--;
        
        dx += nx;
        dy += ny;
        
        dx = dx * 95 / 100;
        dy = dy * 95 / 100;
        
        xx += dx;
        yy += dy;
        
        if (xx > 100)
        {
            xx -= 200;
            x++;
            
            if (x > p.width) x -= p.width;
        }
        else if (xx <= -100)
        {
            xx += 200;
            x--;
            
            if (x < 0) x += p.width;
        }
        
        if (yy > 100)
        {
            yy -= 200;
            y++;
            
            if (y > p.height) y -= p.height;
        }
        else if (yy <= -100)
        {
            yy += 200;
            y--;
            
            if (y < 0) y += p.height;
        }
        
        return moisture < 0;
    }
}

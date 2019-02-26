
package land;

import java.util.HashSet;

public class sector
{
    private static int unique_index = 0;
    
    public final int uid;
    
    public float bottom = 0;
    public float floor = 0;
    public float ceil = 0;
    public float top = 0;
    
    public int t_floor;
    public int t_ceil;
    
    public vector[] points;
    public linedef[] lines;
    public triangle[] triangles;
    public sector outside = null;
    
    public boolean c_floor = true;
    public boolean c_ceil = true;
    
    public HashSet<sector> inside = new HashSet<>();

    public sector(vector[] points, linedef[] lines)
    {
        uid = unique_index++;
        
        this.points = points;
        this.lines = lines;
    }
    
    public void bottom(float value)
    {
        bottom = value;
    }
    
    public void floor(float value)
    {
        floor = value;
    }
    
    public void ceil(float value)
    {
        ceil = value;
    }
    
    public void top(float value)
    {
        top = value;
    }
    
    public void floor_texture(int value)
    {
        t_floor = value;
    }
    
    public void ceil_texture(int value)
    {
        t_ceil = value;
    }
    
    public void cutout(boolean f, boolean c)
    {
        c_floor = f;
        c_ceil = c;
    }
    
    public sector find(float x, float y)
    {
        for (sector o : inside)
        {
            if (o.contains(x, y))
                return o.find(x, y);
        }
        
        return this;
    }
    
    public boolean contains(float x, float y)
    {
        boolean odd = false;
        
        int j = points.length - 1;
        
        for (int i = 0; i < points.length; i++)
        {
            if ((points[i].y > y != points[j].y > y) && (x < (points[j].x - points[i].x) * (double)(y - points[i].y) / (double)(points[j].y - points[i].y) + points[i].x))
                odd = !odd;
            
            j = i;
        }
        
        return odd;
    }
}


package land;

public class linedef
{
    public sector plus;
    public sector minus;
    
    public vector a;
    public vector b;
    
    public wall bot;
    public wall mid;
    public wall top;
    
    public float nx;
    public float ny;
    
    public linedef(vector a, vector b, int l, int m, int t)
    {
        this.a = a;
        this.b = b;
        
        if (l > -1) bot = new wall(this, a, b, l);
        if (m > -1) mid = new wall(this, a, b, m);
        if (t > -1) top = new wall(this, a, b, t);
    }
    
    public void set(sector p, sector m)
    {
        plus = p;
        minus = m;
        
        nx = a.y - b.y;
        ny = -(a.x - b.x);
        
        float nm = (float)Math.sqrt(nx * nx + ny * ny);
        
        nx /= nm;
        ny /= nm;
    }
    
    public vector intersect(linedef l)
    {
        double a1 = b.y - a.y;
        double b1 = a.x - b.x;
        double c1 = (b.x * a.y) - (a.x * b.y);
        
        double r3 = ((a1 * l.a.x) + (b1 * l.a.y) + c1);
        double r4 = ((a1 * l.b.x) + (b1 * l.b.y) + c1);
        
        if ((r3 != 0) && (r4 != 0) && r3 * r4 >= 0) return null;
        
        double a2 = l.b.y - l.a.y;
        double b2 = l.a.x - l.b.x;
        double c2 = (l.b.x * l.a.y) - (l.a.x * l.b.y);
        
        double r1 = (a2 * a.x) + (b2 * a.y) + c2;
        double r2 = (a2 * b.x) + (b2 * b.y) + c2;
        
        if ((r1 != 0) && (r2 != 0) && r1 * r2 >= 0) return null;
        
        double denom = (a1 * b2) - (a2 * b1);
        if (denom == 0) return null;
        
        double offset;
        double x;
        double y;
        
        if (denom < 0) offset = -denom / 2;
        else offset = denom / 2;
        
        double num = (b1 * c2) - (b2 * c1);
        if (num < 0) x = (num - offset) / denom;
        else x = (num + offset) / denom;
        
        num = (a2 * c1) - (a1 * c2);
        if (num < 0) y = ( num - offset) / denom;
        else y = (num + offset) / denom;
        
        return new vector((int)x, (int)y);
    }
}

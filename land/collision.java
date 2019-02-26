package land;

import land.things.thing;

public class collision
{
    public thing thing_1;
    public thing thing_2;
    
    public float overlap;
    
    public float normal_x;
    public float normal_y;
    
    public collision()
    {
        
    }
    
    public void set(thing thing_1, thing thing_2, float overlap, float normal_x, float normal_y)
    {
        this.thing_1 = thing_1;
        this.thing_2 = thing_2;
        
        this.overlap = overlap;
        
        this.normal_x = normal_x;
        this.normal_y = normal_y;
    }
    
    public void resolve_overlap(vector linear_1, vector linear_2)
    {
        if (thing_2 == null)
        {
            linear_1.x += normal_x * overlap;
            linear_1.y += normal_y * overlap;
            
            thing_1.x += linear_1.x;
            thing_1.y += linear_1.y;
        }
        else
        {
            float inverse_inertia = overlap / (thing_1.mass + thing_2.mass);
            
            float t = thing_1.mass * inverse_inertia;
            
            linear_1.x += normal_x * t;
            linear_1.y += normal_y * t;
            
            t = thing_2.mass * inverse_inertia;
            
            linear_2.x -= normal_x * t;
            linear_2.y -= normal_y * t;
            
            thing_1.x += linear_1.x;
            thing_1.y += linear_1.y;
            
            thing_2.x += linear_2.x;
            thing_2.y += linear_2.y;
        }
    }
}
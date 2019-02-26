
package land.things.particles;

import client.graphics.sprite;
import land.map;
import land.things.base.particle;

public class plasma_explosion extends particle
{
    public static sprite[] plasma_explosion;
    
    public int frame;
    public int mod;
    
    public plasma_explosion(map map, float x, float y, float z)
    {
        super(map, x, y, z, 0.0f, 0.0f);
        
        image = plasma_explosion[0];
    }
    
    public void integrate()
    {
        mod++;
        
        if (mod == 8)
        {
            mod = 0;
            
            frame++;
            
            if (frame == plasma_explosion.length)
                gc = true;
            else
                image = plasma_explosion[frame];
        }
    }
}

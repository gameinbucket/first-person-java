
package land.things.items;

import client.graphics.sprite;
import land.map;
import land.things.base.item;

public class medkit extends item
{
    public static sprite medkit_sprite;
    
    public medkit(map map, float x, float y)
    {
        super(map, x, y, 0.6f);
        
        image = medkit_sprite;
    }
}

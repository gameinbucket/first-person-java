
package gameinbucket.app.land.things.items;

import gameinbucket.app.client.graphics.sprite;
import gameinbucket.app.land.map;
import gameinbucket.app.land.things.base.item;

public class medkit extends item {
    public static sprite medkit_sprite;

    public medkit(map map, float x, float y) {
        super(map, x, y, 0.6f);

        image = medkit_sprite;
    }
}


package gameinbucket.app.land.things;

import gameinbucket.app.client.graphics.sprite;
import gameinbucket.app.land.map;

public class tree extends thing {
    public static sprite dead_tree_sprite;

    public tree(map map, float x, float y, float r) {
        super(map, x, y, r, 0.6f, 1.8f, 1.0f);

        sprite = dead_tree_sprite;
    }

    public void integrate() {

    }
}

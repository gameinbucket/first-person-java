
package gameinbucket.app.land.things.base;

import gameinbucket.app.client.graphics.sprite;
import gameinbucket.app.land.map;

public class item extends entity {
    private static int unique_index;

    public int uid;

    public sprite image;

    public item(map map, float x, float y, float radius) {
        uid = unique_index++;

        this.x = x;
        this.y = y;
        this.z = map.find_sector(x, y).floor;

        this.radius = radius;

        int c_min = (int) (x - radius) >> map.shift;
        int r_min = (int) (y - radius) >> map.shift;

        int c_max = (int) (x + radius) >> map.shift;
        int r_max = (int) (y + radius) >> map.shift;

        for (int r = r_min; r <= r_max; r++)
            for (int c = c_min; c <= c_max; c++)
                map.cells[c + r * map.columns].add_item(this);

        map.add_item(this);
    }

    public sprite sprite() {
        return image;
    }
}

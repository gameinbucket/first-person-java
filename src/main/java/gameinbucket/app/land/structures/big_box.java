
package gameinbucket.app.land.structures;

import gameinbucket.app.client.state;
import gameinbucket.app.land.linedef;
import gameinbucket.app.land.sector;
import gameinbucket.app.land.vector;

public abstract class big_box {
    private big_box() {

    }

    public static sector make(state s, int x, int y) {
        vector[] p = { new vector(x, y), new vector(x, y + 12), new vector(x + 12, y + 12), new vector(x + 12, y), };

        int lower = -1;
        int middle = s.t_grass;
        int upper = -1;

        linedef[] lines = new linedef[p.length];

        int j = p.length - 1;

        for (int i = 0; i < p.length; i++) {
            lines[i] = new linedef(p[i], p[j], lower, middle, upper);

            j = i;
        }

        sector sec = new sector(p, lines);
        sec.bottom(0);
        sec.floor(0);
        sec.ceil(5);
        sec.top(0);
        sec.floor_texture(s.t_grass);
        sec.ceil_texture(s.t_grass);

        s.land.add_sector(sec);

        return sec;
    }
}

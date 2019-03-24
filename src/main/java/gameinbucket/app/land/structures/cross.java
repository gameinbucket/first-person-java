
package gameinbucket.app.land.structures;

import gameinbucket.app.client.state;
import gameinbucket.app.land.linedef;
import gameinbucket.app.land.sector;
import gameinbucket.app.land.vector;

public abstract class cross {
    private cross() {

    }

    public static sector make(state s, int x, int y) {
        vector[] p = { new vector(x + 1, y), new vector(x + 1, y + 1), new vector(x, y + 1), new vector(x, y + 2),
                new vector(x + 1, y + 2), new vector(x + 1, y + 3), new vector(x + 2, y + 3), new vector(x + 2, y + 2),
                new vector(x + 3, y + 2), new vector(x + 3, y + 1), new vector(x + 2, y + 1), new vector(x + 2, y), };

        linedef[] lines = new linedef[p.length];

        int j = p.length - 1;

        for (int i = 0; i < p.length; i++) {
            lines[i] = new linedef(p[j], p[i], s.t_planks, -1, s.t_planks);

            j = i;
        }

        sector sec = new sector(p, lines);
        sec.bottom(0);
        sec.floor(1);
        sec.ceil(3);
        sec.top(4);
        sec.floor_texture(s.t_stone);
        sec.ceil_texture(s.t_stone);

        s.land.add_sector(sec);

        return sec;
    }
}

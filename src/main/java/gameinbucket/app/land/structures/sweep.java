
package gameinbucket.app.land.structures;

import gameinbucket.app.client.state;
import gameinbucket.app.land.linedef;
import gameinbucket.app.land.sector;
import gameinbucket.app.land.vector;

public abstract class sweep {
    private sweep() {

    }

    public static sector make(state s, int x, int y) {
        vector[] p = { new vector(x + 0, 0 + y), new vector(x + -5, 5 + y), new vector(x + 0, 10 + y),
                new vector(x + 5, 5 + y), new vector(x + 10, 10 + y), new vector(x + 15, 5 + y),
                new vector(x + 15, 0 + y), };

        linedef[] lines = new linedef[p.length];

        int j = p.length - 1;

        for (int i = 0; i < p.length; i++) {
            lines[i] = new linedef(p[j], p[i], -1, -1, s.t_stone);

            j = i;
        }

        sector sec = new sector(p, lines);
        sec.bottom(0);
        sec.floor(0);
        sec.ceil(4);
        sec.top(5);
        sec.floor_texture(-1);
        sec.ceil_texture(s.t_plank_floor);
        sec.cutout(false, true);

        s.land.add_sector(sec);

        return sec;
    }
}

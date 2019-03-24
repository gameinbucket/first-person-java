
package gameinbucket.app.land.structures;

import gameinbucket.app.client.state;
import gameinbucket.app.land.linedef;
import gameinbucket.app.land.sector;
import gameinbucket.app.land.vector;

public abstract class crunch {
    private crunch() {

    }

    public static sector make(state s, int x, int y) {
        vector[] p = { new vector(x, y), new vector(x, y + 4), new vector(x + 1, y + 3), new vector(x + 2, y + 4),
                new vector(x + 2, y), new vector(x + 1, y + 1), };

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
        sec.top(5);
        sec.floor_texture(s.t_plank_floor);
        sec.ceil_texture(s.t_plank_floor);

        s.land.add_sector(sec);

        return sec;
    }
}

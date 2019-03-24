
package gameinbucket.app.land.structures;

import gameinbucket.app.client.state;
import gameinbucket.app.land.linedef;
import gameinbucket.app.land.sector;
import gameinbucket.app.land.vector;

public abstract class plain_tile {
    private plain_tile() {

    }

    public static sector make(state s) {
        vector[] p = { new vector(0, 0), new vector(0, 127), new vector(127, 127), new vector(127, 0), };

        linedef[] lines = new linedef[0];

        sector sec = new sector(p, lines);
        sec.bottom(0);
        sec.floor(0);
        sec.ceil(10);
        sec.top(0);
        sec.floor_texture(s.t_grass);
        sec.ceil_texture(-1);

        s.land.add_sector(sec);

        return sec;
    }
}

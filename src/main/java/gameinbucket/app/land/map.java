package gameinbucket.app.land;

import gameinbucket.app.client.state;
import gameinbucket.app.land.things.thing;
import java.util.ArrayList;
import java.util.HashSet;
import gameinbucket.app.land.things.base.missile;
import gameinbucket.app.land.things.base.particle;

public class map extends map_base {
    private static final float scale = 1.0f / 4.0f;

    public state state;

    public long time;

    public int columns;
    public int rows;

    public final int shift;

    public map_cell[] cells;

    public int sector_count = 0;
    public sector[] sectors = new sector[2];

    public HashSet<thing> gc = new HashSet<>();

    public map(state s) {
        state = s;
        shift = 5;
    }

    public void add_sector(sector s) {
        if (sector_count == sectors.length) {
            sector[] array = new sector[sectors.length + 5];
            System.arraycopy(sectors, 0, array, 0, sectors.length);
            sectors = array;
        }

        sectors[sector_count++] = s;
    }

    public void bridge(state s, sector add, int add_index, sector base, int base_index) {
        int add_index_plus = add_index + 1;
        int base_index_plus = base_index + 1;

        if (add_index_plus == add.points.length)
            add_index_plus = 0;

        if (base_index_plus == base.points.length)
            base_index_plus = 0;

        linedef[] add_lines_after = new linedef[add.lines.length - 1];

        int i;
        int c = 0;

        for (i = 0; i < add.lines.length; i++) {
            if (i == add_index_plus)
                continue;

            add_lines_after[c] = add.lines[i];
            c++;
        }

        add.lines = add_lines_after;

        linedef[] base_lines_after = new linedef[base.lines.length - 1];

        c = 0;

        for (i = 0; i < base.lines.length; i++) {
            if (i == base_index_plus)
                continue;

            base_lines_after[c] = base.lines[i];
            c++;
        }

        base.lines = base_lines_after;

        vector[] p = { add.points[add_index], base.points[base_index_plus], base.points[base_index],
                add.points[add_index_plus], };

        int lower = -1;
        int middle = s.t_planks;
        int upper = -1;

        linedef[] lines = new linedef[2];

        lines[0] = new linedef(p[1], p[0], lower, middle, upper);
        lines[1] = new linedef(p[3], p[2], lower, middle, upper);

        sector sec = new sector(p, lines);
        sec.bottom(0);
        sec.floor(0);
        sec.ceil(5);
        sec.top(0);
        sec.floor_texture(s.t_plank_floor);
        sec.ceil_texture(s.t_plank_floor);

        add_sector(sec);
    }

    public void remove_sector(sector s) {
        for (int i = 0; i < sector_count; i++) {
            if (sectors[i] == s) {
                sectors[i] = sectors[sector_count - 1];
                sectors[sector_count - 1] = null;

                sector_count--;

                break;
            }
        }
    }

    public sector find_sector(float x, float y) {
        for (int i = 0; i < sectors.length; i++) {
            sector s = sectors[i];

            if (s.outside != null)
                continue;

            if (s.contains(x, y))
                return s.find(x, y);
        }

        return null;
    }

    public void build() {
        float left = 0;
        float top = 0;

        for (int i = 0; i < sector_count; i++) {
            sector s = sectors[i];

            for (int k = 0; k < s.points.length; k++) {
                vector v = s.points[k];

                if (v.x > left)
                    left = v.x;
                if (v.y > top)
                    top = v.y;
            }
        }

        for (int i = 0; i < sector_count; i++) {
            sector s = sectors[i];

            s.inside.clear();

            for (int j = 0; j < sector_count; j++) {
                if (j == i)
                    continue;

                sector o = sectors[j];

                ArrayList<vector> inside = new ArrayList<>();

                for (int k = 0; k < o.points.length; k++) {
                    boolean duplicate = false;

                    for (int c = 0; c < s.points.length; c++) {
                        if (s.points[c] == o.points[k]) {
                            duplicate = true;
                            break;
                        }
                    }

                    if (duplicate)
                        continue;

                    if (s.contains(o.points[k].x, o.points[k].y))
                        inside.add(o.points[k]);
                }

                if (inside.size() == o.points.length)
                    s.inside.add(o);
            }
        }

        for (int i = 0; i < sector_count; i++) {
            sector s = sectors[i];

            HashSet<sector> dead = new HashSet<>();

            for (sector o : s.inside)
                for (sector d : o.inside)
                    dead.add(d);
            for (sector d : dead)
                s.inside.remove(d);
            for (sector o : s.inside)
                o.outside = s;
        }

        final float size = 1 << shift;

        columns = (int) Math.ceil(left / size);
        rows = (int) Math.ceil(top / size);

        cells = new map_cell[columns * rows];

        for (int i = 0; i < cells.length; i++)
            cells[i] = new map_cell(this);

        for (int i = 0; i < sector_count; i++)
            triangulation.make(sectors[i], scale);

        for (int i = 0; i < sector_count; i++)
            build_lines(sectors[i]);
    }

    public void build_lines(sector s) {
        if (s.lines == null)
            return;

        float bottom;
        float floor;
        float ceil;
        float top;

        sector plus;
        sector minus;

        bottom = s.bottom;
        floor = s.floor;
        ceil = s.ceil;
        top = s.top;

        if (s.outside == null) {
            plus = null;
            minus = s;
        } else {
            plus = s;
            minus = s.outside;

            /*
             * bottom = s.outside.floor; floor = s.floor; ceil = s.ceil; top =
             * s.outside.ceil;
             */
        }

        float t_u = 0.0f;

        for (int i = 0; i < s.lines.length; i++) {
            linedef line = s.lines[i];

            raycast_cells(line);

            line.set(plus, minus);

            float t_s = t_u + (float) Math
                    .sqrt((line.a.x - line.b.x) * (line.a.x - line.b.x) + (line.a.y - line.b.y) * (line.a.y - line.b.y))
                    * scale;

            if (line.bot != null)
                line.bot.set(bottom, floor, t_u, bottom * scale, t_s, floor * scale);
            if (line.mid != null)
                line.mid.set(floor, ceil, t_u, floor * scale, t_s, ceil * scale);
            if (line.top != null)
                line.top.set(ceil, top, t_u, ceil * scale, t_s, top * scale);

            t_u = t_s;
        }
    }

    private void raycast_cells(linedef line) {
        double dx = Math.abs(line.b.x - line.a.x);
        double dy = Math.abs(line.b.y - line.a.y);

        int x = (int) (Math.floor(line.a.x));
        int y = (int) (Math.floor(line.a.y));

        int n = 1;
        double error;
        int x_inc, y_inc;

        if (dx == 0) {
            x_inc = 0;
            error = Double.MAX_VALUE;
        } else if (line.b.x > line.a.x) {
            x_inc = 1;
            n += (int) (Math.floor(line.b.x)) - x;
            error = (Math.floor(line.a.x) + 1 - line.a.x) * dy;
        } else {
            x_inc = -1;
            n += x - (int) (Math.floor(line.b.x));
            error = (line.a.x - Math.floor(line.a.x)) * dy;
        }

        if (dy == 0) {
            y_inc = 0;
            error = Double.MIN_VALUE;
        } else if (line.b.y > line.a.y) {
            y_inc = 1;
            n += (int) (Math.floor(line.b.y)) - y;
            error -= (Math.floor(line.a.y) + 1 - line.a.y) * dx;
        } else {
            y_inc = -1;
            n += y - (int) (Math.floor(line.b.y));
            error -= (line.a.y - Math.floor(line.a.y)) * dx;
        }

        while (n > 0) {
            cells[(x >> shift) + (y >> shift) * columns].add_line(line);

            if (error > 0) {
                y += y_inc;
                error -= dx;
            } else {
                x += x_inc;
                error += dy;
            }

            n--;
        }
    }

    public void integrate() {
        for (int i = 0; i < thing_count; i++) {
            thing t = things[i];
            t.integrate();
        }

        for (int i = 0; i < missile_count; i++) {
            missile t = missiles[i];
            t.integrate();

            if (t.gc) {
                missiles[i] = missiles[missile_count - 1];
                missiles[missile_count - 1] = null;
                missile_count--;
                i--;
            }
        }

        for (int i = 0; i < particle_count; i++) {
            particle t = particles[i];
            t.integrate();

            if (t.gc) {
                particles[i] = particles[particle_count - 1];
                particles[particle_count - 1] = null;
                particle_count--;
                i--;
            }
        }

        time++;
    }
}

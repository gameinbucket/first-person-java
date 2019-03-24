
package gameinbucket.app.land.things;

import gameinbucket.app.client.graphics.sprite;
import java.util.HashSet;
import gameinbucket.app.land.map_cell;
import gameinbucket.app.land.linedef;
import gameinbucket.app.land.map;
import gameinbucket.app.land.sector;
import gameinbucket.app.land.things.base.entity;

public abstract class thing extends entity {
    private static int unique_index;

    protected static int flag_demon = 1 << 2;
    protected static int flag_human = 1 << 3;

    public int uid;
    public map map;
    public sector sector;

    public sprite sprite;

    public int health = 1;
    public float height;
    public float mass;
    public float speed;

    public int flags;

    public float r;

    public float ox;
    public float oy;

    public float dx;
    public float dy;
    public float dz;

    public boolean ground = true;

    public int c_min;
    public int r_min;

    public int c_max;
    public int r_max;

    protected HashSet<thing> collided = new HashSet<>();
    protected HashSet<thing> collisions = new HashSet<>();

    public thing(map map, float x, float y, float r, float radius, float height, float mass) {
        this.uid = unique_index++;
        this.map = map;
        this.sector = map.find_sector(x, y);

        this.x = x;
        this.y = y;
        this.z = this.sector.floor;
        this.r = r;

        this.radius = radius;
        this.height = height;
        this.mass = mass;

        add_to_cell();

        map.add_thing(this);
    }

    public void remove_from_cell() {
        for (int r = r_min; r <= r_max; r++)
            for (int c = c_min; c <= c_max; c++)
                map.cells[c + r * map.columns].remove_thing(this);
    }

    public void add_to_cell() {
        int c_min = (int) (x - radius) >> map.shift;
        int r_min = (int) (y - radius) >> map.shift;

        int c_max = (int) (x + radius) >> map.shift;
        int r_max = (int) (y + radius) >> map.shift;

        for (int r = r_min; r <= r_max; r++)
            for (int c = c_min; c <= c_max; c++)
                map.cells[c + r * map.columns].add_thing(this);

        this.c_min = c_min;
        this.r_min = r_min;
        this.c_max = c_max;
        this.r_max = r_max;
    }

    protected void resolve(thing b) {
        float block = radius + b.radius;

        if (Math.abs(x - b.x) > block || Math.abs(y - b.y) > block)
            return;

        if (Math.abs(ox - b.x) > Math.abs(oy - b.y)) {
            if (ox - b.x < 0)
                x = b.x - block;
            else
                x = b.x + block;

            dx = 0.0f;
        } else {
            if (oy - b.y < 0)
                y = b.y - block;
            else
                y = b.y + block;

            dy = 0.0f;
        }
    }

    protected boolean overlap(entity b) {
        float block = radius + b.radius;

        if (Math.abs(x - b.x) > block || Math.abs(y - b.y) > block)
            return false;

        return true;
    }

    protected void overlap(linedef line) {
        float vx = line.b.x - line.a.x;
        float vy = line.b.y - line.a.y;

        float wx = x - line.a.x;
        float wy = y - line.a.y;

        float t = (wx * vx + wy * vy) / (vx * vx + vy * vy);

        boolean endpoint = false;

        if (t < 0) {
            t = 0;
            endpoint = true;
        } else if (t > 1) {
            t = 1;
            endpoint = true;
        }

        float px = line.a.x + vx * t;
        float py = line.a.y + vy * t;

        px -= x;
        py -= y;

        if ((px * px + py * py) > radius * radius)
            return;

        boolean collision = false;

        if (line.mid != null) {
            collision = true;
        } else {
            if (z + height > line.plus.ceil || z + 1.0f < line.plus.floor) {
                collision = true;
            }
        }

        if (collision) {
            if (sector == line.plus)
                return;

            float overlap;

            float normal_x;
            float normal_y;

            if (endpoint) {
                float ex = -px;
                float ey = -py;

                float em = (float) Math.sqrt(ex * ex + ey * ey);

                ex /= em;
                ey /= em;

                overlap = (float) Math
                        .sqrt((px + radius * ex) * (px + radius * ex) + (py + radius * ey) * (py + radius * ey));

                normal_x = ex;
                normal_y = ey;
            } else {
                overlap = (float) Math.sqrt((px + radius * line.nx) * (px + radius * line.nx)
                        + (py + radius * line.ny) * (py + radius * line.ny));

                normal_x = line.nx;
                normal_y = line.ny;
            }

            x += normal_x * overlap;
            y += normal_y * overlap;
        }
    }

    public void integrate() {
        if (ground) {
            dx *= 0.88f;
            dy *= 0.88f;
        }

        if (dx != 0.0f || dy != 0.0f) {
            ox = x;
            oy = y;

            x += dx;
            y += dy;

            remove_from_cell();

            int c_min = (int) (x - radius) >> map.shift;
            int r_min = (int) (y - radius) >> map.shift;

            int c_max = (int) (x + radius) >> map.shift;
            int r_max = (int) (y + radius) >> map.shift;

            collisions.clear();

            for (int r = r_min; r <= r_max; r++) {
                for (int c = c_min; c <= c_max; c++) {
                    map_cell cell = map.cells[c + r * map.columns];

                    for (int thing = 0; thing < cell.thing_count; thing++) {
                        thing t = cell.things[thing];

                        if (collisions.contains(t))
                            continue;

                        if (overlap(t))
                            collided.add(t);

                        collisions.add(t);
                    }
                }
            }

            while (collided.isEmpty() == false) {
                thing closest = null;
                float manhattan = Float.MAX_VALUE;

                for (thing o : collided) {
                    float m = Math.abs(ox - o.x) + Math.abs(oy - o.y);

                    if (m < manhattan) {
                        manhattan = m;
                        closest = o;
                    }
                }

                resolve(closest);

                collided.remove(closest);
            }

            for (int r = r_min; r <= r_max; r++) {
                for (int c = c_min; c <= c_max; c++) {
                    map_cell cell = map.cells[c + r * map.columns];

                    // for (int line = 0; line < cell.lines.length; line++)
                    // overlap(cell.lines[line]);
                }
            }

            add_to_cell();
        }

        if (ground == false || dz != 0.0f) {
            dz -= 0.028f;

            z += dz;

            if (z < sector.floor) {
                ground = true;

                dz = 0.0f;

                z = sector.floor;
            } else {
                ground = false;
            }
        }
    }

    public void damage(int d) {
        health -= d;
    }

    public sprite sprite(thing t) {
        return sprite;
    }
}

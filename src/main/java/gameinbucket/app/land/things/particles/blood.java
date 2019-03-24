
package gameinbucket.app.land.things.particles;

import gameinbucket.app.client.graphics.sprite;
import gameinbucket.app.land.linedef;
import gameinbucket.app.land.map;
import gameinbucket.app.land.map_cell;
import gameinbucket.app.land.sector;
import gameinbucket.app.land.things.base.decal;
import gameinbucket.app.land.things.base.particle;

public class blood extends particle {
    public static sprite blood_small_sprite;
    public static sprite blood_medium_sprite;
    public static sprite blood_large_sprite;

    public static String blood_sound;

    public sector sector;

    public int time_trail = 4;

    public blood(map map, sprite s, float x, float y, float z) {
        super(map, x, y, z, 0.2f, 0.2f);

        image = s;
        sector = map.find_sector(x, y);
    }

    private void hit_floor() {
        gc = true;

        decal d = new decal(map);

        d.texture = image.texture;

        float h = image.height / 2;

        float x = (int) Math.round(this.x * 16.0f) / 16.0f;
        float y = (int) Math.round(this.y * 16.0f) / 16.0f;

        d.x1 = x - image.width;
        d.y1 = y - h;
        d.z1 = sector.floor;

        d.u1 = 0.0f;
        d.v1 = 1.0f;

        d.x2 = x - image.width;
        d.y2 = y + h;
        d.z2 = sector.floor;

        d.u2 = 0.0f;
        d.v2 = 0.0f;

        d.x3 = x + image.width;
        d.y3 = y + h;
        d.z3 = sector.floor;

        d.u3 = 1.0f;
        d.v3 = 0.0f;

        d.x4 = x + image.width;
        d.y4 = y - h;
        d.z4 = sector.floor;

        d.u4 = 1.0f;
        d.v4 = 1.0f;

        d.nx = 0.0f;
        d.ny = 0.0f;
        d.nz = 1.0f;
    }

    private void hit_ceil() {
        gc = true;

        decal d = new decal(map);

        d.texture = image.texture;

        float h = image.height / 2;

        float x = (int) Math.round(this.x * 16.0f) / 16.0f;
        float y = (int) Math.round(this.y * 16.0f) / 16.0f;

        d.x1 = x + image.width;
        d.y1 = y - h;
        d.z1 = sector.ceil;

        d.u1 = 0.0f;
        d.v1 = 1.0f;

        d.x2 = x + image.width;
        d.y2 = y + h;
        d.z2 = sector.ceil;

        d.u2 = 0.0f;
        d.v2 = 0.0f;

        d.x3 = x - image.width;
        d.y3 = y + h;
        d.z3 = sector.ceil;

        d.u3 = 1.0f;
        d.v3 = 0.0f;

        d.x4 = x - image.width;
        d.y4 = y - h;
        d.z4 = sector.ceil;

        d.u4 = 1.0f;
        d.v4 = 1.0f;

        d.nx = 0.0f;
        d.ny = 0.0f;
        d.nz = -1.0f;
    }

    protected boolean overlap(linedef def) {
        float vx = def.b.x - def.a.x;
        float vy = def.b.y - def.a.y;

        float wx = x - def.a.x;
        float wy = y - def.a.y;

        float t = (wx * vx + wy * vy) / (vx * vx + vy * vy);

        if (t < 0)
            t = 0;
        else if (t > 1)
            t = 1;

        float px = def.a.x + vx * t;
        float py = def.a.y + vy * t;

        px -= x;
        py -= y;

        if (px * px + py * py > radius * radius)
            return false;

        if (def.mid != null || z < def.plus.floor || z + height > def.plus.ceil) {
            float x = px + this.x;
            float y = py + this.y;

            gc = true;

            decal d = new decal(map);

            d.texture = image.texture;

            d.x1 = x - def.ny * image.width;
            d.y1 = y + def.nx * image.width;
            d.z1 = z + image.height;

            wx = d.x1 - def.a.x;
            wy = d.y1 - def.a.y;

            t = (wx * vx + wy * vy) / (vx * vx + vy * vy);

            if (t < 0) {
                d.x1 = def.a.x;
                d.y1 = def.a.y;
            }

            d.u1 = 0.0f;
            d.v1 = 1.0f;

            d.x2 = d.x1;
            d.y2 = d.y1;
            d.z2 = z;

            d.u2 = 0.0f;
            d.v2 = 0.0f;

            d.x3 = x + def.ny * image.width;
            d.y3 = y - def.nx * image.width;
            d.z3 = z;

            wx = d.x3 - def.a.x;
            wy = d.y3 - def.a.y;

            t = (wx * vx + wy * vy) / (vx * vx + vy * vy);

            if (t > 1) {
                d.x3 = def.b.x;
                d.y3 = def.b.y;
            }

            d.u3 = 1.0f;
            d.v3 = 0.0f;

            d.x4 = d.x3;
            d.y4 = d.y3;
            d.z4 = d.z1;

            d.u4 = 1.0f;
            d.v4 = 1.0f;

            d.nx = def.nx;
            d.ny = def.ny;
            d.nz = 0.0f;

            return true;
        }

        return false;
    }

    private void overlap() {
        if (z < sector.floor) {
            hit_floor();
            return;
        }

        if (z + height > sector.ceil) {
            hit_ceil();
            return;
        }

        int c_min = (int) (x - radius) >> map.shift;
        int r_min = (int) (y - radius) >> map.shift;

        int c_max = (int) (x + radius) >> map.shift;
        int r_max = (int) (y + radius) >> map.shift;

        for (int r = r_min; r <= r_max; r++) {
            for (int c = c_min; c <= c_max; c++) {
                map_cell cell = map.cells[c + r * map.columns];

                for (int line = 0; line < cell.lines.length; line++) {
                    if (overlap(cell.lines[line]))
                        return;
                }
            }
        }
    }

    public void integrate() {
        dz -= 0.01f;

        dx *= 0.95f;
        dy *= 0.95f;

        x += dx;
        y += dy;
        z += dz;

        overlap();

        if (gc == false) {
            if (--time_trail == 0) {
                time_trail = 4;
                new plasma_explosion(map, x, y, z);
            }
        }
    }
}


package gameinbucket.app.land.things;

import gameinbucket.app.land.map_cell;
import gameinbucket.app.land.linedef;
import gameinbucket.app.land.m_random;
import gameinbucket.app.land.map;

public abstract class npc extends thing {
    public static final int DIRECTION_NORTH = 0;
    public static final int DIRECTION_NORTH_EAST = 1;
    public static final int DIRECTION_EAST = 2;
    public static final int DIRECTION_SOUTH_EAST = 3;
    public static final int DIRECTION_SOUTH = 4;
    public static final int DIRECTION_SOUTH_WEST = 5;
    public static final int DIRECTION_WEST = 6;
    public static final int DIRECTION_NORTH_WEST = 7;
    public static final int DIRECTION_COUNT = 8;
    public static final int DIRECTION_NONE = 8;

    public static final int[] DIRECTION_OPPOSITE = { DIRECTION_SOUTH, DIRECTION_SOUTH_WEST, DIRECTION_WEST,
            DIRECTION_NORTH_WEST, DIRECTION_NORTH, DIRECTION_NORTH_EAST, DIRECTION_EAST, DIRECTION_SOUTH_EAST,
            DIRECTION_NONE };

    public static final int[] DIRECTION_DIAGONALS = { DIRECTION_SOUTH_EAST, DIRECTION_SOUTH_WEST, DIRECTION_NORTH_EAST,
            DIRECTION_NORTH_WEST };

    public static final float[] MOVE_X = { 0.0f, -0.5f, -1.0f, -0.5f, 0.0f, 0.5f, 1.0f, 0.5f };

    public static final float[] MOVE_Y = { 1.0f, 0.5f, 0.0f, -0.5f, -1.0f, -0.5f, 0.0f, 0.5f };

    public int move_count;
    public int direction;

    public thing target;

    public npc(map map, float x, float y, float r, float radius, float height, float mass) {
        super(map, x, y, r, radius, height, mass);
    }

    protected boolean overlap(thing b, float x, float y) {
        float block = radius + b.radius;

        if (Math.abs(x - b.x) > block || Math.abs(y - b.y) > block)
            return false;

        return true;
    }

    protected boolean overlap(linedef line, float x, float y) {
        if (sector == line.plus)
            return false;

        float vx = line.b.x - line.a.x;
        float vy = line.b.y - line.a.y;

        float wx = x - line.a.x;
        float wy = y - line.a.y;

        float t = (wx * vx + wy * vy) / (vx * vx + vy * vy);

        if (t < 0)
            t = 0;
        else if (t > 1)
            t = 1;

        float px = line.a.x + vx * t;
        float py = line.a.y + vy * t;

        px -= x;
        py -= y;

        if (px * px + py * py > radius * radius)
            return false;

        if (line.mid != null || z + height > line.plus.ceil || z + 1.0f < line.plus.floor)
            return true;

        return false;
    }

    protected boolean try_move(float x, float y) {
        int c_min = (int) (x - radius) >> map.shift;
        int r_min = (int) (y - radius) >> map.shift;

        int c_max = (int) (x + radius) >> map.shift;
        int r_max = (int) (y + radius) >> map.shift;

        for (int r = r_min; r <= r_max; r++) {
            for (int c = c_min; c <= c_max; c++) {
                map_cell cell = map.cells[c + r * map.columns];

                for (int thing = 0; thing < cell.thing_count; thing++) {
                    thing t = cell.things[thing];

                    if (t == this)
                        continue;

                    if (overlap(t, x, y))
                        return false;
                }

                for (int line = 0; line < cell.lines.length; line++)
                    if (overlap(cell.lines[line], x, y))
                        return false;
            }
        }

        return true;
    }

    protected boolean move() {
        if (direction == DIRECTION_NONE)
            return true;

        float try_x = x + MOVE_X[direction] * speed;
        float try_y = y + MOVE_Y[direction] * speed;

        if (try_move(try_x, try_y)) {
            remove_from_cell();

            x = try_x;
            y = try_y;

            add_to_cell();

            return true;
        }

        return false;
    }

    protected boolean new_move() {
        if (move() == false)
            return false;

        move_count = 16 + m_random.p_random() & 32;

        return true;
    }

    protected void new_direction() {
        float dx = target.x - x;
        float dy = target.y - y;

        int direction_x;
        int direction_y;

        int old = direction;
        int opposite = DIRECTION_OPPOSITE[direction];

        final float epsilon = 0.32f;

        if (dx > epsilon)
            direction_x = DIRECTION_WEST;
        else if (dx < -epsilon)
            direction_x = DIRECTION_EAST;
        else
            direction_x = DIRECTION_NONE;

        if (dy > epsilon)
            direction_y = DIRECTION_NORTH;
        else if (dy < -epsilon)
            direction_y = DIRECTION_SOUTH;
        else
            direction_y = DIRECTION_NONE;

        if (direction_x != DIRECTION_NONE && direction_y != DIRECTION_NONE) {
            direction = DIRECTION_DIAGONALS[((dy > 0 ? 1 : 0) << 1) + (dx > 0 ? 1 : 0)];

            if (direction != opposite && new_move())
                return;
        }

        if (m_random.p_random() > 200 || Math.abs(dy) > Math.abs(dx)) {
            int temp = direction_x;

            direction_x = direction_y;
            direction_y = temp;
        }

        if (direction_x != opposite) {
            direction = direction_x;

            if (new_move())
                return;
        } else
            direction_x = DIRECTION_NONE;

        if (direction_y != opposite) {
            direction = direction_y;

            if (new_move())
                return;
        } else
            direction_y = DIRECTION_NONE;

        if (old != DIRECTION_NONE) {
            direction = old;

            if (new_move())
                return;
        }

        if ((m_random.p_random() & 1) > 0) {
            for (int i = 0; i < DIRECTION_COUNT; i++) {
                if (i == opposite)
                    continue;

                direction = i;

                if (new_move())
                    return;
            }
        } else {
            for (int i = DIRECTION_COUNT - 1; i > -1; i--) {
                if (i == opposite)
                    continue;

                direction = i;

                if (new_move())
                    return;
            }
        }

        if (opposite != DIRECTION_NONE) {
            direction = opposite;

            if (new_move())
                return;
        }

        direction = DIRECTION_NONE;
    }
}

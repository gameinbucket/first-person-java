
package gameinbucket.app.land.things;

import gameinbucket.app.client.graphics.sprite;
import gameinbucket.app.client.graphics.texture;
import gameinbucket.app.client.keyboard;
import gameinbucket.app.client.mouse;
import gameinbucket.app.land.m_random;
import gameinbucket.app.land.map;
import gameinbucket.app.land.map_cell;
import gameinbucket.app.land.things.base.entity;
import gameinbucket.app.land.things.base.item;
import gameinbucket.app.land.things.particles.blood;
import static org.lwjgl.glfw.GLFW.*;

public class you extends thing {
    public static texture[] hand_animation;

    mouse mouse;
    keyboard keyboard;

    public float look = 0.0f;
    public float roll = 0.0f;

    public item inventory;

    public texture[] animation;
    public int frame;
    public int mod;

    public float eye;
    public float hand_x;
    public float hand_y;

    public thing tracking;

    private boolean sticky_action = false;

    public you(map map, mouse mouse, keyboard keyboard, float x, float y, float r) {
        super(map, x, y, r, 0.5f, 1.76f, 1.0f);

        this.mouse = mouse;
        this.keyboard = keyboard;

        flags = flag_human;
        health = 3;
        animation = hand_animation;
    }

    public void bob_and_height() {
        final float bob_multiply = 3.0f;
        final float bob_limit = 0.7f;
        final float bob_to_eye_mod = 0.6f;
        final float time_to_angle_mod = 10.0f;
        final float bob_to_hand_mod = 32;

        float bob = (dx * dx + dy * dy) * bob_multiply;

        if (bob > bob_limit)
            bob = bob_limit;

        float angle = map.time / time_to_angle_mod;
        float sin = (float) Math.sin(angle);

        eye = z + height + bob * sin * bob_to_eye_mod;

        if (frame == 0) {
            float cos = (float) Math.cos(angle);

            hand_x = bob * cos * bob_to_hand_mod;
            hand_y = bob * Math.abs(sin) * bob_to_hand_mod;
        }
    }

    public void damage(int d) {
        health -= d;

        float z = this.z + height / 2;

        sprite s;

        if (d == 1)
            s = blood.blood_small_sprite;
        else if (d == 2)
            s = blood.blood_medium_sprite;
        else
            s = blood.blood_large_sprite;

        blood b = new blood(map, s, x, y, z);

        b.dz = 0.2f;
    }

    protected boolean overlap(entity b, float x, float y, float radius) {
        float block = radius + b.radius;

        if (Math.abs(x - b.x) > block || Math.abs(y - b.y) > block)
            return false;

        return true;
    }

    public void punch() {
        final float melee_range = radius + 1.0f;
        final float melee_radius = 0.5f;

        float x = this.x + (float) Math.sin(r) * melee_range;
        float y = this.y - (float) Math.cos(r) * melee_range;

        int c_min = (int) (x - melee_radius) >> map.shift;
        int r_min = (int) (y - melee_radius) >> map.shift;

        int c_max = (int) (x + melee_radius) >> map.shift;
        int r_max = (int) (y + melee_radius) >> map.shift;

        collisions.clear();

        for (int r = r_min; r <= r_max; r++) {
            for (int c = c_min; c <= c_max; c++) {
                map_cell cell = map.cells[c + r * map.columns];

                for (int thing = 0; thing < cell.thing_count; thing++) {
                    thing t = cell.things[thing];

                    if (t == this || collisions.contains(t))
                        continue;

                    if (overlap(t, x, y, melee_radius)) {
                        t.damage(1 + m_random.p_random() % 3);
                        return;
                    }

                    collisions.add(t);
                }
            }
        }
    }

    private void find_thing_in_front() {
        tracking = null;

        for (int i = 0; i < map.thing_count; i++) {
            thing t = map.things[i];

            if (t == this)
                continue;

            if ((t.flags & flag_demon) != 0 && t.health > 0) {
                tracking = t;
                return;
            }
        }
    }

    private void look_at_tracked() {
        final float ninety = (float) (90.0 * Math.PI / 180.0);

        float rotation = (float) Math.atan2(y - tracking.y, x - tracking.x) - ninety;
        float l = (float) Math.atan(tracking.z - z);

        r = rotation;
        look = l;
    }

    public void integrate() {
        final float walk = 0.036f;
        final float run = 0.12f;
        final float turn = 0.05f;
        final float turn_fast = 0.07f;
        final float jump = 0.36f;
        final float vault = 0.5f;
        final float dodge = 0.4f;
        final float sensitivity = 0.005f;

        if (ground) {
            final float MAXSPEED = 0.5f;

            /*
             * if (keyboard.down(GLFW_KEY_SPACE)) { if (keyboard.down(GLFW_KEY_W) &&
             * sticky_action == false) { if (keyboard.down(GLFW_KEY_LEFT_SHIFT)) { // jump }
             * else { dx = +(float)Math.sin(r) * vault; dy = -(float)Math.cos(r) * vault; dz
             * = jump;
             * 
             * sticky_action = true; ground = false; } } else if (keyboard.down(GLFW_KEY_S)
             * && sticky_action == false) { dx = -(float)Math.sin(r) * vault; dy =
             * +(float)Math.cos(r) * vault; dz = jump;
             * 
             * sticky_action = true; ground = false; } else if (keyboard.down(GLFW_KEY_A) &&
             * sticky_action == false) { dx = -(float)Math.cos(r) * vault; dy =
             * -(float)Math.sin(r) * vault; dz = jump;
             * 
             * sticky_action = true; ground = false; } else if (keyboard.down(GLFW_KEY_D) &&
             * sticky_action == false) { dx = (float)Math.cos(r) * vault; dy =
             * (float)Math.sin(r) * vault; dz = jump;
             * 
             * sticky_action = true; ground = false; } } else { sticky_action = false; }
             */

            if (ground) {
                float pace = keyboard.down(GLFW_KEY_LEFT_SHIFT) ? run : walk;

                if (keyboard.down(GLFW_KEY_W)) {
                    dx += (float) Math.sin(r) * pace;
                    dy -= (float) Math.cos(r) * pace;
                }

                if (keyboard.down(GLFW_KEY_S)) {
                    dx -= (float) Math.sin(r) * pace * 0.5f;
                    dy += (float) Math.cos(r) * pace * 0.5f;
                }

                if (keyboard.down(GLFW_KEY_A)) {
                    dx -= (float) Math.cos(r) * pace * 0.75f;
                    dy -= (float) Math.sin(r) * pace * 0.75f;
                }

                if (keyboard.down(GLFW_KEY_D)) {
                    dx += (float) Math.cos(r) * pace * 0.75f;
                    dy += (float) Math.sin(r) * pace * 0.75f;
                }

                if (dx > MAXSPEED)
                    dx = MAXSPEED;
                else if (dx < -MAXSPEED)
                    dx = -MAXSPEED;

                if (dy > MAXSPEED)
                    dy = MAXSPEED;
                else if (dy < -MAXSPEED)
                    dy = -MAXSPEED;
            }
        }

        r += mouse.dx * sensitivity;
        look += mouse.dy * sensitivity;

        if (keyboard.down(GLFW_KEY_J) || keyboard.down(GLFW_KEY_LEFT)) {
            if (keyboard.down(GLFW_KEY_LEFT_SHIFT))
                r -= turn_fast;
            else
                r -= turn;
        }

        if (keyboard.down(GLFW_KEY_L) || keyboard.down(GLFW_KEY_RIGHT)) {
            if (keyboard.down(GLFW_KEY_LEFT_SHIFT))
                r += turn_fast;
            else
                r += turn;
        }

        final float view = (float) (Math.PI * 0.4);

        if (keyboard.down(GLFW_KEY_I) || keyboard.down(GLFW_KEY_UP))
            look -= turn;

        if (keyboard.down(GLFW_KEY_K) || keyboard.down(GLFW_KEY_DOWN))
            look += turn;

        if (look < -view)
            look = -view;
        else if (look > view)
            look = view;

        if (keyboard.down(GLFW_KEY_O)) {
            if (tracking != null)
                tracking = null;
            else
                find_thing_in_front();

            keyboard.keys.put(GLFW_KEY_O, false);
        }

        if (tracking != null) {
            if (tracking.health < 1)
                find_thing_in_front();
            else
                look_at_tracked();
        }

        if (frame == 0 && (keyboard.down(GLFW_KEY_H) || mouse.down(GLFW_MOUSE_BUTTON_LEFT))) {
            frame = 1;
        }

        if (frame > 0) {
            mod++;

            if (mod == 8) {
                mod = 0;
                frame++;

                if (frame == animation.length)
                    frame = 0;
                else if (frame == 3)
                    punch();
            }
        }

        super.integrate();

        for (int r = r_min; r <= r_max; r++) {
            for (int c = c_min; c <= c_max; c++) {
                map_cell cell = map.cells[c + r * map.columns];

                for (int items = 0; items < cell.item_count; items++) {
                    item t = cell.items[items];

                    if (overlap(t)) {
                        inventory = t;

                        cell.remove_item(items);
                        items--;

                        map.remove_item(t);
                    }
                }
            }
        }

        bob_and_height();
    }
}

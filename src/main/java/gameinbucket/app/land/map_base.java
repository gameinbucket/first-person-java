
package gameinbucket.app.land;

import gameinbucket.app.land.things.base.decal;
import gameinbucket.app.land.things.base.item;
import gameinbucket.app.land.things.base.missile;
import gameinbucket.app.land.things.base.particle;
import gameinbucket.app.land.things.thing;

public class map_base {
    public int thing_count = 0;
    public int item_count = 0;
    public int missile_count = 0;
    public int particle_count = 0;
    public int decal_count = 0;

    public thing[] things = new thing[10];
    public item[] items = new item[10];
    public missile[] missiles = new missile[10];
    public particle[] particles = new particle[10];
    public decal[] decals = new decal[10];

    public map_base() {

    }

    public void add_thing(thing t) {
        if (thing_count == things.length) {
            thing[] array = new thing[things.length + 5];
            System.arraycopy(things, 0, array, 0, things.length);
            things = array;
        }

        things[thing_count++] = t;
    }

    public void remove_thing(thing t) {
        for (int i = 0; i < thing_count; i++) {
            if (things[i] == t) {
                things[i] = things[thing_count - 1];
                things[thing_count - 1] = null;
                thing_count--;
                break;
            }
        }
    }

    public void add_item(item t) {
        if (item_count == items.length) {
            item[] array = new item[items.length + 5];
            System.arraycopy(items, 0, array, 0, items.length);
            items = array;
        }

        items[item_count++] = t;
    }

    public void remove_item(int i) {
        items[i] = items[item_count - 1];
        items[item_count - 1] = null;
        item_count--;
    }

    public void remove_item(item t) {
        for (int i = 0; i < item_count; i++) {
            if (items[i] == t) {
                items[i] = items[item_count - 1];
                items[item_count - 1] = null;
                item_count--;
                break;
            }
        }
    }

    public void add_missile(missile t) {
        if (missile_count == missiles.length) {
            missile[] array = new missile[missiles.length + 5];
            System.arraycopy(missiles, 0, array, 0, missiles.length);
            missiles = array;
        }

        missiles[missile_count++] = t;
    }

    public void remove_missile(missile t) {
        for (int i = 0; i < missile_count; i++) {
            if (missiles[i] == t) {
                missiles[i] = missiles[missile_count - 1];
                missiles[missile_count - 1] = null;
                missile_count--;
                break;
            }
        }
    }

    public void add_particle(particle t) {
        if (particle_count == particles.length) {
            particle[] array = new particle[particles.length + 5];
            System.arraycopy(particles, 0, array, 0, particles.length);
            particles = array;
        }

        particles[particle_count++] = t;
    }

    public void remove_particle(particle t) {
        for (int i = 0; i < particle_count; i++) {
            if (particles[i] == t) {
                particles[i] = particles[particle_count - 1];
                particles[particle_count - 1] = null;
                particle_count--;
                break;
            }
        }
    }

    public void add_decal(decal t) {
        if (decal_count == decals.length) {
            decal[] array = new decal[decals.length + 5];
            System.arraycopy(decals, 0, array, 0, decals.length);
            decals = array;
        }

        decals[decal_count++] = t;
    }

    public void remove_decal(decal t) {
        for (int i = 0; i < decal_count; i++) {
            if (decals[i] == t) {
                decals[i] = decals[decal_count - 1];
                decals[decal_count - 1] = null;
                decal_count--;
                break;
            }
        }
    }
}

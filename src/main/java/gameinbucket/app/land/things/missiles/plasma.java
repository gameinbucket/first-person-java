
package gameinbucket.app.land.things.missiles;

import gameinbucket.app.client.graphics.sprite;
import gameinbucket.app.client.audio.sound;
import gameinbucket.app.land.map;
import gameinbucket.app.land.things.base.missile;
import gameinbucket.app.land.things.light;
import gameinbucket.app.land.things.particles.plasma_explosion;
import gameinbucket.app.land.things.thing;

public class plasma extends missile {
    public static sprite plasma_sprite;
    public static String plasma_impact;

    public light glow;

    public plasma(map map, int damage, float x, float y, float z, float dx, float dy, float dz) {
        super(map, x, y, z, 0.2f, 0.2f, dx, dy, dz, damage);

        image = plasma_sprite;
        impact_sound = plasma_impact;

        glow = new light(x, y, z, 0.0f, 1.0f, 0.0f, 6.0f);

        map.state.add_light(glow);
    }

    public void hit(thing t) {
        x -= dx;
        y -= dy;
        z -= dz;

        if (t != null)
            t.damage(damage);

        new sound(impact_sound).start();
        new plasma_explosion(map, x, y, z);

        map.state.lights.remove(glow);

        gc = true;
    }

    public void integrate() {
        super.integrate();

        glow.x = x;
        glow.y = z;
        glow.z = y;
    }
}

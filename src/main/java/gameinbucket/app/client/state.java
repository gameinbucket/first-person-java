package gameinbucket.app.client;

import gameinbucket.app.client.audio.midi;
import gameinbucket.app.client.graphics.buffer;
import gameinbucket.app.client.graphics.draw;
import gameinbucket.app.client.graphics.frame;
import gameinbucket.app.client.graphics.matrix;
import gameinbucket.app.client.graphics.occluder;
import gameinbucket.app.client.graphics.opengl;
import gameinbucket.app.client.graphics.sprite;
import gameinbucket.app.client.graphics.texture;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import javax.sound.midi.Sequencer;
import gameinbucket.app.land.structures.barrel;
import gameinbucket.app.land.structures.complex;
import gameinbucket.app.land.structures.crunch;
import gameinbucket.app.land.structures.extra_crunch;
import gameinbucket.app.land.structures.grassland;
import gameinbucket.app.land.map;
import gameinbucket.app.land.linedef;
import gameinbucket.app.land.triangle;
import gameinbucket.app.land.sector;
import gameinbucket.app.land.structures.big_box;
import gameinbucket.app.land.structures.box;
import gameinbucket.app.land.structures.house;
import gameinbucket.app.land.structures.plain_tile;
import gameinbucket.app.land.structures.sweep;
import gameinbucket.app.land.things.baron;
import gameinbucket.app.land.things.base.decal;
import gameinbucket.app.land.things.base.item;
import gameinbucket.app.land.things.base.missile;
import gameinbucket.app.land.things.base.particle;
import gameinbucket.app.land.things.items.medkit;
import gameinbucket.app.land.things.light;
import gameinbucket.app.land.things.missiles.plasma;
import gameinbucket.app.land.things.particles.blood;
import gameinbucket.app.land.things.particles.plasma_explosion;
import gameinbucket.app.land.things.tree;
import gameinbucket.app.land.things.thing;
import gameinbucket.app.land.things.you;
import static org.lwjgl.opengl.ARBImaging.GL_FUNC_ADD;
import static org.lwjgl.opengl.ARBImaging.glBlendEquation;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glDrawBuffers;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.glFramebufferTexture;

public class state {
    private client client;

    private mouse mouse;
    private keyboard keyboard;

    private int p_texture2;
    private int p_screen;
    private int p_geometry;
    private int p_blur_h;
    private int p_blur_v;
    private int p_blur_h_f;
    private int p_blur_v_f;
    private int p_threshold;
    private int p_ambient_occlusion;
    private int p_ambient_light;
    private int p_point_light;
    private int p_bloom;
    private int p_generic;
    private int p_motion_blur;
    private int p_stencil;
    private int p_sky;

    public int t_grass;
    public int t_stone;
    public int t_stone_floor;
    public int t_planks;
    public int t_plank_floor;

    public int[] sky = new int[6];
    private buffer sky_buffer;

    private float[] view_matrix = new float[16];
    private float[] inverse_model_view_matrix = new float[16];
    private float[] inverse_projection_matrix = new float[16];
    private float[] previous_model_view_projection_matrix = new float[16];
    private float[] previous_model_view_projection_matrix_cache = new float[16];
    private float[] current_to_previous_matrix = new float[16];
    private float[] perspective = new float[16];
    private float[] orthographic = new float[16];
    private float[] orthographic_2 = new float[16];
    private float[] orthographic_4 = new float[16];
    private float[] orthographic_8 = new float[16];
    private float[] orthographic_5_11 = new float[16];

    private float you_ratio = 800.0f;
    private float you_scale;

    private int bid = 0;
    private buffer[] bgui;
    private HashSet<Integer> bcache = new HashSet<>();
    private HashMap<Integer, buffer[]> bworld = new HashMap<>();
    private HashMap<Integer, buffer[]> bdecal = new HashMap<>();
    private buffer bcanvas;
    private buffer bcanvas_2;
    private buffer bcanvas_4;
    private buffer bcanvas_8;
    private buffer bcanvas_5_11;

    private HashMap<Integer, Integer> normals = new HashMap<>();

    private frame gbuffer;
    private frame fping;
    private frame fpong;
    private frame fstor;
    private frame fstor_2;
    private frame fstor_4;
    private frame fstor_8;
    private frame fstor_2_pong;
    private frame fstor_4_pong;
    private frame fstor_8_pong;
    private frame fshadow;
    private frame fstor_5_11;

    public occluder occluder = new occluder();

    public map land;
    public you you;

    public ArrayList<light> lights;
    private ArrayList<buffer> blights;

    public Sequencer music;

    private final boolean bench = false;

    public state(client client) {
        this.client = client;

        mouse = client.mouse;
        keyboard = client.keyboard;

        p_geometry = opengl.make_program("geometry", 'n');
        p_texture2 = opengl.make_program("texture2", 'n');
        p_point_light = opengl.make_program("point_light", 'n');
        p_stencil = opengl.make_program("stencil", 'n');
        p_sky = opengl.make_program("sky", 'n');

        p_screen = opengl.make_program("screen", 's');
        p_blur_h = opengl.make_program("blur_horizontal", 's');
        p_blur_v = opengl.make_program("blur_vertical", 's');
        p_blur_h_f = opengl.make_program("blur_horizontal_float", 's');
        p_blur_v_f = opengl.make_program("blur_vertical_float", 's');
        p_threshold = opengl.make_program("threshold2", 's');
        p_ambient_occlusion = opengl.make_program("ambient_occlusion", 's');
        p_ambient_light = opengl.make_program("ambient_light", 's');
        p_bloom = opengl.make_program("bloom", 's');
        p_motion_blur = opengl.make_program("motion_blur", 's');
        p_generic = opengl.make_program("generic", 's');

        sprite.scale(1.0f / 16.0f);

        sprite t_baron_front_walk_0 = new sprite(opengl.texture("baron-front-walk-0", true));
        sprite t_baron_front_walk_1 = new sprite(opengl.texture("baron-front-walk-1", true));

        int t_baron_front_walk_0_normal = opengl.texture("baron-front-walk-0-normal", true).id;
        int t_baron_front_walk_1_normal = opengl.texture("baron-front-walk-1-normal", true).id;

        normals.put(t_baron_front_walk_0.texture, t_baron_front_walk_0_normal);
        normals.put(t_baron_front_walk_1.texture, t_baron_front_walk_1_normal);

        sprite t_baron_front_melee_0 = new sprite(opengl.texture("baron-front-melee-0", true));
        sprite t_baron_front_melee_1 = new sprite(opengl.texture("baron-front-melee-1", true));

        sprite t_baron_front_missile_0 = new sprite(opengl.texture("baron-front-missile-0", true));
        sprite t_baron_front_missile_1 = new sprite(opengl.texture("baron-front-missile-1", true));

        sprite t_baron_front_dead_0 = new sprite(opengl.texture("baron-front-dead-0", true));
        sprite t_baron_front_dead_1 = new sprite(opengl.texture("baron-front-dead-1", true));

        baron.walk_animation = new sprite[] { t_baron_front_walk_0, t_baron_front_walk_1 };
        baron.melee_animation = new sprite[] { t_baron_front_melee_0, t_baron_front_melee_1 };
        baron.missile_animation = new sprite[] { t_baron_front_missile_0, t_baron_front_missile_1 };
        baron.death_animation = new sprite[] { t_baron_front_dead_0, t_baron_front_dead_1 };
        baron.scream_sound = "res/baron-scream.wav";
        baron.pain_sound = "res/baron-pain.wav";
        baron.death_sound = "res/baron-death.wav";
        baron.melee_sound = "res/baron-melee.wav";
        baron.missile_sound = "res/baron-missile.wav";

        tree.dead_tree_sprite = new sprite(opengl.texture("dead-tree", true));
        medkit.medkit_sprite = new sprite(opengl.texture("medkit", true));

        plasma.plasma_sprite = new sprite(opengl.texture("plasma", true));
        plasma.plasma_impact = "res/plasma-impact.wav";

        blood.blood_small_sprite = new sprite(opengl.texture("blood-0", true));
        blood.blood_medium_sprite = new sprite(opengl.texture("blood-1", true));
        blood.blood_large_sprite = new sprite(opengl.texture("blood-2", true));

        sprite pe0 = new sprite(opengl.texture("plasma-explosion-0", true));
        sprite pe1 = new sprite(opengl.texture("plasma-explosion-1", true));
        sprite pe2 = new sprite(opengl.texture("plasma-explosion-2", true));
        plasma_explosion.plasma_explosion = new sprite[] { pe0, pe1, pe2 };

        t_grass = opengl.texture("grass", false).id;
        t_stone = opengl.texture("stone", false).id;
        t_stone_floor = opengl.texture("stone-floor", false).id;
        t_planks = opengl.texture("planks", false).id;
        t_plank_floor = opengl.texture("plank-floor", false).id;

        int t_grass_normal = opengl.texture("grass-normal", false).id;
        int t_stone_normal = opengl.texture("stone-normal", false).id;

        normals.put(t_grass, t_grass_normal);
        normals.put(t_stone, t_stone_normal);

        texture hand_0 = opengl.texture("hand-0", true);
        texture hand_1 = opengl.texture("hand-1", true);
        texture hand_2 = opengl.texture("hand-2", true);
        texture hand_3 = opengl.texture("hand-3", true);

        you.hand_animation = new texture[] { hand_0, hand_1, hand_2, hand_3, hand_2, hand_1, hand_0 };

        sky[0] = opengl.texture("sky-up", true).id;
        sky[1] = opengl.texture("sky-down", true).id;
        sky[2] = opengl.texture("sky-left", true).id;
        sky[3] = opengl.texture("sky-right", true).id;
        sky[4] = opengl.texture("sky-front", true).id;
        sky[5] = opengl.texture("sky-back", true).id;

        sky_buffer = new buffer(3, 0, 0, 0, 24, 36);
        sky_buffer.begin();
        draw.inverse_cube(sky_buffer);
        sky_buffer.end();

        music = midi.play("d_e1m1.mid");

        bgui = new buffer[2];
        bgui[0] = new buffer(2, 0, 2, 0, 4 * 1000, 6 * 1000);
        bgui[1] = new buffer(2, 0, 2, 0, 4 * 1000, 6 * 1000);

        bcanvas = new buffer(2, 0, 0, 0, 4, 6);
        bcanvas_2 = new buffer(2, 0, 0, 0, 4, 6);
        bcanvas_4 = new buffer(2, 0, 0, 0, 4, 6);
        bcanvas_8 = new buffer(2, 0, 0, 0, 4, 6);
        bcanvas_5_11 = new buffer(2, 0, 0, 0, 4, 6);

        gbuffer = new frame(1, 1, new int[] { GL_RGB, GL_RG16F }, new int[] { GL_RGB, GL_RG },
                new int[] { GL_UNSIGNED_INT, GL_FLOAT }, false, true);
        fstor = new frame(1, 1, new int[] { GL_RGB16F }, new int[] { GL_RGB }, new int[] { GL_FLOAT }, false, true);
        fping = new frame(1, 1, new int[] { GL_RGB16F }, new int[] { GL_RGB }, new int[] { GL_FLOAT }, true, false);
        fpong = new frame(1, 1, new int[] { GL_RGB16F }, new int[] { GL_RGB }, new int[] { GL_FLOAT }, true, false);
        fstor_2 = new frame(1, 1, new int[] { GL_RGB16F }, new int[] { GL_RGB }, new int[] { GL_FLOAT }, true, false);
        fstor_4 = new frame(1, 1, new int[] { GL_RGB16F }, new int[] { GL_RGB }, new int[] { GL_FLOAT }, true, false);
        fstor_8 = new frame(1, 1, new int[] { GL_RGB16F }, new int[] { GL_RGB }, new int[] { GL_FLOAT }, true, false);
        fstor_2_pong = new frame(1, 1, new int[] { GL_RGB16F }, new int[] { GL_RGB }, new int[] { GL_FLOAT }, true,
                false);
        fstor_4_pong = new frame(1, 1, new int[] { GL_RGB16F }, new int[] { GL_RGB }, new int[] { GL_FLOAT }, true,
                false);
        fstor_8_pong = new frame(1, 1, new int[] { GL_RGB16F }, new int[] { GL_RGB }, new int[] { GL_FLOAT }, true,
                false);
        fshadow = new frame(1, 1, new int[] { GL_RED }, new int[] { GL_RED }, new int[] { GL_UNSIGNED_BYTE }, false,
                true);
        fstor_5_11 = new frame(1, 1, new int[] { GL_RGB16F }, new int[] { GL_RGB }, new int[] { GL_FLOAT }, false,
                false);

        land = new map(this);

        final boolean plains = true;

        if (plains) {
            plain_tile.make(this);
            house.make(this, 10, 10);
            house.make(this, 40, 60);
            land.build();
        } else {
            sector grass_area = grassland.make(this);
            sector big_box_area = big_box.make(this, 100, 20);

            crunch.make(this, 2, 28);
            extra_crunch.make(this, 16, 29);
            complex.make(this, 11, 41);
            sweep.make(this, 13, 13);
            barrel.make(this, 3, 42);
            box.make(this, 24, 20);

            land.bridge(this, big_box_area, 0, grass_area, 3);

            land.build();
        }

        you = new you(land, mouse, keyboard, 10, 40, 0);

        new baron(land, 3, 23, 0);
        new baron(land, 20, 5, 0);
        new tree(land, 7, 35, 0);
        new medkit(land, 35, 35);

        lights = new ArrayList<>();
        blights = new ArrayList<>();

        Random seed = new Random();

        for (int i = 0; i < 20; i++)
            add_light(new light(1.0f + seed.nextFloat() * 40, 1.0f + seed.nextFloat() * 40,
                    0.1f + seed.nextFloat() * 4.8f, seed.nextFloat() + 0.5f, seed.nextFloat(), seed.nextFloat(),
                    1.0f + seed.nextFloat() * 20.0f));

        resize();
    }

    public void resize() {
        matrix.perspective(perspective, 60.0f, 0.3f, 100.0f, (float) client.width / (float) client.height);
        matrix.orthographic(orthographic, 0.0f, 0.0f, client.width, client.height, 0.0f, 1.0f);
        matrix.orthographic(orthographic_2, 0.0f, 0.0f, client.width / 2, client.height / 2, 0.0f, 1.0f);
        matrix.orthographic(orthographic_4, 0.0f, 0.0f, client.width / 4, client.height / 4, 0.0f, 1.0f);
        matrix.orthographic(orthographic_8, 0.0f, 0.0f, client.width / 8, client.height / 8, 0.0f, 1.0f);
        matrix.orthographic(orthographic_5_11, 0.0f, 0.0f, client.width / 2, (int) (client.height / 2), 0.0f, 1.0f);

        matrix.inverse(inverse_projection_matrix, perspective);

        you_scale = client.width / you_ratio;

        bcanvas.begin();
        draw.screen(bcanvas, 0, 0, client.width, client.height);
        bcanvas.end();

        bcanvas_2.begin();
        draw.screen(bcanvas_2, 0, 0, client.width / 2, client.height / 2);
        bcanvas_2.end();

        bcanvas_4.begin();
        draw.screen(bcanvas_4, 0, 0, client.width / 4, client.height / 4);
        bcanvas_4.end();

        bcanvas_8.begin();
        draw.screen(bcanvas_8, 0, 0, client.width / 8, client.height / 8);
        bcanvas_8.end();

        bcanvas_5_11.begin();
        draw.screen(bcanvas_5_11, 0, 0, client.width / 2, (int) (client.height / 2));
        bcanvas_5_11.end();

        gbuffer.resize(client.width, client.height);
        fstor.resize(client.width, client.height);
        fstor_2.resize(client.width / 2, client.height / 2);
        fstor_4.resize(client.width / 4, client.height / 4);
        fstor_8.resize(client.width / 8, client.height / 8);
        fstor_2_pong.resize(client.width / 2, client.height / 2);
        fstor_4_pong.resize(client.width / 4, client.height / 4);
        fstor_8_pong.resize(client.width / 8, client.height / 8);
        fping.resize(client.width, client.height);
        fpong.resize(client.width, client.height);
        fshadow.resize(client.width, client.height);
        fstor_5_11.resize(client.width / 2, (int) (client.height / 2));
    }

    public void close() {
        music.close();
    }

    public void events() {
        land.integrate();

        light li = lights.get(0);
        li.x = you.x;
        li.y = you.z + you.height / 2;
        li.z = you.y;
    }

    private void cache(int texture) {
        if (bcache.contains(texture) == false) {
            if (bworld.containsKey(texture) == false) {
                buffer[] b = { new buffer(3, 0, 2, 3, 4 * 1000, 6 * 1000), new buffer(3, 0, 2, 3, 4 * 1000, 6 * 1000) };
                bworld.put(texture, b);
            }

            bworld.get(texture)[bid].begin();
            bcache.add(texture);
        }
    }

    private void cache_decal(int texture) {
        if (bcache.contains(texture) == false) {
            if (bdecal.containsKey(texture) == false) {
                buffer[] b = { new buffer(3, 0, 2, 3, 4 * 1000, 6 * 1000), new buffer(3, 0, 2, 3, 4 * 1000, 6 * 1000) };
                bdecal.put(texture, b);
            }

            bdecal.get(texture)[bid].begin();
            bcache.add(texture);
        }
    }

    public void add_light(light li) {
        lights.add(li);

        if (blights.size() < lights.size())
            blights.add(new buffer(3, 0, 0, 0, 8, 36));
    }

    private void geometry_pass() {
        opengl.framebuffer(gbuffer.fbo);
        opengl.view(0, 0, client.width, client.height);
        opengl.clear_color(0.0f, 0.0f, 0.0f);
        opengl.clear();

        opengl.program(p_geometry);

        // opengl.perspective(perspective, view_matrix, -you.x, -you.eye, -you.y, sin_x,
        // cos_x, sin_y, cos_y);

        {
            float x = -you.x;
            float y = -you.eye;
            float z = -you.y;

            float pitch = you.look;
            float yaw = you.r;
            float roll = you.roll;

            float[] temporary = new float[16];
            float[] pitch_matrix = new float[16];
            float[] yaw_matrix = new float[16];
            float[] roll_matrix = new float[16];

            matrix.pitch(pitch_matrix, pitch);
            matrix.yaw(yaw_matrix, yaw);
            matrix.roll(roll_matrix, roll);

            matrix.multiply(temporary, pitch_matrix, yaw_matrix);
            matrix.multiply(view_matrix, roll_matrix, temporary);

            for (int i = 0; i < 16; i++)
                opengl.model_view_matrix[i] = view_matrix[i];

            matrix.translate(opengl.model_view_matrix, x, y, z);
            matrix.multiply(opengl.model_view_projection_matrix, perspective, opengl.model_view_matrix);
        }

        opengl.mvp();
        opengl.uniform("view_matrix", view_matrix);

        matrix.inverse(inverse_model_view_matrix, opengl.model_view_matrix);

        for (int i = 0; i < 16; i++) {
            previous_model_view_projection_matrix[i] = previous_model_view_projection_matrix_cache[i];
            previous_model_view_projection_matrix_cache[i] = opengl.model_view_projection_matrix[i];
        }

        matrix.multiply(current_to_previous_matrix, previous_model_view_projection_matrix, inverse_model_view_matrix);

        occluder.update(opengl.model_view_projection_matrix);

        for (int i = 0; i < land.sector_count; i++) {
            sector s = land.sectors[i];

            if (s.lines != null) {
                for (int j = 0; j < s.lines.length; j++) {
                    linedef l = s.lines[j];

                    if (l.bot != null) {
                        cache(l.bot.texture);
                        draw.wall(bworld.get(l.bot.texture)[bid], l.bot);
                    }

                    if (l.mid != null) {
                        cache(l.mid.texture);
                        draw.wall(bworld.get(l.mid.texture)[bid], l.mid);
                    }

                    if (l.top != null) {
                        cache(l.top.texture);
                        draw.wall(bworld.get(l.top.texture)[bid], l.top);
                    }
                }
            }

            for (int j = 0; j < s.triangles.length; j++) {
                triangle t = s.triangles[j];
                cache(t.texture);
                draw.triangle(bworld.get(t.texture)[bid], t);
            }
        }

        for (int i = 1; i < land.thing_count; i++) {
            thing t = land.things[i];

            if (occluder.circle(t.x, t.y, t.radius))
                continue;

            sprite s = t.sprite(you);
            cache(s.texture);

            float sin_s = t.x - you.x;
            float cos_s = t.y - you.y;
            float length = (float) Math.sqrt(sin_s * sin_s + cos_s * cos_s);
            sin_s /= length;
            cos_s /= length;

            draw.sprite(bworld.get(s.texture)[bid], t.x, t.z, t.y, -sin_s, -cos_s, s);
        }

        for (int i = 0; i < land.item_count; i++) {
            item t = land.items[i];

            if (occluder.circle(t.x, t.y, t.radius))
                continue;

            sprite s = t.sprite();
            cache(s.texture);

            float sin_s = t.x - you.x;
            float cos_s = t.y - you.y;
            float length = (float) Math.sqrt(sin_s * sin_s + cos_s * cos_s);
            sin_s /= length;
            cos_s /= length;

            draw.sprite(bworld.get(s.texture)[bid], t.x, t.z, t.y, -sin_s, -cos_s, s);
        }

        for (int i = 0; i < land.missile_count; i++) {
            missile t = land.missiles[i];

            if (occluder.circle(t.x, t.y, t.radius))
                continue;

            sprite s = t.sprite();
            cache(s.texture);

            float sin_s = t.x - you.x;
            float cos_s = t.y - you.y;
            float length = (float) Math.sqrt(sin_s * sin_s + cos_s * cos_s);
            sin_s /= length;
            cos_s /= length;

            draw.sprite(bworld.get(s.texture)[bid], t.x, t.z, t.y, -sin_s, -cos_s, s);
        }

        float sin_s = (float) Math.sin(-you.r);
        float cos_s = (float) Math.cos(-you.r);

        for (int i = 0; i < land.particle_count; i++) {
            particle t = land.particles[i];

            if (occluder.circle(t.x, t.y, t.radius))
                continue;

            sprite s = t.sprite();
            cache(s.texture);

            draw.sprite(bworld.get(s.texture)[bid], t.x, t.z, t.y, sin_s, cos_s, s);
        }

        for (int b : bcache) {
            buffer[] buf = bworld.get(b);

            buf[bid].end();

            opengl.texture0(b);

            if (normals.containsKey(b))
                opengl.texture1(normals.get(b));
            else
                opengl.texture1(b);

            opengl.draw_elements(buf[bid]);
        }

        bcache.clear();

        for (int i = 0; i < land.decal_count; i++) {
            decal t = land.decals[i];
            cache_decal(t.texture);
            draw.decal(bdecal.get(t.texture)[bid], t);
        }

        opengl.depth_mask_off();
        glEnable(GL_POLYGON_OFFSET_FILL);
        glPolygonOffset(-1, -1);

        for (int b : bcache) {
            buffer[] buf = bdecal.get(b);

            buf[bid].end();

            opengl.texture0(b);

            if (normals.containsKey(b))
                opengl.texture1(normals.get(b));
            else
                opengl.texture1(b);

            opengl.draw_elements(buf[bid]);
        }

        bcache.clear();

        opengl.depth_mask_on();
        glDisable(GL_STENCIL_TEST);
        glDisable(GL_POLYGON_OFFSET_FILL);
    }

    public void draw() {
        long before = System.currentTimeMillis();

        // geometry

        glDepthMask(true);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);

        geometry_pass();

        // lights

        glEnable(GL_BLEND);
        glBlendEquation(GL_FUNC_ADD);
        glBlendFunc(GL_ONE, GL_ONE);

        opengl.framebuffer(fstor.fbo);

        glClear(GL_COLOR_BUFFER_BIT);

        glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, gbuffer.depth_texture, 0);

        glDepthMask(false);

        opengl.program(p_stencil);
        opengl.mvp();

        opengl.program(p_point_light);
        opengl.mvp();

        opengl.uniform("inverse_projection", inverse_projection_matrix);
        opengl.uniform("texel", 1.0f / client.width, 1.0f / client.height);

        opengl.texture0(gbuffer.textures[0]);
        opengl.texture1(gbuffer.textures[1]);
        opengl.texture2(gbuffer.depth_texture);

        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);

        final int cull_type = 1;

        if (cull_type == 1 || cull_type == 2)
            glEnable(GL_STENCIL_TEST);

        if (cull_type == 2) {
            opengl.program(p_stencil);

            glClearStencil(1);
            glClear(GL_STENCIL_BUFFER_BIT);

            glDrawBuffer(GL_NONE);
            glDisable(GL_BLEND);
            glDepthFunc(GL_GEQUAL);
            glCullFace(GL_FRONT);
            glStencilFunc(GL_ALWAYS, 0, 0);
            glStencilOp(GL_KEEP, GL_KEEP, GL_ZERO);

            for (int i = 0; i < lights.size(); i++) {
                light li = lights.get(i);
                buffer b = blights.get(i);
                b.begin();
                draw.cube(b, li.x, li.y, li.z, li.radius);
                b.end();

                opengl.draw_elements(b);
            }

            opengl.program(p_point_light);

            glDrawBuffers(fstor.draw_buffers);
            glEnable(GL_BLEND);
            glStencilFunc(GL_EQUAL, 0, 0xff);
            glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP);
        }

        for (int i = 0; i < lights.size(); i++) {
            light li = lights.get(i);
            buffer b = blights.get(i);

            if (cull_type == 0 || cull_type == 1) {
                b.begin();
                draw.cube(b, li.x, li.y, li.z, li.radius);
                b.end();
            }

            if (cull_type == 1) {
                opengl.program(p_stencil);

                glClear(GL_STENCIL_BUFFER_BIT);

                glDrawBuffer(GL_NONE);
                glDisable(GL_BLEND);
                glDepthFunc(GL_LEQUAL);
                glCullFace(GL_BACK);
                glStencilFunc(GL_ALWAYS, 0, 0);
                glStencilOp(GL_KEEP, GL_INCR, GL_KEEP);

                opengl.draw_elements(b);

                opengl.program(p_point_light);

                glDrawBuffers(fstor.draw_buffers);
                glEnable(GL_BLEND);
                glDepthFunc(GL_GEQUAL);
                glCullFace(GL_FRONT);
                glStencilFunc(GL_EQUAL, 0, 0xff);
                glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP);
            } else if (cull_type == 0 || cull_type == 2) {
                if (you.x + you.radius >= li.x - li.radius && you.x - you.radius <= li.x + li.radius
                        && you.y + you.radius >= li.z - li.radius && you.y - you.radius <= li.z + li.radius) {
                    glDepthFunc(GL_GEQUAL);
                    glCullFace(GL_FRONT);
                } else {
                    glDepthFunc(GL_LEQUAL);
                    glCullFace(GL_BACK);
                }
            }

            matrix.multiply_vector(opengl.model_view_matrix, li.x, li.y, li.z);
            opengl.uniform("light_view_space_position", matrix.vector_x, matrix.vector_y, matrix.vector_z);
            opengl.uniform("light_color", li.r, li.g, li.b);
            opengl.uniform("light_inverse_radius_squared", li.inverse_radius_squared);

            opengl.draw_elements(b);
        }

        glDisable(GL_STENCIL_TEST);
        glDepthFunc(GL_LEQUAL);
        glDisable(GL_DEPTH_TEST);
        glCullFace(GL_BACK);

        opengl.program(p_ambient_light);
        opengl.orthographic(orthographic, 0.0f, 0.0f);
        opengl.mvp();
        opengl.texture0(gbuffer.textures[0]);
        opengl.draw_elements(bcanvas);

        glDisable(GL_BLEND);

        // post processing

        final int process = 0;

        if (process == 0) {
            /*
             * opengl.framebuffer(0); opengl.program(p_screen); opengl.view(0, 0,
             * client.width, client.height); opengl.orthographic(orthographic, 0.0f, 0.0f);
             * opengl.mvp(); opengl.texture0(fstor.textures[0]);
             * opengl.draw_elements(bcanvas);
             */

            opengl.framebuffer(fstor_5_11.fbo);
            opengl.program(p_screen);
            opengl.view(0, 0, client.width / 2, (int) (client.height / 2));
            opengl.orthographic(orthographic_5_11, 0.0f, 0.0f);
            opengl.mvp();
            opengl.texture0(fstor.textures[0]);
            opengl.draw_elements(bcanvas_5_11);

            opengl.framebuffer(0);
            opengl.program(p_screen);
            opengl.view(0, 0, client.width, client.height);
            opengl.orthographic(orthographic, 0.0f, 0.0f);
            opengl.mvp();
            opengl.texture0(fstor_5_11.textures[0]);
            opengl.draw_elements(bcanvas);
        } else if (process == 1) {
            opengl.framebuffer(fshadow.fbo);
            opengl.program(p_ambient_occlusion);
            opengl.view(0, 0, client.width, client.height);
            opengl.uniform("texel", 1.0f / client.width, 1.0f / client.height);
            opengl.orthographic(orthographic, 0.0f, 0.0f);
            opengl.mvp();
            opengl.texture1(gbuffer.textures[1]);
            opengl.texture2(gbuffer.depth_texture);
            opengl.draw_elements(bcanvas);

            opengl.framebuffer(fpong.fbo);
            opengl.program(p_blur_h);
            opengl.mvp();
            opengl.texture0(fshadow.textures[0]);
            opengl.uniform("texel", 1.0f / fping.width);
            opengl.draw_elements(bcanvas);

            opengl.framebuffer(fping.fbo);
            opengl.program(p_blur_v);
            opengl.mvp();
            opengl.texture0(fpong.textures[0]);
            opengl.uniform("texel", 1.0f / fping.height);
            opengl.draw_elements(bcanvas);

            opengl.framebuffer(0);
            opengl.program(p_generic);
            opengl.view(0, 0, client.width, client.height);
            opengl.orthographic(orthographic, 0.0f, 0.0f);
            opengl.mvp();
            opengl.texture0(fstor.textures[0]);
            opengl.texture1(fping.textures[0]);
            opengl.draw_elements(bcanvas);
        } else {
            final boolean occlusion = true;
            final boolean blur_1 = false;
            final boolean blur_2 = false;
            final boolean blur_4 = false;
            final boolean blur_8 = true;

            if (occlusion) {
                opengl.framebuffer(fshadow.fbo);
                opengl.program(p_ambient_occlusion);
                opengl.view(0, 0, client.width, client.height);
                opengl.uniform("texel", 1.0f / client.width, 1.0f / client.height);
                opengl.orthographic(orthographic, 0.0f, 0.0f);
                opengl.mvp();
                opengl.texture1(gbuffer.textures[1]);
                opengl.texture2(gbuffer.depth_texture);
                opengl.draw_elements(bcanvas);

                opengl.framebuffer(fping.fbo);
                opengl.program(p_blur_h_f);
                opengl.mvp();
                opengl.texture0(fshadow.textures[0]);
                opengl.uniform("texel", 1.0f / fping.width);
                opengl.draw_elements(bcanvas);

                opengl.framebuffer(fshadow.fbo);
                opengl.program(p_blur_v_f);
                opengl.mvp();
                opengl.texture0(fping.textures[0]);
                opengl.uniform("texel", 1.0f / fping.height);
                opengl.draw_elements(bcanvas);
            }

            opengl.framebuffer(fping.fbo);
            opengl.program(p_threshold);
            opengl.orthographic(orthographic, 0.0f, 0.0f);
            opengl.mvp();
            opengl.texture0(fstor.textures[0]);
            opengl.uniform("threshold", 0.5f);
            opengl.draw_elements(bcanvas);

            if (blur_1) {
                opengl.framebuffer(fpong.fbo);
                opengl.program(p_blur_h);
                opengl.mvp();
                opengl.texture0(fping.textures[0]);
                opengl.uniform("texel", 1.0f / fping.width);
                opengl.draw_elements(bcanvas);

                opengl.framebuffer(fping.fbo);
                opengl.program(p_blur_v);
                opengl.mvp();
                opengl.texture0(fpong.textures[0]);
                opengl.uniform("texel", 1.0f / fping.height);
                opengl.draw_elements(bcanvas);
            }

            opengl.framebuffer(fstor_2.fbo);
            opengl.program(p_screen);
            opengl.view(0, 0, client.width / 2, client.height / 2);
            opengl.orthographic(orthographic_2, 0.0f, 0.0f);
            opengl.mvp();
            opengl.texture0(fping.textures[0]);
            opengl.draw_elements(bcanvas_2);

            if (blur_2) {
                opengl.framebuffer(fstor_2_pong.fbo);
                opengl.program(p_blur_h);
                opengl.mvp();
                opengl.texture0(fstor_2.textures[0]);
                opengl.uniform("texel", 1.0f / fstor_2.width);
                opengl.draw_elements(bcanvas_2);

                opengl.framebuffer(fstor_2.fbo);
                opengl.program(p_blur_v);
                opengl.mvp();
                opengl.texture0(fstor_2_pong.textures[0]);
                opengl.uniform("texel", 1.0f / fstor_2.height);
                opengl.draw_elements(bcanvas_2);
            }

            opengl.framebuffer(fstor_4.fbo);
            opengl.program(p_screen);
            opengl.view(0, 0, client.width / 4, client.height / 4);
            opengl.orthographic(orthographic_4, 0.0f, 0.0f);
            opengl.mvp();
            opengl.texture0(fstor_2.textures[0]);
            opengl.draw_elements(bcanvas_4);

            if (blur_4) {
                opengl.framebuffer(fstor_4_pong.fbo);
                opengl.program(p_blur_h);
                opengl.mvp();
                opengl.texture0(fstor_4.textures[0]);
                opengl.uniform("texel", 1.0f / fstor_4.width);
                opengl.draw_elements(bcanvas_4);

                opengl.framebuffer(fstor_4.fbo);
                opengl.program(p_blur_v);
                opengl.mvp();
                opengl.texture0(fstor_4_pong.textures[0]);
                opengl.uniform("texel", 1.0f / fstor_4.height);
                opengl.draw_elements(bcanvas_4);
            }

            opengl.framebuffer(fstor_8.fbo);
            opengl.program(p_screen);
            opengl.view(0, 0, client.width / 8, client.height / 8);
            opengl.orthographic(orthographic_8, 0.0f, 0.0f);
            opengl.mvp();
            opengl.texture0(fstor_4.textures[0]);
            opengl.draw_elements(bcanvas_8);

            if (blur_8) {
                opengl.framebuffer(fstor_8_pong.fbo);
                opengl.program(p_blur_h);
                opengl.mvp();
                opengl.texture0(fstor_8.textures[0]);
                opengl.uniform("texel", 1.0f / fstor_8.width);
                opengl.draw_elements(bcanvas_8);

                opengl.framebuffer(fstor_8.fbo);
                opengl.program(p_blur_v);
                opengl.mvp();
                opengl.texture0(fstor_8_pong.textures[0]);
                opengl.uniform("texel", 1.0f / fstor_8.height);
                opengl.draw_elements(bcanvas_8);
            }

            /*
             * opengl.framebuffer(0); opengl.program(p_bloom); opengl.view(0, 0,
             * client.width, client.height); opengl.orthographic(orthographic, 0.0f, 0.0f);
             * opengl.mvp(); opengl.texture0(fstor.textures[0]);
             * //opengl.texture1(fstor_2.textures[0]); opengl.texture1(fshadow.textures[0]);
             * //opengl.texture2(fstor_4.textures[0]); opengl.texture3(fstor_8.textures[0]);
             * opengl.draw_elements(bcanvas);
             */

            opengl.framebuffer(0);
            opengl.program(p_motion_blur);
            opengl.view(0, 0, client.width, client.height);
            opengl.orthographic(orthographic, 0.0f, 0.0f);
            opengl.mvp();
            opengl.uniform("inverse_projection", inverse_projection_matrix);
            opengl.uniform("current_to_previous_matrix", current_to_previous_matrix);
            opengl.texture0(fstor.textures[0]);
            opengl.texture1(gbuffer.depth_texture);
            opengl.draw_elements(bcanvas);
        }

        opengl.program(p_texture2);
        opengl.orthographic(orthographic, 0.0f, 0.0f);
        opengl.mvp();
        texture anim = you.animation[you.frame];
        opengl.texture0(anim.id);
        bgui[bid].begin();
        float w = you_scale * anim.width;
        float h = you_scale * anim.height;
        draw.image(bgui[bid], (client.width - w) / 2 + you.hand_x * you_scale,
                client.height - h + you.hand_y * you_scale, w, h, 0, 0, 1, 1);
        bgui[bid].end();
        opengl.draw_elements(bgui[bid]);

        bid = 1 - bid;

        if (bench)
            System.out.println("time: " + (System.currentTimeMillis() - before));
    }
}

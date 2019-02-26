
package land.things;

import client.graphics.sprite;
import client.audio.sound;
import land.m_random;
import land.map;
import land.map_util;
import land.things.missiles.plasma;
import land.things.particles.blood;

public class baron extends npc
{
    public static sprite[] walk_animation;
    public static sprite[] melee_animation;
    public static sprite[] missile_animation;
    public static sprite[] death_animation;
    
    public static String scream_sound;
    public static String pain_sound;
    public static String death_sound;
    public static String melee_sound;
    public static String missile_sound;
    
    public static final int ANIMATION_RATE = 32;
    
    public static final int ANIMATION_NOT_DONE = 0;
    public static final int ANIMATION_DONE = 1;
    public static final int ANIMATION_ALMOST_DONE = 2;
    
    public static final int STATUS_SLEEP = 0;
    public static final int STATUS_CHASE = 1;
    public static final int STATUS_MELEE = 2;
    public static final int STATUS_MISSILE = 3;
    public static final int STATUS_DEAD = 4;
    public static final int STATUS_LOOK = 5;
    
    public int melee_range = 4;
    public int missile_range = 20;
    
    public int status = STATUS_SLEEP;
    
    public sprite[] animation;
    public int frame;
    public int mod;
    
    public int reaction;
    
    public baron(map map, float x, float y, float r)
    {
        super(map, x, y, r, 0.7f, 4.8f, 1.0f);
        
        flags = flag_demon;
        animation = walk_animation;
        speed = 0.1f;
        health = 5;
        status = STATUS_LOOK;
    }
    
    private void look()
    {
        for (int i = 0; i < map.thing_count; i++)
        {
            thing t = map.things[i];
            
            if (t == this) continue;
            
            if ((t.flags & flag_human) != 0 && t.health > 0)
            {
                target = t;
                new sound(scream_sound).start();
                status = STATUS_CHASE;
                return;
            }
        }
    }
    
    private void missile()
    {
        final float speed = 0.3f;
        
        float angle = (float)Math.atan2(target.y - y, target.x - x);
        
        float dx = (float)Math.cos(angle);
        float dy = (float)Math.sin(angle);
        
        float dist = map_util.approximate_distance(this, target);
        
        float dz = (target.z + target.height / 2 - z - height / 2) / (dist / speed);
        
        new plasma(map, 1 + m_random.p_random() % 3, x + dx * radius * 2.0f, y + dy * radius * 2.0f, z + height / 2, dx * speed, dy * speed, dz);
    }
    
    private void melee()
    {
        if (map_util.approximate_distance(this, target) <= melee_range)
            target.damage(1 + m_random.p_random() % 3);
    }
    
    public void damage(int d)
    {
        health -= d;
        
        if (status != STATUS_DEAD)
        {
            if (health < 1)
            {
                status = STATUS_DEAD;
                mod = 0;
                frame = 0;
                animation = death_animation;
                new sound(death_sound).start();
                remove_from_cell();
            }
            else
            {
                new sound(pain_sound).start();
            }
            
            for (int i = 0; i < 20; i++)
            {
                int random = m_random.p_random() % 3;
                
                sprite s;
                if (random == 1) s = blood.blood_small_sprite;
                else if (random == 2) s = blood.blood_medium_sprite;
                else s = blood.blood_large_sprite;
                
                float x = this.x - m_random.p_random() / 256.0f * radius + m_random.p_random() / 256.0f * radius * 2;
                float y = this.y - m_random.p_random() / 256.0f * radius + m_random.p_random() / 256.0f * radius * 2;
                float z = this.z + m_random.p_random() / 256.0f * height;
                
                blood b = new blood(map, s, x, y, z);
                
                b.dx = m_random.p_random() / 256.0f * 0.3f - m_random.p_random() / 256.0f * 0.6f;
                b.dy = m_random.p_random() / 256.0f * 0.3f - m_random.p_random() / 256.0f * 0.6f;
                b.dz = m_random.p_random() / 256.0f * 0.3f;
            }
        }
    }
    
    public void integrate()
    {
        if (status == STATUS_DEAD)
        {
            if (frame < animation.length - 1)
                animation();
        }
        else if (status == STATUS_MELEE)
        {
            int anim = animation();
            
            if (anim == ANIMATION_ALMOST_DONE)
            {
                reaction = 40 + m_random.p_random() % 220;
                melee();
            }
            else if (anim == ANIMATION_DONE)
            {
                frame = 0;
                status = STATUS_CHASE;
                animation = walk_animation;
            }
        }
        else if (status == STATUS_MISSILE)
        {
            int anim = animation();
            
            if (anim == ANIMATION_ALMOST_DONE)
            {
                reaction = 40 + m_random.p_random() % 220;
                missile();
            }
            else if (anim == ANIMATION_DONE)
            {
                frame = 0;
                status = STATUS_CHASE;
                animation = walk_animation;
            }
        }
        else if (status == STATUS_LOOK)
        {
            look();
            
            if (animation() == ANIMATION_DONE)
                frame = 0;
        }
        else if (status == STATUS_CHASE)
        {
            if (reaction > 0)
                reaction--;
            
            if (target == null || target.health <= 0)
            {
                target = null;
                status = STATUS_LOOK;
            }
            else
            {
                float dist = map_util.approximate_distance(this, target);
                
                if (reaction == 0 && dist <= melee_range)
                {
                    status = STATUS_MELEE;
                    mod = 0;
                    frame = 0;
                    animation = melee_animation;
                    new sound(melee_sound).start();
                }
                else if (reaction == 0 && dist <= missile_range)
                {
                    status = STATUS_MISSILE;
                    mod = 0;
                    frame = 0;
                    animation = missile_animation;
                    new sound(missile_sound).start();
                }
                else
                {
                    move_count--;
                    
                    if (move_count < 0 || move() == false)
                    {
                        new_direction();
                    }
                    
                    if (animation() == ANIMATION_DONE)
                        frame = 0;
                }
            }
        }
    }
    
    private int animation()
    {
        mod++;
        
        if (mod == ANIMATION_RATE)
        {
            mod = 0;
            
            frame++;
            
            if (frame == animation.length - 1)
                return ANIMATION_ALMOST_DONE;
            else if (frame == animation.length)
                return ANIMATION_DONE;
        }
        
        return ANIMATION_NOT_DONE;
    }
    
    public sprite sprite(thing t)
    {
        return animation[frame];
    }
}

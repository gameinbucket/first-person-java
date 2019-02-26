package client.audio;

import client.client;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class sound extends Thread
{
    public static boolean mute = false;
    
    public volatile boolean on = false;
    
    String url;
    
    public sound(String url)
    {
        this.url = url;
    }
    
    public void run()
    {
        if (mute) return;
        
        on = true;
        
        try
        {
            Clip clip = AudioSystem.getClip();
            AudioInputStream stream = AudioSystem.getAudioInputStream(client.class.getResourceAsStream(url));
            
            clip.open(stream);
            clip.start();
        }
        catch (Exception e)
        {
            System.out.println("audio failed " + e);
        }
        
        on = false;
    }
    
}
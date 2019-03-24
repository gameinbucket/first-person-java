package gameinbucket.app.client.audio;

import java.io.BufferedInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class sound extends Thread {

    public static boolean mute = false;
    public volatile boolean on = false;
    String url;

    public sound(String url) {
        this.url = url;
    }

    public void run() {
        if (mute)
            return;

        on = true;

        Path path = Paths.get(url);

        try {
            Clip clip = AudioSystem.getClip();
            BufferedInputStream buffer = new BufferedInputStream(Files.newInputStream(path));
            AudioInputStream stream = AudioSystem.getAudioInputStream(buffer);

            clip.open(stream);
            clip.start();
        } catch (Exception e) {
            System.out.println("audio failed (" + path + ")");
            e.printStackTrace();
        }

        on = false;
    }

}

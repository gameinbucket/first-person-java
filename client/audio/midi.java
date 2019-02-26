
package client.audio;

import java.nio.file.Paths;
import javax.sound.midi.*;

public abstract class midi
{
    public static Sequencer play(String mid)
    {
        try
        {
            Sequence sequence = MidiSystem.getSequence(Paths.get("src/res/" + mid).toAbsolutePath().toFile());
            
            Sequencer sequencer = MidiSystem.getSequencer();
            sequencer.open();
            sequencer.setSequence(sequence);
            sequencer.start();
            
            return sequencer;
        }
        catch (Exception e)
        {
            System.out.println(e);
            System.exit(1);
            return null;
        }
    }
}

package client.audio;

import java.nio.file.Paths;
import javax.sound.midi.*;
import sun.util.logging.PlatformLogger;

public class play_midi
{
    public static void main(String[] arguments) throws Exception
    {
        PlatformLogger.getLogger("java.util.prefs").setLevel(PlatformLogger.Level.SEVERE);
        
        final Sequence sequence = MidiSystem.getSequence(Paths.get("src/res/d_e1m1.mid").toAbsolutePath().toFile());
        
        Sequencer sequencer = MidiSystem.getSequencer();
        sequencer.open();
        sequencer.setSequence(sequence);
        sequencer.start();
        
        sequencer.addMetaEventListener(new MetaEventListener()
        {
            @Override public void meta(MetaMessage metaMsg)
            {
                if (metaMsg.getType() == 0x2f)
                {
                    sequencer.close();
                }
            }
        });
    }
}
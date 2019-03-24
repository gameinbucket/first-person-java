
package gameinbucket.app.client.audio;

import java.nio.file.Paths;
import javax.sound.midi.*;

public class play_midi {
    public static void main(String[] arguments) throws Exception {
        final Sequence sequence = MidiSystem.getSequence(Paths.get("res/d_e1m1.mid").toAbsolutePath().toFile());

        Sequencer sequencer = MidiSystem.getSequencer();
        sequencer.open();
        sequencer.setSequence(sequence);
        sequencer.start();

        sequencer.addMetaEventListener(new MetaEventListener() {
            @Override
            public void meta(MetaMessage metaMsg) {
                if (metaMsg.getType() == 0x2f) {
                    sequencer.close();
                }
            }
        });
    }
}

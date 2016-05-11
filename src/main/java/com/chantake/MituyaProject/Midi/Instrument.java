
package com.chantake.MituyaProject.Midi;

import org.bukkit.Sound;

/**
 *
 * @author fumitti
 */
public class Instrument {

    // This method could use some fixing to include the other noteblock sounds...
    public static Sound getInstrument(int instrument) {

        if (instrument >= 1 && instrument <= 8 || // Piano family
                (instrument >= 17 && instrument <= 27) || // Organs, Electric guitar (jazz) and acoustic guitards
                (instrument >= 41 && instrument <= 43)
                || (instrument >= 47 && instrument <= 112)) { // Strings, ensemble, brass, reed, pipe, synth lead, pad & effects, ethnic

            return Sound.BLOCK_NOTE_PLING;

        } else if ((instrument >= 28 && instrument <= 40) || // Guitar starting from Electric (clean) & bass
                (instrument >= 44 && instrument <= 46)) { // Contrabass to Pizzicato

            return Sound.BLOCK_NOTE_BASS;

        } else if (instrument == 0 || // drums
                (instrument >= 113 && instrument <= 119)) { // percussive

            return Sound.BLOCK_NOTE_BASEDRUM;

        } else if (instrument >= 120 && instrument <= 127) {

            return Sound.BLOCK_NOTE_SNARE;

        } else {

            return Sound.BLOCK_NOTE_PLING;

        }

    }
}

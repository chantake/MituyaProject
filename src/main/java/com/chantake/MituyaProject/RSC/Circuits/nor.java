package com.chantake.MituyaProject.RSC.Circuits;

import com.chantake.MituyaProject.RSC.BitSet.BitSet7;
import com.chantake.MituyaProject.RSC.Circuit.BitSetCircuit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tal Eisenberg
 */
public class nor extends BitSetCircuit {

    @Override
    protected void bitSetChanged(int bitSetIdx, BitSet7 set) {
        try {
            BitSet7 out = (BitSet7)inputBitSets[0].clone();
            for (int i = 1; i < this.inputBitSets.length; i++) {
                out.or(inputBitSets[i]);
            }

            // not gate after series of ors
            out.flip(0, wordlength);

            this.sendBitSet(out);
        }
        catch (CloneNotSupportedException ex) {
            Logger.getLogger(nor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

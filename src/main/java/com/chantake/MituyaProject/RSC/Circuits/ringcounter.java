package com.chantake.MituyaProject.RSC.Circuits;

import com.chantake.MituyaProject.RSC.BitSet.BitSet7;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Tal Eisenberg
 */
public class ringcounter extends counter {

    BitSet7 register;

    @Override
    public void inputChange(int inIdx, boolean newLevel) {
        if (newLevel) {
            if (inIdx == 0) { // clock pin
                register.clear();
                register.set(count);
                if (count < outputs.length - 1) {
                    count++;
                } else {
                    count = 0;
                }
            } else if (inIdx == 1) { // reset pin
                register.clear();
                count = 0;
            }

            sendBitSet(register);
        }
    }

    @Override
    protected boolean init(CommandSender sender, String[] args) {
        if (inputs.length == 0) {
            error(sender, "Expecting at least 1 clock input. A 2nd reset input pin is optional.");
            return false;
        } else if (outputs.length == 0) {
            error(sender, "Expecting at least 1 output.");
            return false;
        }

        register = new BitSet7(outputs.length);
        count = 0;
        return true;
    }
}

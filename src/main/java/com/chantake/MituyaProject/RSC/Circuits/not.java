package com.chantake.MituyaProject.RSC.Circuits;

import com.chantake.MituyaProject.RSC.Circuit.Circuit;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Tal Eisenberg
 */
public class not extends Circuit {

    @Override
    public void inputChange(int inIdx, boolean newLevel) {
        sendOutput(inIdx, !newLevel);
    }

    @Override
    protected boolean init(CommandSender sender, String[] args) {
        if (inputs.length != outputs.length) {
            error(sender, "Expecting the same number of inputs and outputs.");
            return false;
        }

        return true;
    }
}

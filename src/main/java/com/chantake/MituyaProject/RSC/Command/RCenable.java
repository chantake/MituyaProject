package com.chantake.MituyaProject.RSC.Command;

import com.chantake.MituyaProject.RSC.Chip.Chip;
import com.chantake.MituyaProject.RSC.RCPrefs;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author Tal Eisenberg
 */
public class RCenable extends RCRemoteChipCommand {

    @Override
    protected void runWithChip(Chip target, CommandSender sender, Command command, String label, String[] args) {
        if (target.isDisabled()) {
            target.enable();
            sender.sendMessage(RCPrefs.getInfoColor() + "Enabled " + target + ".");
        } else sender.sendMessage(RCPrefs.getDebugColor() + "The chip is already enabled.");
    }
}
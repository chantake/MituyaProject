package com.chantake.MituyaProject.RSC.Command;

import com.chantake.MituyaProject.RSC.Chip.Chip;
import com.chantake.MituyaProject.RSC.RCPrefs;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author Tal Eisenberg
 */
public class RCbreak extends RCRemoteChipCommand {

    @Override
    protected void runWithChip(Chip target, CommandSender sender, Command command, String label, String[] args) {
        if (rc.chipManager().destroyChip(target, sender, false))
            info(sender, ChatColor.YELLOW + target.toString() + RCPrefs.getInfoColor() + " was deactivated.");
    }
}

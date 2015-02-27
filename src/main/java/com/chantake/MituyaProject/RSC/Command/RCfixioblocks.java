package com.chantake.MituyaProject.RSC.Command;

import com.chantake.MituyaProject.RSC.Chip.Chip;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author Tal Eisenberg
 */
public class RCfixioblocks extends RCRemoteChipCommand {

    @Override
    protected void runWithChip(Chip target, CommandSender sender, Command command, String label, String[] args) {
        int blockCount = rc.chipManager().fixIOBlocks(target);
        info(sender, "Finished fixing i/o blocks of " + target + ". " + blockCount + " blocks were replaced.");
    }
}

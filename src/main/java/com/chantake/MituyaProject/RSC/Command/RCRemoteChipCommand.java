package com.chantake.MituyaProject.RSC.Command;

import com.chantake.MituyaProject.RSC.Chip.Chip;
import com.chantake.MituyaProject.RSC.RCPermissions;
import com.chantake.MituyaProject.RSC.RCPrefs;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author taleisenberg
 */
public abstract class RCRemoteChipCommand extends RCCommand {

    private static String[] truncateArgs(String[] args) {
        String[] ret = new String[args.length - 1];
        System.arraycopy(args, 1, ret, 0, ret.length);
        return ret;
    }

    @Override
    public void run(CommandSender sender, Command command, String label, String[] args) {
        Chip c;
        if (useTargetChip(args)) { // use target chip
            c = CommandUtils.findTargetChip(sender);
            if (c != null) runWithChip(c, sender, command, label, args);
        } else {
            if (!RCPermissions.enforceRemoteCommand(sender, command.getName())) return;
            c = rc.chipManager().getAllChips().getById(args[0]);
            if (c != null) runWithChip(c, sender, command, label, truncateArgs(args));
            else
                sender.sendMessage(RCPrefs.getErrorColor() + "There's no activated chip with id " + args[0]);
        }
    }

    protected abstract void runWithChip(Chip target, CommandSender sender, Command command, String label, String[] args);

    protected int argsCountForLocalTarget() {
        return 0;
    }

    private boolean useTargetChip(String[] args) {
        //System.out.println("len=" + args.length + " count=" + argsCountForLocalTarget());
        return args.length == argsCountForLocalTarget();
    }

}

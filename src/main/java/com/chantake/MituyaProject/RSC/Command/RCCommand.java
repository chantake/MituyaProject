package com.chantake.MituyaProject.RSC.Command;

import com.chantake.MituyaProject.RSC.RCPermissions;
import com.chantake.MituyaProject.RSC.RCPrefs;
import com.chantake.MituyaProject.RSC.RedstoneChips;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Tal Eisenberg
 */
public abstract class RCCommand implements CommandExecutor {
    protected RedstoneChips rc;

    public static void error(CommandSender sender, String message) {
        if (sender != null) sender.sendMessage(RCPrefs.getErrorColor() + message);
    }

    public static void info(CommandSender sender, String message) {
        if (sender != null) sender.sendMessage(RCPrefs.getInfoColor() + message);
    }

    public void setRCInstance(RedstoneChips rc) {
        this.rc = rc;
    }

    public boolean isOpRequired() {
        return false;
    }

    public boolean isPlayerRequired() {
        return false;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (isPlayerRequired()) {
            Player player = CommandUtils.enforceIsPlayer(sender);
            if (player == null) return true;
        }

        if (!RCPermissions.enforceCommand(sender, command.getName(), isOpRequired(), true)) return true;

        run(sender, command, label, args);
        return true;
    }

    public abstract void run(CommandSender sender, Command command, String label, String[] args);

}

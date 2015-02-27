package com.chantake.MituyaProject.RSC.Command;

import com.chantake.MituyaProject.RSC.Circuit.CircuitIndex;
import com.chantake.MituyaProject.RSC.Circuit.CircuitLoader;
import com.chantake.MituyaProject.RSC.RCPrefs;
import com.chantake.MituyaProject.RSC.UpdateChecker;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

/**
 * @author Tal Eisenberg
 */
public class RedstoneChips extends RCCommand {

    @Override
    public void run(final CommandSender sender, Command command, String label, String[] args) {
        rc.getServer().getScheduler().runTaskAsynchronously(rc, () -> showMessage(sender));
    }

    private void showMessage(CommandSender sender) {
        String verMsg;

        try {
            if (RCPrefs.getCheckForUpdates()) {
                String curver = UpdateChecker.checkUpdate(rc.getDescription().getVersion());
                if (curver == null) verMsg = " - RedstoneChips is up to date.";
                else verMsg = ChatColor.GREEN + " - A new RedstoneChips version (" + curver + ") is available.";
            } else {
                verMsg = "Update checking is disabled.";
            }
        } catch (IOException ex) {
            verMsg = " - Couldn't check for a new plugin version (" + ex.getClass().getSimpleName() + ".";
        }

        ChatColor color = (sender instanceof Player ? ChatColor.GRAY : ChatColor.WHITE);
        sender.sendMessage(ChatColor.RED + "---- " + rc.getDescription().getName() + " " + rc.getDescription().getVersion() + " -----------------------------");
        sender.sendMessage(ChatColor.YELLOW + "Website: " + color + rc.getDescription().getWebsite());
        sender.sendMessage("Type /rchelp for a list of all plugin commands.");
        sender.sendMessage("");
        sender.sendMessage(verMsg);
        sender.sendMessage("");

        sender.sendMessage(ChatColor.YELLOW + "Installed chip libraries:");

        for (CircuitIndex i : CircuitLoader.getCircuitLibraries()) {
            sender.sendMessage(ChatColor.YELLOW + " - " + color + i.getIndexName() + " " + i.getVersion());
        }
        sender.sendMessage(ChatColor.RED + "-----------------------------------------------------");
    }

}

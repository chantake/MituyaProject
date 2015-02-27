package com.chantake.MituyaProject.RSC.Command;

import com.chantake.MituyaProject.RSC.RCPrefs;
import com.chantake.MituyaProject.RSC.User.ChipProbe;
import com.chantake.MituyaProject.RSC.User.Tool;
import com.chantake.MituyaProject.RSC.User.UserSession;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Tal Eisenberg
 */
public class RCtool extends RCCommand {
    public static void setToItemInHand(com.chantake.MituyaProject.RSC.RedstoneChips rc, Player player) {
        ItemStack item = player.getItemInHand();
        Material type = item.getType();

        setToType(rc, player, type);
    }

    public static void setToType(com.chantake.MituyaProject.RSC.RedstoneChips rc, Player player, Material type) {
        try {
            UserSession session = rc.getUserSession(player, true);
            Tool t = new ChipProbe();
            t.setItem(type);
            session.addTool(t);
        } catch (IllegalArgumentException ie) {
            player.sendMessage(RCPrefs.getErrorColor() + ie.getMessage());
            return;
        }

        info(player, "Chip probe set to " + ChatColor.YELLOW + type.name().toLowerCase() + ". "
                + RCPrefs.getInfoColor() + "Right-click a chip block to for info.");
    }

    public static void clearTools(com.chantake.MituyaProject.RSC.RedstoneChips rc, Player player) {
        UserSession session = rc.getUserSession(player, true);
        session.getTools().clear();

        player.sendMessage(RCPrefs.getInfoColor() + "Tools cleared.");
    }

    @Override
    public boolean isPlayerRequired() {
        return true;
    }

    @Override
    public void run(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            processArg((Player) sender, args[0]);
        } else setToItemInHand(rc, (Player) sender);
    }

    private void processArg(Player player, String arg) {
        try {
            Material m = RCPrefs.findMaterial(arg).getItemType();
            setToType(rc, player, m);
        } catch (IllegalArgumentException e) {
            if ("clear".startsWith(arg)) clearTools(rc, player);
            else error(player, e.getMessage());
        }
    }
}

/*
 * MituyaProject
 * Copyright (C) 2011-2015 chantake <http://328mss.com/>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.chantake.MituyaProject.Command;

import com.chantake.MituyaProject.Exception.PlayerOfflineException;
import com.chantake.MituyaProject.MituyaProject;
import com.chantake.MituyaProject.Player.PlayerInstance;
import com.chantake.mituyaapi.commands.Command;
import com.chantake.mituyaapi.commands.CommandContext;
import com.chantake.mituyaapi.commands.CommandException;
import org.bukkit.entity.Player;

/**
 *
 * @author chantake
 */
public class InfoCommands {

// <editor-fold defaultstate="collapsed" desc="report">
    @Command(aliases = {"report", "rp"}, usage = "", desc = "report command",
            flags = "", min = 0, max = -1)
    public static void reportBrush(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException, PlayerOfflineException {
        if (message.argsLength() > 0) {
            switch (message.getString(0)) {
                case "report":
                case "rp":
                    break;
                case "lastreport":
                case "lastrp":
                case "ltrp":
                    break;
            }
        } else {
            Help(player, null);
        }
    }
// </editor-fold>

    private static void Help(PlayerInstance ins, String help) throws PlayerOfflineException {
        if (help != null) {

        } else {
            ins.sendAttention("***********Info Command***********");
            ins.sendAttention("/info report レポートを作成します");
        }
    }

    /*     @Command(aliases = {"info"},
     usage = "", desc = "",
     flags = "", min = 1, max = -1)
     //@CommandPermissions({"GM"})
     public static void info(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException {
     if (message.getString(0).equals("player")) {
     players.sendMessage(plugin.getColor().AQUA + "*************************Players Info************************");
     players.sendMessage("Online : " + plugin.getServer().getOnlinePlayers().length);
     players.sendMessage("MaxPlayers : " + plugin.getServer().getMaxPlayers());
     } else if (message.getString(0).equals("players") || message.getString(0).equals("online") || message.getString(0).equals("who")) {
     plugin.getColor();
     plugin.getColor();
     String t = "Online Players(" + ChatColor.RED + plugin.getServer().getOnlinePlayers().length + ChatColor.WHITE + ") ";
     for (final Player needle : plugin.getServer().getOnlinePlayers()) {
     t += needle.getName() + ", ";
     }
     players.sendMessage(t);
     } else if (message.getString(0).equalsIgnoreCase("border")) {
     int border = Parameter328.world_data.get(plugin.getWorld_Id(players.getWorld())).getBorder();
     players.sendMessage("BorderGuard : " + border * 2 + " x " + border * 2);
     } else {
     player.sendAttention("/info [server/player/online]");
     }
     }*/
}

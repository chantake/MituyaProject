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
import com.chantake.MituyaProject.Player.Chat.ChatManager;
import com.chantake.MituyaProject.Player.PlayerInstance;
import com.chantake.MituyaProject.Util.RomaToJapanese;
import com.chantake.mituyaapi.commands.Command;
import com.chantake.mituyaapi.commands.CommandContext;
import com.chantake.mituyaapi.commands.CommandException;
import com.chantake.mituyaapi.commands.CommandPermissions;
import org.bukkit.entity.Player;

/**
 *
 * @author chantake
 */
public class AutomaticConversionCommands {

// <editor-fold defaultstate="collapsed" desc="ac">
    @Command(aliases = {"ac", "ime"}, usage = "", desc = "ac command",
            flags = "", min = 0, max = -1)
    @CommandPermissions({"mituya.command.ime"})
    public static void acBrush(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException, PlayerOfflineException {
        if (message.argsLength() > 0) {
            if (message.getString(0).startsWith("con")) {
                String Conversion = RomaToJapanese.getInstance().Conversion(message.getJoinedStrings(1));
                player.sendMessage(Conversion);
            } else if (message.getString(0).equalsIgnoreCase("on") || message.getString(0).startsWith("t")) {
                player.setIME(true);
                player.sendSuccess("自動変換を有効にしました");
            } else if (message.getString(0).equalsIgnoreCase("off") || message.getString(0).startsWith("f")) {
                player.setIME(false);
                player.sendSuccess("自動変換を無効にしました");
            } else {
                String msg = RomaToJapanese.getInstance().Conversion(message.getJoinedStrings(0));
                ChatManager.ChatProcesser(players, player, msg, plugin);
            }
        } else {
            player.sendAttention("/[ac/ime] con 文字   　変換後の文字列を見れます");
            player.sendAttention("/[ac/ime] [on/true]   自動変換を有効にします");
            player.sendAttention("/[ac/ime] [off/false] 自動変換を無効にします");
        }
    }
// </editor-fold>
}

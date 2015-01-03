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

import com.chantake.MituyaProject.Api.ApiData;
import com.chantake.MituyaProject.Exception.PlayerOfflineException;
import com.chantake.MituyaProject.MituyaProject;
import com.chantake.MituyaProject.Player.PlayerInstance;
import com.chantake.mituyaapi.commands.Command;
import com.chantake.mituyaapi.commands.CommandContext;
import com.chantake.mituyaapi.commands.CommandException;
import com.chantake.mituyaapi.commands.CommandPermissions;
import org.bukkit.entity.Player;
import org.bukkit.potion.Potion;

/**
 *
 * @author chantake
 */
public class ApiCommands {

// <editor-fold defaultstate="collapsed" desc="api">
    @Command(aliases = {"api"}, usage = "", desc = "api command",
            flags = "", min = 0, max = -1)
    @CommandPermissions({"mituya.command.api"})
    public static void apiBrush(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException, PlayerOfflineException {
        if (message.argsLength() > 0) {
            if (message.getString(0).equals("getaccesstoken") || message.getString(0).equals("gat")) {
                ApiData data = plugin.getConnectionManager().getApiManager().CreateApiData(player.getName());
                if (data == null) {
                    player.sendAttention("アクセストークンが取得できませんでした");
                } else {
                    player.sendSuccess("=====AccessToken=====");
                    player.sendSuccess("tokenKey : " + data.getTokenKey() + "");
                    player.sendSuccess("tokenSecret : " + data.getTokenSecret() + "");
                    player.sendSuccess("※必ずメモしてください");
                }
            }
        } else {
            player.sendAttention("/api getaccesstoken getaccesstokenを取得します");
            player.sendAttention("※必要以上に取得しないでください");
        }
    }
// </editor-fold>
    Potion po;
}

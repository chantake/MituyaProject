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
public class ReportCommands {

// <editor-fold defaultstate="collapsed" desc="report">
    @Command(aliases = {"report", "rp"}, usage = "", desc = "report command",
            flags = "", min = 0, max = -1)
    public static void reportBrush(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException {
        if (message.argsLength() > 0) {

        } else {

        }
    }
// </editor-fold>

    private void Help(PlayerInstance ins, String help) throws PlayerOfflineException {
        if (help != null) {

        } else {
            ins.sendAttention("");
        }
    }

}

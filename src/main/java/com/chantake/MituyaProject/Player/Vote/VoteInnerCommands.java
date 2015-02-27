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
package com.chantake.MituyaProject.Player.Vote;

import com.chantake.MituyaProject.Exception.PlayerOfflineException;
import com.chantake.MituyaProject.MituyaProject;
import com.chantake.MituyaProject.Player.PlayerInstance;
import com.chantake.mituyaapi.commands.Command;
import com.chantake.mituyaapi.commands.CommandContext;
import com.chantake.mituyaapi.commands.CommandException;
import com.chantake.mituyaapi.commands.CommandPermissions;
import java.util.HashMap;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

/**
 *
 * @author azutake
 */
public class VoteInnerCommands {
    
    @Command(aliases = {"start"}, usage = "/vote start", desc = "voteを開始する", help = "",
            flags = "v", min = 0, max = -1)
    @CommandPermissions({"mituya.vote.manage.start"})
    public static void startBrush(CommandContext message, final MituyaProject plugin, final Player players, final PlayerInstance player) throws CommandException, PlayerOfflineException {
        //player.hasPermission("mituya.mine.look")
        if(message.argsLength() < 1)
        {
            sendCommandHelp(player);
            return;
        }
        
        int main = -1;
        int sub = -1;
        
        switch(message.getString(0))
        {
            case "weather":
                main = 0;
                switch(message.getString(1))
                {
                    case "sun":
                        sub = 0;
                        break;
                    case "rain":
                        sub = 1;
                        break;
                    case "thunder":
                        sub = 2;
                        break;
                }
                break;
            case "time":
                main = 1;
                switch(message.getString(1))
                {
                    case "morning":
                        sub = 0;
                        break;
                    case "day":
                        sub = 1;
                        break;
                    case "evening":
                        sub = 2;
                        break;
                    case "night":
                        sub = 3;
                        break;
                }
                break;
        }
        
        if(main == -1 || sub == -1)
        {
            sendCommandHelp(player);
            return;
        }
        
        boolean start = VoteManager.Default().startVote(player, main, sub);
        
        if(!start)
            player.sendWarning("現在投票受付中のため開始出来ません。既に開始されている投票が終わるまで今しばらくお待ちください。");
    }
    
    private static void sendCommandHelp(PlayerInstance player)
    {
       player.sendMessage(ChatColor.AQUA + "***********Voteコマンドヘルプ***********");
       player.sendMessage(ChatColor.GREEN + "/vote start weather sun" + ChatColor.WHITE + " 天気を晴れにする投票を開始する");
       player.sendMessage(ChatColor.GREEN + "/vote start weather rain" + ChatColor.WHITE + " 天気を雨にする投票を開始する");
       player.sendMessage(ChatColor.GREEN + "/vote start weather thunder" + ChatColor.WHITE + " 天気を雷にする投票を開始する");
       player.sendMessage(ChatColor.GREEN + "/vote start time morning" + ChatColor.WHITE + " 時間を朝にする投票を開始する");
       player.sendMessage(ChatColor.GREEN + "/vote start time day" + ChatColor.WHITE + " 時間を昼にする投票を開始する");
       player.sendMessage(ChatColor.GREEN + "/vote start time evening" + ChatColor.WHITE + " 時間を夕方にする投票を開始する");
       player.sendMessage(ChatColor.GREEN + "/vote start time night" + ChatColor.WHITE + " 時間を夜にする投票を開始する");
       player.sendMessage(ChatColor.GREEN + "/vote yes" + ChatColor.WHITE + " 投票に賛成として参加");
       player.sendMessage(ChatColor.GREEN + "/vote no" + ChatColor.WHITE + " 投票に反対として参加");
       if(player.hasPermission("mituya.vote.admin.forcestop"))
           player.sendMessage(ChatColor.GREEN + "/vote forcestop" + ChatColor.WHITE + " 投票を強制的に即時終了する");
    }
    
    @Command(aliases = {"stop"}, usage = "", desc = "", help = "",
            flags = "v", min = 0, max = -1)
    @CommandPermissions({"mituya.vote.manage.stop"})
    public static void stopBrush(CommandContext message, final MituyaProject plugin, final Player players, final PlayerInstance player) throws CommandException {
        player.sendWarning("現在未実装です");
    }
    
    @Command(aliases = {"forcestop"}, usage = "", desc = "", help = "",
            flags = "v", min = 0, max = -1)
    @CommandPermissions({"mituya.vote.admin.forcestop"})
    public static void forceStopBrush(CommandContext message, final MituyaProject plugin, final Player players, final PlayerInstance player) throws CommandException {
        player.sendWarning("現在未実装です");
    }
    
    @Command(aliases = {"yes"}, usage = "", desc = "", help = "",
            flags = "v", min = 0, max = -1)
    @CommandPermissions({"mituya.vote.join.yes"})
    public static void yesBrush(CommandContext message, final MituyaProject plugin, final Player players, final PlayerInstance player) throws CommandException {
        byte result = VoteManager.Default().voteYes(player);
        
        sendYesNoWarning(result, player);
    }
    
    @Command(aliases = {"no"}, usage = "", desc = "", help = "",
            flags = "v", min = 0, max = -1)
    @CommandPermissions({"mituya.vote.join.no"})
    public static void noBrush(CommandContext message, final MituyaProject plugin, final Player players, final PlayerInstance player) throws CommandException {
        byte result = VoteManager.Default().voteNo(player);
        
        sendYesNoWarning(result, player);
    }
    
    private static void sendYesNoWarning(byte result, PlayerInstance player)
    {
        switch(result)
        {
            case 1:
                player.sendWarning("既に投票済みです。");
                break;
            case 2:
                player.sendWarning("現在投票は行われていません。");
                break;
        }
    }
}

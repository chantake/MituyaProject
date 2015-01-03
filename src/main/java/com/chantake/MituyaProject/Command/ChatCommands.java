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

import com.chantake.MituyaProject.Exception.MituyaProjectException;
import com.chantake.MituyaProject.Exception.PlayerNotFoundException;
import com.chantake.MituyaProject.Exception.PlayerOfflineException;
import com.chantake.MituyaProject.MituyaProject;
import com.chantake.MituyaProject.Player.Chat.ChatType;
import com.chantake.MituyaProject.Player.PlayerInstance;
import com.chantake.MituyaProject.Tool.RomaToJapanese;
import com.chantake.MituyaProject.Tool.Tools;
import com.chantake.mituyaapi.commands.Command;
import com.chantake.mituyaapi.commands.CommandContext;
import com.chantake.mituyaapi.commands.CommandException;
import com.chantake.mituyaapi.commands.CommandPermissions;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * @author chantake
 */
public class ChatCommands {

// <editor-fold defaultstate="collapsed" desc="chat">
    @Command(aliases = {"chat", "チャット", "ct"}, usage = "", desc = "chat command",
            flags = "", min = 0, max = -1)
    @CommandPermissions({"mituya.command.chat"})
    public static void chatBrush(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException, PlayerNotFoundException, PlayerOfflineException {
        if (message.argsLength() > 0) {
            if (message.getString(0).equals("all") || message.getString(0).equals("全体")) {
                player.setChatType(ChatType.All);
                player.sendSuccess("チャットモード:All に設定しました");
            } else if (message.getString(0).equals("world") || message.getString(0).equals("wd") || message.getString(0).equalsIgnoreCase("ワールド")) {
                player.setChatType(ChatType.World);
                player.sendSuccess("チャットモード:World に設定しました");
            } else if (message.getString(0).equals("range") || message.getString(0).equals("rn") || message.getString(0).equalsIgnoreCase("範囲")) {
                player.setChatType(ChatType.範囲);
                player.sendSuccess("チャットモード:範囲チャット に設定しました");
            } else if (message.getString(0).equals("party") || message.getString(0).equals("pt") || message.getString(0).equalsIgnoreCase("パーティ")) {
                player.setChatType(ChatType.パーティ);
                player.sendSuccess("チャットモード:パーティチャット に設定しました");
            } else if (message.getString(0).equals("gm") || message.getString(0).equalsIgnoreCase("GM")) {
                player.setChatType(ChatType.GM);
                player.sendSuccess("チャットモード:GMチャット に設定しました");
            } else if ((message.getString(0).equals("private") || message.getString(0).equals("pv")) && message.argsLength() > 1) {
                PlayerInstance ins = plugin.getInstanceManager().matchSingleInstance(message.getString(1));
                if (ins == null) {
                    throw new PlayerNotFoundException(message.getString(0));
                }
                player.setPrivateMessagePlayer(ins);
                player.setChatType(ChatType.Private);
                player.sendSuccess("プライベートチャット:" + ins.getName() + " を設定しました");
            } else {
                Help(player);
            }
        } else {
            Help(player);
        }
    }
    // </editor-fold>

    private static void Help(PlayerInstance player) {
        StringBuilder sb = new StringBuilder();
        sb.append("チャットタイプリスト： ");
        for (ChatType ct : ChatType.values()) {
            sb.append(ct.getName()).append(", ");
        }
        player.sendAttention(sb.toString());
        player.sendAttention("現在のチャットタイプ：" + player.getChatType().getName());
    }

// <editor-fold defaultstate="collapsed" desc="all">
    @Command(aliases = {"all"}, usage = "", desc = "chat all aliases",
            flags = "", min = 0, max = -1)
    @CommandPermissions({"mituya.command.chat"})
    public static void allBrush(CommandContext args, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException {
        plugin.handleCommand(players, player, "ct all");
    }
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="Message">
    @Command(aliases = {"tell", "msg", "sasayaki", "wisper"}, usage = "", desc = "Message",
            flags = "", min = 0, max = -1)
    @CommandPermissions({"mituya.command.message"})
    public static void msgBrush(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws MituyaProjectException {
        if (message.argsLength() > 1) {
            PlayerInstance ins = plugin.getInstanceManager().matchSingleInstance(message.getString(0));
            if (ins == null) {
                throw new PlayerNotFoundException(message.getString(0));
            } else if (!ins.getPlayer().isOnline()) {
                throw new PlayerOfflineException(ins.getName());
            } else {
                String text = Tools.ChangeText(message.getJoinedStrings(1), plugin, ins);
                //自動変換
                if (player.getIME()) {
                    text = RomaToJapanese.getInstance().Conversion(text);
                }
                player.setPrivateMessagePlayer(ins);
                ins.setPrivateMessagePlayer(player);
                ins.getPlayer().sendMessage(ChatColor.GREEN + "[From:" + players.getName() + "]" + ChatColor.WHITE + text);
                players.sendMessage(ChatColor.GREEN + "[To:" + ins.getName() + "]" + ChatColor.WHITE + text);
            }
        } else {
            player.sendAttention("/msg プレーヤーID メッセージ");
        }
    }
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="Receve Message">
    @Command(aliases = {"r"}, usage = "", desc = "Receve Message",
            flags = "", min = 0, max = -1)
    @CommandPermissions({"mituya.command.message"})
    public static void recevemsgBrush(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws MituyaProjectException {
        if (message.argsLength() > 0) {
            PlayerInstance ins = player.getPrivateMessagePlayer();
            if (ins == null) {
                throw new PlayerNotFoundException("no receve");
            } else if (!ins.getPlayer().isOnline()) {
                throw new PlayerOfflineException(ins.getName());
            } else {
                String text = Tools.ChangeText(message.getJoinedStrings(0), plugin, ins);
                //自動変換
                if (player.getIME()) {
                    text = RomaToJapanese.getInstance().Conversion(text);
                }
                ins.setPrivateMessagePlayer(player);
                ins.getPlayer().sendMessage(ChatColor.GREEN + "[From:" + players.getName() + "]" + ChatColor.WHITE + text);
                players.sendMessage(ChatColor.GREEN + "[To:" + ins.getName() + "]" + ChatColor.WHITE + text);
            }
        } else {
            player.sendAttention("/r メッセージ");
        }
    }
// </editor-fold>
}

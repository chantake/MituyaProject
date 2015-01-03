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
import com.chantake.MituyaProject.Parameter.Parameter328;
import com.chantake.MituyaProject.Player.PlayerInstance;
import com.chantake.MituyaProject.Tool.Tools;
import com.chantake.mituyaapi.commands.Command;
import com.chantake.mituyaapi.commands.CommandContext;
import com.chantake.mituyaapi.commands.CommandException;
import com.chantake.mituyaapi.commands.CommandPermissions;
import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

/**
 *
 * @author chantake
 */
public class SignCommands {

    private static ChatColor color;

// <editor-fold defaultstate="collapsed" desc="sign">
    @Command(aliases = {"sign", "se"}, usage = "", desc = "sign command",
            flags = "", min = 0, max = -1)
    @CommandPermissions({"mituya.sign"})
    public static void signBrush(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException, PlayerOfflineException {
        if (message.argsLength() >= 1) {
            if (player.isSignTarget()) {
                Block block = player.getSignTarget();
                Sign sign = (Sign)block.getState();
                if (plugin.canBuild(players, block) && !plugin.getChestShopManager().isChestShopData(sign)) {
                    if (message.getString(0).equals("set")) {
                        if (message.argsLength() > 1) {
                            int set = 0;
                            String ss = String.valueOf(message.getString(1).charAt(0));
                            if (Tools.CheckInteger(ss)) {
                                set = Tools.StringToInteger(ss) - 1;
                            }
                            if (set > 3 || set < 0) {
                                player.sendAttention("/se set ?行目 \"テキスト\"  ?には1~4までを指定してください");
                                return;
                            }

                            ArrayList<String> list = Tools.mutchString("\\\".+?\\\"", message.getJoinedStrings(1));
                            for (int i = 0; i < list.size() && set < 4; set++, i++) {//4行
                                Tools.setSignLine(plugin, player, sign, set, list.get(i));
                            }
                            player.sendSuccess("看板に文字を設定しました");
                        } else {
                            player.sendAttention("/se set \"テスト\"");
                        }
                    } else if (message.getString(0).equals("add")) {
                        if (sign.getLines().length > 3) {// 既に4行書かれているかチェック
                            player.sendAttention("これ以上追加できません");
                        } else if (message.argsLength() <= 1) {
                            player.sendAttention("/se add \"テスト\"");
                        } else {
                            final int line = sign.getLines().length;
                            ArrayList<String> list = Tools.mutchString("\\\".+?\\\"", message.getJoinedStrings(1));
                            for (int i = 0; i < message.argsLength() - 1 && line < 4; i++) {
                                Tools.setSignLine(plugin, player, sign, line + i, list.get(i));
                            }
                            player.sendSuccess("看板に追記しました");
                        }
                    } else if (message.getString(0).equals("command") || message.getString(0).equals("cmd") || message.getString(0).equals("comando") || message.getString(0).equalsIgnoreCase("コマンド")) {
                        if (message.getString(1).length() > 1) {
                            sign.setLine(0, Parameter328.Sign_Command);
                            ArrayList<String> list = Tools.mutchString("\\\".+?\\\"", message.getJoinedStrings(1));
                            for (int i = 1; i < message.argsLength() - 1 && i < 4; i++) {
                                if (list.get(i).charAt(0) == '/') {
                                    sign.setLine(i, list.get(i));
                                } else {
                                    sign.setLine(i, "/" + list.get(i));
                                }
                            }
                            sign.update(true);
                            player.sendSuccess("看板コマンドを作成しました");
                        } else {
                            player.sendAttention("/sign command [コマンド]...");
                        }
                    } else {
                        player.sendAttention("/sign [set/add/command] 1gyoume 2gyoume");
                    }
                } else {
                    player.sendAttention("看板にアクセスできる権限がありません");
                }
            }
        } else {
            player.sendAttention("/sign [set/add/command] 1gyoume 2gyoume");
        }
    }
// </editor-fold>
}

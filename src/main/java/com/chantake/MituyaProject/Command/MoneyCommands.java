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
import com.chantake.MituyaProject.MituyaProject;
import com.chantake.MituyaProject.Permissions.Rank;
import com.chantake.MituyaProject.Player.PlayerInstance;
import com.chantake.mituyaapi.commands.Command;
import com.chantake.mituyaapi.commands.CommandContext;
import com.chantake.mituyaapi.commands.CommandPermissions;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * @author chantake
 */
public class MoneyCommands {
//<editor-fold defaultstate="collapsed" desc="money">

    @Command(aliases = {"money", "mine"},
            usage = "", desc = "",
            flags = "", min = 0, max = -1)
    @CommandPermissions({"mituya.player.mine"})
    public static void money(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws MituyaProjectException {
        if (message.argsLength() > 0) {
            if (message.getString(0).equalsIgnoreCase("rank")) {
                player.sendAttention("現在このコマンドは使えません");
                /*
                 player.sendInfo(ChatColor.RED + "Mine", "ランキングを表示します");
                 player.sendMessage(ChatColor.GRAY + "検索中です・・・・(数秒かかる場合がございます)");
                 int h = 0;
                 if (message.argsLength() > 1) {
                 h = Integer.valueOf(message.getString(1)) - 1;
                 }
                 player.Ranking(h);*/
            } else if (message.getString(0).equals("give")) {
                if (message.argsLength() > 2) {
                    final PlayerInstance ins = plugin.getInstanceManager().getInstance(message.getString(1));
                    final long mine = Long.valueOf(message.getString(2));
                    if (ins == null) {
                        player.sendAttention("IDもしくはニックネームを正確に入力してください");
                        throw new PlayerNotFoundException(message.getString(1));
                    } else if (ins.equals(player)) {
                        player.sendAttention("自分自身に送金はできません。");
                        return;
                    }
                    if (mine <= 0) {
                        player.sendAttention("マインは1以上を指定してください。");
                    } else {
                        if (player.gainMine(-mine)) {// 残額がない場合は実行しない
                            ins.gainMine(mine);
                            ins.sendInfo(ChatColor.GREEN + "Mine", " " + player.getName() + " 様から " + mine + " Mine を受け取りました。");
                            player.sendInfo(ChatColor.GREEN + "Mine", ins.getName() + " 様に " + mine + " Mine 送りました。");
                        }
                    }
                } else if (message.argsLength() > 1 && player.hasPermission(Rank.Admin)) {
                    player.gainMine(Long.valueOf(message.getString(1)));
                } else {
                    player.sendAttention("/money give [Playerid money]");
                }
            } else if (message.argsLength() > 1 && player.hasPermission("mituya.mine.look")) {
                final PlayerInstance ins = plugin.getInstanceManager().matchSingleInstance(message.getString(0));// プレーヤー検索
                if (ins == null) {
                    throw new PlayerNotFoundException(message.getString(0));
                }
                final StringBuilder st = new StringBuilder();
                st.append("[").append(ChatColor.GREEN).append("Mine").append(ChatColor.WHITE).append("] Player : ").append(ChatColor.AQUA).append(ins.getName()).append("    ").append(ChatColor.GOLD).append(ins.getMine()).append(ChatColor.GREEN).append(" Mine.");
                players.sendMessage(st.toString());
            } else /*if (Tools.Help(message.getString(0)))*/ {
                players.sendMessage(ChatColor.AQUA + "***********Moneyコマンドヘルプ***********");
                players.sendMessage(ChatColor.GREEN + "[/mine /money]" + ChatColor.WHITE + " 自分の所持金を表示する");
                //players.sendMessage(ChatColor.GREEN + "/mine [プレーヤーID]" + ChatColor.WHITE + " 他の人の所持金を表示する");
                //players.sendMessage(ChatColor.GREEN + "/mine rank {順位}" + ChatColor.WHITE + " マネーランクを表示する");
                players.sendMessage(ChatColor.GREEN + "/mine give [プレーヤーID]" + ChatColor.WHITE + " 他のプレーヤーに送金する");
            }
        } else {
            player.sendInfo(ChatColor.RED + "Mine", "所持Mine : " + ChatColor.GOLD + player.getMine() + ChatColor.GREEN + " Mine");
        }
    }
//</editor-fold>
}

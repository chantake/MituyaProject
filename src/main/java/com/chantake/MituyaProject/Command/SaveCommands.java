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
import com.chantake.MituyaProject.Player.LocationData;
import com.chantake.MituyaProject.Player.PlayerInstance;
import com.chantake.MituyaProject.Tool.Tools;
import com.chantake.mituyaapi.commands.Command;
import com.chantake.mituyaapi.commands.CommandContext;
import com.chantake.mituyaapi.commands.CommandException;
import com.chantake.mituyaapi.commands.CommandPermissions;
import org.bukkit.entity.Player;

/**
 *
 * @author chantake
 */
public class SaveCommands {

    //<editor-fold defaultstate="collapsed" desc="save">
    @Command(aliases = {"save", "set"},
            usage = "", desc = "",
            flags = "", min = 0, max = -1)
    @CommandPermissions({"mituya.player.save"})
    public static void saveBrush(CommandContext message, final MituyaProject plugin, final Player players, final PlayerInstance player) throws CommandException, PlayerOfflineException {
        if (message.argsLength() > 0) {
            if (message.getString(0).equalsIgnoreCase("location")) {
                if (players.getWorld().equals(plugin.getWorldManager().getWorld("harvest"))) {
                    player.sendAttention("採掘ワールドではこのコマンドは使用できません.");
                } else {
                    player.SaveLocation(players.getLocation().clone());
                    player.sendSuccess("ロケーションをセーブしました。");
                }
            } else if (message.getString(0).equalsIgnoreCase("loginmessage") || message.getString(0).equalsIgnoreCase("lm")) {
                if (message.argsLength() > 1) {
                    final String text = Tools.SignColor(message.getJoinedStrings(1));
                    if (Tools.getStringLength(text) > 40) {
                        player.sendAttention("ログインメッセージは40文字以内で設定してください");
                        return;
                    }
                    player.sendYesNo("以下の内容で登録してもよろしいでしょうか？ LM:" + text, new Runnable() {
                        @Override
                        public void run() {
                            player.setLoginBMessage(text);
                            player.sendSuccess("ログインメッセージ設定完了。  " + text);
                        }
                    });
                } else {
                    player.sendAttention("/save loginmessage [LoginMessage]");
                }
            } else if (message.getString(0).equalsIgnoreCase("deathmessage") || message.getString(0).equalsIgnoreCase("dm")) {
                if (message.argsLength() > 1) {
                    final String text = Tools.SignColor(message.getJoinedStrings(1));
                    if (Tools.getStringLength(text) > 40) {
                        player.sendAttention("デスメッセージは40文字以内で設定してください");
                        return;
                    }
                    player.sendYesNo("以下の内容で登録してもよろしいでしょうか？ DM:" + text, new Runnable() {
                        @Override
                        public void run() {
                            player.setDeathMessage(text);
                            player.sendSuccess("デスメッセージ設定完了。  " + text);
                        }
                    });
                } else {
                    player.sendAttention("/save deathmessage [メッセージ]");
                }
            } else if (message.getString(0).equalsIgnoreCase("compuss")) {
                if (message.argsLength() > 1) {
                    if (message.getString(1).equalsIgnoreCase("spawn")) {
                        if (message.argsLength() > 2) {
                            if (message.getString(2).equalsIgnoreCase("default")) {
                                players.setCompassTarget(players.getWorld().getSpawnLocation());
                                player.sendSuccess("Compuss set to spawn default Compulete.");
                            } else {
                                player.sendAttention("/save compuss spawn [default]");
                            }
                        } else {
                            players.setCompassTarget(player.getSpawn(players.getWorld()));
                            player.sendSuccess("コンパスをスポーンに設定しました");
                        }
                    } else if (message.getString(1).equalsIgnoreCase("home")) {
                        int subid = 0;
                        if (message.argsLength() > 2) {
                            subid = Tools.StringToInteger(message.getString(2));
                        }
                        LocationData ld = player.getHome(players.getWorld(), subid);
                        if (ld == null) {
                            player.sendAttention("Not set Home(" + subid + ").");
                            return;
                        }
                        players.setCompassTarget(ld.getLocation());
                        player.sendSuccess("Compuss set to home(" + subid + ") Compulete.");
                    } else {
                        player.sendAttention("/save compuss [spawn/spawn default/home]");
                    }
                } else {
                    player.sendAttention("/save compuss [spawn/spawn default/home]");
                }
            } else {
                player.sendAttention("/save [location/loginmessage]");
            }
        } else {
            player.sendAttention("/save loginmessage [メッセージ]");
            player.sendAttention("/save deathmessage [メッセージ]");
            player.sendAttention("/save compuss [spawn/spawn default/home]");
            player.sendAttention("/save [location/loginmessage]");
        }
    }
//</editor-fold>
}

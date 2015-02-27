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
import com.chantake.MituyaProject.Util.Tools;
import com.chantake.mituyaapi.commands.Command;
import com.chantake.mituyaapi.commands.CommandContext;
import com.chantake.mituyaapi.commands.CommandException;
import com.chantake.mituyaapi.commands.CommandPermissions;
import org.bukkit.entity.Player;

/**
 *
 * @author chantake
 */
public class NickNameCommands {

// <editor-fold defaultstate="collapsed" desc="nickname">
    @Command(aliases = {"nick", "nk", "nickname"}, usage = "", desc = "ニックネームコマンド",
            flags = "", min = 0, max = -1)
    @CommandPermissions("mituya.player.nickname")
    public static void nickBrush(CommandContext message, MituyaProject plugin, final Player players, final PlayerInstance player) throws CommandException, PlayerOfflineException {
        if (message.argsLength() > 0) {
            if (message.getString(0).equalsIgnoreCase("set") || message.getString(0).equalsIgnoreCase("save")) {
                if (message.argsLength() == 1) {
                    player.sendAttention("/nick set [ニックネーム]  ニックネームを設定します");
                    player.sendAttention("※ニックネームは大文字8文字、小文字16文字以内で設定してください");
                    player.sendAttention("※カラーコードは使えません");
                    return;
                }
                final String nick = message.getJoinedStrings(1);
                if (nick.length() < 2 || Tools.getStringLength(nick) == 0 || Tools.getStringLength(nick) > 16) {
                    player.sendAttention("ニックネームは2文字以上大文字8文字、小文字16文字以内で設定してください");
                } else if (nick.contains(" ") || nick.contains("　")) {
                    player.sendAttention("空白などの禁止文字が含まれています");
                } else {
                    player.sendYesNo("以下の内容でニックネームを登録してもよろしいでしょうか？ ：" + nick, new Runnable() {
                        @Override
                        public void run() {
                            if (player.setNickName(nick)) {
                                if (player.gainMine(-3000)) {
                                    player.sendSuccess("ニックネームを設定しました：" + nick);
                                }
                            } else {
                                player.sendAttention("既にニックネームもしくはIDが存在しています");
                            }
                        }
                    });
                    player.sendAttention("※3000Mine登録料としてかかります");
                }
            } else if (message.getString(0).equalsIgnoreCase("remove") || message.getString(0).equalsIgnoreCase("clear") || message.getString(0).equalsIgnoreCase("削除")) {
                player.sendYesNo("ニックネームを削除します　よろしいでしょうか？", new Runnable() {
                    @Override
                    public void run() {
                        try {
                            player.removeNickName();
                            player.sendSuccess("ニックネームを削除しました");
                        }
                        catch (PlayerOfflineException ex) {
                        }
                    }
                });
            }
        } else {
            player.sendAttention("/nick set [ニックネーム]  ニックネームを設定します");
            player.sendAttention("/nick remove             ニックネームを削除します");
            player.sendAttention("※ニックネームは大文字8文字、小文字16文字以内で設定してください");
            player.sendAttention("※カラーコードは使えません");
        }
    }
// </editor-fold>
}

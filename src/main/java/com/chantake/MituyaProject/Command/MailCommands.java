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
import com.chantake.MituyaProject.Player.Mail.MailData;
import com.chantake.MituyaProject.Player.PlayerInstance;
import com.chantake.MituyaProject.Util.Tools;
import com.chantake.mituyaapi.commands.Command;
import com.chantake.mituyaapi.commands.CommandContext;
import com.chantake.mituyaapi.commands.CommandPermissions;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author chantake
 */
public class MailCommands {

// <editor-fold defaultstate="collapsed" desc="mail">
    @Command(aliases = {"mail", "メール", "ml"}, usage = "", desc = "メールコマンド",
            flags = "n", min = 0, max = -1)
    @CommandPermissions("mituya.command.mail")
    public static void mailBrush(final CommandContext message, final MituyaProject plugin, final Player players, final PlayerInstance player) throws MituyaProjectException {
        if (message.argsLength() > 0) {
            if (message.getString(0).startsWith("未読メール") || message.getString(0).equalsIgnoreCase("unread")) {
                int unreadMailAmount = plugin.getMailManager().getUnreadMailAmount(players);
                if (unreadMailAmount == 0) {
                    player.sendInfo(ChatColor.GREEN + "Mail", ChatColor.AQUA + "未読メールはありません");
                } else {
                    int page_max = (unreadMailAmount / 10) + 1;
                    int page = 1;
                    if (message.argsLength() > 1) {
                        page = message.getInteger(1);
                        if (page > page_max) {
                            player.sendAttention("ページは 1~" + page_max + " を指定してください");
                            return;
                        }
                    }
                    if (page_max == 0) {
                        page_max = 1;
                    }
                    player.sendInfo(ChatColor.GREEN + "Mail", "メール取得中・・・");
                    Iterator<MailData> unreadMail = plugin.getMailManager().getUnreadMail(players, (page - 1) * 10);
                    if (unreadMail == null) {
                        player.sendAttention("未読メッセージはありません");
                        return;
                    }
                    player.sendMessage(ChatColor.GREEN + "============未読メッセージ ページ(" + page + "/" + page_max + ")============");
                    player.sendMessage(ChatColor.GREEN + " ID | 日時 | From | 件名 (/mail read ID でメール読めます)");
                    while (unreadMail.hasNext()) {
                        MailData md = unreadMail.next();
                        StringBuilder sb = new StringBuilder();
                        sb.append(ChatColor.RED).append(" ").append(md.getId());
                        if (md.getUnread()) {
                            sb.append("未読 ");
                        }
                        sb.append(ChatColor.WHITE).append(" | ").append(md.getTimestamp().toString()).append(" | ").append(ChatColor.AQUA).append(md.getFrom()).append(ChatColor.WHITE).append(" | ").append(md.getSubject());
                        player.sendMessage(sb.toString());
                    }
                }
            } else if (message.getString(0).equals("作成") || message.getString(0).equalsIgnoreCase("send") || message.getString(0).equals("create")) {
                ArrayList<String> list = Tools.mutchString("\\\".+?\\\"", message.getJoinedStrings(1));
                if (message.argsLength() > 1 && list.size() >= 2) {
                    PlayerInstance instance = plugin.getInstanceManager().matchSingleInstance(message.getString(1));
                    if (instance == null) {
                        throw new PlayerNotFoundException(message.getString(1));
                    }
                    Player pr = plugin.getServer().getPlayer(instance.getName());
                    if (list.get(0).length() > 30) {
                        player.sendAttention("件名は30文字以内にしてください");
                        return;
                    }
                    if (list.get(1).length() > 255) {
                        player.sendAttention("本文は255文字以内にしてください");
                        return;
                    }
                    if (pr == null) {
                        plugin.getMailManager().sendMail(players, plugin.getServer().getOfflinePlayer(instance.getName()), list.get(0), list.get(1));
                    } else {
                        plugin.getMailManager().sendMail(players, pr, list.get(0), list.get(1));
                        instance.sendInfo(ChatColor.GREEN + "Mail", ChatColor.YELLOW + player.getName() + " 様からメールを受信しました。(/mail read -n で読むことができます)");
                    }
                    player.sendInfo(ChatColor.GREEN + "Mail", ChatColor.YELLOW + "メールを送りました" + ChatColor.WHITE + " To:" + ChatColor.AQUA + instance.getName() + ChatColor.WHITE + " 件名:" + list.get(0) + " 本文:" + list.get(1));
                } else {
                    player.sendAttention("/mail [作成/send/create] 宛先 \"件名\" \"本文\"  メールを送ります");
                    player.sendAttention("※宛名と本文は空白でもいいので必ず入力してください");
                    player.sendAttention("※本文は255文字以内です");
                }
            } else if (message.getString(0).equals("読む") || message.getString(0).equalsIgnoreCase("read")) {
                if (message.hasFlag('n')) {
                    player.sendInfo(ChatColor.GREEN + "Mail", "メール取得中・・・");
                    MailData newMail = plugin.getMailManager().getNewMail(players);
                    if (newMail == null) {
                        player.sendAttention("新着メッセージはありません");
                        return;
                    }
                    player.sendMessage(ChatColor.GREEN + "============新着メッセージ============");
                    player.sendMessage("ID:" + newMail.getId() + " From:" + newMail.getFrom() + " 件名:" + newMail.getSubject());
                    player.sendMessage("本文:" + newMail.getText());
                    if (newMail.getUnread()) {
                        try {
                            plugin.getInstanceManager().matchSingleInstance(newMail.getFrom()).sendInfo(ChatColor.GREEN + "Mail", player.getName() + " 様がメールを読みました");
                        }
                        catch (NullPointerException e) {
                        }
                        newMail.setUnread(false);
                        plugin.getMailManager().saveMail(newMail);
                    }
                } else if (message.argsLength() > 1) {
                    MailData newMail = plugin.getMailManager().getMailFromID(players, message.getInteger(1));
                    if (newMail == null) {
                        player.sendAttention("ID:" + message.getInteger(1) + " は存在しないか、読む権限がありません");
                        return;
                    }
                    player.sendMessage(ChatColor.GREEN + "============メッセージ============");
                    player.sendMessage("ID:" + newMail.getId() + " From:" + newMail.getFrom() + " 件名:" + newMail.getSubject());
                    player.sendMessage("本文:" + newMail.getText());
                    if (newMail.getUnread()) {
                        try {
                            plugin.getInstanceManager().matchSingleInstance(newMail.getFrom()).sendInfo(ChatColor.GREEN + "Mail", player.getName() + " 様がメールを読みました");
                        }
                        catch (NullPointerException e) {
                        }
                        newMail.setUnread(false);
                        plugin.getMailManager().saveMail(newMail);
                    }
                } else {
                    player.sendAttention("/mail read -n  新着メッセージを読む");
                    player.sendAttention("/mail read ID  IDのメールを読む");
                }
            } else if (message.getString(0).equals("list")) {
                int MailAmount = plugin.getMailManager().getMailAmount(players);
                if (MailAmount == 0) {
                    player.sendInfo(ChatColor.GREEN + "Mail", ChatColor.AQUA + "メールはありません");
                } else {
                    int page_max = (MailAmount / 10) + 1;
                    int page = 1;
                    if (message.argsLength() > 1) {
                        page = message.getInteger(1);
                        if (page > page_max) {
                            player.sendAttention("ページは 1~" + page_max + " を指定してください");
                            return;
                        }
                    }
                    player.sendInfo(ChatColor.GREEN + "Mail", "メール取得中・・・");
                    Iterator<MailData> mail = plugin.getMailManager().getMail(players, (page - 1) * 10);
                    if (mail == null) {
                        player.sendAttention("メッセージはありません");
                        return;
                    }
                    player.sendMessage(ChatColor.GREEN + "============メッセージ ページ(" + page + "/" + page_max + ")============");
                    player.sendMessage(ChatColor.GREEN + " ID | 日時 | From | 件名 (/mail read ID でメール読めます)");
                    while (mail.hasNext()) {
                        MailData md = mail.next();
                        StringBuilder sb = new StringBuilder();
                        sb.append(ChatColor.RED).append(" ").append(md.getId());
                        if (md.getUnread()) {
                            sb.append("未読 ");
                        }
                        sb.append(ChatColor.WHITE).append(" | ").append(md.getTimestamp().toString()).append(" | ").append(ChatColor.AQUA).append(md.getFrom()).append(ChatColor.WHITE).append(" | ").append(md.getSubject());
                        player.sendMessage(sb.toString());
                    }
                }
            } else if (message.getString(0).equals("remove") || message.getString(0).equals("削除")) {
                if (message.argsLength() > 1) {
                    player.sendYesNo("メールを削除しますがよろしいでしょうか？", new Runnable() {
                        @Override
                        public void run() {
                            MailData md = plugin.getMailManager().getMailFromID(players, message.getInteger(1));
                            if (md == null) {
                                player.sendAttention("ID:" + message.getInteger(1) + " は存在しないか、読む権限がありません");
                                return;
                            }
                            plugin.getMailManager().removeMail(md.getId());
                            player.sendInfo(ChatColor.GREEN + "Mail", ChatColor.YELLOW + "メールを削除しました");
                        }
                    });
                } else {
                    player.sendAttention("/mail [remove/削除] ID　　メールを削除します");
                }
            }
        } else {
            player.sendAttention("/[mail/メール/ml]");
            player.sendAttention("/mail [remove/削除] ID    メールを削除します");
            player.sendAttention("/mail list               メールを表示します");
            player.sendAttention("/mail read -n            新着メッセージを読む");
            player.sendAttention("/mail read ID            IDのメールを読む");
            player.sendAttention("/mail [unread/未読メール] 未読メールを読む");
            player.sendAttention("/mail [作成/send/create] 宛先 \"件名\" \"本文\"  メールを送ります");
            player.sendAttention("※宛名と本文は空白でもいいので必ず入力してください");
            player.sendAttention("※本文は255文字以内です");
        }
    }
// </editor-fold>
}

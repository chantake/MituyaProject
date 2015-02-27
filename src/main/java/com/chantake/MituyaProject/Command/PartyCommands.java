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
import com.chantake.MituyaProject.Permissions.Rank;
import com.chantake.MituyaProject.Player.Party.PartyData;
import com.chantake.MituyaProject.Player.Party.PartyManager;
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
public class PartyCommands {

// <editor-fold defaultstate="collapsed" desc="party">
    @Command(aliases = {"party", "pt"}, usage = "", desc = "party command",
            flags = "", min = 0, max = -1)
    @CommandPermissions("mituya.player.party")
    public static void partyBrush(CommandContext message, final MituyaProject plugin, final Player players, final PlayerInstance player) throws MituyaProjectException {
        if (message.argsLength() > 0) {
            if (player.getParty() != -1) {
                final PartyData pd = PartyManager.getParty(player.getParty());
                if (message.getString(0).equals("info")) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("[").append(ChatColor.GREEN).append("Party").append(ChatColor.WHITE).append("]");
                    sb.append(" リーダー:").append(pd.getPartyReader().getName()).append(" メンバー:");

                    //リーダー以外
                    pd.getPartyMembers().stream().filter(ins -> !ins.equals(player)).forEach(ins -> {//リーダー以外
                        sb.append(ins.getName()).append(" ");
                    });
                } else if (message.getString(0).equals("addmember") || message.getString(0).equals("am")) {
                    if (message.argsLength() > 1) {
                        if (pd.getPartyReader().equals(player)) {//パーティリーダーだった場合
                            final PlayerInstance ins = plugin.getInstanceManager().matchSingleInstance(message.getString(1));
                            if (ins == null) {
                                throw new PlayerNotFoundException(message.getString(1));
                            } else {
                                ins.sendYesNoFromPlayer("パーティーの誘いがきています。参加しますか？", player, () -> {
                                    try {
                                        pd.addMember(ins);
                                        player.sendSuccess("プレーヤー " + ins.getName() + " をパーティに追加しました。");
                                        pd.sendPartyMessage(ins.getName() + " がパーティに追加されました");
                                    } catch (PlayerOfflineException ex) {
                                    }
                                });
                            }
                        } else {//パーティリーダー
                            player.sendAttention("パーティリーダー以外追加できません");
                        }
                    } else {
                        player.sendAttention("/pt addmember プレーヤーID");
                    }
                } else /**
                 * * 退席コマンド **
                 */
                if (message.getString(0).equals("leave") || message.getString(0).equals("退席") || message.getString(0).equals("ぇあヴぇ") || message.getString(0).equals("taiseki")) {
                    player.sendYesNo("パーティーから退席します。よろしいですか？", () -> {
                        pd.removeMember(player);//プレーヤーをパーティーから離脱させる
                        player.sendAttention("パーティーから退席しました。");
                        for (PlayerInstance ins : pd.getPartyMembers())//パーティーメンバーの全員に報告
                        {
                            try {
                                if (ins.getPlayer() != null && ins.getPlayer().isOnline()) {
                                    ins.sendAttention(player.getName() + "さんがパーティーから退席しました。");
                                }
                            } catch (PlayerOfflineException ex) {
                            }
                        }
                    });
                } else if (message.getString(0).equals("tphere") || message.getString(0).startsWith("here")) {
                    if (player.hasPermission(Rank.Supporter) && !plugin.getWorldManager().getWorldData(players.getLocation().getWorld()).isCommand()) {
                        if (pd.getPartyReader() == player) {
                            for (final PlayerInstance ins : pd.getPartyMembers()) {
                                if (player != ins) {
                                    Player pr = ins.getPlayer();
                                    if (pr != null && pr.isOnline()) {
                                        ins.sendYesNoFromPlayer("パーティーリーダーが呼びました。飛びますか？", player, () -> {
                                            try {
                                                ins.sendInfo(ChatColor.GREEN + "Party", ChatColor.LIGHT_PURPLE + "パーティリーダーのところに移動しています・・・");
                                                ins.teleport(players.getLocation());
                                            } catch (PlayerOfflineException ex) {
                                            }
                                        });
                                    }
                                }
                            }
                        } else {
                            player.sendAttention("パーティリーダーのみ使えます");
                        }
                    } else {
                        player.sendAttention("このコマンドを使う権限がないか、このワールドでは許可されていません");
                    }
                } else if (message.getString(0).equals("rp") || message.getString(0).startsWith("remove") || message.getString(0).equals("削除")) {
                    player.sendInfo(ChatColor.GREEN + "Party", ChatColor.YELLOW + "パーティを解散しました");
                    pd.getPartyMembers().stream().filter(ins -> player != ins).forEach(ins -> ins.sendInfo(ChatColor.GREEN + "Party", ChatColor.YELLOW + "パーティが解散しました"));
                    PartyManager.removeParty(pd.getId());
                }
            } else if (message.getString(0).equals("cp") || message.getString(0).startsWith("create") || message.getString(0).equals("作成")) {
                if (PartyManager.CreateParty(player)) {
                    player.sendAttention("パーティを作成しました /pt addmember プレーヤーID でパーティにメンバーを追加できます。");
                } else {
                    player.sendAttention("パーティを作成できませんでした。");
                }
            } else {
                player.sendAttention("パーティに参加していません。作成するか参加してください。");
            }
        } else {
            player.sendAttention("----------------パーティーリーダーコマンド----------------");
            player.sendAttention("/party createparty            パーティを作成します");
            player.sendAttention("/party addmember PlayerID     パーティにメンバーを追加します");
            player.sendAttention("/party changereader PlayerID  パーティリーダーを変更します");
            player.sendAttention("/party tphere                 パーティメンバー全員を呼びます");
            player.sendAttention("----------------パーティーメンバーコマンド----------------");
            player.sendAttention("/party leave                  パーティーから退席します。");
        }
    }
    // </editor-fold>
}

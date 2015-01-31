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
package com.chantake.MituyaProject.Player.Chat;

import com.chantake.MituyaProject.Bukkit.Event.MituyaPlayerChatEvent;
import com.chantake.MituyaProject.Exception.PlayerOfflineException;
import com.chantake.MituyaProject.MituyaProject;
import com.chantake.MituyaProject.Parameter.Parameter328;
import com.chantake.MituyaProject.Player.Party.PartyData;
import com.chantake.MituyaProject.Player.Party.PartyManager;
import com.chantake.MituyaProject.Player.PlayerInstance;
import com.chantake.MituyaProject.Tool.RomaToJapanese;
import com.chantake.MituyaProject.Tool.Tools;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * チャットを管理するクラス
 *
 * @author chantake
 */
public class ChatManager {

    /**
     * チャットを処理します
     *
     * @param player Player
     * @param message String
     * @param plugin MituyaProject
     */
    public static void ChatProcesser(Player player, String message, MituyaProject plugin) {
        ChatProcesser(player, plugin.getInstanceManager().getInstance(player), message, plugin);
    }

    /**
     * チャットを処理します
     *
     * @param player Player
     * @param ins PlayerInstance
     * @param chatmessage String
     * @param plugin MituyaProject
     */
    //static Pattern pattern = Pattern.compile("[^\u0020-^\u00a5-\u007E]|\u00a7|u00a74u00a75u00a73u00a74v");//WorldeditCUIが入ってた場合送信する文字列をPatternに登録する。
    public static void ChatProcesser(Player player, PlayerInstance ins, String chatmessage, MituyaProject plugin) {
        try {
            StringBuilder sb;
            String message = Tools.ChangeText(chatmessage, plugin, ins);
            //自動変換
            if (ins.getIME()) {
                message = RomaToJapanese.getInstance().Conversion(message) + ChatColor.WHITE + "(" + ChatColor.DARK_GRAY + message + ChatColor.WHITE + ")";
            }
            //先頭文字
            if (message.length() > 0 && message.charAt(0) == Parameter328.command) {
                message = message.substring(1);// 先頭を削除
            }
            //call MituyaPlayerChatEvent
            MituyaPlayerChatEvent mituyaPlayerChatEvent = new MituyaPlayerChatEvent(player, ins, message, ins.getChatType());
            plugin.getServer().getPluginManager().callEvent(mituyaPlayerChatEvent);

            //  Matcher matcher = pattern.matcher(message);//ログイン時に u00a74u00a75u00a73u00a74vl1が出なくする
            //チャットキャンセル
            if (mituyaPlayerChatEvent.isCancelled()) {//||matcher.find(0)) {
                return;
            }

            //チャットごとの処理
            switch (ins.getChatType()) {
                case All:
                    sb = ChatHead(player, ChatColor.RED);
                    sb.append(message);
                    plugin.broadcastMessage(ChatColor.RED + "[全体]" + sb.toString());
                    break;
                case Private:
                    if (ins.getPrivateMessagePlayer() == null) {
                        ins.sendAttention("/msg プレーヤーID  で相手を指定してください");
                    } else {
                        PlayerInstance re = ins.getPrivateMessagePlayer();
                        sb = new StringBuilder();
                        sb.append(ChatColor.GREEN).append("[From:").append(player.getName()).append("]").append(ChatColor.WHITE);
                        sb.append(message);
                        re.getPlayer().sendMessage(sb.toString());
                        re.setPrivateMessagePlayer(ins);
                        sb.setLength(0);
                        sb.append(ChatColor.GREEN).append("[To:").append(re.getName()).append("]").append(ChatColor.WHITE);
                        sb.append(message);
                        player.sendMessage(sb.toString());
                    }
                    break;
                case World:
                    sb = ChatHead(player, ChatColor.YELLOW);
                    sb.append(message);
                    plugin.broadcastWorldMessage(player.getWorld(), ChatColor.YELLOW + "[ワールド]" + sb.toString());
                    break;
                case パーティ:
                    PartyData pd = PartyManager.getParty(ins.getParty());
                    pd.sendPartyMember(player, message);
                    break;
                case 範囲:
                    sb = ChatHead(player, null);
                    sb.append(message);
                    for (Player pr : plugin.getRangePlayers(player.getLocation(), Parameter328.ChatRange)) {
                        pr.sendMessage("[範囲]" + sb.toString());
                    }
                    break;
                case GM:
                    sb = ChatHead(player, null);
                    sb.append(message);
                    plugin.broadcastGMMessage(ChatColor.AQUA + "[GM]" + sb.toString());
                    break;
            }
        } catch (IllegalStateException | PlayerOfflineException ex) {
            plugin.ErrLog("ChatProcesser err:" + ex);
        }
    }

    /**
     * チャットの先頭部分
     *
     * @param player Player
     * @param head String
     * @param headcolor ChatColor
     * @return StringBuilder
     */
    private static StringBuilder ChatHead(Player player/*, String head*/, ChatColor headcolor) {
        StringBuilder sb = new StringBuilder();
        /*if (head != null) {
         sb.append(headcolor).append("[").append(head).append("]");
         }*/
        sb.append(ChatColor.WHITE).append("[").append(player.getDisplayName()).append("]").append(ChatColor.WHITE).append(" ");
        return sb;
    }
}

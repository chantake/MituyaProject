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
package com.chantake.MituyaProject.Player.Party;

import com.chantake.MituyaProject.Exception.PlayerOfflineException;
import com.chantake.MituyaProject.Parameter.Parameter328;
import com.chantake.MituyaProject.Player.PlayerInstance;
import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * パーティ用のデータクラス
 *
 * @author chantake-mac
 */
public class PartyData {

    private final int id;
    private PlayerInstance party_reader;
    private final ArrayList<PlayerInstance> member = new ArrayList<>();

    public PartyData(PlayerInstance party_reader, int id) {
        this.party_reader = party_reader;
        this.id = id;
        this.member.add(party_reader);
    }

    /**
     * メンバーを追加します
     *
     * @param addmember PlayerInstance
     * @return 追加に成功した場合はtrueを返します　6人を超えてたりして追加できない場合はfalseを返します
     */
    public boolean addMember(PlayerInstance addmember) {
        //パーティメンバーが6人未満かつ参加していない場合
        if (member.size() < Parameter328.MAX_Party_Member && addmember.getParty() == -1) {
            member.add(addmember);
            addmember.setParty(id);
            return true;
        }
        return false;
    }

    /**
     * パーティメンバーを削除します
     *
     * @param removemember PlayerInstance
     */
    public void removeMember(PlayerInstance removemember) {
        member.remove(removemember);
        removemember.setParty(-1);
    }

    /**
     * パーティリーダーを取得します
     *
     * @return PlayerInstance
     */
    public PlayerInstance getPartyReader() {
        return this.party_reader;
    }

    /**
     * パーティメンバーを取得します
     *
     * @return ArrayList<PlayerInstance>
     */
    public ArrayList<PlayerInstance> getPartyMembers() {
        return member;
    }

    /**
     * リーダーを変更します
     *
     * @param newreader PlayerInstance
     * @return 変更に成功した場合はtrueを返します
     */
    public boolean ChangeReader(PlayerInstance newreader) {
        if (member.contains(newreader)) {
            this.party_reader = newreader;
        }
        return false;
    }

    /**
     * パーティメンバーの数を返します
     *
     * @return
     */
    public int getPartyMember() {
        return this.member.size();
    }

    /**
     * パーティメンバー全員にメッセージを送ります
     *
     * @param sender 発言者
     * @param message メッセージ
     * @throws com.chantake.MituyaProject.Exception.PlayerOfflineException
     */
    public void sendPartyMember(Player sender, String message) throws PlayerOfflineException {
        StringBuilder sb = new StringBuilder();
        sb.append(ChatColor.GREEN).append("[パーティ]").append(ChatColor.WHITE).append("[").append(sender.getDisplayName()).append("]").append(ChatColor.WHITE).append(" ").append(message);
        String msg = sb.toString();
        for (PlayerInstance ins : this.member) {
            ins.sendMessage(msg);
        }
    }

    /**
     * パーティメンバーにパーティinfoメッセージを表示します
     *
     * @param message
     * @throws com.chantake.MituyaProject.Exception.PlayerOfflineException
     */
    public void sendPartyMessage(String message) throws PlayerOfflineException {
        for (PlayerInstance ins : this.member) {
            ins.sendInfo(ChatColor.GREEN + "Paery", message);
        }
    }

    /**
     * パーティidを返します
     *
     * @return
     */
    public int getId() {
        return id;
    }

}

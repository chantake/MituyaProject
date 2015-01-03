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

import com.chantake.MituyaProject.Player.PlayerInstance;
import java.util.Map;
import java.util.TreeMap;

/**
 * パーティーマネージャー
 *
 * @author chantake-mac
 */
public class PartyManager {

    private static final Map<Integer, PartyData> party = new TreeMap<>();

    /**
     * パーティを取得します
     *
     * @param id reader
     * @return ArrayList
     */
    public static PartyData getParty(int id) {
        if (party.containsKey(id)) {
            return party.get(id);
        } else {
            return null;
        }
    }

    /**
     * パーティを削除します
     *
     * @param id
     */
    public static void removeParty(int id) {
        PartyData pd = party.get(id);
        for (PlayerInstance ins : pd.getPartyMembers()) {
            ins.setParty(-1);
        }
        party.remove(id);
    }

    /**
     * パーティを作成します
     *
     * @param reader PlayerInstance
     * @return 作成できた場合はtrue
     */
    public static boolean CreateParty(PlayerInstance reader) {
        //パーティに参加してない場合
        if (reader.getParty() == -1) {
            //パーティID
            int id = party.size();
            //パーティを作成
            party.put(id, new PartyData(reader, id));
            //PlayerInstanceにパーティIDを登録
            reader.setParty(id);
            return true;
        } else {
            //参加してるので
            return false;
        }
    }
}

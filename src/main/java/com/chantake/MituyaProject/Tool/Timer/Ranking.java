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
package com.chantake.MituyaProject.Tool.Timer;

import com.chantake.MituyaProject.MituyaProject;

/**
 *
 * @author chantake-mac
 */
public class Ranking implements Runnable {

    public MituyaProject plugin;

    public Ranking(MituyaProject mp) {
        plugin = mp;
    }

    @Override
    public void run() {
        /*int rk = 0;
         int i = 10;
         for (String name : plugin.getInstanceManager().getPlayers().keySet()) {
         PlayerInstance ins = plugin.getInstanceManager().getInstance(name);
         if (ins.getRank() == RankOld.VIP) {
         ins.setRank(ins.getOldRank());
         ins.setDisplayName();
         }
         }
         try {
         Connection con = DatabaseConnection.getConnection();
         //PreparedStatement ps = con.prepareStatement("SELECT * FROM ibalances WHERE rank < 3 ORDER BY `money` DESC LIMIT ?,?");
         PreparedStatement ps = con.prepareStatement("SELECT * FROM ibalances ORDER BY `balance` DESC LIMIT " + rk + "," + i);
         ResultSet rs = ps.executeQuery();
         while (rs.next()) {
         PlayerInstance ins = plugin.getInstanceManager().getInstance(rs.getString("player"));
         if (ins != null && ins.getRank() <= RankOld.VIP) {
         ins.setOldRank();
         ins.setRank(RankOld.VIP);
         ins.setDisplayName();
         ins.SaveAll();
         }
         }
         rs.close();
         ps.close();
         } catch (SQLException e) {
         //this.sendAttention("It failed in the call of the ranking. ");
         }*/
        plugin.Log("ランキングを更新しました");
    }
}

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
package com.chantake.MituyaProject.Timer;

import com.chantake.MituyaProject.MituyaProject;
import com.chantake.MituyaProject.Util.MySqlProcessing;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * オートセーブ
 *
 * @author chantake
 */
public class AutoSave implements Runnable {

    public MituyaProject plugin;

    /**
     * インスタンス作成
     *
     * @param mp MituyaProject
     */
    public AutoSave(MituyaProject mp) {
        plugin = mp;
    }

    @Override
    public void run() {
        plugin.Log("AutoSaveStart...");
        plugin.broadcastMessage(ChatColor.LIGHT_PURPLE + "[Server] オートセーブなう...");
        plugin.SaveAll();
        MySqlProcessing.SaveServer();
        /*
         * オートヒール
         */
        for (final Player pr : plugin.getServer().getOnlinePlayers()) {
            //死んでる人以外
            if (pr.getHealth() > 0) {
                if (pr.getHealth() > pr.getMaxHealth() - 2) {
                    pr.setHealth(pr.getMaxHealth());
                } else {
                    pr.setHealth(pr.getHealth() + 2);
                }
            }
        }
        plugin.broadcastMessage(ChatColor.LIGHT_PURPLE + "[Server] **オートセーブ完了**");
        plugin.Log("AutoSaveComplete.");
    }
}

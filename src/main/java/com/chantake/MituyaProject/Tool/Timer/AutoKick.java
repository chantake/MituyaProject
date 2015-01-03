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

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import com.chantake.MituyaProject.MituyaProject;
import com.chantake.MituyaProject.Permissions.Rank;

/**
 * タイマーにより、自動的に放置者をキックする関数
 *
 * @author chantake
 * @version 1.0.0
 */
public class AutoKick implements Runnable {

    public MituyaProject plugin;
    public static Map<Player, Integer> neglect = new HashMap<>();

    public AutoKick(MituyaProject mp) {
        plugin = mp;
    }

    @Override
    public void run() {
        for (final Player pr : plugin.getServer().getOnlinePlayers()) {
            if (AutoKick.neglect.get(pr) == 0) {
                if (plugin.getInstanceManager().getInstance(pr).hasPermission(Rank.Supporter)) {//Supporter未満のみ
                    pr.kickPlayer("放置していたので落とされました。");
                }
            } else {
                AutoKick.neglect.put(pr, 0);
            }
        }
    }
}

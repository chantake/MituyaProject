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
package com.chantake.MituyaProject.Util;

import com.chantake.MituyaProject.MituyaProject;
import com.chantake.MituyaProject.Timer.PerformanceMonitorThread;
import org.bukkit.ChatColor;

/**
 *
 * @author chantake
 */
public class PerformanceMonitor {

    private final MituyaProject plugin;
    public int monitorInterval = 40;
    public double tps = 0;
    public double elapsedtimesec;
    public long elapsedtime;
    public long prevtime;
    private int id;

    public PerformanceMonitor(MituyaProject plugin) {
        this.plugin = plugin;
    }

    public void init() {
        plugin.Log("PerformanceMonitor 初期化");
        prevtime = System.currentTimeMillis();
        id = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new PerformanceMonitorThread(this), monitorInterval, monitorInterval);
        plugin.Log("PerformanceMonitor 初期化完了");
    }

    public String getTPS(boolean player) {
        StringBuilder builder = new StringBuilder();
        builder.append(ChatColor.YELLOW).append("Ticks per second: ");
        if (player) {
            if (tps >= 17 && tps <= 23) {
                builder.append(ChatColor.GREEN);
            } else if (tps >= 14 && tps <= 26) {
                builder.append(ChatColor.GOLD);
            } else {
                builder.append(ChatColor.RED);
            }
        }
        builder.append(Util.round(tps, 1)).append(" [");
        builder.append(Util.round(tps * 5, 0));
        builder.append("%]");
        return builder.toString();
    }

    public double getTPS() {
        return tps;
    }

    public MituyaProject getPlugin() {
        return plugin;
    }

}

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
import com.chantake.MituyaProject.Parameter.Parameter328;
import com.chantake.MituyaProject.Player.PlayerInstance;
import com.chantake.MituyaProject.World.WorldData;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 *
 * @author chantake-mac
 */
public class BorderGuard implements Runnable {

    public static HashMap<Player, Location> loca = new HashMap<>();
    private final MituyaProject plugin;
    public ChatColor color;

    public BorderGuard(MituyaProject mp) {
        this.plugin = mp;
    }

    @Override
    public void run() {
        for (WorldData wd : this.plugin.getWorldManager().getWorldDatas()) {
            int borderline = wd.getBorder();
            for (final Player pr : wd.getWorld().getPlayers()) {
                PlayerInstance ins = plugin.getInstanceManager().getInstance(pr);
                if (!getBorder(pr.getLocation(), borderline)) {//エリアオーバー
                    pr.teleport(BorderGuard.loca.get(pr));
                    ins.sendAttention(Parameter328.border);
                } else {
                    BorderGuard.loca.put(pr, pr.getLocation());
                }
            }
        }
    }

    public boolean getFlyFromSkyLand(Location lo) {
        return lo.getWorld().equals(plugin.getWorldManager().getWorld("skyland")) && lo.getY() <= 1;
    }

    public boolean getBorder(Location lo, int border) {
        return Math.abs(lo.getX()) <= border && Math.abs(lo.getZ()) <= border;
    }
}

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

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 *
 * @author fumitti
 */
public class ArrowRain implements Runnable {

    private final Player player;
    private final int mode;
    private final long time;

    public ArrowRain(Player p, int m, int t) {
        player = p;
        mode = m;
        time = System.currentTimeMillis() + (t * 1000);
    }

    @Override
    public void run() {
        if (time > System.currentTimeMillis()) {
            if (mode == 1) {
                player.shootArrow();
            } else {
                double diff = (2 * Math.PI) / 24.0;
                for (double a = 0; a < 2 * Math.PI; a += diff) {
                    Vector vel = new Vector(Math.cos(a), 0, Math.sin(a));
                    sendArrowFromPlayer(player, vel, 2, 12);
                }
            }
        } else {
            //TimerManager.getInstance().remove(this);
        }
    }

    public static void sendArrowFromPlayer(Player player,
            Vector dir, float speed, float spread) {
        Location loc = player.getEyeLocation();
        Vector actualDir = dir.clone().normalize();
        Vector finalVecLoc = loc.toVector().add(actualDir.multiply(2));
        loc.setX(finalVecLoc.getX());
        loc.setY(finalVecLoc.getY());
        loc.setZ(finalVecLoc.getZ());
        player.getWorld().spawnArrow(loc, dir, speed, spread);
    }
}

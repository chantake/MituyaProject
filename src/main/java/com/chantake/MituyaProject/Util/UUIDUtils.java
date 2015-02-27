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

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 *
 * @author fumitti
 */
public class UUIDUtils {

    private UUIDUtils() {
    }

    @SuppressWarnings("deprecation")
    public static UUID getPlayerUUID(String playerName) {
        Player player = Bukkit.getPlayerExact(playerName);

        if (player != null) {
            return player.getUniqueId();
        } else {
            return Bukkit.getOfflinePlayer(playerName).getUniqueId();
        }
    }

    public static String getPlayerName(UUID playerUUID) {
        Player player = Bukkit.getPlayer(playerUUID);

        if (player != null) {
            return player.getName();
        } else {
            return Bukkit.getOfflinePlayer(playerUUID).getName();
        }
    }

    public static boolean compareUUID(UUID a, UUID b) {
        return a.toString().equals(b.toString());
    }

}

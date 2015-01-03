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
package com.chantake.MituyaProject.Player;

import com.chantake.MituyaProject.Permissions.Rank;
import org.bukkit.ChatColor;

/**
 *
 * @author chantake
 */
public class DisplayNameManager {

    private final InstanceManager manager;

    public DisplayNameManager(InstanceManager manager) {
        this.manager = manager;
    }

    /**
     * 表示名を取得します
     *
     * @param ins
     */
    public void setDisplayName(PlayerInstance ins) {
        StringBuilder sb = new StringBuilder();
        Rank rank = ins.getRank();
        //rawdisplayname
        if (ins.isNickName()) {
            ins.setRawDisplayName(ins.getNickName());
        } else {
            ins.setRawDisplayName(ins.getName());
        }
        switch (rank) {
            case Admin:
                sb.append("[").append(ChatColor.BLUE).append("Admin").append(ChatColor.WHITE).append("]").append(ChatColor.AQUA).append(ins.getRawDisplayName()).append(ChatColor.WHITE);
                break;
            case SubAdmin:
                sb.append("[").append(ChatColor.BLUE).append("SubAdmin").append(ChatColor.WHITE).append("]").append(ChatColor.AQUA).append(ins.getRawDisplayName()).append(ChatColor.WHITE);
                break;
            case SuperGM:
                sb.append("[").append(ChatColor.RED).append("SuperGM").append(ChatColor.WHITE).append("]").append(ChatColor.GREEN).append(ins.getRawDisplayName()).append(ChatColor.WHITE);
                break;
            case GM:
                sb.append("[").append(ChatColor.RED).append("GM").append(ChatColor.WHITE).append("]").append(ChatColor.GREEN).append(ins.getRawDisplayName()).append(ChatColor.WHITE);
                break;
            case ModeratorReader:
                sb.append("[").append(ChatColor.AQUA).append("Reader").append(ChatColor.WHITE).append("]").append(ChatColor.GREEN).append(ins.getRawDisplayName()).append(ChatColor.WHITE);
                break;
            case Moderator:
                sb.append("[").append(ChatColor.GOLD).append("Mod").append(ChatColor.WHITE).append("]").append(ChatColor.GREEN).append(ins.getRawDisplayName()).append(ChatColor.WHITE);
                break;
            case Supporter:
                sb.append("[").append(ChatColor.GOLD).append("SP").append(ChatColor.WHITE).append("]").append(ins.getRawDisplayName());
                break;
            case LiveReporter:
                sb.append("[").append(ChatColor.YELLOW).append("Live").append(ChatColor.WHITE).append("]").append(ins.getRawDisplayName());
                break;
            case Ust:
                sb.append("[").append(ChatColor.YELLOW).append("Ust").append(ChatColor.WHITE).append("]").append(ins.getRawDisplayName());
                break;
            case Niconama:
                sb.append("[").append(ChatColor.YELLOW).append("Nico2").append(ChatColor.WHITE).append("]").append(ins.getRawDisplayName());
                break;
            default:
                if (rank.getId() != 0) {
                    sb.append("[").append(ChatColor.GREEN).append(rank.getName()).append(ChatColor.WHITE).append("]").append(ins.getRawDisplayName());
                } else {
                    sb.append(ins.getRawDisplayName());
                }
                break;
        }
        ins.setDisplayName(sb.toString());
    }
}

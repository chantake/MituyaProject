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

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.chantake.MituyaProject.MituyaProject;
import com.chantake.MituyaProject.Tool.MySqlProcessing;

/**
 *
 * @author chantake-mac
 */
public class AutoStop implements Runnable {

    public MituyaProject plugin;
    private int stop;
    private boolean reload = false;

    public AutoStop(MituyaProject mp, int time, boolean reload) {
        plugin = mp;
        stop = time * 60;
        this.reload = reload;
    }

    @Override
    public void run() {
        int time;
        int tt = 0;
        if (stop > 60 && stop % 60 != 0) {
            time = -1;
        } else if (stop > 60) {
            tt = stop / 60;
            time = -2;
        } else {
            time = stop;
        }
        switch (time) {
            case -2:
                plugin.broadcastMessage(
                        ChatColor.LIGHT_PURPLE
                        + "[Server] **サーバー停止まで "
                        + ChatColor.YELLOW + tt + ChatColor.LIGHT_PURPLE
                        + " 分.**");
                break;
            case -1:
                break;
            case 0:
                plugin.Log("AutoStopStart...");
                plugin.broadcastMessage(ChatColor.LIGHT_PURPLE + "[Server] 自動停止開始...");
                plugin.SaveAll();
                MySqlProcessing.SaveServer();
                plugin.broadcastMessage(ChatColor.LIGHT_PURPLE + "[Server] **自動保存完了**");
                plugin.Log("AutoSaveComplete.");
                plugin.Log("Stop the server start...");
                plugin.broadcastMessage(ChatColor.LIGHT_PURPLE + "[Server] サーバーが停止します...");
                for (final Player pr : plugin.getServer().getOnlinePlayers()) {
                    pr.kickPlayer("http://328mss.com/ サーバーが停止しました");
                }
                plugin.Stop();
                break;
            case 300:
                plugin.broadcastMessage(
                        ChatColor.LIGHT_PURPLE
                        + "[Server] **サーバー停止まで "
                        + ChatColor.YELLOW + "5" + ChatColor.LIGHT_PURPLE
                        + " 分.**");
                break;
            case 180:
                plugin.broadcastMessage(
                        ChatColor.LIGHT_PURPLE
                        + "[Server] **サーバー停止まで "
                        + ChatColor.YELLOW + "3" + ChatColor.LIGHT_PURPLE
                        + " 分.**");
                break;
            case 120:
                plugin.broadcastMessage(
                        ChatColor.LIGHT_PURPLE
                        + "[Server] **サーバー停止まで "
                        + ChatColor.YELLOW + "2" + ChatColor.LIGHT_PURPLE
                        + " 分.**");
                break;
            case 60:
                plugin.broadcastMessage(
                        ChatColor.LIGHT_PURPLE
                        + "[Server] **サーバー停止まで "
                        + ChatColor.YELLOW + "1" + ChatColor.LIGHT_PURPLE
                        + " 分.**");
                break;
            default:
                plugin.broadcastMessage(
                        ChatColor.LIGHT_PURPLE
                        + "[Server] **サーバー停止まで "
                        + ChatColor.YELLOW + time + ChatColor.LIGHT_PURPLE
                        + "秒.**");
                break;
        }
        stop -= 10;
    }
}

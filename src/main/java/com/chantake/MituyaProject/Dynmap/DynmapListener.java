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
package com.chantake.MituyaProject.Dynmap;

import com.chantake.MituyaProject.Bukkit.Event.MituyaPlayerChatEvent;
import com.chantake.MituyaProject.Bukkit.Event.MituyaPlayerHideEvent;
import com.chantake.MituyaProject.Player.Chat.ChatType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 *
 * @author chantake
 */
public class DynmapListener implements Listener {

    private final DynmapApiConnecter dynmap;

    public DynmapListener(DynmapApiConnecter dynmap) {
        this.dynmap = dynmap;
    }

    /**
     * プレーヤーがログイン時に呼ばれるメソッド
     *
     * @param event PlayerJoinEvent
     * @see PlayerJoinEvent
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        dynmap.getDynmapAPI().postPlayerJoinQuitToWeb(event.getPlayer(), true);
    }

    /**
     * プレーヤーがログアウト時に呼ばれるメソッド
     *
     * @param event PlayerQuitEvent
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        dynmap.getDynmapAPI().postPlayerJoinQuitToWeb(event.getPlayer(), false);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void MituyaPlayerChatEvent(MituyaPlayerChatEvent event) {
        if (event.getChatType() == ChatType.All) {
            this.dynmap.getDynmapAPI().postPlayerMessageToWeb(event.getPlayer(), event.getMessage());
        }
    }

    @EventHandler
    public void MituyaPlayerHideEvent(MituyaPlayerHideEvent event) {
        this.dynmap.getDynmapAPI().setPlayerVisiblity(event.getPlayer(), !event.isHide());
    }
}

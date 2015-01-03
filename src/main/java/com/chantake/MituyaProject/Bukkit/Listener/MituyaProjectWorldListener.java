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
package com.chantake.MituyaProject.Bukkit.Listener;

import com.chantake.MituyaProject.MituyaProject;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldSaveEvent;

/**
 * MituyaProject World listener
 *
 * @author chantake
 */
public class MituyaProjectWorldListener implements Listener {

    private final MituyaProject plugin;
    public String stuff = "";
    public ChatColor color;

    public MituyaProjectWorldListener(final MituyaProject plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        plugin.getDebug().getLl2().add(event.getChunk());
    }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {
        plugin.getDebug().getLl2().remove(event.getChunk());
    }

    @EventHandler
    public void onWorldSave(WorldSaveEvent event) {
        //plugin.Log("ワールドセーブ完了 : "+event.getWorld().getName());
    }
}

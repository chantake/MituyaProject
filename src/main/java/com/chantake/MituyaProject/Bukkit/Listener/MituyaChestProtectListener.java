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

import com.chantake.MituyaProject.MituyaManager;
import com.chantake.MituyaProject.MituyaProject;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 *
 * @author chantake
 */
public class MituyaChestProtectListener extends MituyaManager implements Listener {

    public MituyaChestProtectListener(MituyaProject plugin) {
        super(plugin);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        //キャンセル済みの場合
        if (event.isCancelled() || !event.canBuild()) {
            return;
        }
        Block b = event.getBlock();
        //ホッパーもしくは線路を置いた場合
        if (b.getType().name().contains("RAIL") || b.getType() == Material.HOPPER) {
            //上がチェストかどうか
            Location loc = b.getLocation().clone();
            loc.setY(loc.getY() + 1);
            Block bk = loc.getBlock();
            //建設権限がある場合のみok
            if (this.getPlugin().canBuild(event.getPlayer(), bk)) {
                return;
            }
            //ない場合(ぎりぎり
            if (bk.getType() == Material.CHEST || bk.getType() == Material.DISPENSER || bk.getType() == Material.FURNACE) {
                event.setCancelled(true);
            }
        }
    }
}

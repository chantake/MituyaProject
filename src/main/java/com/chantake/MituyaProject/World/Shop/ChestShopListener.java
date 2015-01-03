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
package com.chantake.MituyaProject.World.Shop;

import com.chantake.MituyaProject.Bukkit.Event.MituyaPlayerSignClickEvent;
import com.chantake.MituyaProject.Exception.PlayerOfflineException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 *
 * @author chantake
 */
public class ChestShopListener implements Listener {

    private final ChestShopManager manager;

    public ChestShopListener(ChestShopManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void MituyaPlayerSignClickEvent(MituyaPlayerSignClickEvent event) {
        try {
            event.setCancel(manager.ChestShop(event.getSign(), event.getPlayerInstance(), event.isRightClick()));
        }
        catch (PlayerOfflineException ex) {
        }
    }

}

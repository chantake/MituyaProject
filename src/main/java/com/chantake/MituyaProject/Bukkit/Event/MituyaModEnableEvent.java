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
package com.chantake.MituyaProject.Bukkit.Event;

import com.chantake.MituyaProject.Player.PlayerInstance;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 *
 * @author いんく
 */
public class MituyaModEnableEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    PlayerInstance pins;
    String version;

    public MituyaModEnableEvent(PlayerInstance ins, String ver) {
        pins = ins;
        version = ver;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public PlayerInstance getInstance() {
        return pins;
    }

    public String getVersion() {
        return version;
    }
}

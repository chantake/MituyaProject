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

import com.chantake.MituyaProject.MituyaProject;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * MituyaProject Disable Event
 *
 * @author chantake
 */
public class MituyaDisableEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private static final long serialVersionUID = -6731010390916182433L;
    private final MituyaProject plugin;

    public MituyaDisableEvent(MituyaProject plugin) {
        this.plugin = plugin;
    }

    public MituyaProject getPlugin() {
        return plugin;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

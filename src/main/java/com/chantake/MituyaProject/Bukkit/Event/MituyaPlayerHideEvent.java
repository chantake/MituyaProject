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

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 *
 * @author chantake
 */
public class MituyaPlayerHideEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private static final long serialVersionUID = 7964929767144587099L;
    private final Player player;
    private boolean hide;

    public MituyaPlayerHideEvent(Player player, boolean hide) {
        this.player = player;
        this.hide = hide;
    }

    /**
     * Hide状態を取得します
     *
     * @return trueだと有効
     */
    public boolean isHide() {
        return hide;
    }

    public Player getPlayer() {
        return player;
    }

    public void setHide(boolean hide) {
        this.hide = hide;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

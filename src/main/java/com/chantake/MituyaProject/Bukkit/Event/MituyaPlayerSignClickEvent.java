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
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * 看板がクリックされたときに呼ばれるイベント
 *
 * @author chantake
 */
public class MituyaPlayerSignClickEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private static final long serialVersionUID = 7964929767144587099L;
    private final Sign sign;
    private final Player player;
    private final PlayerInstance instance;
    private final boolean rightclick;
    private boolean cancel = false;

    public MituyaPlayerSignClickEvent(Sign sign, Player player, PlayerInstance instance, boolean rightclick) {
        this.sign = sign;
        this.player = player;
        this.instance = instance;
        this.rightclick = rightclick;
    }

    public Sign getSign() {
        return sign;
    }

    public boolean isCancel() {
        return cancel;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    public boolean isRightClick() {
        return rightclick;
    }

    public Player getPlayer() {
        return player;
    }

    public PlayerInstance getPlayerInstance() {
        return this.instance;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

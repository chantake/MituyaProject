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

import com.chantake.MituyaProject.Permissions.Rank;
import com.chantake.MituyaProject.Player.PlayerInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * 328の独自システム　ランクチェンジイベント
 *
 * @author chantake
 */
public class MituyaRankChangeEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private static final long serialVersionUID = -6731010390916182433L;
    private final Player player;
    private final PlayerInstance instance;
    private final Rank oldrank;
    private Rank newrank;
    private boolean cancel;

    public MituyaRankChangeEvent(Player player, PlayerInstance instance, Rank oldrank, Rank newrank) {
        this.player = player;
        this.instance = instance;
        this.oldrank = oldrank;
        this.newrank = newrank;
    }

    public boolean isCancel() {
        return cancel;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    public Player getPlayer() {
        return player;
    }

    public PlayerInstance getPlayerInstance() {
        return this.instance;
    }

    public Rank getNewRank() {
        return newrank;
    }

    public Rank getOldRank() {
        return oldrank;
    }

    public void setNewRank(Rank newrank) {
        this.newrank = newrank;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

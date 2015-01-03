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

import com.chantake.MituyaProject.Player.Chat.ChatType;
import com.chantake.MituyaProject.Player.PlayerInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * 328でのプレーヤーチャットイベント
 *
 * @author chantake
 */
public class MituyaPlayerChatEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private static final long serialVersionUID = 7964929767144587099L;
    private final Player player;
    private final PlayerInstance instance;
    private final String message;
    private final ChatType chatType;
    private boolean cancelled = false;

    public MituyaPlayerChatEvent(Player player, PlayerInstance instance, String message, ChatType chatType) {
        this.player = player;
        this.instance = instance;
        this.message = message;
        this.chatType = chatType;
    }

    public Player getPlayer() {
        return player;
    }

    public ChatType getChatType() {
        return chatType;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public String getMessage() {
        return message;
    }

    public PlayerInstance getPlayerInstance() {
        return this.instance;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

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
package com.chantake.MituyaProject.World.MobManager;

import com.chantake.MituyaProject.Bukkit.Event.MituyaDisableEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;

/**
 * MobManagerのBukkit Listener
 *
 * @author chantake
 */
public class MobManagerListener implements Listener {

    private final MobManager manager;

    /**
     * インスタンス作成
     *
     * @param manager MobManager
     */
    public MobManagerListener(MobManager manager) {
        this.manager = manager;
    }

    /**
     * Mobがスポーンした際に呼ばれるメソッド
     *
     * @param event CreatureSpawnEvent
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onCreatureSpawnEvent(CreatureSpawnEvent event) {
        this.manager.putMobSpawnerEntity(event.getEntity(), event.getSpawnReason());
    }

    /**
     * Entityが死亡した時に呼ばれるメソッド
     *
     * @param event EntityDeathEvent
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDeathEvent(EntityDeathEvent event) {
        this.manager.deathEntity(event.getEntity());
    }

    /**
     * 328プラグインが無効になる時に呼ばれるメソッド
     *
     * @param event MituyaDisableEvent
     */
    @EventHandler
    public void onDisableEvent(MituyaDisableEvent event) {
        event.getPlugin().Log("Mobを削除しています");
        this.manager.removeMob();
        event.getPlugin().Log("Mobを削除しました");
    }
}

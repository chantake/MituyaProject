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

import com.chantake.MituyaProject.MituyaProject;
import com.chantake.MituyaProject.Parameter.Parameter328;
import java.util.Iterator;
import java.util.LinkedHashMap;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

/**
 * Mobを管理するクラス
 *
 * @author chantake
 */
public class MobManager {

    /**
     * MituyaProject Plugin
     */
    private final MituyaProject plugin;
    /**
     * スポーンから湧いたmob
     */
    private final LinkedHashMap<LivingEntity, Long> spawner_Entity = new LinkedHashMap<>();
    /**
     * その他消していいmob
     */
    private final LinkedHashMap<LivingEntity, Long> other_Entity = new LinkedHashMap<>();
    /**
     * Bukkitリスナー
     */
    private final MobManagerListener listener = new MobManagerListener(this);

    /**
     * インスタンス作成
     *
     * @param plugin MituyaProject
     */
    public MobManager(MituyaProject plugin) {
        this.plugin = plugin;
    }

    /**
     * 初期化
     */
    public void init() {
        this.plugin.getServer().getPluginManager().registerEvents(this.listener, this.plugin);
    }

    /**
     * スポナーから湧いた自動削除対象のmobを追加
     *
     * @param entity Entity
     * @param reason
     */
    public void putMobSpawnerEntity(LivingEntity entity, SpawnReason reason) {
        if (isRemoveEntity(entity, reason)) {
            if (reason == SpawnReason.SPAWNER) {
                this.spawner_Entity.put(entity, System.currentTimeMillis());
                int a = this.spawner_Entity.size() - Parameter328.Mob_Limit_Spawner;
                //0以上の場合
                if (a > 0) {
                    Iterator<LivingEntity> iterator = this.spawner_Entity.keySet().iterator();
                    for (int i = 0; i < a; i++) {
                        LivingEntity next = iterator.next();
                        this.spawner_Entity.remove(next);
                        plugin.broadcastDebugMessage("Remove Spawner Entity:" + next.getType().getName());
                        next.remove();
                    }
                }
            } else {
                this.other_Entity.put(entity, System.currentTimeMillis());
            }
        }
    }

    /**
     * Mobが死んだときにmapから削除する
     *
     * @param entity
     */
    public void deathEntity(LivingEntity entity) {
        this.spawner_Entity.remove(entity);
        this.other_Entity.remove(entity);
    }

    public boolean isSpawnerEntity(LivingEntity entity) {
        return this.spawner_Entity.containsKey(entity);
    }

    /**
     * 削除していいmobか判断
     *
     * @param entity Entity
     * @param reason SpawneReason 理由
     * @return trueの場合削除してもいいmob
     */
    public boolean isRemoveEntity(LivingEntity entity, SpawnReason reason) {
        //スポナーはすべてok
        if (reason == SpawnReason.SPAWNER) {
            return true;
        }
        //その他Entity削除
        switch (entity.getType()) {
            case BLAZE:
                break;
            case ARROW:
                break;
            case CAVE_SPIDER:
                break;
            case SKELETON:
                break;
            case SPIDER:
                break;
            case CREEPER:
                break;
            case ZOMBIE:
                break;
            case SQUID:
                break;
            case SNOWBALL:
                break;
            case SLIME:
                break;
            case PIG_ZOMBIE:
                break;
            case MAGMA_CUBE:
                break;
            case GHAST:
                break;
            case ENDERMAN:
                break;
            //その他Entityは削除しない
            default:
                //false
                return false;
        }
        return true;
    }

    /**
     * すべてのmobを削除
     */
    public void removeMob() {
        for (LivingEntity entity : this.spawner_Entity.keySet()) {
            entity.remove();
        }
        for (LivingEntity entity : this.other_Entity.keySet()) {
            entity.remove();
        }
        this.spawner_Entity.clear();
        this.other_Entity.clear();
    }

    public void removeOtherMob() {
        plugin.broadcastGMMessage(ChatColor.LIGHT_PURPLE + "[Server] Remove Other Entity :" + this.other_Entity.size());
        for (LivingEntity entity : this.other_Entity.keySet()) {
            entity.remove();
        }
        this.other_Entity.clear();
    }
}

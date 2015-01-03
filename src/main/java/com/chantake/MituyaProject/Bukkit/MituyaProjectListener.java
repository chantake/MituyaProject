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
package com.chantake.MituyaProject.Bukkit;

import com.chantake.MituyaProject.Bukkit.Event.MituyaDisableEvent;
import com.chantake.MituyaProject.Bukkit.Event.MituyaRankChangeEvent;
import com.chantake.MituyaProject.MituyaProject;
import com.chantake.MituyaProject.Player.PlayerInstance;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.world.ChunkLoadEvent;

/**
 * BukkitListener
 *
 * @author chantake
 */
public class MituyaProjectListener implements Listener {

    /**
     * @see MituyaProject
     */
    private final MituyaProject plugin;

    /**
     * インスタンス作成
     *
     * @param plugin
     */
    public MituyaProjectListener(MituyaProject plugin) {
        this.plugin = plugin;
    }

    /**
     * プレーヤーがレベルを変更した際に呼ばれるメソッド
     *
     * @param event
     */
    @EventHandler
    public void onPlayerLevelChange(PlayerLevelChangeEvent event) {
        PlayerInstance ins = plugin.getInstanceManager().getInstance(event.getPlayer());
        ins.sendSuccess(ChatColor.YELLOW + "レベル変更：" + event.getOldLevel() + "Lv から " + event.getNewLevel() + "Lv になりました");
        ins.saveLv(event.getNewLevel());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void MituyaRankChangeEvent(MituyaRankChangeEvent event) {
        if (!event.isCancel()) {
            event.getPlayerInstance().sendSuccess("ランクを変更しました。 Old:" + event.getOldRank().toString() + " New:" + event.getNewRank().toString());
        }
    }

    /*@EventHandler(priority = EventPriority.HIGH)
     public void MituyaPlayerChatEvent(MituyaPlayerChatEvent event) {
     if (event.getMessage().startsWith("u00a")) {
     event.setCancelled(true);
     }
     }*/
    /**
     * PluginがDisableする時に呼ばれるメソッド
     *
     * @param event
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onPluginDisableEvent(PluginDisableEvent event) {
        if (event.getPlugin().getName().equalsIgnoreCase("MituyaProject")) {
            this.plugin.getServer().getPluginManager().callEvent(new MituyaDisableEvent(this.plugin));
        }
    }

    /**
     * Entityが死んだ時に呼び出されるメソッド
     *
     * @param event EntityDeathEvent
     */
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        //死亡判定
        if (event instanceof PlayerDeathEvent) {
            //死んだプレーヤーを取得
            Player player = (Player)event.getEntity();
            //PlayerInstance
            PlayerInstance ins = plugin.getInstanceManager().getInstance(player);
            //PlayerDeathEvent
            PlayerDeathEvent deathEvent = (PlayerDeathEvent)event;

            //<editor-fold defaultstate="collapsed" desc="レベル設定">
            /*
             * レベル設定
             */
            int lv = player.getLevel();
            int hl = 0;
            if (lv <= 15) {
                ins.sendSuccess("15レベルまではレベルが下がりません。");
            } else if (lv <= 50) {
                hl = - 3;
            } else if (lv <= 150) {
                hl = - 2;
            } else {
                hl = - 1;
            }
            lv = lv + hl;
            deathEvent.setNewLevel(lv);
            // </editor-fold>
            if (ins.getDeathMessage().length() > 1) {
                deathEvent.setDeathMessage(deathEvent.getDeathMessage() + " <" + ins.getDeathMessage());
            }
        } else {
            /*
             * Mobが死亡
             */
        }
    }

    @EventHandler
    public void onChunkLoadEvent(ChunkLoadEvent event) {
        if (event.isNewChunk()) {
            if (plugin.getWorldManager().getWorldData(event.getWorld()).getType().equalsIgnoreCase("WATER")) {
                Chunk chunk = event.getChunk();
                for (int y = 64; y < 150; y++) {
                    for (int x = 0; x < 16; x++) {
                        for (int z = 0; z < 16; z++) {
                            Block block = chunk.getBlock(chunk.getX() + x, y, chunk.getZ() + z);
                            if (block.getType() == Material.AIR) {
                                block.setType(Material.WATER);
                            }
                        }
                    }
                }
            }
        }
    }
}

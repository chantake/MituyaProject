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
package com.chantake.MituyaProject.Bukkit.Listener;

import com.chantake.MituyaProject.Exception.PlayerOfflineException;
import com.chantake.MituyaProject.MituyaProject;
import com.chantake.MituyaProject.Parameter.Parameter328;
import com.chantake.MituyaProject.Player.PlayerInstance;
import com.chantake.MituyaProject.Tool.Timer.DamageTeleportCancel;
import com.chantake.MituyaProject.Tool.Timer.TimerManager;
import com.chantake.MituyaProject.World.WorldData;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.World.Environment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.projectiles.ProjectileSource;

/**
 * MituyaProject Entity listener
 *
 * @author chantake
 */
public class MituyaProjectEntityListener implements Listener {

    private final MituyaProject plugin;
    private ChatColor color;
    private final Map<Entity, Player> prdg = new HashMap<>();
    private final Map<Player, Player> prpg = new HashMap<>();

    public MituyaProjectEntityListener(final MituyaProject plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        WorldData wd = plugin.getWorldManager().getWorldData(event.getLocation().getWorld());
        if (wd.isMobStop()) {//mobキャンセル
            event.setCancelled(true);
            return;
        }
        EntityType entityType = event.getEntityType();
        if (entityType == EntityType.WITHER) {//ウィザー召喚禁止
            event.setCancelled(true);
        }

        if (wd.getEnvironment() == Environment.NETHER) {
            if (entityType == EntityType.SNOWMAN) {//スノーマンはネザーに湧きません
                event.setCancelled(true);
            }
        } else if (wd.getEnvironment() == Environment.NORMAL) {
            if (entityType == EntityType.BLAZE || entityType == EntityType.ENDER_DRAGON || entityType == EntityType.GHAST || entityType == EntityType.MAGMA_CUBE) {//ブレイズ、エンダードラゴン、ガスト禁止※通常ワールドで
                event.setCancelled(true);
            }
        }
    }

    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        /*
         * if(event.getEntity() instanceof Player) {
         * event.setFoodLevel(event.getFoodLevel() * 2);
         }
         */
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) throws PlayerOfflineException {
        Entity entity = event.getEntity();
        //PvP
        if (entity instanceof Player) {//攻撃対象がPlayerのみ
            Player player = (Player)event.getEntity();
            //15レベル以下は餓死しないように
            if (event.getCause() == EntityDamageEvent.DamageCause.STARVATION && player.getLevel() <= 15 && player.getHealth() <= 10) {
                event.setDamage(0);
            }

            PlayerInstance ins = plugin.getInstanceManager().getInstance(player);
            ins.setDamagecanncel(true);
            TimerManager.getInstance().schedule(new DamageTeleportCancel(plugin, ins), Parameter328.canteleportnodmgticks);
            if (event instanceof EntityDamageByEntityEvent) {//Entityによる攻撃のみ
                EntityDamageByEntityEvent edbye = (EntityDamageByEntityEvent)event;
                Player damager = null;
                if (edbye.getDamager() instanceof Player) {//攻撃者がPlayer
                    damager = (Player)edbye.getDamager();
                } else if (edbye.getDamager() instanceof Arrow) {//や
                    Arrow arrow = (Arrow)edbye.getDamager();
                    ProjectileSource shooter = arrow.getShooter();
                    if (shooter instanceof Player) {
                        damager = (Player)shooter;
                    }
                }
                if (damager != null) {
                    PlayerInstance dins = plugin.getInstanceManager().getInstance(damager);
                    if (!dins.getPvP()) {//攻撃者PvP判定
                        dins.sendInfo(ChatColor.DARK_GRAY + "PvP", "PvPがオンになっていません。コマンド(/pvp か /pvp on）で設定してください。");
                        event.setCancelled(true);
                    } else if (!ins.getPvP()) {//攻撃対象PvP判定
                        dins.sendInfo(ChatColor.DARK_GRAY + "PvP", ins.getName() + " は PvP をオンにしていません。");
                        event.setCancelled(true);
                    } else {//双方PvPがonになっている場合
                        prpg.put(player, damager);
                    }
                } else {//人以外に攻撃された場合remove
                    prpg.remove(player);
                }
            } else {//モンスター以外に攻撃（落下ダメージなど）の場合もremove
                prpg.remove(player);
            }
        } else {
            if (!(event instanceof EntityDamageByEntityEvent)) {
                prdg.remove(entity);
                return;
            }
            final EntityDamageByEntityEvent e = (EntityDamageByEntityEvent)event;

            if (e.getDamager() instanceof Arrow) {
                Arrow arrow = (Arrow)e.getDamager();
                if (arrow.getShooter() instanceof Player) {
                    prdg.put(entity, (Player)arrow.getShooter());
                }
            } else if (!(e.getDamager() instanceof Player)) {
                prdg.remove(entity);
            } else {
                prdg.put(entity, (Player)e.getDamager());
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) throws PlayerOfflineException {
        event.setDroppedExp((int)(event.getDroppedExp() * Parameter328.Exp));
        if (event.getEntity() instanceof Player) {
            EntityType entityType = event.getEntityType();
            Player player = (Player)event.getEntity();
            PlayerInstance ins = plugin.getInstanceManager().getInstance(player);
            //spawn back
            ins.setDeath();
            //確認機能
            ins.cancelCheckInstance();
            ins.setLastworld(player.getWorld());//last world
            int lv = player.getLevel();
            if (lv < 15) {
                event.setDroppedExp(0);
            } else {
                event.setDroppedExp(event.getDroppedExp() / 10);
            }
            if (prpg.containsKey(player)) {//pvpだった場合
                //負けたプレーヤー PlayerInstance ins

                //勝ったプレーヤー
                PlayerInstance dins = plugin.getInstanceManager().getInstance(prpg.get(player));

                if (ins == dins) {//自殺
                    return;
                }
                int mine = 1000;//基本勝利金
                if (ins.getPvP_Win() != 0) {//連勝している場合（負けたほうが
                    int win = 750;
                    mine = mine + (win * ins.getPvP_Win());
                }
                //連勝フラグ
                ins.resetPvP_Win();//負けたのでリセット
                dins.setPvP_Win();//勝ったので+

                //pvpメッセージ
                plugin.broadcastRangeMessage(player.getLocation(), "[" + ChatColor.DARK_GRAY + "PvP" + ChatColor.WHITE + "] " + ins.getName() + "(" + ins.getPvP_Win() + ")" + ChatColor.DARK_GRAY + " は " + ChatColor.WHITE + dins.getName() + "(" + dins.getPvP_Win() + ")" + ChatColor.DARK_GRAY + " に倒されました。.");
                if ((dins.getPvP_Win() % 10) == 0 && dins.getPvP_Win() >= 0) {
                    plugin.broadcastMessage(ChatColor.DARK_GRAY + "PvP", dins.getName() + " 様が " + dins.getPvP_Win() + "連勝しました。");
                }
                //引き落とし
                //ちゃんと引き落とせた場合
                if (ins.gainMine(-mine)) {//負けたほう
                    dins.gainMine(mine);//勝ったほう
                } else {
                    dins.sendInfo(ChatColor.DARK_GRAY + "PvP", ChatColor.RED + ins.getName() + "様のMineが足りないため受け取れませんでした。");
                }
                
                //PVPでアイテムをドロップさせない
                if (event instanceof PlayerDeathEvent) {
                    PlayerDeathEvent deathEvent = (PlayerDeathEvent)event;
                    deathEvent.setKeepInventory(true);
                }

                //攻撃フラグを削除
                prpg.remove(player);
                return;
            }
        }
        final Entity victim = event.getEntity();
        if (!prdg.containsKey(victim)) {
            return;
        }
        final Player player = prdg.get(victim);
        final PlayerInstance ins = plugin.getInstanceManager().getInstance(player.getName());
        prdg.remove(victim);
        EntityType type = victim.getType();
        int rand = 8;
        long mine = 8;
        if (type == EntityType.SKELETON || type == EntityType.ZOMBIE) {
            rand = 6;
        } else if (type == EntityType.CREEPER) {
            rand = 12;
        } else if (type == EntityType.SQUID) {
            rand = 8;
        } else if (type == EntityType.SLIME) {
            Slime slime = (Slime)victim;
            switch (slime.getSize()) {
                case 4:
                    rand = 8;
                    mine = 8;
                    break;
                case 3:
                    rand = 6;
                    mine = 2;
                    break;
                case 2:
                    rand = 5;
                    mine = 2;
                    break;
                default:
                    rand = 5;
                    mine = 2;
                    break;
            }
        } else if (type == EntityType.SHEEP) {
            rand = 11;
        } else if (type == EntityType.ENDER_DRAGON) {
            rand = 400000;
        } else if (type == EntityType.SILVERFISH) {
            rand = 45;
        } else if (type == EntityType.CAVE_SPIDER) {
            rand = 25;
        } else if (type == EntityType.GIANT) {
            rand = 150000;
        } else if (type == EntityType.PIG_ZOMBIE) {
            rand = 6;
        } else if (type == EntityType.WOLF) {
            final Wolf wf = (Wolf)victim;
            if (wf.getOwner() instanceof Player) {
                rand = -5000;
            } else {
                rand = 25;
            }
        }
        if (plugin.getMobManager().isSpawnerEntity(event.getEntity())) {
            rand = (int)(rand - (rand * 0.4));
        }
        mine += (int)(Math.random() * rand);
        if (ins.getMine() - mine < 0) {
            ins.gainMine(-ins.getMine());
        } else {
            ins.gainMine(mine);
        }
        final StringBuilder stbr = new StringBuilder();
        stbr.append("[").append(ChatColor.DARK_GRAY).append("Entity").append(ChatColor.WHITE).append("]" + " ").append(ChatColor.GRAY).append(this.plugin.getJapaneseMessage().getEntityName(type)).append(ChatColor.WHITE).append(" を倒しました。 入手金額：").append(ChatColor.YELLOW).append(mine).append(ChatColor.GREEN).append("Mine.");
        player.sendMessage(stbr.toString());
    }
}

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
package com.chantake.MituyaProject.World;

import com.chantake.MituyaProject.Bukkit.ItemName;
import com.chantake.MituyaProject.Exception.PlayerOfflineException;
import com.chantake.MituyaProject.MituyaProject;
import com.chantake.MituyaProject.Parameter.Parameter328;
import com.chantake.MituyaProject.Player.PlayerInstance;
import com.chantake.MituyaProject.Tool.MySqlProcessing;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

/**
 * ドロップシステム
 *
 * @author chantake
 */
public class DropItem {

    public static void onBlockBreak(MituyaProject plugin, BlockBreakEvent event, PlayerInstance ins) throws PlayerOfflineException {
        int typeid = event.getBlock().getTypeId();
        int id = 0;
        switch (typeid) {
            case 1:
                id = 2;
                break;
            case 2:
                id = 3;
                break;
            case 3:
                id = 3;
                break;
            case 12:
                id = 4;
                break;
            case 13:
                id = 4;
                break;
            case 18:
                id = 1;
                break;
        }
        if (id == 0) {
            return;
        }
        Jackpot.addjackpot(3, plugin);// ジャックポット追加
        Player player = ins.getPlayer();
        int rand = Rand(Parameter328.DropRate * id);
        if (typeid == 18) {
            if (Rand(250) == 3) {
                if (rand == 328) {
                    ItemDrop(Material.GOLDEN_APPLE.getId(), 0, 1, player.getWorld(), event.getBlock().getLocation());
                    ins.sendInfo(ChatColor.YELLOW + "Drop", ChatColor.YELLOW + "金リンゴをドロップしました");
                }
            }
        } else if (rand >= 492) {
            if (rand <= 494) {
                final int[] itemid = {19, 19, 19, 19, 79, 79, 79, 79, 79, 399};
                final int item = (int)(Math.random() * itemid.length);
                ItemDrop(itemid[item], 0, 1, player.getWorld(), event.getBlock().getLocation());
                String itemname = ItemName.getItemName(itemid[item], (short)0);
                if (itemid[item] == 399) {
                    plugin.broadcastDebugMessage("DropItem 399 player:" + ins.getName());
                }
                ins.sendInfo(ChatColor.YELLOW + "Drop", ChatColor.YELLOW + itemname + " をドロップしました");
            } else if (rand <= 500) {
                final long gmine = 328;
                ins.gainMine(gmine);
                ins.sendInfo(ChatColor.YELLOW + "Drop", "Mine ゲット!!");
            }/* else if (rand <= 510) {//351:3
             ItemDrop(351, 3, 1, player.getWorld(), event.getBlock().getLocation());
             ins.sendInfo(ChatColor.YELLOW + "Drop", ChatColor.YELLOW + "カカオ をドロップしました");
             }*/

        } else if (rand == 328) {
            rand = Rand(Parameter328.DropRate * (id - 1));
            if (rand == 328) {
                ins.gainMine(Parameter328.jackpot);
                ins.sendInfo(ChatColor.YELLOW + "Drop", "ジャックポットゲット!!!" + Parameter328.jackpot + " Mine!!!");
                MySqlProcessing.ShopDealingsLog(ins.getName(), "JP(" + Parameter328.jackpot + ")", 10000, 0, 1, 0);
                plugin.broadcastMessage("[" + ChatColor.YELLOW + "Drop" + ChatColor.WHITE + "] " + ChatColor.YELLOW + event.getPlayer().getName() + " 様が " + ChatColor.GOLD + " ジャックポットをゲットしました!!!!!");
                plugin.broadcastMessage("[" + ChatColor.YELLOW + "Drop" + ChatColor.WHITE + "] " + ChatColor.YELLOW + event.getPlayer().getName() + " は " + ChatColor.GOLD + " " + Parameter328.jackpot + ChatColor.GREEN + " Mine ゲットしました!!!!");
                //plugin.broadcastNotificationMessage(ins.getName()+" get drop mine.", color.AQUA+"Jackpot get!! ("+color.YELLOW+Parameter328.jackpot+color.GREEN+"Mine"+color.YELLOW+")", Material.DIAMOND_BLOCK);
                plugin.sendTwitter("[ドロップ] " + event.getPlayer().getName() + " 様がジャックポットを当てました！！  ジャックポット金額 " + Parameter328.jackpot + " Mine.  おめでとうございます。");
                //初期化
                Parameter328.jackpot = 3000000;
            } else if (rand == 333 || rand == 500) {
                long mine;
                rand = Rand(Parameter328.DropRate * id);
                if (rand == 333) {
                    mine = 328000;
                } else {
                    mine = 100000;
                }
                ins.gainMine(mine);
                ins.sendInfo(ChatColor.YELLOW + "Drop", mine + " Mine ゲット!!!");
                plugin.broadcastMessage("[" + ChatColor.YELLOW + "Drop" + ChatColor.WHITE + "] " + ChatColor.YELLOW + event.getPlayer().getName() + " 様が " + ChatColor.GOLD + " " + mine + ChatColor.GREEN + " Mine ゲットしました!!!!!");
                //plugin.broadcastNotificationMessage(ins.getName()+" get drop mine.", color.AQUA+"Mine get!! ("+color.YELLOW+mine+color.GREEN+"Mine"+color.YELLOW+")", Material.GOLD_BLOCK);
                plugin.sendTwitter("[ドロップ] " + event.getPlayer().getName() + " 様が " + mine + " Mine 当てました！！　おめでとうございます。");
            } else {
                ItemDrop(Material.GOLDEN_APPLE.getId(), 0, 1, player.getWorld(), event.getBlock().getLocation());
                ins.sendInfo(ChatColor.YELLOW + "Drop", ChatColor.YELLOW + "金リンゴをドロップしました");
            }
        }
    }

    public static void ItemDrop(int id, int type, int ammount, World wr, Location lo) {
        final ItemStack is = new ItemStack(id);
        int r = 0;
        int rr = 0;
        boolean rrr = false;
        if (ammount > 64) {
            r = ammount / 64;
            rr = ammount % 64;
            rrr = true;
        }
        is.setDurability((short)type);
        lo.setY(lo.getY() + 1);
        if (!rrr) {
            is.setAmount(ammount);
            wr.dropItem(lo, is);
        } else {
            for (int i = 0; i < r; i++) {
                is.setAmount(64);
                wr.dropItem(lo, is);
            }
            is.setAmount(rr);
            wr.dropItem(lo, is);
        }
    }

    public static int Rand(int ran) {
        return (int)(Math.random() * ran);
    }
}

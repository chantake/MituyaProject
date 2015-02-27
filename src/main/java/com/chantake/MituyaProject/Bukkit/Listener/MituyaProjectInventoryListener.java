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

import com.chantake.MituyaProject.Gachapon.GachaponDataManager;
import com.chantake.MituyaProject.MituyaProject;
import com.chantake.MituyaProject.Player.PlayerInstance;
import com.chantake.MituyaProject.Tool.Tools;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

/**
 * MituyaProject Inventory listener
 *
 * @author ezura573
 */
public class MituyaProjectInventoryListener implements Listener {

    private final MituyaProject plugin;
    public ChatColor color;

    public MituyaProjectInventoryListener(final MituyaProject plugin) {
        this.plugin = plugin;
    }

    // インベントリオープンイベント
    @EventHandler
    public void onInventoryOpenEvent(InventoryOpenEvent event) {
        InventoryType invType = event.getInventory().getType();

        // 鉄床
        if (invType == InventoryType.ANVIL) {
        }
    }

    // インベントリクリックイベント
    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        InventoryType invType = event.getInventory().getType();
         InventoryType.SlotType slotType;
        // 鉄床
        if (invType == InventoryType.ANVIL) {
            slotType = event.getSlotType();
            if (slotType == InventoryType.SlotType.CONTAINER) {
                if (event.getSlot() == 9) {
                    // なぜかCONTAINERのSlot[9]が取り出し口（Bukkitのバグ？)
                    CheckRename(event); // 名前変更チェク処理

                }
            } else if (slotType == InventoryType.SlotType.RESULT) {
                // 本来はRESULTのはず(将来Bukkitが修正された時用)
                CheckRename(event);     // 名前変更チェク処理
            }
        }
        
        if(invType == InventoryType.CRAFTING || invType == InventoryType.WORKBENCH)
        {
            slotType = event.getSlotType();
            if (slotType == InventoryType.SlotType.RESULT) {
                CheckPackBookCopy(event);
            }
             
        }
        

    }

    //本のコピー元がItemパックだった場合キャンセルして、メッセージを送信する。
    private void CheckPackBookCopy(InventoryClickEvent event) {
        PlayerInstance pi = this.plugin.getInstanceManager().getInstance(event.getWhoClicked().getName());
        
        Inventory iv = event.getInventory();
        if (iv == null) {
            return;
        }

        if (iv.getSize() == 0) {
            return;
        }
        
        if(this.plugin.getPackBookManager().isPackBook( event.getCurrentItem())){
            pi.sendAttention("この本は著作権保護されているため複製出来ません。");
            event.setCancelled(true);
        }     
    }
    

// 変更するアイテムと名前をチェックして、３２８ガチャ関連のアイテムの場合は変更をキャンセルする
    private void CheckRename(InventoryClickEvent event) {
        PlayerInstance pi = this.plugin.getInstanceManager().getInstance(event.getWhoClicked().getName());

        Inventory iv = event.getInventory();
        if (iv == null) {
            return;
        }

        if (iv.getSize() == 0) {
            return;
        }

        String old_name = Tools.GetItemName(iv.getItem(0));
        String new_name = Tools.GetItemName(event.getCurrentItem());

        if (event.getCurrentItem().getType() != Material.AIR) {
            //名前の変更チェック
            if (!old_name.equals(new_name)) {
                if (GachaponDataManager.isGachaponItemNameCoincident(old_name)) {
                    pi.sendAttention("このアイテムの名称は変更出来ません。");
                    this.showExplotion(pi);
                    event.setCancelled(true);
                    return;
                }

                if (GachaponDataManager.isGachaponItemNameCoincident(new_name)) {
                    pi.sendAttention("アイテムの偽造は犯罪です！！");
                    plugin.broadcastMessage(ChatColor.AQUA + "<<<328ガチャインフォメーション>>>");

                    plugin.broadcastMessage(ChatColor.RED
                            + pi.getName()
                            + "さんが"
                            + "「"
                            + new_name
                            + "」を偽造しようとしました。");
                    this.showExplotion(pi);
                    event.setCancelled(true);
                    return;
                }

                if (GachaponDataManager.isGachaponItemName(new_name)) {
                    pi.sendAttention("このアイテム名称には変更出来ません。");
                    this.showExplotion(pi);
                    event.setCancelled(true);
                    return;
                }
            }
            
            //修理可否チェック
            if(GachaponDataManager.isDenyRepair(old_name)) {
                pi.sendAttention("このアイテムは修理できません。");
                this.showExplotion(pi);
                event.setCancelled(true);
                return;
            }
        }
    }
    
    private void showExplotion( PlayerInstance pi){
        try{
           pi.getPlayer().getWorld().createExplosion(pi.getLocation(),0,false);
       }catch(Exception ex){
       }       
    }

}

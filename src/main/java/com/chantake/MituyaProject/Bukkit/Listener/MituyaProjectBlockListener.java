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
import com.chantake.MituyaProject.Util.Tools;
import com.chantake.MituyaProject.World.DropItem;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;

/**
 * MituyaProject block listener
 *
 * @author chantake
 */
public class MituyaProjectBlockListener implements Listener {

    private final MituyaProject plugin;
    public ChatColor color;

    public MituyaProjectBlockListener(final MituyaProject plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) throws PlayerOfflineException {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        final PlayerInstance ins = plugin.getInstanceManager().getInstance(event.getPlayer());
        //経験値
        event.setExpToDrop((int)(event.getExpToDrop() * Parameter328.Exp));
        /*if (!ins.getMainWorldInvite() && player.getWorld().getName().equals("world")) {//招待がない場合
         ins.sendAttention("このワールドの権限がありません　招待を貰う必要があります");
         event.setCancelled(true);
         } else */
        if (block.getType() == Material.MOB_SPAWNER) {//モブースポーン
            ItemStack itemInHand = player.getItemInHand();
            if (plugin.canBuild(player, block)) {
                if (itemInHand.getType() == Material.DIAMOND_PICKAXE && itemInHand.getEnchantmentLevel(Enchantment.SILK_TOUCH) != 0) {
                    if (Tools.Rand(10) != 0) {
                        Location bl = block.getLocation();
                        bl.setY(bl.getY() + 1);
                        ItemStack is = new ItemStack(Material.MOB_SPAWNER);//アイテム
                        is.setAmount(1);
                        bl.getWorld().dropItem(bl, is);//ドロップ
                    }
                    /*if (itemInHand.getDurability() < 150) {
                     event.getPlayer().getItemInHand().setType(Material.AIR);
                     } else {
                     event.getPlayer().getItemInHand().setDurability((short)(itemInHand.getDurability() - 150));
                     }*/
                } else {
                    ins.sendAttention("シルクタッチがついたダイアモンドピッケルでのみ回収できます.");
                }
            } else {
                event.setCancelled(true);
                ins.sendAttention("このスポーンブロックは保護されています.");
            }
        } else if (block.getType() == Material.ICE) {//氷
            ItemStack itemInHand = event.getPlayer().getItemInHand();
            if (plugin.canBuild(player, block) && itemInHand.getEnchantmentLevel(Enchantment.SILK_TOUCH) != 0
                    && itemInHand.getType().toString().endsWith("PICKAXE")) {
                block.setType(Material.AIR);
                Location bl = block.getLocation();
                bl.setY(bl.getY() + 1);
                ItemStack is = new ItemStack(Material.ICE);//アイテム
                is.setAmount(1);
                block.getWorld().dropItem(bl, is);//ドロップ
            }
        } else if (block.getState() instanceof Sign) {
            Sign sign = (Sign)event.getBlock().getState();
            //チェストショップの場合
            if (plugin.getChestShopManager().isChestShopData(sign)) {
                plugin.getChestShopManager().removeChestShopData(sign);
                ins.sendInfo(ChatColor.RED + "Shop", ChatColor.YELLOW + "チェストショップを削除しました。");
            }
        } else if (block.getType() == Material.NOTE_BLOCK) {//ﾉｰﾄブロック
            if (plugin.jingleNoteManager.JiggleNoteCheck(event.getBlock().getLocation()))//JiggleNoteで再生しているﾉｰﾄブロックか確認
            {
                //ﾉｰﾄブロックを削除
                plugin.jingleNoteManager.stop(event.getBlock().getLocation());
                //キャンセル
                event.setCancelled(true);
            }
        } else if (block.getType() == Material.SPONGE) {//スポンジ
            event.getBlock().setType(Material.AIR);
        } else {
            DropItem.onBlockBreak(plugin, event, ins);
        }
    }

    @EventHandler
    public void onBlockDamage(BlockDamageEvent event) {
        final PlayerInstance ins = plugin.getInstanceManager().getInstance(event.getPlayer());
        if (event.getBlock().getState() instanceof Sign) {
            final Sign sign = (Sign)event.getBlock().getState();
            if (sign.getLine(0).equalsIgnoreCase(Parameter328.Sign_Command) || sign.getLine(0).equalsIgnoreCase(Parameter328.Sign_Command_Japanese)) {
                for (int i = 1; i < sign.getLines().length; i++) {
                    if (sign.getLine(i).length() > 1) {
                        if (sign.getLine(i).charAt(0) == '/') {
                            plugin.handleCommand(event.getPlayer(), sign.getLine(i));
                        }
                    }
                }
            }
        } else if (event.getBlock().getLocation().getBlock() == ins.getHtblock()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onSignChange(SignChangeEvent e) throws PlayerOfflineException {
        final String line = e.getLine(0).toLowerCase();
        /*
         * if (line.indexOf("deposit items") != -1) { e.setLine(0,
         * "This command cannot be used. "); } else
         */
        if (line.equalsIgnoreCase(Parameter328.Sign_Command) || line.equalsIgnoreCase(Parameter328.Sign_Command_Japanese)) {
            plugin.getInstanceManager().getInstance(e.getPlayer()).sendSuccess("サインコマンドを作成しました");
        } else {
            for (int i = 0; i < 4; i++) {
                if (e.getLine(i).isEmpty()) {
                    continue;
                }
                final String[] splitLine = e.getLine(i).split("&");
                String newLine = splitLine[0];
                for (int j = 1; j < splitLine.length; j++) {
                    // 0123456789abcdef 前はコレ
                    if (splitLine[j].length() == 0 || "0123456789abcdefklmnor".indexOf(splitLine[j].toLowerCase().charAt(0)) == -1 || splitLine[j].length() <= 1) {
                        newLine += "#";
                    } else {
                        newLine += "\u00A7";
                    }
                    newLine += splitLine[j];
                }
                e.setLine(i, newLine);
            }
        }
    }
}

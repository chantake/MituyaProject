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
package com.chantake.MituyaProject.World.SignItemChest;

import com.chantake.MituyaProject.MituyaProject;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 *
 * @author inku
 */
public class ItemChest {
    //*****Plugin*****//

    MituyaProject plugin;
    //****************//
    //*****String*****//
    List<String> ids = new ArrayList<>();
    String splits = ",";
    String perm = "権限あると思った？残念ありませんでした!!!!";//チェスト権限がなかったとき
    String chnot = "チェスト先生…いいやつだった。。。";//チェストがなかったとき
    //****************//
    //*****Boolean****//
    boolean itemselect = false;
    //*****BlockFace**//
    BlockFace[] shopFaces = {BlockFace.SELF, BlockFace.DOWN, BlockFace.UP, BlockFace.EAST, BlockFace.NORTH, BlockFace.WEST, BlockFace.SOUTH};
    BlockFace[] chestFaces = {BlockFace.EAST, BlockFace.NORTH, BlockFace.WEST, BlockFace.SOUTH};
    //****************//

    public ItemChest(MituyaProject pl) {
        plugin = pl;
    }
    //整頓 -あとデバック
    //インベントリ移植 -あとでバック

    /**
     * アイテム移動看板
     *
     * @author いんく
     * @param pl クリックしたプレーヤー
     * @param blo クリックブロック
     * @return
     */
    public boolean doEvent(Player pl, Block blo) {
        Sign si = (Sign)blo.getState();//看板
        String line0 = si.getLine(0);

        if (line0.equals("[Receive Chest]") || line0.equals("[ReceiveChest]") || line0.equals("[Chest]") || line0.equals("[chest]") || line0.equals("[RC]") || line0.equals("[rc]")) {//とりあえず名前思いつかないからこれで
            if (!line0.equals("[Receive Chest]")) {//Receive Chestに統一
                si.setLine(0, "[Receive Chest]");
                si.update();
            }
            List<Entity> iit = getEntity(si.getLocation());

            String line3 = si.getLine(3);
            if (!line3.equals("")) {
                String[] st = line3.split(splits);
                for (String s : st) {
                    try {
                        String[] ss = s.split(":");
                        ids.add(ss[0] + ":" + ss[1]);//ID
                    }
                    catch (Exception e) {
                        ids.add(s + ":#");
                    }
                }
                itemselect = true;//整頓モード
            } else if (iit.size() >= 0) {
                for (Entity enti : iit) {
                    if (enti instanceof ItemFrame) {
                        ItemFrame it = (ItemFrame)enti;
                        ItemStack is = it.getItem();
                        ids.add(is.getTypeId() + ":" + is.getDurability());
                        itemselect = true;//整頓モード
                    }
                }
            }

            List<Chest> chblock = getChest(si);//チェストを取得

            if (chblock.size() <= 0) {
                pl.sendMessage(chnot);
                return false;
            }

            for (Chest ch : chblock) {//チェストループ

                if ((plugin.canBuild(pl, ch.getBlock()))) { //権限チェック
                    PlayerInventory plinv = pl.getInventory();//プレーヤーインベントリ
                    Chest cc = getNeighborChest(ch);
                    if (cc != null) {
                        ch = cc;
                    }
                    Inventory chestinv = ch.getInventory();//チェストインベントリ
                    for (ItemStack stack : plinv.getContents()) {
                        //インベントリに空きがあるか確認
                        if (stack != null) {//アイテムがあるか確認
                            //if (getInventoryCost(ch.getBlockInventory(), stack) >= stack.getAmount()) {
                            if (chestinv.firstEmpty() != -1) {
                                if (itemselect) {
                                    //整頓モード
                                    for (String ii : ids) {
                                        try {
                                            if (((stack.getTypeId() + ":" + stack.getDurability()).equals(ii))) {//リストにあったら移動
                                                if (gainItem(chestinv, stack)) {
                                                    plinv.removeItem(stack);//プレーヤーからアイテムを削除
                                                } else {
                                                    break;
                                                }
                                            } else if (ii.split(":")[1].equals("#") && (stack.getTypeId() + "").equals(ii.split(":")[0])) {
                                                if (gainItem(chestinv, stack)) {
                                                    plinv.removeItem(stack);//プレーヤーからアイテムを削除
                                                } else {
                                                    break;
                                                }
                                            }
                                        }
                                        catch (IllegalArgumentException e) {
                                        }
                                    }
                                } else {
                                    if (gainItem(chestinv, stack)) {
                                        plinv.removeItem(stack);//プレーヤーからアイテムを削除
                                    }
                                }
                            }
                        }
                    }
                    pl.updateInventory();

                } else {//権限なかったら
                    pl.sendMessage(perm);
                }
            }
            return true;
        }
        return false;
    }

    public List<Chest> getChest(Sign sign) {
        List<Chest> ch = new ArrayList<>();
        for (BlockFace bf : shopFaces) {
            Block neighborBlock = sign.getBlock().getRelative(bf);
            if (neighborBlock.getType() == Material.CHEST) {
                ch.add((Chest)neighborBlock.getState());
            }
        }
        return ch;
    }

    public Chest getNeighborChest(Chest chest) {
        for (BlockFace bf : chestFaces) {
            Block neighborBlock = chest.getBlock().getRelative(bf);
            if (neighborBlock.getType() == Material.CHEST) {
                return (Chest)neighborBlock.getState();
            }
        }
        return null;
    }

    public int getInventoryCost(Inventory inventory, ItemStack itemstack) {
        int amari = 0;
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack is = inventory.getItem(i);
            if (is == null) {
                amari += itemstack.getType().getMaxStackSize();
            } else if (is.getType() == itemstack.getType() && is.getDurability() == itemstack.getDurability()) {
                amari += (itemstack.getType().getMaxStackSize() - is.getAmount());
            }
        }
        return amari;
    }

    public List<Entity> getEntity(Location loc) {
        List<Entity> retu = new ArrayList<>();
        for (Entity e : loc.getWorld().getEntities()) {
            Location entityLoc = e.getLocation();
            if (entityLoc.getBlockX() == loc.getBlockX()
                    && entityLoc.getBlockY() == loc.getBlockY()
                    && entityLoc.getBlockZ() == loc.getBlockZ()) {
                retu.add(e);
            }
        }
        return retu;
    }

    public boolean gainItem(Inventory inv, ItemStack itemstack) {
        ItemStack is = itemstack.clone();
        int amount = itemstack.getAmount();
        Inventory inventory = inv;
        if (amount == 0) {
            return false;
        }

        int air = 0;
        int stack;
        int amari = 0;
        int maxstack = itemstack.getMaxStackSize();
        //インベントリの空きチェック
        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) {
                air++;
            }
        }
        if (maxstack >= amount) {
            stack = 1;
        } else {
            stack = amount / maxstack;
            amari = amount % maxstack;
        }
        //インベントリがあいていない
        if (stack > air) {
            return false;
        }

        if (amari > 0 && stack == air) {
            return false;
        }

        if (stack == 1) {
            is.setAmount(amount);
            inventory.addItem(is);
        } else {
            for (int i = 0; i < stack; i++) {
                is.setAmount(maxstack);
                inventory.addItem(is);
            }
            if (amari != 0) {
                is.setAmount(amari);
                inventory.addItem(is);
            }
        }
        return true;
    }
}

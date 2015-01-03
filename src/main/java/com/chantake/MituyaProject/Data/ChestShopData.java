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
package com.chantake.MituyaProject.Data;

import com.chantake.MituyaProject.Tool.Tools;
import com.chantake.MituyaProject.World.Shop.ChestShopManager;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.inventory.ItemStack;

/**
 * ChestShop用のデータクラス
 *
 * @author chantake
 */
public class ChestShopData {

    private int buy = 0;
    private int sell = 0;
    private int amount = 1;
    private short data = 0;
    private int typeid = 0;
    private ItemStack item = null;
    private String key = null;
    private String owner;
    private Sign sign;
    private boolean official = false;

    public ChestShopData(ChestShopManager shop, String key, String owner, int typeid, short data, int amount, int buy, int sell, World world, int x, int y, int z, boolean official) {
        Block block = world.getBlockAt(x, y, z);
        ItemStack is = new ItemStack(typeid, amount, data);
        this.key = key;
        this.item = is;
        this.typeid = typeid;
        this.amount = amount;
        this.buy = buy;
        this.sell = sell;
        this.official = official;
        this.owner = owner;
        if (block != null && block.getState() instanceof Sign) {
            this.init(shop, (Sign)block.getState(), owner, false);
        }
    }

    /**
     * 新規作成
     *
     * @param shop
     * @param sign
     * @param owner
     */
    public ChestShopData(ChestShopManager shop, Sign sign, String owner) {
        this.init(shop, sign, owner, true);
    }

    private void init(ChestShopManager shop, Sign sign, String owner, boolean create) {
        if (create) {
            this.key = shop.getKey(sign);
            this.owner = owner;
            //価格
            String[] price = sign.getLine(2).split(":");
            if (price != null) {
                this.buy = Tools.StringToInteger(price[0]);
                if (price.length > 1) {
                    this.sell = Tools.StringToInteger(price[1]);
                }
            }
            ItemStack is = shop.getItemStack(sign);
            if (is == null) {
                return;
            }
            this.amount = is.getAmount();
            this.item = is;
            this.data = is.getDurability();
            this.typeid = is.getTypeId();
            sign.setLine(0, ChatColor.DARK_RED + this.getOwner());
            sign.setLine(1, ChatColor.WHITE + String.valueOf(this.getAmount()));
            sign.setLine(2, shop.ShopPrice(this));
            sign.setLine(3, ChatColor.DARK_BLUE + shop.getItemName(typeid, data));
            sign.update();
        }
        this.sign = sign;
        shop.setChestShopData(this);
    }

    public String getKey() {
        return this.key;
    }

    public int getBuy() {
        return buy;
    }

    public ItemStack getItem() {
        return item;
    }

    public int getSell() {
        return sell;
    }

    public String getOwner() {
        return owner;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setOfficial(boolean official) {
        if (official) {
            this.setOwner("official");
            this.sign.setLine(0, ChatColor.DARK_RED + this.getOwner());
            this.sign.update();
        }
        this.official = official;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public boolean getOfficial() {
        return this.official;
    }

    public short getData() {
        return data;
    }

    public Sign getSign() {
        return sign;
    }

    public int getTypeid() {
        return typeid;
    }

}

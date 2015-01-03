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

import org.bukkit.inventory.ItemStack;

/**
 *
 * @author chantake
 */
public class CommandShopData {
    //ItemData

    private final ItemStack is;
    //売るとき
    private int buy;
    //買う時
    private int sell;
    //item id
    private final int id;
    //item string id
    private final String string_id;
    //item type
    private final short type;
    //buy 取扱い
    private boolean buy_transaction;
    //sell 取扱い
    private boolean sell_transaction;

    public CommandShopData(int id, short type, int buy, int sell, boolean buy_transaction, boolean sell_transaction) {
        this.buy = buy;
        this.sell = sell;
        this.id = id;
        this.type = type;
        if (type != 0) {
            string_id = id + ":" + type;
        } else {
            string_id = String.valueOf(id);
        }
        this.buy_transaction = buy_transaction;
        this.sell_transaction = sell_transaction;
        is = new ItemStack(id, 1, type);
    }

    public boolean isBuyTransaction() {
        return buy_transaction;
    }

    public boolean isSellTransaction() {
        return sell_transaction;
    }

    public int getBuy() {
        return buy;
    }

    public int getId() {
        return id;
    }

    public String getStringID() {
        return this.string_id;
    }

    public ItemStack getItemStack() {
        return is;
    }

    public int getSell() {
        return sell;
    }

    public short getType() {
        return type;
    }

    public void setBuy(int buy) {
        this.buy = buy;
    }

    public void setSell(int sell) {
        this.sell = sell;
    }

    public void setBuyTransaction(boolean transaction) {
        this.buy_transaction = transaction;
    }

    public void setSellTransaction(boolean transaction) {
        this.sell_transaction = transaction;
    }
}

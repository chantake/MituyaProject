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
package com.chantake.MituyaProject.World.Shop;

import com.chantake.MituyaProject.Bukkit.ItemName;
import com.chantake.MituyaProject.Data.CommandShopData;
import com.chantake.MituyaProject.Exception.PlayerOfflineException;
import com.chantake.MituyaProject.MituyaProject;
import com.chantake.MituyaProject.Player.PlayerInstance;
import com.chantake.MituyaProject.Tool.MySqlProcessing;
import com.chantake.MituyaProject.Tool.Tools;
import com.chantake.mituyaapi.tools.database.JDCConnection;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author chantake
 */
public class CommandShopManager {

    private final MituyaProject plugin;
    //item data
    private final ConcurrentHashMap<String, CommandShopData> item_data = new ConcurrentHashMap<>();

    public CommandShopManager(MituyaProject plugin) {
        this.plugin = plugin;
    }

    public void init() {
        //ロード
        this.loadAllItemDataFromDB();
    }

    /**
     * アイテムを買います
     *
     * @param id itemid:type or item name
     * @param amount item id
     * @param ins PlayerInstance
     * @param player Player
     */
    public void buy(final String id, final int amount, final PlayerInstance ins, final Player player) {
        final CommandShopData itemData = this.getItemData(id);
        //見つからない場合
        if (itemData == null || !itemData.isBuyTransaction() || itemData.getBuy() < 0) {
            ins.sendAttention("アイテムID:" + id + " は見つからないか、現在販売しておりません。");
            return;
        }
        //amount
        if (amount <= 0) {
            ins.sendAttention("個数は1以上を指定してください");
            return;
        }
        //mine
        final int mine = itemData.getBuy() * amount;
        //item name
        final String itemname = plugin.getJapaneseMessage().getItemName(itemData.getStringID());
        ins.sendMineYesNo(-mine, itemname + " を " + amount + "個 購入します 単価:" + itemData.getBuy() + "Mine", new Runnable() {
            @Override
            public void run() {
                try {
                    //お金
                    if (ins.checkMine(-mine) && ins.gainItem(itemData.getItemStack(), amount)) {
                        ins.gainMine(-mine);
                        StringBuilder sb = new StringBuilder();
                        sb.append(ChatColor.AQUA).append(itemname).append("(").append(itemData.getStringID()).append(") ").append(ChatColor.YELLOW).append("を ").append(ChatColor.RED).append(amount).append(ChatColor.YELLOW).append("個 購入しました。");
                        ins.sendInfo(ChatColor.RED + "Shop", sb.toString());
                        MySqlProcessing.ShopDealingsLog(ins.getRawName(), "buy", itemData.getId(), itemData.getType(), amount, mine);
                    }
                }
                catch (PlayerOfflineException ex) {
                }
            }
        });

    }

    /**
     * アイテムを売ります 所持してる量より個数が多い場合は　所持してる個数で売却します
     *
     * @param id アイテムIDもしくはアイテム名
     * @param amount 個数
     * @param ins PlayerInstance
     * @param player Player
     */
    public void sell(final String id, final int amount, final PlayerInstance ins, final Player player) {
        final CommandShopData itemData = this.getItemData(id);
        //見つからない場合
        if (itemData == null || !itemData.isSellTransaction()) {
            ins.sendAttention("アイテムID:" + id + " は見つからないか、現在取り扱っておりません。");
            return;
        }
        //amount
        if (amount <= 0) {
            ins.sendAttention("個数は1以上を指定してください");
            return;
        }
        final PlayerInventory inventory = player.getInventory();

        int items = 0;
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if (item != null && item.getTypeId() == itemData.getId() && item.getDurability() == itemData.getType()) {
                items += inventory.getItem(i).getAmount();
            }
        }
        if (items > amount) {
            items = amount;
        }
        if (items == 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(ChatColor.RED).append(plugin.getJapaneseMessage().getItemName(itemData.getStringID())).append(" がインベントリに無いか、足りません");
            ins.sendInfo(ChatColor.RED + "Shop", sb.toString());
            return;
        }
        final ItemStack is = itemData.getItemStack().clone();
        is.setAmount(items);
        //mine
        final int mine = itemData.getSell() * items;
        //item name
        final String itemname = plugin.getJapaneseMessage().getItemName(itemData.getStringID());
        ins.sendMineYesNo(mine, itemname + " を " + items + "個 売却します 単価:" + itemData.getSell(), new Runnable() {
            @Override
            public void run() {
                //インベントリチェック
                int am = 0;
                for (int i = 0; i < inventory.getSize(); i++) {
                    ItemStack item = inventory.getItem(i);
                    if (item != null && item.getTypeId() == itemData.getId() && item.getDurability() == itemData.getType()) {
                        am += inventory.getItem(i).getAmount();
                    }
                }
                //元より少ない場合
                if (am < is.getAmount()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(ChatColor.RED).append(plugin.getJapaneseMessage().getItemName(itemData.getStringID())).append(" がインベントリに無いか、足りません");
                    ins.sendInfo(ChatColor.RED + "Shop", sb.toString());
                    return;
                }
                //お金
                if (ins.gainMine(mine)) {
                    inventory.removeItem(is);
                    StringBuilder sb = new StringBuilder();
                    sb.append(ChatColor.AQUA).append(itemname).append("(").append(itemData.getStringID()).append(") ").append(ChatColor.YELLOW).append("を ").append(ChatColor.RED).append(is.getAmount()).append(ChatColor.YELLOW).append("個 売りました。");
                    ins.sendInfo(ChatColor.RED + "Shop", sb.toString());
                    MySqlProcessing.ShopDealingsLog(ins.getRawName(), "sell", itemData.getId(), itemData.getType(), is.getAmount(), mine);
                }
            }
        });
    }

    public void price(String id, final PlayerInstance ins) {
        final CommandShopData itemData = this.getItemData(id);
        //見つからない場合
        if (itemData == null || (!itemData.isBuyTransaction() && itemData.isSellTransaction())) {
            ins.sendAttention("アイテムID:" + id + " は見つからないか、現在販売しておりません。");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(ChatColor.AQUA).append(plugin.getJapaneseMessage().getItemName(itemData.getStringID())).append("(").append(itemData.getStringID()).append(") ").append(ChatColor.WHITE).append(" 売値:").append(ChatColor.GOLD);
        if (itemData.isBuyTransaction()) {
            sb.append(itemData.getBuy()).append(ChatColor.GOLD).append("Mine");
        } else {
            sb.append("(購入不可)");
        }
        if (itemData.isSellTransaction()) {
            sb.append(ChatColor.WHITE).append(" 買値:").append(ChatColor.RED).append(itemData.getSell()).append(ChatColor.GREEN).append("Mine");
        } else {
            sb.append("(購入不可)");
        }

        ins.sendInfo(ChatColor.RED + "Shop", sb.toString());
    }

    /**
     * アイテムデータを返します
     *
     * @param id
     * @return 見つからない場合はnullを返します
     */
    public CommandShopData getItemData(String id) {
        String[] ids = id.split(":");
        String itemID;
        //数字じゃない場合
        if (ids.length > 1 && ids[1].equals("0")) {
            itemID = plugin.getJapaneseMessage().getItemID(ids[0]);
        } else if (!Tools.isInteger(ids[0])) {
            itemID = plugin.getJapaneseMessage().getItemID(id);
            if (itemID == null) {
                itemID = String.valueOf(ItemName.getItem(id));
            }
        } else {
            return this.item_data.get(id);
        }

        if (itemID == null) {
            return null;
        } else {
            return this.item_data.get(itemID);
        }
    }

    /**
     * dbからアイテムデータを読み込みます
     */
    public void loadAllItemDataFromDB() {
        try {
            try (JDCConnection con = plugin.getConnection(); PreparedStatement ps = con.prepareStatement("SELECT * FROM `shop_price`"); ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    this.addData(new CommandShopData(rs.getInt("id"), rs.getShort("type"), rs.getInt("buy"), rs.getInt("sell"), ((rs.getByte("buy_transaction") & 0xff) == 1), ((rs.getByte("sell_transaction") & 0xff) == 1)));
                }
            }
        }
        catch (SQLException e) {
            plugin.ErrLog("Missing load cmd item data :" + e);
        }
    }

    private void addData(CommandShopData data) {
        this.item_data.put(data.getStringID(), data);
    }
}

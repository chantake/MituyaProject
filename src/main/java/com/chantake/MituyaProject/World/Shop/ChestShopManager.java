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

import com.chantake.MituyaProject.Data.ChestShopData;
import com.chantake.MituyaProject.Bukkit.ItemName;
import com.chantake.MituyaProject.Exception.PlayerOfflineException;
import com.chantake.MituyaProject.MituyaManager;
import com.chantake.MituyaProject.MituyaProject;
import com.chantake.MituyaProject.Permissions.Rank;
import com.chantake.MituyaProject.Player.PlayerInstance;
import com.chantake.MituyaProject.Tool.MySqlProcessing;
import com.chantake.MituyaProject.Tool.Tools;
import java.util.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

/**
 *
 * @author chantake
 */
public class ChestShopManager extends MituyaManager {

    private final HashMap<String, ChestShopData> shops = new HashMap<>();
    private final ChestShopListener listener = new ChestShopListener(this);
    private final HashMap<MaterialData, ArrayList<ChestShopData>> buy = new HashMap<>();
    private final HashMap<MaterialData, ArrayList<ChestShopData>> sell = new HashMap<>();
    private static final BlockFace[] chestFaces = {BlockFace.EAST, BlockFace.NORTH, BlockFace.WEST, BlockFace.SOUTH};
    private static final BlockFace[] shopFaces = {BlockFace.SELF, BlockFace.DOWN, BlockFace.UP, BlockFace.EAST, BlockFace.NORTH, BlockFace.WEST, BlockFace.SOUTH};

    public ChestShopManager(MituyaProject plugin) {
        super(plugin);
    }

    @Override
    public void init() {
        this.getPlugin().Log("チェストショップを初期化します");
        this.getPlugin().Log("チェストショップデータをロードします");
        MySqlProcessing.addChestShopDataAll(this);
        this.sortsellChestShopDataArrayList();
        this.sortbuyChestShopDataArrayList();
        this.getPlugin().registerEvents(listener);
        this.getPlugin().Log("チェストショップを初期化しました");
    }

    public boolean ChestShop(Sign sign, PlayerInstance ins, boolean rightclick) throws PlayerOfflineException {
        if (this.isChestShopData(sign)) {
            if (rightclick) {
                return this.Buy(sign, ins);
            } else {
                return this.Sell(sign, ins);
            }
        } else {
            if (rightclick && this.isChestShop(sign)) {
                return this.addShop(sign, ins);
            }
        }
        return false;
    }

    public boolean isChestShop(Sign sign) {
        if (sign.getLine(1).length() > 0 && sign.getLine(2).length() > 0 && sign.getLine(3).length() > 0) {
            if (Tools.CheckInteger(sign.getLine(1)) && sign.getLine(2).contains(":")) {
                return true;
            }
        }
        return false;
    }

    public HashMap<String, ChestShopData> getChestShopDatas() {
        return this.shops;
    }

    public ChestShopData getChestShopData(Sign sign) {
        return this.shops.get(this.getKey(sign));
    }

    public boolean isChestShopData(Sign sign) {
        return this.shops.containsKey(this.getKey(sign));
    }

    public void setChestShopData(ChestShopData cd) {
        if (cd.getBuy() > 0) {
            if (!buy.containsKey(cd.getItem().getData())) {
                buy.put(cd.getItem().getData(), new ArrayList<ChestShopData>());
            }
            synchronized (buy.get(cd.getItem().getData())) {
                buy.get(cd.getItem().getData()).add(cd);
                sortbuyChestShopDataArrayList(buy.get(cd.getItem().getData()));
            }
        }
        if (cd.getSell() > 0) {
            if (!sell.containsKey(cd.getItem().getData())) {
                sell.put(cd.getItem().getData(), new ArrayList<ChestShopData>());
            }
            synchronized (sell.get(cd.getItem().getData())) {
                sell.get(cd.getItem().getData()).add(cd);
                sortbuyChestShopDataArrayList(sell.get(cd.getItem().getData()));
            }
        }
        this.shops.put(cd.getKey(), cd);
    }

    public void sortsellChestShopDataArrayList() {
        for (Map.Entry<MaterialData, ArrayList<ChestShopData>> entry : sell.entrySet()) {
            sortsellChestShopDataArrayList(entry.getValue());
        }
    }

    public void sortbuyChestShopDataArrayList() {
        for (Map.Entry<MaterialData, ArrayList<ChestShopData>> entry : buy.entrySet()) {
            sortbuyChestShopDataArrayList(entry.getValue());
        }
    }

    public void sortsellChestShopDataArrayList(ArrayList<ChestShopData> alist) {
        Collections.sort(alist, new Comparator<ChestShopData>() {
            @Override
            public int compare(ChestShopData o1, ChestShopData o2) {
                float o1f = o1.getSell() / o1.getAmount();
                float o2f = o2.getSell() / o2.getAmount();
                if (o1f > o2f) {//降順
                    return -1;
                } else if (o1f < o2f) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
    }

    public void sortbuyChestShopDataArrayList(ArrayList<ChestShopData> alist) {
        Collections.sort(alist, new Comparator<ChestShopData>() {
            @Override
            public int compare(ChestShopData o1, ChestShopData o2) {
                float o1f = o1.getBuy() / o1.getAmount();
                float o2f = o2.getBuy() / o2.getAmount();
                if (o1f > o2f) {//昇順
                    return 1;
                } else if (o1f < o2f) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
    }

    public void removeChestShopData(Sign sign) {
        ChestShopData sd = this.getChestShopData(sign);
        MySqlProcessing.removeChestShop(sd);
        if (buy.containsKey(sd.getItem().getData())) {

            synchronized (buy.get(sd.getItem().getData())) {
                if (buy.get(sd.getItem().getData()).remove(sd)) {
                    if (buy.get(sd.getItem().getData()).isEmpty()) {
                        buy.remove(sd.getItem().getData());
                    }
                }
            }
        }
        if (sell.containsKey(sd.getItem().getData())) {
            synchronized (sell.get(sd.getItem().getData())) {
                if (sell.get(sd.getItem().getData()).remove(sd)) {
                    if (sell.get(sd.getItem().getData()).isEmpty()) {
                        sell.remove(sd.getItem().getData());
                    }
                }
            }
        }
        this.shops.remove(sd.getKey());
    }

    public String getKey(Sign sign) {
        return this.getKey(sign.getX(), sign.getY(), sign.getZ());
    }

    public String getKey(int x, int y, int z) {
        return x + "" + y + "" + z;
    }

    public boolean addShop(Sign sign, PlayerInstance ins) throws PlayerOfflineException {
        Chest ct = this.getShopChest(sign);
        if (ct != null && !this.getPlugin().canBuild(ins.getPlayer(), ct.getBlock())) {
            ins.sendInfo(ChatColor.RED + "Shop", ChatColor.RED + "チェストにアクセスできる権限がありません");
            return false;
        }
        boolean official = sign.getLine(0).toLowerCase().startsWith("official") && ins.hasPermission(Rank.Moderator);
        //ショップを作成
        ChestShopData chestShopData = new ChestShopData(this, sign, ins.getName());
        if (!this.isChestShopData(sign)) {
            ins.sendInfo(ChatColor.RED + "Shop", ChatColor.RED + "ショップ作成に失敗しました");
            return false;
        }
        chestShopData.setOfficial(official);
        MySqlProcessing.saveChestShopData(chestShopData, false);
        ins.sendInfo(ChatColor.RED + "Shop", ChatColor.YELLOW + "ショップを作成しました");
        return true;
    }

    public boolean Sell(Sign sign, PlayerInstance ins) throws PlayerOfflineException {
        if (!ins.isAllowCmd()) {
            return false;
        }
        ChestShopData sd = this.getChestShopData(sign);
        if (sd == null) {
            ins.sendInfo(ChatColor.RED + "Shop", ChatColor.RED + "ショップデータが見つかりません。再登録してください");
            return false;
        }
        Chest chest = this.getShopChest(sign);
        //チェストが見つからない場合
        if (chest == null) {
            ins.sendInfo(ChatColor.RED + "Shop", ChatColor.RED + "チェストが見つかりません");
            return false;
        }
        //チェストにアクセスできてしまう場合
        if (this.getPlugin().canBuild(ins.getPlayer(), chest.getBlock()) && !ins.hasPermission(Rank.GM)) {
            ins.sendInfo(ChatColor.RED + "Shop", ChatColor.RED + "チェストにアクセスできる権限がある場合は買えません");
            return false;
        }
        //価格が正常じゃない場合
        if (sd.getSell() <= 0) {
            ins.sendInfo(ChatColor.RED + "Shop", ChatColor.RED + "価格が0以下なので売ることができません");
            return false;
        }
        //オーナー
        PlayerInstance owner = this.getPlugin().getInstanceManager().getInstance(sd.getOwner());
        Inventory inventory = ins.getPlayer().getInventory();
        Inventory cinventory = chest.getInventory();
        ItemStack item = sd.getItem();
        int id = item.getTypeId();
        short data = item.getDurability();
        int items = 0;
        int start = -1;
        int amount = sd.getAmount();
        item.setAmount(amount);
        //プレーヤーの在庫チェック
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack is = inventory.getItem(i);
            if (is != null && is.getType() == Material.getMaterial(id) && is.getDurability() == data) {
                start = i;
                items += inventory.getItem(i).getAmount();
            }
        }
        //在庫がない場合
        if (items < amount) {
            ins.sendInfo(ChatColor.RED + "Shop", ChatColor.RED + "アイテムを持っていません");
            return false;
        }
        //オーナーと一緒だった場合
        if (sd.getOwner().equals(ins.getName())) {
            ins.sendInfo(ChatColor.RED + "Shop", ChatColor.RED + "自分で売ることはできません");
            return false;
        }
        //チェストの空きチェック
        if (this.getInventoryCost(cinventory, item) < amount) {
            Chest neighbor = this.getNeighborChest(chest);
            if (neighbor == null || this.getInventoryCost(neighbor.getInventory(), item) < amount) {
                ins.sendInfo(ChatColor.RED + "Shop", ChatColor.RED + "チェストが無いか、チェストに空きがありません");
                return false;
            }
            cinventory = neighbor.getInventory();
        }
        //スタック越え
        if (inventory.getItem(start).getAmount() > Material.getMaterial(item.getTypeId()).getMaxStackSize()) {
            ins.sendInfo(ChatColor.RED + "Shop", ChatColor.RED + "アイテムの最大スタック数を超えてるため取引できませんでした");
            return false;
        }
        //アイテムを送る
        if (amount == 1 && start != -1 && inventory.getItem(start).getAmount() == 1) {
            item = inventory.getItem(start);
            item.setAmount(1);
        }
        //プレーヤーから送った分のアイテム削除
        inventory.removeItem(item);
        //チェストにアイテムを送る
        cinventory.addItem(item);
        //オフィシャル
        if (sd.getOfficial()) {
            //アイテム名
            String itemname = ChatColor.BLUE + this.getPlugin().getJapaneseMessage().getItemName(item) + "(" + amount + ")" + ChatColor.AQUA;
            //プレーヤーにMineを送金
            ins.gainMine(sd.getSell());
            //購買完了したというメッセージを送る
            ins.sendInfo(ChatColor.RED + "Shop", ChatColor.AQUA + "オフィシャルショップ" + ChatColor.WHITE + " に、" + itemname + ChatColor.WHITE + " を売りました。");
        } else {
            //オーナーの所持金チェック
            if (!owner.gainMine(-sd.getSell())) {
                ins.sendInfo(ChatColor.RED + "Shop", ChatColor.RED + "オーナーの所持金がありません");
                return false;
            }
            //アイテム名
            String itemname = ChatColor.BLUE + ItemName.getItemName(item) + "(" + amount + ")" + ChatColor.AQUA;
            //プレーヤーにMineを送金
            ins.gainMine(sd.getSell());
            //購買完了したというメッセージを送る
            ins.sendInfo(ChatColor.RED + "Shop", ChatColor.AQUA + owner.getName() + ChatColor.WHITE + " 様に、" + itemname + ChatColor.WHITE + " を売りました。");
            //オーナーにもメッセージを送る（オンラインの場合）
            try {
                if (owner.getPlayer() != null && owner.getPlayer().isOnline()) {
                    owner.sendInfo(ChatColor.RED + "Shop", ChatColor.AQUA + ins.getName() + ChatColor.WHITE + " 様から、" + itemname + ChatColor.WHITE + " を買いました。");
                }
            }
            catch (PlayerOfflineException playerOfflineException) {
            }
        }
        return true;
    }

    public boolean Buy(Sign sign, PlayerInstance ins) throws PlayerOfflineException {
        if (!ins.isAllowCmd()) {
            return false;
        }
        ChestShopData sd = this.getChestShopData(sign);
        if (sd == null) {
            ins.sendInfo(ChatColor.RED + "Shop", ChatColor.RED + "ショップデータが見つかりません。再登録してください");
            return false;
        }
        Chest chest = this.getShopChest(sign);
        //チェストが見つからない場合
        if (chest == null) {
            ins.sendInfo(ChatColor.RED + "Shop", ChatColor.RED + "チェストが見つかりません");
            return false;
        }
        //チェストにアクセスできてしまう場合
        if (this.getPlugin().canBuild(ins.getPlayer(), chest.getBlock()) && !ins.hasPermission(Rank.GM)) {
            ins.sendInfo(ChatColor.RED + "Shop", ChatColor.RED + "チェストにアクセスできる権限がある場合は買えません");
            return false;
        }
        //価格が正常じゃない場合
        if (sd.getBuy() <= 0) {
            ins.sendInfo(ChatColor.RED + "Shop", ChatColor.RED + "価格が0以下なので買うことができません");
            return false;
        }
        //オーナー
        PlayerInstance owner = this.getPlugin().getInstanceManager().getInstance(sd.getOwner());
        Inventory inventory = chest.getInventory();
        ItemStack item = sd.getItem();
        int id = item.getTypeId();
        short data = item.getDurability();
        int items = 0;
        int amount = sd.getAmount();
        item.setAmount(amount);
        int start = -1;
        //チェスト内の在庫チェック
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack is = inventory.getItem(i);
            if (is != null && is.getType() == Material.getMaterial(id) && is.getDurability() == data) {
                start = i;
                items += inventory.getItem(i).getAmount();
            }
        }
        //在庫がない場合
        if (items < amount) {
            Chest neighbor = this.getNeighborChest(chest);
            boolean c = false;
            if (neighbor != null) {
                inventory = neighbor.getInventory();
                items = 0;
                //チェスト内の在庫チェック
                for (int i = 0; i < inventory.getSize(); i++) {
                    ItemStack is = inventory.getItem(i);
                    if (is != null && is.getType() == Material.getMaterial(id) && is.getDurability() == data) {
                        start = i;
                        items += inventory.getItem(i).getAmount();
                    }
                }
                if (!(items < amount)) {
                    c = true;
                }
            }
            if (!c) {
                ins.sendInfo(ChatColor.RED + "Shop", ChatColor.RED + "在庫がありません");
                return false;
            }
        }
        //スタックが多い場合
        if (inventory.getItem(start).getAmount() > Material.getMaterial(item.getTypeId()).getMaxStackSize()) {
            ins.sendInfo(ChatColor.RED + "Shop", ChatColor.RED + "アイテムの最大スタック数を超えてるため取引できませんでした");
            return false;
        }
        //オーナーと一緒だった場合
        if (sd.getOwner().equals(ins.getName())) {
            ins.sendInfo(ChatColor.RED + "Shop", ChatColor.RED + "自分で買うことはできません");
            return false;
        }
        //インベントリ && 所持金チェック
        if (ins.InventryCheck(id, data, amount) && ins.gainMine(-sd.getBuy())) {
            String itemname = ChatColor.BLUE + this.getPlugin().getJapaneseMessage().getItemName(item) + "(" + amount + ")" + ChatColor.AQUA;
            //アイテムを送る
            if (start != -1 && amount == 1 && inventory.getItem(start).getAmount() == 1) {
                item = inventory.getItem(start);
                item.setAmount(1);
                ins.getPlayer().getInventory().addItem(item);
            } else {
                ins.getPlayer().getInventory().addItem(item);
            }
            //チェストから送った分のアイテム削除
            inventory.removeItem(item);

            ins.getPlayer().updateInventory();
            if (sd.getOfficial()) {
                ins.sendInfo(ChatColor.RED + "Shop", ChatColor.AQUA + "オフィシャルショップ" + ChatColor.WHITE + "から、" + itemname + ChatColor.WHITE + " を買いました。");
            } else {
                //販売者にMineを送金
                owner.gainMine(sd.getBuy());
                //購買完了したというメッセージを送る
                ins.sendInfo(ChatColor.RED + "Shop", ChatColor.AQUA + owner.getName() + ChatColor.WHITE + " 様から、" + itemname + ChatColor.WHITE + " を買いました。");
                //オーナーにもメッセージを送る（オンラインの場合）
                try {
                    if (owner.getPlayer() != null && owner.getPlayer().isOnline()) {
                        owner.sendInfo(ChatColor.RED + "Shop", ChatColor.AQUA + ins.getName() + ChatColor.WHITE + " 様に、" + itemname + ChatColor.WHITE + " を売りました。");
                    }
                }
                catch (PlayerOfflineException playerOfflineException) {
                }
            }
            return true;
        }
        return false;
    }

    public ItemStack getItemStack(Sign sign) {
        if (sign.getLine(3) == null) {
            return null;
        }
        int id;
        short type = 0;
        String[] split = sign.getLine(3).split(":");
        if (!Tools.CheckInteger(split[0])) {
            id = ItemName.getItem(split[0]);
        } else {
            id = Tools.StringToInteger(split[0]);
            if (id == 0) {
                Chest chest = this.getShopChest(sign);
                if (chest != null) {
                    for (int i = 0; i < chest.getInventory().getSize(); i++) {
                        ItemStack is = chest.getInventory().getItem(i);
                        if (is != null && is.getType() != Material.AIR) {
                            id = is.getTypeId();
                            type = is.getDurability();
                            break;
                        }
                    }
                    if (id == 0) {
                        Chest neighbor = this.getNeighborChest(chest);
                        if (neighbor != null) {
                            for (int i = 0; i < neighbor.getInventory().getSize(); i++) {
                                ItemStack is = neighbor.getInventory().getItem(i);
                                if (is != null && is.getType() != Material.AIR) {
                                    id = is.getTypeId();
                                    type = is.getDurability();
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (id == 0) {
            return null;
        }
        if (split.length > 1 && Tools.CheckInteger(split[1])) {
            type = (short)Tools.StringToInteger(split[1]);
        }
        int amount = 1;
        if (sign.getLine(1) != null && Tools.CheckInteger(sign.getLine(1))) {
            amount = Tools.StringToInteger(sign.getLine(1));
            Material m = Material.getMaterial(id);
            if (amount > m.getMaxStackSize()) {
                amount = m.getMaxStackSize();
            }
        }
        ItemStack is = new ItemStack(id, amount, type);
        return is;
    }

    /**
     * 看板からチェストを取得します
     *
     * @param sign
     * @return
     */
    public Chest getShopChest(Sign sign) {
        for (BlockFace bf : shopFaces) {
            Block neighborBlock = sign.getBlock().getRelative(bf);
            if (neighborBlock.getType() == Material.CHEST) {
                return (Chest)neighborBlock.getState();
            }
        }
        return null;
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

    public String getItemName(int typeid, short data) {
        String item = ItemName.getItemName(typeid, data);
        if (item.length() > 15) {
            return typeid + ":" + data;
        }
        return item;
    }

    public String ShopPrice(ChestShopData sd) {
        String def = ChatColor.YELLOW + "B " + Tools.IntegerToShortString(sd.getBuy()) + ":" + Tools.IntegerToShortString(sd.getSell()) + " S";
        //15文字以内
        if (def.length() <= 15) {
            return def;
        }
        def = def.replaceAll(".0:", ":");
        def = def.replaceAll(".0 S", " S");
        int size = def.length() - 15;
        if (size <= 2) {
            def = def.replaceAll(" ", "");
        } else if (size <= 4) {
            def = def.replaceAll(" ", "");
            def = def.replaceAll("B ", "");
            def = def.replaceAll(" S", "");
        } else {
            def = def.replaceAll(" ", "");
            def = def.replaceAll(ChatColor.YELLOW + "B ", "");
            def = def.replaceAll(" S", "");
        }
        return def;
    }

    /**
     * コマンドショップ用の売却補助 メッセ―ジの消去のみ
     *
     * @param sd
     * @param hm
     * @param amt
     * @param ins
     * @return
     * @throws PlayerOfflineException
     */
    public int cmdSell(ChestShopData sd, PlayerInstance ins, HashMap<String, Integer[]> hm, int amt) throws PlayerOfflineException {
        if (sd == null) {
            //ins.sendInfo(ChatColor.RED + "Shop", ChatColor.RED + "ショップデータが見つかりません。再登録してください");
            return -1;
        }
        Chest chest = this.getShopChest(sd.getSign());
        //チェストが見つからない場合
        if (chest == null) {
            //ins.sendInfo(ChatColor.RED + "Shop", ChatColor.RED + "チェストが見つかりません");
            return -1;
        }
        //チェストにアクセスできてしまう場合
        if (this.getPlugin().canBuild(ins.getPlayer(), chest.getBlock()) && !ins.hasPermission(Rank.GM)) {
            //ins.sendInfo(ChatColor.RED + "Shop", ChatColor.RED + "チェストにアクセスできる権限がある場合は買えません");
            return -1;
        }
        //価格が正常じゃない場合
        if (sd.getSell() <= 0) {
            //ins.sendInfo(ChatColor.RED + "Shop", ChatColor.RED + "価格が0以下なので売ることができません");
            return -1;
        }
        //オーナー
        PlayerInstance owner = this.getPlugin().getInstanceManager().getInstance(sd.getOwner());
        Inventory inventory = ins.getPlayer().getInventory();
        Inventory cinventory = chest.getInventory();
        ItemStack item = sd.getItem();
        int id = item.getTypeId();
        short data = item.getDurability();
        int items = 0;
        int start = -1;
        int amount = amt;
        item.setAmount(amount);
        //プレーヤーの在庫チェック
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack is = inventory.getItem(i);
            if (is != null && is.getType() == Material.getMaterial(id) && is.getDurability() == data) {
                start = i;
                items += inventory.getItem(i).getAmount();
            }
        }
        //在庫がない場合
        if (items < amount) {
            //ins.sendInfo(ChatColor.RED + "Shop", ChatColor.RED + "アイテムを持っていません");
            amount = items;
            item.setAmount(amount);
        }
        //オーナーと一緒だった場合
        if (sd.getOwner().equals(ins.getName())) {
            //ins.sendInfo(ChatColor.RED + "Shop", ChatColor.RED + "自分で売ることはできません");
            return -1;
        }
        int invcost = this.getInventoryCost(cinventory, item);
        int invcost2;
        //チェストの空きチェック
        if (invcost < 1) {
            Chest neighbor = this.getNeighborChest(chest);
            if (neighbor != null) {
                invcost2 = this.getInventoryCost(neighbor.getInventory(), item);
                if (invcost2 < 1) {
                    cinventory = neighbor.getInventory();
                    invcost = invcost2;
                }
            }
        }
        if (invcost < amount) {
            amount = invcost;
            item.setAmount(amount);
        }
        //スタック越え
        if (inventory.getItem(start).getAmount() > Material.getMaterial(item.getTypeId()).getMaxStackSize()) {
            //ins.sendInfo(ChatColor.RED + "Shop", ChatColor.RED + "アイテムの最大スタック数を超えてるため取引できませんでした");
            return -1;
        }
        //アイテムを送る
        if (amount == 1 && start != -1 && inventory.getItem(start).getAmount() == 1) {
            item = inventory.getItem(start);
            item.setAmount(1);
        }
        //プレーヤーから送った分のアイテム削除
        inventory.removeItem(item);
        //チェストにアイテムを送る
        cinventory.addItem(item);
        int cmdsell = (int)((sd.getSell() / sd.getAmount()) * 0.9);
        if (cmdsell == 0) {
            cmdsell++;
        }
        cmdsell = cmdsell * amount;
        //オフィシャル
        if (sd.getOfficial()) {
            //アイテム名
            String itemname = ChatColor.BLUE + ItemName.getItemName(item) + "(" + amount + ")" + ChatColor.AQUA;
            //プレーヤーにMineを送金
            ins.gainMine(cmdsell, false);
            //購買完了したというメッセージを送る
            //ins.sendInfo(ChatColor.RED + "Shop", ChatColor.AQUA + "オフィシャルショップ" + ChatColor.WHITE + " に、" + itemname + ChatColor.WHITE + " を売りました。");
            if (hm.containsKey("オフィシャルショップ")) {
                Integer[] get = hm.get("オフィシャルショップ");
                get[0] += amount;
                get[1] += cmdsell;
            } else {
                hm.put("オフィシャルショップ", new Integer[]{amount, cmdsell});
            }
        } else {
            //オーナーの所持金チェック
            if (!owner.gainMine(-sd.getSell(), false)) {
                //ins.sendInfo(ChatColor.RED + "Shop", ChatColor.RED + "オーナーの所持金がありません");
                return -1;
            }
            //アイテム名
            String itemname = ChatColor.BLUE + ItemName.getItemName(item) + "(" + amount + ")" + ChatColor.AQUA;
            //プレーヤーにMineを送金
            ins.gainMine((int)(cmdsell), false);
            //購買完了したというメッセージを送る
            //ins.sendInfo(ChatColor.RED + "Shop", ChatColor.AQUA + owner.getName() + ChatColor.WHITE + " 様に、" + itemname + ChatColor.WHITE + " を売りました。");
            //オーナーにもメッセージを送る（オンラインの場合）
            //owner.sendInfo(ChatColor.RED + "Shop", ChatColor.AQUA + ins.getName() + ChatColor.WHITE + " 様から、" + itemname + ChatColor.WHITE + " を買いました。");
            if (hm.containsKey(owner.getName())) {
                Integer[] get = hm.get(owner.getName());
                get[0] += amount;
                get[1] += cmdsell;
            } else {
                hm.put(owner.getName(), new Integer[]{amount, cmdsell});
            }
        }
        return amount;
    }

    /**
     * コマンドショップ用の購入補助 メッセ―ジの消去のみ
     *
     * @param plugin
     * @param sd
     * @param ins
     * @param hm
     * @param amt
     * @return
     * @throws PlayerOfflineException
     */
    public int cmdBuy(MituyaProject plugin, ChestShopData sd, PlayerInstance ins, HashMap<String, Integer[]> hm, int amt) throws PlayerOfflineException {
        if (sd == null) {
            //ins.sendInfo(ChatColor.RED + "Shop", ChatColor.RED + "ショップデータが見つかりません。再登録してください");
            return -1;
        }
        Chest chest = this.getShopChest(sd.getSign());
        //チェストが見つからない場合
        if (chest == null) {
            //ins.sendInfo(ChatColor.RED + "Shop", ChatColor.RED + "チェストが見つかりません");
            return -1;
        }
        //チェストにアクセスできてしまう場合
        if (plugin.canBuild(ins.getPlayer(), chest.getBlock()) && !ins.hasPermission(Rank.GM)) {
            //ins.sendInfo(ChatColor.RED + "Shop", ChatColor.RED + "チェストにアクセスできる権限がある場合は買えません");
            return -1;
        }
        //価格が正常じゃない場合
        if (sd.getBuy() <= 0) {
            //ins.sendInfo(ChatColor.RED + "Shop", ChatColor.RED + "価格が0以下なので買うことができません");
            return -1;
        }
        //オーナー
        PlayerInstance owner = plugin.getInstanceManager().getInstance(sd.getOwner());
        Inventory inventory = chest.getInventory();
        ItemStack item = sd.getItem();
        int id = item.getTypeId();
        short data = item.getDurability();
        int items = 0;
        int amount = amt;
        item.setAmount(amount);
        int start = -1;
        //チェスト内の在庫チェック
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack is = inventory.getItem(i);
            if (is != null && is.getType() == Material.getMaterial(id) && is.getDurability() == data) {
                start = i;
                items += inventory.getItem(i).getAmount();

            }
        }
        //在庫がない場合
        if (items < amount) {
            Chest neighbor = this.getNeighborChest(chest);
            boolean c = false;
            if (neighbor != null) {
                inventory = neighbor.getInventory();
                items = 0;
                //チェスト内の在庫チェック
                for (int i = 0; i < inventory.getSize(); i++) {
                    ItemStack is = inventory.getItem(i);
                    if (is != null && is.getType() == Material.getMaterial(id) && is.getDurability() == data) {
                        start = i;
                        items += inventory.getItem(i).getAmount();
                    }
                    if (is.getAmount() > Material.getMaterial(item.getTypeId()).getMaxStackSize()) {
                        //ins.sendInfo(ChatColor.RED + "Shop", ChatColor.RED + "アイテムの最大スタック数を超えてるため取引できませんでした");
                        return -1;
                    }
                }
                if (!(items < amount)) {
                    c = true;
                }
            }
            if (!c) {
                amount = items;
                item.setAmount(amount);
            }
        }
        //オーナーと一緒だった場合
        if (sd.getOwner().equals(ins.getName())) {
            //ins.sendInfo(ChatColor.RED + "Shop", ChatColor.RED + "自分で買うことはできません");
            return -1;
        }
        int cmdbuy = (int)((sd.getBuy() / sd.getAmount()) * 1.1);
        if (cmdbuy == 0) {
            cmdbuy++;
        }
        cmdbuy = cmdbuy * amount;
        //インベントリ && 所持金チェック
        if (ins.InventryCheck(id, data, amount) && ins.gainMine(-cmdbuy, false)) {
            //アイテムを送る
            if (start != -1 && amount == 1 && inventory.getItem(start).getAmount() == 1) {
                item = inventory.getItem(start);
                item.setAmount(1);
                ins.getPlayer().getInventory().addItem(item);
            } else {
                ins.getPlayer().getInventory().addItem(item);
            }
            //チェストから送った分のアイテム削除
            inventory.removeItem(item);

            ins.getPlayer().updateInventory();
            if (sd.getOfficial()) {
                //ins.sendInfo(ChatColor.RED + "Shop", ChatColor.AQUA + "オフィシャルショップ" + ChatColor.WHITE + "から、" + itemname + ChatColor.WHITE + " を買いました。");
                if (hm.containsKey("オフィシャルショップ")) {
                    Integer[] get = hm.get("オフィシャルショップ");
                    get[0] += amount;
                    get[1] += cmdbuy;
                } else {
                    hm.put("オフィシャルショップ", new Integer[]{amount, cmdbuy});
                }
            } else {
                //販売者にMineを送金
                owner.gainMine(sd.getBuy(), false);
                //購買完了したというメッセージを送る
                //ins.sendInfo(ChatColor.RED + "Shop", ChatColor.AQUA + owner.getName() + ChatColor.WHITE + " 様から、" + itemname + ChatColor.WHITE + " を買いました。");
                //オーナーにもメッセージを送る（オンラインの場合）
                //owner.sendInfo(ChatColor.RED + "Shop", ChatColor.AQUA + ins.getName() + ChatColor.WHITE + " 様に、" + itemname + ChatColor.WHITE + " を売りました。");
                if (hm.containsKey(owner.getName())) {
                    Integer[] get = hm.get(owner.getName());
                    get[0] += amount;
                    get[1] += cmdbuy;
                } else {
                    hm.put(owner.getName(), new Integer[]{amount, cmdbuy});
                }
            }
            return amount;
        }
        return -1;
    }

    /*public boolean oldbuy(PlayerInstance ins, int id, byte type, int amount, HashMap<String, Integer[]> hm) throws PlayerOfflineException {
     if (!ShopRate.getItemPriceBuy(id, type)) {
     //this.sendInfo(color.RED + "Shop", color.DARK_RED + "id:" + id + " は見つからないか、取り扱っていません。");
     } else if (amount <= 0) {
     //this.sendInfo(color.RED + "Shop", color.DARK_RED + "個数は1以上にしてください(1~*)");
     } else {
     long p = (long)(ShopRate.getItemBuy(id, type) * 1.1 * amount);//金額
     if (ins.InventryCheck(id, type, amount) && ins.gainMine((int)-p, false)) {//所持金確認
     ins.gainItem(id, type, amount);
     //this.sendInfo(color.RED + "Shop", color.DARK_GREEN + "アイテムを買いました アイテムID:" + id + " タイプ:" + type + " 個数:" + amount);
     //ショップ利用ログを記録
     MySqlProcessing.ShopDealingsLog(ins.getName(), "Buy", id, type, amount, (int)-p);
     //getPlayer().sendMessage(color.GREEN + "[Shop]" + color.DARK_GREEN + "It succeeded in purchase. ");
     if (hm.containsKey("オフィシャルショップ(∞)")) {
     Integer[] get = hm.get("オフィシャルショップ(∞)");
     get[0] += amount;
     get[1] += (int)p;
     } else {
     hm.put("オフィシャルショップ(∞)", new Integer[]{amount, (int)p});
     }
     return true;
     }
     }
     //ins.sendInfo(ChatColor.RED + "Shop", ChatColor.AQUA + "オフィシャルショップ" + ChatColor.WHITE + "から、" + itemname + ChatColor.WHITE + " を買いました。");
     return false;
     }

     public boolean oldsell(PlayerInstance ins, int id, byte type, int amount, HashMap<String, Integer[]> hm) throws PlayerOfflineException {
     if (!ShopRate.getItemPriceSell(id, type)) {
     //ins.sendInfo(ChatColor.RED + "Shop", ChatColor.DARK_RED + "id:" + id + " は見つからないか、取り扱っていません。");
     return false;
     } else if (amount <= 0) {
     //ins.sendInfo(ChatColor.RED + "Shop", ChatColor.DARK_RED + "個数は1以上にしてください(1~*)");
     return false;
     } else {
     int items = 0;
     ItemStack is = new ItemStack(id);
     Inventory inventory = ins.getPlayer().getInventory();
     for (int i = 0; i < inventory.getSize(); i++) {
     ItemStack item = inventory.getItem(i);
     if (item != null && item.getType() == Material.getMaterial(id) && (int)item.getDurability() == type) {
     items += inventory.getItem(i).getAmount();
     }
     }
     if (items > amount) {
     items = amount;
     }
     if (items == 0) {
     ins.sendInfo(ChatColor.RED + "Shop", ChatColor.DARK_RED + "id:" + id + " がインベントリに在りません。");
     return false;
     }
     long p = (long)(ShopRate.getItemSell(id, type) * 0.9 * items);
     is.setDurability((short)type);
     is.setAmount(items);
     inventory.removeItem(is);
     ShopRate.ItemStock(id, type, -items);
     ins.gainMine(p);
     //ins.sendInfo(ChatColor.RED + "Shop", ChatColor.DARK_GREEN + "アイテムを売りました アイテムID:" + id + " タイプ:" + type + " 個数:" + items);
     //ショップ利用ログを記録
     MySqlProcessing.ShopDealingsLog(ins.getName(), "Sell", id, type, items, (int)p);
     //getPlayer().sendMessage(ChatColor.GREEN + "[Shop]" + ChatColor.DARK_GREEN + "It succeeded in purchase. ");
     if (hm.containsKey("オフィシャルショップ(∞)")) {
     Integer[] get = hm.get("オフィシャルショップ(∞)");
     get[0] += amount;
     get[1] += (int)p;
     } else {
     hm.put("オフィシャルショップ(∞)", new Integer[]{amount, (int)p});
     }
     return true;
     }
     }*/
    public HashMap<MaterialData, ArrayList<ChestShopData>> getBuy() {
        return buy;
    }

    public HashMap<MaterialData, ArrayList<ChestShopData>> getSell() {
        return sell;
    }

    public HashMap<String, ChestShopData> getShops() {
        return shops;
    }
}

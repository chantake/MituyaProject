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
package com.chantake.MituyaProject.Gachapon;

import com.chantake.MituyaProject.MituyaProject;
import com.chantake.mituyaapi.tools.database.JDCConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

/**
 *
 * @author ezura573
 */
public class GachaponPhaseData {

    private final MituyaProject plugin;
    private int phase = 0;                                            // フェーズ 
    private int price = 5000;                                         // 一回あたりの価格
    private final List<GachaponCapsuleData> Capsules = new ArrayList<>();  // ガチャのカプセルデータ
    private final List LotteryTable = new ArrayList<>();                   // 抽選用テーブル  
    private final int LotteryTableScale = 10;                               // 抽選テーブルの倍率(分母が小さいと偏りが多くなるため)
    private final List IDList = new ArrayList<>();                         // IDリスト
    private boolean sale = false;                                     // 販売フラグ
    private long SaleNum = 0;                                         // 販売数

    /**
     * コンストラクタ
     *
     * @param plugin　プラグイン
     * @param Phase フェーズナンバー
     */
    public GachaponPhaseData(MituyaProject plugin, int Phase) {
        this.plugin = plugin;
        this.phase = Phase;
    }

    /**
     * 販売可否フラグをセットします。
     *
     * @param s 販売可否フラグ
     */
    public void SetSale(boolean s) {
        if (this.sale != s) {
            boolean saved = false;
            int Value;
            if (s) {
                Value = 1;
            } else {
                Value = 0;
            }

            try {
                GachaponDataManager.SaveConfig("sale", this.phase, Value);
                saved = true;
            }
            catch (SQLException ex) {
                Logger.getLogger(GachaponPhaseData.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (saved) {
                this.sale = s;
            }
        }
    }

    /**
     * 販売可否フラグを取得します
     *
     * @return 販売可否フラグ
     */
    public boolean GetSale() {
        return this.sale;
    }

    /**
     * フェースを取得します。
     *
     * @return フェーズナンバー
     */
    public int GetPhase() {
        return this.phase;
    }

    /**
     * 価格を設定します。
     *
     * @param Price 価格
     */
    public void SetPrice(int Price) {
        if (this.price != Price) {
            boolean saved = false;
            try {
                GachaponDataManager.SaveConfig("price", this.phase, Price);
                saved = true;
            }
            catch (SQLException ex) {
                Logger.getLogger(GachaponPhaseData.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (saved) {
                this.price = Price;
            }
        }
    }

    /**
     * 価格を取得します。
     *
     * @return 価格
     */
    public int GetPrice() {
        return this.price;
    }

    /**
     * カプセルリストを取得します。
     *
     * @return カプセルリスト
     */
    public List<GachaponCapsuleData> GetCapsules() {
        return Capsules;
    }

    /**
     * カプセルの種類数を取得します。
     *
     * @return カプセルの種類数
     */
    public int GetCapsuleVarietyNum() {
        return Capsules.size();
    }

    /**
     * 販売した数量を設定します。
     *
     * @param num
     */
    public void SetSaleNum(long num) {
        this.SaleNum = num;
    }

    /**
     * 販売した数量を取得します。
     *
     * @return
     */
    public long GetSaleNum() {
        return this.SaleNum;
    }

    /**
     * 販売した数量を加算します。
     */
    public void IncSaleNum() {
        this.SaleNum++;
    }

    // <editor-fold defaultstate="collapsed" desc="LoadDataFromDB">
    /**
     * DBからガチャのデータをロードします。
     *
     * @return データのロードに成功したか否か
     * @throws SQLException
     */
    public boolean LoadDataFromDB() throws SQLException {
        boolean ret = false;
        String sql;

        //各保持データのクリア
        Capsules.clear();
        LotteryTable.clear();
        IDList.clear();

        try (JDCConnection con = plugin.getConnection()) {
            sql = "Select * from gachapon_capsule Where `delete` = 0 AND `phase` = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, this.phase);
            ResultSet rs = ps.executeQuery();

            //レコードセットの取得
            rs.getRow();

            // 検索された行数分ループ
            while (rs.next()) {
                //レコードに基づきカプセルデータを設定
                GachaponCapsuleData gcd = new GachaponCapsuleData(plugin);
                gcd.SetId(rs.getInt("id"));                     // カプセルID
                gcd.SetName(rs.getString("name"));              // カプセル名
                gcd.SetRate(rs.getInt("rate"));                 // 抽選レート(全体に対する分子)
                short publish = rs.getShort("publish_flg");     // 当選公表フラグ 
                if (publish == 0) {
                    gcd.SetPublishFlg(false);
                } else {
                    gcd.SetPublishFlg(true);
                }
                short secret = rs.getShort("secret");           // シークレットフラグ
                if (secret == 0) {
                    gcd.SetSecret(false);
                } else {
                    gcd.SetSecret(true);
                }

                //抽選テーブルにリストのインデックス(IDではない)を追加
                for (int i = 0; i < rs.getInt("rate") * LotteryTableScale; i++) {
                    LotteryTable.add(Capsules.size());
                }

                //カプセル追加
                Capsules.add(gcd);

                //IDリスト追加
                IDList.add(rs.getInt("id"));
            }
            rs.close();
            ps.close();

            for (GachaponCapsuleData Capsule : Capsules) {
                int inv_num = 0;
                sql = "select * from gachapon_description Where capsule_id = ?";
                ps = con.prepareStatement(sql);
                ps.setInt(1, Capsule.GetId());
                rs = ps.executeQuery();
                //レコードセットの取得
                rs.getRow();
                // 検索された行数分ループ
                while (rs.next()) {
                    //アイテム生成
                    int item_index = rs.getInt("id");
                    MaterialData md = new MaterialData(rs.getInt("item_id"), rs.getByte("type"));
                    ItemStack is = md.toItemStack(rs.getInt("amount"));
                    short durability = rs.getShort("durability");
                    if (durability != 0) {
                        is.setDurability(durability);
                    }
                    //必要なインベントリ数計算
                    int maxstack = 64;
                    int n1 = rs.getInt("amount") / maxstack;
                    int n2 = rs.getInt("amount") % maxstack;
                    if (n2 > 0) {
                        n1++;
                    }
                    inv_num += n1;
                    //エンチャント情報を付加
                    String enchantment = rs.getString("enchantment");
                    if (enchantment == null) {
                        enchantment = "";
                    }
                    if (!enchantment.equals("")) {
                        //エンチャント情報を分割
                        String[] str1Ary = enchantment.split(",");
                        int ench_num = str1Ary.length / 2;
                        if (str1Ary.length % 2 == 0) // エンチャント情報数は必ず２の倍数で設定されているべき
                        {
                            //分割数分ループ
                            for (int i2 = 0; i2 < ench_num; i2++) {
                                Enchantment ench = GetEnchantmentFromID(Integer.valueOf(str1Ary[i2 * 2]));
                                int level = Integer.valueOf(str1Ary[i2 * 2 + 1]);
                                is.addUnsafeEnchantment(ench, level);
                            }

                            //エンチャント装備の銘入れ処理
                            String item_name = rs.getString("name");
                            if (item_name == null) {
                                item_name = "";
                            }
                            if (!item_name.equals("")) {
                                ItemMeta im = is.getItemMeta();
                                im.setDisplayName(item_name);
                                is.setItemMeta(im);
                            }
                        } else {
                            plugin.getLogger().log(Level.INFO, "\u3000\u3000\uff01\uff01\u8b66\u544a\uff01\uff01\u3000INDEX[{0}]\u306e\u30a8\u30f3\u30c1\u30e3\u30f3\u30c8\u60c5\u5831\u304c\u4e0d\u6b63\u3067\u3059\u3002", item_index);
                        }
                    }
                    //アイテムをカプセルに追加
                    Capsule.GetItems().add(is);
                }
                rs.close();
                ps.close();
                //必要インベントリ数の更新処理
                Capsule.SetUseInventory(inv_num);
                //ブックデータの生成
                Capsule.CreateBookData();
            }

            //抽選テーブルをシャッフル
            int shuffle_num = LotteryTable.size() * LotteryTableScale * 10;
            for (int i = 0; i < shuffle_num; i++) {
                int rnd = (int)(Math.random() * LotteryTable.size());
                int temp = (int)LotteryTable.get(rnd);
                LotteryTable.remove(rnd);
                LotteryTable.add(temp);
            }
        }
        return LoadConfig();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="GetEnchantmentFromID">
    /**
     * エンチャントメントIDからエンチャントメントを取得します。
     *
     * @param エンチャントメントID
     * @return エンチャントメント
     */
    private Enchantment GetEnchantmentFromID(int id) {
        switch (id) {
            case 0:
                return Enchantment.PROTECTION_ENVIRONMENTAL;
            case 1:
                return Enchantment.PROTECTION_FIRE;
            case 2:
                return Enchantment.PROTECTION_FALL;
            case 3:
                return Enchantment.PROTECTION_EXPLOSIONS;
            case 4:
                return Enchantment.PROTECTION_PROJECTILE;
            case 5:
                return Enchantment.OXYGEN;
            case 6:
                return Enchantment.WATER_WORKER;
            case 7:
                return Enchantment.THORNS;
            case 8:
                return Enchantment.DEPTH_STRIDER;
            case 16:
                return Enchantment.DAMAGE_ALL;
            case 17:
                return Enchantment.DAMAGE_UNDEAD;
            case 18:
                return Enchantment.DAMAGE_ARTHROPODS;
            case 19:
                return Enchantment.KNOCKBACK;
            case 20:
                return Enchantment.FIRE_ASPECT;
            case 21:
                return Enchantment.LOOT_BONUS_MOBS;
            case 32:
                return Enchantment.DIG_SPEED;
            case 33:
                return Enchantment.SILK_TOUCH;
            case 34:
                return Enchantment.DURABILITY;
            case 35:
                return Enchantment.LOOT_BONUS_BLOCKS;
            case 48:
                return Enchantment.ARROW_DAMAGE;
            case 49:
                return Enchantment.ARROW_KNOCKBACK;
            case 50:
                return Enchantment.ARROW_FIRE;
            case 51:
                return Enchantment.ARROW_INFINITE;
            case 61:
                return Enchantment.LUCK;
            case 62:
                return Enchantment.LURE;
            default:
                return Enchantment.DURABILITY;
        }
        //return null;
    }
    // </editor-fold>

    private boolean LoadConfig() throws SQLException {
        String sql;
        try (JDCConnection con = plugin.getConnection()) {
            sql = "SELECT `key`,value_i,value_t FROM gachapon_config WHERE phase = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, this.phase);
                //レコードセットの取得
                try (ResultSet rs = ps.executeQuery()) {
                    //レコードセットの取得
                    rs.getRow();

                    // 検索された行数分ループ
                    while (rs.next()) {
                        String key = rs.getString("key").toLowerCase();
                        switch (key) {

                            case "price":
                                this.price = rs.getInt("value_i");
                                plugin.getLogger().log(Level.INFO, "Phase{0} Price = {1}Mine", new Object[]{this.phase, this.price});
                                break;

                            case "sale":
                                if (rs.getInt("value_i") == 0) {
                                    this.sale = false;
                                    plugin.getLogger().log(Level.INFO, "Phase{0} \u8ca9\u58f2\u505c\u6b62", this.phase);
                                } else {
                                    this.sale = true;
                                    plugin.getLogger().log(Level.INFO, "Phase{0} \u8ca9\u58f2\u4e2d", this.phase);
                                }
                                break;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * 抽選によりカプセルを１個抽出します。
     *
     * @return カプセル
     */
    public GachaponCapsuleData GetLotteryCapsule() {
        int rnd = (int)(Math.random() * LotteryTable.size());
        return Capsules.get((int)LotteryTable.get(rnd));
    }
}

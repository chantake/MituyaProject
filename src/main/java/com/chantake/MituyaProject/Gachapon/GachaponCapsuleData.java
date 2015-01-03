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
import com.chantake.MituyaProject.World.Pack.ItemBook;
import com.chantake.mituyaapi.tools.database.JDCConnection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.inventory.ItemStack;

/**
 * 328ガチャ用のカプセルデータクラスです。
 *
 * @author ezura573
 */
public class GachaponCapsuleData {

    private int id = 0;
    private String name = "";
    private int rate = 0;
    private List<ItemStack> Items = new ArrayList<>();
    private boolean publish_flg = false;
    private boolean secret_flg = false;
    private int use_inventory = 0;
    private final MituyaProject plugin;
    private ItemStack BookData = null;

    public GachaponCapsuleData(MituyaProject plugin) {
        this.plugin = plugin;
    }

    /**
     * カプセルIDを設定します。
     *
     * @param id カプセルID
     *
     */
    public void SetId(int id) {
        this.id = id;
    }

    /**
     * カプセルIDを取得します。
     *
     * @return カプセルID
     */
    public int GetId() {
        return this.id;
    }

    /**
     * カプセル名を設定します。
     *
     * @param name カプセル名
     */
    public void SetName(String name) {
        this.name = name;
    }

    /**
     * カプセル名を取得します。
     *
     * @return カプセル名
     */
    public String GetName() {
        return this.name;
    }

    /**
     * 当選レートを設定します。
     *
     * @param rate 当選レート
     */
    public void SetRate(int rate) {
        this.rate = rate;
    }

    /**
     * 当選レートを取得します。
     *
     * @return 当選レート
     */
    public int GetRate() {
        return this.rate;
    }

    /**
     * カプセルの中身を設定します。
     *
     * @param items カプセルの中身
     */
    public void SetItems(List<ItemStack> items) {
        this.Items = items;
    }

    /**
     * カプセルの中身を取得します。
     *
     * @return カプセルの中身
     */
    public List<ItemStack> GetItems() {
        return this.Items;
    }

    /**
     * 当選時にログインメンバー全員に公表するか否かのフラグを設定します。
     *
     * @param publish_flg フラグ
     */
    public void SetPublishFlg(boolean publish_flg) {
        this.publish_flg = publish_flg;
    }

    /**
     * 当選時にログインメンバー全員に公表するか否かのフラグを取得します。
     *
     * @return フラグ
     */
    public boolean GetPublishFlg() {
        return this.publish_flg;
    }

    /**
     * 使用するインベントリ数を設定します。
     *
     * @param num インベントリ数
     */
    public void SetUseInventory(int num) {
        this.use_inventory = num;
    }

    /**
     * 使用するインベントリ数を取得します。
     *
     * @return インベントリ数
     */
    public int GetUseInventory() {
        return this.use_inventory;
    }

    /**
     * シークレットフラグを設定し、変更があった場合はDBにも保存します。
     *
     * @param secret フラグ
     */
    public void SetSecret(boolean secret) {
        if (secret_flg != secret) {
            try {
                this.updateSecret(this.id, secret);
            }
            catch (SQLException ex) {
                Logger.getLogger(GachaponCapsuleData.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.secret_flg = secret;
        }
    }

    /**
     * シークレットフラグを取得します。
     *
     * @return フラグ
     */
    public boolean GetSecret() {
        return this.secret_flg;
    }

    /**
     * DBのシークレットフラグを更新します。
     *
     * @param CapsuleID カプセルID
     * @param secret シークレットフラグ
     * @return 更新レコード件数
     * @throws SQLException
     */
    private int updateSecret(int CapsuleID, boolean secret) throws SQLException {
        String sql;
        PreparedStatement ps;
        try (JDCConnection con = plugin.getConnection()) {
            sql = "update gachapon_capsule set secret = ? Where `id` = ?";
            ps = con.prepareStatement(sql);
            short secret_num;
            if (secret) {
                secret_num = 1;
            } else {
                secret_num = 0;
            }
            ps.setShort(1, secret_num);
            ps.setInt(2, CapsuleID);
        }
        //UPDATE実行       
        return ps.executeUpdate();
    }

    public boolean CreateBookData() {
        ItemStack[] isarray = new ItemStack[this.GetItems().size()];
        this.GetItems().toArray(isarray);
        ItemBook ib = new ItemBook(this.GetName(), isarray);
        this.BookData = ib.getBook();
        return true;
    }

    public ItemStack GetBookData() {
        return BookData;
    }
}

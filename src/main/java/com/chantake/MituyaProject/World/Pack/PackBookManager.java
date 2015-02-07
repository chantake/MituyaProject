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
package com.chantake.MituyaProject.World.Pack;

import com.chantake.MituyaProject.Exception.PlayerOfflineException;
import com.chantake.MituyaProject.MituyaManager;
import com.chantake.MituyaProject.MituyaProject;
import com.chantake.MituyaProject.Player.PlayerInstance;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BookMeta;

/**
 * 本に様々な機能をつける
 *
 * @author いんく
 */
public class PackBookManager extends MituyaManager {

    /**
     * PackBook用識別子 ※変更した場合既存のPackBookが使えなくなります
     */
    public static final String Author = "328Pack";
    /**
     * インベントリがいっぱい
     */
    private static final String maxInventory = "インベントリが埋まっているようだ";

    public PackBookManager(MituyaProject plugin) {
        super(plugin);
    }

    /**
     * PackBookかどうか判定します
     *
     * @param item ItemStack
     * @return PackBookの場合trueが返ります
     */
    public boolean isPackBook(ItemStack item) {
        //そもそも本かどうか
        if (item.getType() == Material.WRITTEN_BOOK) {
            BookMeta bm = (BookMeta)item.getItemMeta();
            String author = bm.getAuthor();
            //著者が同じ場合true
            if (author != null && author.equals(PackBookManager.Author)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 本を開けます
     *
     * @param player
     * @param ins
     * @return 正常実行された場合はtrue、それ以外はfalseが返ります。
     */
    public boolean openBook(final Player player, final PlayerInstance ins) {
        //手持ちのアイテム
        return this.openBook(player.getItemInHand(), player, ins);
    }

    /*
     * 本を開けます
     */
    public boolean openBook(final ItemStack book, final Player player, final PlayerInstance ins) {
        if (!this.isPackBook(book)) {
            return false;
        }
        BookMeta bm = (BookMeta)book.getItemMeta();
        List<String> data = new ArrayList<>();
        //エンコードされているため除去する
        for(String s : bm.getPages()) {
            data.add(s.replaceAll("§0", ""));
        }
        BookType type = BookType.getType(data.get(0));
        if (type == null) {
            return false;
        }
        data = data.subList(1, data.size());

        switch (type) {
            case Item:
                this.openItemBook(data, player, ins);
                break;
            case Command:
                this.openCommandBook(data, player, ins);
                break;
            default:
                return false;
        }
        player.closeInventory();
        return true;
    }

    /**
     * アイテムbookがクリックされた時に呼び出されるメソッド
     *
     * @param data ItemBook
     * @param player Player
     * @param ins PlayerInstance
     */
    public void openItemBook(final List<String> data, final Player player, final PlayerInstance ins) {
        //持っているアイテムスロットid
        final int heldItemSlot = player.getInventory().getHeldItemSlot();
        final ItemStack is = player.getItemInHand();

        //インベントリを取得
        PlayerInventory inventory = player.getInventory();
        //アイテム所持判定
        if (!is.equals(inventory.getItem(heldItemSlot))) {
            ins.sendAttention("アイテムブックが存在していないか、別のスロットに移動されています。");
            return;
        }
        try {
            //インベントリ空きスロット判定
            if (!ins.InventryCheck(1, (short)0, 64 * data.size())) {
                player.sendMessage(maxInventory);
                //pl.closeInventory(); 閉じる必要あるのかなぁ
                return;
            }
        }
        catch (PlayerOfflineException ex) {
        }
        //削除する
        player.getInventory().setItem(heldItemSlot, null);
        //展開
        YamlConfiguration config = new YamlConfiguration();
        for (String s : data) {
            try {
                config.loadFromString(s);
                ins.gainItem((ItemStack)config.get("item"));
            }
            catch (InvalidConfigurationException ex) {
                Logger.getLogger(PackBookManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (PlayerOfflineException ex) {
            }
        }
        player.updateInventory();
        ins.sendSuccess("アイテムパックを開封しました。");
    }

    /**
     * アイテムbookがクリックされた時に呼び出されるメソッド
     *
     * @param data ItemBook
     * @param player Player
     * @param ins PlayerInstance
     * @return
     */
    public boolean openCommandBook(final List<String> data, final Player player, final PlayerInstance ins) {
        return true;
    }

    /**
     * 本を作成します
     *
     * @param type BookType
     * @param title 本の題名
     * @param data データ
     * @see PackBookManager.BookType
     * @return 正常に作成できた場合はItemStackが、それ以外はnullが返ります。
     */
    public static ItemStack createBook(BookType type, String title, List<String> data) {
        //nullの場合は作成出来ません
        if (title == null || data == null) {
            return null;
        }
        /*
         * 本を作成
         */
        ItemStack is = new ItemStack(Material.WRITTEN_BOOK, 1);
        BookMeta bm = (BookMeta)is.getItemMeta();
        //PackBook識別用
        bm.setAuthor(PackBookManager.Author);
        //タイトル
        bm.setTitle(title);
        //1ページ目はPackBookのタイプ等の情報が入る
        data.add(0, type.getType());
        //本に記入
        bm.setPages(data);
        //itemstackに代入
        is.setItemMeta(bm);
        return is;
    }
}

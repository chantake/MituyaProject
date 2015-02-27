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
package com.chantake.MituyaProject.Util;

import com.chantake.MituyaProject.Data.ChestShopData;
import com.chantake.MituyaProject.Dynmap.MarkerBridgeAPI;
import com.chantake.MituyaProject.MituyaProject;
import com.chantake.MituyaProject.Parameter.Parameter328;
import com.chantake.MituyaProject.Player.LocationData;
import com.chantake.MituyaProject.Player.PlayerInstance;
import com.chantake.MituyaProject.World.Shop.ChestShopManager;
import com.chantake.MituyaProject.World.WorldManager;
import com.chantake.mituyaapi.tools.database.JDCConnection;
import org.bukkit.Location;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * MySqlを使用、または操作するクラスです
 *
 * @author chantake
 * @author ezura573
 * @version 2.1.0
 */
public class MySqlProcessing {

    private static MituyaProject plugin;

    public static void setPlugin(MituyaProject plugin) {
        MySqlProcessing.plugin = plugin;
    }

    /**
     * DBから指定ユーザの[characters]テーブルレコードＩＤを検索して返します。
     *
     * @param name 検索する名前
     * @return レコードＩＤ(見つからない場合は-1)
     * @author ezura573
     * @since 2.1.0
     */
    public static int FindPlayerCharID(String name) {
        // キャラクタ情報用テーブルを検索先に指定して、検索関数に処理をさせる。
        return MySqlProcessing.FindPlayerIDFunctionBody(FindIdType.Character,
                name, true);
    }

    // <editor-fold defaultstate="collapsed"
    // desc="FindPlayerCharID（DBから指定ユーザの[characters]テーブルレコードＩＤを検索して返します。）">

    /**
     * DBから指定ユーザの[characters]テーブルレコードＩＤを検索して返します。
     *
     * @param name 検索する名前
     * @param pfind 完全一致検索フラグ(true:完全一致検索　/ false:前方一致検索)
     * @return レコードＩＤ(見つからない場合は-1)
     * @author ezura573
     * @since 2.1.0
     */
    public static int FindPlayerCharID(String name, boolean pfind) {
        // キャラクタ情報用テーブルを検索先に指定して、検索関数に処理をさせる。
        return MySqlProcessing.FindPlayerIDFunctionBody(FindIdType.Character,
                name, pfind);
    }

    // <editor-fold defaultstate="collapsed"
    // desc="FindPlayerCharIDメソッドのオーバーロード+1">

    /**
     * DBから指定ユーザの[hometable]テーブルレコードＩＤを検索して返します。
     *
     * @param name 検索する名前
     * @return レコードＩＤ(見つからない場合は-1)
     * @author ezura573
     * @since 2.1.0
     */
    public static int FindPlayerHomeID(String name) {
        // ホーム用テーブルを検索先に指定して、検索関数に処理をさせる。
        return MySqlProcessing.FindPlayerIDFunctionBody(FindIdType.Home, name, true);
    }

    // <editor-fold defaultstate="collapsed"
    // desc="FindPlayerHomeID（DBから指定ユーザの[hometable]テーブルレコードＩＤを検索して返します。）">

    /**
     * DBから指定ユーザの[hometable]テーブルレコードＩＤを検索して返します。
     *
     * @param name 検索する名前
     * @param pfind 完全一致検索フラグ(true:完全一致検索　/ false:前方一致検索)
     * @return レコードＩＤ(見つからない場合は-1)
     * @author ezura573
     * @since 2.1.0
     */
    public static int FindPlayerHomeID(String name, boolean pfind) {
        // ホーム用テーブルを検索先に指定して、検索関数に処理をさせる。
        return MySqlProcessing.FindPlayerIDFunctionBody(FindIdType.Home, name,
                pfind);
    }

    // <editor-fold defaultstate="collapsed"
    // desc="FindPlayerHomeIDメソッドのオーバーロード+1">

    /**
     * 指定種別に対応するDBテーブルより指定ユーザのレコードＩＤを検索して返します。
     *
     * @param ftype 検索するテーブル種別
     * @param name 検索する名前
     * @param pfind 完全一致検索フラグ(true:完全一致検索　/ false:前方一致検索)
     * @return レコードＩＤ(見つからない場合は-1)
     * @author ezura573
     * @since 2.1.0
     */
    private static int FindPlayerIDFunctionBody(FindIdType ftype, String name,
            boolean pfind) {
        // 検索に使用するSQLの初期化
        String sql;

        // 戻り値の初期化
        int id = -1;

        // 完全一致でftypeで指定された種類に対応するテーブルより、指定されたユーザ名のレコードIDを検索するようにSQLをセットする
        switch (ftype) {
            case Character:
                sql = "SELECT id FROM characters WHERE name = ?";
                break;
            case Home:
                sql = "SELECT id FROM hometable WHERE name = ?";
                break;
            case Mine:
                sql = "SELECT id FROM ibalances WHERE player = ?";
                break;
            case Spawn:
                sql = "SELECT id FROM spawnlocation WHERE name = ?";
                break;
            case TLocation:
                sql = "SELECT id FROM templocation WHERE name = ?";
                break;
            default:
                return -1;
        }

        // pfindで前方一致検索が指定されていた場合はSQLを変更
        if (!pfind) {
            sql = sql.replaceAll("=", "LIKE");
        }

        try {
            try (JDCConnection con = plugin.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

                // パラメータを設定
                if (pfind) {
                    // 完全一致検索なので、nameをそのまま設定
                    ps.setString(1, name);
                } else {
                    // 前方一致検索なので、nemeにワイルドカードを付加して設定
                    ps.setString(1, name + "%");
                }
                try ( // クエリーを実行して結果セットを取得
                        ResultSet rs = ps.executeQuery()) {
                    rs.getRow();

                    // 検索された行数分ループ
                    while (rs.next()) {

                        // idを取得
                        id = rs.getInt("id");

                        // 一件のみ取得するためbreak;
                        break;
                    }
                }
            }
        }
        catch (final SQLException ex) {
        }
        return id;
    }

    // <editor-fold defaultstate="collapsed" desc="FindPlayerIDFunctionBody（指定種別に対応するDBテーブルより指定ユーザのレコードＩＤを検索して返します。）">

    /**
     * DBから指定ユーザの[ibalances]テーブルレコードＩＤを検索して返します。
     *
     * @param name 検索する名前
     * @return レコードＩＤ(見つからない場合は-1)
     * @author ezura573
     * @since 2.1.0
     */
    public static int FindPlayerMineID(String name) {
        // Mine用テーブルを検索先に指定して、検索関数に処理をさせる。
        return MySqlProcessing.FindPlayerIDFunctionBody(FindIdType.Mine, name,
                true);
    }

    // <editor-fold defaultstate="collapsed"
    // desc="FindPlayerMineID（DBから指定ユーザの[ibalances]テーブルレコードＩＤを検索して返します。）">

    /**
     * DBから指定ユーザの[ibalances]テーブルレコードＩＤを検索して返します。
     *
     * @param name 検索する名前
     * @param pfind 完全一致検索フラグ(true:完全一致検索　/ false:前方一致検索)
     * @return レコードＩＤ(見つからない場合は-1)
     * @author ezura573
     * @since 2.1.0
     */
    public static int FindPlayerMineID(String name, boolean pfind) {
        // Mine用テーブルを検索先に指定して、検索関数に処理をさせる。
        return MySqlProcessing.FindPlayerIDFunctionBody(FindIdType.Mine, name,
                pfind);
    }

    // <editor-fold defaultstate="collapsed"
    // desc="FindPlayerMineIDメソッドのオーバーロード+1">

    /**
     * DBから指定ユーザの[spawnlocation]テーブルレコードＩＤを検索して返します。
     *
     * @param name 検索する名前
     * @return レコードＩＤ(見つからない場合は-1)
     * @author ezura573
     * @since 2.1.0
     */
    public static int FindPlayerSpawnID(String name) {
        // スポーン用テーブルを検索先に指定して、検索関数に処理をさせる。
        return MySqlProcessing.FindPlayerIDFunctionBody(FindIdType.Spawn, name,
                true);
    }

    // <editor-fold defaultstate="collapsed"
    // desc="FindPlayerSpawnID（DBから指定ユーザの[spawnlocation]テーブルレコードＩＤを検索して返します。）">

    /**
     * DBから指定ユーザの[spawnlocation]テーブルレコードＩＤを検索して返します。
     *
     * @param name 検索する名前
     * @param pfind 完全一致検索フラグ(true:完全一致検索　/ false:前方一致検索)
     * @return レコードＩＤ(見つからない場合は-1)
     * @author ezura573
     * @since 2.1.0
     */
    public static int FindPlayerSpawnID(String name, boolean pfind) {
        // スポーン用テーブルを検索先に指定して、検索関数に処理をさせる。
        return MySqlProcessing.FindPlayerIDFunctionBody(FindIdType.Spawn, name,
                pfind);
    }

    // <editor-fold defaultstate="collapsed"
    // desc="FindPlayerSpawnIDメソッドのオーバーロード+1">

    /**
     * DBから指定ユーザの[templocation]テーブルレコードＩＤを検索して返します。
     *
     * @param name 検索する名前
     * @return レコードＩＤ(見つからない場合は-1)
     * @author ezura573
     * @since 2.1.0
     */
    public static int FindPlayerTLocationID(String name) {
        // 仮位置用テーブルを検索先に指定して、検索関数に処理をさせる。
        return MySqlProcessing.FindPlayerIDFunctionBody(FindIdType.TLocation,
                name, true);
    }

    // <editor-fold defaultstate="collapsed"
    // desc="FindPlayerTLocationID（DBから指定ユーザの[templocation]テーブルレコードＩＤを検索して返します。）">

    /**
     * DBから指定ユーザの[templocation]テーブルレコードＩＤを検索して返します。
     *
     * @param name 検索する名前
     * @param pfind 完全一致検索フラグ(true:完全一致検索　/ false:前方一致検索)
     * @return レコードＩＤ(見つからない場合は-1)
     * @author ezura573
     * @since 2.1.0
     */
    public static int FindPlayerTLocationID(String name, boolean pfind) {
        // 仮位置用テーブルを検索先に指定して、検索関数に処理をさせる。
        return MySqlProcessing.FindPlayerIDFunctionBody(FindIdType.TLocation,
                name, pfind);
    }

    // <editor-fold defaultstate="collapsed"
    // desc="FindPlayerTLocationIDメソッドのオーバーロード＋1">

    /**
     * SQLから完全一致で検索し、検索結果が1件のときtrueを返す。それ以外はfalse
     *
     * @param name 検索する名前
     * @return 検索結果が1件のときtrue それ以外はfalse
     * @author chantake
     * @since 2.0.0
     */
    public static boolean isPlayer(String name) {
        boolean result = false;
        try (JDCConnection con = plugin.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("SELECT * FROM characters WHERE name = ?")) {
                ps.setString(1, name);
                try (ResultSet rs = ps.executeQuery()) {
                    rs.last();
                    if (rs.getRow() == 1) {
                        result = true;
                    }
                }
            }
        }
        catch (final SQLException ex) {
            // Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null,
            // ex);
        }
        return result;
    }

    // <editor-fold defaultstate="collapsed"
    // desc="getPlayer(String name) SQLから完全一致で検索し、検索結果が1件のときtrueを返す。それ以外はfalse">

    /**
     * プレーヤーをmysqlから取得します
     *
     * @param name
     * @return
     */
    public static String getPlayer(String name) {
        String result = null;
        try (JDCConnection con = plugin.getConnection(); PreparedStatement ps = con.prepareStatement("SELECT * FROM `characters` WHERE `name` = ?")) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                rs.last();
                if (rs.getRow() == 1) {
                    result = rs.getString("name");
                }
            }
        }
        catch (final SQLException ex) {
            // Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null,
            // ex);
        }
        return result;
    }

    public static String matchSinglePlayer(String name) {
        String result = null;
        try (JDCConnection con = plugin.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("SELECT * FROM `characters` WHERE `name` = ? OR `nickname` = ?")) {
                ps.setString(1, name);
                ps.setString(2, name);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        result = rs.getString("name");
                    }
                }
            }
        }
        catch (final SQLException ex) {
            // Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null,
            // ex);
        }
        return result;
    }

    /**
     * SQLから完全一致で検索し、検索結果が1件のときtrueを返す。それ以外はfalse
     *
     * @param name 検索する名前
     * @return 検索結果が1件のときtrue それ以外はfalse
     * @author chantake
     * @since 2.0.0
     */
    public static boolean getPlayerNickName(String name) {
        try {
            final int count;
            try (JDCConnection c = plugin.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT * FROM `characters` WHERE nickname = ?")) {
                ps.setString(1, name);
                final ResultSet rs = ps.executeQuery();
                rs.last();
                count = rs.getRow();
            }
            return count == 1;
        }
        catch (final SQLException ex) {
            // Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null,
            // ex);
            return false;
        }
    }

    /**
     * SQLから完全一致で検索し、検索結果が1件のときtrueを返す。それ以外はfalse
     *
     * @param name 検索する名前
     * @return 検索結果が1件のときはIDを返し それ以外は-1
     * @author chantake
     * @since 2.0.0
     */
    public static int getPlayerHome(String name) {
        try {
            int id;
            try (JDCConnection c = plugin.getConnection()) {
                ResultSet rs;
                try (PreparedStatement ps = c.prepareStatement("SELECT * FROM hometable WHERE name = ?")) {
                    ps.setString(1, name);
                    rs = ps.executeQuery();
                    rs.last();
                    id = rs.getInt("id");
                }
                rs.close();
            }
            return id;
        }
        catch (SQLException ex) {
            // Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    // <editor-fold defaultstate="collapsed" desc="getPlayerHome(String name) SQLから完全一致で検索し、検索結果が1件のときtrueを返す。それ以外はfalse">

    /**
     * SQLから完全一致で検索し、検索結果が1件のときIDを返す。それ以外は合算修復処理後新規レコード生成
     *
     * @param name 検索する名前
     * @return 検索結果が1件のときはIDを返し それ以外は-1
     * @author chantake
     * @since 2.0.0
     */
    public static int getPlayerMine(String name) {
        try {
            final JDCConnection c = plugin.getConnection();
            PreparedStatement ps = c.prepareStatement("SELECT * FROM ibalances WHERE name = ?");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            rs.last();
            final int count = rs.getRow();
            final int id = rs.getInt("id");
            if (count == 1) {
                ps.close();
                rs.close();
                c.close();
                return id;
            } else {
                rs.first();
                int money = 0;
                do {
                    money += rs.getInt("balance") - 10000;
                    final ResultSet rs2;
                    try (PreparedStatement ps2 = c.prepareStatement("DELETE FROM ibalances WHERE id = ?")) {
                        ps2.setInt(1, rs.getInt("id"));
                        rs2 = ps.executeQuery();
                    }
                    rs2.close();
                } while (rs.next());
                ps.close();
                rs.close();
                money += 10000;
                ps = c.prepareStatement(
                        "INSERT INTO ibalances (player,balance) VALUES (?,?)",
                        Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, name);
                ps.setInt(2, money);
                ps.executeUpdate();
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    final int ret = rs.getInt(1);
                    ps.close();
                    rs.close();
                    c.close();
                    return ret;
                } else {
                    ps.close();
                    rs.close();
                    c.close();
                    throw new RuntimeException("Inserting char failed.");
                }
            }
        }
        catch (final SQLException ex) {
            // Logger.getLogger(Mt.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed"
    // desc="getPlayerMine(String name) SQLから完全一致で検索し、検索結果が1件のときtrueを返す。それ以外はfalse">

    public static String getPlayerSerch(String name) {
        String names = null;
        try {
            try (JDCConnection c = plugin.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT * FROM `characters` WHERE `name` LIKE ? OR `nickname` LIKE ?")) {
                String se = "%" + name + "%";
                ps.setString(1, se);
                ps.setString(2, se);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        names = rs.getString("name");
                    }
                }
            }
        }
        catch (final SQLException ex) {
            // Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null,
            // ex);
        }
        return names;
    }

    public static String getPlayerPrefixSerch(String name) {
        String names = null;
        try {
            try (JDCConnection c = plugin.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT * FROM `characters` WHERE `name` LIKE ? OR `nickname` LIKE ?")) {
                String se = name + "%";
                ps.setString(1, se);
                ps.setString(2, se);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        names = rs.getString("name");
                    }
                }
            }
        }
        catch (final SQLException ex) {
            // Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null,
            // ex);
        }
        return names;
    }

    public static String getPlayerFromNickName(String name) {
        try {
            String names;
            final int count;
            try (JDCConnection c = plugin.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT * FROM characters WHERE nickname like ?")) {
                ps.setString(1, name);
                final ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    rs.close();
                    ps.close();
                    c.close();
                    return null;
                }
                names = rs.getString("name");
                rs.last();
                count = rs.getRow();
            }
            // 検索結果が1件の場合のみ返す
            if (count == 1) {
                return names;
            } else {
                return null;
            }
        }
        catch (final SQLException ex) {
            return null;
            // Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null,
            // ex);
        }
    }

    /**
     * SQLから完全一致で検索し、検索結果が1件のときtrueを返す。それ以外はfalse
     *
     * @param name 検索する名前
     * @return 検索結果が1件のときはIDを返し それ以外は-1
     * @author chantake
     * @since 2.0.0
     */
    public static int getPlayerSpawn(String name) {
        try {
            final int count;
            final int id;
            try (JDCConnection c = plugin.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT * FROM spawnlocation WHERE name = ?")) {
                ps.setString(1, name);
                final ResultSet rs = ps.executeQuery();
                rs.last();
                count = rs.getRow();
                id = rs.getInt("id");
            }
            if (count == 1) {
                return id;
            } else {
                return -1;
            }
        }
        catch (final SQLException ex) {
            // Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null,
            // ex);
            return -1;
        }
    }

    // <editor-fold defaultstate="collapsed"
    // desc="getPlayerSpawn(String name) SQLから完全一致で検索し、検索結果が1件のときtrueを返す。それ以外はfalse">

    /**
     * SQLから完全一致で検索し、検索結果が1件のときtrueを返す。それ以外はfalse
     *
     * @param name 検索する名前
     * @return 検索結果が1件のときはIDを返し それ以外は-1
     * @author chantake
     * @since 2.0.0
     */
    public static int getPlayerTemp(String name) {
        try {
            final int count;
            final int id;
            try (JDCConnection c = plugin.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT * FROM templocation WHERE name = ?")) {
                ps.setString(1, name);
                final ResultSet rs = ps.executeQuery();
                rs.last();
                count = rs.getRow();
                id = rs.getInt("id");
            }
            if (count == 1) {
                return id;
            } else {
                return -1;
            }
        }
        catch (final SQLException ex) {
            // Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null,
            // ex);
            return -1;
        }
    }

    // <editor-fold defaultstate="collapsed"
    // desc="getPlayerTemp(String name) SQLから完全一致で検索し、検索結果が1件のときtrueを返す。それ以外はfalse">

    public static int getPrice(int id, int type) {
        int price = -1;
        try {
            try (JDCConnection con = plugin.getConnection()) {
                PreparedStatement ps;
                /*
                 * ps = con.prepareStatement( "SELECT * FROM simpleshop WHERE id = ?
                 * AND type = ?"); ps.setInt(1, id); ps.setInt(2, type);
                 */
                ps = con.prepareStatement("SELECT * FROM simpleshop WHERE item = ?");
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        rs.close();
                        ps.close();
                        con.close();
                        // throw new
                        // RuntimeException("Loading char failed (not found)");
                    }
                    price = rs.getInt("buy") / rs.getInt("per");
                }
                ps.close();
            }
        }
        catch (final SQLException ex) {
        }
        return price;
    }

    // </editor-fold>
    public static void getServer() {
        try {
            try (JDCConnection con = plugin.getConnection();
                    PreparedStatement ps = con.prepareStatement("SELECT * FROM server");
                    ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Parameter328.taxmine = rs.getLong("tax");
                    Parameter328.maxonline = rs.getInt("maxonline");
                    Parameter328.jackpot = rs.getLong("jackpot");
                }
            }
        }
        catch (final SQLException ex) {
        }
    }

    public static boolean giveEveryOne(int money) {
        boolean re = true;
        try (JDCConnection c = plugin.getConnection();
                PreparedStatement ps = c.prepareStatement("UPDATE ibalances SET balance = balance + " + money)) {
            ps.executeUpdate();
        }
        catch (final SQLException ex) {
            Logger.getLogger(MySqlProcessing.class.getName()).log(Level.SEVERE, null, ex);
            re = false;
        }
        return re;
    }

    public static void giveMoney(PlayerInstance player, PlayerInstance cp,
            long money) {
        /*
         * cp atesaki
         */
        player.gainMine(-money);
        cp.gainMine(money);
    }

    // </editor-fold>
    public static void giveTaxAll(int money) {
        Parameter328.taxmine = money;
    }

    /**
     * DBにGMコマンド使用ログを記録します。
     *
     * @param plugin
     * @param name コマンド使用者名
     * @param command 使用コマンド文字列
     * @param location コマンド使用時のキャラクタロケーション
     * @return 結果(true:記録成功 false:記録失敗)
     * @author ezura573
     * @since 2.1.0
     */
    @SuppressWarnings("finally")
    public static boolean GmCommandLog(MituyaProject plugin, String name, String command, Location location) {
        // 戻り値の初期化
        boolean ret = false;

        // ステートメント生成
        PreparedStatement ps = null;

        try {
            try (JDCConnection con = plugin.getConnection()) {
                final String sql = "insert into gm_command_log (name,command,world,x,y,z,yaw,pitch,log_dtm) values(?,?,?,?,?,?,?,?,now())";
                ps = con.prepareStatement(sql);

                // パラメータをセット
                ps.setString(1, name);
                ps.setString(2, command);
                ps.setLong(3, plugin.getWorldManager().getWorldData(location.getWorld()).getId());
                ps.setDouble(4, location.getX());
                ps.setDouble(5, location.getY());
                ps.setDouble(6, location.getZ());
                ps.setFloat(7, location.getYaw());
                ps.setFloat(8, location.getPitch());

                // SQL文を発行し、結果を取得
                final int result = ps.executeUpdate();

                // データ作成が正常か判定
                if (result < 0) {
                    throw new SQLException();
                }
            }
        }
        catch (final SQLException ex) {
        }
        finally {
            try {
                if (ps != null) {
                    // ステートメントを閉じる
                    ps.close();

                    ret = true;
                }
            }
            catch (final SQLException e) {
            }
            return ret;
        }
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed"
    // desc="GmCommandLog（DBにGMコマンド使用ログを記録します。）">

    // </editor-fold>
    public static void MituyaProject(MituyaProject plugin) {
        MySqlProcessing.plugin = plugin;
    }

    // </editor-fold>
    @Deprecated
    public static boolean PlayerHome(String name) {
        try {
            final int count;
            try (JDCConnection c = plugin.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT * FROM hometable WHERE name = ?")) {
                ps.setString(1, name);
                final ResultSet rs = ps.executeQuery();
                rs.last();
                count = rs.getRow();
            }
            return count == 1;
        }
        catch (final SQLException ex) {
            // Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null,
            // ex);
            return false;
        }
    }

    // </editor-fold>
    @Deprecated
    public static boolean PlayerHomeId(int id) {
        try {
            final int count;
            try (JDCConnection c = plugin.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT * FROM hometable WHERE id = ?")) {
                ps.setInt(1, id);
                final ResultSet rs = ps.executeQuery();
                rs.last();
                count = rs.getRow();
            }
            return count == 1;
        }
        catch (final SQLException ex) {
            // Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null,
            // ex);
            return false;
        }
    }

    // </editor-fold>
    @Deprecated
    public static int PlayerLocation(String name) {
        int na = -1;
        try {
            final int count;
            try (JDCConnection c = plugin.getConnection()) {
                final ResultSet rs;
                try (PreparedStatement ps = c.prepareStatement("SELECT * FROM templocation WHERE name = ?")) {
                    ps.setString(1, name);
                    rs = ps.executeQuery();
                    rs.last();
                    count = rs.getRow();
                    na = rs.getInt("id");
                }
                rs.close();
            }
            if (count == 0) {
                return na;
            } else {
                return na;
            }
        }
        catch (final SQLException ex) {
            // Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null,
            // ex);
            return na;
        }
    }

    /**
     * WolrdConfigをロードします
     *
     * @param wm
     */
    public static void LoadWolrdConfig(WorldManager wm) {
        try {
            try (JDCConnection con = plugin.getConnection(); PreparedStatement ps = con.prepareStatement("SELECT * FROM `world`"); ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    wm.LoadWorld(rs.getInt("id"), rs.getString("name"), rs.getInt("environment"), rs.getString("type"), rs.getInt("border"), ((rs.getByte("mob") & 0xff) == 1), ((rs.getByte("command") & 0xff) == 1), rs.getInt("difficulty"));
                }
            }
        }
        catch (SQLException e) {
            plugin.ErrLog("Missing load wolrd_id :" + e);
        }
    }

    /**
     * Wolrd_idをロードします
     *
     * @param ins
     */
    public static void LoadHome(PlayerInstance ins) {
        try {
            try (JDCConnection con = plugin.getConnection(); PreparedStatement ps = con.prepareStatement("SELECT * FROM hometable WHERE id = ?")) {
                ps.setInt(1, ins.getHome_Id());
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Location lo = new Location(plugin.getWorldManager().getWorld(rs.getInt("world")), rs.getDouble("x"), rs.getByte("y") & 0xff, rs.getDouble("z"), rs.getShort("yaw"), rs.getShort("pitch"));
                        LocationData locationData = new LocationData(lo, rs.getInt("subid"));
                        locationData.setPublic((rs.getByte("publicAll") & 0xff) == 1);
                        locationData.setMessage(rs.getString("welcomeMessage"));
                        ins.setHomeData(locationData);
                    }
                }
            }
        }
        catch (SQLException e) {
            plugin.ErrLog("Missing load wolrd_id :" + e);
        }
    }

    public static void SaveHome(PlayerInstance ins, LocationData ld, boolean update) {
        try {
            try (JDCConnection con = plugin.getConnection()) {
                PreparedStatement ps;
                Location loc = ld.getLocation();
                if (update) {
                    ps = con.prepareStatement("UPDATE hometable SET name = ?, x = ?, y = ?, z = ?, yaw = ?, pitch = ?, publicAll = ?, permissions = ?, welcomeMessage = ? WHERE id = ? AND world = ? AND subid = ?");
                    ps.setString(1, ins.getRawName());
                    ps.setDouble(2, ld.getLocation().getX());
                    ps.setByte(3, (byte)loc.getY());
                    ps.setDouble(4, loc.getZ());
                    ps.setShort(5, (short)loc.getYaw());
                    ps.setShort(6, (short)loc.getPitch());
                    ps.setByte(7, (byte)(ld.getPublic() ? 1 : 0));
                    ps.setString(8, ld.getPermission());
                    ps.setString(9, ld.getMessage());
                    ps.setInt(10, ins.getHome_Id());
                    ps.setInt(11, plugin.getWorldManager().getWorldData(ld.getLocation().getWorld()).getId());
                    ps.setInt(12, ld.getId());
                } else {
                    if (ins.getHome_Id() <= 0) {
                        ps = con.prepareStatement("INSERT INTO hometable (name,world,subid,x,y,z,yaw,pitch,publicAll,permissions,welcomeMessage) VALUES (?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                        ps.setString(1, ins.getRawName());
                        ps.setInt(2, plugin.getWorldManager().getWorldData(ld.getLocation().getWorld()).getId());
                        ps.setInt(3, ld.getId());
                        ps.setDouble(4, ld.getLocation().getX());
                        ps.setByte(5, (byte)loc.getY());
                        ps.setDouble(6, loc.getZ());
                        ps.setShort(7, (short)loc.getYaw());
                        ps.setShort(8, (short)loc.getPitch());
                        ps.setByte(9, (byte)(ld.getPublic() ? 1 : 0));
                        ps.setString(10, ld.getPermission());
                        ps.setString(11, ld.getMessage());
                    } else {
                        ps = con.prepareStatement("INSERT INTO hometable (id,name,world,subid,x,y,z,yaw,pitch,publicAll,permissions,welcomeMessage) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");
                        ps.setInt(1, ins.getHome_Id());
                        ps.setString(2, ins.getRawName());
                        ps.setInt(3, plugin.getWorldManager().getWorldData(ld.getLocation().getWorld()).getId());
                        ps.setInt(4, ld.getId());
                        ps.setDouble(5, ld.getLocation().getX());
                        ps.setByte(6, (byte)loc.getY());
                        ps.setDouble(7, ld.getLocation().getZ());
                        ps.setShort(8, (short)loc.getYaw());
                        ps.setShort(9, (short)loc.getPitch());
                        ps.setByte(10, (byte)(ld.getPublic() ? 1 : 0));
                        ps.setString(11, ld.getPermission());
                        ps.setString(12, ld.getMessage());
                    }
                }
                int updateRows = ps.executeUpdate();
                //homeID生成
                if (ins.getHome_Id() <= 0) {
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            ins.setHome_Id(rs.getInt(1));
                        } else {
                            throw new RuntimeException("Inserting char failed.");
                        }
                    }
                } else if (updateRows < 1) {
                    throw new RuntimeException("Home not in database (" + ins.getHome_Id() + ins.getName() + ")");
                }
                ps.close();
            }
            ins.SaveCharToDB(true);
        }
        catch (SQLException ex) {
            Logger.getLogger(PlayerInstance.class.getName()).log(Level.SEVERE, "SaveHomeToDB", ex);
        }
    }

    // </editor-fold>
    @Deprecated
    public static String PlayerSerchHome(String name) {
        try {
            String names;
            final int count;
            try (JDCConnection c = plugin.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT * FROM hometable WHERE name like ?")) {
                ps.setString(1, name + "%");
                final ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    rs.close();
                    ps.close();
                    c.close();
                    return "";
                }
                names = rs.getString("name");
                rs.last();
                count = rs.getRow();
            }
            if (count == 1) {
                return names;
            } else {
                return "";
            }
        }
        catch (final SQLException ex) {
            // Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null,
            // ex);
            return "";
        }
    }

    // </editor-fold>
    @Deprecated
    public static String PlayerSercho(String name) {
        try {
            String names;
            final int count;
            try (JDCConnection c = plugin.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT * FROM characters WHERE name like ?")) {
                ps.setString(1, name + "%");
                final ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    rs.close();
                    ps.close();
                    c.close();
                }
                names = rs.getString("name");
                rs.last();
                count = rs.getRow();
            }
            if (count == 1) {
                return names;
            } else {
                return "";
            }
        }
        catch (final SQLException ex) {
            // Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null,
            // ex);
        }
        if (MySqlProcessing.PlayerHome(name)) {
            return MySqlProcessing.PlayerSerchHome(name);
        } else {
            return "";
        }
    }

    // </editor-fold>
    @Deprecated
    public static int PlayerSpawn(String name) {
        int na = -1;
        try {
            final int count;
            try (JDCConnection c = plugin.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT * FROM spawnlocation WHERE name = ?")) {
                ps.setString(1, name);
                final ResultSet rs = ps.executeQuery();
                rs.last();
                count = rs.getRow();
                na = rs.getInt("id");
            }
            if (count == 0) {
                return na;
            } else {
                return na;
            }
        }
        catch (final SQLException ex) {
            // Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null,
            // ex);
            return na;
        }
    }

    // </editor-fold>
    public static void SaveMaxOnline() {
        try {
            try (JDCConnection c = plugin.getConnection(); PreparedStatement ps = c.prepareStatement("UPDATE server SET maxonline = ?,day = CURRENT_TIMESTAMP() LIMIT 1")) {
                ps.setInt(1, Parameter328.maxonline);
                ps.executeUpdate();
            }
        }
        catch (final SQLException ex) {
            Logger.getLogger(MySqlProcessing.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
    }

    // </editor-fold>
    public static void SaveServer() {
        try {
            try (JDCConnection c = plugin.getConnection(); PreparedStatement ps = c.prepareStatement("UPDATE server SET tax = ?, online = ?, jackpot = ? LIMIT 1")) {
                ps.setLong(1, Parameter328.taxmine);
                ps.setInt(
                        2,
                        MySqlProcessing.plugin.getServer().getOnlinePlayers().size());
                ps.setLong(3, Parameter328.jackpot);
                ps.executeUpdate();
            }
        }
        catch (final SQLException ex) {
            Logger.getLogger(MySqlProcessing.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
    }

    // </editor-fold>
    @Deprecated
    public static void SaveTax() {
        try {
            try (JDCConnection c = plugin.getConnection(); PreparedStatement ps = c.prepareStatement("UPDATE server SET tax = "
                    + Parameter328.taxmine + " LIMIT 1")) {
                ps.executeUpdate();
            }
        }
        catch (final SQLException ex) {
            Logger.getLogger(MySqlProcessing.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
    }

    /**
     * DBにショップ取引ログを記録します。
     *
     * @param name 取引者名
     * @param Dealings 取引内容(BUY/SELLを文字列で指定)
     * @param id アイテムID
     * @param type アイテムタイプ
     * @param amount 取引個数
     * @param money 取引金額
     * @return 結果(true:記録成功 false:記録失敗)
     */
    @SuppressWarnings("finally")
    public static boolean ShopDealingsLog(String name, String Dealings, int id, int type, int amount, int money) {
        // 戻り値の初期化
        boolean ret = false;

        // ステートメント生成
        PreparedStatement ps = null;

        try {
            try (JDCConnection con = plugin.getConnection()) {
                final String sql = "insert into shop_log (name,Dealings,item_id,item_type,Amount,money,log_dtm) values(?,?,?,?,?,?,now())";
                ps = con.prepareStatement(sql);

                // パラメータをセット
                ps.setString(1, name);
                ps.setString(2, Dealings);
                ps.setInt(3, id);
                ps.setInt(4, type);
                ps.setInt(5, amount);
                ps.setInt(6, money);

                // SQL文を発行し、結果を取得
                final int result = ps.executeUpdate();

                // データ作成が正常か判定
                if (result < 0) {
                    throw new SQLException();
                }
            }
        }
        catch (final SQLException ex) {
        }
        finally {
            try {
                if (ps != null) {
                    // ステートメントを閉じる
                    ps.close();

                    ret = true;
                }
            }
            catch (final SQLException e) {
            }
            return ret;
        }
    }// </editor-fold>

    // <editor-fold defaultstate="collapsed"
    // desc="ShopDealingsLog(DBにショップ取引ログを記録します。)">

    /**
     * Wolrd_idをロードします
     *
     * @param api
     */
    public static void addHomeMarkerAll(MarkerBridgeAPI api) {
        int amount = 0;
        try {
            try (JDCConnection con = plugin.getConnection(); PreparedStatement ps = con.prepareStatement("SELECT * FROM `hometable`"); ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    if (((rs.getByte("publicAll") & 0xff) == 1)) {//homeがpublicの時だけ
                        ++amount;
                        Location lo = new Location(plugin.getWorldManager().getWorld(rs.getInt("world")), rs.getDouble("x"), rs.getByte("y") & 0xff, rs.getDouble("z"));
                        LocationData ld = new LocationData(lo, rs.getInt("subid"));
                        api.createHomeMarker(rs.getString("name"), ld);
                    }
                }
            }
            plugin.Log("ホームマーカー(" + amount + ") 読み込みました");
        }
        catch (SQLException e) {
            plugin.ErrLog("Missing load addHomeMarkerAll:" + e);
        }
    }

    /*
     public static void saveMobSpawnerOwner(CreatureSpawner creaturespawner, String owner, boolean update) {
     try {
     Location location = creaturespawner.getBlock().getLocation();
     JDCConnection con = plugin.getConnection();
     PreparedStatement ps;
     if (update) {
     ps = con.prepareStatement("UPDATE mospawner SET `player` = ? WHERE `location` = ?");
     } else {
     ps = con.prepareStatement("INSERT INTO mospawner (`player`,`location`) VALUES (?,?)");
     }
     ps.setString(1, owner);
     ps.setString(2, LocationToString(location));
     int updateRows = ps.executeUpdate();
     if (updateRows < 1) {
     throw new RuntimeException("saveMobSpawnerOwner not in database (" + owner + ")");
     }
     ps.close();
     } catch (SQLException ex) {
     Logger.getLogger(PlayerInstance.class.getName()).log(Level.SEVERE, null, ex);
     }
     }

     public static String getMobSpawnerOwner(CreatureSpawner creaturespawner) {
     try {
     Location location = creaturespawner.getBlock().getLocation();
     final JDCConnection c = plugin.getConnection();
     final PreparedStatement ps = c.prepareStatement("SELECT player FROM mospawner WHERE `location` = ?");
     ps.setString(1, LocationToString(location));
     final ResultSet rs = ps.executeQuery();
     if (!rs.next()) {
     rs.close();
     ps.close();
     return null;
     }
     String name = rs.getString("player");
     rs.last();
     final int count = rs.getRow();
     ps.close();
     rs.close();
     // 検索結果が1件の場合のみ返す
     if (count == 1) {
     return name;
     } else {
     return null;
     }
     } catch (final SQLException ex) {
     return null;
     }
     }

     public static boolean removeSpawner(CreatureSpawner creaturespawner) {
     try {
     Location location = creaturespawner.getBlock().getLocation();
     final JDCConnection c = plugin.getConnection();
     final PreparedStatement ps = c.prepareStatement("DELETE FROM mospawner WHERE `location` = ?");
     ps.setString(1, LocationToString(location));
     ps.executeUpdate();
     ps.close();
     return true;
     } catch (final SQLException ex) {
     ex.printStackTrace();
     return false;
     }
     }*/
    private static String LocationToString(Location location) {
        return String.valueOf(location.getBlockX() + location.getBlockY() + location.getBlockZ());
    }

    public static void saveChestShopData(ChestShopData cshop, boolean update) {
        try {
            try (JDCConnection con = plugin.getConnection()) {
                PreparedStatement ps;
                if (update) {
                    ps = con.prepareStatement("UPDATE chestshop SET `owner` = ?, `typeid` = ?, `data` = ?, `amount` = ?, `buy` = ?, `sell` = ?, `world` = ?, `x` = ?, `y` = ?, `z` = ?, `official` = ? WHERE `id` = ?");
                    ps.setString(1, cshop.getOwner());
                    ps.setInt(2, cshop.getTypeid());
                    ps.setShort(3, cshop.getData());
                    ps.setInt(4, cshop.getAmount());
                    ps.setInt(5, cshop.getBuy());
                    ps.setInt(6, cshop.getSell());
                    ps.setInt(7, plugin.getWorldManager().getWorldData(cshop.getSign().getWorld()).getId());
                    ps.setInt(8, cshop.getSign().getX());
                    ps.setInt(9, cshop.getSign().getY());
                    ps.setInt(10, cshop.getSign().getZ());
                    ps.setByte(11, (byte)(cshop.getOfficial() ? 1 : 0));
                    ps.setString(12, cshop.getKey());
                } else {
                    ps = con.prepareStatement("INSERT INTO chestshop (`id`,`owner`,`typeid`,`data`,`amount`,`buy`,`sell`,`world`,`x`,`y`,`z`,`official`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");
                    ps.setString(1, cshop.getKey());
                    ps.setString(2, cshop.getOwner());
                    ps.setInt(3, cshop.getTypeid());
                    ps.setShort(4, cshop.getData());
                    ps.setInt(5, cshop.getAmount());
                    ps.setInt(6, cshop.getBuy());
                    ps.setInt(7, cshop.getSell());
                    ps.setInt(8, plugin.getWorldManager().getWorldData(cshop.getSign().getWorld()).getId());
                    ps.setInt(9, cshop.getSign().getX());
                    ps.setInt(10, cshop.getSign().getY());
                    ps.setInt(11, cshop.getSign().getZ());
                    ps.setByte(12, (byte)(cshop.getOfficial() ? 1 : 0));
                }
                int updateRows = ps.executeUpdate();
                if (updateRows < 1) {
                    throw new RuntimeException("saveChestShopData not in database (" + cshop.getKey() + ")");
                }
                ps.close();
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(PlayerInstance.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void addChestShopDataAll(ChestShopManager cshop) {
        int amount = 0;
        try {
            try (JDCConnection con = plugin.getConnection(); PreparedStatement ps = con.prepareStatement("SELECT * FROM `chestshop`")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    ++amount;
                    new ChestShopData(cshop, rs.getString("id"), rs.getString("owner"), rs.getInt("typeid"), rs.getShort("data"), rs.getInt("amount"), rs.getInt("buy"), rs.getInt("sell"), plugin.getWorldManager().getWorld(rs.getInt("world")), rs.getInt("x"), rs.getInt("y"), rs.getInt("z"), ((rs.getByte("official") & 0xff) == 1));
                }
                rs.close();
            }
            plugin.Log("チェストショップ(" + amount + ") 読み込みました");
        }
        catch (SQLException e) {
            plugin.ErrLog("Missing load addChestShopDataAll:" + e);
        }
    }

    public static boolean removeChestShop(ChestShopData cshop) {
        try {
            try (JDCConnection c = plugin.getConnection(); PreparedStatement ps = c.prepareStatement("DELETE FROM chestshop WHERE `id` = ?")) {
                ps.setString(1, cshop.getKey());
                ps.executeUpdate();
            }
            return true;
        }
        catch (final SQLException ex) {
            return false;
        }
    }

    /**
     * ID検索の種類を持つ列挙型クラスです。
     *
     * @author ezura573
     * @since 2.1.0
     */
    private enum FindIdType {

        /**
         * キャラクタ情報[characters]
         */
        Character,
        /**
         * ホーム情報[hometable]
         */
        Home,
        /**
         * Mine情報[ibalances]
         */
        Mine,
        /**
         * スポーン情報[spawnlocation]
         */
        Spawn,
        /**
         * Tempロケーション情報[templocation]
         */
        TLocation
    }
}

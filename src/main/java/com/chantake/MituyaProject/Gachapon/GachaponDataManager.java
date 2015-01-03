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
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

/**
 * 328ガチャに関する各機能を提供します。
 *
 * @author ezura573
 */
public class GachaponDataManager {

    // <editor-fold defaultstate="collapsed" desc="プライベート変数">
    private static MituyaProject plugin;
    private static boolean init_flg = false;                                 // 初期化済フラグ
    private static final List<String> GachaponItemNames = new ArrayList<>();      // ガチャ専用アイテム名リスト
    private static final List<GachaponPhaseData> Phases = new ArrayList<>();      // フェーズデータ
    private static int MaxPhase = 0;                                         // フェーズの最大値
    private static int DefaultPhase = 0;                                     // デフォルトフェーズ
    private static long BuyInterval = 1000;                                  // 購入インターバル
    private static double MinBuyTPS = 10.0d;                                 // 購入時に必要なTPS
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="コンストラクタ">
    /**
     * コンストラクタ
     *
     * @param plugin プラグイン
     */
    public GachaponDataManager(MituyaProject plugin) {
        GachaponDataManager.plugin = plugin;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="init">
    /**
     * DBからデータをロードしてガチャの使用準備を行います。
     *
     * @return 準備が成功したか否か
     */
    public boolean init() {
        boolean ret = true;    // 戻り値

        plugin.getLogger().log(Level.INFO, "328ガチャの初期化を開始します。");

        try {
            LoadDataFromDB();
        }
        catch (SQLException ex) {
            ret = false;
            Logger.getLogger(GachaponDataManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (ret) {
            init_flg = true;
            plugin.getLogger().log(Level.INFO, "328ガチャの初期化が完了しました。");
        } else {
            init_flg = false;
            plugin.getLogger().log(Level.INFO, "328ガチャの初期化に失敗しました。");
        }
        return ret;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="LoadDataFromDB">
    /**
     * DBからガチャのデータをロードします。
     *
     * @return データのロードに成功したか否か
     * @throws SQLException
     */
    private boolean LoadDataFromDB() throws SQLException {
        String sql;

        MaxPhase = 0;
        Phases.clear();
        GachaponItemNames.clear();

        //フェーズリスト取得
        try (JDCConnection con = plugin.getConnection()) {
            sql = "Select phase from gachapon_capsule GROUP BY phase ORDER BY phase ASC";
            try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

                //レコードセットの取得
                rs.getRow();

                // 検索された行数分ループ
                while (rs.next()) {
                    Phases.add(new GachaponPhaseData(plugin, rs.getInt("phase")));
                    if (MaxPhase < rs.getInt("phase")) {
                        MaxPhase = rs.getInt("phase");
                    }
                }
            }
        }

        //購入数取得
        try (JDCConnection con = plugin.getConnection()) {
            sql = "SELECT "
                    + "gachapon_capsule.phase,"
                    + "count(gachapon_log.id) as cnt "
                    + "FROM "
                    + "gachapon_capsule "
                    + "INNER JOIN gachapon_log ON gachapon_log.capsule_id = gachapon_capsule.id "
                    + "GROUP BY "
                    + "gachapon_capsule.phase "
                    + "ORDER BY "
                    + "gachapon_capsule.phase ASC";
            try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

                //レコードセットの取得
                rs.getRow();

                // 検索された行数分ループ
                while (rs.next()) {
                    int phase = rs.getInt("phase");
                    long cnt = rs.getLong("cnt");
                    for (GachaponPhaseData Phase : Phases) {
                        if (Phase.GetPhase() == phase) {
                            Phase.SetSaleNum(cnt);
                            break;
                        }
                    }
                }
            }
        }

        for (GachaponPhaseData Phase : Phases) {
            plugin.getLogger().log(Level.INFO, "Phase[{0}]\u3092\u30ed\u30fc\u30c9\u4e2d", Phase.GetPhase());
            Phase.LoadDataFromDB();
            plugin.getLogger().log(Level.INFO, "Phase[{0}]\u5b8c\u4e86", Phase.GetPhase());
        }

        // 変更禁止アイテム名取得処理
        try (JDCConnection con = plugin.getConnection()) {
            sql = "select * from gachapon_description Where denyrename = 1";
            try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    GachaponItemNames.add(rs.getString("name"));
                }
            }
        }

        return LoadConfig();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="reloadData">
    /**
     * DBからガチャのデータを再ロードします。
     *
     * @return
     */
    public boolean reloadData() {
        boolean ret = true;    // 戻り値        
        plugin.getLogger().log(Level.INFO, "328ガチャの再初期化を開始します。");

        init_flg = false;

        try {
            LoadDataFromDB();
        }
        catch (SQLException ex) {
            ret = false;
            Logger.getLogger(GachaponDataManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (ret) {
            init_flg = true;
            plugin.getLogger().log(Level.INFO, "328ガチャの再初期化が完了しました。");
        } else {
            init_flg = false;
            plugin.getLogger().log(Level.INFO, "328ガチャの再初期化に失敗しました。");
        }
        return ret;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="InsertGachaponLog">
    /**
     * ガチャ購入ログをDBに記録します。
     *
     * @param playerName　ガチャ購入者名
     * @param capsuleID 当選カプセルID
     * @throws SQLException
     */
    public static void InsertGachaponLog(String playerName, int capsuleID) throws SQLException {
        String sql = "insert into gachapon_log (player,capsule_id,DTM) values (?,?,now())";
        try (JDCConnection con = plugin.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, playerName);
            ps.setInt(2, capsuleID);

            //INSERT実行       
            ps.executeUpdate();
        }
    }
    // </editor-fold>

    private boolean LoadConfig() throws SQLException {
        String sql;
        try (JDCConnection con = plugin.getConnection()) {
            sql = "SELECT `key`,value_i,value_t FROM gachapon_config WHERE phase = 0";
            try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

                //レコードセットの取得
                rs.getRow();

                // 検索された行数分ループ
                while (rs.next()) {
                    String key = rs.getString("key").toLowerCase();
                    switch (key) {
                        case "defaultphase":
                            DefaultPhase = rs.getInt("value_i");
                            break;
                        case "buyinterval":
                            BuyInterval = (long)rs.getInt("value_i");
                            break;
                        case "min_buytps":
                            MinBuyTPS = (long)rs.getInt("value_i");
                            break;
                    }
                }
            }
        }
        return true;
    }

    /**
     * デフォルトフェーズを設定します。
     *
     * @param phase デフォルトフェーズ
     */
    public static void SetDefaultPhase(int phase) {
        if (DefaultPhase != phase) {
            boolean saved = false;
            try {
                SaveConfig("defaultphase", 0, phase);
                saved = true;
            }
            catch (SQLException ex) {
                Logger.getLogger(GachaponDataManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (saved) {
                DefaultPhase = phase;
            }
        }
    }

    /**
     * デフォルトフェーズを取得します。
     *
     * @return デフォルトフェーズ
     */
    public static int GetDefaultPhase() {
        return DefaultPhase;
    }

    /**
     * 初期化済みフラグを取得します。
     *
     * @return フラグ
     */
    public static boolean GetInit() {
        return init_flg;
    }

    /**
     * 購入可能間隔を設定します。
     *
     * @param interval
     */
    public static void SetBuyInterval(long interval) {
        if (BuyInterval != interval) {
            boolean saved = false;
            try {
                SaveConfig("buyinterval", 0, (int)interval);
                saved = true;
            }
            catch (SQLException ex) {
                Logger.getLogger(GachaponDataManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (saved) {
                BuyInterval = interval;
            }
        }
    }

    /**
     * 購入可能間隔を取得します。
     *
     * @return
     */
    public static long GetBuyInterval() {
        return BuyInterval;
    }

    public static void SetMinBuyTPS(double tps) {
        if (MinBuyTPS != tps) {
            boolean saved = false;
            try {
                SaveConfig("min_buytps", 0, (int)tps);
                saved = true;
            }
            catch (SQLException ex) {
                Logger.getLogger(GachaponDataManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (saved) {
                MinBuyTPS = tps;
            }
        }
    }

    public static double GetMinBuyTPS() {
        return MinBuyTPS;
    }

    /**
     * 指定IDのカプセルを全フェーズから検索して取得します。 見つからない場合はNULLを返します。
     *
     * @param id　カプセルID
     * @return カプセルデータ
     */
    public static GachaponCapsuleData GetCapsule(int id) {
        for (GachaponPhaseData Phase : Phases) {
            for (int i2 = 0; i2 < Phase.GetCapsules().size(); i2++) {
                if (Phase.GetCapsules().get(i2).GetId() == id) {
                    return Phase.GetCapsules().get(i2);
                }
            }
        }
        return null;
    }

    /**
     * 抽選により指定フェーズのカプセルを１個抽出します。
     *
     * @param Phase
     * @return カプセル
     */
    public static GachaponCapsuleData GetLotteryCapsule(int Phase) {
        for (GachaponPhaseData Phase1 : Phases) {
            if (Phase1.GetPhase() == Phase) {
                return Phase1.GetLotteryCapsule();
            }
        }
        return null;
    }

    /**
     * ハズレの本データを取得します。
     *
     * @return ハズレの本データ
     */
    public static ItemStack GetFailureBook() {
        ItemStack is = new ItemStack(Material.WRITTEN_BOOK, 1);
        BookMeta bm = (BookMeta)is.getItemMeta();
        bm.addPage(new String[]{"は  ず  れ"});
        bm.setAuthor("328mss");
        bm.setTitle("はずれ");
        is.setItemMeta(bm);
        return is;
    }

    /**
     * old_nameに指定されたアイテム名が名前変更禁止リストに完全一致で存在するか検索します。
     *
     * @param old_name 検索するアイテム名
     * @return 一致フラグ
     */
    public static boolean isGachaponItemNameCoincident(String old_name) {
        for (String GachaponItemName : GachaponItemNames) {
            if (old_name.matches(GachaponItemName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * new_name
     *
     * @param new_name 指定されたアイテム名が名前変更禁止リストに部分一致で存在するか検索します。
     * @return 一致フラグ
     */
    public static boolean isGachaponItemName(String new_name) {
        for (String GachaponItemName : GachaponItemNames) {
            if (new_name.matches(".*" + GachaponItemName + ".*")) {
                return true;
            }
        }
        return false;
    }

    /**
     * DBから全体の購入数を取得します。
     *
     * @return 購入数
     * @throws java.sql.SQLException
     */
    public static int GetGachaponBuyNumPublic() throws SQLException {
        int ret = 0;

        String sql = "SELECT count(id) as 'cnt' FROM gachapon_log";
        try (JDCConnection con = plugin.getConnection(); PreparedStatement ps = con.prepareStatement(sql); // SQL実行
                ResultSet rs = ps.executeQuery()) {

            // レコードセットの取得
            rs.getRow();

            // データ取得
            while (rs.next()) {
                ret = rs.getInt("cnt");
                break;
            }

        }
        return ret;
    }

    /**
     * DBから個人の購入数を取得します。
     *
     * @param PlayerName プレイヤー名
     * @return 購入数
     * @throws java.sql.SQLException
     */
    public static int GetGachaponBuyNumPrivate(String PlayerName) throws SQLException {
        int ret = 0;

        String sql = "SELECT count(id) as 'cnt' FROM gachapon_log WHERE player = ?";
        try (JDCConnection con = plugin.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, PlayerName);

            // レコードセットの取得
            try ( // SQL実行
                    ResultSet rs = ps.executeQuery()) {
                // レコードセットの取得
                rs.getRow();

                // データ取得
                while (rs.next()) {
                    ret = rs.getInt("cnt");
                    break;
                }
            }
        }
        return ret;
    }

    /**
     * 指定プレイヤーが指定ガチャフェーズのアイテムをコンプリートしたか否かを取得します。
     *
     * @param PlayerName プレイヤー名
     * @param Phase
     * @return true:コンプリート　false;未コンプリート
     * @throws SQLException
     */
    public static boolean CheckPlayerComplete(String PlayerName, int Phase) throws SQLException {
        for (GachaponPhaseData Phase1 : Phases) {
            if (Phase1.GetPhase() == Phase) {
                return Phase1.CheckPlayerComplete(PlayerName);
            }
        }
        return false;
    }

    /**
     * 指定プレイヤーのガチャの出現状態データを取得します。
     *
     * @param PlayerName
     * @return 出現済カプセルIDリスト
     * @throws SQLException
     */
    public static List GetUserEmergeData(String PlayerName) throws SQLException {
        List ret = new ArrayList<>();

        String sql = "SELECT capsule_id,count(id) FROM gachapon_log WHERE player = ? GROUP BY capsule_id";
        try (JDCConnection con = plugin.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, PlayerName);

            // レコードセットの取得
            try ( // SQL実行
                    ResultSet rs = ps.executeQuery()) {
                // レコードセットの取得
                rs.getRow();

                // データ取得
                while (rs.next()) {
                    ret.add(rs.getInt("capsule_id"));
                }
            }
        }
        return ret;
    }

    /**
     * フェーズの最大値を取得します。
     *
     * @return フェーズの最大値
     */
    public static int GetMaxPhase() {
        return MaxPhase;
    }

    /**
     * 指定されたフェーズのデータを取得します。
     *
     * @param Phase　フェーズ番号
     * @return フェーズデータ
     */
    public static GachaponPhaseData GetPhaseData(int Phase) {
        for (GachaponPhaseData Phase1 : Phases) {
            if (Phase1.GetPhase() == Phase) {
                return Phase1;
            }
        }
        return null;
    }

    public static List<GachaponPhaseData> GetPhaseData() {
        return Phases;
    }

    public static boolean SaveConfig(String Key, int Phase, int Value) throws SQLException {
        String sql;
        try (JDCConnection con = plugin.getConnection()) {
            sql = "update gachapon_config set value_i = ? WHERE `key` = ? AND phase = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, Value);
                ps.setString(2, Key);
                ps.setInt(3, Phase);

                ps.executeUpdate();
            }
        }
        return true;
    }

}

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

import com.chantake.MituyaProject.Util.MySqlProcessing;
import org.bukkit.Location;

/**
 *
 * @author ezura573
 */
public class SpawnDataManager {

    // <editor-fold defaultstate="collapsed" desc="ローカル変数宣言">
    private Location location = null; // Minecraft内位置情報
    private boolean useflag = false; // 使用フラグ
    private int recodeid = -1; // データベース内のレコードID
    private String ownername = ""; // 所有者名
    private int spawn_world = -1;
    private int spawn_id = -1;

	// </editor-fold>
    private void CreateNewRecode(String name) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * @todo セーブ関数の作成
     * @todo 新規レコード追加関数作成
     */
    /**
     * Location型のMinecraft内位置情報を設定します。
     *
     * @return　Minecraft内の位置情報
     */
    public Location getLocation() {
        return location;
    }
    // <editor-fold defaultstate="collapsed" desc="[Ownername]">

    /**
     * Location型のMinecraft内位置情報を取得します。
     *
     * @param value Minecraft内位置情報
     */
    public void setLocation(Location value) {
        location = value;
    }
    // <editor-fold defaultstate="collapsed" desc="[Recodeid]">

    public String getOwnername() {
        return ownername;
    }// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="[Spawn_id]">

    public void setOwnername(String ownername) {
        this.ownername = ownername;
    }
    // <editor-fold defaultstate="collapsed" desc="[Spawn_world]">

    public int getRecodeid() {
        return recodeid;
    }// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="[Useflag]">

    public void setRecodeid(int recodeid) {
        this.recodeid = recodeid;
    }

    public int getSpawn_id() {
        return spawn_id;
    }// </editor-fold>

    public void setSpawn_id(int spawn_id) {
        this.spawn_id = spawn_id;
    }

    public int getSpawn_world() {
        return spawn_world;
    }// </editor-fold>

    public void setSpawn_world(int spawn_world) {
        this.spawn_world = spawn_world;
    }

    public boolean isUseflag() {
        return useflag;
    }// </editor-fold>

    public void setUseflag(boolean useflag) {
        this.useflag = useflag;
    }

    /**
     * データベースに接続して、指定されたプレイヤーのスポーン情報を取得します。
     *
     * @param name        プレイヤー名
     * @param perfectflag 完全一致検索フラグ(true:完全一致検索　/ false:前方一致検索)
     * @return 読み込みの成功したか失敗したか
     */
    public boolean LoadSpawn(String name, boolean perfectflag) {
        boolean ret; // 戻り値の初期化

        // スポーンテーブルから対象プレーヤのレコードIDを取得
        recodeid = MySqlProcessing.FindPlayerSpawnID(name, perfectflag);

        // レコードが見つかった場合の処理
        if (recodeid > 0) {
            // じょうずによめましたー
            ret = true;
        } else {
            // 新規空レコード作成
            CreateNewRecode(name);
            ret = false;
        }
        return ret;
    }

    /**
     * データベースに接続して、指定されたプレイヤーのスポーン情報を保存します。完全一致検索
     *
     * @param name プレイヤー名
     * @return 読み込みの成功したか失敗したか
     */
    public boolean SaveSpawn(String name) {
        boolean ret; // 戻り値の初期化

        // スポーンテーブルから対象プレーヤのレコードIDを取得
        recodeid = MySqlProcessing.FindPlayerSpawnID(name, true);

        // レコードが見つかった場合の処理
        if (recodeid > 0) {
            // じょうずに保存しましたー
            ret = true;
        } else {
            // 新規空レコード作成
            CreateNewRecode(name);
            // 保存処理(作成とおなじでいいのかもね)
            ret = false;
        }
        return ret;
    }
}

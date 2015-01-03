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
package com.chantake.MituyaProject.World;

import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldType;

/**
 * ワールドのデータクラス
 *
 * @author chantake
 */
public class WorldData {

    /**
     * ワールドID
     */
    private final int id;
    /**
     * World
     */
    private World world;
    /**
     * エリア制限
     */
    private final int border;
    /**
     * Mob制限（trueの場合湧かない）
     */
    private final boolean mob;
    /**
     * コマンド制限 (trueの場合デフォルトで全制限)
     */
    private final boolean command;
    /*
     * type
     */
    private final String type;

    /**
     * インスタンス作成
     *
     * @param id ワールドID
     * @param wd World
     * @param type
     * @param border エリア制限
     * @param mob mob制限
     * @param command
     */
    public WorldData(int id, World wd, String type, int border, boolean mob, boolean command) {
        this.id = id;
        this.world = wd;
        this.type = type;
        this.border = border;
        this.mob = mob;
        this.command = command;
    }

    /**
     * Environment(ワールドの環境)を取得します
     *
     * @return Environment
     * @see Environment
     */
    public Environment getEnvironment() {
        return this.world.getEnvironment();
    }

    /**
     * WorldTypeを取得します
     *
     * @return WorldType
     * @see WorldType
     */
    public WorldType getWorldType() {
        return this.world.getWorldType();
    }

    /**
     * Worldを取得します
     *
     * @return World
     * @see World
     */
    public World getWorld() {
        return this.world;
    }

    /**
     * Worldを設定します
     *
     * @param wd World
     * @see World
     */
    @Deprecated
    public void setWorld(World wd) {
        this.world = wd;
    }

    /**
     * Worldに難易度を設定
     *
     * @param wd Difficulty
     * @see Difficulty
     */
    public void setDifficulty(Difficulty wd) {
        this.world.setDifficulty(wd);
    }

    /**
     * Worldの難易度を取得
     *
     * @return Difficulty
     * @see Difficulty
     */
    public Difficulty getDifficulty() {
        return this.world.getDifficulty();
    }

    /**
     * エリア制限を取得します
     *
     * @return 制限する半径を返します <br /> 例）5000の場合<br /> 5000x2=10000 10000x10000となり<br/> x座標が-5000から5000までとなります
     */
    public int getBorder() {
        return this.border;
    }

    /**
     * Mob制限を取得します
     *
     * @return trueの場合Mobを制限します
     */
    public boolean isMobStop() {
        return mob;
    }

    /**
     * ワールドIDを取得します
     *
     * @return ワールドID
     */
    public int getId() {
        return id;
    }

    /**
     * コマンドを制限してるかどうかを取得します
     *
     * @return 制限してる場合はtrue
     */
    public boolean isCommand() {
        return command;
    }

    public String getType() {
        return type;
    }

}

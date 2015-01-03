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
package com.chantake.MituyaProject.Permissions;

/**
 *
 * @author chantake
 */
public enum Rank {

    Admin(23, "admin", "管理者"),
    SubAdmin(22, "subadmin", "副管理者"),
    SuperGM(21, "supergm", "ゲームマスター"),
    GM(20, "gm", "ゲームマスター"),
    ModeratorReader(11, "moderatorreader", "モデレーターリーダー"),
    Moderator(10, "moderator", "モデレーター"),
    Supporter(5, "supporter", "サポーター"),
    Ust(1, "ust", "生放送主"),
    Niconama(1, "nico2", "生放送主"),
    LiveReporter(1, "live", "生放送主"),
    Default(0, "default", "デフォルト");

    private final int rank;
    private final String permission;
    private final String name;
    private final String mean;

    private Rank(int rank, String name, String mean) {
        this.rank = rank;
        this.name = name;
        this.permission = "mituya.rank." + name;
        this.mean = mean;
    }

    public int getId() {
        return this.rank;
    }

    public String getPermission() {
        return this.permission;
    }

    public String getPermissionName() {
        return this.toString();
    }

    public String getName() {
        return name;
    }

    public String getMean() {
        return this.permission;
    }

    public static Rank getRank(int rank) {
        for (Rank rk : Rank.values()) {
            if (rk.getId() == rank) {
                return rk;
            }
        }
        return Rank.Default;
    }

    public static Rank getRank(String permission) {
        for (Rank rk : Rank.values()) {
            if (rk.getName().equalsIgnoreCase(permission)) {
                return rk;
            }
        }
        for (Rank rk : Rank.values()) {
            if (rk.getPermission().endsWith(permission)) {
                return rk;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.name + "(" + this.mean + ")";
    }

}

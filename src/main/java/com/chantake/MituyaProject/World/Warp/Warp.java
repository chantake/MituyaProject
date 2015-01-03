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
package com.chantake.MituyaProject.World.Warp;

import com.chantake.MituyaProject.MituyaProject;

/**
 *
 * @author chantake-mac
 */
public class Warp {

    private static Warp instance = null;
    private static MituyaProject plugin;

    public Warp(MituyaProject plugin) {
        Warp.plugin = plugin;
    }

    public static enum Permission {

        NORMAL(0),
        PUBLIC(1),
        PRIVATE(2),
        PASSWORD(3),
        CHARGE(4),
        GM_ONLY(10),
        Admin_ONLY(11),
        GUILD_ONLY(12);

        private Permission(int rank) {
            this.rank = rank;
        }

        public int getValue() {
            return this.rank;
        }

        public static Permission getPermission(int var) {
            for (Permission pr : Permission.values()) {
                if (pr.getValue() == var) {
                    return pr;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            return this.name() + ":" + rank;
        }
        private final int rank;
    }

    public static Warp getInstance(MituyaProject pl) {
        if (instance == null) {
            instance = new Warp(pl);
        }
        return instance;
    }

    public WarpData getWarpData(String name) {
        return null;
    }

    public void SaveWarpDataToDB(WarpData wd, boolean update) {

    }
}

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

import com.chantake.MituyaProject.Exception.PlayerOfflineException;
import com.chantake.MituyaProject.Player.LocationData;
import com.chantake.MituyaProject.Player.PlayerInstance;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author chantake-mac
 */
public class WarpManager {

    private static final Map<String, LocationData> warpdata = new HashMap<>();

    public static LocationData getWarpData(String name) {
        //ワープが存在する場合
        if (warpdata.containsKey(name)) {
            return warpdata.get(name);
        } else {
            return null;
        }
    }

    public static void WarpProcessor(PlayerInstance ins, String name) throws PlayerOfflineException {
        LocationData ld = getWarpData(name);
        if (ld == null) {
            ins.sendAttention("ワープゾーン:" + name + " はみつかりませんでした");
        } else {
            Object jsono = JSONValue.parse(ld.getPermission());
            if (jsono instanceof JSONObject) {
                JSONObject json = (JSONObject)jsono;
            }
        }
    }

}

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

import com.chantake.MituyaProject.Player.LocationData;
import com.chantake.MituyaProject.World.Warp.Warp.Permission;
import org.bukkit.Location;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author chantake-mac
 */
class WarpData {

    private Warp warp;
    private LocationData ld = null;
    private Permission permission = null;

    public WarpData(Warp warp, LocationData ld) {
        this.warp = warp;
        this.ld = ld;
    }

    public Location getLocation() {
        return this.ld.getLocation();
    }

    public void setLocation(Location location) {
        this.ld.setLocation(location);
    }

    public Permission getPermission() {
        return permission;
    }

    /**
     * 権限を設定します
     *
     * @see Warp.Permission
     * @param permission Permission
     */
    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public void Save() {
        JSONObject json = new JSONObject();
        json.put("permission", this.permission.getValue());
        JSONArray array = new JSONArray();

        this.ld.setPermission(json.toJSONString());
    }

    public void LoadWarpData(String data) {
        JSONObject json = (JSONObject)JSONValue.parse(data);
        this.permission = Warp.Permission.getPermission(new Integer(json.get("permission").toString()));

    }

}

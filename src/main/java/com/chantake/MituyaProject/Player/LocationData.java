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
package com.chantake.MituyaProject.Player;

import org.bukkit.Location;

/**
 * Location(homeなどの設定付き）を格納するデータクラス
 *
 * @author chantake
 */
public class LocationData {

    public LocationData(Location location, int id) {
        this.location = location;
        this.id = id;
    }
    private int id;
    private boolean location_public;
    private String message;
    private Location location;
    private String permission = "null";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean getPublic() {
        return this.location_public;
    }

    public void setPublic(boolean location_public) {
        this.location_public = location_public;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    @Override
    public String toString() {
        return "[id:" + this.getId() + " location:" + this.getLocation().toString() + " public:" + this.getPublic() + "]";
    }

}

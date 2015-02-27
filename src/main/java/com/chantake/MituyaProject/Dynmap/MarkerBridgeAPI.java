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
package com.chantake.MituyaProject.Dynmap;

import com.chantake.MituyaProject.Player.LocationData;
import com.chantake.MituyaProject.Util.MySqlProcessing;
import com.chantake.MituyaProject.World.WorldData;
import org.bukkit.Location;
import org.dynmap.markers.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author fumitti
 */
public class MarkerBridgeAPI {

    private final DynmapApiConnecter apiConnecter;
    private final MarkerAPI markerAPI;
    private final String HOME_MARKER_SETID = "homes";
    private final String ChestShop_MARKER_SETID = "chestshop";
    private final Map<WorldData, AreaMarker> square_borders = new HashMap<>();

    public MarkerBridgeAPI(DynmapApiConnecter api) {
        this.apiConnecter = api;
        this.markerAPI = api.getDynmapAPI().getMarkerAPI();
        //初期化
        this.init();
    }

    private void init() {
        //home markerを表示
        addHomeMarkerAll();
        addAllWorldBoderMarker();
    }

    /**
     * Home用のMarkerSetを取得します
     *
     * @return MarkerSet
     */
    private MarkerSet getHomeMarkerSet() {
        MarkerSet markerSet = markerAPI.getMarkerSet(HOME_MARKER_SETID);
        if (markerSet == null) {
            markerSet = markerAPI.createMarkerSet(HOME_MARKER_SETID, "Home", null, false);
            markerSet.setHideByDefault(true);
        }
        return markerSet;
    }

    /**
     * Markerを作成します
     *
     * @param id markerid
     * @param location Location
     * @param icon MarkerIcon
     * @param set MarkerSet
     * @param label String
     */
    private void createMarker(String id, Location location, MarkerIcon icon, MarkerSet set, String label) {
        set.createMarker(id, label, location.getWorld().getName(), location.getX() + 0.5, location.getY() + 0.5, location.getZ() + 0.5, icon, true);
    }

    /**
     * 看板のMarkerを作成します
     *
     * @param location Location
     * @param label
     */
    public void createSignMarker(Location location, String label) {
        this.createMarker(getSignMarkerID(location), location, markerAPI.getMarkerIcon(MarkerIcon.SIGN), markerAPI.getMarkerSet(MarkerSet.DEFAULT), label);
    }

    public void createTempMarker(Location loc, MarkerIcon icon, MarkerSet set, String label) {
        set.createMarker(getSignMarkerID(loc), label, loc.getWorld().getName(), loc.getX() + 0.5, loc.getY() + 0.5, loc.getZ() + 0.5, icon, false);
    }

    public void createTempMarker(Location loc, String Label) {
        createTempMarker(loc, markerAPI.getMarkerIcon(MarkerIcon.SIGN), markerAPI.getMarkerSet(MarkerSet.DEFAULT), Label);
    }

    /**
     * HomeMarkerを作成します
     *
     * @param playerid PlayerID
     * @param ld LocationData
     */
    public void createHomeMarker(String playerid, LocationData ld) {
        StringBuilder label = new StringBuilder();
        //label作成
        label.append(playerid).append("(").append(ld.getId()).append(")のホーム");
        this.createMarker(this.getHomeMarkerID(playerid, ld), ld.getLocation(), markerAPI.getMarkerIcon("house"), getHomeMarkerSet(), label.toString());
    }

    /**
     * Markerを削除します
     *
     * @param playerid PlayerID
     * @param ld LocationData
     */
    public void deleteHomeMarker(String playerid, LocationData ld) {
        deleteMarker(this.getHomeMarkerID(playerid, ld));
    }

    /**
     * Markerを削除します
     *
     * @param id MarkerID
     */
    public void deleteMarker(String id) {
        Set<MarkerSet> markerSets = markerAPI.getMarkerSets();
        for (MarkerSet markerSet : markerSets) {
            Marker findMarker = markerSet.findMarker(id);
            if (findMarker != null) {
                findMarker.deleteMarker();
            }
        }
    }

    /**
     * MarkerIDを作成します
     *
     * @param location Location
     * @return 328_markertype_worldID_x_y_z
     */
    private String getSignMarkerID(Location location) {
        StringBuilder sb = this.getMarkerType(MarkerType.Sign);
        //328_markertype_worldID_x_y_z
        sb.append(this.apiConnecter.getPlugin().getWorldManager().getWorldData(location.getWorld()).getId()).append("_").append(location.getBlockX()).append("_").append(location.getBlockY()).append("_").append(location.getBlockZ());
        return sb.toString();
    }

    /**
     * MarkerIDを作成します
     *
     * @param playerid PlayerID
     * @param ld LocationData
     * @return 328_markertype_worldID_playerid_homeid
     */
    private String getHomeMarkerID(String playerid, LocationData ld) {
        StringBuilder sb = this.getMarkerType(MarkerType.Home);
        //328_markertype_worldID_playerid_homeid
        sb.append(this.apiConnecter.getPlugin().getWorldManager().getWorldData(ld.getLocation().getWorld()).getId()).append("_").append(playerid).append("_").append(ld.getId());
        return sb.toString();
    }

    /**
     * MarkerTypeを取得します
     *
     * @param mt MarkerType マーカーの種類
     * @see MarkerType
     * @return StringBuilder
     */
    private StringBuilder getMarkerType(MarkerType mt) {
        StringBuilder sb = new StringBuilder();
        sb.append("328_").append(mt.getValue()).append("_");
        return sb;
    }

    private void addHomeMarkerAll() {
        MySqlProcessing.addHomeMarkerAll(this);
    }

    private void addAllWorldBoderMarker() {
        MarkerSet createMarkerSet = markerAPI.createMarkerSet("mituya.worldborder.markerset", "境界", null, false);
        for (WorldData data : this.apiConnecter.getPlugin().getWorldManager().getWorldDatas()) {
            AreaMarker marker = square_borders.get(data);
            String name = data.getWorld().getName();
            double[] xVals = {-data.getBorder(), data.getBorder()};
            double[] zVals = {-data.getBorder(), data.getBorder()};
            if (marker == null) {
                marker = createMarkerSet.createAreaMarker("worldborder_" + name, "ワールドの境界 : " + (data.getBorder() * 2) + "x" + (data.getBorder() * 2), false, name, xVals, zVals, true);
                marker.setDescription("<div class=\"infowindow\">ワールドの境界<br />" + (data.getBorder() * 2) + "x" + (data.getBorder() * 2) + "</div>");
                marker.setLineStyle(3, 1.0, 0xFF0000);
                marker.setFillStyle(0.0, 0x000000);
                this.square_borders.put(data, marker);
            } else {
                marker.setCornerLocations(xVals, zVals);
            }
        }
    }

    /**
     * Markerの種類
     */
    public enum MarkerType {

        Sign(0),
        Home(1),
        ChestShop(2),
        Other(-1);
        private final int value;

        private MarkerType(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }
}

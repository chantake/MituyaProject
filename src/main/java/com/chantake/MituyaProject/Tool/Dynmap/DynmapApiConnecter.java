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
package com.chantake.MituyaProject.Tool.Dynmap;

import com.chantake.MituyaProject.MituyaProject;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.dynmap.DynmapAPI;
import org.dynmap.DynmapCommonAPI;

/**
 *
 * @author chantake
 */
public class DynmapApiConnecter {

    private final MituyaProject plugin;
    private DynmapCommonAPI common = null;
    private DynmapAPI dynmap = null;
    private final DynmapListener listener = new DynmapListener(this);
    private MarkerBridgeAPI marker = null;

    public DynmapApiConnecter(MituyaProject plugin) throws NoClassDefFoundError {
        this.plugin = plugin;
    }

    /**
     * 初期化
     */
    public void init() {
        PluginManager pm = this.plugin.getServer().getPluginManager();
        Plugin pg = pm.getPlugin("dynmap");
        if (pg != null) {
            this.plugin.Log("Dynmap が見つかりました。");
            if (!pg.isEnabled()) {//プラグインがロードされてない場合はロードさせる
                pm.enablePlugin(pg);//ロード
            }
            this.dynmap = (DynmapAPI)pg;
            this.common = (DynmapCommonAPI)dynmap;
            //Listener登録
            plugin.getServer().getPluginManager().registerEvents(listener, plugin);
            //マーカー
            this.marker = new MarkerBridgeAPI(this);
        } else {
            this.plugin.Log("Dynmap が見つかりませんでした。");
        }
    }

    public DynmapCommonAPI getCommon() {
        return common;
    }

    public DynmapAPI getDynmapAPI() {
        return dynmap;
    }

    public MarkerBridgeAPI getMarker() {
        return marker;
    }

    public MituyaProject getPlugin() {
        return plugin;
    }
}

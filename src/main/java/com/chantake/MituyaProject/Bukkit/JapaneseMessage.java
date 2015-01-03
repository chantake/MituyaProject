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
package com.chantake.MituyaProject.Bukkit;

import com.chantake.MituyaProject.MituyaProject;
import com.chantake.mituyaapi.tools.database.JDCConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONValue;

/**
 *
 * @author chantake
 */
public class JapaneseMessage {

    private final MituyaProject plugin;
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, String>> message = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, String>> msg_id = new ConcurrentHashMap<>();

    public JapaneseMessage(MituyaProject plugin) {
        this.plugin = plugin;
    }

    public void init() {
        this.plugin.Log("日本語メッセージ 初期化開始");
        //load japanese message
        this.loadAllMessageFromDB();
        int ids = 0;
        for (ConcurrentHashMap<String, String> map : this.msg_id.values()) {
            ids += map.size();
        }
        this.plugin.Log("日本語メッセージ 初期化完了 ids:" + ids);
    }

    /**
     * DBからすべてのメッセージを読み込みます
     */
    private void loadAllMessageFromDB() {
        try (JDCConnection con = plugin.getConnection(); PreparedStatement ps = con.prepareStatement("SELECT * FROM `jp_message`"); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                this.addData(rs.getString("type"), rs.getString("id"), rs.getString("message"), rs.getString("aliases"));
            }
        }
        catch (SQLException e) {
            plugin.ErrLog("Missing load jp_message :" + e);
        }
    }

    public void addData(String type, String id, String message, String aliases) {
        ArrayList<String> list = null;
        if (aliases != null) {
            list = (ArrayList<String>)JSONValue.parse(aliases);
        }
        this.addData(type, id, message, list);
    }

    /**
     * データを追加します
     *
     * @param type
     * @param id
     * @param message
     */
    private void addData(String type, String id, String message, ArrayList<String> aliases) {
        ConcurrentHashMap<String, String> map;
        if (!this.message.containsKey(type)) {
            map = new ConcurrentHashMap<>();
        } else {
            map = this.message.get(type);
        }
        //mapにidと紐付ける
        map.put(message, id);
        //エイリアスも追加
        if (aliases != null && aliases.size() > 0) {
            for (String msg : aliases) {
                map.put(msg, id);
            }
        }
        //mapにつっこむ
        this.message.putIfAbsent(type, map);

        ConcurrentHashMap<String, String> map2;
        if (!this.msg_id.containsKey(type)) {
            map2 = new ConcurrentHashMap<>();
        } else {
            map2 = this.msg_id.get(type);
        }
        //mapにidと紐付ける
        map2.put(id, message);
        //mapにつっこむ
        this.msg_id.putIfAbsent(type, map2);
    }

    /**
     * 日本語のアイテム名を返します
     *
     * @param id アイテムID 例 35:1
     * @return 見つからなかった場合は英語を返します
     */
    public String getItemName(String id) {
        String name = this.getMessage("item", id);
        if (name == null) {
            name = ItemName.getItemName(id);
        }
        return name;
    }

    public String getItemName(ItemStack is) {
        String id = String.valueOf(is.getTypeId());
        if (is.getDurability() != 0) {
            id += ":" + is.getDurability();
        }
        return this.getItemName(id);
    }

    public String getItemID(String name) {
        return this.getID("item", name);
    }

    /**
     * 日本語のEntity名を返します
     *
     * @param entity_type
     * @return 見つからなかった場合は英語を返します
     */
    public String getEntityName(EntityType entity_type) {
        String name = this.getMessage("entity", String.valueOf(entity_type.getTypeId()));
        if (name == null) {
            name = entity_type.getName();
        }
        return name;
    }

    public String getEntityName(String id) {
        return this.getMessage("entity", id);
    }

    public String getEntityID(String name) {
        return this.getID("entity", name);
    }

    public String getID(String type, String name) {
        try {
            return this.message.get(type).get(name);
        }
        catch (NullPointerException ex) {
            return null;
        }
    }

    public String getMessage(String type, String id) {
        try {
            String get = this.msg_id.get(type).get(id);
            if (get != null) {
                return get;
            }
            return this.msg_id.get(type).get(id.toLowerCase());
        }
        catch (NullPointerException ex) {
            return null;
        }
    }
}

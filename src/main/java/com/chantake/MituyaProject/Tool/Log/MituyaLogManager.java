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
package com.chantake.MituyaProject.Tool.Log;

import com.chantake.MituyaProject.MituyaManager;
import com.chantake.MituyaProject.MituyaProject;
import com.chantake.mituyaapi.tools.database.JDCConnection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

/**
 *
 * @author chantake
 */
public class MituyaLogManager extends MituyaManager {

    public MituyaLogManager(MituyaProject plugin) {
        super(plugin);
    }

    @Override
    public void init() {
        this.getPlugin().Log("[MituyaLogManager] 初期化開始");
        createDBTable();
        this.getPlugin().Log("[MituyaLogManager] 初期化完了");
    }

    private void createDBTable() {
        try (JDCConnection con = this.getPlugin().getConnection(); Statement st = con.createStatement();) {
            st.executeUpdate("CREATE TABLE IF NOT EXISTS log_data (id INT(11) NOT NULL AUTO_INCREMENT, type_id INT(11) NOT NULL, `key` VARCHAR(50) NOT NULL, data VARCHAR(255), `date` DATETIME, PRIMARY KEY (id))");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS log_type (id INT(11) NOT NULL, type VARCHAR(20) NOT NULL, PRIMARY KEY (id))");
            st.executeUpdate("TRUNCATE TABLE log_type");
            for (LogType type : LogType.values()) {
                st.executeUpdate("insert into log_type (id,type) values(" + type.getId() + ",'" + type.name() + "')");
            }
        }
        catch (final SQLException ex) {
            System.out.println(ex);
        }
    }

    public void putLog(LogType type, String key, Object[] data) {
        this.putLog(type, key, Arrays.asList(data));
    }

    public void putLog(LogType type, String key, List data) {
        this.putLog(type, key, JSONArray.toJSONString(data));
    }

    public void putLog(LogType type, String key, Map data) {
        this.putLog(type, key, JSONValue.toJSONString(data));
    }

    public void putLog(LogType type, String key, String data) {
        this.putLogToDB(type.getId(), key, data);
    }

    private void putLogToDB(int type, String key, String data) {
        try {
            try (JDCConnection con = this.getPlugin().getConnection(); PreparedStatement ps = con.prepareStatement("insert into log_data (type_id, `key`, data, `date`) values(?,?,?,now())");) {
                // パラメータをセット
                ps.setInt(1, type);
                ps.setString(2, key);
                ps.setString(3, data);

                // データ作成が正常か判定
                if (ps.executeUpdate() < 0) {
                    throw new SQLException();
                }
            }
        }
        catch (SQLException ex) {
            this.getPlugin().Log("MituyaLogManager Err: " + ex);
        }
    }
}

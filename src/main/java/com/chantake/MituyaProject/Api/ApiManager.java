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
package com.chantake.MituyaProject.Api;

import com.chantake.MituyaProject.MituyaProject;
import com.chantake.mituyaapi.tools.database.JDCConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * Apiを管理するクラス
 *
 * @author chantake
 */
public class ApiManager {

    private final MituyaProject plugin;

    public ApiManager(MituyaProject plugin) {
        this.plugin = plugin;
    }

    private final ArrayList<ApiData> apidata = new ArrayList<>();

    /**
     * ApiDataを取得します
     *
     * @param tokenKey Apikey
     * @param tokenSecret
     * @return 存在する場合はApiDataを、存在しない場合はDBから検索し、それでもみつからない場合はnullを返します
     */
    public ApiData getApiData(String tokenKey, String tokenSecret) {
        //ロードしていない場合
        ApiData loadedApiData = this.getLoadedApiData(tokenKey, tokenSecret);
        if (loadedApiData != null) {
            return loadedApiData;
        }
        ApiData loadApiData = this.loadApiDataFromDB(tokenKey, tokenSecret);
        if (loadApiData != null) {
            this.apidata.add(loadApiData);
            return loadApiData;
        }
        return null;
    }

    /**
     * ロードしてる場合はApiDataが返ります
     *
     * @param tokenKey
     * @param tokenSecret
     * @return ロードしてない場合はnullが返ります
     */
    private ApiData getLoadedApiData(String tokenKey, String tokenSecret) {
        for (ApiData data : apidata) {
            if (data.getTokenKey().equals(tokenKey) && data.getTokenSecret().equals(tokenSecret)) {
                return data;
            }
        }
        return null;
    }

    /**
     * ApiDataを作成します
     *
     * @param autor 作者
     * @return 作成に成功した場合はApiDataを、失敗した場合はnullが返ります
     */
    public ApiData CreateApiData(String autor) {
        String tokenKey = RandomStringUtils.randomAlphanumeric(20);//20文字
        String tokenSecret = RandomStringUtils.randomAlphanumeric(20);//20文字
        ApiData apiData = new ApiData("", autor, "", "", "", tokenKey, tokenSecret);
        try {
            this.SaveApiData(apiData, false);
            return apiData;
        }
        catch (Exception ex) {
            Logger.getLogger(ApiManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * ApiDataをDBに保存します
     *
     * @param apidata ApiData
     * @param update 新規追加の場合は false 、更新する場合は true
     * @throws Exception
     */
    public void SaveApiData(ApiData apidata, boolean update) throws Exception {
        try (JDCConnection con = plugin.getConnection()) {
            PreparedStatement ps;
            if (update) {
                ps = con.prepareStatement("UPDATE api SET `name` = ?, `ver` = ?, `autor` = ?, `web` = ?, `description` = ? WHERE `tokenKey` = ? AND `tokenSecret` = ?");
                ps.setString(1, apidata.getName());
                ps.setString(2, apidata.getVer());
                ps.setString(3, apidata.getAutor());
                ps.setString(4, apidata.getWeb());
                ps.setString(5, apidata.getDescription());
                ps.setString(6, apidata.getTokenKey());
                ps.setString(7, apidata.getTokenSecret());
            } else {
                ps = con.prepareStatement("INSERT INTO api (`tokenKey`,`tokenSecret`,`name`, `ver`, `autor`, `web`, `description`) VALUES (?,?,?,?,?,?,?)");
                ps.setString(1, apidata.getTokenKey());
                ps.setString(2, apidata.getTokenSecret());
                ps.setString(3, apidata.getName());
                ps.setString(4, apidata.getVer());
                ps.setString(5, apidata.getAutor());
                ps.setString(6, apidata.getWeb());
                ps.setString(7, apidata.getDescription());
            }
            int updateRows = ps.executeUpdate();
            if (updateRows < 1) {
                throw new RuntimeException("apidata not in database (" + apidata.getName() + ")");
            }
            ps.close();
        }
    }

    /**
     * ApiDataをDBから読み込みます
     *
     * @param key Apikey
     * @return 存在する場合はApiDataを、ない場合はnullを返します
     */
    private ApiData loadApiDataFromDB(String tokenKey, String tokenSecret) {
        try {
            ApiData api;
            try (JDCConnection c = plugin.getConnection()) {
                final ResultSet rs;
                try (PreparedStatement ps = c.prepareStatement("SELECT * FROM api WHERE `tokenKey` = ? AND `tokenSecret` = ?")) {
                    ps.setString(1, tokenKey);
                    ps.setString(2, tokenSecret);
                    rs = ps.executeQuery();
                    if (!rs.next()) {
                        rs.close();
                        ps.close();
                        return null;
                    }
                    api = new ApiData(rs.getString("name"), rs.getString("ver"), rs.getString("autor"), rs.getString("web"), rs.getString("description"), tokenKey, tokenSecret);
                }
                rs.close();
            }
            return api;
        }
        catch (final SQLException ex) {
            return null;
        }
    }
}

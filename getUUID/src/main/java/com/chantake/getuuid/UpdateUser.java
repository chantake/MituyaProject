/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.chantake.getuuid;

import com.chantake.mituyaapi.tools.database.DatabaseConnectionManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.Security;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author chantake
 */
public class UpdateUser {
    
    private final String url = " https://api.mojang.com/users/profiles/minecraft/";
    private DatabaseConnectionManager manager;

    public UpdateUser(String[] args) {
        MituyaLogger.info("test");
        Security.setProperty("jdk.certpath.disabledAlgorithms", "");
        manager = new DatabaseConnectionManager(args[0], args[1], args[2]);
        System.out.println("url: " + args[0]);
        System.out.println("id: " + args[1]);
        System.out.println("pass: " + args[2]);
        try {
            manager.getConnection().close();
            MituyaLogger.info("初期完了");
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        getAllPlayerUUID();
    }
    
    
    public void getAllPlayerUUID() {
        int amount = 0;
        try {
            try (Connection con = manager.getConnection(); PreparedStatement cr = con.prepareStatement("SELECT id,`name`,`uuid` FROM `characters`")) {
                ResultSet rs = cr.executeQuery();
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    ++amount;
                    byte[] uid = rs.getBytes("uuid");
                    if(uid != null && uid[0] != 0) {
                        System.out.println("Skip " + name + "(" + id + ") : " + convertToEntityAttribute(uid).toString());
                        continue;
                    }
                    UUID uuid = getUUIDFromName(name);
                    if (uuid == null) {
                        System.out.println("uuid取得できませんでした");
                        continue;
                    }
                    updatePlayerData(id, uuid);
                    System.out.println("GET " + name + "(" + id + ") : " + uuid.toString());
                    if((amount % 100) != 0) {
                        sleep(300);
                    } else {
                        sleep(1000 * 30);
                    }
                }
                rs.close();
            }
            System.out.println("uuid取得完了 " + amount + " 件");
        } catch (SQLException e) {
            System.out.println("getAllPlayerUUID えらー:" + e);
        }
    }

    public synchronized void sleep(long msec) {	//指定ミリ秒実行を止めるメソッド
        try {
            wait(msec);
        } catch (InterruptedException e) {
        }
    }

    public Boolean updatePlayerData(int id, UUID uuid) {
        try {
            try (Connection con = manager.getConnection()) {
                PreparedStatement ps;
                ps = con.prepareStatement("UPDATE `characters` SET `uuid` = ? WHERE id = ?");
                ps.setBytes(1, convertToDatabaseColumn(uuid));
                ps.setInt(2, id);
                int updateRows = ps.executeUpdate();
                if (updateRows < 1) {
                    System.out.println("updatePlayerData not in database (" + id + ")");
                }
                ps.close();
                return true;
            }
        } catch (SQLException ex) {
            System.out.println("updatePlayerData えらー:" + ex);
        }
        return false;
    }

    public byte[] convertToDatabaseColumn(UUID attribute) {
        if (attribute == null) {
            return null;
        }

        return ByteBuffer.allocate(16)
                .putLong(attribute.getMostSignificantBits())
                .putLong(attribute.getLeastSignificantBits())
                .array();
    }

    public UUID convertToEntityAttribute(byte[] dbData) {
        if (dbData == null) {
            return null;
        }

        ByteBuffer buffer = ByteBuffer.wrap(dbData);
        long most = buffer.getLong();
        long least = buffer.getLong();
        return new UUID(most, least);
    }

    public JSONObject readJsonFromUrl(String url) throws IOException, ParseException {
        try (InputStream is = new URL(url).openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            //System.out.println("text:"+jsonText);
            return (JSONObject) new JSONParser().parse(jsonText);
        }
    }

    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public UUID getUUIDFromName(String name) {
        JSONObject json;
        String u = url + name + "?at=" + System.currentTimeMillis();
        try {
            json = readJsonFromUrl(u);
        } catch (IOException | ParseException ex) {
            System.out.println("err getUUIDFromName:" + ex);
            return null;
        }
        for (int i = 0; i < 10; i++) {

        }
        return getUUID((String) json.get("id"));
    }

    private UUID getUUID(String id) {
        //System.out.println("uuid:"+id);
        return UUID.fromString(id.substring(0, 8) + "-" + id.substring(8, 12) + "-" + id.substring(12, 16) + "-" + id.substring(16, 20) + "-" + id.substring(20, 32));
    }
}

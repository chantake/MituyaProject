/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chantake.getuuid;

import com.chantake.mituyaapi.tools.database.DatabaseConnectionManager;
import com.mysql.jdbc.Statement;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.Security;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author chantake
 */
public class GetLinkShellList {

    private static final WorldName world = WorldName.Gungnir;
    private static int total_linkshells;
    private static final String numbers = "0123456789";
    private static final String alphabets = "abcdefghijklmnopqrstuvwxyz";
    private static final String symbols = "'.,:!?-_ ";
    private static final int MAX_COUNT = 1000;
    private static final int ORDER = 2;
    private static final String texts = numbers + alphabets /*+ symbols*/;

    private DatabaseConnectionManager manager;

    public GetLinkShellList(String[] args) {
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
        this.init();
    }

    public void init() {
        getLinkShellList();
        getLinkShell();
    }

    public void getLinkShell() {
        int amount = 0;
        try {
            try (Connection con = manager.getConnection(); PreparedStatement cr = con.prepareStatement("SELECT id,`name`,`count`,`link` FROM `linkshells`")) {
                ResultSet rs = cr.executeQuery();
                while (rs.next()) {
                    LinkShell ls = new LinkShell(rs.getString("id"), rs.getString("name"), rs.getInt("count"), rs.getString("link"));
                    ++amount;
                    System.out.println("GET " + ls.getName() + "(" + ls.getId() + ") count: " + ls.getCount());
                    getPlayerList(ls);
                    sleep(1300);
                }
                rs.close();
            }
            System.out.println("LinkShell " + amount + " 件");
        } catch (SQLException e) {
            System.out.println("getLinkShell えらー:" + e);
        }
    }

    public void getPlayerList(LinkShell ls) {
        System.out.println("getPlayerList name:" + ls.getName() + " count:" + ls.getCount());
        for (int i = 0; i * 50 < ls.getCount(); i++) {
            System.out.println((i + 1) * 50 + "/" + ls.getCount() + " page:" + (i + 1));
            getPlayerList(ls, String.valueOf(i + 1));
        }
    }

    public void getPlayerList(LinkShell ls, String page) {
        JSONObject json;
        try {
            String url = "http://pipes.yahoo.com/pipes/pipe.run?_id=12c646a2955252c0df71f9f494105488&_render=json&id=" + ls.getId() + "&page=" + page;
            json = readJsonFromUrl(url);
        } catch (IOException | ParseException ex) {
            return;
        }
        JSONObject data = (JSONObject) json.get("value");
        JSONArray items = (JSONArray) data.get("items");
        for (int i = 0; i < items.size(); i++) {
            HashMap item;
            try {
                item = (HashMap) items.get(i);
            } catch (IndexOutOfBoundsException e) {
                return;
            }
            Player pr = new Player(item);
            savePlayer(ls, pr);
            System.out.println(pr.toString());
        }
    }

    public void getLinkShellList() {
        total_linkshells = getLinkShellsCount(null, world, null);
        System.out.println("total LinkShells:" + total_linkshells);
        
        for (int i = 0; i < texts.length(); i++) {
            if (getLinkShellsCount(String.valueOf(texts.charAt(i)), "1-10") > MAX_COUNT) {
                getLinkShellsCountLoop(String.valueOf(texts.charAt(i)), "1-10");
            }
        }

        /*
        //数字
        for (int i = 0; i < numbers.length(); i++) {
            String t = String.valueOf(numbers.charAt(i));
            if (getLinkShellsCount(t, "1-10") > MAX_COUNT) {
                //数字
                for (int ii = 0; ii < numbers.length(); ii++) {
                    getLinkShellsCount(t + numbers.charAt(i), "1-10");
                }
                //アルファベット
                for (int ii = 0; ii < alphabets.length(); ii++) {
                    getLinkShellsCount(t + alphabets.charAt(i), "1-10");
                }
                //記号
                for (int ii = 0; ii < symbols.length(); ii++) {
                    getLinkShellsCount(t + symbols.charAt(i), "1-10");
                }
            }
        }
        //アルファベット
        for (int i = 0; i < alphabets.length(); i++) {
            String t = String.valueOf(alphabets.charAt(i));
            if (getLinkShellsCount(t, "1-10") > MAX_COUNT) {
                //数字
                for (int ii = 0; ii < numbers.length(); ii++) {
                    getLinkShellsCount(t + numbers.charAt(ii), "1-10");
                }
                //アルファベット
                for (int ii = 0; ii < alphabets.length(); ii++) {
                    getLinkShellsCount(t + alphabets.charAt(ii), "1-10");
                }
                //記号
                for (int ii = 0; ii < symbols.length(); ii++) {
                    getLinkShellsCount(t + symbols.charAt(ii), "1-10");
                }
            }
        }
        //記号
        for (int i = 0; i < symbols.length(); i++) {
            String t = String.valueOf(symbols.charAt(i));
            if (getLinkShellsCount(t, "1-10") > MAX_COUNT) {
                //数字
                for (int ii = 0; ii < numbers.length(); ii++) {
                    String tt = String.valueOf(numbers.charAt(ii));
                    if (getLinkShellsCount(t + tt, "1-10") > MAX_COUNT) {

                    }
                }
                //アルファベット
                for (int ii = 0; ii < alphabets.length(); ii++) {
                    String tt = String.valueOf(alphabets.charAt(ii));
                    getLinkShellsCount(t + tt, "1-10");
                }
                //記号
                for (int ii = 0; ii < symbols.length(); ii++) {
                    String tt = String.valueOf(symbols.charAt(ii));
                    getLinkShellsCount(t + tt, "1-10");
                }
            }
        }
        */
        //11-30
        getLinkShellsCount(null, "11-30");
        //31-50
        getLinkShellsCount(null, "31-50");
        //51-
        getLinkShellsCount(null, "51-");
    }

    private void getLinkShellsCountLoop(String t, String character_count) {
        String tt;
        for (int i = 0; i < texts.length(); i++) {
            tt = t + texts.charAt(i);
            if (getLinkShellsCount(tt, character_count) > MAX_COUNT) {
                getLinkShellsCountLoop(tt, character_count);
            }
        }
    }

    private void getLinkShells(String q, WorldName worldname, String character_count, int count) {
        System.out.println("getLinkShells q:" + q + " character_count:" + character_count + " count:" + count);
        for (int i = 0; i * 50 < count; i++) {
            System.out.println((i + 1) * 50 + "/" + count + " page:" + (i + 1));
            getLinkShells(q, worldname, character_count, String.valueOf(i + 1));
        }
    }

    private void getLinkShells(String q, WorldName worldname, String character_count, String page) {
        if (q == null) {
            q = "";
        }
        if (character_count == null) {
            character_count = "";
        }
        try {
            q = URLEncoder.encode(q, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(GetLinkShellList.class.getName()).log(Level.SEVERE, null, ex);
        }
        JSONObject json;
        try {
            json = readJsonFromUrl("http://pipes.yahoo.com/pipes/pipe.run?_id=980918c9b8204d550ce9ae7459f8ab69&_render=json&order="+ORDER+"&page=" + page + "&worldname=" + worldname.getName() + "&q=" + q + "&character_count=" + character_count);
        } catch (IOException | ParseException ex) {
            return;
        }
        JSONObject data = (JSONObject) json.get("value");
        JSONArray items = (JSONArray) data.get("items");
        for (int i = 0; i < items.size(); i++) {
            HashMap item;
            try {
                item = (HashMap) items.get(i);
            } catch (IndexOutOfBoundsException e) {
                return;
            }
            LinkShell linkshell = new LinkShell(item);
            saveLinkShell(linkshell);
            System.out.println(linkshell.toString());
        }
        sleep(1300);
    }

    private Boolean saveLinkShell(LinkShell ls) {
        try {
            try (Connection con = manager.getConnection()) {
                PreparedStatement ps;
                ps = con.prepareStatement("INSERT INTO linkshells (id,name,count,link) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, ls.getId());
                ps.setString(2, ls.getName());
                ps.setInt(3, ls.getCount());
                ps.setString(4, ls.getLink());
                int updateRows = ps.executeUpdate();
                if (updateRows < 1) {
                    System.out.println("saveLinkShell not in database");
                }
                ps.close();
                return true;
            }
        } catch (SQLException ex) {
            //System.out.println("saveLinkShell えらー:" + ex);
        }
        return false;
    }

    private Boolean savePlayer(LinkShell ls, Player pr) {
        try {
            try (Connection con = manager.getConnection()) {
                PreparedStatement ps;
                ps = con.prepareStatement("INSERT INTO players (id,ls_id,name,first_name,family_name,link) VALUES (?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, pr.getId());
                ps.setString(2, ls.getId());
                ps.setString(3, pr.getName());
                ps.setString(4, pr.getFirst_name());
                ps.setString(5, pr.getFamily_name());
                ps.setString(6, pr.getLink());
                int updateRows = ps.executeUpdate();
                if (updateRows < 1) {
                    System.out.println("savePlayer not in database");
                }
                ps.close();
                return true;
            }
        } catch (SQLException ex) {
            //System.out.println("saveLinkShell えらー:" + ex);
        }
        return false;
    }

    private int getLinkShellsCount(String q, String character_count) {
        int count = getLinkShellsCount(q, world, character_count);
        if (count <= MAX_COUNT && count >= 1) {
            getLinkShells(q, world, character_count, count);
        }
        return count;
    }

    private int getLinkShellsCount(String q, WorldName worldname, String character_count) {
        if (q == null) {
            q = "";
        }
        if (character_count == null) {
            character_count = "";
        }
        try {
            q = URLEncoder.encode(q, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(GetLinkShellList.class.getName()).log(Level.SEVERE, null, ex);
        }
        JSONObject json;
        try {
            String url = "http://pipes.yahoo.com/pipes/pipe.run?_id=3a7bcdf3b4df5de5bab2c67189c3613f&_render=json&order="+ORDER+"&worldname=" + worldname.getName() + "&q=" + q + "&character_count=" + character_count;
            System.out.println("url:" + url);
            json = readJsonFromUrl(url);
        } catch (IOException | ParseException ex) {
            return -1;
        }
        JSONObject data = (JSONObject) json.get("value");
        JSONArray items = (JSONArray) data.get("items");
        HashMap item;
        try {
            item = (HashMap) items.get(0);
        } catch (IndexOutOfBoundsException e) {
            return -1;
        }
        int count = Integer.valueOf((String) item.get("content"));
        //debug
        System.out.println("q:" + q + " character_count:" + character_count + " count:" + count);
        sleep(1500);
        return count;
    }

    public synchronized void sleep(long msec) {	//指定ミリ秒実行を止めるメソッド
        try {
            wait(msec);
        } catch (InterruptedException e) {
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

    public JSONObject readJsonFromUrl(String url) throws IOException, ParseException {
        try (InputStream is = new URL(url).openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            return (JSONObject) new JSONParser().parse(jsonText);
        }
    }

}

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
package com.chantake.MituyaProject.Player.Mail;

import com.chantake.MituyaProject.MituyaProject;
import com.chantake.mituyaapi.tools.database.JDCConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 *
 * @author chantake
 */
public class MailManager {

    private final MituyaProject plugin;

    public MailManager(MituyaProject plugin) {
        this.plugin = plugin;
    }

    /**
     * 未読メールを取得します
     *
     * @param player
     * @return
     */
    public MailData getNewMail(Player player) {
        MailData md = null;
        try {
            JDCConnection con = plugin.getConnection();
            try (PreparedStatement ps = con.prepareStatement("SELECT * FROM `mail` WHERE to_player = ? ORDER BY id DESC")) {
                ps.setString(1, player.getName());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        md = new MailData(rs.getInt("id"), rs.getString("from_player"), rs.getString("to_player"), rs.getString("subject"), rs.getString("text"), ((rs.getByte("unread") & 0xff) == 1), rs.getTimestamp("date"));
                    }
                }
            }
        }
        catch (SQLException e) {
            return null;
        }
        return md;
    }

    public MailData getMailFromID(Player player, int id) {
        MailData md = null;
        try {
            try (JDCConnection con = plugin.getConnection(); PreparedStatement ps = con.prepareStatement("SELECT * FROM `mail` WHERE to_player = ? AND id = ?")) {
                ps.setString(1, player.getName());
                ps.setInt(2, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        md = new MailData(rs.getInt("id"), rs.getString("from_player"), rs.getString("to_player"), rs.getString("subject"), rs.getString("text"), ((rs.getByte("unread") & 0xff) == 1), rs.getTimestamp("date"));
                    }
                }
            }
        }
        catch (SQLException e) {
            return null;
        }
        return md;
    }

    /**
     * 未読メールを取得します
     *
     * @param player
     * @param i
     * @return
     */
    public Iterator<MailData> getUnreadMail(Player player, int i) {
        ArrayList<MailData> data = new ArrayList<>();
        try {
            try (JDCConnection con = plugin.getConnection(); PreparedStatement ps = con.prepareStatement("SELECT * FROM `mail` WHERE to_player = ? ORDER BY id DESC LIMIT " + i + "," + 10)) {
                ps.setString(1, player.getName());
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        boolean unread = ((rs.getByte("unread") & 0xff) == 1);
                        if (unread) {
                            MailData md = new MailData(rs.getInt("id"), rs.getString("from_player"), rs.getString("to_player"), rs.getString("subject"), rs.getString("text"), unread, rs.getTimestamp("date"));
                            data.add(md);
                        }
                    }
                }
            }
        }
        catch (SQLException e) {
            return null;
        }
        return data.iterator();
    }

    public int getUnreadMailAmount(Player player) {
        int amount = 0;
        try {
            try (JDCConnection con = plugin.getConnection(); PreparedStatement ps = con.prepareStatement("SELECT * FROM `mail` WHERE to_player = ?")) {
                ps.setString(1, player.getName());
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        if (((rs.getByte("unread") & 0xff) == 1)) {
                            amount++;
                        }
                    }
                }
            }
        }
        catch (SQLException e) {
        }
        return amount;
    }

    public int getMailAmount(Player player) {
        int amount = 0;
        try {
            try (JDCConnection con = plugin.getConnection(); PreparedStatement ps = con.prepareStatement("SELECT * FROM `mail` WHERE to_player = ?")) {
                ps.setString(1, player.getName());
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        amount++;
                    }
                }
            }
        }
        catch (SQLException e) {
        }
        return amount;
    }

    public int getSendMailAmount(Player player) {
        int amount = 0;
        try {
            try (JDCConnection con = plugin.getConnection(); PreparedStatement ps = con.prepareStatement("SELECT * FROM `mail` WHERE from_player = ?")) {
                ps.setString(1, player.getName());
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        amount++;
                    }
                }
            }
        }
        catch (SQLException e) {
        }
        return amount;
    }

    /**
     * メールを取得します
     *
     * @param player
     * @param i
     * @return
     */
    public Iterator<MailData> getMail(Player player, int i) {
        ArrayList<MailData> data = new ArrayList<>();
        try {
            try (JDCConnection con = plugin.getConnection(); PreparedStatement ps = con.prepareStatement("SELECT * FROM `mail` WHERE to_player = ? ORDER BY id DESC LIMIT " + i + "," + 10)) {
                ps.setString(1, player.getName());
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        data.add(new MailData(rs.getInt("id"), rs.getString("from_player"), rs.getString("to_player"), rs.getString("subject"), rs.getString("text"), ((rs.getByte("unread") & 0xff) == 1), rs.getTimestamp("date")));
                    }
                }
            }
        }
        catch (SQLException e) {
            plugin.ErrLog("メール読み込みエラー　getUnreadMail：" + e);
        }
        return data.iterator();
    }

    public Iterator<MailData> getSendMail(Player player, int i) {
        ArrayList<MailData> data = new ArrayList<>();
        try {
            try (JDCConnection con = plugin.getConnection(); PreparedStatement ps = con.prepareStatement("SELECT * FROM `mail` WHERE from_player = ? ORDER BY id DESC LIMIT " + i + "," + 10)) {
                ps.setString(1, player.getName());
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        data.add(new MailData(rs.getInt("id"), rs.getString("from_player"), rs.getString("to_player"), rs.getString("subject"), rs.getString("text"), ((rs.getByte("unread") & 0xff) == 1), rs.getTimestamp("date")));
                    }
                }
            }
        }
        catch (SQLException e) {
            plugin.ErrLog("メール読み込みエラー　getUnreadMail：" + e);
        }
        return data.iterator();
    }

    /**
     * メールを送ります
     *
     * @param from 送り主
     * @param to あて先
     * @param subject 件名
     * @param text 本文
     */
    private void sendMail(Player from, String to, String subject, String text) {
        MailData md = new MailData(-1, from.getName(), to, subject, text, true, new Timestamp(System.currentTimeMillis()));
        this.saveMail(md);
    }

    /**
     * メールを送ります
     *
     * @param from 送り主
     * @param to あて先
     * @param subject 件名
     * @param text 本文
     */
    public void sendMail(Player from, Player to, String subject, String text) {
        this.sendMail(from, to.getName(), subject, text);
    }

    /**
     * メールを送ります
     *
     * @param from 送り主
     * @param to あて先
     * @param subject 件名
     * @param text 本文
     */
    public void sendMail(Player from, OfflinePlayer to, String subject, String text) {
        this.sendMail(from, to.getName(), subject, text);
    }

    /**
     * メールをセーブします
     *
     * @param mailData
     */
    public void saveMail(MailData mailData) {
        try {
            boolean update = (mailData.getId() != -1);
            try (JDCConnection con = plugin.getConnection()) {
                PreparedStatement ps;
                if (mailData.getId() != -1) {
                    ps = con.prepareStatement("UPDATE mail SET from_player = ?, to_player = ?, subject = ?, text = ?, unread = ?, date = ? WHERE id = ?");
                } else {
                    ps = con.prepareStatement("INSERT INTO mail (from_player, to_player, subject, text, unread, date) VALUES (?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                }
                ps.setString(1, mailData.getFrom());//from
                ps.setString(2, mailData.getTo());//to
                ps.setString(3, mailData.getSubject());//件名
                ps.setString(4, mailData.getText());//本文
                ps.setByte(5, (byte)(mailData.getUnread() ? 1 : 0));//未読
                ps.setTimestamp(6, mailData.getTimestamp());//時間
                if (update) {
                    ps.setInt(7, mailData.getId());//id
                }
                int updateRows = ps.executeUpdate();
                if (!update) {
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            mailData.setId(rs.getInt(1));
                        } else {
                            throw new RuntimeException("Inserting char failed.");
                        }
                    }
                } else if (updateRows < 1) {
                    throw new RuntimeException("saveMail not in database (" + mailData.getId() + ")");
                }
                ps.close();
            }
        }
        catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, "saveMail", ex);
        }
    }

    public boolean removeMail(int id) {
        try {
            try (JDCConnection c = plugin.getConnection(); PreparedStatement ps = c.prepareStatement("DELETE FROM mail WHERE `id` = ?")) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }
            return true;
        }
        catch (final SQLException ex) {
            return false;
        }
    }
}

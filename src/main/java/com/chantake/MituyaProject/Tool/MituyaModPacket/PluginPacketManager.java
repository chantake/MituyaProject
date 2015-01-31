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
package com.chantake.MituyaProject.Tool.MituyaModPacket;

import com.chantake.MituyaProject.MituyaProject;

/**
 *
 * @author いんく
 */
public class PluginPacketManager {

    static MituyaProject plugin;

    public static void setPlugin(MituyaProject pl) {
        plugin = pl;
    }
    
    /**
     * 全体にメッセージ送信
     *
     * @param ch チャンネル(2byte)
     * @param data Data
     *
     * @author いんく
     * @throws java.io.IOException
     */
    /*public static void sendPacket(byte[] ch, byte[] data) throws IOException {
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    os.write(ch);
    os.write(ch);
    for (Player pl : Bukkit.getOnlinePlayers()) {
    CraftPlayer cpl = ((CraftPlayer)pl);
    
    cpl.addChannel("Mituya");
    cpl.sendPluginMessage(plugin, "Mituya", os.toByteArray());
    }
    }*/

    /**
     * 指定した人にバイトを送信
     *
     * @param pl PlayerInstance
     * @param ch チャンネル(2byte)
     * @param data Data
     *
     * @author いんく
     * @throws java.io.IOException
     */
    /*public static void sendPacket(PlayerInstance pl, byte[] ch, byte[] data) throws IOException {
    if (pl.getMituyaMod()) {
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    os.write(ch);
    os.write(ch);
    try {
    CraftPlayer cpl = ((CraftPlayer)pl.getPlayer());
    
    cpl.addChannel("Mituya");
    cpl.sendPluginMessage(plugin, "Mituya", os.toByteArray());
    }
    catch (PlayerOfflineException ex) {
    }
    }
    }*/

    /* public static void NoChecksendPacket(PlayerInstance pl, byte[] ch, byte[] data) throws IOException {
    try {
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    os.write(ch);
    os.write(ch);
    CraftPlayer cpl = ((CraftPlayer)pl.getPlayer());
    
    cpl.addChannel("Mituya");
    cpl.sendPluginMessage(plugin, "Mituya", os.toByteArray());
    }
    catch (PlayerOfflineException ex) {
    }
    
    }*/

    /**
     * プレーヤーにYesNoの確認画面を表示
     *
     * @param pl PlayerInstance
     * @param title 確認タイトル
     * @param msg メッセージ
     *
     * @author いんく
     * @throws java.io.IOException
     */
    /*public static void SendYesNo(PlayerInstance pl, String title, String msg) throws IOException {
    if (pl.getMituyaMod()) {
    try {
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(os);
    dos.write(PacketParameter.YesNoSend);
    dos.writeBoolean(true);//DefaultMode
    dos.writeUTF(title);
    dos.writeUTF(msg);
    CraftPlayer cpl = ((CraftPlayer)pl.getPlayer());
    cpl.addChannel("Mituya");
    cpl.sendPluginMessage(plugin, "Mituya", os.toByteArray());
    }
    catch (PlayerOfflineException ex) {
    }
    }
    }*/

    /**
     * プレーヤーにYesNoの確認画面を表示
     *
     * @param pl PlayerInstance
     * @param title 確認タイトル
     * @param msg メッセージ
     * @param yesbutton ボタン(oh...Yes!)
     * @param nobutton ボタン(のおおおお！)
     *
     * @author いんく
     * @throws java.io.IOException
     */
    /*public static void SendYesNo(PlayerInstance pl, String title, String msg, String yesbutton, String nobutton) throws IOException {
    if (pl.getMituyaMod()) {
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(os);
    dos.write(PacketParameter.YesNoSend);
    dos.writeBoolean(false);//DefaultMode
    dos.writeUTF(title);
    dos.writeUTF(msg);
    dos.writeUTF(yesbutton);
    dos.writeUTF(nobutton);
    try {
    CraftPlayer cpl = ((CraftPlayer)pl.getPlayer());
    cpl.addChannel("Mituya");
    cpl.sendPluginMessage(plugin, "Mituya", os.toByteArray());
    }
    catch (PlayerOfflineException ex) {
    }
    }
    }*/

    /*public static void CloseYesNo(PlayerInstance ins) throws IOException {
    if (ins.getMituyaMod()) {
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(os);
    dos.write(PacketParameter.YesNoSend);
    dos.write(PacketParameter.Null);
    
    try {
    CraftPlayer cpl = ((CraftPlayer)ins.getPlayer());
    cpl.addChannel("Mituya");
    cpl.sendPluginMessage(plugin, "Mituya", os.toByteArray());
    }
    catch (PlayerOfflineException ex) {
    }
    }
    }*/

    /**
     * サーバーに接続
     *
     * @param pl PlayerInstance
     * @param ip IP
     *
     * @author いんく
     * @param port
     * @throws java.io.IOException
     */
    /*public static void ConnectServer(PlayerInstance pl, String ip, int port) throws IOException {
    if (pl.getMituyaMod()) {
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(os);
    dos.write(PacketParameter.ConnectServer);
    dos.writeUTF(ip);
    dos.writeInt(port);
    try {
    
    CraftPlayer cpl = ((CraftPlayer)pl.getPlayer());
    
    cpl.addChannel("Mituya");
    cpl.sendPluginMessage(plugin, "Mituya", os.toByteArray());
    }
    catch (PlayerOfflineException ex) {
    Logger.getLogger(PluginPacketManager.class.getName()).log(Level.SEVERE, null, ex);
    }
    }
    }*/

    /*public static void PlayDownloadSound(PlayerInstance pl, String url) throws IOException {
    if (pl.getMituyaMod()) {
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(os);
    dos.write(PacketParameter.PlayDownloadSound);
    dos.writeUTF(url);
    
    try {
    CraftPlayer cpl = ((CraftPlayer)pl.getPlayer());
    cpl.addChannel("Mituya");
    cpl.sendPluginMessage(plugin, "Mituya", os.toByteArray());
    }
    catch (PlayerOfflineException ex) {
    Logger.getLogger(PluginPacketManager.class.getName()).log(Level.SEVERE, null, ex);
    }
    }
    }*/
}

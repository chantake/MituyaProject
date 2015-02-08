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

import com.chantake.MituyaProject.Exception.PlayerOfflineException;
import com.chantake.MituyaProject.Gachapon.GachaponUserPhaseData;
import com.chantake.MituyaProject.MituyaProject;
import com.chantake.MituyaProject.Parameter.Parameter328;
import com.chantake.MituyaProject.Permissions.Rank;
import com.chantake.MituyaProject.Player.Chat.ChatType;
import com.chantake.MituyaProject.Tool.Encrypter;
import com.chantake.MituyaProject.Tool.HexTool;
import com.chantake.MituyaProject.Tool.MySqlProcessing;
import com.chantake.MituyaProject.Tool.Timer.AutoKick;
import com.chantake.MituyaProject.Tool.Timer.CancelCheckInstance;
import com.chantake.MituyaProject.Tool.Timer.PlayerTeleport;
import com.chantake.MituyaProject.Tool.UUIDUtils;
import com.chantake.mituyaapi.tools.database.JDCConnection;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffectType;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * プレーヤーごとにインスタンスを持つクラスです
 *
 * @author chantake
 * @version 4.0.0
 */
public class PlayerInstance implements PlayerInstanceMBean {

    private final HashMap task = new HashMap();
    private final TreeMap<Integer, TreeMap<Integer, LocationData>> home_data = new TreeMap<>();
    private final TreeMap<Integer, Location> spawn = new TreeMap<>();
    private final LinkedList<Long> chat_log_time = new LinkedList<>();
    private final LinkedList<Long> cmd_log_time = new LinkedList<>();
    private final List<GachaponUserPhaseData> GachaponData = new ArrayList<>();
    private MituyaProject plugin;
    private UUID uuid;
    private boolean debug = false;
    private boolean gobaku = true;
    private String mituyamodversion = null;
    private ChatColor color;
    private Rank rank;
    private int setacctask = 0;
    private boolean adminacti;
    private Location savelocation;
    private int tpp = 0;
    private boolean spongeperm = false;
    private String name, disname, rawdisname, guild;
    /**
     * 所有Mine
     */
    private int mine_id;
    private long mine, mine_log;
    private int id, tax;
    private int guildid;
    private String loginbmessage, deathmessage;
    private String accatt;
    private int level, mp, max_mp;
    private int home_id = -1, location_id = -1, spawn_id = -1;
    private boolean character;
    private int discolor;
    private int world_id;
    private String pass;
    private boolean savelocation_use;
    private boolean dead;
    private boolean neglect;
    private boolean check;
    private boolean check_skip;
    private Location htlocation;
    private Block htblock;
    private boolean tp;
    private boolean pvp = false;
    private boolean damagetick = false;
    private int pvp_win = 0;
    private Location death;
    private PlayerInstance rmp = null;
    private World lastworld = null;
    private Location harvest = null;
    private boolean world_invite = false;
    private ChatType chat_type = ChatType.All;
    private int party = -1;
    private boolean shownote = false;
    private boolean ime = true;
    private String nickname = "";
    private Timestamp lastlogin = null;
    private Runnable check_instance;
    private int check_id;
    private long check_time;
    private long last_chat_time = 0;
    private long last_cmd_time = 0;
    private List<String> command_tasks = new ArrayList<>(3);
    private long GachaponBuyTime = 0;

    /**
     * インスタンス作成
     *
     * @param PlayerID MinecraftID
     * @param plugin MituyaProject
     */
    public PlayerInstance(String PlayerID, MituyaProject plugin) {
        this.name = PlayerID;
        this.plugin = plugin;
    }
    
    /**
     * 暫定対応
     * @param uuid 
     */
    @Deprecated
    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }
    
    /**
     * 暫定対応
     * @param uuid
     * @return 
     */
    @Deprecated
    public UUID getUUID(UUID uuid) {
        return this.uuid;
    }

    /**
     * 表示名を変更します。（オフライン対応）
     */
    public void setDisplayName() {
        this.plugin.getInstanceManager().getDisplayNameManager().setDisplayName(this);
        try {
            Player player = this.getPlayer();
            //表示名
            player.setDisplayName(this.getDisplayName());
            //tab押した時のあれ
            player.setPlayerListName(this.getRawDisplayName());
            //あれ
            player.setCustomName(this.getNickName());
        } catch (NullPointerException | PlayerOfflineException ex) {
        }
    }

    public String getMituyaModVersion() {
        return mituyamodversion;
    }

    public void setMituyaModVersion(String ver) {
        mituyamodversion = ver;
    }

    /**
     * パスワードが設定されている場合はTrue
     *
     * @return パスワードが設定されている場合はTrue
     */
    public boolean Pass() {
        return pass.equals("") || pass != null;
    }

    public String getPass() {
        return pass;
    }

    public boolean isPass() {
        return this.pass.length() > 1;
    }

    public void setPass(String pass) {
        this.pass = Encrypter.getHash(pass, Encrypter.Algorithm.SHA512);//Sha512で暗号化
        this.SaveCharToDB(true);//キャラクター情報を更新
    }

    private void addChatLog() {
        if (this.chat_log_time.size() > 100) {
            this.chat_log_time.remove(0);
        }
        this.chat_log_time.add(System.currentTimeMillis());
    }

    public boolean isAllowChat() {
        int size = this.chat_log_time.size();
        if (size < 5) {
            this.addChatLog();
            return true;
        } else if (this.last_chat_time != 0 && ((System.currentTimeMillis() - this.last_chat_time) / 1000) < Parameter328.Chat_Sleep_Time) {
            return false;
        }
        long last = this.chat_log_time.get(size - 5);
        long time = (System.currentTimeMillis() - last) / 1000;
        if (time < 3) {
            last_chat_time = last;
            this.addChatLog();
            return false;
        } else {
            this.addChatLog();
            return true;
        }
    }

    private void addCmdLog() {
        if (this.cmd_log_time.size() > 100) {
            this.cmd_log_time.remove(0);
        }
        this.cmd_log_time.add(System.currentTimeMillis());
    }

    private boolean isAllowCommand() {
        int size = this.cmd_log_time.size();
        if (size < 5) {
            this.addCmdLog();
            return true;
        } else if (this.last_cmd_time != 0 && ((System.currentTimeMillis() - this.last_cmd_time) / 1000) < Parameter328.Cmd_Sleep_Time) {
            return false;
        }
        long last = this.cmd_log_time.get(size - 5);
        long time = (System.currentTimeMillis() - last) / 1000;
        if (time < 3) {
            this.last_cmd_time = last;
            this.addCmdLog();
            return false;
        } else {
            this.addCmdLog();
            return true;
        }
    }

    public boolean isAllowCmd() {
        if (!this.isAllowCommand()) {
            this.sendAttention("時間をおいて実行してください");
            this.removeCommandTasks();
            return false;
        }
        return true;
    }

    /**
     * Playerを取得します
     *
     * @return Player
     * @throws PlayerOfflineException プレーヤーがオフラインの場合PlayerExceptionが返ります
     */
    public Player getPlayer() throws PlayerOfflineException {
        try {
            Player player = plugin.getServer().getPlayer(UUIDUtils.getPlayerUUID(name));
            if (player == null) {
                throw new PlayerOfflineException(name);
            }
            return player;
        } catch (NullPointerException ex) {
            throw new PlayerOfflineException(name);
        }
    }

    public void setPlayer(Player pr) {
        name = pr.getName();
    }

    public boolean getDamagecanncel() {
        return this.damagetick;
    }

    public void setDamagecanncel(boolean i_am_mikki) {
        this.damagetick = i_am_mikki;
    }

    public boolean Debug() {
        return debug;
    }

    public void DebugV() {
        this.debug = !debug;
    }

    public PlayerInstance getPrivateMessagePlayer() {
        return this.rmp;
    }

    public void setPrivateMessagePlayer(PlayerInstance player) {
        this.rmp = player;
    }

    /**
     * Mineを取得
     *
     * @return 所持Mineを返します
     */
    @Override
    public long getMine() {
        return this.mine;
    }

    /**
     * mineを書き換え
     *
     * @param mine
     */
    @Override
    public void setMine(long mine) {
        this.mine = mine;
        //一定額変更があった場合だけ保存する
        if (Math.abs(this.mine_log - mine) >= Parameter328.SaveMineValue) {
            this.SaveMineToDB(true);
            this.mine_log = mine;
        }
    }

    /**
     * 取引する際のmineをチェックします
     *
     * @param mine 取引するmine
     * @return 取引ができる場合はtrue
     */
    public boolean checkMine(long mine) {
        return this.checkMine(mine, false);
    }

    /**
     * Mineを操作します
     *
     * @param mine 操作するMine
     * @param message メッセージの有無
     * @return 操作後のMineがマイナスの場合はfalseを返し、更新しません
     */
    public boolean checkMine(long mine, boolean message) {
        long complete_mine = this.mine + mine;
        if (complete_mine < 0) {//0以下の場合
            if (message) {
                this.sendAttention("Mineが足りません Mine:" + this.getMine());
            }
            return false;
        }
        return true;
    }

    /**
     * Mineを操作します
     *
     * @param mine 操作するMine
     * @return 操作後のMineがマイナスの場合はfalseを返し、更新しません
     */
    private boolean gainRawMine(long mine) {
        long complete_mine = this.mine + mine;
        if (complete_mine < 0) {//Mineが減ってしまう場合はfalseを返し更新しない
            return false;
        }
        this.setMine(complete_mine);//Mineを更新
        return true;
    }

    /**
     * Mineを操作します
     *
     * @param mine 操作するMine
     * @return 操作後のMineがマイナスの場合はfalseを返し、更新しません
     */
    public boolean gainMine(long mine) {
        boolean mine_success = this.gainRawMine(mine);
        this.gainMineMessage(mine_success, mine);
        return mine_success;
    }

    /**
     * Mineを操作します
     *
     * @param mine 操作するMine
     * @return 操作後のMineがマイナスの場合はfalseを返し、更新しません
     */
    public boolean gainMine(int mine) {
        return this.gainMine((long) mine);
    }

    /**
     * Mineを操作します
     *
     * @param mine 操作するMine
     * @param msg
     * @return 操作後のMineがマイナスの場合はfalseを返し、更新しません
     */
    public boolean gainMine(int mine, boolean msg) {
        if (msg) {
            return this.gainMine((long) mine);
        } else {
            return this.gainRawMine((long) mine);
        }
    }

    /**
     * gainMineのメッセージ表示用メゾッド
     *
     * @param mine_success
     * @param mine
     * @param tax
     */
    private void gainMineMessage(boolean mine_success, long mine) {
        try {
            Player pr = this.getPlayer();
            String info = ChatColor.GREEN + "Mine";
            if (mine_success) {
                StringBuilder sb = new StringBuilder();
                StringBuilder append = sb.append(ChatColor.AQUA);
                if (mine > 0) {
                    sb.append("Mineを獲得しました");
                } else {
                    sb.append("Mineを消費しました");
                }
                StringBuilder append1 = sb.append(" ").append(ChatColor.GOLD).append(mine).append(ChatColor.GREEN).append("mine");
                this.sendInfo(info, sb.toString());
            } else {
                this.sendInfo(info, ChatColor.RED + "Mineが足りません。");
            }
        } catch (PlayerOfflineException ex) {
        }
    }

    /**
     * 税金を設定します
     *
     * @param tax 設定する税金
     */
    public void setTax(int tax) {
        this.tax = tax;
    }

    public void setGobaku() {
        //誤爆反転
        gobaku = !gobaku;
    }

    public boolean getGobaku() {
        return gobaku;
    }

    public Rank getRank() {
        return rank;
    }

    /**
     * ランクを変更します
     *
     * @param rank Rank 直接使用禁止
     */
    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public int getAccTask() {
        return setacctask;
    }

    public void setAccTask(int i) {
        setacctask = i;
    }

    public HashMap getTask() {
        return task;
    }

    public void setAccAtt(String string) {
        accatt = string;
    }

    public boolean getActivate() {
        return adminacti;
    }

    public void setActivate(boolean b) {
        adminacti = b;
    }

    /**
     * TempLocationをセーブし、savelocation_use　をtrueにします。
     *
     * @param location Location
     * @see Location
     * @author chantake
     * @since 2011/03/10
     */
    public void SaveLocation(Location location) {
        this.savelocation = location;
        if (this.location_id == -1) {
            this.SaveLocationToDB(false);
        } else {
            this.SaveLocationToDB(true);
        }
        this.setSaveLocation(true);
    }

    public Location getLocation() {
        return savelocation;
    }

    public boolean UseSaveLocation() {
        return savelocation_use;
    }

    public boolean getSponge() {
        return this.spongeperm;
    }

    /**
     * spongeperm に代入
     *
     * @param perm spongepermに代入
     * @author inku_2525
     * @since 2012/08/11
     */
    public void setSponge(boolean perm) {
        this.spongeperm = perm;
    }

    /**
     * savelocation_useに代入
     *
     * @param use savelocation_useに代入
     * @author chantake
     * @since 2011/03/10
     */
    public void setSaveLocation(boolean use) {
        savelocation_use = use;
    }

    public int getTpPublic() {
        return tpp;
    }

    /**
     *
     * @param type パブリックタイプ 0:private　1:public 2:certification
     *
     */
    public void setTpPublic(int type) {
        tpp = type;
    }

    /**
     * ランクを変更します
     *
     * @param rank Rank
     * @return 変更に成功した場合はtrue
     */
    public boolean changeRank(Rank rank) {
        try {
            return this.plugin.getMituyaPermissionManager().setRank(this.getPlayer(), this, rank);
        } catch (PlayerOfflineException ex) {
            return this.plugin.getMituyaPermissionManager().setRank(null, this, rank);
        }
    }

    public boolean changeRank(String string) {
        return this.changeRank(Rank.getRank(string));
    }

    /**
     * Homeをセーブします
     *
     * @param location Location
     * @param subid ホームID
     * @param pc public
     * @param message Message
     */
    public void SaveHome(Location location, int subid, boolean pc, String message) {
        LocationData ld = new LocationData(location, subid);
        if (message == null) {
            this.setDefaultHome(ld);
        } else {
            ld.setMessage(message);
            ld.setPublic(pc);
        }
        this.SaveHome(ld);
    }

    /**
     * Homeをセーブします
     *
     * @param data LocationData
     */
    public void SaveHome(LocationData data) {
        //homeがない場合は新規、その他はアップでーと
        if (this.getHome(data.getLocation().getWorld(), data.getId()) == null) {
            MySqlProcessing.SaveHome(this, data, false);
        } else {
            MySqlProcessing.SaveHome(this, data, true);
        }
        this.setHomeData(data);
    }

    /**
     * homeを設定します
     *
     * @param data
     */
    public void setHomeData(LocationData data) {
        this.putHomeData(data.getLocation().getWorld(), data);
    }

    /**
     * homeデータを追加します。既に存在する場合は上書きします
     *
     * @param wd World
     * @param data LocationData
     */
    private void putHomeData(World wd, LocationData data) {
        this.putHomeData(plugin.getWorldManager().getWorldData(wd).getId(), data);
    }

    /**
     * homeデータを追加します。既に存在する場合は上書きします
     *
     * @param world_id ホームID
     * @param data LocationData
     */
    private void putHomeData(int world_id, LocationData data) {
        TreeMap<Integer, LocationData> homes;
        if (!this.home_data.containsKey(world_id)) {
            homes = new TreeMap<>();
        } else {
            homes = this.home_data.get(world_id);
        }
        homes.put(data.getId(), data);
        this.home_data.put(world_id, homes);
    }

    /**
     * ワールドごとにhomeを取得します
     *
     * @param wd World
     * @return
     */
    public TreeMap getWorldHome(World wd) {
        return this.getWorldHome(plugin.getWorldManager().getWorldData(wd).getId());
    }

    /**
     * ワールドごとにhomeを取得します
     *
     * @param world_id worldid
     * @return
     */
    public TreeMap getWorldHome(int world_id) {
        return this.home_data.get(world_id);
    }

    /**
     * ワールドでHomeを使用してるかどうかを返します
     *
     * @param wd World
     * @return trueの場合ワールドでHomeを使用してます
     */
    public boolean isHomeWorldUse(World wd) {
        if (this.isHomeUse()) {
            return !this.getWorldHome(wd).isEmpty();
        }
        return false;
    }

    /**
     * Homeを使用してるかどうか
     *
     * @return Homeを使ってる場合はtrue
     */
    public boolean isHomeUse() {
        return this.home_id != -1;
    }

    public int getHome_Id() {
        return this.home_id;
    }

    public void setHome_Id(int id) {
        this.home_id = id;
    }

    public LocationData getHome(World wd, int subid) {
        return this.getHome(plugin.getWorldManager().getWorldData(wd).getId(), subid);
    }

    /**
     * homeを取得します
     *
     * @param world world id
     * @param subid subid
     * @return 取得できた場合はLocationData 見つからない場合はnull
     */
    public LocationData getHome(int world, int subid) {
        TreeMap<Integer, LocationData> worldHome = this.getWorldHome(world);
        if (worldHome == null) {
            return null;
        }
        return worldHome.get(subid);
    }

    public void setHomeWelcome(String text, int world, int num) {
        this.getHome(world, num).setMessage(text);
    }

    public void setHomePublic(int world, int num) {
        LocationData ld = this.getHome(world, num);
        ld.setPublic(!ld.getPublic());
        try {
            //もしpublicなら
            if (ld.getPublic()) {
                //Markerを作成
                plugin.getDynmap().getMarker().createHomeMarker(name, ld);
            } else {
                //Markerを削除
                plugin.getDynmap().getMarker().deleteHomeMarker(name, ld);
            }
        } catch (NullPointerException e) {
        }
    }

    public boolean getHomePublic(int world, int num) {
        return this.getHome(world, num).getPublic();
    }

    public String getHomeWelcome(int world, int num) {
        return this.getHome(world, num).getMessage();
    }

    public Location getHomeLocation(int world, int number) {
        return this.getHome(world, number).getLocation();
    }

    /**
     * ニックネームを優先し、名前を返します minecraft idは getRawName、表示名はgetRawDisplayNameを使ってください
     *
     * @return 名前を返します
     */
    public String getName() {
        if (this.getNickName().length() > 0) {
            return this.getNickName();
        } else {
            return this.getRawName();
        }
    }

    /**
     * minecraft id を返します
     *
     * @return
     */
    public String getRawName() {
        return this.name;
    }

    @Override
    public String getDisplayName() {
        return this.disname;
    }

    /**
     * 表示名を変更する
     *
     * @param disname ディスプレイネーム
     */
    @Override
    public void setDisplayName(String disname) {
        this.disname = disname;
    }

    public String getRawDisplayName() {
        return this.rawdisname;
    }

    public void setRawDisplayName(String name) {
        this.rawdisname = name;
    }

    public void setSpawn(Location location) {
        spawn.put(plugin.getWorldManager().getWorldData(location.getWorld()).getId(), location);
        if (this.spawn_id == -1) {
            this.SaveSpawnToDB(false, location);
        } else {
            this.SaveSpawnToDB(true, location);
        }
    }

    /**
     * スポーンを返します
     *
     * @param wd
     * @return オリジナルスポーンから検索し返します、みつからない場合はそのワールドのデフォルトスポーンを返します
     */
    public Location getSpawn(World wd) {
        int wid = plugin.getWorldManager().getWorldData(wd).getId();
        if (this.spawn.containsKey(wid)) {
            return this.spawn.get(wid);
        }
        return plugin.getWorldSpawn(wd);
    }

    public void removeCustomSpawn(World wd) {
        int wid = plugin.getWorldManager().getWorldData(wd).getId();
        this.spawn.remove(wid);
    }

    public float getCorrectedYaw() throws PlayerOfflineException {
        float aa = (this.getPlayer().getLocation().getYaw() - 90) % 360;
        if (aa < 0) {
            aa += 360.0F;
        }
        return aa;
    }

    public String getLoginBMessage() {
        return loginbmessage;
    }

    public void setLoginBMessage(String text) {
        loginbmessage = text;
    }

    public String getDeathMessage() {
        return deathmessage;
    }

    public void setDeathMessage(String deathmessage) {
        this.deathmessage = deathmessage;
    }

    public void SetGachaponBuyTime(long time) {
        this.GachaponBuyTime = time;
    }

    public long GetGachaponBuyTime() {
        return this.GachaponBuyTime;
    }

    public List<GachaponUserPhaseData> GetGachaponData() {
        return this.GachaponData;
    }

    /**
     * Mineの所持ランキング取得
     *
     * @param i
     * @throws com.chantake.MituyaProject.Exception.PlayerOfflineException
     * @since 3.1.0
     */
    public void Ranking(int i) throws PlayerOfflineException {
        String re = "";
        try {
            try (JDCConnection con = plugin.getConnection()) {
                PreparedStatement ps = con.prepareStatement("SELECT i.player, i.balance, c.rank FROM ibalances AS i INNER JOIN characters c ON i.player=c.`name` WHERE c.rank < 1000 ORDER BY i.balance DESC LIMIT " + i + "," + 18);
                ResultSet rs = ps.executeQuery();
                this.sendMessage(ChatColor.YELLOW + "***********所持Mineランキング***********");
                while (rs.next()) {
                    StringBuilder stra = new StringBuilder();
                    i += 1;
                    stra.append(ChatColor.WHITE).append("[").append(ChatColor.YELLOW).append(i).append(ChatColor.WHITE).append("]");
                    stra.append(ChatColor.YELLOW);
                    stra.append(" ").append(rs.getString("player"));
                    stra.append(ChatColor.GOLD);
                    String st = rs.getString("balance") + ChatColor.GREEN + " Mine";
                    int length = 50 - stra.toString().length();
                    this.getPlayer().sendMessage(stra.toString() + HexTool.rightJust(st, length, ' '));
                }
                rs.close();
                ps.close();
            }
        } catch (SQLException e) {
            this.sendAttention("マネーランキングの呼び出しに失敗しました");
            plugin.ErrLog("Money Ranking err :" + e);
        }
    }

    @Override
    public int getLevel() {
        try {
            return this.getPlayer().getLevel();
        } catch (PlayerOfflineException ex) {

            return 0;
        }
    }

    @Override
    public void setLevel(int lv) {
        try {
            this.getPlayer().setLevel(lv);
        } catch (PlayerOfflineException ex) {
        }
    }

    @Override
    public int getMineId() {
        return mine_id;
    }

    public void dead(boolean b) {
        dead = b;
    }

    public boolean getDead() {
        return dead;
    }

    public void ItemDrop(int id, int type, int ammount, World wr, Location lo) {
        ItemStack is = new ItemStack(id);
        int r = 0;
        int rr = 0;
        boolean rrr = false;
        if (ammount > 64) {
            r = ammount / 64;
            rr = ammount % 64;
            rrr = true;
        }
        is.setDurability((short) type);
        lo.setY(lo.getY() + 1);
        if (!rrr) {
            is.setAmount(ammount);
            wr.dropItem(lo, is);
        } else {
            for (int i = 0; i < r; i++) {
                is.setAmount(64);
                wr.dropItem(lo, is);
            }
            is.setAmount(rr);
            wr.dropItem(lo, is);
        }
    }

    public boolean InventryCheck(int id, short type, int amount) throws PlayerOfflineException {
        Inventory inventory = getPlayer().getInventory();
        int air = 0;
        int stack;
        int amari = 0;
        int maxstack = Material.getMaterial(id).getMaxStackSize();
        //インベントリの空きチェック
        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) {
                air++;
            }
        }
        if (maxstack >= amount) {
            stack = 1;
        } else {
            stack = amount / maxstack;
            amari = amount % maxstack;
        }
        //インベントリがあいていない
        if (stack > air) {
            this.sendAttention("インベントリを空けてください。 ＠" + (stack - air));
            return false;
        }

        if (amari > 0 && stack == air) {
            this.sendAttention("インベントリを空けてください。 ＠１");
            return false;
        }

        return true;
    }

    /**
     * アイテムを渡します
     *
     * @param itemstack
     * @param amount
     * @return
     * @throws com.chantake.MituyaProject.Exception.PlayerOfflineException
     */
    public boolean gainItem(ItemStack itemstack, int amount) throws PlayerOfflineException {
        ItemStack is = itemstack.clone();
        Inventory inventory = getPlayer().getInventory();
        if (amount == 0) {
            this.sendAttention("0以外の個数を指定してください");
            return false;
        }

        int air = 0;
        int stack;
        int amari = 0;
        int maxstack = itemstack.getMaxStackSize();
        //インベントリの空きチェック
        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) {
                air++;
            }
        }
        if (maxstack >= amount) {
            stack = 1;
        } else {
            stack = amount / maxstack;
            amari = amount % maxstack;
        }
        //インベントリがあいていない
        if (stack > air) {
            this.sendAttention("インベントリを空けてください。 ＠" + (stack - air));
            return false;
        }

        if (amari > 0 && stack == air) {
            this.sendAttention("インベントリを空けてください。 ＠１");
            return false;
        }

        if (stack == 1) {
            is.setAmount(amount);
            inventory.addItem(is);
        } else {
            for (int i = 0; i < stack; i++) {
                is.setAmount(maxstack);
                inventory.addItem(is);
            }
            if (amari != 0) {
                is.setAmount(amari);
                inventory.addItem(is);
            }
        }
        return true;
    }

    /**
     * アイテムを渡します
     *
     * @param itemstack
     * @return trueの場合は完了
     * @throws PlayerOfflineException
     */
    public boolean gainItem(ItemStack itemstack) throws PlayerOfflineException {
        return this.gainItem(itemstack, itemstack.getAmount());
    }

    /**
     * アイテムを渡します
     *
     * @param id アイテムID
     * @param data
     * @param amount 数
     * @return
     * @throws com.chantake.MituyaProject.Exception.PlayerOfflineException
     */
    public boolean gainItem(int id, short data, int amount) throws PlayerOfflineException {
        ItemStack is = new ItemStack(id);
        //タイプを設定
        is.setDurability(data);
        return this.gainItem(is, amount);
    }

    /**
     * アイテムをインベントリに追加する
     *
     * @param item id:type の形で配列にする
     * @param amount 数
     * @throws com.chantake.MituyaProject.Exception.PlayerOfflineException
     */
    public void gainItem(String[] item, int amount) throws PlayerOfflineException {
        for (String ii : item) {
            String[] it = ii.split(":");
            int idd = Integer.valueOf(it[0]);
            int type = Integer.valueOf(it[1]);
            ItemStack is = new ItemStack(idd);
            Inventory inventory = getPlayer().getInventory();
            int stack = 0;
            int amari = 0;
            int maxstack = Material.getMaterial(idd).getMaxStackSize();
            if (stack <= maxstack) {
                stack = 1;
            } else {
                stack = amount / maxstack;
                amari = amount % maxstack;
            }
            is.setDurability((short) type);//アイテムタイプ設定
            if (stack == 1) {
                is.setAmount(amount);
                inventory.addItem(is);
            } else {
                for (int i = 0; i < stack; i++) {
                    is.setAmount(maxstack);
                    inventory.addItem(is);
                }
                is.setAmount(amari);
                inventory.addItem(is);
            }
        }
    }

    /**
     * ログイン時に実行し、プレーヤー情報を更新する。
     *
     * @param pr Player
     * @see Player
     * @author chantake
     * @since 2011/03/09
     */
    public void Login(Player pr) {
        //this.setPlayer(pr);//プレーヤーインスタンス更新
        //this.setName(pr.getName());//プレーヤー名更新
        try {
            ManagementFactory.getPlatformMBeanServer().registerMBean(this, ObjectName.getInstance("OnlinePlayers:type=" + pr.getName()));
        } catch (MalformedObjectNameException | NullPointerException | InstanceAlreadyExistsException | MBeanRegistrationException | NotCompliantMBeanException ex) {
        }
        this.LoadCharFromDB(true);//キャラクターロード
        this.LoadMineFromDB();//Mineをロード
        this.LoadHomeFromDB();//ホームロード
        this.LoadTempLocationFromDB();//TempLocationをロード
        this.LoadSpawnLocationFromDB();//SpawnLocationをロード
        this.setDisplayName();//表示名変更
        this.CheckDB();//既存のDb情報取得
        AutoKick.neglect.put(pr, 1);//放置判定
    }

    /**
     * オフライン時に実行し、プレーヤー情報を取得する。
     *
     * @see Player
     * @author chantake
     * @since 2011/03/09
     */
    public void Offline() {
        // this.setDisplayName();//表示名変更
        this.LoadCharFromDB(false);//キャラクターロード
        this.LoadMineFromDB();//Mineをロード
        this.LoadHomeFromDB();//ホームロード
        /**
         * オフラインで使うことがないのでコメントアウト*
         */
        //this.LoadTempLocationFromDB();//TempLocationをロード
        //this.LoadSpawnLocationFromDB();//SpawnLocationをロード
    }

    /**
     * ログアウト時に実行し、プレーヤー情報を保存する。
     *
     * @author chantake
     * @since 2011/03/09
     */
    public void SaveQuit() {
        if (this.id == -1) {
            this.SaveCharToDB(false);
        } else {
            this.SaveCharToDB(true);//プレーヤー更新
        }
        this.SaveMineToDB(true);//Mine更新
        //homeを使っている場合のみ保存
        if (this.isHomeUse()) {
            this.SaveAllHomeToDB();//Home更新
        }
        //TempLocationを使っている場合のみ保存
        if (this.savelocation_use) {
            this.SaveLocationToDB(true);//TempLocation更新
        }
        //個別Spawnを使っている場合のみ保存
        if (this.spawn.size() > 0) {
            this.SaveAllSpawnToDB();//個別Spawn更新
        }
        try {
            ManagementFactory.getPlatformMBeanServer().unregisterMBean(ObjectName.getInstance("OnlinePlayers:type=" + getPlayer().getName()));
        } catch (PlayerOfflineException | MalformedObjectNameException | NullPointerException | InstanceNotFoundException | MBeanRegistrationException ex) {
        }
        plugin.Log(this.name + " : セーブ完了");
    }

    /**
     * SaveAllや、サーバー停止時に呼び出され、プレーヤー情報を保存する。
     *
     * @author chantake
     * @since 2011/03/09
     */
    @Override
    public void SaveAll() {
        if (this.id == -1) {
            this.SaveCharToDB(false);
        } else {
            this.SaveCharToDB(true);//プレーヤー更新
        }
        this.SaveMineToDB(true);//Mine更新
        //homeを使っている場合のみ保存
        if (this.isHomeUse()) {
            this.SaveAllHomeToDB();//Home更新
        }
        //TempLocationを使っている場合のみ保存
        if (this.savelocation_use) {
            this.SaveLocationToDB(true);//TempLocation更新
        }
        //個別Spawnを使っている場合のみ保存
        if (this.spawn.size() > 0) {
            this.SaveAllSpawnToDB();//個別Spawn更新
        }

    }

    /**
     * 正しい値が保存されているかチェックし、置き換えます。
     */
    public void CheckDB() {
        //既にレコードがあるか検索
        /*
         * if (this.home_id == -1) { this.home_id = MySqlProcessing.getPlayerHome(this.name); }
         */
        /*
         * if (this.mine_id == -1) { this.mine_id = MySqlProcessing.getPlayerMine(this.name); }
         */
        if (this.location_id == -1) {
            this.location_id = MySqlProcessing.getPlayerTemp(this.name);
        }
        if (this.spawn_id == -1) {
            this.spawn_id = MySqlProcessing.getPlayerSpawn(this.name);
        }

        if (this.mine <= 0) {
            this.mine = MySqlProcessing.getPlayerMine(this.name);
        }

        /*
         * //Mineに関してはない場合作成 if (this.mine_id == -1) { this.setDefaultMine(); this.SaveMineToDB(false); }
         *
         * //Charcterもない場合は新規作成 if (this.id == -1) { this.SaveCharToDB(false); }
         */
        //lastworldがnullかつ、スカイランドにいる場合（最後のワールドが保存されていない場合）
        /*
         * if (this.lastworld == null && this.getPlayer().getLocation().getWorld().getName().equals("skyland")) { //招待状があってメインワールドに入場できる場合は通常ワールド if
         * (this.getMainWorldInvite()) { this.lastworld = plugin.getWorld(Parameter328.MainWorld); } else {//入れないので新規追加ワールド this.lastworld =
         * plugin.getWorld(Parameter328.SecondWorld); } }
         */
    }

    // <editor-fold defaultstate="collapsed" desc="新規キャラクタレコードの作成">

    /**
     * キャラクターをDBからロードします。見つからない場合は新規レコードを作成します。
     *
     * @param CreateFlag レコードが見つからない場合は、この値がtrueの場合のみ新規レコードを作成します。
     * @return 成功:true/失敗:false を返します。
     * @author chantake
     * @since 2011/03/09
     */
    private boolean LoadCharFromDB(boolean CreateFlag) {
        boolean ret = false;
        this.setDefaultChar();
        try {
            try (JDCConnection con = plugin.getConnection()) {
                PreparedStatement ps = con.prepareStatement("SELECT * FROM characters WHERE name = ?");
                ps.setString(1, this.name);//キャラクター名
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    rs.close();
                    ps.close();
                    //見つからない場合は作成
                    if (CreateFlag) {
                        ret = this.CreateCharTableToDB();
                        if (ret) {
                            try {
                                WelcomeMessage();
                            } catch (PlayerOfflineException ex) {
                            }
                        }
                    }
                } else {
                    this.id = rs.getInt("id");
                    this.disname = rs.getString("disname");
                    this.nickname = rs.getString("nickname");
                    this.world_id = rs.getInt("world");
                    this.pass = rs.getString("pass");
                    //this.home_id = rs.getInt("home");
                    this.rank = Rank.getRank(rs.getString("rank"));
                    this.level = rs.getInt("lv");
                    this.spawn_id = rs.getInt("spawnlocation");
                    this.location_id = rs.getInt("templocation");
                    this.mine_id = rs.getInt("money");
                    this.tax = rs.getInt("tax");
                    this.gobaku = (rs.getByte("gobaku") & 0xff) == 1;
                    this.loginbmessage = rs.getString("loginmessage");
                    this.guildid = rs.getInt("guild");
                    this.tpp = (rs.getInt("tppublic"));
                    this.check_skip = ((rs.getByte("check") & 0xff) == 1);
                    this.pvp = ((rs.getByte("pvp") & 0xff) == 1);
                    this.ime = ((rs.getByte("ime") & 0xff) == 1);
                    this.world_invite = ((rs.getByte("world_invite") & 0xff) == 1);
                    int wid = rs.getInt("lastworld");
                    if (wid == -1) {
                        wid = plugin.getWorldManager().getWorldData(Parameter328.SecondWorld).getId();
                    }
                    this.lastworld = plugin.getWorldManager().getWorld(wid);
                    this.deathmessage = rs.getString("deathmessage");
                    this.mp = rs.getInt("mp");
                    this.max_mp = rs.getInt("max_mp");
                    try {
                        this.lastlogin = rs.getTimestamp("lastconnect");
                        if (lastlogin != null) {
                            GregorianCalendar nt = new GregorianCalendar();
                            GregorianCalendar yyd = new GregorianCalendar(nt.get(Calendar.YEAR), nt.get(Calendar.MONTH), nt.get(Calendar.DAY_OF_MONTH) - 2);
                            if (lastlogin.before(yyd.getTime()) && Parameter328.Login_Bonus) {
                                GregorianCalendar yd = new GregorianCalendar(nt.get(Calendar.YEAR), nt.get(Calendar.MONTH), nt.get(Calendar.DAY_OF_MONTH) - 1);
                                if (lastlogin.before(yd.getTime())) {
                                    //連日路銀(コンボとかつけても面白いかもNE!!!)
                                } else {
                                    //隔日路銀
                                }
                            }
                        }
                    } catch (SQLException ex) {
                    }
                    rs.close();
                    ps.close();

                    //マイン(所持金)データが見つからない場合は生成する。
                    if (this.mine_id == -1) {
                        ret = this.CreateMineTableToDB();
                    } else {
                        ret = true;
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PlayerInstance.class.getName()).log(Level.SEVERE, null, ex);
            this.sendWarning("SQL err. character zyouhouga syutoku dekimasen. sa-ba-kanrisyani renrakusitekudasai.");
        }

        return ret;
    }

    // <editor-fold defaultstate="collapsed" desc="新規マイン(所持金)レコードの作成">

    /**
     * キャラクターレコードを新規作成します。
     *
     * @return 成功:true/失敗:false を返します。
     * @author ezura573
     * @since 3.4.0
     */
    private boolean CreateCharTableToDB() {
        boolean ret = false;
        PreparedStatement ps;
        ResultSet rs;

        //既存のレコードIDを検索
        this.mine_id = MySqlProcessing.FindPlayerMineID(this.name);
        this.spawn_id = MySqlProcessing.FindPlayerSpawnID(this.name);
        //this.home_id = MySqlProcessing.FindPlayerHomeID(this.name);
        this.location_id = MySqlProcessing.FindPlayerTLocationID(this.name);

        //マイン(所持金)データが見つからない場合は生成する。
        if (this.mine_id == -1) {
            if (!this.CreateMineTableToDB()) {
                return ret;
            }
        }

        try {
            try (JDCConnection con = plugin.getConnection()) {
                ps = con.prepareStatement(
                        "INSERT INTO characters (name,disname,nickname,ip,world,pass,home,rank,lv,spawnlocation,templocation,money,tax,gobaku,loginmessage,guild,tppublic,`check`,pvp,ime,lastworld,deathmessage,mp,max_mp,sponge,firstlogin) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now())",
                        Statement.RETURN_GENERATED_KEYS);

                ps.setString(1, this.name);//name
                ps.setString(2, this.getDisplayName());//表示名
                ps.setString(3, this.nickname);//ニックネーム
                try {
                    ps.setString(4, this.getPlayer().getAddress().toString());//IPアドレス
                } catch (PlayerOfflineException ex) {
                    ps.setString(4, "null");//IPアドレス
                }
                ps.setInt(5, plugin.getWorldManager().getWorldData(Parameter328.SecondWorld).getId());//WorldID
                ps.setString(6, this.pass);//アカウントパスワード
                //ps.setInt(6, this.home_id);//homeテーブルのレコードID
                ps.setInt(7, -2);//homeテーブルのレコードID
                ps.setString(8, this.getRank().getName());//権限
                ps.setInt(9, 0);//レベル
                ps.setInt(10, this.spawn_id);//spawnlocationテーブルのレコードID
                ps.setInt(11, this.location_id);//templocationテーブルのレコードID
                ps.setInt(12, this.mine_id);//MineテーブルのレコードID
                ps.setInt(13, this.tax);//使用税金総額
                ps.setByte(14, (byte) (this.gobaku ? 1 : 0));//誤爆設定
                ps.setString(15, this.loginbmessage);//ログインメッセージ
                ps.setInt(16, this.guildid);//ギルドID
                ps.setInt(17, this.tpp);//tp public
                ps.setByte(18, (byte) (false ? 1 : 0));
                ps.setByte(19, (byte) (false ? 1 : 0));
                ps.setByte(20, (byte) (false ? 1 : 0));
                ps.setInt(21, -1);//lastworld worldid
                ps.setString(22, this.deathmessage);//deathmessage
                ps.setInt(23, this.mp);//mp
                ps.setInt(24, this.max_mp);//max_mp
                ps.setByte(25, (byte) (this.spongeperm ? 1 : 0));
                //SQL実行
                ps.executeUpdate();

                //挿入されたレコードのIDを取得する
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    this.id = rs.getInt(1);
                    character = true;
                    ret = true;
                    plugin.Log("CreateDefaultCharToDB : " + this.name + "(" + this.id + ")");
                } else {
                    throw new RuntimeException("Inserting char failed.");
                }
                rs.close();
                ps.close();
            }
        } catch (SQLException ex) {
            character = false;
            Logger.getLogger(PlayerInstance.class.getName()).log(Level.SEVERE, "CreateDefaultCharToDB", ex);
        }

        return ret;
    }// </editor-fold>

    /**
     * マイン(所持金)レコードを新規作成します。
     *
     * @return 成功:true/失敗:false を返します。
     * @author ezura573
     * @since 3.4.0
     */
    private boolean CreateMineTableToDB() {
        boolean ret = false;
        PreparedStatement ps;
        ResultSet rs;

        try {
            try (JDCConnection con = plugin.getConnection()) {
                ps = con.prepareStatement("INSERT INTO ibalances (player,balance) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, this.name);
                ps.setLong(2, Parameter328.Default_Mine);

                //SQL実行
                ps.executeUpdate();

                //挿入されたレコードのIDを取得する
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    this.mine_id = rs.getInt(1);
                    ret = true;
                    plugin.Log("CreateDefaultMineToDB : " + this.name + "(" + this.mine_id + ")");
                } else {
                    throw new RuntimeException("Inserting char failed.");
                }
                rs.close();
                ps.close();
            }
        } catch (SQLException ex) {
            this.mine_id = -1;
            Logger.getLogger(PlayerInstance.class.getName()).log(Level.SEVERE, "CreateDefaultMineToDB", ex);
        }
        return ret;
    }// </editor-fold>

    /**
     * キャラクターレコードをアップデート、新規作成します。
     *
     * @param update Trueの場合アップデート、falseの場合新規レコード作成します。
     * @author chantake
     * @since 2011/03/09
     */
    public void SaveCharToDB(boolean update) {
        try {
            try (JDCConnection con = plugin.getConnection()) {
                PreparedStatement ps;
                if (update) {
                    ps = con.prepareStatement("UPDATE characters SET name = ?, disname = ?, nickname = ?, ip = ?, world = ?, pass = ?, home = ?, rank = ?, lv = ?, spawnlocation = ?, templocation = ?, money = ?, tax = ?, gobaku = ?, loginmessage = ?, guild = ?, tppublic = ?, `check` = ?, pvp = ?, ime = ?, world_invite = ?, deathmessage = ?, mp = ?, max_mp = ?, lastworld = ? WHERE id = ?");
                } else {
                    ps = con.prepareStatement("INSERT INTO characters (name,disname,nickname,ip,world,pass,home,rank,lv,spawnlocation,templocation,money,tax,gobaku,loginmessage,guild,tppublic,`check`,pvp,ime,world_invite,deathmessage,mp,max_mp,lastworld) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                            Statement.RETURN_GENERATED_KEYS);
                }
                ps.setString(1, this.name);//name
                ps.setString(2, this.getDisplayName());//表示名
                ps.setString(3, this.nickname);//ニックネーム
                Player pr = null;
                try {
                    pr = this.getPlayer();
                } catch (PlayerOfflineException ex) {
                }
                if (pr != null) {
                    try {
                        ps.setString(4, pr.getAddress().toString());//IPアドレス
                        ps.setInt(5, this.getPlugin().getWorldManager().getWorldData(pr.getLocation().getWorld()).getId());//WorldID
                    } catch (NullPointerException ex) {
                        ps.setString(4, "null");//IPアドレス
                        ps.setInt(5, -1);//WorldID
                    }
                } else {
                    ps.setString(4, "null");//IPアドレス
                    ps.setInt(5, -1);//WorldID
                }
                ps.setString(6, this.pass);//アカウントパスワード
                //ps.setInt(6, this.home_id);//homeテーブルのレコードID
                ps.setInt(7, -2);//homeテーブルのレコードID(無効化)
                ps.setString(8, this.getRank().getName());//権限
                ps.setInt(9, this.level);//レベル
                ps.setInt(10, this.spawn_id);//spawnlocationテーブルのレコードID
                ps.setInt(11, this.location_id);//templocationテーブルのレコードID
                ps.setInt(12, this.mine_id);//MineテーブルのレコードID
                ps.setInt(13, this.tax);//使用税金総額
                ps.setByte(14, (byte) (this.gobaku ? 1 : 0));//誤爆設定
                ps.setString(15, this.loginbmessage);//ログインメッセージ
                ps.setInt(16, this.guildid);//ギルドID
                ps.setInt(17, this.tpp);//tp public
                ps.setByte(18, (byte) (this.check_skip ? 1 : 0));//check
                ps.setByte(19, (byte) (this.pvp ? 1 : 0));//pvp
                ps.setByte(20, (byte) (this.ime ? 1 : 0));//ime
                ps.setByte(21, (byte) (this.world_invite ? 1 : 0));//world_invite
                ps.setString(22, this.deathmessage);//deathmessage
                ps.setInt(23, this.mp);//mp
                ps.setInt(24, this.max_mp);//maxmp
                if (!update) {
                    ps.setInt(25, plugin.getWorldManager().getWorldData(lastworld).getId());//lastworld
                } else {
                    ps.setInt(25, plugin.getWorldManager().getWorldData(Parameter328.SecondWorld).getId());//lastworld
                    ps.setInt(26, this.id);//テーブルID
                }
                int updateRows = ps.executeUpdate();
                if (!update) {
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            this.id = rs.getInt(1);
                            plugin.Log("SaveDefaultCharToDB : " + this.name + "(" + this.id + ")");
                        } else {
                            throw new RuntimeException("Inserting char failed.");
                        }
                    }
                } else if (updateRows < 1) {
                    throw new RuntimeException("Character not in database (" + id + ")");
                }
                character = true;
                ps.close();
            }
        } catch (SQLException ex) {
            character = false;
            Logger.getLogger(PlayerInstance.class.getName()).log(Level.SEVERE, "SaveDefaultCharToDB", ex);
        }
    }

    /**
     * HomeをDBからロードします。見つからない場合はhome_load,home_useをfalseにします。
     *
     * @author chantake
     * @since 2011/03/10
     */
    private void LoadHomeFromDB() {
        this.home_id = MySqlProcessing.getPlayerHome(this.getRawName());

        if (this.home_id != -1) {
            MySqlProcessing.LoadHome(this);
        }
    }

    public void SaveAllHomeToDB() {
        if (this.isHomeUse()) {
            for (int world : this.home_data.keySet()) {
                for (int subid : this.home_data.get(world).keySet()) {
                    MySqlProcessing.SaveHome(this, this.home_data.get(world).get(subid), true);
                }
            }
        }
    }

    public void SaveAllSpawnToDB() {
        for (int wid : this.spawn.keySet()) {
            this.SaveSpawnToDB(true, this.spawn.get(wid));
        }
    }

    /**
     * MineをDBからロードします。見つからない場合は新規レコードを作成します。
     *
     * @author chantake
     * @since 2011/03/10
     */
    private void LoadMineFromDB() {
        boolean no = true;//判定
        try {
            try (JDCConnection con = plugin.getConnection()) {
                PreparedStatement ps;
                ps = con.prepareStatement("SELECT * FROM ibalances WHERE player = ?");
                ps.setString(1, this.name);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        //レコードが見つからない場合
                        rs.close();
                        ps.close();
                        no = false;
                        this.mine_id = -1;
                        return;
                        //throw new RuntimeException("Loading char failed (not found)");
                    }
                    this.mine_id = rs.getInt("id");
                    this.mine = rs.getLong("balance");
                }
                ps.close();
            }
        } catch (SQLException ex) {
            this.mine_id = -1;
            this.sendWarning("Sql err LoadMine. サーバー管理者に連絡してください");
            Logger.getLogger(PlayerInstance.class.getName()).log(Level.SEVERE, "LoadMineFromDB", ex);
        }
        //System.out.println("Mine_id:" + this.mine_id);
    }

    /**
     * Mineをデフォルト値に上書きします。
     *
     * @author chantake
     * @since 2011/03/10
     */
    public void setDefaultMine() {
        this.setMine(Parameter328.Default_Mine);//初期Mineをセット
    }

    /**
     * Homeをデフォルト値に上書きします。
     *
     * @author chantake
     * @param ld
     * @since 3.3.0
     */
    public void setDefaultHome(LocationData ld) {
        ld.setMessage(ChatColor.AQUA + this.name + ChatColor.LIGHT_PURPLE + " の家");
        ld.setPublic(false);
    }

    /**
     * Characterをデフォルト値に上書きします。
     *
     * @author chantake
     * @since 2011/03/10
     */
    private void setDefaultChar() {
        this.id = -1;
        this.rank = Rank.Default;//権限
        this.level = 0;//Level
        this.pass = null;//パスワードnull
        this.home_id = -1;//未作成
        this.spawn_id = -1;//spawn id
        this.location_id = -1;//未作成
        this.mine_id = -1;//未作成
        this.tax = 0;//税金を0に
        this.gobaku = true;//誤爆On
        this.check = false;//確認をoffにする
        this.loginbmessage = "";//ログインメッセージ
        this.guildid = -1;//未作成
        this.tpp = 1;//tppublic
        this.deathmessage = "";
        this.mp = 0;
        this.max_mp = 0;
    }

    /**
     * Mineをアップデート、新規作成します。
     *
     * @param update Trueの場合アップデート、falseの場合新規レコード作成します。
     * @author chantake
     * @since 2011/03/10
     */
    private void SaveMineToDB(boolean update) {
        try {
            try (JDCConnection con = plugin.getConnection()) {
                PreparedStatement ps;
                if (update) {
                    ps = con.prepareStatement("UPDATE ibalances SET balance = ? WHERE player = ?");
                    ps.setLong(1, this.mine);
                    ps.setString(2, name);
                } else {
                    //etDefaultMine();
                    ps = con.prepareStatement("INSERT INTO ibalances (player,balance) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, name);
                    ps.setLong(2, Parameter328.Default_Mine);
                }
                int updateRows = ps.executeUpdate();
                if (!update) {
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            this.mine_id = rs.getInt(1);
                        } else {
                            throw new RuntimeException("Inserting char failed.");
                        }
                    }
                } else if (updateRows < 1) {
                    throw new RuntimeException("ibalances not in database (" + id + ")");
                }
                ps.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(PlayerInstance.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * tempLocationをDBからロードします。見つからない場合はsavelocation_useをfalseにします。
     *
     * @author chantake
     * @since 2011/03/10
     */
    private void LoadTempLocationFromDB() {
        try {
            try (JDCConnection con = plugin.getConnection()) {
                PreparedStatement ps;

                ps = con.prepareStatement("SELECT * FROM templocation WHERE id = ?");
                ps.setInt(1, this.location_id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        rs.close();
                        ps.close();
                        savelocation_use = false;
                        return;
                        //throw new RuntimeException("Loading char failed (not found)");
                    }
                    this.savelocation = new Location(plugin.getWorldManager().getWorld(rs.getInt("world")), rs.getDouble("x"), rs.getByte("y") & 0xff, rs.getDouble("z"), rs.getShort("yaw"), rs.getShort("pitch"));
                    this.setSaveLocation((rs.getByte("use") & 0xff) == 1);
                }
                ps.close();
            }
        } catch (SQLException ex) {
            this.savelocation_use = false;
            //Logger.getLogger(PlayerInstance.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * SpawnLocationをDBからロードします。見つからない場合はspawn_useをfalseにします。
     *
     * @author chantake
     * @since 2011/03/10
     */
    private void LoadSpawnLocationFromDB() {
        try {
            try (JDCConnection con = plugin.getConnection()) {
                PreparedStatement ps;

                ps = con.prepareStatement("SELECT * FROM spawnlocation WHERE id = ?");
                ps.setInt(1, this.spawn_id);
                try (ResultSet rs = ps.executeQuery()) {
                    int i = 0;
                    while (rs.next()) {
                        i++;
                        int spawn_world = rs.getInt("world");
                        this.spawn.put(spawn_world, new Location(plugin.getWorldManager().getWorld(spawn_world), rs.getDouble("x"), rs.getByte("y") & 0xff, rs.getDouble("z"), rs.getShort("yaw"), rs.getShort("pitch")));
                    }
                }
                ps.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(PlayerInstance.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public MituyaProject getPlugin() {
        return plugin;
    }

    /**
     * MituyaProjectのインスタンスをセット
     *
     * @param plugin
     * @see MituyaProject
     * @author chantake
     * @since 2011/03/10
     */
    public void setPlugin(MituyaProject plugin) {
        this.plugin = plugin;
    }

    /**
     * TempLocaitonをアップデート、新規作成します。
     *
     * @param update Trueの場合アップデート、falseの場合新規レコード作成します。
     * @author chantake
     * @since 2011/03/10
     */
    private void SaveLocationToDB(boolean update) {
        try {
            try (JDCConnection con = plugin.getConnection()) {
                PreparedStatement ps;
                if (update) {
                    ps = con.prepareStatement("UPDATE templocation SET name = ?, world = ?,x = ?, y = ?, z = ?, yaw = ?, pitch = ?, `use` = ? WHERE id = ?");
                } else {
                    ps = con.prepareStatement("INSERT INTO templocation (name,world,x,y,z,yaw,pitch,`use`) VALUES (?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                }
                ps.setString(1, this.name);
                ps.setInt(2, this.getPlugin().getWorldManager().getWorldData(this.savelocation.getWorld()).getId());
                ps.setDouble(3, this.savelocation.getX());
                ps.setByte(4, (byte) this.savelocation.getY());
                ps.setDouble(5, this.savelocation.getZ());
                ps.setShort(6, (short) this.savelocation.getYaw());
                ps.setShort(7, (short) this.savelocation.getPitch());
                if (update) {
                    ps.setByte(8, (byte) (this.savelocation_use ? 1 : 0));
                    ps.setInt(9, this.location_id);
                } else {
                    ps.setByte(8, (byte) 1);
                }
                int updateRows = ps.executeUpdate();
                if (!update) {
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            this.location_id = rs.getInt(1);
                        } else {
                            throw new RuntimeException("Inserting char failed.");
                        }
                    }
                } else if (updateRows < 1) {
                    throw new RuntimeException("Templocation not in database (" + id + ")");
                }
                ps.close();
            }
            SaveCharToDB(true);
        } catch (SQLException ex) {
            Logger.getLogger(PlayerInstance.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 個別Spawnをアップデート、新規作成します。
     *
     * @param update Trueの場合アップデート、falseの場合新規レコード作成します。
     * @author chantake
     * @since 2011/03/10
     */
    private void SaveSpawnToDB(boolean update, Location location) {
        try {
            try (JDCConnection con = plugin.getConnection()) {
                PreparedStatement ps;
                if (update) {
                    ps = con.prepareStatement("UPDATE spawnlocation SET name = ?, world = ?,x = ?, y = ?, z = ?, yaw = ?, pitch = ? WHERE id = ?");
                } else {
                    ps = con.prepareStatement("INSERT INTO spawnlocation (name,world,x,y,z,yaw,pitch) VALUES (?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                }
                ps.setString(1, name);
                ps.setInt(2, plugin.getWorldManager().getWorldData(location.getWorld()).getId());
                ps.setDouble(3, location.getX());
                ps.setByte(4, (byte) location.getY());
                ps.setDouble(5, location.getZ());
                ps.setShort(6, (short) location.getYaw());
                ps.setShort(7, (short) location.getPitch());
                if (update) {
                    ps.setInt(8, this.spawn_id);
                }
                int updateRows = ps.executeUpdate();
                if (!update) {
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            this.spawn_id = rs.getInt(1);
                        } else {
                            throw new RuntimeException("Inserting char failed.");
                        }
                    }
                } else if (updateRows < 1) {
                    throw new RuntimeException("SpawnLocation not in database (" + spawn_id + ")");
                }
                ps.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(PlayerInstance.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 放置フラグ
     *
     * @param b 放置True
     * @author chantake
     * @since 2011/03/23
     */
    public void neglect(boolean b) {
        neglect = b;
    }

    /**
     * 放置フラグ取得
     *
     * @author chantake
     * @return
     * @since 2011/03/23
     */
    public boolean neglect() {
        return neglect;
    }

    /**
     * ユーザーに確認メッセージを出し、実行します
     *
     * @param message
     * @param runnable
     */
    public void sendYesNo(String message, Runnable runnable, boolean canskip) {
        //インスタンスを登録
        if (this.setCheckInstance(runnable)) {
            //確認をスキップして実行する
            if (this.isCheckSkip() && canskip) {
                //実行する
                this.executionCheckInstance();
            } else {//スキップしないで確認メッセージを出す
                //確認フラグをセット
                this.setCheck(true);
                //固有メッセージ
                this.sendMessage(ChatColor.YELLOW + message);
                //確認メッセージ
                this.sendMessage(Parameter328.Check_Message);
            }
        }
    }
    
    public void sendYesNo(String message, Runnable runnable) {
        sendYesNo(message, runnable, true);
    }
    
    public void forceSendYesNo(String message, Runnable runnable) {
        sendYesNo(message, runnable, false);
    }

    /**
     * 他のプレーヤーから確認メッセージを送ります
     *
     * @param message メッセージ
     * @param runnable インスタンス
     * @param ins 送るPlayerInstance
     */
    public void sendYesNoFromPlayer(String message, PlayerInstance ins, Runnable runnable) {
        //他の確認をしてる時は断る
        if (this.isCheck()) {
            ins.sendAttention(ins.getName() + " は、現在応答できません。");
        } else {
            this.forceSendYesNo(message, runnable);
        }
    }

    /**
     * ユーザーに確認し、実行します（Mineを使う場合）メッセージは先頭につきます
     *
     * @param mine
     * @param message
     * @param runnable
     */
    public void sendMineYesNo(int mine, String message, Runnable runnable) {
        StringBuilder sb = new StringBuilder();
        //messageがある場合は先頭につける
        if (message != null && message.length() > 0) {
            sb.append(ChatColor.YELLOW).append(message).append(" ");
        }
        sb.append(ChatColor.GOLD).append(mine).append(ChatColor.GREEN).append("mine").append(ChatColor.YELLOW);
        if (mine <= 0) {
            sb.append(" かかりますが、よろしいでしょうか？");
        } else {
            sb.append(" となりますが、よろしいでしょうか？");
        }
        //確認する
        this.sendYesNo(sb.toString(), runnable);
    }

    /**
     * ユーザーに確認し、実行します（Mineを使う場合）
     *
     * @param mine
     * @param runnable
     */
    public void sendMineYesNo(int mine, Runnable runnable) {
        this.sendMineYesNo(mine, null, runnable);
    }

    /**
     * 変数checkを変更します。
     *
     * @since 3.0.2
     */
    public void setCheck() {
        this.check_skip = !check_skip;
    }

    /**
     * 変数checkを返します
     *
     * @return check
     * @since 3.0.2
     */
    public boolean isCheck() {
        return this.check;
    }

    /**
     * 変数checkを変更します。
     *
     * @param check
     * @since 3.0.2
     */
    public void setCheck(boolean check) {
        this.check = check;
    }

    /**
     * 確認をスキップするかどうか
     *
     * @return check_skip
     */
    public boolean isCheckSkip() {
        return check_skip;
    }

    /**
     * 選択している看板を取得します
     *
     * @return 看板が存在しない場合はnullを返します
     * @throws com.chantake.MituyaProject.Exception.PlayerOfflineException
     * @see Sign
     */
    public Block getSignTarget() throws PlayerOfflineException {
        return this.getPlayer().getTargetBlock(null, 25);
    }

    /**
     * 選択している看板を取得します
     *
     * @return 看板が存在しない場合はnullを返します
     * @throws com.chantake.MituyaProject.Exception.PlayerOfflineException
     * @see Sign
     */
    public boolean isSignTarget() throws PlayerOfflineException {
        if (this.getPlayer().getTargetBlock(null, 25).getState() instanceof Sign) {
            return true;
        } else {
            this.sendAttention("看板を選択して下さい。");
            return false;
        }
    }

    /**
     * checkInstanceを実行します
     */
    public void executionCheckInstance() {
        try {
            Player player = this.getPlayer();
            //offline もしくはインスタンスが消えてないかどうか
            if (player.isOnline() && this.getCheckInstance() != null) {
                //インスタンスを実行
                this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, this.getCheckInstance());
                //他のコマンドが残っている場合はそれを実行
                this.plugin.getSignCommandManager().executionCommandTaskFromCheckInstance(player, this);
            }
        } catch (PlayerOfflineException ex) {
            this.sendAttention("エラーが発生しました。GMおよび管理者に連絡してください。");
            plugin.getLogger().log(Level.WARNING, "check instance err.", ex);
        }
        //インスタンスを削除します
        this.removeCheckInstance();
    }

    /**
     * 確認フラグとインスタンスを削除します
     */
    public void removeCheckInstance() {
        //インスタンスを削除
        this.check_instance = null;
        //フラグを消す
        this.setCheck(false);
        //スケジュールをキャンセル
        this.plugin.getServer().getScheduler().cancelTask(this.check_id);
    }

    public void cancelCheckInstance() {
        if (this.isCheck()) {
            this.sendAttention("**キャンセルしました**");
        }
        this.removeCheckInstance();
    }

    /**
     * check instance を設定
     *
     * @param runnable
     */
    private boolean setCheckInstance(Runnable runnable) {
        if (this.check_instance != null) {
            this.sendAttention("別の確認メッセージが表示されています。この処理はキャンセルされました。");
            return false;
        }
        this.check_instance = runnable;
        //自動キャンセル
        this.check_id = this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new CancelCheckInstance(this), Parameter328.Check_Limit * 20L);
        return true;
    }

    // <editor-fold defaultstate="collapsed" desc="sendMessage（プレーヤーにメッセージを表示します。）">

    /**
     * 確認後に実行するインスタンスを取得
     *
     * @return
     */
    private Runnable getCheckInstance() {
        return this.check_instance;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="sendInfo（プレーヤーにinfoメッセージを表示します。）">

    /**
     * プレーヤーにメッセージを表示します。
     *
     * @param message　メッセージ
     * @author chantake
     * @since 2011/03/23
     */
    public void sendMessage(String message) {
        try {
            this.getPlayer().sendMessage(message);
        } catch (NullPointerException | PlayerOfflineException ex) {
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="sendProcessing（プレーヤーに処理中メッセージを表示します。）">

    /**
     * プレーヤーにinfoメッセージを表示します。
     *
     * @param info
     * @param message　メッセージ
     * @author chantake
     * @since 2011/03/23
     */
    public void sendInfo(String info, String message) {
        this.sendMessage("[" + info + ChatColor.WHITE + "] " + message);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="sendSuccess（プレーヤーに成功メッセージを表示します。）">

    /**
     * プレーヤーに処理中メッセージを表示します。
     *
     * @param message　メッセージ
     * @author chantake
     * @since 2011/03/23
     */
    public void sendProcessing(String message) {
        this.sendMessage(ChatColor.LIGHT_PURPLE + message + "...");
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="sendAttention（プレーヤーに注意メッセージを表示します。）">

    /**
     * プレーヤーに成功メッセージを表示します。
     *
     * @param message　メッセージ
     * @author chantake
     * @since 2011/03/23
     */
    public void sendSuccess(String message) {
        this.sendMessage(ChatColor.YELLOW + message);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="sendWarning（プレーヤーに警告メッセージを表示します。）">

    /**
     * プレーヤーに注意メッセージを表示します。
     *
     * @param message　メッセージ
     * @author chantake
     * @since 2011/03/23
     */
    public void sendAttention(String message) {
        this.sendMessage(ChatColor.RED + message);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="sendServer（プレーヤーにサーバーメッセージを表示します。）">

    /**
     * プレーヤーに警告メッセージを表示します。
     *
     * @param message　メッセージ
     * @author chantake
     * @since 2011/03/23
     */
    public void sendWarning(String message) {
        this.sendMessage(ChatColor.RED + "[Warning] " + message);
        plugin.ErrLog("[Warning] Player:" + this.getName() + " " + message);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="sendSystem（プレーヤーにシステムメッセージを表示します。）">

    /**
     * プレーヤーにサーバーメッセージを表示します。
     *
     * @param message　メッセージ
     * @author chantake
     * @since 2011/03/23
     */
    public void sendServer(String message) {
        this.sendMessage(ChatColor.LIGHT_PURPLE + "[Server] " + message);
    }
    // </editor-fold>
    // </editor-fold>

    /**
     * プレーヤーにシステムメッセージを表示します。
     *
     * @param message　メッセージ
     * @author chantake
     * @since 2011/03/23
     */
    public void sendSystem(String message) {
        this.sendMessage(ChatColor.LIGHT_PURPLE + "[System] " + ChatColor.WHITE + message);
    }

    @Override
    public String toString() {
        return this.getName() + "(" + this.id + ")";
    }

    public void WelcomeMessage() throws PlayerOfflineException {
        //アイテムプレゼント
        Player pr = this.getPlayer();
        PlayerInventory inventory = pr.getInventory();
        inventory.addItem(new ItemStack(50, 20));//トーチ
        inventory.addItem(new ItemStack(50, 20));
        inventory.addItem(new ItemStack(272, 3));
        inventory.addItem(new ItemStack(273, 3));
        inventory.addItem(new ItemStack(274, 3));
        inventory.addItem(new ItemStack(275, 3));
        inventory.addItem(new ItemStack(271, 1));
        inventory.addItem(new ItemStack(269, 1));
        inventory.addItem(new ItemStack(322, 10));
        inventory.addItem(new ItemStack(17, 64));
        inventory.addItem(new ItemStack(6, 32));
        this.setSpawn(plugin.getWorldSpawn(plugin.getServer().getWorld("world")));
        plugin.broadcastMessage("[" + ChatColor.YELLOW + "Welcome" + ChatColor.WHITE + "] " + ChatColor.YELLOW + this.getName() + ChatColor.AQUA + " 様、始めまして 328mssServerへようこそ。");
        this.sendInfo(ChatColor.YELLOW + "Welcome", "コマンドやサーバーについての詳しい情報はHPを見てください");
        this.sendInfo(ChatColor.YELLOW + "Welcome", "http://mc.328mss.com/");
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new PlayerTeleport(this), 4 * 10L);//4秒
    }

    @Override
    public void summonMonsters(String monstername, int amount) {
        try {
            if (monstername != null) {
                for (int i = 0; i <= amount; i++) {
                    Location l = getPlayer().getLocation();
                    LivingEntity mob = (LivingEntity) getPlayer().getWorld().spawnEntity(l, EntityType.fromName(monstername));
                }
            }
        } catch (PlayerOfflineException e) {
        }
    }

    public void dropItem(int id, int type, int ammount) throws PlayerOfflineException {
        ItemStack is = new ItemStack(type);
        Location lo = getPlayer().getLocation();
        int r = 0;
        int rr = 0;
        boolean rrr = false;
        if (ammount > 64) {
            r = ammount / 64;
            rr = ammount % 64;
            rrr = true;
        }
        is.setDurability((short) type);
        lo.setY(lo.getY() + 1);
        if (!rrr) {
            is.setAmount(ammount);
            getPlayer().getWorld().dropItem(lo, is);
        } else {
            for (int i = 0; i < r; i++) {
                is.setAmount(64);
                getPlayer().getWorld().dropItem(lo, is);
            }
            is.setAmount(rr);
            getPlayer().getWorld().dropItem(lo, is);
        }
    }

    public void setDeath() throws PlayerOfflineException {
        this.death = this.getPlayer().getLocation().clone();
        this.sendAttention("セーブロケーションを保存しました.");
    }

    public Location getDeath() {
        return this.death;
    }

    public boolean getPvP() {
        return this.pvp;
    }

    public void setPvP(boolean pvp) {
        this.pvp = pvp;
    }

    public void togglePvP() {
        this.pvp = !pvp;
    }

    public int getPvP_Win() {
        return this.pvp_win;
    }

    public void setPvP_Win() {
        ++this.pvp_win;
    }

    public void resetPvP_Win() {
        this.pvp_win = 0;
    }

    public void setHTLocation(Location lo) {
        htlocation = lo;
    }

    public Location getHtlocation() {
        return htlocation;
    }

    public void setHTBlock(Block b) {
        htblock = b;
    }

    public Block getHtblock() {
        return htblock;
    }

    public void setTp() {
        tp = true;
    }

    public boolean isTp() {
        return tp;
    }

    public World getLastworld() {
        return lastworld;
    }

    public void setLastworld(World lastworld) {
        this.lastworld = lastworld;
    }

    public Location getHarvest() {
        if (this.harvest == null) {
            return this.getMainWorld().getSpawnLocation();
        }
        return this.harvest;
    }

    public void setHarvest(Location loc) {
        this.harvest = loc;
    }

    public void setHarvest() throws PlayerOfflineException {
        this.setHarvest(this.getPlayer().getLocation());
    }

    public World getMainWorld() {
        if (this.world_invite) {//招待がある場合
            return plugin.getWorldManager().getWorld(Parameter328.MainWorld);
        } else {
            return plugin.getWorldManager().getWorld(Parameter328.SecondWorld);
        }
    }

    public ChatType getChatType() {
        return chat_type;
    }

    /*public boolean getMainWorldInvite() {
     return this.world_invite;
     }

     public void setMainWorldInvite(boolean invite) {
     this.world_invite = invite;
     }*/
    public void setChatType(ChatType chat_type) {
        this.chat_type = chat_type;
    }

    public int getParty() {
        return this.party;
    }

    public void setParty(int party) {
        this.party = party;
    }

    public void Kick(String reason) throws PlayerOfflineException {
        getPlayer().kickPlayer(reason);
    }

    public void fire(int along) throws PlayerOfflineException {
        getPlayer().setFireTicks(along);
    }

    public int getitemInHandID() throws PlayerOfflineException {
        return getPlayer().getItemInHand().getTypeId();
    }

    public int getSaveLv() {
        return this.level;
    }

    public void saveLv(int level) {
        this.level = level;
    }

    public void setLv(int lv) throws PlayerOfflineException {
        this.getPlayer().setLevel(lv);
    }

    public boolean getShowNote() {
        return this.shownote;
    }

    public void setShowNote(boolean note) {
        this.shownote = note;
    }

    public boolean getIME() {
        return this.ime;
    }

    public void setIME(boolean ime) {
        this.ime = ime;
    }

    /**
     * ニックネームを返します
     *
     * @return
     */
    public String getNickName() {
        return this.nickname;
    }

    public boolean setNickName(String name) {
        if (!MySqlProcessing.isPlayer(name) && !MySqlProcessing.getPlayerNickName(name)) {
            this.nickname = name;
            this.setDisplayName();
            this.SaveCharToDB(true);
            return true;
        } else {
            return false;
        }
    }

    public void removeNickName() throws PlayerOfflineException {
        this.nickname = "";
        this.setDisplayName();
        this.getPlayer().setPlayerListName(this.getName());
        this.SaveCharToDB(true);
    }

    public boolean isNickName() {
        return !this.nickname.equals("") || this.nickname.length() > 0;
    }

    public boolean hasPermission(Rank rank) {
        return this.getRank().getId() >= rank.getId();
    }

    public boolean hasPermission(String permission) {
        try {
            return plugin.permission.has(this.getPlayer(), permission);
        } catch (PlayerOfflineException ex) {
            return false;
        }
    }

    public boolean isDebug() {
        return this.debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    @Override
    public String getPlayerListName() {
        try {
            return getPlayer().getPlayerListName();
        } catch (PlayerOfflineException ex) {
            return "ERROR";
        }
    }

    @Override
    public void setPlayerListName(String name) {
        try {
            getPlayer().setPlayerListName(name);
        } catch (PlayerOfflineException ex) {
        }
    }

    @Override
    public float getExhaustion() {
        try {
            return getPlayer().getExhaustion();
        } catch (PlayerOfflineException ex) {
            return 0;
        }
    }

    @Override
    public void setExhaustion(float value) {
        try {
            getPlayer().setSaturation(value);
        } catch (PlayerOfflineException ex) {
        }
    }

    @Override
    public float getSaturation() {
        try {
            return getPlayer().getSaturation();
        } catch (PlayerOfflineException ex) {
            return 0;
        }
    }

    @Override
    public void setSaturation(float value) {
        try {
            getPlayer().setSaturation(value);
        } catch (PlayerOfflineException ex) {
        }
    }

    @Override
    public float getExp() {
        try {
            return getPlayer().getExp();
        } catch (PlayerOfflineException ex) {
            return 0;
        }
    }

    @Override
    public void setExp(float exp) {
        try {
            getPlayer().setExp(exp);
        } catch (PlayerOfflineException ex) {
        }
    }

    @Override
    public int getFoodLevel() {
        try {
            return getPlayer().getFoodLevel();
        } catch (PlayerOfflineException ex) {
            return 0;
        }
    }

    @Override
    public void setFoodLevel(int value) {
        try {
            getPlayer().setFoodLevel(value);
        } catch (PlayerOfflineException ex) {
        }
    }

    @Override
    public int getTotalExperience() {
        try {
            return getPlayer().getTotalExperience();
        } catch (PlayerOfflineException ex) {
            return 0;
        }
    }

    @Override
    public void setTotalExperience(int te) {
        try {
            this.getPlayer().setTotalExperience(te);
        } catch (PlayerOfflineException ex) {
        }
    }

    @Override
    public void chat(String str) {
        try {
            getPlayer().chat(str);
        } catch (PlayerOfflineException ex) {
        }
    }

    @Override
    public void showPlayer(String player) {
        try {
            Player pl = plugin.getInstanceManager().getInstance(player).getPlayer();
            getPlayer().showPlayer(pl);
        } catch (PlayerOfflineException ex) {
        }
    }

    @Override
    public void hidePlayer(String player) {
        try {
            Player pl = plugin.getInstanceManager().getInstance(player).getPlayer();
            getPlayer().hidePlayer(pl);
        } catch (PlayerOfflineException ex) {
        }
    }

    @Override
    public void kickPlayer(String message) {
        try {
            getPlayer().kickPlayer(message);
        } catch (PlayerOfflineException ex) {
        }
    }

    @Override
    public void shootArrow() {
        try {
            getPlayer().launchProjectile(Arrow.class);
        } catch (PlayerOfflineException ex) {
        }
    }

    @Override
    public void throwEgg() {
        try {
            getPlayer().launchProjectile(Egg.class);
        } catch (PlayerOfflineException ex) {
        }
    }

    @Override
    public void throwEnderPearl() {
        try {
            getPlayer().launchProjectile(EnderPearl.class);
        } catch (PlayerOfflineException ex) {
        }
    }

    @Override
    public void throwFireball() {
        try {
            getPlayer().launchProjectile(Fireball.class);
        } catch (PlayerOfflineException ex) {
        }
    }

    @Override
    public void throwSmallFireBall() {
        try {
            getPlayer().launchProjectile(SmallFireball.class);
        } catch (PlayerOfflineException ex) {
        }
    }

    @Override
    public void throwSnowball() {
        try {
            getPlayer().launchProjectile(Snowball.class);
        } catch (PlayerOfflineException ex) {
        }
    }

    @Override
    public double getHealth() {
        try {
            return getPlayer().getHealth();
        } catch (PlayerOfflineException ex) {
            return -1;
        }
    }

    @Override
    public void setHealth(double health) {
        try {
            getPlayer().setHealth(health);
        } catch (PlayerOfflineException ex) {
        }
    }

    @Override
    public double getMaxHealth() {
        try {
            return getPlayer().getMaxHealth();
        } catch (PlayerOfflineException ex) {
            return -1;
        }
    }

    @Override
    public int getMaximumAir() {
        try {
            return getPlayer().getMaximumAir();
        } catch (PlayerOfflineException ex) {
            return -1;
        }
    }

    @Override
    public void setMaximumAir(int ticks) {
        try {
            getPlayer().setMaximumAir(ticks);
        } catch (PlayerOfflineException ex) {
        }
    }

    @Override
    public int getRemainingAir() {
        try {
            return getPlayer().getRemainingAir();
        } catch (PlayerOfflineException ex) {
            return -1;
        }
    }

    @Override
    public void setRemainingAir(int ticks) {
        try {
            getPlayer().setRemainingAir(ticks);
        } catch (PlayerOfflineException ex) {
        }
    }

    @Override
    public void damage(int amount) {
        try {
            getPlayer().damage(amount);
        } catch (PlayerOfflineException ex) {
        }
    }

    @Override
    public int getMaxFireTicks() {
        try {
            return getPlayer().getMaxFireTicks();
        } catch (PlayerOfflineException ex) {
            return -1;
        }
    }

    @Override
    public int getFireTicks() {
        try {
            return getPlayer().getFireTicks();
        } catch (PlayerOfflineException ex) {
            return -1;
        }
    }

    @Override
    public void setFireTicks(int ticks) {
        try {
            getPlayer().setFireTicks(ticks);
        } catch (PlayerOfflineException ex) {
        }
    }

    @Override
    public void playEntityEffect(String type) {
        try {
            getPlayer().playEffect(EntityEffect.valueOf(type));
        } catch (PlayerOfflineException ex) {
        }
    }

    @Override
    public String checkEntityEffectType() {
        return "HURT DEATH WOLF_SMOKE WOLF_HEARTS WOLF_SHAKE SHEEP_EAT";
    }

    @Override
    public void playEffect(String effect, int data) {
        try {
            getPlayer().playEffect(getPlayer().getLocation(), Effect.valueOf(effect), data);
        } catch (PlayerOfflineException ex) {
        }
    }

    @Override
    public String checkEffectType() {
        return "CLICK2 CLICK1 BOW_FIRE DOOR_TOGGLE EXTINGUISH RECORD_PLAY GHAST_SHRIEK GHAST_SHOOT BLAZE_SHOOT ZOMBIE_CHEW_WOODEN_DOOR ZOMBIE_CHEW_IRON_DOOR ZOMBIE_DESTROY_DOOR SMOKE STEP_SOUND POTION_BREAK ENDER_SIGNAL MOBSPAWNER_FLAMES";
    }

    @Override
    public void playSound(String sound, float volume, float note) {
        try {
            getPlayer().playSound(getPlayer().getLocation(), Sound.valueOf(sound), volume, note);
        } catch (PlayerOfflineException ex) {
        }
    }

    @Override
    public String checkSoundType() {
        return "AMBIENCE_CAVE AMBIENCE_RAIN AMBIENCE_THUNDER ANVIL_BREAK ANVIL_LAND ANVIL_USE ARROW_HIT BREATH BURP CHEST_CLOSE CHEST_OPEN CLICK DOOR_CLOSE DOOR_OPEN DRINK EATEXPLODEFALL_BIGFALL_SMALLFIREFIRE_IGNITEFIZZFUSEGLASSHURTHURT_FLESHITEM_BREAKITEM_PICKUPLAVALAVA_POPLEVEL_UPMINECART_BASEMINECART_INSIDENOTE_BASSNOTE_PIANONOTE_BASS_DRUMNOTE_STICKSNOTE_BASS_GUITARNOTE_SNARE_DRUMNOTE_PLINGORB_PICKUPPISTON_EXTENDPISTON_RETRACTPORTALPORTAL_TRAVELPORTAL_TRIGGERSHOOT_ARROWSPLASHSPLASH2STEP_GRASSSTEP_GRAVELSTEP_LADDERSTEP_SANDSTEP_SNOWSTEP_STONESTEP_WOODSTEP_WOOLSWIMWATERWOOD_CLICKBAT_DEATHBAT_HURTBAT_IDLEBAT_LOOPBAT_TAKEOFFBLAZE_BREATHBLAZE_DEATHBLAZE_HITCAT_HISSCAT_HITCAT_MEOWCAT_PURRCAT_PURREOWCHICKEN_IDLECHICKEN_HURTCHICKEN_EGG_POPCHICKEN_WALKCOW_IDLECOW_HURTCOW_WALKCREEPER_HISSCREEPER_DEATHENDERDRAGON_DEATHENDERDRAGON_GROWLENDERDRAGON_HITENDERDRAGON_WINGSENDERMAN_DEATHENDERMAN_HITENDERMAN_IDLEENDERMAN_TELEPORTENDERMAN_SCREAMENDERMAN_STAREGHAST_SCREAMGHAST_SCREAM2GHAST_CHARGEGHAST_DEATHGHAST_FIREBALLGHAST_MOANIRONGOLEM_DEATHIRONGOLEM_HITIRONGOLEM_THROW IRONGOLEM_WALK MAGMACUBE_WALK MAGMACUBE_WALK2 MAGMACUBE_JUMP PIG_IDLE PIG_DEATH PIG_WALK SHEEP_IDLE SHEEP_SHEAR SHEEP_WALK SILVERFISH_HIT SILVERFISH_KILL SILVERFISH_IDLE SILVERFISH_WALK SKELETON_IDLE SKELETON_DEATH SKELETON_HURT SKELETON_WALK SLIME_ATTACK SLIME_WALK SLIME_WALK2 SPIDER_IDLE SPIDER_DEATH SPIDER_WALK WITHER_DEATH WITHER_HURT WITHER_IDLE WITHER_SHOOT WITHER_SPAWN WOLF_BARK WOLF_DEATH WOLF_GROWL WOLF_HOWL WOLF_HURT WOLF_PANT WOLF_SHAKE WOLF_WALK WOLF_WHINE ZOMBIE_METAL ZOMBIE_WOOD ZOMBIE_WOODBREAK ZOMBIE_IDLE ZOMBIE_DEATH ZOMBIE_HURT ZOMBIE_INFECT ZOMBIE_UNFECT ZOMBIE_REMEDY ZOMBIE_PIG_IDLE ZOMBIE_PIG_ANGRY ZOMBIE_PIG_DEATH ZOMBIE_PIG_HURT DIG_WOOL DIG_GRASS DIG_GRAVEL DIG_SAND DIG_SNOW DIG_STONE DIG_WOOD";
    }

    @Override
    public void addPotionEffect(String type, int duration, int amplifier) {
        try {
            getPlayer().addPotionEffect(PotionEffectType.getByName(type).createEffect(duration, amplifier));
        } catch (PlayerOfflineException ex) {
        }
    }

    @Override
    public String checkPotionEffectType() {
        return "SPEED SLOW FAST_DIGGING SLOW_DIGGING INCREASE_DAMAGE HEAL HARM JUMP CONFUSION REGENERATION DAMAGE_RESISTANCE FIRE_RESISTANCE WATER_BREATHING INVISIBILITY BLINDNESS NIGHT_VISION HUNGER WEAKNESS POISON ";
    }

    @Override
    public void addUnsafeEnchantment(String type, int level) {
        try {
            getPlayer().getItemInHand().addUnsafeEnchantment(Enchantment.getByName(type), level);
        } catch (PlayerOfflineException ex) {
        }
    }

    @Override
    public void addEnchantment(String type, int level) {
        try {
            getPlayer().getItemInHand().addEnchantment(Enchantment.getByName(type), level);
        } catch (PlayerOfflineException ex) {
        }
    }

    @Override
    public String checkEnchantmentType() {
        return "PROTECTION_ENVIRONMENTAL PROTECTION_FIRE PROTECTION_FALL PROTECTION_EXPLOSIONS PROTECTION_PROJECTILE OXYGEN WATER_WORKER DAMAGE_ALL DAMAGE_UNDEAD DAMAGE_ARTHROPODS KNOCKBACK FIRE_ASPECT LOOT_BONUS_MOBS DIG_SPEED SILK_TOUCH DURABILITY LOOT_BONUS_BLOCKS ARROW_DAMAGE ARROW_KNOCKBACK ARROW_FIRE ARROW_INFINITE";
    }

    public boolean teleport(Location l) throws PlayerOfflineException {
        if (damagetick) {
            sendAttention("直前に喰らったダメージによりテレポートが出来ない！");
            return false;
        } else {
            getPlayer().teleport(l);
            return true;
        }
    }

    public Timestamp getLastlogin() {
        return lastlogin;
    }

    public Iterator<String> getCommandTask() {
        return command_tasks.iterator();
    }

    public List<String> getCommandTasks() {
        return this.command_tasks;
    }

    public void setCommandTasks(List task) {
        this.command_tasks = task;
    }

    public void removeCommandTasks() {
        this.command_tasks.clear();
    }
}

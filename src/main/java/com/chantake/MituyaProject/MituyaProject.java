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
package com.chantake.MituyaProject;

import com.chantake.MituyaProject.Api.ConnectionManager;
import com.chantake.MituyaProject.Bukkit.JapaneseMessage;
import com.chantake.MituyaProject.Bukkit.Listener.*;
import com.chantake.MituyaProject.Bukkit.MituyaProjectListener;
import com.chantake.MituyaProject.Command.*;
import com.chantake.MituyaProject.Event.EventScriptManager;
import com.chantake.MituyaProject.Exception.MituyaProjectException;
import com.chantake.MituyaProject.Exception.PlayerNotFoundException;
import com.chantake.MituyaProject.Exception.PlayerOfflineException;
import com.chantake.MituyaProject.Gachapon.GachaponDataManager;
import com.chantake.MituyaProject.MBean.Debug;
import com.chantake.MituyaProject.MBean.Server;
import com.chantake.MituyaProject.Midi.JingleNoteManager;
import com.chantake.MituyaProject.Parameter.Parameter328;
import com.chantake.MituyaProject.Permissions.MituyaPermissionManager;
import com.chantake.MituyaProject.Permissions.Rank;
import com.chantake.MituyaProject.Player.InstanceManager;
import com.chantake.MituyaProject.Player.Mail.MailManager;
import com.chantake.MituyaProject.Player.PlayerInstance;
import com.chantake.MituyaProject.Player.Sign.ItemChestManager;
import com.chantake.MituyaProject.Player.Sign.SignCommandManager;
import com.chantake.MituyaProject.Player.Sign.SignElevatorManager;
import com.chantake.MituyaProject.Protocol.Listener.SignSendWrapper;
import com.chantake.MituyaProject.RSC.RedstoneChips;
import com.chantake.MituyaProject.Tool.Dynmap.DynmapApiConnecter;
import com.chantake.MituyaProject.Tool.HiraganaToKanji;
import com.chantake.MituyaProject.Tool.Log.LogType;
import com.chantake.MituyaProject.Tool.Log.MituyaLogManager;
import com.chantake.MituyaProject.Tool.MituyaModPacket.PluginPacketManager;
import com.chantake.MituyaProject.Tool.MySqlProcessing;
import com.chantake.MituyaProject.Tool.PerformanceMonitor;
import com.chantake.MituyaProject.Tool.Timer.*;
import com.chantake.MituyaProject.Tool.Tools;
import com.chantake.MituyaProject.Tool.Twitter.TwitterManager;
import com.chantake.MituyaProject.World.MobManager.MobManager;
import com.chantake.MituyaProject.World.Pack.PackBookManager;
import com.chantake.MituyaProject.World.Shop.ChestShopManager;
import com.chantake.MituyaProject.World.Shop.CommandShopManager;
import com.chantake.MituyaProject.World.WorldManager;
import com.chantake.mituyaapi.commands.*;
import com.chantake.mituyaapi.tools.database.DatabaseConnectionManager;
import com.chantake.mituyaapi.tools.database.JDCConnection;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.griefcraft.lwc.LWC;
import com.griefcraft.lwc.LWCPlugin;
import com.griefcraft.model.Protection;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.scheduler.BukkitScheduler;

import javax.management.*;
import javax.script.ScriptEngine;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * MituyaProject for Bukkit
 *
 * @author chantake
 */
public final class MituyaProject extends JavaPlugin {

    private final MituyaProjectPlayerListener playerListener = new MituyaProjectPlayerListener(this);
    private final MituyaProjectBlockListener blockListener = new MituyaProjectBlockListener(this);
    private final MituyaProjectEntityListener entityListener = new MituyaProjectEntityListener(this);
    private final MituyaProjectWorldListener worldListener = new MituyaProjectWorldListener(this);
    private final MituyaProjectServerListener serverListener = new MituyaProjectServerListener(this);
    private final MituyaProjectInventoryListener inventoryListener = new MituyaProjectInventoryListener(this);
    private final MituyaProjectPluginMessageListener pluginmsgListener = new MituyaProjectPluginMessageListener(this);
    private final MituyaChestProtectListener chestProtectListener = new MituyaChestProtectListener(this);
    private final PerformanceMonitor performanceMonitor = new PerformanceMonitor(this);
    private final JapaneseMessage japaneseMessage = new JapaneseMessage(this);
    private final InstanceManager instanceManager = new InstanceManager(this);
    private final WorldManager worldManager = new WorldManager(this);
    private final SignCommandManager signCommandManager = new SignCommandManager(this);
    private final SignElevatorManager elevatorManager = new SignElevatorManager(this);
    private final ChestShopManager chestShopManager = new ChestShopManager(this);
    private final ItemChestManager itemChestManager = new ItemChestManager(this);
    private final PackBookManager bookManager = new PackBookManager(this);
    private final MituyaLogManager logManager = new MituyaLogManager(this);
    private final Debug debug = new Debug(this);
    private final HashMap<Player, Boolean> debugees = new HashMap<>();
    private final TwitterManager twitter = new TwitterManager(this);
    private final MailManager mailManager = new MailManager(this);
    private final ConnectionManager connectionManager = new ConnectionManager(this);
    private final MituyaPermissionManager permissionManager = new MituyaPermissionManager(this);
    private final DynmapApiConnecter dynmap = new DynmapApiConnecter(this);
    private final MobManager mobmanager = new MobManager(this);
    //ホッパー実装につき廃止（処理がかぶるため）
    //private DropChest dropitem = new DropChest(this);//吸引力が変わらないチェスト
    private final CommandShopManager cmdshop = new CommandShopManager(this);//コマンドショップ
    private final String cfile = "config.properties";
    private final HashMap<String, ScriptEngine> engine = new HashMap<>();
    private final RedstoneChips redstoneChips = new RedstoneChips(this);
    private final GachaponDataManager gachaponDataManager = new GachaponDataManager(this);
    public Permission permission = null;
    public Economy economy = null;
    public Chat chat = null;
    public Tools tool;
    public JingleNoteManager jingleNoteManager;
    public long uptime_start = System.currentTimeMillis();
    public Calendar uptime = Calendar.getInstance(TimeZone.getTimeZone("GMT+9:00"));
    // Plugin Name
    public String name = "MituyaProject";
    // Plugin CodeName
    public String codename = "Mituya";
    // PacketChannel
    public String packetchannel = "Mituya";
    public Properties prop = new Properties();
    public String directry = "plugins/" + name;
    private final File config = new File(directry + File.separator + cfile);
    public String scriptPath = this.directry + "/script/";
    private DatabaseConnectionManager databaseConnectionManager = null;
    private EventScriptManager eventScriptManager;
    private WorldGuardPlugin worldguard = null;
    private LWC lwc = null;
    /**
     * バージョン<br> ＜メジャー・バージョン＞.＜マイナー・バージョン＞.＜リビジョン＞
     */
    private CommandsManager<PlayerInstance> commands;
    private ProtocolManager protocolManager;

    private static class MituyaProjectHolder {
        protected static MituyaProject instance;
        
    }

    public static MituyaProject getInstance() {
        return MituyaProjectHolder.instance;
    }

    /**
     * プラグインアンロード処理
     */
    @Override
    public void onDisable() {
        SaveAll();
        jingleNoteManager.stopAll();
        TimerManager.getInstance().stop();
        this.Log("データベースを切断中です・・・");
        final MituyaProject plugin = this;
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                plugin.getDatabaseConnectionManager().close();
                plugin.Log("DBをすべて切断しました");
            }
        });
        try {
            th.start();
            th.join(20L);
        } catch (InterruptedException ex) {
            Logger.getLogger(MituyaProject.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.Log("DBをすべて切断しました。");
        //redstoneChips.Disable();
        this.Log("プラグインを無効化しました");
    }

    /**
     * プラグインロード時処理
     */
    @Override
    public void onEnable() {
        MituyaProjectHolder.instance = this;
        this.Log("初期設定を開始します");
        try {
            //DB初期設定
            final Properties db = new Properties();
            try (FileReader fdb = new FileReader("plugins/MituyaProject/db.properties")) {
                db.load(fdb);
                databaseConnectionManager = new DatabaseConnectionManager(db.getProperty("url"), db.getProperty("user"), db.getProperty("password"));
            }
            MySqlProcessing.setPlugin(this);
            this.Log("DBに接続しました");
        } catch (IOException ex) {
            Logger.getLogger(MituyaProject.class.getName()).log(Level.SEVERE, null, ex);
        }
        //メンテナンスモード設定
        if (this.getServer().getPort() != 25565) {
            Parameter328.Mentenance = true;
        }
        this.Log("メンテナンスモード：" + Parameter328.Mentenance);
        //ワールドロード
        this.worldManager.LoadWorlds();
        //Vault連携
        setupPermissions();
        //ProtocolLib
        protocolManager = ProtocolLibrary.getProtocolManager();
        //protocolManager.addPacketListener(new ServerPingWrapper(this));
        //protocolManager.addPacketListener(new SignSendWrapper(this));

        try {
            final PluginManager pm = getServer().getPluginManager();
            pm.registerEvents(playerListener, this);
            // Block
            pm.registerEvents(blockListener, this);
            // Entitiy
            pm.registerEvents(entityListener, this);
            // World
            pm.registerEvents(worldListener, this);
            //server
            pm.registerEvents(serverListener, this);
            //inventory
            pm.registerEvents(inventoryListener, this);
            //new Listener
            pm.registerEvents(new MituyaProjectListener(this), this);
            //chestprotect
            pm.registerEvents(chestProtectListener, this);

            Messenger ms = Bukkit.getMessenger();
            PluginPacketManager.setPlugin(this);
            ms.registerOutgoingPluginChannel(this, packetchannel);//MituyaModパケット
            ms.registerIncomingPluginChannel(this, packetchannel, pluginmsgListener);//受信イベント

            jingleNoteManager = new JingleNoteManager();
            Parameter328.run();

        } catch (final Exception ex) {
            Logger.getLogger(MituyaProject.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            ManagementFactory.getPlatformMBeanServer().registerMBean(new Server(this), ObjectName.getInstance("328:type=Infomation"));
            ManagementFactory.getPlatformMBeanServer().registerMBean(this.debug, ObjectName.getInstance("328:type=Debug"));
        } catch (final NullPointerException | InstanceAlreadyExistsException | MBeanRegistrationException | MalformedObjectNameException | NotCompliantMBeanException e) {
        }
        Timer();// Timerスタート
        MySqlProcessing.MituyaProject(this);
        MySqlProcessing.getServer();
        //コンフィグファイル読み込み
        this.setupConfig();
        //各種初期化
        this.init();
        //this.getServer().getScheduler().scheduleSyncRepeatingTask(this, dropitem, 20, 20);//ダイソンチェストの設定
        if (this.getServer().getOnlinePlayers().size() > 0) {//オンラインプレーヤーがいる場合
            for (Player pr : this.getServer().getOnlinePlayers()) {
                this.getInstanceManager().getInstance(pr).Login(pr);
            }
        } else {
            if (!Parameter328.Mentenance) {
                final MituyaProject plugin = this;
                this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                    @Override
                    public void run() {
                        plugin.sendTwitter("サーバーを起動しました motd:" + getServer().getMotd() + " #minecraft");
                    }
                }, 10 * 20L);
            }
        }
        this.Log("みつやプラグイン version." + getDescription().getVersion() + " は有効です");
    }

    /**
     * 初期化
     */
    public void init() {
        //PerformanceMonitor
        this.getPerformanceMonitor().init();
        //PermissionManager
        this.getMituyaPermissionManager().init();
        //JapaneseMessage
        this.getJapaneseMessage().init();
        //Command
        this.initCommands();
        //InstanceManager
        this.getInstanceManager().init();
        //CommandManager
        this.getCommandShopManager().init();
        //MobManager
        this.getMobManager().init();
        //Dynmap
        this.getDynmap().init();
        //ScriptManager
        this.eventScriptManager.init();
        //signCommandManager
        this.getSignCommandManager().init();
        //signElevatorManager
        this.getSignElevatorManager().init();
        //ChestShopManager
        this.getChestShopManager().init();
        //itemChestManager
        this.getItemChestManager().init();
        //PackBookManager
        this.getPackBookManager().init();
        //LogManager
        this.getLogManager().init();
        //redstoneChips
        //this.getRedstoneChips().initRSC();
        //gachaponDataManager
        this.gachaponDataManager.init();
        //ConnectionManager
        Thread tl = new Thread(this.getConnectionManager());
        tl.start();
        //ワールドガード
        this.setWorldGuard();
        //lwc
        this.setLWC();
        //IME
        new HiraganaToKanji();
        try {
            //Twitter
            this.getTwitterManager().init();
        } catch (Exception e) {
            this.ErrLog("twitter : " + e);
        }
    }

    /**
     * ワールドメッセージ
     *
     * @param world
     * @param message
     */
    public void broadcastWorldMessage(World world, String message) {
        for (Player player : world.getPlayers()) {
            player.sendMessage(message);
        }
    }

    /**
     * Instanceを取得します
     *
     * @return
     * @see InstanceManager
     */
    public InstanceManager getInstanceManager() {
        return this.instanceManager;
    }

    /**
     * ブロードキャストメッセージ
     *
     * @param cr
     * @param message
     */
    public void broadcastMessage(String cr, String message) {
        for (Player pr : this.getServer().getOnlinePlayers()) {
            this.getInstanceManager().getInstance(pr).sendInfo(cr, message);
        }
    }

    /**
     * 日英自動切り替え機能付きブロードキャストメッセージ
     *
     * @param message
     */
    public void broadcastMessage(String message) {
        for (Player pr : this.getServer().getOnlinePlayers()) {
            pr.sendMessage(message);
        }
    }

    /**
     * GMブロードキャストメッセージ
     *
     * @param message メッセージ
     */
    public void broadcastGMMessage(String message) {
        for (Player pr : this.getServer().getOnlinePlayers()) {
            PlayerInstance ins = this.getInstanceManager().getInstance(pr);
            if (ins.hasPermission(Rank.GM)) {//GM以上
                ins.sendMessage(message);
            }
        }
    }

    /**
     * 範囲ブロードキャストメッセージ
     *
     * @param location Locaiton
     * @param message メッセージ
     */
    public void broadcastRangeMessage(Location location, String message) {
        for (Player pr : getRangePlayers(location, Parameter328.ChatRange)) {
            pr.sendMessage(message);
        }
    }

    public void broadcastDebugMessage(String message) {
        for (Player pr : this.getServer().getOnlinePlayers()) {
            PlayerInstance ins = this.getInstanceManager().getInstance(pr);
            if (ins.isDebug()) {//Debugモード
                ins.sendMessage("[Debug] " + message);
            }
        }
    }

    /**
     * 範囲内のプレーヤーを取得します
     *
     * @param location 中心座標
     * @param range 範囲
     * @return
     */
    public List<Player> getRangePlayers(Location location, int range) {
        List<Player> list = new ArrayList<>();
        for (Player pr : location.getWorld().getPlayers()) {
            int x = Math.abs(location.getBlockX());
            int px = Math.abs(pr.getLocation().getBlockX());
            int y = Math.abs(location.getBlockY());
            int py = Math.abs(pr.getLocation().getBlockY());
            if (px <= (x + range) && px >= (x - range) && py <= (y + range) && py >= (y - range)) {
                list.add(pr);
            }
        }
        return list;

    }

    /**
     * コマンドを実行します
     *
     * @param player Player
     * @param command コマンド
     * @return
     */
    public boolean handleCommand(Player player, String command) {
        PlayerInstance ins = this.getInstanceManager().getInstance(player);
        return handleCommand(player, ins, command.split(" "));
    }

    /**
     * コマンドを実行します
     *
     * @param player Player
     * @param split コマンド
     * @return
     */
    public boolean handleCommand(Player player, String[] split) {
        PlayerInstance ins = this.getInstanceManager().getInstance(player);
        return handleCommand(player, ins, split);
    }

    /**
     * コマンドを実行します
     *
     * @param player Player
     * @param ins PlayerInstance
     * @param command コマンド
     * @return
     */
    public boolean handleCommand(Player player, PlayerInstance ins, String command) {
        return this.handleCommand(player, ins, command.split(" "));
    }

    /**
     * コマンドを実行します
     *
     * @param player Player
     * @param ins PlayerInstance
     * @param split コマンド
     * @return コマンドが正常実行された場合はtrueが返ります
     */
    public boolean handleCommand(Player player, PlayerInstance ins, String[] split) {
        try {
            if (split.length == 0) {
                return false;
            }

            if (!ins.isAllowCmd()) {
                return false;
            }

            if (split[0].charAt(0) == '/' || split[0].charAt(0) == '・') {
                split[0] = split[0].substring(1);// 先頭文字を削除
            }

            //検索用コマンド
            String searchCmd = split[0].toLowerCase();

            //コマンドを検索
            if (!this.commands.hasCommand(searchCmd)) {
                return false;
            }

            //コマンド処理を開始
            long start = System.currentTimeMillis();

            try {
                this.commands.execute(split, ins, this, player, ins);
                //コマンドログ
                this.getLogManager().putLog(LogType.Command, player.getName(), split);
            } catch (CommandPermissionsException e) {
                ins.sendAttention("このコマンドを使うための権限がないか、このワールドでは許可されていません。");
                ins.removeCommandTasks();
                return false;
            } catch (MissingNestedCommandException e) {
                ins.sendAttention("コマンドが見つかりません: /" + searchCmd + "  " + e.getUsage());
                ins.removeCommandTasks();
                return false;
            } catch (CommandUsageException e) {
                ins.sendAttention("区切り(引数)が多すぎます  " + e.getUsage());
                ins.removeCommandTasks();
                return false;
            } catch (WrappedCommandException e) {
                throw e.getCause();
            } catch (UnhandledCommandException e) {
                ins.removeCommandTasks();
                return false;
            } catch (CommandException ex) {
                ins.sendAttention(ex.getMessage());
                ins.removeCommandTasks();
                return false;
            } finally {
                if (permission.has(player, "mituya.command.manager")) {
                    long time = System.currentTimeMillis() - start;
                    long second = time / 1000;
                    if (second >= 0.2) {
                        ins.sendInfo(ChatColor.GRAY + "CommandManager", "コマンド経過時間： " + time + "ms(ミリ秒)" + " " + second + "s(秒)");
                    }
                }
            }
        } catch (NumberFormatException e) {
            ins.sendAttention("数字を入力してください");
            ins.removeCommandTasks();
            return false;
        } catch (PlayerNotFoundException e) {
            ins.sendAttention("プレーヤー " + ChatColor.YELLOW + e.getPlayerName() + ChatColor.RED + " は見つかりませんでした。");
            ins.removeCommandTasks();
            return false;
        } catch (PlayerOfflineException e) {
            ins.sendAttention("プレーヤー " + ChatColor.YELLOW + e.getPlayerName() + ChatColor.RED + " はオフラインです。");
            ins.removeCommandTasks();
            return false;
        } catch (MituyaProjectException e) {
            ins.sendAttention(e.getMessage());
            ins.removeCommandTasks();
            return false;
        } catch (Throwable excp) {
            if (ins.hasPermission(Rank.GM)) {
                ins.sendAttention("エラーが発生しました。コンソールをご確認ください。");
            } else {
                ins.sendAttention("エラーが発生しました。管理者へ報告してください。");
            }
            this.getLogger().log(Level.WARNING, "コマンドエラー", excp);
            ins.removeCommandTasks();
            return false;
        }
        //看板コマンド
        this.getSignCommandManager().executionCommandTask(player, ins);
        return true;
    }

    /**
     * コマンドがあるかどうかを判定します
     *
     * @param command コマンド
     * @return コマンドがあるとtrueが返ります
     */
    public boolean hasCommand(String command) {
        return this.commands.hasCommand(command);
    }

    /**
     * すべてのコマンドを返します
     *
     * @return
     */
    public Map<String, String> getCommands() {
        return commands.getCommands();
    }

    /**
     * セーブオール
     */
    public void SaveAll() {
        this.getWorldManager().saveWorlds();// Mapセーブ
        this.getServer().savePlayers();// プレーヤーセーブ
        SavePlayerInstance();// インスタンスセーブ
        //redstoneChips.getCircuitPersistence().saveCircuits();
    }

    /**
     * すべてのプレーヤーをセーブします
     */
    public void SavePlayerInstance() {
        for (final Player pr : this.getServer().getOnlinePlayers()) {
            try {
                this.getInstanceManager().getInstance(pr).SaveAll();
            } catch (Exception ex) {
                this.ErrLog("SavePlayerInstance Err:" + pr.getName());
            }
        }

    }

    public void setDebugging(final Player player, final boolean value) {
        debugees.put(player, value);
    }

    public Location getWorldSpawn(World wd) {
        if (wd.getName().equalsIgnoreCase("world")) {
            return new Location(wd, 0, 64, 0, -90, 0);
        } else if (wd.getName().equalsIgnoreCase("world_nether")) {
            return new Location(wd, 0, 64, 0, 0, 0);
        } else if (wd.getName().equalsIgnoreCase("skyland")) {
            return new Location(wd, 12, 80, 8, 2, 0);
        } else if (wd.getName().equalsIgnoreCase("new_world")) {
            return new Location(wd, 96, 64, -8, 1, 0);
            /*
             * } else if (wd.getName().equalsIgnoreCase("harvest")) { return wd.getSpawnLocation();
             */
        } else {
            return wd.getSpawnLocation();
        }
    }

    /**
     * WorldMangerを返します
     *
     * @return WorldManager
     * @see WorldManager
     */
    public WorldManager getWorldManager() {
        return this.worldManager;
    }

    /**
     * サーバーを止めます
     */
    public void Stop() {
        this.Log("サーバーを停止しています・・・");
        this.sendTwitter("サーバーを停止しました 328mss.com:25565 #minecraft");
        this.getServer().shutdown();
    }

    /**
     * タイマー初期化
     */
    public void Timer() {
        BukkitScheduler scheduler = this.getServer().getScheduler();
        //AutoSave 25分のはず
        scheduler.scheduleSyncRepeatingTask(this, new AutoSave(this), 30 * 60 * 20L, 30 * 60 * 20L);
        //Tipsたぶん5分
        scheduler.scheduleSyncRepeatingTask(this, new Tips(this), 5 * 60 * 20L, 5 * 60 * 20L);
        //BorderGuardたぶん3秒
        scheduler.scheduleSyncRepeatingTask(this, new BorderGuard(this), 3 * 20L, 3 * 20L);
        //RemoveMobTime
        scheduler.scheduleSyncRepeatingTask(this, new AutoRemove(this), Parameter328.removeMobtime * 60 * 20L, Parameter328.removeMobtime * 60 * 20L);
        //
        scheduler.scheduleSyncRepeatingTask(this, new AutoSqlSave(this), 60 * 20L, 60 * 20L);
        TimerManager tMan = TimerManager.getInstance();
        tMan.start();
    }

    /**
     * ワールドをアンロードして削除します
     *
     * @param world ワールド名
     * @return 成功した場合はtrue
     */
    public boolean DeleteWorld(String world) {
        this.getServer().unloadWorld(world, false);//unloadの前にチャンクを保存しない
        //ワールドディレクトリを削除
        File dir = new File(world);
        if (dir.isDirectory()) {
            try {
                FileUtils.deleteDirectory(dir);
                this.Log("ワールド：" + world + " ディレクトリを削除しました");
                return true;
            } catch (IOException e) {
                this.ErrLog("ワールドディレクトリを削除できませんでした :" + e);
                return false;
            }
        } else {
            this.ErrLog("ワールド：" + world + "は、存在しないか、ディレクトリではありません");
            return false;
        }
    }

    /**
     * WorldGuardを設定します
     */
    private void setWorldGuard() {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            this.ErrLog("WorldGuardが見つかりません");
        } else {
            this.worldguard = (WorldGuardPlugin) plugin;
            this.Log("WorldGuardが見つかりました");
        }
    }

    /**
     * LWCを設定します
     */
    private void setLWC() {
        Plugin lwcPlugin = getServer().getPluginManager().getPlugin("LWC");
        if (lwcPlugin != null) {
            this.lwc = ((LWCPlugin) lwcPlugin).getLWC();
            this.Log("LWCが見つかりました");
        } else {
            this.ErrLog("LWCが見つかりませんでした");
        }
    }

    /**
     * LWCを取得します
     *
     * @return LWC
     * @see LWC
     */
    public LWC getLWC() {
        return this.lwc;
    }

    public WorldGuardPlugin getWorldGuard() {
        return this.worldguard;
    }

    public DynmapApiConnecter getDynmap() throws NullPointerException {
        return this.dynmap;
    }

    /**
     * そのブロックにプレーヤーがアクセスする権限があるかどうかを調べます
     *
     * @param player Player
     * @param block Block
     * @return WorldGuardとLWCから検索し、両方でのアクセス許可があった場合のみtrueを返します
     */
    public boolean canBuild(Player player, Block block) {
        if (this.getWorldGuard().canBuild(player, block.getLocation())) {
            Protection protection = this.getLWC().findProtection(block);  // also accepts x, y, z

            if (protection != null) {//lwc保護がある場合
                return this.getLWC().canAccessProtection(player, block);
            }
            //ないのでtrue
            return true;
        }
        return false;
    }

    public void ErrLog(String message) {
        this.Log("エラー： " + message);
    }

    public void Log(String message) {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(this.name).append("] ").append(message);
        this.getServer().getLogger().log(Level.INFO, sb.toString());
    }

    /**
     * @param message
     */
    public void sendTwitter(String message) {
        try {
            this.getTwitterManager().send328Tweet(message);
        } catch (Exception e) {
        }
    }

    public void setupConfig() {
        new File(directry).mkdir();
        String savelocation = "MituyaProject.mine.use.location";
        String savespawn = "MituyaProject.mine.use.spawn";
        String spawnback = "MituyaProject.mine.spawn.spawnback";
        String tax = "MituyaProject.mine.tax";
        String say = "MituyaProject.mine.use.say";
        String border = "MituyaProject.message.border";
        String exp = "MituyaProject.world.exp";
        String ets = "MituyaProject.event.script";
        String drop = "MituyaProject.world.drop";
        if (!config.exists()) {
            try {
                this.Log("設定ファイルを作成中・・・");
                config.createNewFile();
                OutputStreamWriter osw;
                try (FileOutputStream out = new FileOutputStream(config)) {
                    osw = new OutputStreamWriter(out, "UTF-8");
                    prop.put(savelocation, String.valueOf(Parameter328.savelocation));
                    prop.put(savespawn, String.valueOf(Parameter328.savespawn));
                    prop.put(spawnback, String.valueOf(Parameter328.spawnback));
                    prop.put(say, String.valueOf(Parameter328.say));
                    prop.put(border, Parameter328.border);
                    prop.put(exp, String.valueOf(Parameter328.Exp));
                    prop.put(drop, String.valueOf(Parameter328.DropRate));
                    prop.put(ets, "test");
                    prop.store(osw, "");
                }
                osw.close();
                this.Log("設定ファイルを作成しました");
            } catch (IOException e) {
                this.Log("設定ファイルを作成できませんでした。デフォルトのファイルを使用してください。");
                return;
            }
        }
        try {
            InputStreamReader isr;
            try (FileInputStream in = new FileInputStream(config)) {
                isr = new InputStreamReader(in, "UTF-8");
                prop.load(isr);
                //Parameter328.savelocation = Integer.valueOf(prop.getProperty(savelocation));
                //Parameter328.savespawn = Integer.valueOf(prop.getProperty(savespawn));
                //Parameter328.spawnback = Integer.valueOf(prop.getProperty(spawnback));
                //Parameter328.tax = Float.valueOf(prop.getProperty(savespawn));
                //Parameter328.say = Integer.valueOf(prop.getProperty(say));
                Parameter328.border = prop.getProperty(border);
                Parameter328.Exp = Float.valueOf(prop.getProperty(exp));
                this.eventScriptManager = new EventScriptManager(this, prop.getProperty(ets).split(","));
                //Parameter328.DropRate = Integer.valueOf(prop.getProperty(drop));
            }
            isr.close();
            this.Log("設定ファイルの読み込みが完了しました");
        } catch (IOException e) {
            this.Log("設定ファイルが読み込みできませんでした。再起動してください。");
        }
    }

    public void setScriptEngine(String path, ScriptEngine engine) {
        this.engine.put(path, engine);
    }

    public ScriptEngine getScriptEngine(String path) {
        return this.engine.get(path);
    }

    public void removeScriptEngine(String path) {
        this.engine.remove(path);
    }

    public void initCommands() {
        this.Log("コマンドを初期化します");
        commands = new CommandsManager<PlayerInstance>() {
            @Override
            public boolean hasPermission(PlayerInstance ins, String perm) {
                try {

                    return permission.has(ins.getPlayer(), perm);
                } catch (PlayerOfflineException ex) {
                    System.out.print(ex);
                    return false;
                }
            }
        };

        commands.register(GeneralCommands.class);
        commands.register(DebuggingCommands.class);
        commands.register(ServerCommands.class);//サーバーコマンド
        commands.register(TeleportCommands.class);//テレポートコマンド
        commands.register(HomeCommands.class);//ホームコマンド
        commands.register(WarpCommands.class);//ワープコマンド
        commands.register(SaveCommands.class);//セーブコマンド
        commands.register(ShopCommands.class);//ショップコマンド
        commands.register(ChatCommands.class);//チャットコマンド
        commands.register(PartyCommands.class);//パーティコマンド
        commands.register(MobSpawnerCommands.class);//スポーンブロックコマンド
        commands.register(MoneyCommands.class);//マネーコマンド
        commands.register(PluginCommands.class);//プラグインコマンド
        commands.register(SignCommands.class);//看板コマンド
        commands.register(NoteBlockCommands.class);//NoteBlockコマンド
        commands.register(NPCCommands.class);//NPCコマンド
        commands.register(AutomaticConversionCommands.class);//ACコマンド
        commands.register(NickNameCommands.class);//ニックネームコマンド
        commands.register(MailCommands.class);//メールコマンド
        commands.register(ApiCommands.class);//Apiコマンド
        commands.register(RSCCommands.class);//RSCコマンド
        commands.register(GachaponCommands.class);//328ガチャコマンド
        commands.register(LotteryCommands.class);//宝くじコマンド
        commands.register(EnchantmentCommands.class);//エンチャントコマンド
        commands.register(MonitorCommand.class);//モニターコマンド
        //commands.register(MoveCommands.class);//移動コマンド

        this.Log("コマンドの読み込みが完了しました");
    }

    public ProtocolManager getProtocolManager() {
        return protocolManager;
    }

    public TwitterManager getTwitterManager() {
        return twitter;
    }

    public MailManager getMailManager() {
        return mailManager;
    }

    public CommandShopManager getCommandShopManager() {
        return this.cmdshop;
    }

    public void scheduleAtTimestamp(Runnable runnable, long timestamp) {
        this.getServer().getScheduler().scheduleSyncDelayedTask(this, runnable, ((timestamp - System.currentTimeMillis()) / 1000) * 20L);
    }

    public synchronized ConnectionManager getConnectionManager() {
        return connectionManager;
    }

    /**
     * Debugを返します
     *
     * @return Debug
     * @see Debug
     */
    public Debug getDebug() {
        return this.debug;
    }

    public MituyaPermissionManager getMituyaPermissionManager() {
        return permissionManager;
    }

    public MobManager getMobManager() {
        return this.mobmanager;
    }

    public RedstoneChips getRedstoneChips() {
        return redstoneChips;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
        if (chatProvider != null) {
            chat = chatProvider.getProvider();
        }

        return (chat != null);
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }

    public JapaneseMessage getJapaneseMessage() {
        return this.japaneseMessage;
    }

    public PerformanceMonitor getPerformanceMonitor() {
        return this.performanceMonitor;
    }

    public JDCConnection getConnection() throws SQLException {
        return databaseConnectionManager.getConnection();
    }

    public DatabaseConnectionManager getDatabaseConnectionManager() {
        return this.databaseConnectionManager;
    }

    /**
     * Listenerを登録します
     *
     * @param listener
     */
    public void registerEvents(Listener listener) {
        this.getServer().getPluginManager().registerEvents(listener, this);
        this.Log("Listener : " + listener.getClass().getName() + " を登録しました");
    }

    public SignCommandManager getSignCommandManager() {
        return this.signCommandManager;
    }

    private SignElevatorManager getSignElevatorManager() {
        return this.elevatorManager;
    }

    public ChestShopManager getChestShopManager() {
        return this.chestShopManager;
    }

    public ItemChestManager getItemChestManager() {
        return this.itemChestManager;
    }

    public PackBookManager getPackBookManager() {
        return this.bookManager;
    }

    public MituyaLogManager getLogManager() {
        return this.logManager;
    }
}

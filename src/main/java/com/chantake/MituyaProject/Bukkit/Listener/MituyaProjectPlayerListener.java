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
package com.chantake.MituyaProject.Bukkit.Listener;

import com.chantake.MituyaProject.Bukkit.Event.MituyaPlayerSignClickEvent;
import com.chantake.MituyaProject.Bukkit.NoteBlockNote;
import com.chantake.MituyaProject.Exception.PlayerOfflineException;
import com.chantake.MituyaProject.Midi.MidiJingleSequencer;
import com.chantake.MituyaProject.MituyaProject;
import com.chantake.MituyaProject.Parameter.Parameter328;
import com.chantake.MituyaProject.Permissions.Rank;
import com.chantake.MituyaProject.Player.Chat.ChatManager;
import com.chantake.MituyaProject.Player.PlayerInstance;
import com.chantake.MituyaProject.Timer.BorderGuard;
import com.chantake.MituyaProject.Util.MySqlProcessing;
import com.chantake.MituyaProject.Util.Tools;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.NoteBlock;
import org.bukkit.block.Sign;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.player.*;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * プレーヤーリスナー
 *
 * @author chantake
 * @version 1.0.0
 */
public class MituyaProjectPlayerListener implements Listener {

    private static ChatColor color;
    private final MituyaProject plugin;
    private final Map<InetAddress, String> ips = new HashMap<>();

    /**
     * MituyaProjectを代入してインスタンスを生成します
     *
     * @param instance MituyaProject
     * @see MituyaProject
     */
    public MituyaProjectPlayerListener(MituyaProject instance) {
        plugin = instance;
    }

    public void onPlayerAnimation(PlayerAnimationEvent event) {
    }

    public void onEnchantItem(EnchantItemEvent event) {
    }

    /**
     * プレーヤーがテレポートした時によばれるメゾッド
     *
     * @param event PlayerTeleportEvent
     * @throws com.chantake.MituyaProject.Exception.PlayerOfflineException
     * @see PlayerTeleportEvent
     */
    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) throws PlayerOfflineException {
        World from = event.getFrom().getWorld();
        World to = event.getTo().getWorld();
        PlayerInstance ins = plugin.getInstanceManager().getInstance(event.getPlayer());
        //boder外
        if (!this.isBorder(event.getTo(), this.plugin.getWorldManager().getWorldData(to).getBorder())) {
            ins.sendAttention("ワールドのボーダーを超えているためテレポートできません");
            event.setCancelled(true);
            return;
        }
        if (!from.equals(to)) {
            if (to.equals(plugin.getWorldManager().getWorld("harvest"))) {
                ins.setHarvest();
            } else {
                ins.setLastworld(from);
            }
            ins.sendSuccess(from.getName() + " ワールド から、" + to.getName() + " ワールドに移動しました。");
        }
    }

    /**
     * プレーヤーがポータル（ネザーゲート）を利用した時によばれるメゾッド
     *
     * @param event PlayerPortalEvent
     * @throws com.chantake.MituyaProject.Exception.PlayerOfflineException
     * @see PlayerPortalEvent
     */
    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent event) throws PlayerOfflineException {
        PlayerInstance ins = plugin.getInstanceManager().getInstance(event.getPlayer());
        Player pr = event.getPlayer();
        if (event.getPlayer().getWorld().equals(plugin.getWorldManager().getWorld("skyland"))) {//スカイランドから
            event.setCancelled(true);
            pr.teleport(plugin.getWorldSpawn(ins.getLastworld()));
        } else if (event.getPlayer().getWorld().equals(plugin.getWorldManager().getWorld("world_nether")) && !this.getBorder(event.getTo())) {
            event.setCancelled(true);
            ins.sendMessage("エリア制限がかかっています。 エリア制限外に出ないところで作成、または利用してください。");
        } else if (event.getPlayer().getLocation().getY() >= 119 && event.getPlayer().getItemInHand().getType() == Material.FEATHER) {
            World world = plugin.getWorldManager().getWorld("skyland");
            ins.setLastworld(pr.getWorld());
            event.setCancelled(true);
            pr.teleport(plugin.getWorldSpawn(world));
            ins.sendMessage(ChatColor.AQUA + "スカイランドへようこそ");
        } else if (event.getFrom().getWorld().equals(this.plugin.getWorldManager().getWorld(Parameter328.SecondWorld))) {//セカンドワールドからネザーへ
            Location to = new Location(this.plugin.getWorldManager().getWorld("new_nether"), (event.getFrom().getX() * 1 / 8), event.getFrom().getY(), (event.getFrom().getZ() * 1 / 8), event.getFrom().getYaw(), event.getFrom().getPitch());
            event.setTo(event.getPortalTravelAgent().findOrCreate(to));
            ins.sendMessage(ChatColor.AQUA + "ニューネザーへようこそ");
        } else if (event.getFrom().getWorld().equals(this.plugin.getWorldManager().getWorld("new_nether"))) {
            Location to = new Location(this.plugin.getWorldManager().getWorld(Parameter328.SecondWorld), (event.getFrom().getX() * 8), event.getFrom().getY(), (event.getFrom().getZ() * 8), event.getFrom().getYaw(), event.getFrom().getPitch());
            if (!this.getBorder(to)) {
                ins.sendMessage("エリア制限がかかっています。 エリア制限外に出ないところで作成、または利用してください。");
                event.setCancelled(true);
                return;
            }
            event.setTo(event.getPortalTravelAgent().findOrCreate(to));
        } else if (event.getFrom().getWorld().equals(this.plugin.getWorldManager().getWorld("harvest"))) {//ハーベストからハーベストネザーへ
            Location to = new Location(this.plugin.getWorldManager().getWorld("harvest_nether"), (event.getFrom().getX() * 1 / 8), event.getFrom().getY(), (event.getFrom().getZ() * 1 / 8), event.getFrom().getYaw(), event.getFrom().getPitch());
            event.setTo(event.getPortalTravelAgent().findOrCreate(to));
            ins.sendMessage(ChatColor.AQUA + "ハーベストネザーへようこそ");
        } else if (event.getFrom().getWorld().equals(this.plugin.getWorldManager().getWorld("harvest_nether"))) {
            Location to = new Location(this.plugin.getWorldManager().getWorld("harvest"), (event.getFrom().getX() * 8), event.getFrom().getY(), (event.getFrom().getZ() * 8), event.getFrom().getYaw(), event.getFrom().getPitch());
            if (!this.getBorder(to)) {
                ins.sendMessage("エリア制限がかかっています。 エリア制限外に出ないところで作成、または利用してください。");
                event.setCancelled(true);
                return;
            }
            event.setTo(event.getPortalTravelAgent().findOrCreate(to));
        }
    }

    /**
     * プレーヤーがチャット（コマンド以外）した場合によばれるメゾッド
     *
     * @param event PlayerChatEvent
     * @throws com.chantake.MituyaProject.Exception.PlayerOfflineException
     * @see PlayerChatEvent
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) throws PlayerOfflineException {
        final Player players = event.getPlayer();//プレーヤー取得
        final PlayerInstance player = plugin.getInstanceManager().getInstance(players);//プレーヤーインスタンスを取得
        String mes = event.getMessage();//メッセージ取得
        //チャット制限
        if (!player.isAllowChat()) {
            player.sendAttention("30秒間チャットができません");
            event.setCancelled(true);
            return;
        }

        //自動切り替え
        this.PlayerLanguageJudgment(player, mes.charAt(0));

        //確認メッセージがあるかどうか
        if (player.isCheck()) {
            if (mes.equalsIgnoreCase("yes") || mes.equalsIgnoreCase("y")) {
                //インスタンスを実行
                player.executionCheckInstance();
            } else {
                player.cancelCheckInstance();
                //他のコマンドが残っている場合はそれを実行
                plugin.getSignCommandManager().executionCommandTaskFromCheckInstance(players, player);
            }
            event.setCancelled(true);
            return;
        }
        String[] message;
        if (mes.charAt(0) == '・') {
            message = event.getMessage().split("　");
        } else {
            message = event.getMessage().split(" ");
        }
        if (mes.charAt(0) == '・' && plugin.handleCommand(event.getPlayer(), message)) {
            event.setCancelled(true);
        } else if (player.getGobaku() && plugin.hasCommand(message[0].toLowerCase())) {//誤爆
            player.sendAttention("誤爆しました : " + mes);
            player.sendAttention("※ $を先頭に付けるとチャットとして表示することができます 例)$/home");
        } else {
            //メッセージを置換
            ChatManager.ChatProcesser(players, player, mes, plugin);
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String[] split = event.getMessage().split(" ");

        if (plugin.handleCommand(event.getPlayer(), split)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event) throws PlayerOfflineException, IOException, InvalidConfigurationException {
        final Player pr = event.getPlayer();
        final PlayerInstance ins = plugin.getInstanceManager().getInstance(pr);
        if (event.getAction() != Action.PHYSICAL) {

            if (this.plugin.getPackBookManager().openBook(pr, ins)) {
                /*
                 * PackBook
                 */
                event.setCancelled(true);
            } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                // ブロッククリック
                Block clickblock = event.getClickedBlock();

                //看板
                if (clickblock.getState() instanceof Sign) {
                    /*
                     * SignClick
                     */
                    MituyaPlayerSignClickEvent mituyaPlayerSignClickEvent = new MituyaPlayerSignClickEvent((Sign)clickblock.getState(), pr, ins, event.getAction() == Action.RIGHT_CLICK_BLOCK);
                    //CallSignEvent
                    this.plugin.getServer().getPluginManager().callEvent(mituyaPlayerSignClickEvent);
                    event.setCancelled(mituyaPlayerSignClickEvent.isCancel());
                }

                if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    /*
                     * 右クリック
                     */
                    if (pr.getItemInHand().getType() == Material.WOOD_SPADE) {
                        final StringBuilder stbr = new StringBuilder();
                        stbr.append("[").append(ChatColor.GREEN).append("Item").append(ChatColor.WHITE).append("] ItemId:").append(clickblock.getTypeId()).append(" ItemType:").append(clickblock.getData()).append(" ItemName:").append(clickblock.getState().getType().toString()).append(" pos x:").append(clickblock.getX()).append(" y:").append(clickblock.getY()).append(" z:").append(clickblock.getZ());
                        event.getPlayer().sendMessage(stbr.toString());
                    } else if (clickblock.getState().getType() == Material.NOTE_BLOCK) {
                        if (!plugin.canBuild(pr, clickblock)) {
                            event.setCancelled(true);
                            ins.sendAttention("この音ブロックは保護されています");
                        }
                    }
                } else {
                    /*
                     * 左クリック
                     */
                }
                //ブロック情報表示
                if (clickblock.getState().getType() == Material.NOTE_BLOCK && ins.getShowNote()) {
                    NoteBlock nb = (NoteBlock)clickblock.getState();
                    ins.sendInfo(ChatColor.GREEN + "NoteBlock", NoteBlockNote.GetNote(nb.getNote().getId(), event.getAction() == Action.RIGHT_CLICK_BLOCK));
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity vehicle = player.getVehicle();
        Entity target = event.getRightClicked();

        if (player.getItemInHand().getType() == Material.SADDLE && plugin.permission.has(player, "mituya.ride")) {
            target.setPassenger(player);
        }
    }

    /**
     * ログインが確定し（Bukkit内部で）ゲーム内に入ったときによばれるメゾッド
     *
     * @param event PlayerJoinEvent
     * @throws com.chantake.MituyaProject.Exception.PlayerOfflineException
     * @see PlayerJoinEvent
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws PlayerOfflineException {
        //Player
        final Player pr = event.getPlayer();
        //PlayerInstance取得（328プラグインのプレーヤー情報を保持してるインスタンス）
        final PlayerInstance ins = plugin.getInstanceManager().getInstance(pr);
        ins.Login(pr);// ログイン時ロード
        //ニックネーム設定
        pr.setCustomName(ins.getNickName());
        //joinメッセージ
        if (ins.getLoginBMessage().length() > 0) {
            event.setJoinMessage(ChatColor.YELLOW + ins.getName() + " 様がログインしました " + ChatColor.WHITE + "<" + ins.getLoginBMessage());
        } else {
            event.setJoinMessage(ChatColor.YELLOW + ins.getName() + " 様がログインしました ");
        }
        //Opだった場合はop権限はく奪（乗っ取り防止）
        if (pr.isOp()) {
            pr.setOp(false);
        }
        //強制サバイバルモード
        if (pr.getGameMode() == GameMode.CREATIVE) {
            pr.setGameMode(GameMode.SURVIVAL);
        }
        //メンテナンスモード
        if (Parameter328.Mentenance && !ins.hasPermission(Rank.GM)) {
            //event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Sorry. Server is Mentenance now." + Tools.BR + "サーバメンテナンスです。ごめいわくをおかけします。");
            pr.kickPlayer("http://328mss.com/ サーバメンテナンスです。ご迷惑をおかけいたします。");
            return;
        }
        //ボーダーから外れている場合はロケーションをエリア内に戻す
        SetBorder(pr, ins);
        //ログインメッセージ
        this.LoginMessage(pr, ins);
        //ログイン時イントロ 壊すと止まるため一時停止
        this.sendIntro(pr);
    }

    /**
     * プレーヤーログイン時に呼ばれるイベント
     *
     * @param event PlayerLoginEvent
     * @see PlayerLoginEvent
     */
    @EventHandler
    public void PlayerLoginEvent(PlayerLoginEvent event) {
        //IpAddress
        InetAddress address = event.getAddress();
        //location
        String lo;
        //取得済み
        if (this.ips.containsKey(address)) {
            lo = this.ips.get(address);
        } else {//未取得
            //getで取得
            StringBuilder get = Tools.get("http://geoip.wtanaka.com/cc" + event.getAddress().toString());
            //取得できない場合はnullを代入
            if (get == null) {
                lo = null;
            } else {
                lo = get.toString();
                //高速化のためローカルに保存
                this.ips.put(address, lo);
            }
        }

        if (lo == null) {
            return;
        }

        switch (lo) {
            case "jp"://Japan
                break;
            case "zz"://local
                break;
            case "kr"://Korea
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "403 The access from ip is forbidden.");
                break;
            case "cn"://China
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "403 The access from ip is forbidden.");
                break;
            default://Other
                this.plugin.broadcastGMMessage("[" + ChatColor.RED + "警告" + ChatColor.WHITE + "] 海外からのアクセスです From:" + lo + " IP:" + address.toString().substring(1));
                plugin.Log("Access From " + lo + " ip:" + address.toString());
                break;
        }

    }

    public void onPlayerMove(PlayerMoveEvent event) {
        //AutoKick.neglect.put(event.getPlayer(), 1);
    }

    /**
     * プレーヤーがログアウトした時によばれるメゾッド
     *
     * @param event PlayerQuitEvent
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        //プレーヤーセーブ
        Player player = event.getPlayer();
        PlayerInstance instance = plugin.getInstanceManager().getInstance(player);
        instance.SaveQuit();
        //確認機能
        instance.removeCheckInstance();
        //コマンド削除
        instance.removeCommandTasks();
        String name;
        if (instance.isNickName()) {
            name = instance.getNickName();
        } else {
            name = instance.getName();
        }
        event.setQuitMessage(ChatColor.DARK_GRAY + name + " 様がログアウトしました");
        //Midiストップ
        plugin.jingleNoteManager.stop(player.getName());
        //Notificationメッセージ
        //plugin.broadcastNotificationMessage(event.getPlayer().getName(), "has quit the game.", Parameter328.Icon_Quit);
        if (plugin.getServer().getOnlinePlayers().size() == 0) {
            System.gc();//0人になったら好機とみてFullGCを走らせてみる
        }
    }

    /**
     * プレーヤーがリスポーンした時によばれるメゾッド
     *
     * @param event PlayerRespawnEvent
     */
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        final Player players = event.getPlayer();
        final PlayerInstance player = plugin.getInstanceManager().getInstance(players);
        player.setPlayer(players);// Player更新
        World wd = player.getLastworld();
        event.setRespawnLocation(player.getSpawn(wd));
    }

    /**
     * ime切り替え
     *
     * @param ins
     * @param c ins
     * @throws com.chantake.MituyaProject.Exception.PlayerOfflineException
     */
    public void PlayerLanguageJudgment(PlayerInstance ins, char c) throws PlayerOfflineException {
        if (!((c <= '\u007e') || // 英数字
                (c == '\u00a5') || // \記号
                (c == '\u203e') || // ~記号
                (c >= '\uff61' && c <= '\uff9f') // 半角カナ
                ) && ins.getIME()) {
            //2バイトの場合imeを有効化
            ins.setIME(false);
            ins.getPlayer().sendMessage(ChatColor.YELLOW + "自動変換を無効にしました " + ChatColor.RED + "※/ime on で自動変換を有効にできます");
        }
    }

    public boolean getBorder(Location lo) {
        final int borderline = this.getBorder(lo.getWorld());
        return Math.abs(lo.getX()) <= borderline && Math.abs(lo.getZ()) <= borderline;
    }

    public int getBorder(World wd) {
        return this.plugin.getWorldManager().getWorldData(wd).getBorder();
    }

    public void SetBorder(Player pr, PlayerInstance ins) {
        if (ins.getLastlogin() != null && ins.getLastlogin().before(Parameter328.delete_world) && pr.getWorld().getName().equals("world")) {
            pr.teleport(plugin.getWorldSpawn(pr.getWorld()));
        }
        final int borderline = this.getBorder(pr.getWorld());
        final Location lo = pr.getLocation();
        int x = (int)Math.abs(lo.getX());
        int z = (int)Math.abs(lo.getZ());

        if ((x > borderline || z > borderline) && lo.getWorld().getName().equals("world")) {
            pr.teleport(plugin.getWorldSpawn(pr.getWorld()));
            pr.sendMessage(ChatColor.RED + Parameter328.border);
            return;
        }
        if (Math.abs(lo.getX()) > borderline) {
            lo.setY(3);
            if (lo.getX() > 0) {
                lo.setX(borderline - 3);
            } else {
                lo.setX(borderline + 3);
            }
            pr.teleport(lo);
            pr.sendMessage(ChatColor.RED + Parameter328.border);
        } else if (Math.abs(lo.getZ()) > borderline) {
            lo.setY(3);
            if (lo.getZ() > 0) {
                lo.setZ(borderline - 3);
            } else {
                lo.setZ(borderline + 3);
            }
            pr.teleport(lo);
            pr.sendMessage(ChatColor.RED + Parameter328.border);
        }
        BorderGuard.loca.put(pr, pr.getLocation());
    }

    /**
     * イントロを再生します
     *
     * @param pr
     */
    private void sendIntro(Player pr) {
        try {
            MidiJingleSequencer sequencer = new MidiJingleSequencer(new File(plugin.getDataFolder(), "intro.mid"), false);
            plugin.jingleNoteManager.play(pr.getName(), sequencer);
        }
        catch (IOException | InvalidMidiDataException | MidiUnavailableException e) {
        }
    }

    /**
     * ログインメッセージを表示します
     *
     * @param pr
     * @param ins
     */
    private void LoginMessage(Player pr, PlayerInstance ins) throws PlayerOfflineException {
        //Rei's MiniMap用オプション拡張コード送信
        ins.sendMessage("§0§0§1§2§3§4§5§6§7§e§f");
        //ログインメッセージ
        ins.sendMessage(Parameter328.LoginMessage);
        StringBuilder stbl = new StringBuilder();
        stbl.append(ChatColor.GREEN).append("オンラインプレーヤー (").append(ChatColor.RED).append(plugin.getServer().getOnlinePlayers().size()).append(ChatColor.GREEN).append(")  ").append(ChatColor.AQUA).append("みつやプラグインバージョン ").append(ChatColor.LIGHT_PURPLE).append(plugin.getDescription().getVersion());
        pr.sendMessage(stbl.toString());
        stbl.delete(0, stbl.length());
        stbl.append(ChatColor.YELLOW).append("ジャックポット ").append(ChatColor.GOLD).append(Parameter328.jackpot).append(ChatColor.GREEN).append(" Mine.");
        pr.sendMessage(stbl.toString());
        int unreadMailAmount = plugin.getMailManager().getUnreadMailAmount(pr);
        if (unreadMailAmount == 0) {
            ins.sendInfo(ChatColor.GREEN + "Mail", ChatColor.AQUA + "未読メールはありません (/mail)");
        } else {
            ins.sendInfo(ChatColor.GREEN + "Mail", ChatColor.RED + "" + unreadMailAmount + " 通の未読メールがあります (/mail unread)");
        }
        //メンテナンスの場合
        if (Parameter328.Mentenance) {
            ins.sendAttention("現在メンテナンスモードが有効になってます！");
        }
        //Notificationメッセージ
        //plugin.broadcastNotificationMessage(lm, ins.getLoginBMessage(), Parameter328.Icon_Login);
        //サーバー同時オンライン人数更新メッセージ
        if (plugin.getServer().getOnlinePlayers().size() > Parameter328.maxonline) {
            Parameter328.maxonline = plugin.getServer().getOnlinePlayers().size();
            MySqlProcessing.SaveMaxOnline();
            StringBuilder stbr = new StringBuilder();
            stbr.append("[").append(ChatColor.LIGHT_PURPLE).append("Server").append(ChatColor.WHITE).append("]").append(ChatColor.YELLOW).append("The maximum, online number(").append(ChatColor.AQUA).append(Parameter328.maxonline).append(ChatColor.YELLOW).append(") was achieved. Thank you.");
            plugin.broadcastMessage(stbr.toString());
        }
    }

    public boolean getFlyFromSkyLand(Location lo) {
        return lo.getWorld().equals(plugin.getWorldManager().getWorld("skyland")) && lo.getY() <= 0;
    }

    public boolean isBorder(Location lo, int border) {
        return Math.abs(lo.getX()) <= border && Math.abs(lo.getZ()) <= border;
    }
}

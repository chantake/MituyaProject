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

import com.chantake.MituyaProject.Bukkit.Event.MituyaPlayerHideEvent;
import com.chantake.MituyaProject.MituyaProject;
import com.chantake.MituyaProject.Permissions.Rank;
import java.util.ArrayList;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * HidePlayerManager
 *
 * @author chantake
 */
public class HideManager {

    private MituyaProject plugin;
    protected ArrayList<String> hideplayers = new ArrayList<>();
    protected ArrayList<Player> hide_onlineplayers = new ArrayList<>();
    private final HideListener listener = new HideListener(this);

    public HideManager() {
    }

    /**
     * 初期化
     *
     * @param plugin
     */
    public void init(MituyaProject plugin) {
        this.plugin = plugin;
        //Listener登録
        this.plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    }

    /**
     * Hideするプレーヤーを追加
     *
     * @param player
     */
    public void addHidePlayer(Player player) {
        MituyaPlayerHideEvent mituyaPlayerHideEvent = new MituyaPlayerHideEvent(player, true);
        this.plugin.getServer().getPluginManager().callEvent(mituyaPlayerHideEvent);
        if (mituyaPlayerHideEvent.isHide()) {
            //追加
            this.hideplayers.add(player.getName());
            this.hide_onlineplayers.add(player);
            //反映
            this.HideOnlinePlayers(player);
        }
    }

    /**
     * プレーヤーが対象にいる場合にhide対象から削除します
     *
     * @param player プレーヤー名
     */
    public void removeHidePlayer(Player player) {
        MituyaPlayerHideEvent mituyaPlayerHideEvent = new MituyaPlayerHideEvent(player, false);
        this.plugin.getServer().getPluginManager().callEvent(mituyaPlayerHideEvent);
        this.hideplayers.remove(player.getName());
        this.hide_onlineplayers.remove(player);
        this.showOnlinePlayers(player);
    }

    /**
     * Hide対象のプレーヤーを取得します
     *
     * @return
     */
    public Iterable<String> getHidePlayers() {
        return this.hideplayers;
    }

    /**
     * オンラインのhideプレーヤーを取得します
     *
     * @return online hide Player
     */
    public Iterable<Player> getHideOnlinePlayers() {
        return this.hide_onlineplayers;
    }

    /**
     * 指定したPlayerがHide対象かどうかを取得します
     *
     * @param player Player
     * @return hide対象の場合はtrue
     */
    public boolean isHidePlayer(Player player) {
        return this.hideplayers.contains(player.getName());
    }

    /**
     * オンラインユーザー全員からPlayerをhideします
     *
     * @param player hideするPlayer
     */
    private void HideOnlinePlayers(Player player) {
        for (Player pr : this.plugin.getServer().getOnlinePlayers()) {
            //見えないようにする
            pr.hidePlayer(player);
        }
        this.plugin.broadcastDebugMessage("HidePlayer:" + player.getName());
    }

    /**
     * オンラインユーザー全員からPlayerを見えるようにする
     *
     * @param player hideするPlayer
     */
    private void showOnlinePlayers(Player player) {
        for (Player pr : this.plugin.getServer().getOnlinePlayers()) {
            //show
            pr.showPlayer(player);
        }
    }

    /**
     * HideListener
     */
    public class HideListener implements Listener {

        private final HideManager manager;

        public HideListener(HideManager manager) {
            this.manager = manager;
        }

        @EventHandler
        public void onPlayerQuit(PlayerQuitEvent event) {
            Player player = event.getPlayer();
            //もしhide対象だった場合
            if (this.manager.isHidePlayer(player)) {
                //オンラインプレーヤーリストから除外
                this.manager.hide_onlineplayers.remove(player);
            }
        }

        /**
         * ログイン時に呼ばれるメソッド
         *
         * @param event
         */
        @EventHandler(priority = EventPriority.HIGH)
        public void onPlayerJoin(PlayerJoinEvent event) {
            Player player = event.getPlayer();
            //もしhide対象だった場合
            if (this.manager.isHidePlayer(player)) {
                //call
                this.manager.plugin.getServer().getPluginManager().callEvent(new MituyaPlayerHideEvent(player, true));
                //オンラインプレーヤー追加
                this.manager.hide_onlineplayers.add(player);
                //反映
                this.manager.HideOnlinePlayers(player);
            }
            //hideする
            for (Player pr : this.manager.plugin.getServer().getOnlinePlayers()) {
                //GM未満は見えないように
                if (!this.manager.plugin.getInstanceManager().getInstance(pr).hasPermission(Rank.Moderator)) {
                    for (Player hp : this.manager.hide_onlineplayers) {
                        pr.hidePlayer(hp);
                    }
                }
            }
        }
    }
}

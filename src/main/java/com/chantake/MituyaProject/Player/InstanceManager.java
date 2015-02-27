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

import com.chantake.MituyaProject.MituyaProject;
import com.chantake.MituyaProject.Util.MySqlProcessing;
import com.chantake.MituyaProject.Util.UUIDUtils;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * PlayerInstanceを検索して呼び出すクラスです
 *
 * @author chantake
 */
public class InstanceManager {

    final private ConcurrentHashMap<String, PlayerInstance> players = new ConcurrentHashMap<>();
    private final MituyaProject plugin;
    private final DisplayNameManager displayNameManager = new DisplayNameManager(this);
    private final HideManager hideManager = new HideManager();

    public InstanceManager(MituyaProject plugin) {
        this.plugin = plugin;
    }

    /**
     * 初期化
     */
    public void init() {
        this.hideManager.init(plugin);
    }

    /**
     * インスタンスを追加
     *
     * @param name プレーヤー名
     * @return
     */
    private synchronized PlayerInstance addInstance(String name) {
        PlayerInstance ins = new PlayerInstance(name, plugin);
        this.players.put(name, ins);
        return ins;
    }

    /**
     * PlayerをKeyにし、PlayerInstanceを返します。　Playerがnullの場合、オフラインの場合は使わないで下さい。
     *
     * @param player
     * @return PlayerInstance ログイン時のみ（Player!=null)時のみ使用してください。
     * @see org.bukkit.entity.Player
     * @see com.chantake.MituyaProject.Player.PlayerInstance
     * @author chantake
     * @since 2013/02/06
     */
    public PlayerInstance getInstance(Player player) {
        String name = player.getName();
        if (!this.players.containsKey(name)) {// Mapにプレーヤーが見つからないとき
            // 新規インスタンス作成
            return this.addInstance(name);
        } else {
            return this.getInstance(name);
        }
    }

    /**
     * UUIDをKeyにし、PlayerInstanceを返します。　Playerがnullの場合、オフラインの場合は使わないで下さい。
     *
     * @param uuid UUID
     * @return PlayerInstance ログイン時のみ（Player!=null)時のみ使用してください。
     * @see org.bukkit.entity.Player
     * @see com.chantake.MituyaProject.Player.PlayerInstance
     * @author fumitti
     * @since 2014/05/15
     */
    public PlayerInstance getInstance(UUID uuid) {
        String name = UUIDUtils.getPlayerName(uuid);
        if (!this.players.containsKey(name)) {// Mapにプレーヤーが見つからないとき
            // 新規インスタンス作成
            return this.addInstance(name);
        } else {
            return this.getInstance(name);
        }
    }

    /**
     * インスタンスを取得します
     *
     * @param name PlayerID 完全一致
     * @return 名前が存在する場合はPlayerInstanceを返します。ロードしてない場合はロードします。存在しない場合はnullが返ります。
     */
    public PlayerInstance getInstance(String name) {
        //ロードしてない場合
        if (this.players.containsKey(name)) {
            return this.players.get(name);
        } else {
            String playerSerch = MySqlProcessing.getPlayer(name);
            if (playerSerch != null) {//見つかった場合は返す
                return this.addOfflineInstance(playerSerch);
            }
        }
        return null;
    }

    /**
     * PlayerInstanceをプレーヤー名(String)で検索します。。
     *
     * @param name プレーヤー名、前方のみでもok
     * @return keyが一致した場合 PlayerInstance を返し、見つからなければSQLでも検索し、それでも見つからない場合はnullを返します。
     * @see org.bukkit.entity.Player
     * @see com.chantake.MituyaProject.Player.PlayerInstance
     * @author chantake
     */
    public PlayerInstance matchSingleInstance(String name) {
        /*
         * オンラインプレーヤーから検索
         */
        List<PlayerInstance> matchPlayer = this.matchInstance(name);
        //見つかった場合は返す
        if (!matchPlayer.isEmpty()) {//1件以上引っかかった場合
            //PlayerInstance
            return matchPlayer.get(0);
        }

        //指定
        if (name.charAt(0) == '@' && name.length() >= 2) {
            /*
             * 完全一致
             */
            String playerSerch = MySqlProcessing.getPlayer(name.substring(1));
            if (playerSerch != null) {//見つかった場合は返す
                return this.addOfflineInstance(playerSerch);
            }
        } else if (name.charAt(0) == '*' && name.length() >= 2) {
            /*
             * 前方一致
             */
            String playerSerch = MySqlProcessing.getPlayerSerch(name.substring(1));
            if (playerSerch != null) {//見つかった場合は返す
                return this.addOfflineInstance(playerSerch);
            }
        } else {
            /*
             * キャッシュから検索
             */
            //完全一致
            if (this.players.containsKey(name)) {
                return this.players.get(name);
            } else {
                //ニックネームから検索（完全一致）
                for (PlayerInstance ins : this.players.values()) {
                    //ニックネーム空の場合は飛ばす
                    if (ins.getNickName() == null) {
                        continue;
                    }
                    //完全一致
                    if (ins.getNickName().equalsIgnoreCase(name)) {
                        matchPlayer.clear();
                        matchPlayer.add(ins);
                        break;
                    }
                    //先頭一致
                    if (ins.getRawName().startsWith(name) || ins.getNickName().startsWith(name)) {
                        matchPlayer.add(ins);
                    }
                }
            }
            //キャッシュで見つかった場合
            if (!matchPlayer.isEmpty()) {//1件以上引っかかった場合
                //PlayerInstance
                return matchPlayer.get(0);
            }

            /**
             * DBから検索
             */
            String playerSerch = MySqlProcessing.matchSinglePlayer(name);
            if (playerSerch == null) {
                //先頭一致
                playerSerch = MySqlProcessing.getPlayerPrefixSerch(name);
                if (playerSerch == null) {
                    //部分一致
                    playerSerch = MySqlProcessing.getPlayerSerch(name);
                }
            }
            if (playerSerch == null) {
                return null;
            }
            //新規インスタンス
            return this.addOfflineInstance(playerSerch);
        }
        return null;
    }

    /**
     * オフラインプレーヤーを追加します
     *
     * @param name
     * @return
     */
    private PlayerInstance addOfflineInstance(String name) {
        //新規インスタンス
        PlayerInstance ins = this.addInstance(name);
        //オフラインロード
        ins.Offline();
        return ins;
    }

    /**
     * オンラインプレーヤーを検索します ※完全一致が優先されます
     *
     * @param name 検索する名前
     * @return 見つかった場合は
     */
    public List<PlayerInstance> matchInstance(String name) {
        List<PlayerInstance> matchedPlayers = new ArrayList<>();
        /*
         * 完全一致が優先されます
         */
        if (name.charAt(0) == '@' && name.length() >= 2) {
            /*
             * 完全一致
             */
            name = name.substring(1);
            for (Player iterPlayer : this.plugin.getServer().getOnlinePlayers()) {
                String iterPlayerName = iterPlayer.getName();
                PlayerInstance ins = this.players.get(iterPlayerName);

                //IDもしくはニックネームが完全一致した場合
                if (name.equalsIgnoreCase(iterPlayerName) || name.equalsIgnoreCase(ins.getNickName())) {
                    matchedPlayers.add(ins);
                    break;
                }
            }
        } else if (name.charAt(0) == '*' && name.length() >= 2) {
            /*
             * 部分一致
             */
            name = name.substring(1);
            for (Player iterPlayer : this.plugin.getServer().getOnlinePlayers()) {
                String iterPlayerName = iterPlayer.getName();
                PlayerInstance ins = this.players.get(iterPlayerName);
                String nickname = ins.getNickName();

                //IDもしくはニックネームが完全一致した場合
                if (name.equalsIgnoreCase(iterPlayerName) || name.equalsIgnoreCase(nickname)) {
                    matchedPlayers.clear();
                    matchedPlayers.add(ins);
                    break;
                }
                String lowername = name.toLowerCase();
                //検索文字が含まれる場合はlistに追加
                if (iterPlayerName.toLowerCase().contains(lowername) || nickname.toLowerCase().contains(lowername)) {
                    // Partial match
                    matchedPlayers.add(ins);
                }
            }
        } else {
            /*
             * 前方一致
             */
            for (Player iterPlayer : this.plugin.getServer().getOnlinePlayers()) {
                String iterPlayerName = iterPlayer.getName();
                PlayerInstance ins = this.players.get(iterPlayerName);
                String nickname = ins.getNickName();

                //IDもしくはニックネームが完全一致した場合
                if (name.equalsIgnoreCase(iterPlayerName) || name.equalsIgnoreCase(nickname)) {
                    matchedPlayers.clear();
                    matchedPlayers.add(ins);
                    break;
                }
                String lowername = name.toLowerCase();
                //前方一致した場合は追加
                if (iterPlayerName.toLowerCase().startsWith(lowername) || nickname.toLowerCase().startsWith(lowername)) {
                    // Partial match
                    matchedPlayers.add(ins);
                }
            }
        }
        return matchedPlayers;
    }

    /**
     * オンラインプレーヤーを検索します
     *
     * @param name 検索する名前
     * @return
     */
    public List<PlayerInstance> matchInstanceName(String name) {
        List<PlayerInstance> matchedPlayers = new ArrayList<>();
        if (name.charAt(0) == '@' && name.length() >= 2) {
            /*
             * 完全一致
             */
            name = name.substring(1);
            for (Player iterPlayer : this.plugin.getServer().getOnlinePlayers()) {
                String iterPlayerName = iterPlayer.getName();
                PlayerInstance ins = this.players.get(iterPlayerName);

                //IDもしくはニックネームが完全一致した場合
                if (name.equalsIgnoreCase(iterPlayerName) || name.equalsIgnoreCase(ins.getNickName())) {
                    matchedPlayers.add(ins);
                    break;
                }
            }
        } else if (name.charAt(0) == '*' && name.length() >= 2) {
            /*
             * 部分一致
             */
            name = name.substring(1);
            for (Player iterPlayer : this.plugin.getServer().getOnlinePlayers()) {
                String iterPlayerName = iterPlayer.getName();
                PlayerInstance ins = this.players.get(iterPlayerName);

                String lowername = name.toLowerCase();
                //検索文字が含まれる場合はlistに追加
                if (iterPlayerName.toLowerCase().contains(lowername) || ins.getNickName().toLowerCase().contains(lowername)) {
                    // Partial match
                    matchedPlayers.add(ins);
                }
            }
        } else {
            /*
             * 前方一致
             */
            for (Player iterPlayer : this.plugin.getServer().getOnlinePlayers()) {
                String iterPlayerName = iterPlayer.getName();
                PlayerInstance ins = this.players.get(iterPlayerName);

                String lowername = name.toLowerCase();
                //前方一致した場合は追加
                if (iterPlayerName.toLowerCase().startsWith(lowername) || ins.getNickName().toLowerCase().startsWith(lowername)) {
                    // Partial match
                    matchedPlayers.add(ins);
                }
            }
        }
        return matchedPlayers;
    }

    public synchronized Map<String, PlayerInstance> getPlayers() {
        return this.players;
    }

    public DisplayNameManager getDisplayNameManager() {
        return this.displayNameManager;
    }

    public HideManager getHideManager() {
        return hideManager;
    }
}

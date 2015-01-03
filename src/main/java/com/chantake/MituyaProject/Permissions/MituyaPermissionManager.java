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
package com.chantake.MituyaProject.Permissions;

import com.chantake.MituyaProject.Bukkit.Event.MituyaRankChangeEvent;
import com.chantake.MituyaProject.MituyaManager;
import com.chantake.MituyaProject.MituyaProject;
import com.chantake.MituyaProject.Player.PlayerInstance;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

/**
 *
 * @author chantake
 */
public class MituyaPermissionManager extends MituyaManager {

    private ru.tehkode.permissions.PermissionManager pem = null;
    private final PermissionExListener listener = new PermissionExListener(this);

    public MituyaPermissionManager(MituyaProject plugin) {
        super(plugin);
    }

    @Override
    final public void init() {
        this.setupPermissionsEx();
    }

    /**
     * PermissionExを設定
     */
    private void setupPermissionsEx() {
        if (this.getPlugin().getServer().getPluginManager().isPluginEnabled("PermissionsEx")) {
            pem = PermissionsEx.getPermissionManager();
            this.getPlugin().registerEvents(listener);
            this.getPlugin().Log("PermissionsExが見つかりました");
        } else {
            this.getPlugin().ErrLog("PermissionsExが見つかりません");
        }
    }

    /**
     * Rankを変更する
     *
     * @param player PlayerもしくはOfflinePlayer
     * @param ins PlayerInstance
     * @param rank Rank
     * @return
     */
    public boolean setRank(Object player, PlayerInstance ins, Rank rank) {
        //イベント
        MituyaRankChangeEvent rankChangeEvent = new MituyaRankChangeEvent((Player)player, ins, rank, rank);
        //イベントをcallする
        getPlugin().getServer().getPluginManager().callEvent(rankChangeEvent);
        //キャンセルの場合はランクを変えずにreturnする
        if (rankChangeEvent.isCancel()) {
            return false;
        }
        //ランク変更
        ins.setRank(rankChangeEvent.getNewRank());
        ins.setDisplayName();
        ins.SaveCharToDB(true);
        return true;
    }

    /**
     * Vault MituyaPermissionManager
     *
     * @return ru.tehkode.permissions.MituyaPermissionManager
     * @see ru.tehkode.permissions.MituyaPermissionManager
     */
    public Permission getPermission() {
        return getPlugin().permission;
    }

    public PermissionManager getPermissionsEx() {
        return this.pem;
    }
}

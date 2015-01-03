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
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import ru.tehkode.permissions.PermissionUser;

/**
 *
 * @author chantake
 */
public class PermissionExListener implements Listener {

    private final MituyaPermissionManager manager;

    public PermissionExListener(MituyaPermissionManager manager) {
        this.manager = manager;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void MituyaRankChangeEvent(MituyaRankChangeEvent event) {
        if (!event.isCancel()) {
            ru.tehkode.permissions.PermissionManager pex = this.manager.getPermissionsEx();
            PermissionUser user = pex.getUser(event.getPlayerInstance().getRawName());
            String[] groups = {event.getNewRank().name()};
            user.setGroups(groups);
        }
    }
}

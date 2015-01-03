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
package com.chantake.MituyaProject.Api;

import com.chantake.MituyaProject.Player.PlayerInstance;
import org.apache.mina.core.session.IoSession;

/**
 *
 * @author chantake
 */
public class SessionData {

    private final ApiData api;
    private PlayerInstance instance = null;
    private final IoSession session;

    public SessionData(ApiData api, IoSession session) {
        this.api = api;
        this.session = session;
    }

    public ApiData getApiData() {
        return api;
    }

    public IoSession getSession() {
        return this.session;
    }

    public void sendSession(String message) {
        this.getSession().write(message);
    }

    public void sendSession(DataConText dataConText) {
        this.getSession().write(dataConText.toString());
    }

    public void setInstance(PlayerInstance instance) {
        this.instance = instance;
    }

    public PlayerInstance getInstance() {
        return instance;
    }

}

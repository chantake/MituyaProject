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

import com.chantake.MituyaProject.Exception.ApiException;
import com.chantake.MituyaProject.Exception.MituyaProjectException;
import com.chantake.MituyaProject.MituyaProject;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author chantake
 */
public class ServerHandler extends IoHandlerAdapter {

    private final MituyaProject plugin;
    private final ConnectionManager connectionManager;

    public ServerHandler(MituyaProject plugin, ConnectionManager connectionManager) {
        this.plugin = plugin;
        this.connectionManager = connectionManager;
    }

    public ConnectionManager getConnectionManager() {
        return connectionManager;
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        plugin.Log("セッション受付開始");
        session.write("Hello");
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        Object json = JSONValue.parse(message.toString());
        if (json instanceof JSONObject) { //jsonかどうか判定
            SessionData sessionData = this.getConnectionManager().getSessionData(session);
            if (sessionData != null) {//Api認証済み
                try {
                    DataProcessor.DataProcessor(plugin, this, sessionData, new DataConText(json));
                }
                catch (ApiException ex) {
                    this.sendErrMessage(session, ex.getErr(), ex.getMessage());
                } catch (MituyaProjectException ex) {
                    this.sendErrMessage(session, Err.Other, ex.getMessage());
                }
            } else {
                try {
                    DataProcessor.apiActivation(plugin, this, session, (JSONObject)json);
                }
                catch (ApiException ex) {
                    this.sendErrMessage(session, ex.getErr(), ex.getMessage());
                }
            }
        } else {
            //Jsonじゃないのでエラーを返す
            this.sendErrMessage(session, Err.NotJson, null);
        }
    }

    public void sendErrMessage(IoSession session, Err errcode, String message) {
        if (message == null) {
            message = "";
        }
        session.write("Err:" + errcode.getCode() + " " + errcode.toString() + " " + message);
    }

    /**
     * エラーコード
     */
    public enum Err {

        NotJson(100),
        NotActivate(200),
        ApiActivationErr(300),
        ActivationErr(310),
        Other(500),
        CommandErr(600),
        ChatErr(700);

        private final int code;

        private Err(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }
}

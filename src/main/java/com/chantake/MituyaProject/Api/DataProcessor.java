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
import com.chantake.MituyaProject.Exception.PlayerOfflineException;
import com.chantake.MituyaProject.MituyaProject;
import com.chantake.MituyaProject.Player.PlayerInstance;
import com.chantake.MituyaProject.Tool.Encrypter;
import com.chantake.MituyaProject.Tool.Tools;
import org.apache.mina.core.session.IoSession;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

/**
 *
 * @author chantake-mac
 */
public class DataProcessor {

    public static void DataProcessor(MituyaProject plugin, ServerHandler handler, SessionData session, DataConText data) throws MituyaProjectException {
        if (data.getHead().equals("act")) {
            String id = null, pass = null;
            try {
                id = (String)data.getObject("id");
                pass = (String)data.getObject("pass");
            }
            catch (Exception ex) {
                throw new ApiException(ServerHandler.Err.ActivationErr, "idとpassが存在しないか、キャストに失敗しました");
            }
            if (id == null && pass == null) {
                throw new ApiException(ServerHandler.Err.ActivationErr, "idとパスワードが取得できません");
            }
            System.out.println("id:" + id + " pass:" + pass);
            PlayerInstance ins = plugin.getInstanceManager().getInstance(id);
            if (ins == null) {
                throw new ApiException(ServerHandler.Err.ActivationErr, "プレーヤー(" + id + ")が見つかりません");
            }
            if (!ins.isPass()) {
                throw new ApiException(ServerHandler.Err.ActivationErr, "パスワードを設定していません");
            }
            if (!ins.getPass().equals(Encrypter.getHash(pass, Encrypter.Algorithm.SHA512))) {
                throw new ApiException(ServerHandler.Err.ActivationErr, "パスワードが間違っています");
            }
            session.setInstance(ins);
            session.sendSession("認証完了");
        } else if (data.getHead().equalsIgnoreCase("rawmessage")) {
            String stringData = data.getStringData();
            plugin.broadcastMessage(Tools.ChangeText(stringData, plugin, null));
        } else if (session.getInstance() != null) {
            PlayerProcessor(plugin, handler, session, data);
        } else {
            throw new ApiException(ServerHandler.Err.Other, "無効なデータです");
        }
    }

    public static void PlayerProcessor(MituyaProject plugin, ServerHandler handl, SessionData session, DataConText data) throws MituyaProjectException {
        PlayerInstance ins = session.getInstance();
        Player player = plugin.getServer().getPlayer(ins.getName());
        if (player == null || !player.isOnline()) {
            throw new PlayerOfflineException("オフラインです");
        }
        switch (data.getHead()) {
            case "chat": {
                String dt = data.getStringData();
                if (dt == null) {
                    throw new ApiException(ServerHandler.Err.ChatErr, "チャットが取得できません");
                }
                player.chat(dt);
                session.sendSession("チャットを送信しました");
                break;
            }
            case "send": {
                String dt = data.getStringData();
                if (dt == null) {
                    throw new ApiException(ServerHandler.Err.ChatErr, "データが取得できません");
                }
                player.sendMessage(Tools.ChangeText(dt, plugin, ins));
                session.sendSession("メッセージを送りました");
                break;
            }
        }
    }

    public static void apiActivation(MituyaProject plugin, ServerHandler handler, IoSession session, JSONObject jsono) throws ApiException {
        if (!jsono.containsKey("tokenKey") && !jsono.containsKey("tokenSecret")) {
            throw new ApiException(ServerHandler.Err.NotActivate, "tokenKey,tokenSecretが取得できません");
        }
        String tokenKey, tokenSecret;
        try {
            tokenKey = (String)jsono.get("tokenKey");
            tokenSecret = (String)jsono.get("tokenSecret");
        }
        catch (Exception ex) {
            throw new ApiException(ServerHandler.Err.NotActivate, "データの値が不正です");
        }
        ApiData apiData = handler.getConnectionManager().getApiManager().getApiData(tokenKey, tokenSecret);
        if (apiData == null) {
            throw new ApiException(ServerHandler.Err.ApiActivationErr, "Apiが登録されていません ゲーム内から登録してください");
        }
        handler.getConnectionManager().createSessionData(apiData, session);
    }
}

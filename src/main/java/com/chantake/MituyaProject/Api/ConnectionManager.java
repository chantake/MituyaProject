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

import com.chantake.MituyaProject.MituyaProject;
import com.chantake.MituyaProject.Parameter.Parameter328;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.HashMap;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

/**
 *
 * @author chantake
 */
public class ConnectionManager implements Runnable {

    private MituyaProject plugin;
    private final ApiManager apiManager = new ApiManager(plugin);
    private final HashMap<IoSession, SessionData> session = new HashMap<>();

    public ConnectionManager(MituyaProject plugin) {
        this.plugin = plugin;
    }

    public void init() {
        try {
            this.plugin.Log("MituyaApi初期化開始　ポート：" + Parameter328.api_port + "");
            IoAcceptor acceptor = new NioSocketAcceptor();
            LoggingFilter loggingFilter = new LoggingFilter();
            ProtocolCodecFilter protocolCodecFilter = new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8")));

            acceptor.getFilterChain().addLast("logger", loggingFilter);
            acceptor.getFilterChain().addLast("codec", protocolCodecFilter);

            acceptor.setHandler(new ServerHandler(plugin, this));

            acceptor.getSessionConfig().setReadBufferSize(2048);
            acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
            acceptor.bind(new InetSocketAddress(Parameter328.api_port));
            this.plugin.Log("MituyaApi初期化完了");
        }
        catch (IOException ex) {
            this.plugin.ErrLog("Api起動に失敗しました " + ex);
        }
    }

    public ApiManager getApiManager() {
        return apiManager;
    }

    public SessionData getSessionData(IoSession session) {
        if (this.session.containsKey(session)) {
            return this.session.get(session);
        }
        return null;
    }

    public SessionData createSessionData(ApiData apiData, IoSession ioSession) {
        SessionData sessionData = new SessionData(apiData, ioSession);
        this.session.put(ioSession, sessionData);
        return sessionData;
    }

    @Override
    public void run() {
        this.init();
    }

}

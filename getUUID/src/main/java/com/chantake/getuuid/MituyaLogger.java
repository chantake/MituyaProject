/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chantake.getuuid;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author chantake
 */
public class MituyaLogger {

    private static final String LOGGER_NAME = "MituyaBot";
    private static final Logger logger;
    private static final Level level = Level.CONFIG;

    static {
        logger = Logger.getLogger(LOGGER_NAME);

        //コンソールに出力するハンドラーを生成
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new SimpleFormatter());
        handler.setLevel(level);
        logger.setLevel(level);

        //"MyLogger"に作成したハンドラーを設定
        logger.addHandler(handler);

        //親ロガーへ通知しない
        logger.setUseParentHandlers(false);
    }

    public static void info(String msg) {
        logger.info(msg);
    }

    public static void config(String msg) {
        logger.config(msg);
    }

    public static void warning(String string) {
       logger.warning(string);
    }
}

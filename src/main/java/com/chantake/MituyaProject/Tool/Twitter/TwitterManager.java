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
package com.chantake.MituyaProject.Tool.Twitter;

import com.chantake.MituyaProject.MituyaProject;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

/**
 *
 * @author chantake
 */
public class TwitterManager {

    private final String consumerKey = "";
    private final String consumerSecret = "";
    // AccessTokenのキー
    private final String tokenKey = "";
    // AccessTokenの値
    private final String tokenSecret = "";
    private final MituyaProject plugin;
    private Twitter twitter = null;

    public TwitterManager(MituyaProject plugin) {
        this.plugin = plugin;
    }

    /**
     * 初期設定
     */
    public void init() {
        plugin.Log("Twitterを読み込みます");
        TwitterFactory factory = new TwitterFactory();
        //AccessToken at = new AccessToken(tokenKey, tokenSecret);
        // AccessTokenによる認証
        this.twitter = factory.getInstance();
        //this.twitter.setOAuthConsumer(consumerKey, consumerSecret);
        plugin.Log("Twitterを設定しました");
    }

    /**
     * ハッシュタグ(#328mss)付きのツイートをします
     *
     * @param message
     */
    public void send328Tweet(String message) {
        try {
            this.twitter.updateStatus(message + " #328mss");
        }
        catch (NullPointerException | TwitterException ex) {
            plugin.ErrLog("ツイートに失敗しました send328Tweet:" + ex);
        }
    }
}

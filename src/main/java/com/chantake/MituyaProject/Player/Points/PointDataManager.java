/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chantake.MituyaProject.Player.Points;


import com.chantake.MituyaProject.Player.PlayerInstance;

/**
 *
 * @author ezura
 */
public class PointDataManager {
    
    /**
     * ポイント獲得ログをDBに記録します。
     */
    public static void InsertPointLog(){
    }
    
    /**
     * デイリーポイント最終獲得時間をDBから読み込みます。
     */
    public static void LoadDailyPointTimeFromDB(String UUID){
        
    }
    
    /**
     * 指定プレイヤーにデイリーポイントを付与します。
     * @param pi 
     */
    public static void InvestDailyPoint(PlayerInstance pi){
        long OneDay = 60 * 60 * 24;
        long now = System.currentTimeMillis() / 1000L / OneDay;
        now = now * OneDay;
        
        
        long last = pi.GetPointData().GetDailyPointsTime() / OneDay;
        last = last * OneDay;
        
        if(last + OneDay <= now){
            pi.GetPointData().SetPoints(pi.GetPointData().GetPoints() + 1);
            pi.sendMessage("デイリーポイントを獲得しました。");
            
        }
    }
    
    
    
    
}

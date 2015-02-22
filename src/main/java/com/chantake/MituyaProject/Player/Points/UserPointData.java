/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chantake.MituyaProject.Player.Points;

/**
 *
 * @author ezura
 */
public class UserPointData {
    private int points = 0;
    private long DailyPointTime = 0; 
    
    public void SetPoints(int value){
        this.points = value;
    }
    
    public int GetPoints(){
        return this.points;
    }
    
    /**
     * デイリーポイント獲得時間をセットします。
     * @param value Unixタイムスタンプ
     */
    public void SetDailyPointsTime(long value){
        this.DailyPointTime = value;
    }
    
    /**
     * デイリーポイント獲得時間を取得します。
     * @return Unixタイムスタンプ
     */
    public long GetDailyPointsTime(){
        return this.DailyPointTime;
    }
    
    
}

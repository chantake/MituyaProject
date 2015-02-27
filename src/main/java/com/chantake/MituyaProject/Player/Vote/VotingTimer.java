/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chantake.MituyaProject.Player.Vote;

import com.chantake.MituyaProject.MituyaProject;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * @author azutake
 */
public class VotingTimer implements Runnable {

    public MituyaProject _plugin;
    private int _Time;
    
    public VotingTimer(MituyaProject plugin, int time)
    {
        _plugin = plugin;
        _Time = time;
    }
    
    @Override
    public void run() {
        int time;
        int tt = 0;
        if (_Time > 60 && _Time % 60 != 0) {
            time = -1;
        } else if (_Time > 60) {
            tt = _Time / 60;
            time = -2;
        } else {
            time = _Time;
        }
        switch (time) {
            case -2:
                
                sendBroadcastMessage("投票受付終了まで残り" + ChatColor.AQUA + tt + ChatColor.GREEN + " 分です。");
                break;
            case -1:
                break;
            case 0:
                VoteManager.Default().voteEnded();
                VoteManager.Default().cancelTask();
                break;
            case 180:
                sendBroadcastMessage("投票受付終了まで残り" + ChatColor.AQUA + "3" + ChatColor.GREEN + " 分です。");
                break;
            case 120:
                sendBroadcastMessage("投票受付終了まで残り" + ChatColor.AQUA + "2" + ChatColor.GREEN + " 分です。");
                break;
            case 60:
                sendBroadcastMessage("投票受付終了まで残り" + ChatColor.AQUA + "1" + ChatColor.GREEN + " 分です。");
                break;
            default:
                sendBroadcastMessage("投票受付終了まで残り" + ChatColor.AQUA + time + ChatColor.GREEN + " 秒です。");
                break;
        }
        _Time -= 10;
    }
    
    private void sendBroadcastMessage(String message)
    {
        _plugin.broadcastMessage("[" + ChatColor.GOLD + "Vote" + ChatColor.WHITE +  "]" + ChatColor.GREEN + message);
    }
}

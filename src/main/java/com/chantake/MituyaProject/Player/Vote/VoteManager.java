/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chantake.MituyaProject.Player.Vote;

import com.chantake.MituyaProject.Exception.PlayerOfflineException;
import com.chantake.MituyaProject.MituyaProject;
import com.chantake.MituyaProject.Player.PlayerInstance;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitScheduler;

/**
 *
 * @author azutake
 */
public class VoteManager {
    private static VoteManager _default;
    
    private MituyaProject _plugin;
    private boolean _voting = false;
    private PlayerInstance _votingPlayer;
    private int _vote_main = -1;
    private int _vote_sub = -1;
    private int _agrees = 0;
    private World _vote_world;
    private List<String> _voteJoinPlayers;
    
    private int _executingVoteTask = -1;
    
    /**
     * デフォルトで使用されるべきマネージャ
     * @return 現在使用されるべきデフォルトマネージャ。初期化されていなければnullが返る
     */
    public static VoteManager Default()
    {
        if(_default != null)
            return _default;
        
        return null;
    }
    
    public VoteManager(MituyaProject plugin)
    {
        _plugin = plugin;
        _default = this;
        _voteJoinPlayers = new ArrayList<>();
    }
    
    public boolean startVote(PlayerInstance votingPlayer, int main, int sub) throws PlayerOfflineException
    {
        if(_voting)
            return false;
        
        _voting = true;
        _votingPlayer = votingPlayer;
        
        _vote_main = main;
        _vote_sub = sub;
        
        _vote_world = votingPlayer.getPlayer().getWorld();
        
        String strMain = "";
        String strSub = "";
        
        switch(main)
        {
            case 0:
                strMain = "天気";
                switch(sub)
                {
                    case 0:
                        strSub = "晴れ";
                        break;
                    case 1:
                        strSub = "雨";
                        break;
                    case 2:
                        strSub = "雷";
                        break;
                }
                break;
            case 1:
                strSub = "時間";
                switch(sub)
                {
                    case 0:
                        strSub = "朝";
                        break;
                    case 1:
                        strSub = "昼";
                        break;
                    case 2:
                        strSub = "夕";
                        break;
                    case 3:
                        strSub = "夜";
                        break;
                }
                break;
        }
        
        sendBroadcastMessage(ChatColor.YELLOW + votingPlayer.getDisplayName() + ChatColor.GREEN + "様が" + strMain + "を" + strSub + "にすることを提案しました。賛成しますか？ " + ChatColor.AQUA + "/vote yes" + ChatColor.GREEN + "又は" + ChatColor.RED + "/vote no" + ChatColor.GREEN + "で投票してください");
        
        _executingVoteTask = _plugin.getServer().getScheduler().scheduleSyncRepeatingTask(_plugin, new VotingTimer(_plugin, 30), 0, 10 * 20L);
        
        return true;
    }
    
    public byte voteJoin(PlayerInstance player, boolean joinMode)
    {
        if(!_voting)
            return 2;
        
        if(_voteJoinPlayers.contains(player.getRawName()))
            return 1;
        
        if(joinMode)
            ++_agrees;
        
        _voteJoinPlayers.add(player.getRawName());
        
        player.sendSuccess((joinMode ? ChatColor.AQUA + "賛成" : ChatColor.RED + "反対") + ChatColor.YELLOW + "で投票しました。");
        
        return 0;
    }
    
    public byte voteYes(PlayerInstance player)
    {
        return voteJoin(player, true);
    }
    
    public byte voteNo(PlayerInstance player)
    {
        return voteJoin(player, false);
    }
    
    public void voteEnded()
    {
        if(!_voting)
            return;
        
        boolean voteResult = getVoteResult(_agrees, _voteJoinPlayers.size());
        
        sendBroadcastMessage("投票受付が終了しました。" + ChatColor.AQUA + "賛成 " + ChatColor.GREEN + _agrees + "人、" + ChatColor.RED + "反対 " + ChatColor.GREEN + (_voteJoinPlayers.size() - _agrees) + "人で" + (voteResult ? ChatColor.AQUA + "可決" : ChatColor.RED + "否決") + ChatColor.GREEN + "となりました");
        
        //可決時
        if(voteResult)
        {
            switch(_vote_main)
            {
                case 0:
                    switch(_vote_sub)
                    {
                        case 0:
                            _vote_world.setStorm(false);
                            _vote_world.setThundering(false);
                            break;
                        case 1:
                            _vote_world.setStorm(true);
                            _vote_world.setThundering(false);
                            break;
                        case 2:
                            _vote_world.setStorm(true);
                            _vote_world.setThundering(true);
                            break;
                    }
                    
                    _vote_world.setWeatherDuration(1000000);
                    break;
                case 1:
                    switch(_vote_sub)
                    {
                        case 0:
                            _vote_world.setTime(0);
                            break;
                        case 1:
                            _vote_world.setTime(6000);
                            break;
                        case 2:
                            _vote_world.setTime(12000);
                            break;
                        case 3:
                            _vote_world.setTime(14000);
                            break;
                    }
                    break;
            }
        }
        
        _voting = false;
        _votingPlayer = null;
        _voteJoinPlayers.clear();
        _agrees = 0;
        _vote_main = -1;
        _vote_sub = -1;
    }
    
    private boolean getVoteResult(int agrees, int joinPlayers)
    {
        if(joinPlayers == 0)
            return true;
        
        if(agrees >= joinPlayers)
            return true;
        
        return false;
    }
    
    private void sendBroadcastMessage(String message)
    {
        _plugin.broadcastMessage("[" + ChatColor.GOLD + "Vote" + ChatColor.WHITE +  "]" + ChatColor.GREEN + message);
    }
    
    public void cancelTask()
    {
        if(_executingVoteTask == -1)
            return;
        
        _plugin.getServer().getScheduler().cancelTask(_executingVoteTask);
        _executingVoteTask = -1;
    }
}

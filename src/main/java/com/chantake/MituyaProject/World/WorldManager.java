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
package com.chantake.MituyaProject.World;

import com.chantake.MituyaProject.MituyaProject;
import com.chantake.MituyaProject.Tool.MySqlProcessing;
import com.chantake.MituyaProject.World.Generator.NoneGenerator;
import com.chantake.MituyaProject.World.Generator.Skyland.SkyLandChunkGenerator;
import com.chantake.MituyaProject.World.Generator.listener.SkyLandListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.generator.ChunkGenerator;

/**
 * ワールドを管理します
 *
 * @author chantake
 */
public class WorldManager {

    /**
     * MituyaProject
     */
    private final MituyaProject plugin;
    /**
     * WorldDataを保持するMap
     */
    private final Map<String, WorldData> world_data = new HashMap<>();

    /**
     * インスタンス作成
     *
     * @param plugin MituyaProject
     */
    public WorldManager(MituyaProject plugin) {
        this.plugin = plugin;
    }

    /**
     * すべてのワールドデータをDBからロードします
     */
    public void LoadWorlds() {
        plugin.Log("ワールドをロードします");
        MySqlProcessing.LoadWolrdConfig(this);
        plugin.Log("すべてのワールドを読み込みました");
    }

    /**
     * ワールドデータをロードします
     *
     * @param id ワールドID
     * @param name ワールド名
     * @param environment 環境
     * @param worldType ワールドタイプ
     * @param border エリア制限
     * @param mob mob制限
     * @param command
     * @param difficulty 難易度
     */
    public void LoadWorld(int id, String name, int environment, String worldType, int border, boolean mob, boolean command, int difficulty) {
        try {
            Environment en = Environment.getEnvironment(environment);
            Difficulty diff = Difficulty.getByValue(difficulty);
            WorldType type;
            ChunkGenerator generator = null;
            if (worldType.equalsIgnoreCase("NONE")) {
                type = WorldType.FLAT;
                generator = new NoneGenerator();
            } else if (worldType.startsWith("LARGE")) {
                type = WorldType.LARGE_BIOMES;
            } else if (worldType.equalsIgnoreCase("WATER")) {
                type = WorldType.NORMAL;
            } else if (worldType.equalsIgnoreCase("SKYLAND")) {
                type = WorldType.NORMAL;
                generator = new SkyLandChunkGenerator();
                plugin.getServer().getPluginManager().registerEvents(new SkyLandListener(plugin), plugin);
            } else {
                type = WorldType.getByName(worldType);
            }
            World world = this.addWorld(name, en, type, generator, null, diff);
            if (world != null) {
                WorldData wd = new WorldData(id, world, worldType, border, mob, command);
                this.world_data.put(world.getName(), wd);
                plugin.Log("LoadWorld：" + name);
            }
        }
        catch (Exception e) {
        }
    }

    /**
     * ワールドを追加します
     *
     * @param name ワールド名
     * @param environment 環境
     * @param worldType ワールドタイプ
     * @param generator
     * @param seed シード値
     * @param difficulty
     * @return World
     */
    public World addWorld(String name, Environment environment, WorldType worldType, ChunkGenerator generator, String seed, Difficulty difficulty) {
        WorldCreator wc = new WorldCreator(name);
        if (seed != null && seed.length() > 0) {
            wc.seed(Long.valueOf(seed));
        }
        if (generator != null) {
            wc.generator(generator);
        }
        wc.environment(environment);
        wc.type(worldType);
        World wd = wc.createWorld();
        //難易度変更
        if (difficulty != null) {
            wd.setDifficulty(difficulty);
        }
        return wd;
    }

    /**
     * WorldDataを取得します
     *
     * @param name ワールド名
     * @return WorldData
     */
    public WorldData getWorldData(String name) {
        if (this.world_data.containsKey(name)) {
            return this.world_data.get(name);
        } else {
            return null;
        }
    }

    /**
     * WorldDataを取得します
     *
     * @param world World
     * @return WorldData
     */
    public WorldData getWorldData(World world) {
        return this.getWorldData(world.getName());
    }

    /**
     * WorldData
     *
     * @param id ワールドID
     * @return WorldData
     */
    public WorldData getWorldData(int id) {
        for (WorldData wd : this.world_data.values()) {
            if (wd.getId() == id) {
                return wd;
            }
        }
        return null;
    }

    /**
     * Worldを取得します
     *
     * @param world ワールド名
     * @return World
     */
    public World getWorld(String world) {
        return this.getWorldData(world).getWorld();
    }

    /**
     * Worldを取得します
     *
     * @param id ワールドID
     * @return World
     */
    public World getWorld(int id) {
        return this.getWorldData(id).getWorld();
    }

    /**
     * Worldをセーブする
     */
    public void saveWorlds() {
        //iteratorを生成
        Iterator<World> iterator = this.getWorlds().iterator();
        //ループ
        while (iterator.hasNext()) {
            World wd = iterator.next();
            //セーブ
            wd.save();
        }
    }

    /**
     * ロード済みワールドを取得します
     *
     * @return Iterable<World>
     */
    public Iterable<World> getWorlds() {
        return this.plugin.getServer().getWorlds();
    }

    public Iterable<WorldData> getWorldDatas() {
        return this.world_data.values();
    }

    public Iterable<String> getWorldNames() {
        return this.world_data.keySet();
    }
}

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
package com.chantake.MituyaProject.Event;

import com.chantake.MituyaProject.MituyaProject;
import com.chantake.MituyaProject.Tool.Script.AbstractScriptManager;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

/**
 *
 * @author chantake
 */
public class EventScriptManager extends AbstractScriptManager {

    private final MituyaProject plugin;

    private class EventEntry {

        public EventEntry(String script, Invocable iv, EventManager em) {
            this.script = script;
            this.iv = iv;
            this.em = em;
        }
        public String script;
        public Invocable iv;
        public EventManager em;
    }
    private final Map<String, EventEntry> events = new LinkedHashMap<>();

    public EventScriptManager(MituyaProject plugin, String[] split) {
        this.plugin = plugin;
        for (String script : split) {
            if (!script.equals("")) {
                Invocable iv = getInvocable("event/" + script + ".js", plugin);
                if (iv != null) {
                    events.put(script, new EventEntry(script, iv, new EventManager(plugin, iv, script)));
                } else {
                    plugin.ErrLog(script + " は見つからないか、ファイルが壊れています");
                }

            }
        }
    }

    public EventManager getEventManager(String event) {
        EventEntry entry = events.get(event);
        if (entry == null) {
            return null;
        }
        return entry.em;
    }

    public void init() {
        for (EventEntry entry : events.values()) {
            try {
                ((ScriptEngine)entry.iv).put("em", entry.em);
                entry.iv.invokeFunction("init", (Object)null);
            }
            catch (ScriptException | NoSuchMethodException ex) {
                Logger.getLogger(EventScriptManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void cancel() {
        for (EventEntry entry : events.values()) {
            entry.em.cancel();
        }
    }
}

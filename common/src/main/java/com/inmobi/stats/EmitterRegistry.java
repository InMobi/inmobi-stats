package com.inmobi.stats;

import com.inmobi.stats.StatsEmitter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Collections;
import java.util.Collection;
import java.util.Properties;
import java.io.FileInputStream;

public class EmitterRegistry {

    private static Map<String, StatsEmitter> registry = new HashMap<String, StatsEmitter>();

    public static synchronized StatsEmitter lookup(String config) throws Exception {

        if (registry.containsKey(config)) {
            return registry.get(config);
        } else {
            Properties props = new Properties();
            props.load(new FileInputStream(config));
            String className = props.getProperty("className", null);
            StatsEmitter emitter = null;
            if (className != null || className.length() == 0) {
                emitter = (StatsEmitter) Class.forName(className).newInstance();
                emitter.init(props);
            } else {
                throw new Exception("className property in \"" + config + "\" is either empty or not specified");
            }
            if (emitter != null) {
                registry.put(config, emitter);
                return emitter;
            }
        }
        return null;
    }

    public static synchronized void remove(String config) {
        if (registry.containsKey(config)) {
            registry.get(config).removeAll();
            registry.remove(config);
        }
    }

    public static synchronized void removeAll() {
        Collection<StatsEmitter> se = (Collection<StatsEmitter>) registry.values();
        for (StatsEmitter s : se) {
            s.removeAll();
        }
        registry.clear();
    }
}


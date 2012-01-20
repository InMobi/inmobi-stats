package com.inmobi.stats;

import com.inmobi.stats.StatsEmitter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Collections;
import java.util.Properties;
import java.io.FileInputStream;

public class EmitterRegistery {

    private static Map<String, StatsEmitter> registery = Collections.synchronizedMap(new HashMap<String, StatsEmitter>());

    public static StatsEmitter lookup(String config) throws Exception {

        if (registery.containsKey(config)) {
            return registery.get(config);
        } else {
            Properties props = new Properties();
            props.load(new FileInputStream(config));
            String className = props.getProperty("className", null);
            StatsEmitter emitter = null;
            if (className != null || className.length() == 0) {
                emitter = (StatsEmitter) Class.forName("com.inmobi.stats.emitter." + className).newInstance();
                emitter.init(props);
                emitter.start();
            } else {
                throw new RuntimeException("className property in \"" + config + "\" is either empty or not specified");
            }
            if (emitter != null) {
                registery.put(config, emitter);
                return emitter;
            }
        }
        return null;
    }

    public static void remove(String config) {
        if (registery.containsKey(config)) {
            registery.get(config).stop();
            registery.remove(config);
        }
    }

    public static void removeAll() {
        Set hset = registery.keySet();
        synchronized(registery) {
            for (Object s : hset) {
                registery.get((String)s).stop();
            }
        }
        hset.clear();
    }
}


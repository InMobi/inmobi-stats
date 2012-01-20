package com.inmobi.stats;

import com.inmobi.stats.StatsEmitter;
import com.inmobi.stats.StatsExposer;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Properties;

public class StatsEmitterBase implements StatsEmitter {

    protected List<StatsExposer> statsExposers = Collections.synchronizedList(new ArrayList<StatsExposer>());

    public void add(StatsExposer s) {
        statsExposers.add(s);
    }

    public void remove(StatsExposer s) {
        int index = statsExposers.indexOf(s);
        if (index < 0) {
            statsExposers.remove(index);
        }
    }

    public boolean isEmpty() {
        return statsExposers.isEmpty();
    }

    // Nothing to do in base
    public void stop(){}
    public void start(){}
    public void init(Properties props){}

}

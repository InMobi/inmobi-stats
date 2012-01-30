package com.inmobi.stats;

import com.inmobi.stats.StatsEmitter;
import com.inmobi.stats.StatsExposer;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Properties;

public abstract class StatsEmitterBase implements StatsEmitter {

    protected List<StatsExposer> statsExposers = Collections.synchronizedList(new ArrayList<StatsExposer>());

    @Override
    public synchronized void add(StatsExposer s) {
        statsExposers.add(s);
    }

    @Override
    public synchronized void remove(StatsExposer s) {
        statsExposers.remove(s);
    }

    public boolean isEmpty() {
        return statsExposers.isEmpty();
    }

}

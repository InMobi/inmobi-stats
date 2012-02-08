package com.inmobi.stats;

import com.inmobi.stats.StatsExposer;
import java.util.Properties;

public interface StatsEmitter {

    public void init(Properties props);

    public void add(StatsExposer s);

    public void remove(StatsExposer s);

    public void removeAll();
}

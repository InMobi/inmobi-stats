package com.inmobi.stats;

import java.util.HashMap;

public interface StatsExposer {

    public HashMap<String, Number> getStats();

    public HashMap<String, String> getContexts();

}

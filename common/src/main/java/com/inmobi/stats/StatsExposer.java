package com.inmobi.stats;

import java.util.Map;

public interface StatsExposer {

    public Map<String, Number> getStats();

    public Map<String, String> getContexts();

}

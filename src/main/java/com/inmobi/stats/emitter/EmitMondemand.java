package com.inmobi.stats.emitter;

import java.net.InetAddress;
import org.mondemand.Client;
import org.mondemand.Transport;
import org.mondemand.transport.LWESTransport;
import com.inmobi.stats.StatsExposer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.io.FileInputStream;

public class EmitMondemand implements Runnable {
    private Thread t;
    private StatsExposer exposer;
    private Client client = null;
    private Transport transport = null;
    private volatile boolean should_run = true;
    private Properties props = new Properties();
    private String conf_prefix;
    private Integer sleep; 
    private String hostname = null;

    public EmitMondemand(StatsExposer s, String conf_prefix, HashMap<String, String> contexts) {
        this.conf_prefix = conf_prefix;
        exposer = s;
        try {
            props.load(new FileInputStream("emit_mondemand.properties"));
        } catch (Exception e) {}
        try { hostname = InetAddress.getLocalHost().getHostName(); } catch (Exception e) {}
        sleep = Integer.valueOf(props.getProperty(conf_prefix + ".poll_interval", "10000"));
        client = new Client(props.getProperty(conf_prefix + ".app_name", "sample_app"));

        if (hostname != null)
            client.addContext("hostname", hostname);

        Iterator it = contexts.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            client.addContext((String)pair.getKey(), (String)(pair.getValue() == null ? "" : pair.getValue()));
        }

        try {
            transport = new LWESTransport(InetAddress.getByName(props.getProperty(conf_prefix + ".emit_address", "224.1.1.11")), Integer.valueOf(props.getProperty(conf_prefix + ".emit_port", "9191")), null);
            client.addTransport(transport);
        } catch(Exception e) { e.printStackTrace(); }
        t = new Thread(this);
        t.start();
    }

    public void run() {
        HashMap<String, Number> stats;
        while (should_run) {
            stats = exposer.getStats();
            Iterator it = stats.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                client.setKey((String)pair.getKey(), ((Number)pair.getValue()).longValue());
            }
            client.flushStats(true);
            try {
                Thread.sleep(this.sleep);
            } catch (InterruptedException e) {}
        }
    }

    public void stop() {
        should_run = false;
        t.interrupt();
    }
}

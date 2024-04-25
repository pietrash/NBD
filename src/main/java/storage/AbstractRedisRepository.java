package storage;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import org.ini4j.Ini;
import org.ini4j.IniPreferences;
import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisClientConfig;
import redis.clients.jedis.JedisPooled;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.prefs.Preferences;

public abstract class AbstractRedisRepository {
    private static final Jsonb jsonb = JsonbBuilder.create();
    private static JedisPooled pool;

    public static Jsonb getJsonb() {
        return jsonb;
    }

    public static JedisPooled getPool() {
        return pool;
    }

    public void initDbConnection() {
        try {
            File file = new File("./src/resources/conf.ini");
            Ini ini = new Ini(file);

            Preferences preferences = new IniPreferences(ini);
            JedisClientConfig clientConfig = DefaultJedisClientConfig.builder().socketTimeoutMillis(5000).build();

            pool = new JedisPooled(new HostAndPort(preferences.node("HostPort").get("host", "host"),
                    Integer.parseInt(preferences.node("HostPort").get("port", "port"))), clientConfig);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean checkConnection() {
        return pool.getPool().getResource().isConnected();
    }

    public void clearCache() {
        Set<String> keys = pool.keys("*");
        for (String key : keys) {
            pool.del(key);
        }
    }

    public void close() throws Exception {
        pool.getPool().destroy();
        pool.close();
    }
}
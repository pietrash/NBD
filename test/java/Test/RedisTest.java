package Test;

import model.ClientAddress;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.*;
import storage.ClientMongoRepository;
import storage.ClientRedisRepository;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class RedisTest {

    @Test
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @Benchmark
    public void addClientTest() throws Exception {
        ClientAddress clientAddress1 = new ClientAddress(1, "Mariusz", "Pudzianowski", 1, "Pudzianowska", 200, "Pudzianów", 123456);
        ClientMongoRepository clientMongoRepository = new ClientMongoRepository();
        ClientRedisRepository clientRedisRepository = new ClientRedisRepository(clientMongoRepository);
        clientRedisRepository.addClient(clientAddress1);

        Assertions.assertNotNull(clientRedisRepository.getClient(1));
        Assertions.assertNull(clientRedisRepository.getClient(2));

        clientRedisRepository.clearCache();
        clientRedisRepository.close();
    }

    @Test
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @Benchmark
    public void addClientMongoTest() {
        ClientAddress clientAddress = new ClientAddress(1, "Pablo", "Escobar", "Uliczna", 1, "Mehiko", 121212);

        ClientMongoRepository clientMongoRepository = new ClientMongoRepository();
        clientMongoRepository.addClient(clientAddress);
        Assertions.assertEquals(clientMongoRepository.getClient(1).getFirstName(), "Pablo");

        clientMongoRepository.getMongoDatabase().drop();
    }

    @Test
    public void removeClientsTest() {
        ClientAddress clientAddress1 = new ClientAddress(1, "Mariusz", "Pudzianowski", 1, "Pudzianowska", 200, "Pudzianów", 123456);
        ClientMongoRepository clientMongoRepository = new ClientMongoRepository();
        ClientRedisRepository clientRedisRepository = new ClientRedisRepository(clientMongoRepository);
        clientRedisRepository.addClient(clientAddress1);

        Assertions.assertNotNull(clientRedisRepository.getClient(1));
        clientRedisRepository.removeClient(1);
        Assertions.assertNull(clientRedisRepository.getClient(1));

        clientRedisRepository.clearCache();
    }

    @Test
    public void clearCacheTest() {
        ClientAddress clientAddress1 = new ClientAddress(1, "Mariusz", "Pudzianowski", 1, "Pudzianowska", 200, "Pudzianów", 123456);
        ClientMongoRepository clientMongoRepository = new ClientMongoRepository();
        ClientRedisRepository clientRedisRepository = new ClientRedisRepository(clientMongoRepository);

        clientRedisRepository.addClient(clientAddress1);

        Assertions.assertNotNull(clientRedisRepository.getClient(1));
        clientRedisRepository.clearCache();
        Assertions.assertNull(clientRedisRepository.getClient(1));
    }

    @Test
    public void noRedisTest() {
        ClientAddress clientAddress1 = new ClientAddress(1, "Mariusz", "Pudzianowski", 1, "Pudzianowska", 200, "Pudzianów", 123456);
        ClientMongoRepository clientMongoRepository = new ClientMongoRepository();
        ClientRedisRepository clientRedisRepository = new ClientRedisRepository(clientMongoRepository);
        clientRedisRepository.addClient(clientAddress1);
        clientMongoRepository.addClient(clientAddress1);

        try {
            clientRedisRepository.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Assertions.assertNotNull(clientRedisRepository.getClient(1));
        clientMongoRepository.getMongoDatabase().drop();
    }

    @Test
    public void benchmarkTest() throws IOException {
        String[] argv = {};
        org.openjdk.jmh.Main.main(argv);
    }

    @Test
    public void expireTest() throws Exception {
        ClientAddress clientAddress1 = new ClientAddress(1, "Mariusz", "Pudzianowski", 1, "Pudzianowska", 200, "Pudzianów", 123456);
        ClientMongoRepository clientMongoRepository = new ClientMongoRepository();
        ClientRedisRepository clientRedisRepository = new ClientRedisRepository(clientMongoRepository);
        clientRedisRepository.addClient(clientAddress1);

        Thread.sleep(2000);
        Assertions.assertNotNull(clientRedisRepository.getClient(1));
        Thread.sleep(4000);
        Assertions.assertNull(clientRedisRepository.getClient(1));

        clientRedisRepository.clearCache();
        clientRedisRepository.close();
    }
}

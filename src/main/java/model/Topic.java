package model;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.KafkaFuture;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class Topic {

    public void createTopic(String name, int partitionNumber, short replicationFactor) {
        Properties properties = new Properties();
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka1:9192,kafka1:9292,kafka1:9392");

        try (Admin admin = Admin.create(properties)) {
            NewTopic newTopic = new NewTopic(name, partitionNumber, replicationFactor);
            CreateTopicsOptions options = new CreateTopicsOptions()
                    .timeoutMs(1000)
                    .validateOnly(false)
                    .retryOnQuotaViolation(true);
            CreateTopicsResult result = admin.createTopics(List.of(newTopic), options);
            KafkaFuture<Void> futureResult = result.values().get(name);
            futureResult.get();
        } catch (ExecutionException | InterruptedException ee) {
            System.out.println(ee.getCause());
        }
    }
}

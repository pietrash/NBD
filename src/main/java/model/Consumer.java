package model;

import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.ConsumerGroupDescription;
import org.apache.kafka.clients.admin.DescribeConsumerGroupsResult;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.text.MessageFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Consumer {

    private final List<KafkaConsumer<Integer, String>> consumerGroup = new ArrayList<>();
    private KafkaConsumer consumer;

    public Consumer() {

    }

    public void consumerInit(String topic) {
        Properties consumerConfig = new Properties();
        consumerConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class.getName());
        consumerConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerConfig.put(ConsumerConfig.GROUP_ID_CONFIG, "consumerGroup");
        consumerConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka1:9192,kafka2:9292,kafka3:9392");

        consumer = new KafkaConsumer(consumerConfig);
        consumer.subscribe(List.of(topic));
    }

    public void addToConsumerGroup() {
        consumerGroup.add(consumer);
    }

    public void deleteConsumerGroup(String name) throws InterruptedException, ExecutionException {
        Properties properties = new Properties();
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka1:9192,kafka2:9292,kafka3:9392");
        try (Admin admin = Admin.create(properties)) {
            DescribeConsumerGroupsResult describeConsumerGroupsResult = admin.describeConsumerGroups(List.of(name));
            Map<String, KafkaFuture<ConsumerGroupDescription>> describedGroups =
                    describeConsumerGroupsResult.describedGroups();
            for (Future<ConsumerGroupDescription> group : describedGroups.values()) {
                ConsumerGroupDescription consumerGroupDescription = group.get();
                System.out.println(consumerGroupDescription);
            }
            admin.deleteConsumerGroups(List.of(name));
        }
    }


//    public void receiveRents() {
//        Map<Integer, Long> offsets = new HashMap<>();
//        Duration timeout = Duration.of(100, ChronoUnit.MILLIS);
//        MessageFormat messageFormatter = new MessageFormat("Temat {0}, partycja {1}, offset {2, number, integer}, " +
//                "klucz {3}, wartość {4}");
//
//
//        ConsumerRecords<Integer, String> records = consumer.poll(timeout);
//        for (ConsumerRecord<Integer, String> record : records) {
//            String result = messageFormatter.format(new Object[]{record.topic(), record.partition(), record.offset(),
//                    record.key(), record.value()});
//            System.out.println(result);
//            offsets.put(record.partition(), record.offset());
//        }
//        System.out.println(offsets);
//        consumer.commitAsync();
//    }
//
//    public void receiveFromBeginning() {
//        consumer.poll(0);
//        Set<TopicPartition> consumerAssignment = consumer.assignment();
//        System.out.println(consumer.groupMetadata().memberId() + " " + consumerAssignment);
//        consumer.seekToBeginning(consumerAssignment);
//
//        Duration timeout = Duration.of(1000, ChronoUnit.MILLIS);
//        MessageFormat messageFormatter = new MessageFormat("Temat {0}, partycja {1}, offset {2, number, integer}, " +
//                "klucz {3}, wartość {4}");
//
//        Set<TopicPartition> unfinishedPartitions = new HashSet<>(consumerAssignment);
//        while (!unfinishedPartitions.isEmpty()) {
//            ConsumerRecords<Integer, String> records = consumer.poll(timeout);
//            for (ConsumerRecord<Integer, String> record : records) {
//                String result = messageFormatter.format(new Object[]{record.topic(), record.partition(), record.offset(),
//                        record.key(), record.value()});
//                System.out.println(result);
//
//                for (TopicPartition partition : consumerAssignment) {
//                    long position = consumer.position(partition, timeout);
//                    if (partition.partition() == record.partition() && record.offset() == position - 1) {
//                        unfinishedPartitions.remove(partition);
//                    }
//                }
//            }
//        }
//    }

    public void consume() {
        try {
            consumer.poll(0);
            Set<TopicPartition> consumerAssignment = consumer.assignment();
            System.out.println(consumer.groupMetadata().memberId() + " " + consumerAssignment);
            consumer.seekToBeginning(consumerAssignment);

            Duration timeout = Duration.of(100, ChronoUnit.MILLIS);
            MessageFormat messageFormatter = new MessageFormat("Temat {0}, partycja {1}, offset {2, number, integer}, " +
                    "klucz {3}, wartość {4}");
            while (true) {
                ConsumerRecords<Integer, String> records = consumer.poll(timeout);
                for (ConsumerRecord<Integer, String> record : records) {
                    String result = messageFormatter.format(new Object[]{
                            record.topic(),
                            record.partition(),
                            record.offset(),
                            record.key(),
                            record.value()});
                    System.out.println(result);
                }
            }
        } catch (WakeupException we) {
            System.out.println("Job Finished");
        }
    }
}

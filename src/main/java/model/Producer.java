package model;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class Producer {

    public KafkaProducer producer;

    public Producer() {
        initProducer();
    }

    private void initProducer() {
        Properties producerConfig = new Properties();
        producerConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        producerConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerConfig.put(ProducerConfig.CLIENT_ID_CONFIG, "local");
        producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka1:9192,kafka2:9292,kafka3:9392");
        producerConfig.put(ProducerConfig.ACKS_CONFIG, "all");
//        producerConfig.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);

        producer = new KafkaProducer<>(producerConfig);
    }

    private void onCompletion(RecordMetadata data, Exception exeption) {
        if (exeption == null) {
            System.out.println(data.offset());
        } else {
            System.out.println(exeption);
        }
    }

    public void sendMessageAsync(String message) throws InterruptedException {
        Callback callback = this::onCompletion;

        ProducerRecord<Integer, String> record = new ProducerRecord<>("Test", 1, "test");
        producer.send(record, callback);
    }

}
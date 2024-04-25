import model.Consumer;
import model.Topic;
import org.junit.Test;

public class KafkaTest {

    @Test
    public void addTopicTest() throws InterruptedException {
        Topic topic = new Topic();
        topic.createTopic("Wypozyczenia", 3, (short) 3);
    }

    @Test
    public void consumerTest() {
        Consumer consumer = new Consumer();
        consumer.consumerInit("Wypozyczenia");
        consumer.addToConsumerGroup();
        consumer.consume();
    }
}

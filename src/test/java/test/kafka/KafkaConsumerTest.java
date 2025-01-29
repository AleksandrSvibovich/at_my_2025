package test.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class KafkaConsumerTest<S, S1> {
    public static void main(String[] args) {
        Properties properties = new Properties(); //create new property by java utils
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); // set a bootstrap server
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group"); // название группы может быть любое
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName()); // строковая сериализация
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName()); // строковая десериализация
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); // чтение с самого начала

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singletonList("test-topic")); // подписка на топик который уже был создан

        int i = 1;
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100)); // пауза на  100 мс
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println(i + ". Consumed message key " + record.key() + ", value was " + record.value() + ". Record offset was " + record.offset());
                    i++;
                }
            }
        } finally {
            consumer.close();
        }
    }
}


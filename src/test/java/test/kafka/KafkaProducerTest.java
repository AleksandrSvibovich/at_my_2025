package test.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class KafkaProducerTest {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group"); // хз откуда от придумал тест групп
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        KafkaProducer<String,String> producer = new KafkaProducer<>(properties);

        try {
            for (int i = 0; i <3 ; i++) {
                String key = "KeySSSS "+i;
                String value = "ValueSSSS "+i*2;
                ProducerRecord<String,String> record = new ProducerRecord<>("test-topic",key,value);
                producer.send(record,(metadata,exception) ->{
                    if (exception!=null){
                        exception.printStackTrace();
                    }else {
                        System.out.printf("Message was sent, key - %s, value - %s, partition - %s, offset - %s", key, value, metadata.partition(), metadata.offset());
                    }
                });
            }
        }finally {
            producer.close();
        }
    }
}

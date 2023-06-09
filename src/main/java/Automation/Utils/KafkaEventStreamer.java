package Automation.Utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class KafkaEventStreamer
{

    Properties properties;

    public void publishMessage(Config testConfig, String bootstrapServers, String topicName, String messageContent)
    {
        if (validateMessageContent(topicName, messageContent))
        {
            // creating kafka producer properties
            properties = new Properties();
            properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
            properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

            // creating kafka producer
            KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(properties);
            ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topicName, messageContent);
            testConfig.logComment("Publishing event on topic: " + topicName + "\n" + "Event content: \n" + messageContent);
            kafkaProducer.send(producerRecord, new Callback()
            {
                public void onCompletion(RecordMetadata recordMetadata, Exception e)
                {

                    if (e == null)
                    {
                        testConfig.logComment("Successfully received the details as: \n" + "Topic: " + recordMetadata.topic() + "\n" + "Partition: " + recordMetadata.partition() + "\n" + "Offset: " + recordMetadata.offset() + "\n" + "Timestamp: " + recordMetadata.timestamp());
                    } else
                    {
                        testConfig.logFail("Can't publish,getting error: " + e.getMessage());
                    }
                }
            });
            kafkaProducer.flush();
            kafkaProducer.close();
        } else
        {
            testConfig.logFailToEndExecution("Kafka message event body is invalid : " + messageContent);
        }
    }

    public void subscribeMessage(Config testConfig, String bootstrapServers, String topicName, String groupId)
    {
        // Creating consumer properties
        properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        // creating consumer
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);
        // Subscribing
        consumer.subscribe(Collections.singletonList(topicName));
        // polling
        int count = 0;
        while (count < 1000)
        {
            count++;
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records)
            {
                testConfig.logComment("Key: " + record.key() + ", Value:" + record.value());
                testConfig.logComment("Partition:" + record.partition() + ",Offset:" + record.offset());
            }
        }
    }

    private boolean validateMessageContent(String topicName, String messageContent)
    {
        boolean result = false;
        if (StringUtils.isNotEmpty(messageContent) && messageContent.contains("header") && messageContent.contains("body") && !messageContent.contains("{$"))
        {
            if (topicName.startsWith("papi-"))
            {
                String tempString = messageContent.substring(messageContent.indexOf("merchant_id") + "merchant_id\":".length());
                String merchantIdValue = tempString.substring(0, tempString.indexOf(","));
                result = !StringUtils.isEmpty(merchantIdValue) && !merchantIdValue.trim().equalsIgnoreCase("null");
            } else
            {
                result = true;
            }
        }
        return result;
    }
}

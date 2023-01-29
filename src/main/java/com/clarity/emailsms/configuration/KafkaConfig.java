package com.clarity.emailsms.configuration;

import com.clarity.emailsms.kafka.dto.TransferKafkaDTO;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Class configuration for kafka producer
 * Read config data from application.yml
 */
@Configuration
@EnableKafka
public class KafkaConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String kafkaProducerBootstrapServer;

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String kafkaConsumerBootstrapServer;


    @Value(value = "${spring.kafka.consumer.group-id}")
    private String groupId;


    /**
     * Generate kafka producer configuration
     *
     * @return Kafka configuration, include all needed producer configuration
     */

    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProducerBootstrapServer);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    /**
     * Generate kafka producer factory from configuration
     *
     * @return {@link DefaultKafkaProducerFactory}
     */
    private ProducerFactory<String, TransferKafkaDTO> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    /**
     * Kafka template for send processed data to kafka
     *
     * @return kafka template with config properties
     */
    @Bean
    public KafkaTemplate<String, TransferKafkaDTO> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }



    public ConsumerFactory<String, TransferKafkaDTO> consumerFactory(){
        Map<String, Object> props = new HashMap<>();
        JsonDeserializer<TransferKafkaDTO> deserializer = new JsonDeserializer<>(TransferKafkaDTO.class, false);
        deserializer.addTrustedPackages("*");
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConsumerBootstrapServer);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

       return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }


    @Bean("KafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, TransferKafkaDTO> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, TransferKafkaDTO> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());

        return factory;
    }

    @Bean
    public ProducerFactory<String, TransferKafkaDTO> producerFactoryTicket() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProducerBootstrapServer);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(configProps);
    }


    @Bean
    public NewTopic createNotificationToSMS() {
        return TopicBuilder.name("notification-to-sms")
                .build();
    }
    @Bean
    public NewTopic createNotificationToEmail() {
        return TopicBuilder.name("notification-to-email")
                .build();
    }

}
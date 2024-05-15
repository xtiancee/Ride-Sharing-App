package com.ridesharing.rideservice.config;

import com.ridesharing.core.dto.*;
import com.ridesharing.core.utils.Constants;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic rideTopic() {
        return TopicBuilder.name("RideTopic").build();
    }

    @Bean
    public DefaultKafkaProducerFactory<String, RideMatchRequest> rideMatchRequestProducerFactory(){
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, RideMatchRequest> rideMatchRequestkafkaTemplate() {
        return new KafkaTemplate<>(rideMatchRequestProducerFactory());
    }

    @Bean
    public DefaultKafkaProducerFactory<String, RiderRideApprovedDto> riderRideApprovedProducerFactory(){
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, RiderRideApprovedDto> riderRideApprovedkafkaTemplate() {
        return new KafkaTemplate<>( riderRideApprovedProducerFactory());
    }

    @Bean
    public DefaultKafkaProducerFactory<String, DriverRequest> driverRequestProducerFactory(){
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }


    @Bean
    public KafkaTemplate<String, DriverRequest> driverRequestkafkaTemplate() {
        return new KafkaTemplate<>(driverRequestProducerFactory());
    }


    @Bean
    public DefaultKafkaProducerFactory<String, ClientWhoDisconnectDto> clientWhoDisconnectedProducerFactory(){
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }


    @Bean
    public KafkaTemplate<String, ClientWhoDisconnectDto> clientWhoDisconnectedkafkaTemplate() {
        return new KafkaTemplate<>(clientWhoDisconnectedProducerFactory());
    }


    @Bean
    public Map<String, Object> clientDisconnectedConsumerConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, Constants.CLIENT_DISCONNECT_TOPIC_GROUP);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return props;
    }

    @Bean
    public ConsumerFactory<String, ClientDisconnectDto> clientDisconnectedConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(clientDisconnectedConsumerConfig());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ClientDisconnectDto> clientDisconnectedListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ClientDisconnectDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(clientDisconnectedConsumerFactory());
        return factory;
    }
}

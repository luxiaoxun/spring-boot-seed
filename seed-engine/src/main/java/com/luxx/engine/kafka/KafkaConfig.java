package com.luxx.engine.kafka;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Properties;

@Service
public class KafkaConfig {
    @Value("${kafka.sasl.username:}")
    private String kafkaUsername;

    @Value("${kafka.sasl.password:}")
    private String kafkaPassword;

    @Value("${kafka.ssl.password:}")
    private String sslPassword;

    @Value("${kafka.ssl.keystore.location:}")
    private String sslKeystoreLocation;

    @Value("${kafka.ssl.truststore.location:}")
    private String sslTruststoreLocation;

    public void updateSaslConfig(Map<String, Object> config) {
        if (StringUtils.hasLength(kafkaUsername) && StringUtils.hasLength(kafkaPassword)) {
            config.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
            config.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
            config.put(SslConfigs.SSL_KEY_PASSWORD_CONFIG, sslPassword);
            config.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, sslKeystoreLocation);
            config.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, sslPassword);
            config.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, sslTruststoreLocation);
            config.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, sslPassword);
            config.put(SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG, "");
            String jaasTemplate = "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"%s\" password=\"%s\";";
            String jaasCfg = String.format(jaasTemplate, kafkaUsername, kafkaPassword);
            config.put(SaslConfigs.SASL_JAAS_CONFIG, jaasCfg);
        }
    }

    public void updateSaslConfig(Properties config) {
        if (StringUtils.hasLength(kafkaUsername) && StringUtils.hasLength(kafkaPassword)) {
            config.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
            config.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
            config.put(SslConfigs.SSL_KEY_PASSWORD_CONFIG, sslPassword);
            config.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, sslKeystoreLocation);
            config.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, sslPassword);
            config.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, sslTruststoreLocation);
            config.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, sslPassword);
            config.put(SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG, "");
            String jaasTemplate = "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"%s\" password=\"%s\";";
            String jaasCfg = String.format(jaasTemplate, kafkaUsername, kafkaPassword);
            config.put(SaslConfigs.SASL_JAAS_CONFIG, jaasCfg);
        }
    }
}

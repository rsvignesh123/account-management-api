package com.management.Accounts.configs;


import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

@Slf4j
    @Configuration
    public class MongoAppConfig extends AbstractMongoClientConfiguration {
        @Value("${spring.data.mongodb.uri}")
        private String uri;

        @Override
        protected String getDatabaseName() {
            return "uzhavarnanbargal";
        }

        @Override
        public MongoClient mongoClient() {
            log.info("Mongo URI: {}", uri);
            ConnectionString connectionString = new ConnectionString(uri);
            MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .build();
            return MongoClients.create(mongoClientSettings);
        }
}

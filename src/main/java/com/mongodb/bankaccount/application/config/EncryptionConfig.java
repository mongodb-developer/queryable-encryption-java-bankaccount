package com.mongodb.bankaccount.application.config;

import com.mongodb.AutoEncryptionSettings;
import com.mongodb.ClientEncryptionSettings;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoDatabase;
import lombok.Data;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.mongodb")
public class EncryptionConfig {

    private String uri;
    private String kmsProviderName;
    private String keyVaultNamespace;
    private String encryptedDatabaseName;
    private String encryptedCollectionName;
    private String cmk;
    private String cryptSharedLibPath;

    private final Map<String, Map<String, Object>> kmsProviderCredentials = new HashMap<>();

    @Bean
    public ApplicationRunner createEncryptedCollectionRunner(MongoDatabase db, EncryptionFieldConfig fieldConfig) {
        return args -> {
            if (!fieldConfig.collectionExists(db, encryptedCollectionName)) {
                fieldConfig.createEncryptedCollection(db, clientEncryptionSettings());
            }
        };
    }

    private ClientEncryptionSettings clientEncryptionSettings() {
        return ClientEncryptionSettings.builder()
                .keyVaultMongoClientSettings(getMongoClientSettings())
                .keyVaultNamespace(keyVaultNamespace)
                .kmsProviders(kmsProviderCredentials)
                .build();
    }

    protected MongoClientSettings getMongoClientSettings() {
        kmsProviderCredentials.put("local", createLocalKmsProvider());

        AutoEncryptionSettings autoEncryptionSettings = AutoEncryptionSettings.builder()
                .keyVaultNamespace(keyVaultNamespace)
                .kmsProviders(kmsProviderCredentials)
                .extraOptions(createExtraOptions())
                .build();

        return MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(uri))
                .autoEncryptionSettings(autoEncryptionSettings)
                .build();
    }

    private Map<String, Object> createLocalKmsProvider() {
        Map<String, Object> keyMap = new HashMap<>();
        keyMap.put("key", Base64.getDecoder().decode(cmk));
        return keyMap;
    }

    private Map<String, Object> createExtraOptions() {
        Map<String, Object> extraOptions = new HashMap<>();
        extraOptions.put("cryptSharedLibPath", cryptSharedLibPath);
        return extraOptions;
    }
}

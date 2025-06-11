package com.telconova.tracking.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class StorageConfig {

    @Component
    @ConfigurationProperties(prefix = "app.storage")
    public static class StorageProperties {
        private String type;
        private AzureStorageProperties azure = new AzureStorageProperties();

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public AzureStorageProperties getAzure() {
            return azure;
        }

        public void setAzure(AzureStorageProperties azure) {
            this.azure = azure;
        }
    }

    public static class AzureStorageProperties {
        private String connectionString;
        private String containerName;
        private String accountName;
        private String accountKey;

        public String getConnectionString() {
            return connectionString;
        }

        public void setConnectionString(String connectionString) {
            this.connectionString = connectionString;
        }

        public String getContainerName() {
            return containerName;
        }

        public void setContainerName(String containerName) {
            this.containerName = containerName;
        }

        public String getAccountName() {
            return accountName;
        }

        public void setAccountName(String accountName) {
            this.accountName = accountName;
        }

        public String getAccountKey() {
            return accountKey;
        }

        public void setAccountKey(String accountKey) {
            this.accountKey = accountKey;
        }
    }
}

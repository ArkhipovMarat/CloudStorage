package ru.netology.cloud_storage.entity.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "file")
@Component
public class FileStorageProperties {
    private String upload;

    public String getUpload() {
        return upload;
    }

    public void setUpload(final String upload) {
        this.upload = upload;
    }
}

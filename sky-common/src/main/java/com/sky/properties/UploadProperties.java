package com.sky.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "sky.file.upload")
public class UploadProperties {

    /**
     * 文件上传本地路径
     */
    private String path;
}

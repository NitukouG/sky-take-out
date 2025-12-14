package com.sky.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FileUploadUtil {

    // 禁止实例化
    private FileUploadUtil() {}

    /**
     * 上传文件到本地
     *
     * @param file       前端上传的文件
     * @param basePath   本地存储根路径（如：D:/upload/）
     * @return           实际存储的文件名
     */
    public static String upload(MultipartFile file, String basePath) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("上传文件为空");
        }

        // 1. 创建目录
        File dir = new File(basePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 2. 获取原始文件名
        String originalFilename = file.getOriginalFilename();

        // 3. 获取文件后缀
        String suffix = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // 4. 生成新文件名（防止重名）
        String newFilename = UUID.randomUUID().toString() + suffix;

        // 5. 保存文件
        File dest = new File(dir, newFilename);
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            throw new RuntimeException("文件上传失败", e);
        }

        return newFilename;
    }
}

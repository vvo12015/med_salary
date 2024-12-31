package net.vrakin.medsalary.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Клас для зберігання властивостей конфігурації, пов'язаних зі сховищем файлів.
 *
 * Налаштовується через префікс `storage` у файлі конфігурації (наприклад, `application.properties` або `application.yml`).
 */
@Configuration
@ConfigurationProperties(prefix = "storage")
@Getter
@Setter
public class StorageProperties {

    /**
     * Директорія для завантаження файлів.
     *
     * Вказується у властивості `storage.uploadDir`.
     */
    private String uploadDir;

    /**
     * Робоча директорія для обробки файлів.
     *
     * Вказується у властивості `storage.workDir`.
     */
    private String workDir;
}

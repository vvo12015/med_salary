package net.vrakin.medsalary.service;

import lombok.Getter;
import net.vrakin.medsalary.config.StorageProperties;
import net.vrakin.medsalary.exception.StorageException;
import net.vrakin.medsalary.exception.StorageFileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

/**
 * Сервіс для управління зберіганням файлів на файловій системі.
 *
 * <p>Використовується для завантаження, збереження, видалення і отримання файлів
 * із директорій, визначених у властивостях додатку.</p>
 *
 * <p>Ключові можливості:
 * <ul>
 *     <li>Збереження файлів у робочій та кореневій директорії.</li>
 *     <li>Завантаження файлів як ресурсів.</li>
 *     <li>Видалення окремих файлів або очищення всієї директорії.</li>
 *     <li>Ініціалізація директорії для зберігання файлів.</li>
 * </ul>
 * </p>
 *
 * @author YourName
 * @version 1.0
 */
@Service
@Getter
public class FileSystemStorageService implements StorageService {

    /** Шлях до робочої директорії для зберігання файлів. */
    private final Path workLocation;

    /** Шлях до кореневої директорії для зберігання файлів. */
    private final Path rootLocation;

    /**
     * Конструктор для ініціалізації шляхів до директорій на основі властивостей.
     *
     * @param properties Властивості конфігурації зберігання {@link StorageProperties}.
     * @throws StorageException Якщо передані директорії пусті або некоректні.
     */
    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        if (properties.getUploadDir().trim().isEmpty()) {
            throw new StorageException("File upload location cannot be empty.");
        }
        if (properties.getWorkDir().trim().isEmpty()) {
            throw new StorageException("File work location cannot be empty.");
        }

        this.rootLocation = Paths.get(properties.getUploadDir());
        this.workLocation = Paths.get(properties.getWorkDir());
    }

    /**
     * Зберігає файл у кореневій директорії.
     *
     * @param file     Файл для збереження.
     * @param filename Ім'я файлу, за яким його буде збережено.
     * @return Об'єкт {@link File}, що представляє збережений файл.
     */
    @Override
    public File storeUploadDir(MultipartFile file, String filename) {
        return store(file, filename, this.rootLocation.toAbsolutePath());
    }

    /**
     * Зберігає файл у робочій директорії.
     *
     * @param file     Файл для збереження.
     * @param filename Ім'я файлу, за яким його буде збережено.
     * @return Об'єкт {@link File}, що представляє збережений файл.
     */
    @Override
    public File storeWorkDir(MultipartFile file, String filename) {
        return store(file, filename, this.workLocation.toAbsolutePath());
    }

    /**
     * Зберігає файл у вказаній директорії.
     *
     * @param file     Файл для збереження.
     * @param filename Ім'я файлу.
     * @param path     Директорія, у яку буде збережено файл.
     * @return Об'єкт {@link File}, що представляє збережений файл.
     * @throws StorageException У разі помилки збереження.
     */
    private File store(MultipartFile file, String filename, Path path) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }
            Path destinationFile = path.resolve(Paths.get(filename)).normalize().toAbsolutePath();

            if (!destinationFile.getParent().equals(path.toAbsolutePath())) {
                throw new StorageException("Cannot store file outside current directory.");
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }

            return destinationFile.toFile();
        } catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }

    /**
     * Завантажує всі файли у кореневій директорії.
     *
     * @return Потік шляхів до файлів.
     * @throws StorageException У разі помилки читання файлів.
     */
    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }
    }

    /**
     * Завантажує шлях до файлу за його ім'ям у кореневій директорії.
     *
     * @param filename Ім'я файлу.
     * @return Шлях до файлу.
     */
    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    /**
     * Завантажує шлях до файлу за його ім'ям у робочій директорії.
     *
     * @param filename Ім'я файлу.
     * @return Шлях до файлу.
     */
    @Override
    public Path loadFromWorkDir(String filename) {
        return workLocation.resolve(filename);
    }

    /**
     * Завантажує файл як ресурс.
     *
     * @param filename Ім'я файлу.
     * @return Об'єкт {@link Resource}, що представляє файл.
     * @throws StorageFileNotFoundException Якщо файл не знайдено або неможливо прочитати.
     */
    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException("Could not read file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    /**
     * Видаляє файл за його ім'ям.
     *
     * @param filename Ім'я файлу для видалення.
     */
    @Override
    public void delete(String filename) {
        FileSystemUtils.deleteRecursively(load(filename).toFile());
    }

    /**
     * Видаляє всі файли у кореневій директорії.
     */
    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    /**
     * Ініціалізує директорію для зберігання файлів, створюючи її, якщо вона не існує.
     *
     * @throws StorageException Якщо неможливо створити директорію.
     */
    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}
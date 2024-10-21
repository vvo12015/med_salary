package net.vrakin.medsalary.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    File store(MultipartFile file, String filename);

    Stream<Path> loadAll();

    Path load(String filename);

    Path loadFromWorkDir(String filename);

    Resource loadAsResource(String filename);

    void delete(String filename);

    void deleteAll();

}

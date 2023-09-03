package com.github.leeonky.tool;

import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TempFiles {
    private static final TempFiles TEMP_FILES = new TempFiles();
    private final String path = String.format("%s/grep-cucumber", System.getProperty("java.io.tmpdir"));

    @SneakyThrows
    private TempFiles() {
        Files.createDirectories(Paths.get(path));
    }

    public static TempFiles tempFiles() {
        return TEMP_FILES;
    }

    @SneakyThrows
    public void createWithContent(String fileName, String content) {
        Files.writeString(getAbsolutePath(fileName), content);
    }

    public void createWithContent(String fileName, byte[] content) throws IOException {
        Files.write(getAbsolutePath(fileName), content);
    }

    public Path getAbsolutePath(String fileName) {
        return Paths.get(path, fileName);
    }

    public Path getPath() {
        return Paths.get(path);
    }

    @SneakyThrows
    public String readContent(String file) {
        return new String(Files.readAllBytes(getAbsolutePath(file)));
    }

    @SneakyThrows
    public byte[] readBinaryContent(String file) {
        return Files.readAllBytes(getAbsolutePath(file));
    }

    @SneakyThrows
    public void clean() {
        cleanIfExists("");
    }

    @SneakyThrows
    public void createWithDirectoryAndContent(String dir, String fileName, String updatedContent) {
        Files.createDirectories(Path.of(path, dir));
        Files.writeString(Path.of(path, dir, fileName), updatedContent);
    }

    private void cleanIfExists(String folder) throws IOException {
        File directory = new File(path + folder);
        if (directory.exists()) {
            FileUtils.cleanDirectory(directory);
        } else {
            directory.mkdirs();
        }
    }

    @SneakyThrows
    public void createDirectory(String dir) {
        Files.createDirectories(Path.of(path, dir));
    }
}

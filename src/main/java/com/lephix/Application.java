package com.lephix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.env.Environment;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Main Application
 *
 * Created by longxiang on 15/12/15.
 */
@EnableAutoConfiguration
public class Application implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    Environment environment;
    int count = 0;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        File sourceFolder = new File(environment.getProperty("source.folder"));
        final String targetFolder = environment.getProperty("target.folder");
        final Pattern pattern = Pattern.compile(environment.getProperty("file.pattern"));

        if (!sourceFolder.isDirectory()) {
            LOG.error("source.folder is not a Folder {} ", sourceFolder.getAbsolutePath());
            return;
        }

        try {
            Path sourcePath = FileSystems.getDefault().getPath(sourceFolder.getAbsolutePath());

            Files.walkFileTree(sourcePath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Matcher matcher = pattern.matcher(file.toString());
                    if (matcher.matches()) {
                        count++;
                        File destFile = new File(targetFolder + File.separator + file.getFileName());

                        System.out.println("Start copying file from " + file.toAbsolutePath() + " to " + destFile.getAbsolutePath());
                        FileCopyUtils.copy(file.toFile(), destFile);
                        System.out.println("Copy finished.");
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    if (exc instanceof NoSuchFileException) {
                        LOG.warn("Found a potential missing visit branch {}", file.toString());
                    } else {
                        LOG.error("Found a UNKNOWN error, but let's continue.");
                    }
                    return FileVisitResult.CONTINUE;
                }
            });

            System.out.println(count + " files have been copied.");
        } catch (Exception e) {
            LOG.error("Find error during visiting and copying files", e);
        }
    }


    @Autowired
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}

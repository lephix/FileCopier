package com.lephix.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * Created by longxiang on 16/1/4.
 */
public class MTSCommand implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(MTSCommand.class);

    private String source;
    private String target;
    private Pattern pattern;
    private int count = 0;

    public MTSCommand(String source, String target, Pattern pattern) {
        this.source = source;
        this.target = target;
        this.pattern = pattern;
    }

    @Override
    public void run() {
        File sourceFolder = new File(source);

        if (!sourceFolder.isDirectory()) {
            LOG.error("source is not a Folder {} ", sourceFolder.getAbsolutePath());
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
                        File destFile = new File(target + File.separator + file.getFileName());

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
}

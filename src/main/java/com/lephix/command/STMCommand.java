package com.lephix.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * Created by longxiang on 16/1/4.
 */
public class STMCommand implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(STMCommand.class);

    private String source;
    private String target;
    private Pattern pattern;
    private int count = 0;

    public  STMCommand(String source, String target, Pattern pattern) {
        this.source = source;
        this.target = target;
        this.pattern = pattern;
    }

    @Override
    public void run() {
        File targetFolder = new File(target);

        if (!targetFolder.isDirectory()) {
            LOG.error("target is not a folder {} ", targetFolder.getAbsolutePath());
        }

        try {
            File sourceFile = new File(source);
            File[] files = targetFolder.listFiles();
            assert files != null;

            for (File file : files) {
                Matcher matcher = pattern.matcher(file.toString());
                if (!matcher.matches()) {
                    continue;
                }
                if (sourceFile.isFile()) {
                    File targetFile = new File(file.getAbsoluteFile() + File.separator + sourceFile.getName());
                    FileSystemUtils.copyRecursively(sourceFile, targetFile);
                    System.out.println("copied from " + sourceFile.getAbsolutePath() + " to " + targetFile.getAbsolutePath());
                } else if (sourceFile.isDirectory()){
                    FileSystemUtils.copyRecursively(sourceFile, file);
                    System.out.println("copied from " + sourceFile.getAbsolutePath() + " to " + file.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            LOG.error("Find error during visiting and copying files", e);
        }
    }
}

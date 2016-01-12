package com.lephix;

import com.lephix.command.MTSCommand;
import com.lephix.command.STMCommand;
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
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String command = environment.getProperty("command");

        String source = environment.getProperty("source");
        final String target = environment.getProperty("target");
        final Pattern pattern = Pattern.compile(environment.getProperty("file.pattern"));

        switch (command) {
            case "mts":
                MTSCommand mtsCommand = new MTSCommand(source, target, pattern);
                mtsCommand.run();
                break;
            case "stm":
                STMCommand stmCommand = new STMCommand(source, target, pattern);
                stmCommand.run();
                break;
            default:
                LOG.error("No command is recognized.");
                break;
        }

    }


    @Autowired
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}

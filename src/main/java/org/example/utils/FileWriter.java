package org.example.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileWriter {
    private static String logFilePath = "logs/application.log"; // Default log file path
    private static FileWriter instance;


    private FileWriter() {}

    // Singleton instance getter
    public static FileWriter getInstance() {
        if (instance == null) {
            synchronized (FileWriter.class) {
                if (instance == null) {
                    instance = new FileWriter();
                }
            }
        }
        return instance;
    }

    /**
     * Allows setting a custom log file path.
     * @param path The path to the log file.
     */
    public static void setLogFilePath(String path) {
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("Log file path cannot be null or blank.");
        }
        logFilePath = path;
    }

    // Synchronized method to write log messages to the file
    public synchronized void writeLog(String message) {
        ensureLogDirectoryExists();
        try (BufferedWriter writer = new BufferedWriter(new java.io.FileWriter(logFilePath, true))) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            writer.write(timestamp + " - " + message);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Failed to write log: " + e.getMessage());
        }
    }

    // Ensure that the directory for the log file exists
    private void ensureLogDirectoryExists() {
        File logFile = new File(logFilePath);
        File parentDir = logFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                System.err.println("Failed to create log directory: " + parentDir.getAbsolutePath());
            }
        }
    }
}

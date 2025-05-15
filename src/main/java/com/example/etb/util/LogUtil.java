package com.example.etb.util;

import org.springframework.stereotype.Component;

@Component
public class LogUtil {

    /**
     * Logs a success message with a custom tag.
     * @param tag - The tag for identification.
     * @param message - The success message to log.
     */
    public void logSuccess(String tag, String message) {
        String formattedMessage = "[SUCCESS] [" + tag + "] " + message;
        System.out.println(formattedMessage);
    }

    /**
     * Logs an error message with a custom tag.
     * @param tag - The tag for identification.
     * @param message - The error message to log.
     */
    public void logError(String tag, String message) {
        String formattedMessage = "[ERROR] [" + tag + "] " + message;
        System.err.println(formattedMessage);
    }

    /**
     * Logs a debug message with a custom tag.
     * @param tag - The tag for identification.
     * @param message - The debug message to log.
     */
    public void logDebug(String tag, String message) {
        String formattedMessage = "[DEBUG] [" + tag + "] " + message;
        System.out.println(formattedMessage);
    }
}

package com.app.Kmail.util;

public final class FileNameUtil {

    private FileNameUtil() {
        // Prevent instantiation
    }

    /**
     * Extracts the file name from the S3 key.
     *
     * @param key the S3 object key
     * @return the file name
     */
    public static String extractFileNameFromKey(String key) {
        return key.substring(key.lastIndexOf('/') + 1);
    }

    /**
     * Extracts the prefix (name without extension) from a file name.
     *
     * @param fileName the full file name
     * @return the prefix of the file name
     */
    public static String extractPrefix(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
    }

    /**
     * Extracts the suffix (extension including the dot) from a file name.
     *
     * @param fileName the full file name
     * @return the suffix of the file name, or an empty string if none found
     */
    public static String extractSuffix(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex);
    }
}

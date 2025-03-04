package com.app.Kmail.exceptions;

public class DbSchemaVersionMismatchException extends RuntimeException {
    public DbSchemaVersionMismatchException(String message) {
        super(message);
    }
}
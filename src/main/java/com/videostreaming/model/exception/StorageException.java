package com.videostreaming.model.exception;

public class StorageException extends RuntimeException {

    private static final String EXCEPTION_MESSAGE = "Storage exception: %s";

    public StorageException(String message) {
        super(String.format(EXCEPTION_MESSAGE, message));
    }
}

package com.qiwi.servlet;

/**
 * Created by etrofimov on 12.07.17.
 */
public enum Status {
    OK (0),
    EXISTS (1),
    WRONG_FORMAT (2),
    BAD_PASSWORD (3),
    NOT_EXISTS (4),
    OTHER (5),
    WRONG_PASSWORD (6),
    NOT_FOUND (7);

    private final int statusCode;

    Status(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}

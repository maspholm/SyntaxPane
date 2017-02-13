package com.syntax.manage;
/**
 * Represent exception during working with com.syntax
 */
public class SyntaxException extends Exception {
    private static final long serialVersionUID = 0;
    public SyntaxException() {
        super();
    }
    public SyntaxException(String message) {
        super(message);
    }
    public SyntaxException(String message, Throwable cause) {
        super(message, cause);
    }
    public SyntaxException(Throwable cause) {
        super(cause);
    }
}
package com.encryption;

public class TooManyBitsException extends Exception{
    public TooManyBitsException() {
        super();
    }

    public TooManyBitsException(String message) {
        super(message);
    }

    public TooManyBitsException(String message, Throwable cause) {
        super(message, cause);
    }

    public TooManyBitsException(Throwable cause) {
        super(cause);
    }
}

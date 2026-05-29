package com.msa4hipgram.global.errors.custom;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
      super(message);
    }
}

package com.msa4hipgram.global.errors.custom;

public class DeletedRecordException extends RuntimeException {
    public DeletedRecordException(String message) {
        super(message);
    }
}

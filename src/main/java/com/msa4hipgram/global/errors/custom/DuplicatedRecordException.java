package com.msa4hipgram.global.errors.custom;

public class DuplicatedRecordException extends RuntimeException {
    // 커스텀 에러는 보통 RuntimeException을 상속 받는다.
    // RuntimeException -> unchecked , Exception -> checked

    public DuplicatedRecordException(String message) {
        super(message);
    }
}

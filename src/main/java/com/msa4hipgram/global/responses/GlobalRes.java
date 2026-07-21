package com.msa4hipgram.global.responses;

import com.msa4hipgram.global.responses.constant.CustomResponseCode;

public record GlobalRes<T>(
    String code
    , String message
    , T data
) {
    public static <T> GlobalRes<T> from(CustomResponseCode customResponseCode, T data) {
        return new GlobalRes<T>(customResponseCode.getCode(), customResponseCode.name(), data);
    }

    public static GlobalRes<Void> from(CustomResponseCode customResponseCode) {
        return new GlobalRes<Void>(customResponseCode.getCode(), customResponseCode.name(), null);
    }

    public static <T> GlobalRes<T> success(T data) {
        return GlobalRes.<T>from(CustomResponseCode.SUCCESS, data);
        //return new GlobalRes<T>(CustomResponseCode.SUCCESS.getCode(), CustomResponseCode.SUCCESS.name(), data);
    }

    public static GlobalRes<Void> success() {
        return GlobalRes.<Void>from(CustomResponseCode.SUCCESS);
        //return new GlobalRes<Void>(CustomResponseCode.SUCCESS.getCode(), CustomResponseCode.SUCCESS.name(), null);
    }
}

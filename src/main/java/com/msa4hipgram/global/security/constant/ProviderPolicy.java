package com.msa4hipgram.global.security.constant;

import lombok.Getter;

@Getter
public enum ProviderPolicy {
    // private ProviderPolicy NONE = new ProviderPolicy("NONE");
    NONE("NONE")
    , KAKAO("KAKAO")
    , GOOGLE("GOOGLE");

    private final String provider;

    ProviderPolicy(String provider) {
        this.provider = provider;
    }

}

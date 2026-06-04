package com.msa4hipgram.global.util.file;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "file") // 최상위 경로(?)
public record FileConfig(
    String serverUri
    , String storagePath
    , String profilePath
    , String postPath
    , List<String> allowExtensionList
    ) {
}

package com.msa4hipgram.domain.file.responses;

import lombok.Builder;

@Builder
public record FileRes(
    String fileUri
) {
}

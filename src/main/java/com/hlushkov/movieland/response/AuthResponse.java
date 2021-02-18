package com.hlushkov.movieland.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String userUUID;
    private String nickname;
}

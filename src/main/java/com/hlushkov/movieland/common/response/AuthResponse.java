package com.hlushkov.movieland.common.response;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    private String userUUID;
    private String nickname;
}

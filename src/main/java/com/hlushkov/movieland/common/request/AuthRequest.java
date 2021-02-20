package com.hlushkov.movieland.common.request;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
}

package com.hlushkov.movieland.security.entity;

import com.hlushkov.movieland.entity.User;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class Session {
    String userUUID;
    User user;
    LocalDateTime expireDate;
}

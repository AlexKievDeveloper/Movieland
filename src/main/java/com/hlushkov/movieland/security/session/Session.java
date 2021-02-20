package com.hlushkov.movieland.security.session;

import com.hlushkov.movieland.entity.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Session {
    private String userUUID;
    private User user;
    private LocalDateTime expireDate;
}

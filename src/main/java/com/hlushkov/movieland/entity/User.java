package com.hlushkov.movieland.entity;

import com.hlushkov.movieland.common.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private int id;
    private String nickname;
    private String email;
    private String password;
    private String salt;
    private Role role;
}

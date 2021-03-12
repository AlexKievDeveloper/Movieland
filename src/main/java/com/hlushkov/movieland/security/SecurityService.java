package com.hlushkov.movieland.security;

import com.hlushkov.movieland.common.request.AuthRequest;
import com.hlushkov.movieland.entity.User;
import com.hlushkov.movieland.security.entity.Session;

import java.util.Optional;

public interface SecurityService {

    Optional<Session> login(AuthRequest authRequest);

    boolean removeSession(String userUUID);

    Optional<User> getUserByUUID(String userUUID);

}

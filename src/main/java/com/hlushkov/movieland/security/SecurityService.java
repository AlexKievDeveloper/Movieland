package com.hlushkov.movieland.security;

import java.util.Optional;

public interface SecurityService {

    Optional<Session> login(String email, String password);

    boolean removeSession(String userUUID);

    Optional<Session> getSession(String userUUID);
}

package com.hlushkov.movieland.dao;

import com.hlushkov.movieland.entity.User;

import java.util.Optional;

public interface UserDao {

    Optional<User> findByEmail(String userEmail);
}

package com.hlushkov.movieland.dao;

import com.hlushkov.movieland.entity.User;

import java.util.Optional;

public interface UserDao {

    User findByEmail(String userEmail);
}

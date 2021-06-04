package com.hlushkov.movieland.dao;

import com.hlushkov.movieland.entity.User;

public interface UserDao {

    User findByEmail(String userEmail);

}

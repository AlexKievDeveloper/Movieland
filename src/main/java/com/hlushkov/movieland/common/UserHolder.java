package com.hlushkov.movieland.common;

import com.hlushkov.movieland.entity.User;

public class UserHolder {

    private static final ThreadLocal<User> userThreadLocal = new ThreadLocal<>();

    public final static User getUser() {
        return userThreadLocal.get();
    }

    public final static void setUser(User user) {
        userThreadLocal.set(user);
    }

}

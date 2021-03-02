package com.hlushkov.movieland.security.util;

import com.hlushkov.movieland.entity.User;

public class UserHolder {

    private static final ThreadLocal<User> USER_THREAD_LOCAL = new ThreadLocal<>();

    public static User getUser() {
        return USER_THREAD_LOCAL.get();
    }

    public static void setUser(User user) {
        USER_THREAD_LOCAL.set(user);
    }

    public static void removeUser() {
        USER_THREAD_LOCAL.remove();
    }

}

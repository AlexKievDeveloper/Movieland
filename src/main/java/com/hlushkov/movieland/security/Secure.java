package com.hlushkov.movieland.security;

import com.hlushkov.movieland.common.Role;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Secure {
    Role[] value();
}

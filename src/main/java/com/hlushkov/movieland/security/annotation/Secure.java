package com.hlushkov.movieland.security.annotation;

import com.hlushkov.movieland.common.Role;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Secure {
    Role[] value();
}

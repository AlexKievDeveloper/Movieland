package com.hlushkov.movieland.entity;

public enum SortDirection {
    DESC("DESC"), ASC("ASC");

    private final String direction;

    SortDirection(String direction) {
        this.direction = direction;
    }

    public String getDirection() {
        return direction;
    }
}

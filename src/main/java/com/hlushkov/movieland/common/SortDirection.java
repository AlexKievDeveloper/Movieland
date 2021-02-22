package com.hlushkov.movieland.common;

public enum SortDirection {

    DESC("desc"), ASC("asc");

    private final String direction;

    SortDirection(String direction) {
        this.direction = direction;
    }

    public String getDirection() {
        return direction;
    }

    public static SortDirection getSortDirection(String name) {
        SortDirection[] sortDirections = SortDirection.values();
        for (SortDirection sortDirection : sortDirections) {
            if (sortDirection.getDirection().equals(name)) {
                return sortDirection;
            }
        }

        throw new IllegalArgumentException("No direction for name ".concat(name));
    }
}
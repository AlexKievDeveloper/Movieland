package com.hlushkov.movieland.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Movie {
    private int id;
    private String nameRussian;
    private String nameNative;
    private String description;
    private int yearOfRelease;
    private double rating;
    private double price;
    private String picturePath;
}

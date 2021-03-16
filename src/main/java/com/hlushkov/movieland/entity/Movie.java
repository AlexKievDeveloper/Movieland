package com.hlushkov.movieland.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Movie implements Cloneable {
    private int id;
    private String nameRussian;
    private String nameNative;
    private String description;
    private int yearOfRelease;
    private double rating;
    private double price;
    private String picturePath;

    @Override
    public Movie clone() {
        return Movie.builder()
                .id(this.getId())
                .nameRussian(this.getNameRussian())
                .nameNative(this.getNameNative())
                .description(this.getDescription())
                .yearOfRelease(this.getYearOfRelease())
                .rating(this.getRating())
                .price(this.getPrice())
                .picturePath(this.getPicturePath())
                .build();
    }
}

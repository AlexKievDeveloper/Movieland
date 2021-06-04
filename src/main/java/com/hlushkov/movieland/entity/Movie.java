package com.hlushkov.movieland.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.Value;

import java.util.Map;

@Value
@Builder
public class Movie implements Cloneable {
    int id;
    String nameRussian;
    String nameNative;
    String description;
    int yearOfRelease;
    double rating;
    double price;
    String picturePath;

    @JsonCreator
    public static Movie create(Map<String, Object> object) {
        return Movie.builder()
                .nameRussian((String) object.get("nameRussian"))
                .nameNative((String) object.get("nameNative"))
                .description((String) object.get("description"))
                .yearOfRelease((int) object.get("yearOfRelease"))
                .rating((double) object.get("rating"))
                .price((double) object.get("price"))
                .picturePath((String) object.get("picturePath"))
                .build();
    }

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

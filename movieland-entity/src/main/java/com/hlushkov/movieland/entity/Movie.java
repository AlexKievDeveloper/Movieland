package com.hlushkov.movieland.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Movie {
    int id;
    String nameRussian;
    String nameNative;
    int yearOfRelease;
    double rating;
    double price;
    String picturePath;
}

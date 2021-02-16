package com.hlushkov.movieland.dto;

import com.hlushkov.movieland.entity.Country;
import com.hlushkov.movieland.entity.Genre;
import com.hlushkov.movieland.entity.Review;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MovieWithDetails {
    private int id;
    private String nameRussian;
    private String nameNative;
    private String description;
    private int yearOfRelease;
    private double rating;
    private double price;
    private String picturePath;
    private List<Genre> genres;
    private List<Country> countries;
    private List<Review> reviews;
}

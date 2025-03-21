package com.jp.testingapp.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Movie {

    private Long movie_id;

    private String name;

    private Integer year;

    private String cast;

    private LocalDate release_date;
}


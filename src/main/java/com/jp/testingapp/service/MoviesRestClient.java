package com.jp.testingapp.service;

import com.jp.testingapp.model.Movie;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static com.jp.testingapp.config.AppConstants.GET_ALL_MOVIES_V1;

@Service
public class MoviesRestClient {

    private WebClient webClient;
    private List<Movie> movieList;

    public List<Movie> getAlMoviesList() {
        // http://localhost:8081/movieservice/swagger-ui.html#/movies-controller/allMoviesUsingGET
        // http://localhost:8081/movieservice/v1/allMovies
        return webClient.get().uri(GET_ALL_MOVIES_V1)
                .retrieve()
                .bodyToFlux(Movie.class)
                .collectList()
                .block();
    }
}
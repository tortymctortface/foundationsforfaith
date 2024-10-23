package com.edee.foundationsforfaith.services.impl;

import com.edee.foundationsforfaith.entities.Movie;
import com.edee.foundationsforfaith.repositories.MovieRepository;
import com.edee.foundationsforfaith.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public List<Movie> getAllMovies(){
        return movieRepository.findAll();
    }
}

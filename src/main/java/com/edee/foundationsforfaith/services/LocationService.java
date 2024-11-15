package com.edee.foundationsforfaith.services;

import com.edee.foundationsforfaith.entities.Location;

import java.util.List;
import java.util.Optional;

public interface LocationService {

    List<Location> getAllLocations();
    Optional<List<Location>> getLocationsByCountry(String country);
    Location findOrCreateLocation (String country, String area);
}

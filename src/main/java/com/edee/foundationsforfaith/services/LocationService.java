package com.edee.foundationsforfaith.services;

import com.edee.foundationsforfaith.entities.Location;

public interface LocationService {
    Location createLocation(String country, String area);
}

package com.edee.foundationsforfaith.services.impl;
import com.edee.foundationsforfaith.entities.Location;
import com.edee.foundationsforfaith.repositories.LocationRepository;
import com.edee.foundationsforfaith.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class LocationServiceImpl implements LocationService{

    @Autowired
    private LocationRepository locationRepository;
//
//    @Autowired
//    private MongoTemplate mongoTemplate;

    public Location createLocation (String country, String area){
        Location location = new Location();
        location.setCountry(country);
        location.setAreaName(area);
        return locationRepository.insert(location);
    }
}

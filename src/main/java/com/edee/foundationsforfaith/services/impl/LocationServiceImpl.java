package com.edee.foundationsforfaith.services.impl;
import com.edee.foundationsforfaith.entities.Location;
import com.edee.foundationsforfaith.entities.Project;
import com.edee.foundationsforfaith.repositories.LocationRepository;
import com.edee.foundationsforfaith.services.LocationService;
import org.bson.types.ObjectId;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationServiceImpl implements LocationService{

    @Autowired
    private LocationRepository locationRepository;

    @Override
    public List<Location> getAllLocations(){
        return locationRepository.findAll();
    }

    @Override
    public Optional<List<Location>> getLocationsByCountry(String country){
        return locationRepository.findLocationsByCountry(country);
    }

    @Override
    public Location findOrCreateLocation (String country, String area){
        Optional<Location> existingLocation = locationRepository.findLocationByCountryAndArea(country, area);
        if(existingLocation.isPresent()){
            return existingLocation.get();
        } else{
            Location location = new Location(null,(Jsoup.clean(country, Safelist.basic())), (Jsoup.clean(area, Safelist.basic())), null);
            return locationRepository.insert(location);
        }
    }
}

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

    public List<Location> getAllLocations(){
        return locationRepository.findAll();
    }

    public Optional<List<Location>> getLocationsByCountry(String country){
        return locationRepository.findLocationsByCountry(country);
    }

//    public Optional<Location> getLocationByCountryAndArea(String country, String area){
//        return locationRepository.findLocationByCountryAndArea(country, area);
//    }

    public Location findOrCreateLocation (String country, String area){
        Optional<Location> existingLocation = locationRepository.findLocationByCountryAndArea(country, area);
        if(existingLocation.isPresent()){
            return existingLocation.get();
        } else{
            Location location = new Location();
            location.setCountry(Jsoup.clean(country, Safelist.basic()));
            location.setArea(Jsoup.clean(area, Safelist.basic()));
            return locationRepository.insert(location);
        }
    }

//    public Location createOrUpdateLocation (String country, String area, ObjectId objectId){
//        Optional<Location> location = getLocationByCountryAndArea(country, area);
//        if(location.isPresent()){
//            location.
//        }else{
//            location.setCountry(country);
//            location.setArea(area);
//        }
//
//        return locationRepository.insert(location);
//    }
}

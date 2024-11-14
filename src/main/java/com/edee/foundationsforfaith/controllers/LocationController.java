package com.edee.foundationsforfaith.controllers;

import com.edee.foundationsforfaith.entities.Location;
import com.edee.foundationsforfaith.entities.Project;
import com.edee.foundationsforfaith.services.LocationService;
import org.bson.types.ObjectId;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class LocationController {

    @Autowired
    LocationService locationService;

    @GetMapping("/locations")
    public ResponseEntity<List<Location>> allLocations(){
        return new ResponseEntity<List<Location>>(locationService.getAllLocations(), HttpStatus.OK);
    }

    @GetMapping("/locations/{country}")
    public ResponseEntity<Optional<List<Location>>> allLocations(@PathVariable String country){
        return new ResponseEntity<Optional<List<Location>>>(locationService.getLocationsByCountry(country), HttpStatus.OK);
    }

    @PostMapping("/location/{country}/{area}")
    public ResponseEntity<Location> createNewLocation (@PathVariable String country, @PathVariable String area){
        String safeCountry = Jsoup.clean(country, Safelist.basic());
        String safeArea = Jsoup.clean(area, Safelist.basic());
        return new ResponseEntity<Location>((locationService.createLocation(safeCountry,safeArea)), HttpStatus.CREATED);
    }
}

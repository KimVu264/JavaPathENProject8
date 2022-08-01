package gpsUtil.controller;

import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import gpsUtil.service.GpsUtilService;

import java.util.List;
import java.util.UUID;

@RestController
public class GpsUtilController {

    private final Logger logger = LoggerFactory.getLogger(GpsUtilController.class);
    @Autowired
    GpsUtilService gpsUtilService;

    @GetMapping("attractions")
    public List<Attraction> getAttraction(){
        logger.debug(" get all attractions request");
        return gpsUtilService.getAttractions();
    }

    @GetMapping("userLocation")
    public VisitedLocation getUserLocation(@RequestParam UUID userID )  {
        logger.debug(" get location for user id: {} request",userID);
        return gpsUtilService.getUserLocation(userID);
    }
}

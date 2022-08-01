package gpsUtil.service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GpsUtilService {

    private final Logger logger = LoggerFactory.getLogger("GpsUtilServiceLog");
    private final GpsUtil gpsUtil = new GpsUtil();

    /**
     * getUserLocation to Get the user location
     *
     * @param userId User id
     * @return visitedLocation from Jar
     */
    public VisitedLocation getUserLocation(UUID userId) {
        logger.info("Service visited location for userId : {}", userId);
        return gpsUtil.getUserLocation(userId);
    }

    /**
     * getUserLocation to Get the user location
     *
     * @return List of all attraction from Jar
     */
    public List<Attraction> getAttractions() {
        logger.info("Service getAllAttractions from gpsUtil");
        return gpsUtil.getAttractions();
    }
}

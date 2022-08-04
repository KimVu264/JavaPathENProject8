package tourGuide.service;

import common.model.Attraction;
import common.model.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourGuide.repository.GpsRepository;

import java.util.List;
import java.util.UUID;

@Service
public class GpsUtilService {
    private final Logger logger = LoggerFactory.getLogger(TourGuideService.class);
    private final GpsRepository gpsRepository;

    @Autowired
    public GpsUtilService(GpsRepository gpsRepository) {
        this.gpsRepository = gpsRepository;
    }

    public List<Attraction> getAttractions() {
        //logger.debug("getting attractions ");
        return gpsRepository.getAttractionList();
    }

    public VisitedLocation getUserLocation(UUID userId) {
        //logger.debug("getting User Location for user ID: {}",userId);
        return gpsRepository.getUserLocation(userId);
    }
}

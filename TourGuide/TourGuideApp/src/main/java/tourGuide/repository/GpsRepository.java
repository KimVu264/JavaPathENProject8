package tourGuide.repository;

import common.model.Attraction;
import common.model.VisitedLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import tourGuide.proxy.GpsUtilProxy;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class GpsRepository {

    private List<Attraction> attractionList;
    private final GpsUtilProxy gpsUtilProxy;

    @Autowired
    public GpsRepository(GpsUtilProxy gpsUtilProxy) {
        this.gpsUtilProxy = gpsUtilProxy;
        attractionList = gpsUtilProxy.getAttractions();
    }

    public List<Attraction> getAttractionList() {
        return attractionList;
    }

    public VisitedLocation getUserLocation(UUID userId) {
        return gpsUtilProxy.getUserLocation(userId);
    }
}

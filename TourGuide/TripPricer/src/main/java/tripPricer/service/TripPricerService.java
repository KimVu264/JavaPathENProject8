package tripPricer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tripPricer.Provider;
import tripPricer.TripPricer;

import java.util.List;
import java.util.UUID;

@Service
public class TripPricerService {

    private final TripPricer tripPricer;
    public static final String TRIP_PRICER_API_KEY = "test-server-api-key";

    @Autowired
    public TripPricerService(TripPricer tripPricer) {
        this.tripPricer = tripPricer;
    }

    public List<Provider> getPricer(UUID userId, int numberOfAdults, int numberOfChildren, int tripDuration, int rewardPoints ) {

        return tripPricer.getPrice(TRIP_PRICER_API_KEY, userId, numberOfAdults, numberOfChildren, tripDuration, rewardPoints);
    }
}

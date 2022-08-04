package tripPricer.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tripPricer.Provider;
import tripPricer.TripPricer;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class TripPricerServiceTest {

    private final TripPricer tripPricer = new TripPricer();

    String tripPricerApiKey = "test-server-api-key";
    UUID uuid = UUID.randomUUID();
    int adults = 1;
    int children = 0;
    int nightsStay = 7;
    int rewardsPoints = 10;

    @Test
    void getTripDealsCallTest() {
        List<Provider> result = tripPricer.getPrice(tripPricerApiKey,uuid,adults,children,nightsStay,rewardsPoints);
        assertNotNull(result);
    }
}

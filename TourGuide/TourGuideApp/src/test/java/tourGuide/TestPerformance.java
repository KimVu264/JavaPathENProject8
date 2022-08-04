package tourGuide;

import common.model.Attraction;
import common.model.User;
import common.model.VisitedLocation;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tourGuide.helper.InternalTestHelper;
import tourGuide.helper.InternalTestingData;
import tourGuide.proxy.RewardCentralProxy;
import tourGuide.proxy.TripPricerProxy;
import tourGuide.repository.GpsRepository;
import tourGuide.repository.UserRepository;
import tourGuide.service.GpsUtilService;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.service.UserService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TestPerformance {

    /*
     * A note on performance improvements:
     *
     *     The number of users generated for the high volume tests can be easily adjusted via this method:
     *
     *     		InternalTestHelper.setInternalUserNumber(100000);
     *
     *
     *     These tests can be modified to suit new solutions, just as long as the performance metrics
     *     at the end of the tests remains consistent.
     *
     *     These are performance metrics that we are trying to hit:
     *
     *     highVolumeTrackLocation: 100,000 users within 15 minutes:
     *     		assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
     *
     *     highVolumeGetRewards: 100,000 users within 20 minutes:
     *          assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
     */
    static final Logger logger = LogManager.getLogger("TestPerformance");

    @Autowired
    InternalTestingData internalTestingData;
    @Autowired
    private GpsUtilService gpsUtilService;
    @Autowired
    private UserService userService;
    @Autowired
    private TripPricerProxy tripPricerProxy;
    @Autowired
    private RewardsService rewardsService;
    @Autowired
    private TourGuideService tourGuideService;
    @Autowired
    private UserRepository userRepository;
    private StopWatch stopWatch;

    @BeforeEach
    public void setUp() {
        Locale.setDefault(new Locale.Builder().setLanguage("en").setRegion("US").build());
        stopWatch = new StopWatch();
        internalTestingData = new InternalTestingData(userRepository);
        // Users should be incremented up to 100,000, and test finishes within 15 minutes
        InternalTestHelper.setInternalUserNumber(100000);
        tourGuideService = new TourGuideService(rewardsService,userService, gpsUtilService, tripPricerProxy, internalTestingData);
    }

    @Test
    public void highVolumeTrackLocation() {

        List<User> allUsers = tourGuideService.getAllUsers();

        stopWatch.start();

        List<CompletableFuture<?>> tasksFuture = new ArrayList<>();
        allUsers.forEach(u -> tasksFuture.add(tourGuideService.trackUserLocation(u.getUserId())));
        tasksFuture.forEach(CompletableFuture::join);

        assertTrue(allUsers.get(0).getVisitedLocations().size()>1);
        assertEquals(100000, allUsers.size());

        stopWatch.stop();

        logger.info("highVolumeTrackLocation: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
        assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
    }

    @Test
    public void highVolumeGetRewards() {

        Attraction attraction = gpsUtilService.getAttractions().get(0);
        List<User> allUsers;
        allUsers = tourGuideService.getAllUsers();
        allUsers.forEach(u -> u.addToVisitedLocations(new VisitedLocation(u.getUserId(), attraction, new Date())));

        stopWatch.start();

        CompletableFuture<?>[] completableFutures = allUsers.parallelStream().map(rewardsService::calculateRewards).toArray(CompletableFuture[]::new);
        CompletableFuture.allOf(completableFutures).join();

        for (User userDto : allUsers) {
            assertNotEquals(0, userDto.getUserRewards().get(0).getRewardPoints());
            assertTrue(userDto.getUserRewards().size() > 0);
        }

        stopWatch.stop();

        logger.info("highVolumeGetRewards / Time Elapsed: {}"  + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + "seconds.");
        assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
    }
}

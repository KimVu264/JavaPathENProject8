package tourGuide;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import rewardCentral.RewardCentral;
import tourGuide.helper.InternalTestHelper;
import tourGuide.helper.InternalTestingData;
import tourGuide.model.User;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

@ActiveProfiles("test")
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
    /**
     * Create ThreadPool of 100
     */
    ExecutorService executor = Executors.newFixedThreadPool(100);
    @Autowired
    InternalTestingData internalTestingData;
    @Autowired
    private GpsUtil gpsUtil;
    @Autowired
    private RewardCentral rewardCentral;
    @Autowired
    private RewardsService rewardsService;
    @Autowired
    private TourGuideService tourGuideService;
    private StopWatch stopWatch;

    @Before
    public void setUp() {
        Locale.setDefault(new Locale.Builder().setLanguage("en").setRegion("US").build());
        stopWatch = new StopWatch();
        gpsUtil = new GpsUtil();
        rewardCentral = new RewardCentral();
        rewardsService = new RewardsService(gpsUtil, new RewardCentral());
        internalTestingData = new InternalTestingData();
        // Users should be incremented up to 100,000, and test finishes within 15 minutes
        InternalTestHelper.setInternalUserNumber(1000);
        tourGuideService = new TourGuideService(gpsUtil, rewardsService, rewardCentral, internalTestingData);

    }

    @Test
    public void highVolumeTrackLocation() {

        List<User> allUserDtos = tourGuideService.getAllUsers();

        // Create a list of future
        List<CompletableFuture> completableFutures = new ArrayList<>();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        for (User userDto : allUserDtos) {
            CompletableFuture<Void> allFutures = CompletableFuture.runAsync(() -> {
                tourGuideService.trackUserLocation(userDto);
            });
            completableFutures.add(allFutures);
        }

        // Create a combined Future using allOf() and method join() to return the results
        CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[completableFutures.size()])).join();

        stopWatch.stop();

        System.out.println("highVolumeTrackLocation: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
        assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
    }

    @Test
    public void highVolumeGetRewards() {

        Attraction attraction = gpsUtil.getAttractions().get(0);
        List<User> allUsers;
        List<CompletableFuture> completableFutures = new ArrayList<>();
        allUsers = tourGuideService.getAllUsers();
        allUsers.forEach(u -> u.addToVisitedLocations(new VisitedLocation(u.getUserId(), attraction, new Date())));

        stopWatch.start();
        allUsers.forEach(u -> {completableFutures.add(rewardsService.calculateRewards(u));});

        CompletableFuture<Void> completableFuture = CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[completableFutures.size()]));
        completableFuture.join();

        for (User userDto : allUsers) {
            assertTrue(userDto.getUserRewards().size() > 0);
        }

        stopWatch.stop();

        logger.info("highVolumeGetRewards / Time Elapsed: {} seconds.", TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
        assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
    }
}

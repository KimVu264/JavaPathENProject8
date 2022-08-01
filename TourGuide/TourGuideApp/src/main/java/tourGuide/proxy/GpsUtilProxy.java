package tourGuide.proxy;

import common.model.Attraction;
import common.model.VisitedLocation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Service
@FeignClient(value="microservice-gpsUtil", url="localhost:9000")
public interface GpsUtilProxy {

    @GetMapping("/userLocation")
    VisitedLocation getUserLocation (@RequestParam("userId") UUID userId);

    @GetMapping("/attractions")
    List<Attraction> getAttractions();
}

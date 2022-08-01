package tripPricer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tripPricer.TripPricer;

import java.util.Locale;

@Configuration
public class TripPricerConfig {

    @Bean
    public Locale getLocale() {
        Locale.setDefault(Locale.US);
        return Locale.getDefault();
    }

    @Bean
    public TripPricer getTripPricer() {
        return new TripPricer();
    }
}

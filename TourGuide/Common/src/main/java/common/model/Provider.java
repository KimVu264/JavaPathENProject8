package common.model;

import java.util.UUID;

public class Provider {
    private  String name;
    private  double price;
    private UUID tripPriceId;

    public Provider(String name, double price, UUID tripId) {

        this.name   = name;
        this.price  = price;
        this.tripPriceId = tripId;
    }

    public Provider() {

    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public double getPrice() {

        return price;
    }

    public void setPrice(double price) {

        this.price = price;
    }

    public UUID getTripId() {

        return tripPriceId;
    }

    public void setTripId(UUID tripId) {

        this.tripPriceId = tripId;
    }
}

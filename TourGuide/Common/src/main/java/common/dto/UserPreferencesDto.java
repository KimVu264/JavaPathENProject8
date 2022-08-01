package common.dto;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

public class UserPreferencesDto {

    private int attractionProximity = Integer.MAX_VALUE;
    private CurrencyUnit currency = Monetary.getCurrency("USD");
    private double lowerPricePoint = 0;
    private double highPricePoint = Integer.MAX_VALUE;
    private int tripDuration = 1;
    private int ticketQuantity = 1;
    private int numberOfAdults = 1;
    private int numberOfChildren = 0;

    public UserPreferencesDto() {
    }

    public UserPreferencesDto (int attractionProximity, CurrencyUnit currency, double lowerPricePoint, double highPricePoint, int tripDuration, int ticketQuantity, int numberOfAdults, int numberOfChildren) {

        this.attractionProximity = attractionProximity;
        this.currency            = currency;
        this.lowerPricePoint     = lowerPricePoint;
        this.highPricePoint      = highPricePoint;
        this.tripDuration        = tripDuration;
        this.ticketQuantity      = ticketQuantity;
        this.numberOfAdults      = numberOfAdults;
        this.numberOfChildren    = numberOfChildren;
    }

    public UserPreferencesDto (int tripDuration, int ticketQuantity, int numberOfAdults, int numberOfChildren) {
        this.tripDuration     = tripDuration;
        this.ticketQuantity   = ticketQuantity;
        this.numberOfAdults   = numberOfAdults;
        this.numberOfChildren = numberOfChildren;
    }

    public void setAttractionProximity(int attractionProximity) {
        this.attractionProximity = attractionProximity;
    }

    public int getAttractionProximity() {
        return attractionProximity;
    }

    public int getTripDuration() {
        return tripDuration;
    }

    public void setTripDuration(int tripDuration) {
        this.tripDuration = tripDuration;
    }

    public int getTicketQuantity() {
        return ticketQuantity;
    }

    public void setTicketQuantity(int ticketQuantity) {
        this.ticketQuantity = ticketQuantity;
    }

    public int getNumberOfAdults() {
        return numberOfAdults;
    }

    public void setNumberOfAdults(int numberOfAdults) {
        this.numberOfAdults = numberOfAdults;
    }

    public int getNumberOfChildren() {
        return numberOfChildren;
    }

    public void setNumberOfChildren(int numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
    }

}

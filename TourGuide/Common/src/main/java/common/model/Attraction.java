package common.model;

import java.util.UUID;

public class Attraction extends Location {

    private String attractionName;
    private String city;
    private String state;
    private UUID attractionId;

    public Attraction() {

    }

    public Attraction(double latitude, double longitude, String attractionName, String city, String state, UUID attractionId) {
        super(latitude, longitude);
        this.attractionName = attractionName;
        this.city           = city;
        this.state          = state;
        this.attractionId   = attractionId;
    }

    public String getAttractionName() {

        return attractionName;
    }

    public void setAttractionName(String attractionName) {

        this.attractionName = attractionName;
    }

    public String getCity() {

        return city;
    }

    public void setCity(String city) {

        this.city = city;
    }

    public String getState() {

        return state;
    }

    public void setState(String state) {

        this.state = state;
    }

    public UUID getAttractionId() {

        return attractionId;
    }

    public void setAttractionId(UUID attractionId) {

        this.attractionId = attractionId;
    }
}

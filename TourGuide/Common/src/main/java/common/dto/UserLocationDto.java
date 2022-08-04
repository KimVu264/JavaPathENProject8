package common.dto;

import common.model.Location;

import java.util.UUID;

public class UserLocationDto {
    private UUID userId;
    private Location location;

    public UserLocationDto() {
    }

    public UserLocationDto(UUID uuid, Location location) {
        this.userId = uuid;
        this.location = location;
    }

    public String getUserId() {
        return userId.toString();
    }

    public Location getLocation() {
        return location;
    }
}

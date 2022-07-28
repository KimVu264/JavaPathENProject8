package tourGuide.dto;


import gpsUtil.location.Attraction;
import gpsUtil.location.Location;

public class NearByAttractionsDto {
	private Attraction attraction;
	private Location userLocation;
	private Double distance;
	private Integer rewardPoints;

	public NearByAttractionsDto() {
	}

	public NearByAttractionsDto(Attraction attraction, Location userLocation, Double distance, Integer rewardPoints) {
		this.attraction = attraction;
		this.userLocation = userLocation;
		this.distance = distance;
		this.rewardPoints = rewardPoints;
	}

	public Attraction getAttraction() {
		return attraction;
	}

	public void setAttraction(Attraction attraction) {
		this.attraction = attraction;
	}

	public Location getUserLocation() {
		return userLocation;
	}

	public void setUserLocation(Location userLocation) {
		this.userLocation = userLocation;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public Integer getRewardPoints() {
		return rewardPoints;
	}

	public void setRewardPoints(Integer rewardPoints) {
		this.rewardPoints = rewardPoints;
	}

}

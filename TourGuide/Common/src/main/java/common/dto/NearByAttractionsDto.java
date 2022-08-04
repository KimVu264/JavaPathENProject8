package common.dto;

import common.model.Attraction;
import common.model.Location;

public class NearByAttractionsDto extends Location {
	private String attractionName;
	private double distance;
	private int rewardPoints;

	public NearByAttractionsDto(double latitude, double longitude, String attractionName, double distance, int rewardPoints) {

		super(latitude, longitude);
		this.attractionName = attractionName;
		this.distance       = distance;
		this.rewardPoints   = rewardPoints;
	}

	public NearByAttractionsDto() {

	}

	public String getAttractionName() {

		return attractionName;
	}

	public void setAttractionName(String attractionName) {

		this.attractionName = attractionName;
	}



	public double getDistance() {

		return distance;
	}

	public void setDistance(double distance) {

		this.distance = distance;
	}

	public int getRewardPoints() {

		return rewardPoints;
	}

	public void setRewardPoints(int rewardPoints) {

		this.rewardPoints = rewardPoints;
	}

}

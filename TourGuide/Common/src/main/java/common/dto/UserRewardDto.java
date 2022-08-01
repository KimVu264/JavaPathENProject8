package common.dto;

import common.model.Attraction;
import common.model.VisitedLocation;

public class UserRewardDto {
    public final VisitedLocation visitedLocation;
    public final Attraction attraction;
    private int rewardPoints;

    public UserRewardDto(VisitedLocation visitedLocation, Attraction attraction, int rewardPoints) {
        this.visitedLocation = visitedLocation;
        this.attraction = attraction;
        this.rewardPoints = rewardPoints;
    }

    public int getRewardPoints() {
        return rewardPoints;
    }

    public void setRewardPoints(int rewardPoints) {
        this.rewardPoints = rewardPoints;
    }

    public VisitedLocation getVisitedLocation() {
        return visitedLocation;
    }

    public Attraction getAttraction() {
        return attraction;
    }
}

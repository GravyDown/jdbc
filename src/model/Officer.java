package model;

public class Officer {
    private long officerId;
    private String officerKey;
    private String officerName;
    private String officerPhone;
    private String officerRegion;
    private int starRating;

    // Getters and Setters
    public long getOfficerId() { return officerId; }
    public void setOfficerId(long officerId) { this.officerId = officerId; }

    public String getOfficerKey() { return officerKey; }
    public void setOfficerKey(String officerKey) { this.officerKey = officerKey; }

    public String getOfficerName() { return officerName; }
    public void setOfficerName(String officerName) { this.officerName = officerName; }

    public String getOfficerPhone() { return officerPhone; }
    public void setOfficerPhone(String officerPhone) { this.officerPhone = officerPhone; }

    public String getOfficerRegion() { return officerRegion; }
    public void setOfficerRegion(String officerRegion) { this.officerRegion = officerRegion; }

    public int getStarRating() { return starRating; }
    public void setStarRating(int starRating) { this.starRating = starRating; }
}

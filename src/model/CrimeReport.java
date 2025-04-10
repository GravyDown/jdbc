package model;

public class CrimeReport {
    private int reportId;
    private int citizenId;
    private String crimeType;
    private String description;
    private String location;
    private String evidenceUrl;
    private String status;
    private String timestamp;

    public CrimeReport(int reportId, int citizenId, String crimeType, String description, 
                       String location, String evidenceUrl, String status, String timestamp) {
        this.reportId = reportId;
        this.citizenId = citizenId;
        this.crimeType = crimeType;
        this.description = description;
        this.location = location;
        this.evidenceUrl = evidenceUrl;
        this.status = status;
        this.timestamp = timestamp;
    }

    // Getters
    public int getReportId() { return reportId; }
    public int getCitizenId() { return citizenId; }
    public String getCrimeType() { return crimeType; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }
    public String getEvidenceUrl() { return evidenceUrl; }
    public String getStatus() { return status; }
    public String getTimestamp() { return timestamp; }
}
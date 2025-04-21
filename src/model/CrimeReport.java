package model;

public class CrimeReport {
    private int reportId;
    private int citizenId;
    private String crimeType;
    private String description;
    private String location;
    private String case_status;
    private String timestamp;

    public CrimeReport(int reportId, int citizenId, String crimeType, String description, 
                       String location, String case_status, String timestamp) {
        this.reportId = reportId;
        this.citizenId = citizenId;
        this.crimeType = crimeType;
        this.description = description;
        this.location = location;
        
        this.case_status = case_status;
        this.timestamp = timestamp;
    }

    // Getters
    public int getReportId() { return reportId; }
    public int getCitizenId() { return citizenId; }
    public String getCrimeType() { return crimeType; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }
    
    public String getcase_status() { return case_status; }
    public String getTimestamp() { return timestamp; }
}
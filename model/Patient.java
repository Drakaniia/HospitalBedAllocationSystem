package model;

public class Patient implements Comparable<Patient> {
    private int patientId;
    private String name;
    private int criticality; // Lower value means higher priority (1 = most critical)
    private long arrivalTime; // Timestamp of when patient arrived
    private boolean assigned; // Whether the patient has been assigned a bed
    private Bed assignedBed; // Reference to the assigned bed
    private long dischargeTime; // Timestamp when patient was discharged
    private boolean discharged; // Whether the patient has been discharged

    public Patient(int patientId, String name, int criticality) {
        this.patientId = patientId;
        this.name = name;
        this.criticality = criticality;
        this.arrivalTime = System.currentTimeMillis();
        this.assigned = false;
        this.assignedBed = null;
        this.discharged = false;
        this.dischargeTime = -1;
    }

    // Getters
    public int getPatientId() {
        return patientId;
    }

    public String getName() {
        return name;
    }

    public int getCriticality() {
        return criticality;
    }

    public long getArrivalTime() {
        return arrivalTime;
    }

    public boolean isAssigned() {
        return assigned;
    }

    public Bed getAssignedBed() {
        return assignedBed;
    }

    public boolean isDischarged() {
        return discharged;
    }

    public long getDischargeTime() {
        return dischargeTime;
    }

    // Setters
    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }

    public void setAssignedBed(Bed bed) {
        this.assignedBed = bed;
    }

    public void setCriticality(int criticality) {
        this.criticality = criticality;
    }

    public void setArrivalTime(long arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setDischarged(boolean discharged) {
        this.discharged = discharged;
        if (discharged && this.dischargeTime == -1) { // Only set if not already set
            this.dischargeTime = System.currentTimeMillis();
        }
    }

    public void setDischargeTime(long dischargeTime) {
        this.dischargeTime = dischargeTime;
        if (dischargeTime > 0) {
            this.discharged = true; // Mark as discharged if a discharge time is set
        }
    }

    // Implementation of compareTo for Min-Heap priority
    // Priority is determined first by criticality, then by arrival time
    @Override
    public int compareTo(Patient other) {
        if (this.criticality != other.criticality) {
            return Integer.compare(this.criticality, other.criticality);
        }
        // If criticality is the same, prioritize by arrival time (earlier arrival has higher priority)
        return Long.compare(this.arrivalTime, other.arrivalTime);
    }

    @Override
    public String toString() {
        return "Patient{" +
                "patientId=" + patientId +
                ", name='" + name + '\'' +
                ", criticality=" + criticality +
                ", arrivalTime=" + arrivalTime +
                ", assigned=" + assigned +
                ", assignedBed=" + (assignedBed != null ? assignedBed.getBedId() : "None") +
                ", discharged=" + discharged +
                ", dischargeTime=" + dischargeTime +
                '}';
    }
}
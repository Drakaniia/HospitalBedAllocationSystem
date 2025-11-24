public class Bed {
    private int bedId;
    private String location; // e.g., "ICU-101", "Ward-A-001"
    private String type; // e.g., "ICU", "Regular", "Semi-private"
    private boolean occupied;
    private Patient assignedPatient;

    public Bed(int bedId, String location) {
        this.bedId = bedId;
        this.location = location;
        this.type = determineTypeFromLocation(location); // Determine type based on location
        this.occupied = false;
        this.assignedPatient = null;
    }

    public Bed(int bedId, String location, String type) {
        this.bedId = bedId;
        this.location = location;
        this.type = type;
        this.occupied = false;
        this.assignedPatient = null;
    }

    // Helper method to determine bed type from location
    private String determineTypeFromLocation(String location) {
        if (location.toUpperCase().contains("ICU")) {
            return "ICU";
        } else if (location.toUpperCase().contains("WARD")) {
            return "Regular Ward";
        } else if (location.toUpperCase().contains("PRIVATE")) {
            return "Private Room";
        } else {
            return "General";
        }
    }

    // Getters
    public int getBedId() {
        return bedId;
    }

    public String getLocation() {
        return location;
    }

    public String getType() {
        return type;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public Patient getAssignedPatient() {
        return assignedPatient;
    }

    // Methods for bed assignment
    public boolean assignPatient(Patient patient) {
        if (!occupied) {
            this.occupied = true;
            this.assignedPatient = patient;
            patient.setAssigned(true);
            patient.setAssignedBed(this);
            return true;
        }
        return false;
    }

    public boolean releaseBed() {
        if (occupied) {
            this.occupied = false;
            if (assignedPatient != null) {
                assignedPatient.setAssigned(false);
                assignedPatient.setAssignedBed(null);
                assignedPatient.setDischarged(true); // Mark patient as discharged
                assignedPatient = null;
            }
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Bed{" +
                "bedId=" + bedId +
                ", location='" + location + '\'' +
                ", occupied=" + occupied +
                ", assignedPatient=" + (assignedPatient != null ? assignedPatient.getPatientId() : "None") +
                '}';
    }
}
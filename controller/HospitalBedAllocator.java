package controller;

import java.util.ArrayList;
import java.util.List;
import model.Patient;
import model.Bed;

public class HospitalBedAllocator {
    private MinHeap patientHeap;
    private List<Bed> availableBeds;
    private List<Bed> allBeds;
    private List<Patient> dischargedPatients;

    public HospitalBedAllocator() {
        this.patientHeap = new MinHeap();
        this.availableBeds = new ArrayList<>();
        this.allBeds = new ArrayList<>();
        this.dischargedPatients = new ArrayList<>();
    }
    
    // Add a new bed to the system
    public void addBed(Bed bed) {
        allBeds.add(bed);
        if (!bed.isOccupied()) {
            availableBeds.add(bed);
            // Assign the bed to a patient if there are waiting patients
            assignBedWhenAvailable();
        }
    }
    
    // Insert a new patient into the system
    public void insertPatient(Patient patient) {
        patientHeap.insert(patient);
        System.out.println("Patient inserted: " + patient);

        // Only try to assign a bed if there are available beds
        if (!availableBeds.isEmpty()) {
            assignBedWhenAvailable();
        }
    }
    
    
    // Auto-assign beds when a bed becomes available
    public void assignBedWhenAvailable() {
        if (!availableBeds.isEmpty() && !patientHeap.isEmpty()) {
            Bed bed = availableBeds.get(0);
            Patient patient = patientHeap.extractMin(); // Get the highest priority patient
            
            if (patient != null) {
                if (bed.assignPatient(patient)) {
                    availableBeds.remove(0);
                    System.out.println("Highest priority patient " + patient.getPatientId() + 
                                     " assigned to bed " + bed.getBedId());
                } else {
                    // If assignment failed for some reason, put patient back in the heap
                    patientHeap.insert(patient);
                }
            }
        }
    }
    
    // Reassign a patient if their criticality is upgraded
    public boolean reassignPatient(int patientId, int newCriticality) {
        Patient patient = patientHeap.findPatient(patientId);
        if (patient != null) {
            // Remove patient from heap
            if (patientHeap.removePatient(patientId)) {
                // Update patient's criticality
                patient.setCriticality(newCriticality);
                // Reset arrival time to current time to give priority among same criticality
                patient.setArrivalTime(System.currentTimeMillis());
                patient.setAssigned(false);
                patient.setAssignedBed(null);

                patientHeap.insert(patient);

                System.out.println("Patient " + patientId + " criticality updated to " + newCriticality +
                                 " and reinserted into queue");

                // Try to assign a bed if one is available
                if (!availableBeds.isEmpty()) {
                    Bed bed = availableBeds.get(0);
                    if (bed.assignPatient(patient)) {
                        availableBeds.remove(0);
                        System.out.println("Reassigned patient " + patient.getPatientId() + " to bed " + bed.getBedId());
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    // Release a bed when a patient is discharged
    public boolean releaseBed(int bedId) {
        for (Bed bed : allBeds) {
            if (bed.getBedId() == bedId && bed.isOccupied()) {
                Patient patient = bed.getAssignedPatient();
                if (patient != null) {
                    System.out.println("Patient " + patient.getPatientId() + " (" + patient.getName() +
                                     ") discharged from bed " + bedId);
                    // Add to discharged list before releasing
                    dischargedPatients.add(patient);
                }

                bed.releaseBed();
                availableBeds.add(bed);
                System.out.println("Bed " + bedId + " released");

                // Try to assign this bed to the highest priority waiting patient
                assignBedWhenAvailable();
                return true;
            }
        }
        return false;
    }
    
    // Display the heap structure level by level
    public void displayHeapLevelByLevel() {
        patientHeap.displayLevelByLevel();
    }
    
    // Get number of waiting patients
    public int getWaitingPatientCount() {
        return patientHeap.size();
    }
    
    // Get number of available beds
    public int getAvailableBedCount() {
        return availableBeds.size();
    }
    
    // Get all waiting patients
    public List<Patient> getWaitingPatients() {
        return patientHeap.getHeapList();
    }

    // Get total number of beds
    public int getAllBedsCount() {
        return allBeds.size();
    }

    // Display all beds with their status
    public void displayAllBeds() {
        System.out.println("Bed ID | Location    | Type        | Status    | Patient");
        System.out.println("-------|-------------|-------------|-----------|--------");
        for (Bed bed : allBeds) {
            String status = bed.isOccupied() ? "Occupied" : "Available";
            String patientInfo = bed.getAssignedPatient() != null ?
                               "P" + bed.getAssignedPatient().getPatientId() + " - " +
                               bed.getAssignedPatient().getName() :
                               "None";
            System.out.printf("%-7d| %-11s | %-11s | %-9s | %s%n",
                            bed.getBedId(), bed.getLocation(), bed.getType(), status, patientInfo);
        }
    }

    // Method to get discharged patients
    public List<Patient> getDischargedPatients() {
        return new ArrayList<>(dischargedPatients);
    }

    // Method to display discharged patients
    public void displayDischargedPatients() {
        if (dischargedPatients.isEmpty()) {
            System.out.println("No patients have been discharged yet.");
            return;
        }
        System.out.println("Discharged Patients:");
        System.out.println("ID  | Name              | Criticality | Discharge Time");
        System.out.println("----|-------------------|-------------|---------------");
        for (Patient patient : dischargedPatients) {
            System.out.printf("P%-3d| %-17s | %-11d | %s%n",
                            patient.getPatientId(),
                            patient.getName(),
                            patient.getCriticality(),
                            new java.util.Date(patient.getDischargeTime()));
        }
    }

    // Save system data to files
    public void saveSystemData() {
        // Save all patients (waiting list)
        List<Patient> allPatients = getWaitingPatients();
        // Add discharged patients
        allPatients.addAll(getDischargedPatients());
        DataPersistence.savePatients(allPatients);

        // Save all beds
        DataPersistence.saveBeds(allBeds);

        // Save discharged patients separately
        DataPersistence.saveDischargedPatients(getDischargedPatients());

        System.out.println("System data saved successfully!");
    }

    // Load system data from files
    public void loadSystemData() {
        // Load patients
        List<Patient> loadedPatients = DataPersistence.loadPatients();
        patientHeap = new MinHeap();
        for (Patient patient : loadedPatients) {
            // Only add to heap if not discharged
            if (!patient.isDischarged()) {
                patientHeap.insert(patient);
            }
        }

        // Load beds
        List<Bed> loadedBeds = DataPersistence.loadBeds();
        allBeds.clear();
        availableBeds.clear();

        for (Bed bed : loadedBeds) {
            allBeds.add(bed);
            if (!bed.isOccupied()) {
                availableBeds.add(bed);
            }
        }

        // Load discharged patients
        dischargedPatients = DataPersistence.loadDischargedPatients();

        System.out.println("System data loaded successfully!");
    }

    // Method to find patient by ID
    public Patient findPatientById(int patientId) {
        // Check in waiting heap
        for (Patient patient : patientHeap.getHeapList()) {
            if (patient.getPatientId() == patientId) {
                return patient;
            }
        }

        // Check in assigned patients
        for (Bed bed : allBeds) {
            if (bed.isOccupied()) {
                Patient patient = bed.getAssignedPatient();
                if (patient != null && patient.getPatientId() == patientId) {
                    return patient;
                }
            }
        }

        // Check in discharged patients
        for (Patient patient : dischargedPatients) {
            if (patient.getPatientId() == patientId) {
                return patient;
            }
        }

        return null; // Patient not found
    }

    // Method to find patients by name
    public List<Patient> findPatientsByName(String name) {
        List<Patient> results = new ArrayList<>();

        // Check in waiting heap
        for (Patient patient : patientHeap.getHeapList()) {
            if (patient.getName().toLowerCase().contains(name.toLowerCase())) {
                results.add(patient);
            }
        }

        // Check in assigned patients
        for (Bed bed : allBeds) {
            if (bed.isOccupied()) {
                Patient patient = bed.getAssignedPatient();
                if (patient != null && patient.getName().toLowerCase().contains(name.toLowerCase())) {
                    results.add(patient);
                }
            }
        }

        // Check in discharged patients
        for (Patient patient : dischargedPatients) {
            if (patient.getName().toLowerCase().contains(name.toLowerCase())) {
                results.add(patient);
            }
        }

        return results;
    }

    // Method to get patients by criticality level
    public List<Patient> getPatientsByCriticality(int criticality) {
        List<Patient> results = new ArrayList<>();

        // Check in waiting heap
        for (Patient patient : patientHeap.getHeapList()) {
            if (patient.getCriticality() == criticality) {
                results.add(patient);
            }
        }

        // Check in assigned patients
        for (Bed bed : allBeds) {
            if (bed.isOccupied()) {
                Patient patient = bed.getAssignedPatient();
                if (patient != null && patient.getCriticality() == criticality) {
                    results.add(patient);
                }
            }
        }

        // Check in discharged patients
        for (Patient patient : dischargedPatients) {
            if (patient.getCriticality() == criticality) {
                results.add(patient);
            }
        }

        return results;
    }

    // Method to find beds by type
    public List<Bed> findBedsByType(String type) {
        List<Bed> results = new ArrayList<>();
        for (Bed bed : allBeds) {
            if (bed.getType().toLowerCase().contains(type.toLowerCase())) {
                results.add(bed);
            }
        }
        return results;
    }

    // Method to find available beds by type
    public List<Bed> findAvailableBedsByType(String type) {
        List<Bed> results = new ArrayList<>();
        for (Bed bed : allBeds) {
            if (!bed.isOccupied() && bed.getType().toLowerCase().contains(type.toLowerCase())) {
                results.add(bed);
            }
        }
        return results;
    }

    // Method to get all patients for display (waiting, assigned, and discharged)
    public List<Patient> getAllPatientsForDisplay() {
        List<Patient> allPatients = new ArrayList<>();

        // Add patients from the waiting heap
        allPatients.addAll(patientHeap.getHeapList());

        // Add assigned patients (from beds)
        for (Bed bed : allBeds) {
            if (bed.isOccupied() && bed.getAssignedPatient() != null) {
                Patient patient = bed.getAssignedPatient();
                // Only add if not already in the list
                if (!allPatients.contains(patient)) {
                    allPatients.add(patient);
                }
            }
        }

        // Add discharged patients
        allPatients.addAll(dischargedPatients);

        return allPatients;
    }

    // Method to get all beds
    public List<Bed> getAllBeds() {
        return new ArrayList<>(allBeds);
    }

    // Getter for the patient heap (for visualization)
    public MinHeap getPatientHeap() {
        return patientHeap;
    }
}
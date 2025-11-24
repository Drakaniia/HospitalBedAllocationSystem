package controller;

import java.io.*;
import java.util.*;
import model.Patient;
import model.Bed;

public class DataPersistence {
    private static final String PATIENTS_FILE = "patients.txt";
    private static final String BEDS_FILE = "beds.txt";
    private static final String DISCHARGED_PATIENTS_FILE = "discharged_patients.txt";
    
    // Save all patients to file
    public static void savePatients(List<Patient> patients) {
        try (PrintWriter out = new PrintWriter(new FileWriter(PATIENTS_FILE))) {
            for (Patient patient : patients) {
                out.println(patient.getPatientId() + "," + 
                           patient.getName() + "," + 
                           patient.getCriticality() + "," + 
                           patient.getArrivalTime() + "," + 
                           patient.isAssigned() + "," + 
                           patient.isDischarged());
            }
        } catch (IOException e) {
            System.out.println("Error saving patients: " + e.getMessage());
        }
    }
    
    // Load patients from file
    public static List<Patient> loadPatients() {
        List<Patient> patients = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(PATIENTS_FILE))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;
                
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    int patientId = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    int criticality = Integer.parseInt(parts[2]);
                    long arrivalTime = Long.parseLong(parts[3]);
                    
                    Patient patient = new Patient(patientId, name, criticality);
                    patient.setArrivalTime(arrivalTime);
                    
                    // Set assigned status
                    patient.setAssigned(Boolean.parseBoolean(parts[4]));
                    
                    // Set discharged status
                    if (parts.length > 5) {
                        patient.setDischarged(Boolean.parseBoolean(parts[5]));
                    }
                    
                    patients.add(patient);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No existing patient data found. Starting fresh.");
        }
        return patients;
    }
    
    // Save all beds to file
    public static void saveBeds(List<Bed> beds) {
        try (PrintWriter out = new PrintWriter(new FileWriter(BEDS_FILE))) {
            for (Bed bed : beds) {
                out.println(bed.getBedId() + "," + 
                           bed.getLocation() + "," + 
                           bed.getType() + "," + 
                           bed.isOccupied());
            }
        } catch (IOException e) {
            System.out.println("Error saving beds: " + e.getMessage());
        }
    }
    
    // Load beds from file
    public static List<Bed> loadBeds() {
        List<Bed> beds = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(BEDS_FILE))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;
                
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    int bedId = Integer.parseInt(parts[0]);
                    String location = parts[1];
                    String type = parts[2];
                    boolean occupied = Boolean.parseBoolean(parts[3]);
                    
                    Bed bed = new Bed(bedId, location, type);
                    beds.add(bed);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No existing bed data found. Starting fresh.");
            return beds;
        }
        return beds;
    }
    
    // Save discharged patients to file
    public static void saveDischargedPatients(List<Patient> dischargedPatients) {
        try (PrintWriter out = new PrintWriter(new FileWriter(DISCHARGED_PATIENTS_FILE))) {
            for (Patient patient : dischargedPatients) {
                out.println(patient.getPatientId() + "," + 
                           patient.getName() + "," + 
                           patient.getCriticality() + "," + 
                           patient.getArrivalTime() + "," + 
                           patient.getDischargeTime());
            }
        } catch (IOException e) {
            System.out.println("Error saving discharged patients: " + e.getMessage());
        }
    }
    
    // Load discharged patients from file
    public static List<Patient> loadDischargedPatients() {
        List<Patient> patients = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(DISCHARGED_PATIENTS_FILE))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;
                
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    int patientId = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    int criticality = Integer.parseInt(parts[2]);
                    long arrivalTime = Long.parseLong(parts[3]);
                    long dischargeTime = Long.parseLong(parts[4]);
                    
                    Patient patient = new Patient(patientId, name, criticality);
                    patient.setArrivalTime(arrivalTime);
                    patient.setDischarged(true);
                    
                    patients.add(patient);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No discharged patient data found.");
        }
        return patients;
    }
}
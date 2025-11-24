public class HospitalBedAllocationSystem {
    public static void main(String[] args) {
        // Create allocator system
        HospitalBedAllocator allocator = new HospitalBedAllocator();
        
        // Insert some patients with different criticalities first (they go to waiting list)
        System.out.println("=== Hospital Bed Allocation System Demo ===\n");

        // Insert some patients with different criticalities
        System.out.println("1. Inserting patients (no beds available yet):");
        Patient patient1 = new Patient(101, "John Doe", 3); // Low criticality
        Patient patient2 = new Patient(102, "Jane Smith", 1); // High criticality
        Patient patient3 = new Patient(103, "Robert Johnson", 2); // Medium criticality

        allocator.insertPatient(patient1);
        allocator.insertPatient(patient2);
        allocator.insertPatient(patient3);

        // Now add beds to the system (this will trigger assignments)
        System.out.println("\n2. Adding beds to the system (this will assign patients in priority order):");
        Bed bed1 = new Bed(1, "ICU-101");
        Bed bed2 = new Bed(2, "ICU-102");
        Bed bed3 = new Bed(3, "Ward-A-001");
        allocator.addBed(bed1);
        allocator.addBed(bed2);
        allocator.addBed(bed3);
        
        System.out.println("\n3. Current heap structure after bed assignments:");
        allocator.displayHeapLevelByLevel();

        System.out.println("\n4. Adding more patients to the waiting list:");
        Patient patient4 = new Patient(104, "Alice Williams", 3); // Low criticality
        Patient patient5 = new Patient(105, "Bob Brown", 1); // High criticality
        allocator.insertPatient(patient4);
        allocator.insertPatient(patient5);

        System.out.println("\n5. Current heap structure with new patients:");
        allocator.displayHeapLevelByLevel();

        System.out.println("\n6. Releasing a bed (Bed 1) to trigger assignment to waiting patients:");
        allocator.releaseBed(1);

        System.out.println("\n7. Current heap structure after bed release:");
        allocator.displayHeapLevelByLevel();

        System.out.println("\n8. Upgrading patient 104's criticality from 3 to 1:");
        allocator.reassignPatient(104, 1);

        System.out.println("\n9. Heap structure after criticality upgrade:");
        allocator.displayHeapLevelByLevel();

        System.out.println("\n10. Releasing another bed (Bed 2):");
        allocator.releaseBed(2);

        System.out.println("\n11. Final heap structure:");
        allocator.displayHeapLevelByLevel();

        System.out.println("\n12. System status:");
        System.out.println("Waiting patients: " + allocator.getWaitingPatientCount());
        System.out.println("Available beds: " + allocator.getAvailableBedCount());
        
        System.out.println("\n=== Demo completed ===");
    }
}
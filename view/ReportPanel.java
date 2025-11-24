package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import controller.HospitalBedAllocator;
import model.Patient;
import model.Bed;

public class ReportPanel extends JPanel {
    private controller.HospitalBedAllocator allocator;
    private JTextArea statsArea;
    private JTable patientTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> reportTypeCombo;
    private JButton generateButton;

    public ReportPanel(controller.HospitalBedAllocator allocator) {
        this.allocator = allocator;
        initializeComponents();
        layoutComponents();
        setupEventHandlers();
        refreshData();
    }

    private void initializeComponents() {
        // Statistics area
        statsArea = new JTextArea(10, 50);
        statsArea.setEditable(false);
        statsArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        
        // Patient table for reports
        tableModel = new DefaultTableModel();
        patientTable = new JTable(tableModel);
        
        // Report type selector
        reportTypeCombo = new JComboBox<>(new String[]{
            "General Statistics", 
            "Waiting Patients", 
            "Occupied Beds", 
            "Discharged Patients",
            "Patients by Criticality"
        });
        generateButton = new JButton("Generate Report");
        
        // Set up table
        String[] columnNames = {"ID", "Name", "Criticality", "Status", "Assigned Bed"};
        tableModel.setColumnIdentifiers(columnNames);
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        // Top panel for report selection
        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.add(new JLabel("Report Type:"));
        controlPanel.add(reportTypeCombo);
        controlPanel.add(generateButton);
        
        // Split the main area
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        
        // Top part: statistics
        JScrollPane statsScrollPane = new JScrollPane(statsArea);
        statsScrollPane.setBorder(BorderFactory.createTitledBorder("Statistics"));
        
        // Bottom part: patient table
        JScrollPane tableScrollPane = new JScrollPane(patientTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Report Details"));
        
        splitPane.setTopComponent(statsScrollPane);
        splitPane.setBottomComponent(tableScrollPane);
        splitPane.setDividerLocation(250);
        
        add(controlPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
    }

    private void setupEventHandlers() {
        generateButton.addActionListener(e -> generateReport());
    }

    private void generateReport() {
        String reportType = (String) reportTypeCombo.getSelectedItem();
        
        switch (reportType) {
            case "General Statistics":
                generateGeneralStats();
                break;
            case "Waiting Patients":
                generateWaitingPatientsReport();
                break;
            case "Occupied Beds":
                generateOccupiedBedsReport();
                break;
            case "Discharged Patients":
                generateDischargedPatientsReport();
                break;
            case "Patients by Criticality":
                generatePatientsByCriticalityReport();
                break;
        }
    }

    private void generateGeneralStats() {
        int waitingCount = allocator.getWaitingPatientCount();
        int availableBeds = allocator.getAvailableBedCount();
        int totalBeds = allocator.getAllBedsCount();
        int occupiedBeds = totalBeds - availableBeds;
        double occupancyRate = totalBeds > 0 ? (double) occupiedBeds / totalBeds * 100 : 0;
        
        // Clear table
        tableModel.setRowCount(0);
        
        // Update stats area
        statsArea.setText("");
        statsArea.append("=== GENERAL STATISTICS ===\n");
        statsArea.append("Total Beds: " + totalBeds + "\n");
        statsArea.append("Occupied Beds: " + occupiedBeds + "\n");
        statsArea.append("Available Beds: " + availableBeds + "\n");
        statsArea.append(String.format("Occupancy Rate: %.2f%%\n", occupancyRate));
        statsArea.append("Waiting Patients: " + waitingCount + "\n\n");
        
        // Add some additional stats
        statsArea.append("=== ADDITIONAL METRICS ===\n");
        statsArea.append("Discharged Patients: " + allocator.getDischargedPatients().size() + "\n");
        
        // Count patients by criticality
        for (int i = 1; i <= 3; i++) {
            int count = allocator.getPatientsByCriticality(i).size();
            statsArea.append("Criticality " + i + " Patients: " + count + "\n");
        }
    }

    private void generateWaitingPatientsReport() {
        List<Patient> waitingPatients = allocator.getWaitingPatients();
        
        // Clear stats area
        statsArea.setText("");
        statsArea.append("=== WAITING PATIENTS REPORT ===\n");
        statsArea.append("Number of waiting patients: " + waitingPatients.size() + "\n\n");
        
        // Clear and populate table
        tableModel.setRowCount(0);
        
        if (waitingPatients.isEmpty()) {
            statsArea.append("No patients are currently waiting.\n");
            return;
        }
        
        statsArea.append("Waiting Patients List:\n");
        for (Patient patient : waitingPatients) {
            Object[] row = {
                "P" + patient.getPatientId(),
                patient.getName(),
                patient.getCriticality(),
                "Waiting",
                "None"
            };
            tableModel.addRow(row);
            statsArea.append("- P" + patient.getPatientId() + ": " + patient.getName() + 
                           " (Criticality: " + patient.getCriticality() + ")\n");
        }
    }

    private void generateOccupiedBedsReport() {
        List<Bed> allBeds = allocator.getAllBeds();
        
        // Clear stats area
        statsArea.setText("");
        statsArea.append("=== OCCUPIED BEDS REPORT ===\n");
        
        // Clear and populate table
        tableModel.setRowCount(0);
        
        int occupiedCount = 0;
        for (Bed bed : allBeds) {
            if (bed.isOccupied() && bed.getAssignedPatient() != null) {
                Patient patient = bed.getAssignedPatient();
                Object[] row = {
                    "P" + patient.getPatientId(),
                    patient.getName(),
                    patient.getCriticality(),
                    "Occupied by Patient",
                    bed.getBedId()
                };
                tableModel.addRow(row);
                occupiedCount++;
            }
        }
        
        statsArea.append("Number of occupied beds: " + occupiedCount + "\n\n");
        statsArea.append("Occupied Beds Details:\n");
        for (Bed bed : allBeds) {
            if (bed.isOccupied() && bed.getAssignedPatient() != null) {
                Patient patient = bed.getAssignedPatient();
                statsArea.append("Bed " + bed.getBedId() + " (" + bed.getLocation() + "): " + 
                               "P" + patient.getPatientId() + " - " + patient.getName() + 
                               " (Criticality: " + patient.getCriticality() + ")\n");
            }
        }
    }

    private void generateDischargedPatientsReport() {
        List<Patient> dischargedPatients = allocator.getDischargedPatients();
        
        // Clear stats area
        statsArea.setText("");
        statsArea.append("=== DISCHARGED PATIENTS REPORT ===\n");
        statsArea.append("Number of discharged patients: " + dischargedPatients.size() + "\n\n");
        
        // Clear and populate table
        tableModel.setRowCount(0);
        
        if (dischargedPatients.isEmpty()) {
            statsArea.append("No patients have been discharged yet.\n");
            return;
        }
        
        statsArea.append("Discharged Patients List:\n");
        for (Patient patient : dischargedPatients) {
            Object[] row = {
                "P" + patient.getPatientId(),
                patient.getName(),
                patient.getCriticality(),
                "Discharged",
                "N/A"
            };
            tableModel.addRow(row);
            statsArea.append("- P" + patient.getPatientId() + ": " + patient.getName() + 
                           " (Criticality: " + patient.getCriticality() + 
                           ", Discharged: " + new java.util.Date(patient.getDischargeTime()) + ")\n");
        }
    }

    private void generatePatientsByCriticalityReport() {
        // Ask user for criticality level
        String input = JOptionPane.showInputDialog(this, "Enter criticality level (1-3):", "2");
        if (input == null || input.trim().isEmpty()) return;
        
        try {
            int criticality = Integer.parseInt(input.trim());
            if (criticality < 1 || criticality > 3) {
                JOptionPane.showMessageDialog(this, "Criticality must be between 1 and 3.", 
                                            "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            List<Patient> patients = allocator.getPatientsByCriticality(criticality);
            
            // Clear stats area
            statsArea.setText("");
            statsArea.append("=== PATIENTS WITH CRITICALITY " + criticality + " REPORT ===\n");
            statsArea.append("Number of patients: " + patients.size() + "\n\n");
            
            // Clear and populate table
            tableModel.setRowCount(0);
            
            statsArea.append("Patients with Criticality " + criticality + ":\n");
            for (Patient patient : patients) {
                String status = patient.isAssigned() ? 
                              (patient.isDischarged() ? "Discharged" : "Assigned") : "Waiting";
                String bedInfo = patient.getAssignedBed() != null ? 
                               String.valueOf(patient.getAssignedBed().getBedId()) : "None";
                
                Object[] row = {
                    "P" + patient.getPatientId(),
                    patient.getName(),
                    patient.getCriticality(),
                    status,
                    bedInfo
                };
                tableModel.addRow(row);
                statsArea.append("- P" + patient.getPatientId() + ": " + patient.getName() + 
                               " (" + status + ")\n");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void refreshData() {
        generateReport(); // Generate the default report
    }
}
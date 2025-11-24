import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PatientManagementPanel extends JPanel {
    private HospitalBedAllocator allocator;
    private JTable patientTable;
    private DefaultTableModel tableModel;
    private JTextField idField, nameField;
    private JComboBox<Integer> criticalityCombo;
    private JButton registerButton, updateButton, searchButton;
    private JTextField searchField;

    public PatientManagementPanel(HospitalBedAllocator allocator) {
        this.allocator = allocator;
        initializeComponents();
        layoutComponents();
        setupEventHandlers();
        refreshData();
    }

    private void initializeComponents() {
        // Initialize components
        tableModel = new DefaultTableModel();
        patientTable = new JTable(tableModel);
        patientTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        idField = new JTextField(10);
        nameField = new JTextField(20);
        criticalityCombo = new JComboBox<>(new Integer[]{1, 2, 3});
        registerButton = new JButton("Register Patient");
        updateButton = new JButton("Update Criticality");
        searchButton = new JButton("Search");
        searchField = new JTextField(15);
        
        // Set up table
        String[] columnNames = {"ID", "Name", "Criticality", "Arrival Time", "Assigned Bed", "Status"};
        tableModel.setColumnIdentifiers(columnNames);
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());

        // Top panel for registration
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Register New Patient"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Patient ID:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(idField, gbc);

        gbc.gridx = 2;
        inputPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 3;
        inputPanel.add(nameField, gbc);

        gbc.gridx = 4;
        inputPanel.add(new JLabel("Criticality:"), gbc);
        gbc.gridx = 5;
        inputPanel.add(criticalityCombo, gbc);

        gbc.gridx = 6;
        inputPanel.add(registerButton, gbc);

        // Advanced search panel
        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search & Filter Patients"));
        GridBagConstraints searchGbc = new GridBagConstraints();
        searchGbc.insets = new Insets(5, 5, 5, 5);

        searchGbc.gridx = 0; searchGbc.gridy = 0;
        searchPanel.add(new JLabel("Name:"), searchGbc);
        searchGbc.gridx = 1;
        searchPanel.add(searchField, searchGbc);

        searchGbc.gridx = 2;
        searchPanel.add(new JLabel("Criticality:"), searchGbc);
        JComboBox<Integer> searchCriticalityCombo = new JComboBox<>(new Integer[]{0, 1, 2, 3}); // 0 = all
        searchCriticalityCombo.setSelectedIndex(0);
        searchGbc.gridx = 3;
        searchPanel.add(searchCriticalityCombo, searchGbc);

        searchGbc.gridx = 4;
        JButton advancedSearchButton = new JButton("Advanced Search");
        searchPanel.add(advancedSearchButton, searchGbc);

        // Add event handler for advanced search
        advancedSearchButton.addActionListener(e -> performAdvancedSearch(
            searchField.getText().trim(),
            (Integer) searchCriticalityCombo.getSelectedItem()
        ));

        // Main content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(inputPanel, BorderLayout.NORTH);
        contentPanel.add(new JScrollPane(patientTable), BorderLayout.CENTER);
        contentPanel.add(searchPanel, BorderLayout.SOUTH);

        // Bottom panel for updates
        JPanel updatePanel = new JPanel(new FlowLayout());
        updatePanel.setBorder(BorderFactory.createTitledBorder("Update Patient Criticality"));
        updatePanel.add(new JLabel("Select Patient ID:"));
        JTextField updateIdField = new JTextField(10);
        updatePanel.add(updateIdField);
        updatePanel.add(new JLabel("New Criticality:"));
        JComboBox<Integer> updateCriticalityCombo = new JComboBox<>(new Integer[]{1, 2, 3});
        updatePanel.add(updateCriticalityCombo);
        updatePanel.add(updateButton);

        add(contentPanel, BorderLayout.CENTER);
        add(updatePanel, BorderLayout.SOUTH);
    }

    private void setupEventHandlers() {
        registerButton.addActionListener(e -> registerPatient());
        updateButton.addActionListener(e -> updatePatientCriticality());
        searchButton.addActionListener(e -> searchPatients());

        patientTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = patientTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String patientIdStr = (String) tableModel.getValueAt(selectedRow, 0);
                    if (patientIdStr != null && patientIdStr.startsWith("P")) {
                        patientIdStr = patientIdStr.substring(1); // Remove 'P' prefix
                    }
                    idField.setText(patientIdStr);
                }
            }
        });
    }

    private void performAdvancedSearch(String nameFilter, Integer criticalityFilter) {
        List<Patient> allPatients = allocator.getAllPatientsForDisplay();

        tableModel.setRowCount(0); // Clear existing data

        for (Patient patient : allPatients) {
            // Apply name filter
            boolean nameMatch = nameFilter.isEmpty() ||
                              patient.getName().toLowerCase().contains(nameFilter.toLowerCase());

            // Apply criticality filter (0 means no filter)
            boolean criticalityMatch = criticalityFilter == 0 || patient.getCriticality() == criticalityFilter;

            if (nameMatch && criticalityMatch) {
                String bedInfo = patient.getAssignedBed() != null ?
                               String.valueOf(patient.getAssignedBed().getBedId()) : "None";
                String status = patient.isAssigned() ?
                              (patient.isDischarged() ? "Discharged" : "Assigned") : "Waiting";

                Object[] row = {
                    "P" + patient.getPatientId(),
                    patient.getName(),
                    patient.getCriticality(),
                    new java.util.Date(patient.getArrivalTime()),
                    bedInfo,
                    status
                };
                tableModel.addRow(row);
            }
        }
    }

    private void registerPatient() {
        try {
            int patientId = Integer.parseInt(idField.getText().trim());
            String name = nameField.getText().trim();
            int criticality = (Integer) criticalityCombo.getSelectedItem();
            
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a name for the patient.", 
                                            "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Patient patient = new Patient(patientId, name, criticality);
            allocator.insertPatient(patient);
            
            JOptionPane.showMessageDialog(this, "Patient registered successfully!", 
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
            
            refreshData();
            
            // Clear input fields
            idField.setText("");
            nameField.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid patient ID (number).", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updatePatientCriticality() {
        try {
            String idText = JOptionPane.showInputDialog(this, "Enter Patient ID to update:");
            if (idText == null || idText.trim().isEmpty()) return;
            
            int patientId = Integer.parseInt(idText.trim());
            int newCriticality = Integer.parseInt(
                JOptionPane.showInputDialog(this, "Enter new criticality (1-3):", "2"));
            
            if (newCriticality < 1 || newCriticality > 3) {
                JOptionPane.showMessageDialog(this, "Criticality must be between 1 and 3.", 
                                            "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            boolean updated = allocator.reassignPatient(patientId, newCriticality);
            if (updated) {
                JOptionPane.showMessageDialog(this, "Patient criticality updated successfully!", 
                                            "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshData();
            } else {
                JOptionPane.showMessageDialog(this, "Patient not found.", 
                                            "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers.", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchPatients() {
        String searchName = searchField.getText().trim().toLowerCase();
        List<Patient> allPatients = allocator.getAllPatientsForDisplay();
        
        tableModel.setRowCount(0); // Clear existing data
        
        for (Patient patient : allPatients) {
            if (patient.getName().toLowerCase().contains(searchName)) {
                String bedInfo = patient.getAssignedBed() != null ? 
                               String.valueOf(patient.getAssignedBed().getBedId()) : "None";
                String status = patient.isAssigned() ? 
                              (patient.isDischarged() ? "Discharged" : "Assigned") : "Waiting";
                
                Object[] row = {
                    "P" + patient.getPatientId(),
                    patient.getName(),
                    patient.getCriticality(),
                    new java.util.Date(patient.getArrivalTime()),
                    bedInfo,
                    status
                };
                tableModel.addRow(row);
            }
        }
    }

    public void refreshData() {
        List<Patient> allPatients = allocator.getAllPatientsForDisplay();
        
        tableModel.setRowCount(0); // Clear existing data
        
        for (Patient patient : allPatients) {
            String bedInfo = patient.getAssignedBed() != null ? 
                           String.valueOf(patient.getAssignedBed().getBedId()) : "None";
            String status = patient.isAssigned() ? 
                          (patient.isDischarged() ? "Discharged" : "Assigned") : "Waiting";
            
            Object[] row = {
                "P" + patient.getPatientId(),
                patient.getName(),
                patient.getCriticality(),
                new java.util.Date(patient.getArrivalTime()),
                bedInfo,
                status
            };
            tableModel.addRow(row);
        }
    }
}
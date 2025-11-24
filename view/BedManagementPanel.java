package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import model.Bed;

public class BedManagementPanel extends JPanel {
    private controller.HospitalBedAllocator allocator;
    private JTable bedTable;
    private DefaultTableModel tableModel;
    private JTextField bedIdField, locationField;
    private JComboBox<String> typeCombo;
    private JButton addButton, releaseButton, searchButton;
    private JTextField searchTypeField;

    public BedManagementPanel(controller.HospitalBedAllocator allocator) {
        this.allocator = allocator;
        initializeComponents();
        layoutComponents();
        setupEventHandlers();
        refreshData();
    }

    private void initializeComponents() {
        // Initialize components
        tableModel = new DefaultTableModel();
        bedTable = new JTable(tableModel);
        bedTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        bedIdField = new JTextField(10);
        locationField = new JTextField(20);
        typeCombo = new JComboBox<>(new String[]{"ICU", "Regular Ward", "Private Room", "General"});
        addButton = new JButton("Add Bed");
        releaseButton = new JButton("Release Bed");
        searchButton = new JButton("Search by Type");
        searchTypeField = new JTextField(15);
        
        // Set up table
        String[] columnNames = {"Bed ID", "Location", "Type", "Status", "Assigned Patient"};
        tableModel.setColumnIdentifiers(columnNames);
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());

        // Top panel for bed management
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Manage Beds"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Bed ID:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(bedIdField, gbc);

        gbc.gridx = 2;
        inputPanel.add(new JLabel("Location:"), gbc);
        gbc.gridx = 3;
        inputPanel.add(locationField, gbc);

        gbc.gridx = 4;
        inputPanel.add(new JLabel("Type:"), gbc);
        gbc.gridx = 5;
        inputPanel.add(typeCombo, gbc);

        gbc.gridx = 6;
        inputPanel.add(addButton, gbc);

        gbc.gridx = 7;
        inputPanel.add(releaseButton, gbc);

        // Advanced search panel
        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search & Filter Beds"));
        GridBagConstraints searchGbc = new GridBagConstraints();
        searchGbc.insets = new Insets(5, 5, 5, 5);

        searchGbc.gridx = 0; searchGbc.gridy = 0;
        searchPanel.add(new JLabel("Type:"), searchGbc);
        searchGbc.gridx = 1;
        searchPanel.add(searchTypeField, searchGbc);

        searchGbc.gridx = 2;
        searchPanel.add(new JLabel("Status:"), searchGbc);
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"All", "Available", "Occupied"});
        searchGbc.gridx = 3;
        searchPanel.add(statusCombo, searchGbc);

        searchGbc.gridx = 4;
        JButton advancedSearchButton = new JButton("Advanced Search");
        searchPanel.add(advancedSearchButton, searchGbc);

        // Add event handler for advanced search
        advancedSearchButton.addActionListener(e -> performAdvancedBedSearch(
            searchTypeField.getText().trim(),
            (String) statusCombo.getSelectedItem()
        ));

        // Main content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(inputPanel, BorderLayout.NORTH);
        contentPanel.add(new JScrollPane(bedTable), BorderLayout.CENTER);
        contentPanel.add(searchPanel, BorderLayout.SOUTH);

        add(contentPanel, BorderLayout.CENTER);
    }

    private void setupEventHandlers() {
        addButton.addActionListener(e -> addBed());
        releaseButton.addActionListener(e -> releaseBed());
        searchButton.addActionListener(e -> searchBeds());

        bedTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = bedTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String bedIdStr = (String) tableModel.getValueAt(selectedRow, 0);
                    bedIdField.setText(bedIdStr);
                }
            }
        });
    }

    private void performAdvancedBedSearch(String typeFilter, String statusFilter) {
        List<Bed> allBeds = allocator.getAllBeds();

        tableModel.setRowCount(0); // Clear existing data

        for (Bed bed : allBeds) {
            // Apply type filter
            boolean typeMatch = typeFilter.isEmpty() ||
                              bed.getType().toLowerCase().contains(typeFilter.toLowerCase());

            // Apply status filter
            boolean statusMatch = statusFilter.equals("All") ||
                                (statusFilter.equals("Available") && !bed.isOccupied()) ||
                                (statusFilter.equals("Occupied") && bed.isOccupied());

            if (typeMatch && statusMatch) {
                String patientInfo = bed.getAssignedPatient() != null ?
                                   "P" + bed.getAssignedPatient().getPatientId() +
                                   " (" + bed.getAssignedPatient().getName() + ")" : "None";

                Object[] row = {
                    bed.getBedId(),
                    bed.getLocation(),
                    bed.getType(),
                    bed.isOccupied() ? "Occupied" : "Available",
                    patientInfo
                };
                tableModel.addRow(row);
            }
        }
    }

    private void addBed() {
        try {
            int bedId = Integer.parseInt(bedIdField.getText().trim());
            String location = locationField.getText().trim();
            String type = (String) typeCombo.getSelectedItem();
            
            if (location.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a location for the bed.", 
                                            "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Bed bed = new Bed(bedId, location, type);
            allocator.addBed(bed);
            
            JOptionPane.showMessageDialog(this, "Bed added successfully!", 
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
            
            refreshData();
            
            // Clear input fields
            bedIdField.setText("");
            locationField.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid bed ID (number).", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void releaseBed() {
        try {
            String bedIdStr = JOptionPane.showInputDialog(this, "Enter Bed ID to release:");
            if (bedIdStr == null || bedIdStr.trim().isEmpty()) return;
            
            int bedId = Integer.parseInt(bedIdStr.trim());
            
            boolean released = allocator.releaseBed(bedId);
            if (released) {
                JOptionPane.showMessageDialog(this, "Bed " + bedId + " released successfully!", 
                                            "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshData();
            } else {
                JOptionPane.showMessageDialog(this, "Bed not found or not occupied.", 
                                            "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid bed ID.", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchBeds() {
        String searchType = searchTypeField.getText().trim().toLowerCase();
        List<Bed> allBeds = allocator.getAllBeds();
        
        tableModel.setRowCount(0); // Clear existing data
        
        for (Bed bed : allBeds) {
            if (bed.getType().toLowerCase().contains(searchType)) {
                String patientInfo = bed.getAssignedPatient() != null ? 
                                   "P" + bed.getAssignedPatient().getPatientId() + 
                                   " (" + bed.getAssignedPatient().getName() + ")" : "None";
                
                Object[] row = {
                    bed.getBedId(),
                    bed.getLocation(),
                    bed.getType(),
                    bed.isOccupied() ? "Occupied" : "Available",
                    patientInfo
                };
                tableModel.addRow(row);
            }
        }
    }

    public void refreshData() {
        List<Bed> allBeds = allocator.getAllBeds();
        
        tableModel.setRowCount(0); // Clear existing data
        
        for (Bed bed : allBeds) {
            String patientInfo = bed.getAssignedPatient() != null ? 
                               "P" + bed.getAssignedPatient().getPatientId() + 
                               " (" + bed.getAssignedPatient().getName() + ")" : "None";
            
            Object[] row = {
                bed.getBedId(),
                bed.getLocation(),
                bed.getType(),
                bed.isOccupied() ? "Occupied" : "Available",
                patientInfo
            };
            tableModel.addRow(row);
        }
    }
}
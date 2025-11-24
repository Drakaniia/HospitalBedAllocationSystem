package view;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import java.awt.*;
import java.awt.image.BufferedImage;
import model.Bed;

public class HospitalManagementGUI extends JFrame {
    private controller.HospitalBedAllocator allocator;
    private JTabbedPane tabbedPane;
    private PatientManagementPanel patientPanel;
    private BedManagementPanel bedPanel;
    private ReportPanel reportPanel;
    private HeapVisualizationPanel heapPanel;

    public HospitalManagementGUI() {
        allocator = new controller.HospitalBedAllocator();
        
        // Initialize the demo data
        initializeDemoData();
        
        // Set up the main window
        setTitle("Hospital Management System - Min-Heap Priority Scheduling");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null); // Center the window
        setIconImage(createIcon());
        
        // Create the tabbed pane
        tabbedPane = new JTabbedPane();
        
        // Create panels for each tab
        patientPanel = new PatientManagementPanel(allocator);
        bedPanel = new BedManagementPanel(allocator);
        reportPanel = new ReportPanel(allocator);
        heapPanel = new HeapVisualizationPanel(allocator);
        
        // Add tabs
        tabbedPane.addTab("Patients", null, patientPanel, "Patient Management");
        tabbedPane.addTab("Beds", null, bedPanel, "Bed Management");
        tabbedPane.addTab("Reports", null, reportPanel, "Reports and Statistics");
        tabbedPane.addTab("Heap", null, heapPanel, "Heap Visualization");
        
        // Add the tabbed pane to the main window
        add(tabbedPane, BorderLayout.CENTER);
        
        // Create menu bar
        createMenuBar();
    }
    
    private void initializeDemoData() {
        // Add some initial beds
        allocator.addBed(new Bed(1, "ICU-101", "ICU"));
        allocator.addBed(new Bed(2, "ICU-102", "ICU"));
        allocator.addBed(new Bed(3, "Ward-A-001", "Regular Ward"));
        allocator.addBed(new Bed(4, "Ward-A-002", "Regular Ward"));
        allocator.addBed(new Bed(5, "Ward-B-001", "General"));
    }
    
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // File menu
        JMenu fileMenu = new JMenu("File");
        
        JMenuItem saveItem = new JMenuItem("Save Data");
        saveItem.addActionListener(e -> saveData());
        fileMenu.add(saveItem);
        
        JMenuItem loadItem = new JMenuItem("Load Data");
        loadItem.addActionListener(e -> loadData());
        fileMenu.add(loadItem);
        
        fileMenu.addSeparator();
        
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        
        menuBar.add(fileMenu);
        
        // View menu
        JMenu viewMenu = new JMenu("View");
        
        JMenuItem refreshItem = new JMenuItem("Refresh All Views");
        refreshItem.addActionListener(e -> refreshAllViews());
        viewMenu.add(refreshItem);
        
        menuBar.add(viewMenu);
        
        setJMenuBar(menuBar);
    }
    
    private void saveData() {
        allocator.saveSystemData();
        JOptionPane.showMessageDialog(this, "System data saved successfully!", 
                                    "Save Data", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void loadData() {
        allocator.loadSystemData();
        refreshAllViews();
        JOptionPane.showMessageDialog(this, "System data loaded successfully!", 
                                    "Load Data", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void refreshAllViews() {
        patientPanel.refreshData();
        bedPanel.refreshData();
        reportPanel.refreshData();
        heapPanel.refreshVisualization();
    }

    private Image createIcon() {
        // Create a simple icon (in a real application, you might load an image file)
        BufferedImage img = new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setColor(new Color(70, 130, 180)); // Steel blue
        g2d.fillRect(0, 0, 32, 32);
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.drawString("H", 8, 22);
        g2d.dispose();
        return img;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set a more modern look and feel if available, otherwise use system default
                for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if (info.getName().equals("Nimbus")) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
                // If Nimbus is not available, use system look and feel
                if (!UIManager.getLookAndFeel().getName().equals("Nimbus")) {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                }
            } catch (Exception e) {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            new HospitalManagementGUI().setVisible(true);
        });
    }
}
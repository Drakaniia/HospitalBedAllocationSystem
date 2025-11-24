import view.HospitalManagementGUI;

/**
 * Main entry point for the Hospital Management System.
 * This class serves as an alternative to run.sh for running the application in IDEs like Eclipse or IntelliJ.
 */
public class Main {
    public static void main(String[] args) {
        // Launch the Hospital Management GUI
        java.awt.EventQueue.invokeLater(() -> {
            try {
                // Set a modern look and feel if available
                for (javax.swing.UIManager.LookAndFeelInfo info : 
                     javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception e) {
                // If Nimbus isn't available, use system look and feel
                try {
                    javax.swing.UIManager.setLookAndFeel(
                        javax.swing.UIManager.getSystemLookAndFeelClassName());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            
            // Create and display the main GUI window
            new HospitalManagementGUI().setVisible(true);
        });
    }
}
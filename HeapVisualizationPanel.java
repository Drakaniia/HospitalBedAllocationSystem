import javax.swing.*;
import java.awt.*;
import java.util.List;

public class HeapVisualizationPanel extends JPanel {
    private HospitalBedAllocator allocator;
    private JTextArea heapDisplayArea;
    private JButton refreshButton;
    private HeapVisualizationCanvas canvas;

    public HeapVisualizationPanel(HospitalBedAllocator allocator) {
        this.allocator = allocator;
        initializeComponents();
        layoutComponents();
        setupEventHandlers();
        refreshVisualization();
    }

    private void initializeComponents() {
        heapDisplayArea = new JTextArea(15, 50);
        heapDisplayArea.setEditable(false);
        heapDisplayArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        
        refreshButton = new JButton("Refresh Visualization");
        
        canvas = new HeapVisualizationCanvas(allocator);
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        // Top control panel
        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.add(refreshButton);
        
        // Create split pane for text view and graphical view
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        
        // Top: Text representation
        JScrollPane textScrollPane = new JScrollPane(heapDisplayArea);
        textScrollPane.setBorder(BorderFactory.createTitledBorder("Heap Text Representation"));
        
        // Bottom: Graphical representation
        JScrollPane canvasScrollPane = new JScrollPane(canvas);
        canvasScrollPane.setBorder(BorderFactory.createTitledBorder("Heap Graphical Visualization"));
        
        splitPane.setTopComponent(textScrollPane);
        splitPane.setBottomComponent(canvasScrollPane);
        splitPane.setDividerLocation(200);
        
        add(controlPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
    }

    private void setupEventHandlers() {
        refreshButton.addActionListener(e -> refreshVisualization());
    }

    public void refreshVisualization() {
        // Update text representation
        updateTextVisualization();
        
        // Update graphical representation
        canvas.repaint();
    }

    private void updateTextVisualization() {
        heapDisplayArea.setText("");
        
        // Get the heap from the allocator
        MinHeap heap = getHeapFromAllocator();
        
        if (heap.isEmpty()) {
            heapDisplayArea.append("The patient priority queue is currently empty.\n");
            return;
        }
        
        heapDisplayArea.append("HEAP STRUCTURE (Level by Level):\n");
        heapDisplayArea.append("================================\n\n");
        
        // Show heap level by level (similar to the original display method)
        int size = heap.size();
        if (size == 0) {
            heapDisplayArea.append("Heap is empty\n");
            return;
        }

        int level = 0;
        int start = 0;
        int count = 1;

        while (start < size) {
            heapDisplayArea.append("Level " + level + ": ");
            int end = Math.min(start + count, size);

            for (int i = start; i < end; i++) {
                Patient patient = heap.getPatientAt(i);  // Need to add this method to MinHeap
                if (patient != null) {
                    heapDisplayArea.append(patient.toString() + " | ");
                }
            }
            heapDisplayArea.append("\n");

            start += count;
            count *= 2;
            level++;
        }
        
        heapDisplayArea.append("\n");
        heapDisplayArea.append("HEAP STATISTICS:\n");
        heapDisplayArea.append("Total Patients in Queue: " + size + "\n");
        if (!heap.isEmpty()) {
            Patient minPatient = heap.getMin();
            if (minPatient != null) {
                heapDisplayArea.append("Highest Priority Patient: P" + minPatient.getPatientId() + 
                                     " (" + minPatient.getName() + ") - Criticality: " + minPatient.getCriticality() + "\n");
            }
        }
    }

    // Helper method to access the heap from allocator (need to add getter to HospitalBedAllocator)
    private MinHeap getHeapFromAllocator() {
        return allocator.getPatientHeap();
    }
}

// Separate class for graphical visualization
class HeapVisualizationCanvas extends JPanel {
    private HospitalBedAllocator allocator;
    private MinHeap heap;
    private List<Patient> heapList;
    
    public HeapVisualizationCanvas(HospitalBedAllocator allocator) {
        this.allocator = allocator;
        setPreferredSize(new Dimension(800, 400));
        setBackground(Color.WHITE);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Get current heap data
        heap = allocator.getPatientHeap();
        heapList = heap.getHeapList();
        
        if (heapList.isEmpty()) {
            g2d.drawString("The patient priority queue is currently empty.", 10, 20);
            return;
        }
        
        drawHeapTree(g2d);
    }
    
    private void drawHeapTree(Graphics2D g2d) {
        int nodeRadius = 25;
        int horizontalSpacing = 80;
        int verticalSpacing = 70;
        
        if (heapList.isEmpty()) return;
        
        // Calculate positions and draw the tree structure
        drawNodeLevel(g2d, 0, getWidth() / 2, 50, nodeRadius, 0, horizontalSpacing, verticalSpacing);
    }
    
    private void drawNodeLevel(Graphics2D g2d, int index, int x, int y, int radius,
                              int level, int hSpacing, int vSpacing) {
        if (index >= heapList.size()) return;
        
        Patient patient = heapList.get(index);
        String nodeLabel = "P" + patient.getPatientId() + "\n" + patient.getCriticality();
        
        // Draw the node
        drawNode(g2d, x, y, radius, nodeLabel, patient.getCriticality());
        
        // Calculate positions for children
        int leftChildIndex = 2 * index + 1;
        int rightChildIndex = 2 * index + 2;
        
        int levelWidth = (int) Math.pow(2, level + 1);
        int newHSpacing = Math.max(30, hSpacing / 2);
        
        if (leftChildIndex < heapList.size()) {
            int leftX = x - (hSpacing / 2);
            int leftY = y + vSpacing;
            // Draw line to left child
            g2d.drawLine(x, y + radius, leftX, leftY - radius);
            // Draw left child
            drawNodeLevel(g2d, leftChildIndex, leftX, leftY, radius, level + 1, newHSpacing, vSpacing);
        }
        
        if (rightChildIndex < heapList.size()) {
            int rightX = x + (hSpacing / 2);
            int rightY = y + vSpacing;
            // Draw line to right child
            g2d.drawLine(x, y + radius, rightX, rightY - radius);
            // Draw right child
            drawNodeLevel(g2d, rightChildIndex, rightX, rightY, radius, level + 1, newHSpacing, vSpacing);
        }
    }
    
    private void drawNode(Graphics2D g2d, int x, int y, int radius, String label, int criticality) {
        // Determine color based on criticality
        Color nodeColor;
        switch (criticality) {
            case 1: // Most critical
                nodeColor = new Color(220, 20, 60); // Red
                break;
            case 2: // Medium criticality
                nodeColor = new Color(255, 165, 0); // Orange
                break;
            case 3: // Low criticality
                nodeColor = new Color(60, 179, 113); // Green
                break;
            default:
                nodeColor = Color.LIGHT_GRAY;
        }
        
        // Draw the node
        g2d.setColor(nodeColor);
        g2d.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
        g2d.setColor(Color.BLACK);
        g2d.drawOval(x - radius, y - radius, 2 * radius, 2 * radius);
        
        // Draw the label
        g2d.setColor(Color.WHITE);
        FontMetrics fm = g2d.getFontMetrics();
        String[] lines = label.split("\n");
        
        int height = fm.getHeight();
        int textY = y - (lines.length - 1) * height / 2;
        
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            int textX = x - fm.stringWidth(line) / 2;
            g2d.drawString(line, textX, textY + i * height);
        }
    }
}
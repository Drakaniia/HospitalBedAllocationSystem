import java.util.ArrayList;
import java.util.List;

public class MinHeap {
    private List<Patient> heap;

    public MinHeap() {
        this.heap = new ArrayList<>();
    }

    // Get the size of the heap
    public int size() {
        return heap.size();
    }

    // Check if heap is empty
    public boolean isEmpty() {
        return heap.size() == 0;
    }

    // Get parent index
    private int parent(int i) {
        return (i - 1) / 2;
    }

    // Get left child index
    private int leftChild(int i) {
        return 2 * i + 1;
    }

    // Get right child index
    private int rightChild(int i) {
        return 2 * i + 2;
    }

    // Swap elements at indices i and j
    private void swap(int i, int j) {
        Patient temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    // Insert a new patient into the heap
    public void insert(Patient patient) {
        heap.add(patient);
        heapifyUp(heap.size() - 1);
    }

    // Maintain heap property by moving element up
    private void heapifyUp(int index) {
        while (index != 0 && 
               heap.get(parent(index)).compareTo(heap.get(index)) > 0) {
            swap(index, parent(index));
            index = parent(index);
        }
    }

    // Get the minimum element (root)
    public Patient getMin() {
        if (size() == 0) {
            return null;
        }
        return heap.get(0);
    }

    // Extract the minimum element (root) from the heap
    public Patient extractMin() {
        if (size() == 0) {
            return null;
        }

        Patient root = heap.get(0);
        Patient lastElement = heap.remove(size() - 1);

        if (size() > 0) {
            heap.set(0, lastElement);
            heapifyDown(0);
        }

        return root;
    }

    // Maintain heap property by moving element down
    private void heapifyDown(int index) {
        int smallest = index;
        int left = leftChild(index);
        int right = rightChild(index);

        // Find the smallest among root, left child, and right child
        if (left < size() && 
            heap.get(left).compareTo(heap.get(smallest)) < 0) {
            smallest = left;
        }

        if (right < size() && 
            heap.get(right).compareTo(heap.get(smallest)) < 0) {
            smallest = right;
        }

        // If smallest is not the root, swap and continue heapifying
        if (smallest != index) {
            swap(index, smallest);
            heapifyDown(smallest);
        }
    }
    
    // Find a patient by ID in the heap
    public Patient findPatient(int patientId) {
        for (Patient patient : heap) {
            if (patient.getPatientId() == patientId) {
                return patient;
            }
        }
        return null;
    }
    
    // Remove a specific patient from the heap
    public boolean removePatient(int patientId) {
        for (int i = 0; i < heap.size(); i++) {
            if (heap.get(i).getPatientId() == patientId) {
                // Replace with last element
                Patient lastElement = heap.remove(heap.size() - 1);
                
                if (i < heap.size()) { // If we didn't remove the last element
                    heap.set(i, lastElement);
                    
                    // Restore heap property by either heapifying up or down
                    if (i > 0 && heap.get(parent(i)).compareTo(heap.get(i)) > 0) {
                        heapifyUp(i);
                    } else {
                        heapifyDown(i);
                    }
                }
                return true;
            }
        }
        return false;
    }
    
    // Update a patient's priority by removing and re-inserting
    public void updatePatientPriority(Patient patient) {
        removePatient(patient.getPatientId());
        insert(patient);
    }
    
    // Display the heap level by level
    public void displayLevelByLevel() {
        if (isEmpty()) {
            System.out.println("Heap is empty");
            return;
        }

        System.out.println("Heap structure level by level:");
        int level = 0;
        int start = 0;
        int count = 1;

        while (start < size()) {
            System.out.print("Level " + level + ": ");
            int end = Math.min(start + count, size());

            for (int i = start; i < end; i++) {
                System.out.print(heap.get(i) + " | ");
            }
            System.out.println();

            start += count;
            count *= 2;
            level++;
        }
    }

    // Get a copy of the internal heap list (for display purposes)
    public List<Patient> getHeapList() {
        return new ArrayList<>(heap);
    }

    // Get the minimum element without removing it
    public Patient peek() {
        if (size() == 0) {
            return null;
        }
        return heap.get(0);
    }

    // Get patient at specific index (for visualization)
    public Patient getPatientAt(int index) {
        if (index < 0 || index >= heap.size()) {
            return null;
        }
        return heap.get(index);
    }

    // Getter for the heap (for visualization)
    public List<Patient> getInternalHeap() {
        return heap;
    }
}
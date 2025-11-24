# Hospital Management System - GUI Edition

A comprehensive GUI hospital bed allocation system using Min-Heap for priority scheduling based on patient criticality and arrival time.

## Features

- **Patient Registration**: Register patients with ID, name, and criticality level
- **Bed Management**: Add, view, and manage hospital beds with different types (ICU, Ward, etc.)
- **Priority-Based Assignment**: Automatic bed assignment using Min-Heap based on criticality → arrival time
- **Patient Operations**: Update criticality, search patients by ID/name, and manage waiting lists
- **Discharge Management**: Properly handle patient discharge and bed reassignment
- **Reporting**: Comprehensive reporting with statistics, occupancy rates, and patient lists
- **Data Persistence**: Save and load system data to preserve state between sessions
- **Search & Filter**: Advanced search capabilities for patients and beds
- **Heap Visualization**: Graphical tree representation of the Min-Heap structure
- **Modern GUI**: User-friendly tabbed interface with intuitive navigation

## Prerequisites

- Java 8 or higher
- VSCode with Java extensions (Extension Pack for Java recommended)

## How to Run in VSCode

### Method 1: Using VSCode Java Extension

1. Open VSCode
2. Open the folder containing the hospital management system files
3. VSCode should automatically detect the Java project
4. In the Explorer panel, right-click on `HospitalManagementGUI.java`
5. Select "Run Java" or "Run Code" from the context menu
6. The GUI application will start in a new window

### Method 2: Using Terminal in VSCode

1. Open VSCode and the project folder
2. Open the integrated terminal: `Ctrl + `` (backtick) or `View > Terminal`
3. Compile all Java files:
   ```bash
   javac *.java
   ```
4. Run the GUI system:
   ```bash
   java HospitalManagementGUI
   ```

### Method 3: Using VSCode Run and Debug

1. Open VSCode and the project folder
2. Press `Ctrl + Shift + P` to open the command palette
3. Type "Java: Run Projects" and select it
4. Select `HospitalManagementGUI.java` to run

### Method 4: Using the Run Scripts (Recommended)

The project includes convenient run scripts that compile and run the GUI system in one command:

#### For Linux/Mac/Git Bash on Windows:
1. Open the integrated terminal in VSCode: `Ctrl + `` (backtick) or `View > Terminal`
2. Make the script executable (first time only):
   ```bash
   chmod +x run.sh
   ```
3. Run the GUI system using the script:
   ```bash
   ./run.sh
   ```

#### For Windows Command Prompt:
1. Open the integrated terminal in VSCode: `Ctrl + `` (backtick) or `View > Terminal`
2. Run the batch file:
   ```cmd
   run.bat
   ```

Both scripts will automatically compile all Java files and run the GUI Hospital Management System with appropriate messages and error checking.

## Project Structure

```
HospitalBedAllocationSystem/
├── HospitalManagementGUI.java       # Main GUI entry point with tabbed interface
├── PatientManagementPanel.java      # Patient management GUI panel
├── BedManagementPanel.java          # Bed management GUI panel
├── ReportPanel.java                 # Reporting GUI panel
├── HeapVisualizationPanel.java      # Heap visualization GUI panel
├── HospitalBedAllocator.java        # Core allocation logic
├── MinHeap.java                     # Min-Heap implementation for priority queue
├── Patient.java                     # Patient data model
├── Bed.java                         # Bed data model
├── DataPersistence.java             # File-based data storage
├── run.sh                           # Linux/Mac/Git Bash run script for GUI
├── run.bat                          # Windows batch run script for GUI
├── .gitignore                       # Git ignore file
├── CONTRIBUTING.md                  # Contribution guidelines
├── LICENSE                          # Project license
├── GITHUB_SETUP.md                  # GitHub setup instructions
├── README.md                        # Project documentation
└── README_backup.md                 # Backup of original README
```

## How to Use

1. **Start the System**: Run `HospitalManagementGUI.java` or use one of the run scripts
2. **Use the Tabbed Interface**: Navigate between different system functions using the tabs:
   - **Patients Tab**: Register new patients, update criticality, search patients
   - **Beds Tab**: Add/view beds, release occupied beds, search beds
   - **Reports Tab**: View statistics, occupancy rates, and patient lists
   - **Heap Tab**: Visualize the Min-Heap structure with graphical tree representation
3. **Save/Load Data**: Use the File menu to save or load system data
4. **Refresh Views**: Use the View menu to refresh all panels

## System Workflow

1. **Patient Registration**: Patients are added with a criticality level (1=Most Critical, 2=Medium, 3=Low)
2. **Bed Assignment**: When beds are available, they are automatically assigned to the highest priority patient
3. **Priority Management**: Patient criticality can be updated, changing their position in the queue
4. **Discharge Process**: When patients are discharged, beds become available for next highest priority patient
5. **Data Persistence**: All data can be saved/loaded between sessions

## Key Functionality

- **Min-Heap Priority Queue**: Ensures O(log n) insertion and O(1) access to highest priority patient
- **Automatic Assignment**: Beds are assigned automatically when available
- **Criticality Updates**: Patient priorities can be changed dynamically
- **Search Capabilities**: Find patients by ID, name, or criticality level
- **Reporting**: Detailed statistics and reports on system status

## Data Files

The system creates the following files for persistence:
- `patients.txt` - Active patient records
- `beds.txt` - Bed configuration
- `discharged_patients.txt` - Discharged patient records

## GitHub Setup

To initialize this project on GitHub at https://github.com/Drakaniia/HospitalBedAllocationSystem.git:

1. **Open Command Prompt/Terminal** in the HospitalBedAllocationSystem directory:
   ```bash
   cd C:\Users\Qwenzy\Desktop\syste\HospitalBedAllocationSystem
   ```

2. **Initialize a Git repository**:
   ```bash
   git init
   ```

3. **Add all files to the repository**:
   ```bash
   git add .
   ```

4. **Make the initial commit**:
   ```bash
   git commit -m "Initial commit: Hospital Management System GUI"
   ```

5. **Add the GitHub remote repository**:
   ```bash
   git remote add origin https://github.com/Drakaniia/HospitalBedAllocationSystem.git
   ```

6. **Push the code to GitHub**:
   ```bash
   git branch -M main
   git push -u origin main
   ```

For more detailed instructions, see the GITHUB_SETUP.md file in the project directory.
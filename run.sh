#!/bin/bash

# Hospital Management System GUI Run Script
# This script compiles and runs the GUI Hospital Management System

echo "Hospital Management System - GUI Version"
echo "========================================="

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "Error: Java is not installed or not in PATH"
    exit 1
fi

# Check if javac is installed
if ! command -v javac &> /dev/null; then
    echo "Error: Java compiler (javac) is not installed or not in PATH"
    exit 1
fi

echo "Compiling the GUI Hospital Management System..."
javac *.java

if [ $? -ne 0 ]; then
    echo "Compilation failed!"
    exit 1
fi

echo "Compilation successful!"
echo ""
echo "Starting GUI Hospital Management System..."
echo ""
echo "The GUI application will open in a new window."
echo ""

java HospitalManagementGUI
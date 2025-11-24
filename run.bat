@echo off
REM Hospital Management System GUI Run Script
REM This script compiles and runs the GUI Hospital Management System

echo Hospital Management System - GUI Version
echo =========================================

REM Check if Java is available
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo Error: Java is not installed or not in PATH
    pause
    exit /b 1
)

REM Check if javac is available
javac -version >nul 2>&1
if %errorlevel% neq 0 (
    echo Error: Java compiler (javac) is not installed or not in PATH
    pause
    exit /b 1
)

echo Compiling the GUI Hospital Management System...
REM Compile all Java files with proper package structure
for /r %%f in (*.java) do (
    javac "%%f"
)

if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b 1
)

echo Compilation successful!
echo.
echo Starting GUI Hospital Management System...
echo.
echo The GUI application will open in a new window.
echo.

java view.HospitalManagementGUI
pause
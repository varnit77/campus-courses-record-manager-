Campus Course & Records Manager (CCRM)
A simple Java SE console application for managing campus courses, student records, enrollments, grading, and backups.

Overview

What it is: A small command-line records manager demonstrating common campus administration features (student and course CRUD, enrollments, grade recording, CSV import/export, backups).
Intended use: Educational/demo project — runs locally and stores data in project-relative data/ and backup/ directories.
Features

Student Management: add, list, search, update, deactivate students.
Course Management: add, list, search, filter by department/instructor/semester.
Enrollment & Grading: enroll/unenroll students and record marks; GPA calculation and reporting.
Import/Export: import/export students and courses via CSV (default files in test-data/).
Backup & Utilities: create backups of data and list/calculate backup sizes.
Reports & Analytics: simple reports (active counts, top students, course statistics).
Technologies / Tools Used

Language: Java (Java SE, 8+ compatible).
Build tools: javac, jar (no external build system required).
Runtime: java (JVM).
Steps to Install & Run (Windows PowerShell)

Prerequisites: install a JDK (Java 8 or later) and ensure javac and java are on your PATH.
Check with: javac -version and java -version
Open PowerShell and change to the project root (the folder that contains src and test-data):
cd "C:\Users\DELL\Downloads\campus-course-records-manager-main (1)\campus-course-records-manager-main"
Compile all Java sources into an out directory (preserves package structure):
$files = Get-ChildItem -Path .\src -Recurse -Filter *.java | ForEach-Object { $_.FullName }
javac -d out $files
Run the application (enable assertions recommended):
java -ea -cp out edu.ccrm.Main
Run from the project root so relative paths like test-data, data, and backup resolve correctly.
Optional: create a runnable JAR:
'Main-Class: edu.ccrm.Main' | Out-File -Encoding ascii manifest.txt
jar cfm ccrm.jar manifest.txt -C out .
java -ea -jar ccrm.jar
Instructions for Testing / Using the App

When the app starts, follow the interactive menu. Default data paths and CSV files:
Default student CSV: test-data/students.csv
Default course CSV: test-data/courses.csv
To import the provided test data: choose 4. Import/Export Data → 1 (students) or 2 (courses), then press Enter to accept the default path.
To verify functionality quickly:
Add a student and a course, enroll the student, record marks, and view the student's profile/GPA.
Create a backup from the Backup menu and list backup files.
Troubleshooting

"Assertions are disabled": re-run with -ea.
"Could not find or load main class": ensure you used -cp out and compiled sources into out\\edu\\ccrm\\Main.class.
Compilation errors: confirm package declarations match src subfolders and paste errors here for help.

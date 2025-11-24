@echo off
echo Building Campus Course & Records Manager...
echo.

echo Cleaning bin directory...
if exist bin rmdir /s /q bin
mkdir bin

echo Compiling source code...
javac -d bin src/edu/ccrm/util/*.java src/edu/ccrm/domain/*.java src/edu/ccrm/config/*.java src/edu/ccrm/service/*.java src/edu/ccrm/io/*.java src/edu/ccrm/cli/*.java src/edu/ccrm/Main.java

if %errorlevel% equ 0 (
    echo Compilation successful!
    echo.
    echo To run the application:
    echo java -ea -cp bin edu.ccrm.Main
) else (
    echo Compilation failed!
)

pause

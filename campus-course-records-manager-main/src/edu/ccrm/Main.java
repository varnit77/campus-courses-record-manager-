package edu.ccrm;

import edu.ccrm.cli.Menu;
import edu.ccrm.config.AppConfig;

public class Main {
    public static void main(String[] args) {
        // Enable assertions
        boolean assertionsEnabled = false;
        assert assertionsEnabled = true;
        
        if (assertionsEnabled) {
            System.out.println("Assertions are enabled");
        } else {
            System.out.println("Warning: Assertions are disabled. Enable with -ea VM option");
        }
        
        // Initialize configuration
        AppConfig config = AppConfig.getInstance();
        config.ensureDirectoriesExist();
        
        System.out.println("=== Campus Course & Records Manager ===");
        System.out.println("Application: " + config.getProperty("app.name"));
        System.out.println("Version: " + config.getProperty("app.version"));
        
        // Start the menu system
        Menu menu = new Menu();
        menu.run();
    }
}
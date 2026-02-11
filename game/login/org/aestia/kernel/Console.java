package org.aestia.kernel;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class Console extends Thread {
    private Scanner scanner;
    
    public Console() {
        this.scanner = new Scanner(System.in);
    }
    
    public void initialize() {
        super.setDaemon(true);
        super.start();
    }
    
    @Override
    public void run() {
        while (Main.config.isRunning()) {
            try {
                System.out.println("Console > \n");
                this.parse(this.scanner.nextLine());
            }
            catch (NoSuchElementException ex) {}
        }
        super.interrupt();
    }
    
    public void parse(final String line) {
        final String[] infos = line.split("\\ ");
        switch (infos[0].toUpperCase()) {
            case "UPTIME": {
            	System.out.println(EmulatorInfos.uptime());
                break;
            }
            default:
                break;
        }
    }
}

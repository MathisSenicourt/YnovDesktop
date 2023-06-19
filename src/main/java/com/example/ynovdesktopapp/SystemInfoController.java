package com.example.ynovdesktopapp;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SystemInfoController extends Stage {

    @FXML
    private TextField hostNameField;

    @FXML
    private TextField ipAddressField;

    @FXML
    private Label operatingSystemLabel;

    @FXML
    private Text javaVersionText;

    @FXML
    private TextField userNameField;

    @FXML
    private TextField userDirectoryField;

    @FXML
    private Label processorArchitectureLabel;

    @FXML
    private Label processorCountLabel;

    @FXML
    private Label totalMemoryLabel;

    public void init() {
        retrieveSystemInfo();
    }

    private void retrieveSystemInfo() {
        try {
            // Récupérer le nom d'hôte
            String hostName = InetAddress.getLocalHost().getHostName();
            hostNameField.setText(hostName);

            // Récupérer l'adresse IP
            String ipAddress = InetAddress.getLocalHost().getHostAddress();
            ipAddressField.setText(ipAddress);

            // Récupérer le système d'exploitation
            String operatingSystem = System.getProperty("os.name");
            operatingSystemLabel.setText(operatingSystem);

            // Récupérer la version de Java
            String javaVersion = System.getProperty("java.version");
            javaVersionText.setText(javaVersion);

            // Récupérer le nom de l'utilisateur
            String userName = System.getProperty("user.name");
            userNameField.setText(userName);

            // Récupérer le répertoire de l'utilisateur
            String userDirectory = System.getProperty("user.home");
            userDirectoryField.setText(userDirectory);

            // Récupérer l'architecture du processeur
            String processorArchitecture = System.getProperty("os.arch");
            processorArchitectureLabel.setText(processorArchitecture);

            // Récupérer le nombre de processeurs
            int processorCount = Runtime.getRuntime().availableProcessors();
            processorCountLabel.setText(String.valueOf(processorCount));

            // Récupérer la mémoire totale (RAM)
            long totalMemory = Runtime.getRuntime().totalMemory();
            String totalMemoryFormatted = formatMemorySize(totalMemory);
            totalMemoryLabel.setText(totalMemoryFormatted);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    private String formatMemorySize(long bytes) {
        long kilobytes = bytes / 1024;
        long megabytes = kilobytes / 1024;
        long gigabytes = megabytes / 1024;

        if (gigabytes > 0) {
            return String.format("%d GB", gigabytes);
        } else if (megabytes > 0) {
            return String.format("%d MB", megabytes);
        } else {
            return String.format("%d KB", kilobytes);
        }
    }

}

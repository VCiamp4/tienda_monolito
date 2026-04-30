package com.tienda.monolito.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.credentials-path}")
    private String credentialsPath;

    @Value("${firebase.project-id}")
    private String projectId;

    @PostConstruct
    public void initFirebase() throws IOException {
        if (!FirebaseApp.getApps().isEmpty()) {
            return;
        }

        try {
            InputStream credentialsStream = getCredentialsStream();
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(credentialsStream))
                    .setProjectId(projectId)
                    .build();

            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            System.err.println("Firebase credentials not found or invalid. Firebase authentication will not be available. Error: " + e.getMessage());
            // Don't rethrow - allow the application to start without Firebase
        }
    }

    // Intenta cargar desde el classpath primero, luego desde el filesystem
    private InputStream getCredentialsStream() throws IOException {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(credentialsPath);
        if (stream != null) {
            return stream;
        }
        // Try filesystem
        java.io.File file = new java.io.File(credentialsPath);
        if (!file.exists()) {
            throw new IOException("Firebase credentials file not found: " + credentialsPath);
        }
        return new FileInputStream(credentialsPath);
    }
}

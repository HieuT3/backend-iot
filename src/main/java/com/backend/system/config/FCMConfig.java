package com.backend.system.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;

@Configuration
public class FCMConfig {

    @Value("${firebase.credentials.path}")
    private Resource firebaseCredentials;

    @Bean
    public FirebaseMessaging initializeFireBase() throws IOException {
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(firebaseCredentials.getInputStream());

        FirebaseOptions firebaseOptions = FirebaseOptions
                .builder()
                .setCredentials(googleCredentials)
                .build();

        FirebaseApp firebaseApp = FirebaseApp.initializeApp(firebaseOptions);
        return FirebaseMessaging.getInstance(firebaseApp);
    }
}

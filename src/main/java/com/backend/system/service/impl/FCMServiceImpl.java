package com.backend.system.service.impl;

import com.backend.system.entity.NoticeFCM;
import com.backend.system.service.FCMService;
import com.google.api.core.ApiFuture;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FCMServiceImpl implements FCMService {

    FirebaseMessaging firebaseMessaging;

    @Override
    public void sendNotification(NoticeFCM noticeFCM) {
        List<String> registrationTokens = noticeFCM.getRegistrationTokens();
        Notification notification = Notification
                .builder()
                .setTitle(noticeFCM.getSubject())
                .setBody(noticeFCM.getContent())
                .setImage(noticeFCM.getImagePath())
                .build();

        MulticastMessage multicastMessage = MulticastMessage
                .builder()
                .addAllTokens(registrationTokens)
                .setNotification(notification)
                .putAllData(noticeFCM.getData())
                .build();

        ApiFuture<BatchResponse> apiFuture = firebaseMessaging.sendEachForMulticastAsync(multicastMessage);
        apiFuture.addListener(
                () -> {
                    try {
                        BatchResponse batchResponse = apiFuture.get();
                        log.info("Send notification success: {}", batchResponse.getSuccessCount());
                        log.warn("Send notification failure: {}", batchResponse.getFailureCount());
                    } catch (InterruptedException | ExecutionException e) {
                        log.error("Error sending FCM notification: {}", e.getMessage());
                    }
                }, Runnable::run
        );
    }
}

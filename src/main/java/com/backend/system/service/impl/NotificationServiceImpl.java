package com.backend.system.service.impl;

import com.backend.system.entity.Notification;
import com.backend.system.service.NotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationServiceImpl implements NotificationService {

    SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void sendMessage(String destination, Notification notification) {
        simpMessagingTemplate.convertAndSend(destination, notification);
    }
}

package com.backend.system.service;

import com.backend.system.entity.Notification;

public interface NotificationService {
    void sendMessage(String destination, Notification notification);
}

package com.backend.system.service;

import com.backend.system.dto.firebase.Notice;

public interface FCMService {
    void sendNotification(Notice notice);
}

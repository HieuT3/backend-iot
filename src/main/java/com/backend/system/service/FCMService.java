package com.backend.system.service;

import com.backend.system.entity.NoticeFCM;

public interface FCMService {
    void sendNotification(NoticeFCM noticeFCM);
}

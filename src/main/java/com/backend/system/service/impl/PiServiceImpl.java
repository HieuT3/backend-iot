package com.backend.system.service.impl;

import com.backend.system.constant.ModeType;
import com.backend.system.dto.request.PiRequest;
import com.backend.system.dto.response.PiResponse;
import com.backend.system.entity.Notification;
import com.backend.system.entity.Pi;
import com.backend.system.exception.AppException;
import com.backend.system.exception.ErrorCode;
import com.backend.system.mapper.PiMapper;
import com.backend.system.repository.PiRepository;
import com.backend.system.service.NotificationService;
import com.backend.system.service.PiService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PiServiceImpl implements PiService {

    PiRepository piRepository;
    PiMapper piMapper;
    NotificationService notificationService;

    @Override
    public List<PiResponse> getAll() {
        return piRepository.findAll()
                .stream()
                .map(piMapper::convertToPiResponse)
                .toList();
    }

    @Override
    public Pi getPiEntityById(Long piId) {
        return piRepository.findById(piId)
                .orElseThrow(() -> new AppException(ErrorCode.PI_NOT_FOUND));
    }

    @Override
    public PiResponse getPiById(Long piId) {
        return piMapper.convertToPiResponse(getPiEntityById(piId));
    }

    @Override
    public PiResponse addPi(PiRequest piRequest) {
        Pi pi = new Pi();
        pi.setDescription(piRequest.getDescription());
        pi.setMode(piRequest.getMode());
        return piMapper.convertToPiResponse(piRepository.save(pi));
    }

    @Override
    public PiResponse updatePiById(Long piId, PiRequest piRequest) {
        Pi pi = getPiEntityById(piId);
        pi.setDescription(piRequest.getDescription());
        pi.setMode(piRequest.getMode());
        return piMapper.convertToPiResponse(piRepository.save(pi));
    }

    @Override
    public PiResponse updateMode(Long piId, ModeType mode) {
        Pi pi = getPiEntityById(piId);
        pi.setMode(mode);
        pi = piRepository.save(pi);

        sendMessage(pi);

        System.out.println("Pi mode updated: " + pi.getMode());

        return piMapper.convertToPiResponse(pi);
    }

    @Override
    public void deletePiById(Long piId) {
        Pi pi = getPiEntityById(piId);
        piRepository.delete(pi);
    }

    private void sendMessage(Pi pi) {
        Notification<Pi> notification = Notification.<Pi>builder()
                .message("UPDATE_MODE")
                .data(pi)
                .build();

        System.out.println("Sending message: " + notification);

        notificationService.sendMessage("/topic/messages", notification);
    }
}

package com.backend.system.service;

import com.backend.system.constant.ModeType;
import com.backend.system.dto.request.PiRequest;
import com.backend.system.dto.response.PiResponse;
import com.backend.system.entity.Pi;

import java.util.List;

public interface PiService {
    List<PiResponse> getAll();
    Pi getPiEntityById(Long piId);
    PiResponse getPiById(Long piId);
    PiResponse addPi(PiRequest piRequest);
    PiResponse updatePiById(Long piId, PiRequest piRequest);
    PiResponse updateMode(Long piId, ModeType mode);
    void deletePiById(Long piId);
}

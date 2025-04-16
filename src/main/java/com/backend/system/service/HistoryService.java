package com.backend.system.service;

import com.backend.system.dto.request.HistoryRequest;
import com.backend.system.dto.response.HistoryResponse;
import org.springframework.data.domain.Page;
import java.time.LocalDate;

public interface HistoryService {
     Page<HistoryResponse> getAll(int page, int limit, LocalDate start, LocalDate end);
     HistoryResponse getHistoryById(Long historyId);
     HistoryResponse addHistory(HistoryRequest historyRequest);
     HistoryResponse updateHistoryById(Long historyId, HistoryRequest historyRequest);
     void deleteHistoryById(Long historyId);
}

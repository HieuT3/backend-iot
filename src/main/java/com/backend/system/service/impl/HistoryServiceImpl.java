package com.backend.system.service.impl;

import com.backend.system.dto.request.HistoryRequest;
import com.backend.system.dto.response.HistoryResponse;
import com.backend.system.entity.History;
import com.backend.system.entity.People;
import com.backend.system.exception.AppException;
import com.backend.system.exception.ErrorCode;
import com.backend.system.mapper.HistoryMapper;
import com.backend.system.repository.HistoryRepository;
import com.backend.system.service.CloudinaryService;
import com.backend.system.service.HistoryService;
import com.backend.system.service.PeopleService;
import com.cloudinary.utils.ObjectUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Base64;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {

    HistoryRepository historyRepository;
    HistoryMapper historyMapper;
    PeopleService peopleService;
    CloudinaryService cloudinaryService;

    private Page<History> getAll(Pageable pageable) {
        return historyRepository.findAll(pageable);
    }

    @Override
    public Page<HistoryResponse> getAll(int page, int limit, LocalDate start, LocalDate end) {
        Pageable pageable = PageRequest.of(page, limit);
        if (start == null || end == null) return getAll(pageable).map(historyMapper::toHistoryResponse);
        LocalDateTime startDate = start.atStartOfDay();
        LocalDateTime endDate = end.atTime(LocalTime.MAX);
        return historyRepository.findAllByTimestampAfterAndTimestampBefore(startDate, endDate, pageable)
                .map(historyMapper::toHistoryResponse);
    }

    @Override
    public HistoryResponse getHistoryById(Long historyId) {
        return historyMapper.toHistoryResponse(getHistoryEntityById(historyId));
    }

    private BufferedImage base64ToImage(String base64String) throws Exception {
        String cleanBase64 = base64String.replaceFirst("^data:image/[^;]+;base64,", "");
        byte[] imageBytes = Base64.getDecoder().decode(cleanBase64);
        ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
        BufferedImage image = ImageIO.read(bis);
        bis.close();
        if (image == null) {
            throw new IllegalArgumentException("Không thể đọc dữ liệu ảnh từ chuỗi Base64");
        }
        return image;
    }

    public String uploadImage(String base64String) throws Exception {
        BufferedImage image = base64ToImage(base64String);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);
        byte[] imageBytes = baos.toByteArray();

        return cloudinaryService.uploadBytes(imageBytes);
    }

    @Override
    public HistoryResponse addHistory(HistoryRequest historyRequest) throws Exception {
        Optional<People> optional = Optional.empty();
        if (historyRequest.getPeopleId() != null)
            optional = peopleService.getOptionalPeopleById(historyRequest.getPeopleId());
        String imagePath = uploadImage(historyRequest.getImagePath());
        System.out.println(imagePath);
        History history = new History();
        history.setTimestamp(historyRequest.getTimestamp());
        history.setImagePath(imagePath);
        history.setMode(historyRequest.getMode());
        optional.ifPresent(history::setPeople);
        return historyMapper.toHistoryResponse(historyRepository.save(history));
    }

    @Override
    public HistoryResponse updateHistoryById(Long historyId, HistoryRequest historyRequest) {
        History existingHistory = getHistoryEntityById(historyId);
        existingHistory.setTimestamp(historyRequest.getTimestamp());
        existingHistory.setImagePath(historyRequest.getImagePath());
        existingHistory.setMode(historyRequest.getMode());
        return historyMapper.toHistoryResponse(
                historyRepository.save(existingHistory)
        );
    }

    @Override
    public void deleteHistoryById(Long historyId) {
        History existingHistory = getHistoryEntityById(historyId);
        historyRepository.delete(existingHistory);
    }

    @Override
    public Page<HistoryResponse> getHistoriesByPeopleId(Long peopleId, int page, int limit) {
        Pageable pageable = PageRequest.of(page, page);
        People people = peopleService.getPeopleEntityById(peopleId);
        return historyRepository.findAllByPeople(people, pageable)
                .map(historyMapper::toHistoryResponse);

    }

    private History getHistoryEntityById(Long historyId) {
        return historyRepository.findById(historyId)
                .orElseThrow(() -> new AppException(ErrorCode.HISTORY_NOT_FOUND));
    }
}

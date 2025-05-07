package com.backend.system.service.impl;

import com.backend.system.entity.NoticeFCM;
import com.backend.system.dto.request.WarningRequest;
import com.backend.system.dto.response.RegistrationTokenResponse;
import com.backend.system.dto.response.WarningResponse;
import com.backend.system.entity.Warning;
import com.backend.system.exception.AppException;
import com.backend.system.exception.ErrorCode;
import com.backend.system.mapper.WarningMapper;
import com.backend.system.repository.WarningRepository;
import com.backend.system.service.CloudinaryService;
import com.backend.system.service.FCMService;
import com.backend.system.service.UserService;
import com.backend.system.service.WarningService;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class WarningServiceImpl implements WarningService {

    WarningRepository warningRepository;
    FCMService fcmService;
    UserService userService;
    WarningMapper warningMapper;
    CloudinaryService cloudinaryService;

    private Page<WarningResponse> getAll(Pageable pageable) {
        return warningRepository.findAll(pageable)
                .map(warningMapper::covertToWarningResponse);
    }

    @Override
    public Page<WarningResponse> getAll(int page, int limit, LocalDate start, LocalDate end) {
        Pageable pageable = PageRequest.of(page, limit);
        if (start == null || end == null) return getAll(pageable);
        LocalDateTime startDate = start.atStartOfDay();
        LocalDateTime endDate = end.atTime(LocalTime.MAX);
        return warningRepository.findAllByTimestampAfterAndTimestampBefore(startDate, endDate, pageable)
                .map(warningMapper::covertToWarningResponse);
    }

    @Override
    public WarningResponse getWarningById(Long warningId) {
        return warningMapper.covertToWarningResponse(getWarningEntityById(warningId));
    }

    @Override
    public Warning getWarningEntityById(Long warningId) {
        return warningRepository.findById(warningId)
                .orElseThrow(() -> new AppException(ErrorCode.WARNING_NOT_FOUND));
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
    public WarningResponse addWarning(WarningRequest warningRequest) throws Exception {
        String imagePath = uploadImage(warningRequest.getImagePath());
        Warning warning = new Warning();

        warning.setTimestamp(warningRequest.getTimestamp());
        warning.setImagePath(imagePath);
        warning.setInfo(warningRequest.getInfo());
        warning = warningRepository.save(warning);
//        sendNotification(warning);
        return warningMapper.covertToWarningResponse(warning);
    }

    private void sendNotification(Warning warning) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        List<String> tokens = userService.getAllRegistrationTokens()
                .stream()
                .map(RegistrationTokenResponse::getToken)
                .toList();

        Map<String, String> data = Map.of(
                "timestamp", warning.getTimestamp().format(dateTimeFormatter),
                "info", warning.getInfo(),
                "image", warning.getImagePath()
        );
        NoticeFCM noticeFCM = new NoticeFCM();
        noticeFCM.setSubject("");
        noticeFCM.setContent("");
        noticeFCM.setData(data);
        noticeFCM.setImagePath(warning.getImagePath());
        noticeFCM.setRegistrationTokens(tokens);

        fcmService.sendNotification(noticeFCM);
    }

    @Override
    public WarningResponse updateWarningById(Long warningId, WarningRequest warningRequest) {
        Warning existingWaring = getWarningEntityById(warningId);
        existingWaring.setTimestamp(warningRequest.getTimestamp());
        existingWaring.setImagePath(warningRequest.getImagePath());
        existingWaring.setInfo(warningRequest.getInfo());
        return warningMapper.covertToWarningResponse(warningRepository.save(existingWaring));
    }


    @Override
    public void deleteWarningById(Long warningId) {
        Warning existingWarning = getWarningEntityById(warningId);
        warningRepository.delete(existingWarning);
    }
}

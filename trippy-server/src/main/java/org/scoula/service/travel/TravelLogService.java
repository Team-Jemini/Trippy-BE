package org.scoula.service.travel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.scoula.controller.travel.log.dto.req.TravelLogCreateDTO;
import org.scoula.controller.travel.log.dto.res.TravelLogDTO;
import org.scoula.domain.travel.TravelLogVO;
import org.scoula.external.s3.S3Service;
import org.scoula.mapper.travel.TravelLogMapper;
import org.scoula.service.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class TravelLogService {
    private final TravelLogMapper travelLogMapper;
    private final UserService userService;
    private final S3Service s3Service;

//    public List<TravelLogDTO> getTravelLogs(final Long userId) {
//        userService.validateUserExists(userId);
//
//        return travelLogMapper.getAllTravelLogs(userId).stream()
//                .map(TravelLogDTO::from)
//                .toList();
//    }
public List<TravelLogDTO> getTravelLogs(final Long userId) {
    userService.validateUserExists(userId);

    List<Map<String, Object>> logs = travelLogMapper.getAllTravelLogs(userId);

    return logs.stream()
            .map(log -> new TravelLogDTO(
                    ((Number) log.get("travelId")).longValue(),
                    ((Number) log.get("userId")).longValue(),
                    (String) log.get("title"),
                    (LocalDateTime) log.get("travelBeginDate"),
                    (LocalDateTime) log.get("travelEndDate"),
                    (String) log.get("destination"),
                    (Boolean) log.get("isGenerated"),
                    (String) log.get("travelImg"),
                    ((Number) log.get("memberCount")).intValue()
            ))
            .toList();
}

    @Transactional
    public void createTravelLog(final Long userId, final TravelLogCreateDTO dto, final MultipartFile travelImg) {
        System.out.println("시작");

    userService.validateUserExists(userId);

        String uploadedUrl = null;
        if (travelImg != null && !travelImg.isEmpty()) {
            uploadedUrl = s3Service.uploadTravelLogImage(travelImg);
        }

        TravelLogVO travelLog = TravelLogVO.builder()
                .userId(userId)
                .title(dto.title())
                .travelBeginDate(dto.travelBeginDate())
                .travelEndDate(dto.travelEndDate())
                .destination(dto.destination())
                .isGenerated(dto.isGenerated())
                .travelImg(uploadedUrl) // S3 URL 저장
                .build();


        System.out.println("저장 쿼리 실행");
        travelLogMapper.save(travelLog);
        System.out.println("끝");
    }

}

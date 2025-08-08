package org.scoula.service.travel;

import java.util.List;

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

    public List<TravelLogDTO> getTravelLogs(final Long userId) {
        userService.validateUserExists(userId);

        return travelLogMapper.getAllTravelLogs(userId).stream()
                .map(TravelLogDTO::from)
                .toList();
    }

    @Transactional
    public void createTravelLog(final Long userId, final TravelLogCreateDTO dto, final MultipartFile travelImg) {
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
                .memberCount(dto.memberCount())
                .travelImg(uploadedUrl)
                .build();

        travelLogMapper.save(travelLog);
    }}

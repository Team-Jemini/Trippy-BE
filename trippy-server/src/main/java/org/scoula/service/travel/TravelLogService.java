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

	public List<TravelLogDTO> getTravelLogs(final Long userId) {
		userService.validateUserExists(userId);

		List<Map<String, Object>> logs = travelLogMapper.getAllTravelLogs(userId);

		return logs.stream()
			.map(log -> new TravelLogDTO(
				((Number)log.get("travelId")).longValue(),
				((Number)log.get("userId")).longValue(),
				((String)log.get("accountId")),
				(String)log.get("title"),
				(LocalDateTime)log.get("travelBeginDate"),
				(LocalDateTime)log.get("travelEndDate"),
				(String)log.get("destination"),
				(Boolean)log.get("isGenerated"),
				(String)log.get("travelImg"),
				((Number)log.get("memberCount")).longValue()
			))
			.toList();
	}

	@Transactional
	public void createTravelLog(final Long userId, final TravelLogCreateDTO dto, final MultipartFile travelImg) {

		userService.validateUserExists(userId);
		String uploadedUrl = s3Service.uploadTravelLogImage(travelImg);

		TravelLogVO travelLog = TravelLogVO.builder()
			.userId(userId)
			.accountId(dto.accountId())
			.title(dto.title())
			.travelBeginDate(dto.travelBeginDate())
			.travelEndDate(dto.travelEndDate())
			.destination(dto.destination())
			.isGenerated(dto.isGenerated())
			.travelImg(uploadedUrl) // S3 URL 저장
			.build();

		travelLogMapper.save(travelLog);
	}

	/**
	 * 여행 기간이 기존 로그와 겹치지 않으면 true, 겹치면 false 반환 (경계 포함 겹침)
	 */
	public boolean isTravelDateAvailable(final Long userId,
										 final LocalDateTime begin,
										 final LocalDateTime end) {
		userService.validateUserExists(userId);

		if (begin == null || end == null) {
			throw new IllegalArgumentException("begin/end는 필수입니다.");
		}
		if (end.isBefore(begin)) {
			throw new IllegalArgumentException("end는 begin 이후여야 합니다.");
		}

		// 겹치지 않는 조건: (existing_end < begin) OR (existing_begin > end)
		// 따라서 겹치는 것의 count: NOT (existing_end < begin OR existing_begin > end)
		int overlapCount = travelLogMapper.countOverlappingTravelLogs(userId, begin, end);
		return overlapCount == 0;
	}

}

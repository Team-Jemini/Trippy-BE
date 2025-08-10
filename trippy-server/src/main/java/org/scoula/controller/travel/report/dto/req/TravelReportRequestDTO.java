package org.scoula.controller.travel.report.dto.req;

import java.time.LocalDateTime;

public record TravelReportRequestDTO(
        Long travelId,
        Long userId // 선택: 여행 소유자 검증에 사용
) {}
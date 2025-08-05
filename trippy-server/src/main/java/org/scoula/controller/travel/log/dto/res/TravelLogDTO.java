package org.scoula.controller.travel.log.dto.res;


import java.time.LocalDateTime;

public record TravelLogDTO(
        Long travelId,
        Long userId,
        String title,
        LocalDateTime travelBeginDate,
        LocalDateTime travelEndDate,
        String destination,
        Boolean isGenerated,
        String travelImg,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static TravelLogDTO from(org.scoula.domain.travel.TravelLogVO vo) {
        return new TravelLogDTO(
                vo.getTravelId(),
                vo.getUserId(),
                vo.getTitle(),
                vo.getTravelBeginDate(),
                vo.getTravelEndDate(),
                vo.getDestination(),
                vo.getIsGenerated(),
                vo.getTravelImg(),
                vo.getCreatedAt(),
                vo.getUpdatedAt()
        );
    }
}
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
        Integer memberCount
) {}
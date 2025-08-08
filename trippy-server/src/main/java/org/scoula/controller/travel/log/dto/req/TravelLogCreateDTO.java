package org.scoula.controller.travel.log.dto.req;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TravelLogCreateDTO (
        String title,
        LocalDateTime travelBeginDate,
        LocalDateTime travelEndDate,
        String destination,
        Boolean isGenerated,
        Integer memberCount
) {}
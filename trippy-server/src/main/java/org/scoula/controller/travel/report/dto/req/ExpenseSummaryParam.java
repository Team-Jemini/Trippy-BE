package org.scoula.controller.travel.report.dto.req;

import java.time.LocalDateTime;

public record ExpenseSummaryParam(
        String accountId,
        Long userId,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime
) {
}

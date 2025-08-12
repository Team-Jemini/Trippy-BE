package org.scoula.controller.travel.report.dto.req;

public record TravelReportInsertParam(
        String accountId,
        Long userId,
        Long travelId,
        Integer totalExpense,
        Integer totalFood,
        Integer totalActivity,
        Integer totalAcc,
        Integer totalTransport,
        Integer totalShop
) {
}

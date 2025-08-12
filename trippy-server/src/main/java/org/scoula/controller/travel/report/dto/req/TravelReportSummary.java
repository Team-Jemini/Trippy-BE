package org.scoula.controller.travel.report.dto.req;

public record TravelReportSummary(
        long totalExpense,
        long totalFood,
        long totalActivity,
        long totalAcc,
        long totalTransport,
        long totalShop
) {
}

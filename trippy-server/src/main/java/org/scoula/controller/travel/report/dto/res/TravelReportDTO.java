package org.scoula.controller.travel.report.dto.res;

import org.scoula.domain.travel.TravelReportVO;

public record TravelReportDTO(
        Long travelId,
        String destination,
        String travelBeginDate,
        String travelEndDate,
        Integer totalExpense,
        Integer totalFood,
        Integer totalActivity,
        Integer totalAcc,
        Integer totalTransport,
        Integer totalShop,
        String maxSpendingTitle,
        Long maxSpendingAmount
) {
    public static TravelReportDTO from(TravelReportVO vo) {
        return new TravelReportDTO(
                vo.getTravelId(),
                vo.getDestination(),
                vo.getTravelBeginDate(),
                vo.getTravelEndDate(),
                vo.getTotalExpense(),
                vo.getTotalFood(),
                vo.getTotalActivity(),
                vo.getTotalAcc(),
                vo.getTotalTransport(),
                vo.getTotalShop(),
                vo.getMaxSpendingTitle(),
                vo.getMaxSpendingAmount()
        );
    }
}
package org.scoula.service.travel;

import lombok.RequiredArgsConstructor;
import org.scoula.controller.travel.log.dto.res.TravelLogDTO;
import org.scoula.controller.travel.report.dto.req.ExpenseSummaryParam;
import org.scoula.controller.travel.report.dto.req.TravelReportInsertParam;
import org.scoula.controller.travel.report.dto.req.TravelReportRequestDTO;
import org.scoula.controller.travel.report.dto.req.TravelReportSummary;
import org.scoula.controller.travel.report.dto.res.TravelReportDTO;
import org.scoula.domain.travel.TravelLogVO;
import org.scoula.mapper.travel.TravelReportMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class TravelReportService {
    private final TravelReportMapper travelReportMapper;

    public TravelReportDTO getTravelReport(final Long travelId) {
        return TravelReportDTO.from(travelReportMapper.getTravelReport(travelId));
    }

    /**
     * 1) travel_log에서 account_id/user_id/기간 조회
     * 2) 그 기준으로 transaction 집계( withdraw )
     * 3) 총/카테고리별 합계 산출
     * 4) travel_report 저장
     */
    @Transactional
    public void createTravelReport(final TravelReportRequestDTO req) {
        if (req.travelId() == null)
            throw new ApiException(TRAVEL_ID_REQUIRED);

        final TravelLogVO travelLog = travelReportMapper.selectTravelLog(req.travelId());
        if (travelLog == null)
            throw new ApiException(TRAVEL_LOG_NOT_FOUND, req.travelId());

        if (travelLog.getAccountId() == null || travelLog.getAccountId().isBlank())
            throw new ApiException(TRAVEL_LOG_ACCOUNT_ID_EMPTY, req.travelId());

        // 2) 집계 → Map
        Map<String, Object> sum = travelReportMapper.selectExpenseSummary(
                new ExpenseSummaryParam(
                        travelLog.getAccountId(),
                        travelLog.getUserId(),
                        travelLog.getTravelBeginDate(),
                        travelLog.getTravelEndDate()
                )
        );

        long totalExpense   = ((Number) sum.getOrDefault("totalExpense",   0L)).longValue();
        long totalFood      = ((Number) sum.getOrDefault("totalFood",      0L)).longValue();
        long totalActivity  = ((Number) sum.getOrDefault("totalActivity",  0L)).longValue();
        long totalAcc       = ((Number) sum.getOrDefault("totalAcc",       0L)).longValue();
        long totalTransport = ((Number) sum.getOrDefault("totalTransport", 0L)).longValue();
        long totalShop      = ((Number) sum.getOrDefault("totalShop",      0L)).longValue();

        // 3)+4) 저장 (travel_report 컬럼이 INT라면 안전하게 변환)
        var insert = new TravelReportInsertParam(
                travelLog.getAccountId(),
                travelLog.getUserId(),
                travelLog.getTravelId(),
                Math.toIntExact(totalExpense),
                Math.toIntExact(totalFood),
                Math.toIntExact(totalActivity),
                Math.toIntExact(totalAcc),
                Math.toIntExact(totalTransport),
                Math.toIntExact(totalShop)
        );
        int rows = travelReportMapper.insertTravelReport(insert);

        int upd = travelReportMapper.markTravelLogGenerated(travelLog.getTravelId());

    }
}

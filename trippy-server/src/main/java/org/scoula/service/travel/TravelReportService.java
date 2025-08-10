package org.scoula.service.travel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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
        if (req.travelId() == null) throw new IllegalArgumentException("travelId는 필수입니다.");

        final TravelLogVO travelLog = travelReportMapper.selectTravelLog(req.travelId());
        if (travelLog == null) throw new IllegalArgumentException("해당 travelId가 존재하지 않습니다. travelId=" + req.travelId());
        if (travelLog.getAccountId() == null || travelLog.getAccountId().isBlank())
            throw new IllegalStateException("travel_log.account_id가 비어 있습니다. travelId=" + req.travelId());

        log.info("[TR] step1 travelId={}, accountId={}, userId={}, begin={}, end={}",
                travelLog.getTravelId(), travelLog.getAccountId(), travelLog.getUserId(),
                travelLog.getTravelBeginDate(), travelLog.getTravelEndDate());

        // 2) 집계 → Map
        Map<String, Object> sum = travelReportMapper.selectExpenseSummary(
                new ExpenseSummaryParam(
                        travelLog.getAccountId(),
                        travelLog.getUserId(),
                        travelLog.getTravelBeginDate(),
                        travelLog.getTravelEndDate()
                )
        );

        long totalExpense   = n(sum, "totalExpense");
        long totalFood      = n(sum, "totalFood");
        long totalActivity  = n(sum, "totalActivity");
        long totalAcc       = n(sum, "totalAcc");
        long totalTransport = n(sum, "totalTransport");
        long totalShop      = n(sum, "totalShop");

        log.info("[TR] step2 summary=exp:{}, food:{}, act:{}, acc:{}, trans:{}, shop:{}",
                totalExpense, totalFood, totalActivity, totalAcc, totalTransport, totalShop);

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

        log.info("[TR] step3 inserted rows={}, travelId={}, accountId={}, userId={}",
                rows, travelLog.getTravelId(), travelLog.getAccountId(), travelLog.getUserId());
    }

    private static long n(Map<String, Object> m, String k) {
        Object v = (m == null) ? null : m.get(k);
        return (v == null) ? 0L : ((Number) v).longValue();
    }
}

package org.scoula.service.travel;

import lombok.RequiredArgsConstructor;
import org.scoula.controller.travel.log.dto.res.TravelLogDTO;
import org.scoula.controller.travel.report.dto.req.TravelReportRequestDTO;
import org.scoula.controller.travel.report.dto.res.TravelReportDTO;
import org.scoula.domain.travel.TravelLogVO;
import org.scoula.mapper.travel.TravelReportMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TravelReportService {
    private final TravelReportMapper travelReportMapper;

    public TravelReportDTO getTravelReport(final Long travelId) {
        return TravelReportDTO.from(travelReportMapper.getTravelReport(travelId));
    }

    /**
     * 1) travel_log에서 travelId로 account_id, user_id, 기간 조회
     * 2) 그 기준으로 transaction 필터링(Withdraw만)
     * 3) 총/카테고리별 소비 합계 계산
     * 4) travel_report에 저장
     */
//    @Transactional
//    public void createTravelReport(final TravelReportRequestDTO req) {
//        if (req.travelId() == null) throw new IllegalArgumentException("travelId는 필수입니다.");
//
//        // 1) 여행 기본 정보 조회 (account_id, user_id, 기간)
//        TravelLogDTO log = travelReportMapper.selectTravelLogInfo(req.travelId());
//        if (log == null) throw new IllegalArgumentException("해당 travelId가 존재하지 않습니다: " + req.travelId());
//
//        // 2) 기간 내 거래 합계(출금/지출: withdraw) 조회
//        ExpenseSummaryParam param = new ExpenseSummaryParam(
//                log.accountId(), log.userId(), log.travelBeginDate(), log.travelEndDate()
//        );
//        TravelReportSummary sum = travelReportMapper.selectExpenseSummary(param);
//        if (sum == null) sum = new TravelReportSummary(0,0,0,0,0,0);
//
//        // 3) 저장 파라미터 구성 → 4) 저장
//        TravelReportInsertParam insert = new TravelReportInsertParam(
//                log.accountId(),
//                log.userId(),
//                log.travelId(),
//                nz(sum.totalExpense()),
//                nz(sum.totalFood()),
//                nz(sum.totalActivity()),
//                nz(sum.totalAcc()),
//                nz(sum.totalTransport()),
//                nz(sum.totalShop())
//        );
//        travelReportMapper.insertTravelReport(insert);
//    }
//
//    private int nz(Integer v) { return v == null ? 0 : Math.max(0, v); }
}

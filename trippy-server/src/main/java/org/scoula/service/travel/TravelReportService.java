package org.scoula.service.travel;

import lombok.RequiredArgsConstructor;
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
     * 요청 바디의 travelId를 기준으로 travel_log에서 account_id/기간을 조회하고,
     * 해당 account_id + user_id + 기간으로 transaction 집계 후 travel_report에 저장.
     * @return 생성된 settlementId
     */
    @Transactional
    public long createTravelReport(final TravelReportRequestDTO req) {
        if (req.travelId() == null) {
            throw new IllegalArgumentException("travelId는 필수입니다.");
        }

        // 1) travel_log에서 account_id, user_id, 기간 조회
        final TravelLogVO log = travelReportMapper.selectTravelLogInfo(req.travelId());
        if (log == null) {
            throw new IllegalArgumentException("해당 travelId의 여행이 존재하지 않습니다. travelId=" + req.travelId());
        }

        // (선택) userId 검증: 요청자와 여행 소유자가 같은지 확인
        if (req.userId() != null && !req.userId().equals(log.getUserId())) {
            throw new IllegalArgumentException("요청 사용자와 여행 소유자가 일치하지 않습니다.");
        }

        // 2) 기간 내 거래 합계 조회 (withdraw 기준)
        ExpenseSummaryParam param = new ExpenseSummaryParam();
        param.setAccountId(log.getAccountId());
        param.setUserId(log.getUserId());
        param.setStartDateTime(log.getTravelBeginDate());
        param.setEndDateTime(log.getTravelEndDate());

        TravelReportVO summary = travelReportMapper.selectExpenseSummary(param);
        if (summary == null) {
            summary = new TravelReportVO();
            summary.setTotalExpense(0);
            summary.setTotalFood(0);
            summary.setTotalActivity(0);
            summary.setTotalAcc(0);
            summary.setTotalTransport(0);
            summary.setTotalShop(0);
        }

        // 3) 저장
        TravelReportVO toSave = new TravelReportVO();
        toSave.setAccountId(log.getAccountId());
        toSave.setUserId(log.getUserId());
        toSave.setTravelId(req.travelId());
        toSave.setTotalExpense(nz(summary.getTotalExpense()));
        toSave.setTotalFood(nz(summary.getTotalFood()));
        toSave.setTotalActivity(nz(summary.getTotalActivity()));
        toSave.setTotalAcc(nz(summary.getTotalAcc()));
        toSave.setTotalTransport(nz(summary.getTotalTransport()));
        toSave.setTotalShop(nz(summary.getTotalShop()));

        travelReportMapper.insertTravelReport(toSave); // useGeneratedKeys=true 가정
        return toSave.getSettlementId();
    }

    private int nz(Integer v) { return v == null ? 0 : Math.max(0, v); }

}

package org.scoula.mapper.travel;

import org.apache.ibatis.annotations.Mapper;
import org.scoula.domain.travel.TravelLogVO;
import org.scoula.domain.travel.TravelReportVO;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface TravelReportMapper {
    TravelReportVO getTravelReport(Long travelId);
//    // travel_log에서 account_id + 기간 조회
//    TravelLogVO selectTravelLogInfo(@Param("travelId") Long travelId);
//
//    // 기간 내 거래 합계 조회 (withdraw 기준)
//    TravelReportVO selectExpenseSummary(@Param("param") ExpenseSummaryParam param);
//
//    int insertTravelReport(TravelReportVO vo);
}

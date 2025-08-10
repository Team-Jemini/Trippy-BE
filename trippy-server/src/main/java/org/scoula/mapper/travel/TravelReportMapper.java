package org.scoula.mapper.travel;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.scoula.controller.travel.log.dto.res.TravelLogDTO;
import org.scoula.controller.travel.report.dto.req.ExpenseSummaryParam;
import org.scoula.controller.travel.report.dto.req.TravelReportInsertParam;
import org.scoula.controller.travel.report.dto.req.TravelReportSummary;
import org.scoula.domain.travel.TravelLogVO;
import org.scoula.domain.travel.TravelReportVO;

import java.util.List;
import java.util.Map;

@Mapper
public interface TravelReportMapper {
    TravelReportVO getTravelReport(@Param("travelId") Long travelId);

    TravelLogVO selectTravelLog(@Param("travelId") Long travelId);

    Map<String, Object> selectExpenseSummary(@Param("param") ExpenseSummaryParam p);

    int insertTravelReport(@Param("param") TravelReportInsertParam p);
}

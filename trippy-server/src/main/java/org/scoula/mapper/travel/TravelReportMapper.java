package org.scoula.mapper.travel;

import org.apache.ibatis.annotations.Mapper;
import org.scoula.domain.travel.TravelReportVO;

import java.util.List;

@Mapper
public interface TravelReportMapper {
    TravelReportVO getTravelReport(Long travelId);
}

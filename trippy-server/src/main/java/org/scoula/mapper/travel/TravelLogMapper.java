package org.scoula.mapper.travel;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

import org.scoula.domain.travel.TravelLogVO;
import org.springframework.data.repository.query.Param;

@Mapper
public interface TravelLogMapper {
    List<Map<String, Object>> getAllTravelLogs(@Param("userId") Long userId);
    void save(TravelLogVO travelLogVO);
}

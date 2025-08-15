package org.scoula.mapper.travel;

import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.scoula.domain.travel.TravelLogVO;

@Mapper
public interface TravelLogMapper {
    List<Map<String, Object>> getAllTravelLogs(@Param("userId") Long userId);
    int countOverlappingTravelLogs(@Param("userId") Long userId,
                                   @Param("begin") LocalDateTime begin,
                                   @Param("end") LocalDateTime end);
    void save(TravelLogVO travelLogVO);

    TravelLogVO findByTravelId(@Param("travelId") Long travelId);
    Long selectLastInsertId();

    int existsAvailableGroupAccount(Long userId);
}

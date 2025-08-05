package org.scoula.mapper.travel;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import org.scoula.domain.travel.TravelLogVO;

@Mapper
public interface TravelLogMapper {
    List<TravelLogVO> getAllTravelLogs();
}

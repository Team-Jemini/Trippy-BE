package org.scoula.service.travel;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.scoula.domain.travel.TravelLogVO;
import org.scoula.mapper.travel.TravelLogMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TravelLogService {
    private final TravelLogMapper travelLogMapper;

    public List<TravelLogVO> getAllTravelLogs() {
        return travelLogMapper.getAllTravelLogs();
    }
}
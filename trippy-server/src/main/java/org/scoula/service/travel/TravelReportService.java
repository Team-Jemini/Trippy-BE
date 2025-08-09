package org.scoula.service.travel;

import lombok.RequiredArgsConstructor;
import org.scoula.controller.travel.report.dto.res.TravelReportDTO;
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


}

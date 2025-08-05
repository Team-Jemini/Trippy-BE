package org.scoula.service.travel;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.scoula.controller.travel.log.dto.res.TravelLogDTO;
import org.scoula.mapper.travel.TravelLogMapper;
import org.scoula.service.user.UserService;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TravelLogService {
    private final TravelLogMapper travelLogMapper;
    private final UserService userService;

    public List<TravelLogDTO> getTravelLogs(final Long userId) {
        userService.validateUserExists(userId);

        return travelLogMapper.getAllTravelLogs(userId).stream()
                .map(TravelLogDTO::from)
                .toList();
    }


}
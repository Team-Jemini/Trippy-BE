package org.scoula.service.identification;

import lombok.RequiredArgsConstructor;
import org.scoula.controller.identification.dto.PassportDTO;
import org.scoula.mapper.identification.PassportMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PassportService {

    private final PassportMapper passportMapper;

    public PassportDTO getPassport(Long userId){
        return PassportDTO.from(passportMapper.findByUserId(userId));
    }
}

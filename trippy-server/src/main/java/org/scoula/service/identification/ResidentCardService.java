package org.scoula.service.identification;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.controller.identification.dto.ResidentCardDTO;
import org.scoula.domain.identification.IdCardVO;
import org.scoula.mapper.identification.IdCardMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class ResidentCardService {

    private final IdCardMapper ResidentCardMapper;

    @Value("${aws.qr-url}")
    private String qrUrl;

    public ResidentCardDTO getResidentCardInfo(Long userId){

        // TODO: 유저 유효성 검사

        IdCardVO idCardVO = ResidentCardMapper.findById(userId);

        return new ResidentCardDTO(
                idCardVO.getName(),
                idCardVO.getIdCardDate(),
                idCardVO.getIdCardNum(),
                idCardVO.getAddress(),
                idCardVO.getImgUrl(),
                qrUrl
        );
    }
}

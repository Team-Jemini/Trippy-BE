package org.scoula.service.identification;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.controller.identification.dto.req.ResidentCardReq;
import org.scoula.controller.identification.dto.res.ResidentCardDTO;
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
        return ResidentCardDTO.from(idCardVO, qrUrl);
    }

    public int addResidentCardInfo(Long userId, ResidentCardReq req){

        // TODO: 올바른 사용자인지 검사

        return mapper.addResidentCardInfo(
                IdCardVO.builder()
                    .userId(userId)
                .idCardNum(req.identity())
                .idCardDate(req.resIssueDate())
                .name(req.name())
                .address(req.address())
                        .imgUrl(req.imgUrl())
                .build()
        );
    }
}

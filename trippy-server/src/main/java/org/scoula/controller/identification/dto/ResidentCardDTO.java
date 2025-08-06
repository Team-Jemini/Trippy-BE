package org.scoula.controller.identification.dto;

import org.scoula.domain.identification.IdCardVO;

public record ResidentCardDTO(
        String resUserName,
        String resIssueDate,
        String resUserIdentity,
        String address,
        String imgUrl,
        String QrUrl
) {
    public static ResidentCardDTO from(IdCardVO vo, String qrUrl) {
        return new ResidentCardDTO(
                vo.getName(),
                vo.getIdCardDate(),
                vo.getIdCardNum(),
                vo.getAddress(),
                vo.getImgUrl(),
                qrUrl
        );
    }
}

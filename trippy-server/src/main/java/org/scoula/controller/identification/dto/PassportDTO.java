package org.scoula.controller.identification.dto;

import org.scoula.domain.identification.PassportVO;
import org.scoula.domain.user.Gender;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PassportDTO(
        String nameKr,
        String nameEn,
        LocalDate birthDate,
        Gender gender,
        String countryCode,
        LocalDateTime expireDate
) {

    public static PassportDTO from(PassportVO vo){
        return new PassportDTO(
                vo.getNameKr(),
                vo.getNameEn(),
                vo.getBirthDate(),
                vo.getGender(),
                vo.getCountryCode(),
                vo.getExpireDate()
        );
    }
}

package org.scoula.controller.identification.dto.req;

import org.scoula.domain.user.Gender;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PassportReq(
        String passportNumber,
        String nameKr,
        String nameEn,
        LocalDate birthDate,
        Gender gender,
        String countryCode,
        LocalDateTime expireDate
) {

}

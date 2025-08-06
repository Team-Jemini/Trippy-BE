package org.scoula.domain.identification;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.scoula.controller.identification.dto.req.PassportReq;
import org.scoula.domain.BaseTime;
import org.scoula.domain.user.Gender;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class PassportVO extends BaseTime {
	private Long userId;
	private String passportNumber;
	private String nameKr;
	private String nameEn;
	private LocalDate birthDate;
	private Gender gender;
	private String countryCode;
	private LocalDateTime expireDate;

	public static PassportVO from(Long userId, PassportReq req) {
		return new PassportVO(
				userId,
				"a",
				req.nameKr(),
				req.nameEn(),
				req.birthDate(),
				req.gender(),
				req.countryCode(),
				req.expireDate()
		);
	}
}
package org.scoula.domain.user;

import java.time.LocalDate;

import org.scoula.domain.BaseTime;

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
public class UserVO extends BaseTime {
	private Long userId;
	private String name;
	private String password;
	private String phone;
	private LocalDate birth;
	private Gender gender;
	private String email;
}
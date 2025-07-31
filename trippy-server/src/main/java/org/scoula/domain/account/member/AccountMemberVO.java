package org.scoula.domain.account.member;

import java.time.LocalDateTime;

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
public class AccountMemberVO extends BaseTime {
	private String accountId; //모임의 계좌
	private Long userId;
	private Role role;
	private LocalDateTime joinedAt;
	private String mainAccount; //구성원 각각의 대표계좌
}
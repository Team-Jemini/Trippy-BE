package org.scoula.service.invite;

import org.scoula.common.util.JwtTokenUtil;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class InviteService {

	private final JwtTokenUtil jwtTokenUtil;

	// 토큰 발급
	public String createInviteTokenURL(Long userId, String accountId, String accountName) {
		log.info("createInviteTokenURL - userId: {}, accountId: {}, accountName: {}", userId, accountId, accountName);

		return jwtTokenUtil.createInviteToken(userId, accountId, accountName);
	}
}

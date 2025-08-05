package org.scoula.service.invite;

import org.scoula.common.util.JwtTokenUtil;
import org.scoula.controller.invite.dto.response.AcceptInviteResponseDTO;
import org.scoula.controller.invite.dto.response.InviteResponseDTO;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class InviteService {

	private final JwtTokenUtil jwtTokenUtil;
	private final String BASE_URL = "http://localhost:5173/?token=";

	public InviteResponseDTO createInviteTokenURL(Long userId, String accountId, String accountName) {
		return new InviteResponseDTO(BASE_URL + jwtTokenUtil.createInviteToken(userId, accountId, accountName));
	}

	public AcceptInviteResponseDTO parseInviteToken(String token) {
		return jwtTokenUtil.parseInviteToken(token);
	}
}

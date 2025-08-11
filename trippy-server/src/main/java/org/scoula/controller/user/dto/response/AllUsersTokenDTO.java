package org.scoula.controller.user.dto.response;

import org.scoula.domain.user.UserVO;

public record AllUsersTokenDTO(
	Long userId,
	String name,
	String accessToken
) {
	public static AllUsersTokenDTO from(UserVO user, String accessToken) {
		return new AllUsersTokenDTO(
			user.getUserId(),
			user.getName(),
			accessToken
		);
	}
}
